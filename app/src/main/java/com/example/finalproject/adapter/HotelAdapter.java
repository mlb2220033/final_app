package com.example.finalproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.booking.HotelDetailActivity;
import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.Hotel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHodel> {
    ArrayList<Hotel> hotelList;
    Activity activity;


    public HotelAdapter(ArrayList<Hotel> listHotel, Activity activity) {
        this.hotelList = listHotel;
        this.activity = activity;
    }

    @NonNull
    @Override
    public HotelAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.hotel_list, parent, false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.ViewHodel holder, int position) {

        Hotel hotel = hotelList.get(position);

        holder.txtHotelName.setText(hotel.getHotelName());
        holder.txtHotelAddress.setText(hotel.getHotelAddress());
        holder.txtPricePerNight.setText(String.format("%,.2f", hotel.getPricePerNight()) + " VNĐ");
        holder.txtStarRating.setText(String.valueOf(hotel.getStarRating()));

        if (hotel.getDistance() != 0.00) {
            holder.txtDistance.setText(String.format("%.2f km", hotel.getDistance()));
            holder.txtDistance.setVisibility(View.VISIBLE);
        } else {
            holder.txtDistance.setVisibility(View.GONE);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child(user.getUid()).child("favorites");
            favoritesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean isFavorite = snapshot.hasChild(hotel.getHotelID());

                    holder.imgLike.setVisibility(isFavorite ? View.VISIBLE : View.GONE);
                    holder.imgDisLike.setVisibility(isFavorite ? View.GONE : View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            holder.imgLike.setVisibility(View.GONE);
            holder.imgDisLike.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(hotel.getHotelImage()).into(holder.imgHotel);

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgLike.setVisibility(View.GONE);
                holder.imgDisLike.setVisibility(View.VISIBLE);
                Toast.makeText(v.getContext(), "Remove from favorites list", Toast.LENGTH_SHORT).show();
                removeFromFavorites(hotel.getHotelID());
            }
        });

        holder.imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgDisLike.setVisibility(View.GONE);
                holder.imgLike.setVisibility(View.VISIBLE);
                Toast.makeText(v.getContext(), "Add to favorites list", Toast.LENGTH_SHORT).show();
                addToFavorites(hotel.getHotelID());
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HotelDetailActivity.class);

                intent.putExtra("hotelID", hotel.getHotelID());

                intent.putExtra("txtHotelName", hotel.getHotelName());
                intent.putExtra("txtHotelAddress", hotel.getHotelAddress());
                intent.putExtra("txtHotelDescription", hotel.getHotelDescription());
                intent.putExtra("txtPricePerNight", hotel.getPricePerNight());
                intent.putExtra("imgHotel", hotel.getHotelImage());
                intent.putExtra("txtStarRating", hotel.getStarRating());
                intent.putExtra("txtHotelPhone", hotel.getHotelPhone());
                intent.putExtra("txtHotelGmail", hotel.getHotelGmail());

                activity.startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }


    public class ViewHodel extends RecyclerView.ViewHolder {
        ImageView imgHotel, imgLike, imgDisLike;
        TextView txtHotelName, txtHotelAddress, txtPricePerNight, txtStarRating, txtDistance;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);

            txtHotelName = itemView.findViewById(R.id.txtHotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPricePerNight = itemView.findViewById(R.id.txtPricePerNight);
            txtStarRating = itemView.findViewById(R.id.txtStarRating);
            txtDistance = itemView.findViewById(R.id.txtDistance);

            imgHotel = itemView.findViewById(R.id.imgHotel);
            imgLike = itemView.findViewById(R.id.imgLike);
            imgDisLike = itemView.findViewById(R.id.imgDisLike);
        }
    }

    private void addToFavorites(String hotelID) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("favorites");
            databaseReference.child(hotelID).setValue(true);
        }
    }

    private void removeFromFavorites(String hotelID) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("favorites");
            databaseReference.child(hotelID).removeValue();
        }
    }

}
