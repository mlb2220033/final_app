package com.example.finalproject.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.booking.ViewDetailsActivity;
import com.example.finalproject.model.BookingHistory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ActiveBookingAdapter extends RecyclerView.Adapter<ActiveBookingAdapter.HolderActiveBooking> {
    private Context context;
    private ArrayList<BookingHistory> activeBookingList;

    public ActiveBookingAdapter(Context context, ArrayList<BookingHistory> activeBookingList) {
        this.context = context;
        this.activeBookingList = activeBookingList;
    }

    @NonNull
    @Override
    public HolderActiveBooking onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_show_active_booking, parent, false);

        return new HolderActiveBooking(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HolderActiveBooking holder, int position) {
        BookingHistory bookingHistory = activeBookingList.get(position);
        String id = bookingHistory.getId();
        String hotelID = bookingHistory.getHotel_id();
        Float Price = bookingHistory.getCost();
        String Status = bookingHistory.getStatus();
        Long timestamp = bookingHistory.getTime_stamp();
        Integer Room = bookingHistory.getRoom_count();
        String RoomType = bookingHistory.getType_room();

        loadHotelInfo(bookingHistory, holder);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateString = sdf.format(new Date(timestamp));

        holder.tvBooking.setText("Hotel ID: " + hotelID);
        holder.tvStatus.setText(Status);
        holder.tvDate.setText(dateString);
        holder.tvType.setText(Room + ", " + RoomType);
        holder.tvCost.setText(String.format("VND %,.0f", Price));
        if (Status.equals("Paid")) {
            holder.tvStatus.setBackgroundResource(R.drawable.shape_paid);
        } else if (Status.equals("Confirmed")) {
            holder.tvStatus.setBackgroundResource(R.drawable.shape_confirmed);
        } else if (Status.equals("Cancelled")) {
            holder.tvStatus.setBackgroundResource(R.drawable.shape_cancelled);
            holder.tvCancel.setEnabled(false);
            holder.tvCancel.setTextColor(R.color.Neutral_500);
        } else if (Status.equals("Completed")) {
            holder.tvStatus.setBackgroundResource(R.drawable.shape_completed);
        }

        holder.tvViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewDetailsActivity.class);
                intent.putExtra("hotel_id", bookingHistory.getHotel_id());
                intent.putExtra("time_stamp", bookingHistory.getTime_stamp());
                context.startActivity(intent);
            }
        });

        holder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm Cancel");
                builder.setMessage("Are you sure you want to cancel this booking?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int currentPosition = holder.getBindingAdapterPosition();
                        if (currentPosition != RecyclerView.NO_POSITION) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("status", "Cancelled");
                            DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getUid()).child("booking-history").child(id);
                            bookingRef.updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                            activeBookingList.get(currentPosition).setStatus("Cancelled");
                                            notifyItemChanged(currentPosition);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ActiveBookingAdapter", "Failed to cancel booking: " + e.getMessage());
                                        }
                                    });
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void loadHotelInfo(BookingHistory bookingHistory, HolderActiveBooking holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Hotels");
        ref.child(bookingHistory.getHotel_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String hotelName = dataSnapshot.child("hotelName").getValue(String.class);
                            holder.tvName.setText(hotelName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ActiveBookingAdapter", "Failed to load hotel info: " + error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return activeBookingList.size();
    }

    static class HolderActiveBooking extends RecyclerView.ViewHolder {
        private TextView tvBooking, tvCost, tvDate, tvStatus, tvType, tvName, tvViewDetail, tvCancel;

        public HolderActiveBooking(@NonNull View itemView) {
            super(itemView);
            tvBooking = itemView.findViewById(R.id.tvBooking);
            tvName = itemView.findViewById(R.id.tvName);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvViewDetail = itemView.findViewById(R.id.tvViewDetail);
            tvCancel = itemView.findViewById(R.id.tvCancel);
        }
    }
}
