package com.example.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.booking.FavoriteListActivity;
import com.example.finalproject.booking.HotelDetailActivity;
import com.example.finalproject.model.Hotel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelFavoriteAdapter extends RecyclerView.Adapter<HotelFavoriteAdapter.ViewHodel> {

    ArrayList<Hotel> hotelList;
    Context context;
    private OnListEmptyListener onListEmptyListener;

    public HotelFavoriteAdapter(ArrayList<Hotel> hotelList, Context context) {
        this.hotelList = hotelList;
        this.context = context;
    }

    @NonNull
    @Override
    public HotelFavoriteAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_hotel_item, parent, false);
        return new HotelFavoriteAdapter.ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelFavoriteAdapter.ViewHodel holder, int position) {
        Hotel hotel = hotelList.get(position);

        holder.txtHotelName.setText(hotel.getHotelName());
        holder.txtHotelAddress.setText(hotel.getHotelAddress());

        Picasso.get().load(hotel.getHotelImage()).into(holder.imgHotel);

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Remove from favorites list", Toast.LENGTH_SHORT).show();
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    hotelList.remove(position);
                    notifyItemRemoved(position);
                    removeFromFavorites(hotel.getHotelID());

                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HotelDetailActivity.class);

                intent.putExtra("hotelID", hotel.getHotelID());

                intent.putExtra("txtHotelName", hotel.getHotelName());
                intent.putExtra("txtHotelAddress", hotel.getHotelAddress());
                intent.putExtra("txtHotelDescription", hotel.getHotelDescription());
                intent.putExtra("txtPricePerNight", hotel.getPricePerNight());
                intent.putExtra("imgHotel", hotel.getHotelImage());
                intent.putExtra("txtStarRating", hotel.getStarRating());
                intent.putExtra("txtHotelPhone", hotel.getHotelPhone());
                intent.putExtra("txtHotelGmail", hotel.getHotelGmail());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    private void removeFromFavorites(String hotelID) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("favorites");
            databaseReference.child(hotelID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        checkListEmpty();
                    }
                }
            });
        }
    }

    public void setOnListEmptyListener(OnListEmptyListener listener) {
        this.onListEmptyListener = listener;
    }

    private void checkListEmpty() {
        if (hotelList.isEmpty() && onListEmptyListener != null) {
            onListEmptyListener.onListEmpty(true);
        } else if (!hotelList.isEmpty() && onListEmptyListener != null) {
            onListEmptyListener.onListEmpty(false);
        }
    }

    public interface OnListEmptyListener {
        void onListEmpty(boolean isEmpty);
    }

    public class ViewHodel extends RecyclerView.ViewHolder {
        ImageView imgHotel, imgLike, imgDisLike;
        TextView txtHotelName, txtHotelAddress;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);

            txtHotelName = itemView.findViewById(R.id.txtHotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);

            imgHotel = itemView.findViewById(R.id.imgHotel);
            imgLike = itemView.findViewById(R.id.imgLike);
//            imgDisLike = itemView.findViewById(R.id.imgDisLike);
        }
    }
}
