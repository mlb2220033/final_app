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
import com.example.finalproject.booking.HotelDetailActivity;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.DataHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    private ArrayList<Hotel> hotelPopularList;
    private Context context;

    public PopularAdapter(ArrayList<Hotel> hotelPopularList) {
        this.hotelPopularList = hotelPopularList;
    }

    @NonNull
    @Override
    public PopularAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_popular, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewholder holder, int position) {
        Hotel hotel = hotelPopularList.get(position);

        holder.txtHotelName.setText(hotel.getHotelName());
        holder.txtHotelAddress.setText(hotel.getHotelAddress());
        holder.txtPricePerNight.setText(String.format("%,.2f VNĐ", hotel.getPricePerNight()));
        holder.txtStarRating.setText(String.valueOf(hotel.getStarRating()));

        if (hotel.getDistance() != 0.00) {
            holder.txtDistance.setText(String.format("%.2f km", hotel.getDistance()));
            holder.txtDistance.setVisibility(View.VISIBLE);
        } else {
            holder.txtDistance.setVisibility(View.GONE);
        }

        Picasso.get().load(hotel.getHotelImage()).into(holder.imgHotel);

        holder.imgLike.setVisibility(hotel.isLiked() ? View.VISIBLE : View.GONE);
        holder.imgDisLike.setVisibility(hotel.isLiked() ? View.GONE : View.VISIBLE);

        holder.imgLike.setOnClickListener(v -> {
            holder.imgLike.setVisibility(View.GONE);
            holder.imgDisLike.setVisibility(View.VISIBLE);
            Toast.makeText(v.getContext(), "Remove from favorites list", Toast.LENGTH_SHORT).show();
        });

        holder.imgDisLike.setOnClickListener(v -> {
            holder.imgDisLike.setVisibility(View.GONE);
            holder.imgLike.setVisibility(View.VISIBLE);
            Toast.makeText(v.getContext(), "Add to favorites list", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnClickListener(v -> {
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
            context.startActivity(intent);
        });

        // Calculate distance
        DatabaseReference hotelMarkersRef = FirebaseDatabase.getInstance().getReference("Hotels")
                .child(hotel.getHotelID()).child("hotelMarkers");
        hotelMarkersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && DataHolder.latitude != null && DataHolder.longitude != null) {
                    Double hotelLatitude = snapshot.child("latitude").getValue(Double.class);
                    Double hotelLongitude = snapshot.child("longitude").getValue(Double.class);
                    if (hotelLatitude != null && hotelLongitude != null) {
                        double distance = calculateDistance(hotelLatitude, hotelLongitude,
                                Double.parseDouble(DataHolder.latitude.trim()),
                                Double.parseDouble(DataHolder.longitude.trim()));
                        hotel.setDistance(distance);
                        holder.txtDistance.setText(String.format("%.2f km", distance));
                        holder.txtDistance.setVisibility(View.VISIBLE);

                        // Notify the adapter about the data change
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event, for example:
                Toast.makeText(context, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelPopularList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imgHotel, imgLike, imgDisLike;
        TextView txtHotelName, txtHotelAddress, txtPricePerNight, txtStarRating, txtDistance;

        public Viewholder(View itemView) {
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

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.60934;  // convert miles to kilometers
        return dist;
    }
}
