package com.example.finalproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.BookingHistory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        View view = LayoutInflater.from(context).inflate(R.layout.row_show_active_booking, parent,false);

        return new HolderActiveBooking(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderActiveBooking holder, int position) {
        //getdata
        BookingHistory bookingHistory = activeBookingList.get(position);
        String BookingID = bookingHistory.getHotel_id();
        Float Price = bookingHistory.getCost();
        String Status = bookingHistory.getStatus();
        Long timestamp  =bookingHistory.getTime_stamp();
        Integer Room = bookingHistory.getRoom_count();
        String RoomType = bookingHistory.getType_room();

        //Load Hotel Info
        loadHotelInfo(bookingHistory,holder);

        // Convert timestamp to a human-readable date string
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateString = sdf.format(new Date(timestamp));

        // Set data
        holder.tvBooking.setText("Hotel ID: "+BookingID);
        holder.tvStatus.setText(Status);
        holder.tvDate.setText(dateString);
        holder.tvType.setText(Room + ", " + RoomType);
        holder.tvCost.setText(String.format("VND %,.0f", Price));
        if (Status.equals("Paid")){
            holder.tvStatus.setBackgroundResource(R.drawable.shape_paid);
        } else if (Status.equals("Confirmed")){
            holder.tvStatus.setBackgroundResource(R.drawable.shape_confirmed);
        } else if (Status.equals("Cancelled")){
            holder.tvStatus.setBackgroundResource(R.drawable.shape_cancelled);
        } else if (Status.equals("Completed")){
            holder.tvStatus.setBackgroundResource(R.drawable.shape_completed);
        }




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
                        // Handle error
                    }
                });
    }

    @Override
    public int getItemCount() {
        return activeBookingList.size();
    }

    //view holder
    class HolderActiveBooking extends RecyclerView.ViewHolder{
        //views of layout
        private TextView tvBooking,tvCost,tvDate,tvStatus,tvType,tvName;

        public HolderActiveBooking(@NonNull View itemView){
            super(itemView);

            //init views of layout
            tvBooking= itemView.findViewById(R.id.tvBooking);
            tvName = itemView.findViewById(R.id.tvName);
            tvCost= itemView.findViewById(R.id.tvCost);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate= itemView.findViewById(R.id.tvDate);
            tvStatus= itemView.findViewById(R.id.tvStatus);

        }
    }
}
