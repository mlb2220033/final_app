package com.example.finalproject.adapter;

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
import com.example.finalproject.model.Hotel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHodel> {
    ArrayList<Hotel> hotelList;
    Context context;

    public HotelAdapter(ArrayList<Hotel> listHotet, Context context) {
        this.hotelList = listHotet;
        this.context = context;
    }

    @NonNull
    @Override
    public HotelAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotel_list,parent,false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.ViewHodel holder, int position) {

        Hotel hotel = hotelList.get(position);

        holder.txtHotelName.setText(hotel.getHotelName());
        holder.txtHotelAddress.setText(hotel.getHotelAddress());
        holder.txtPricePerNight.setText(String.valueOf(hotel.getPricePerNight()));
        holder.txtStarRating.setText(String.valueOf(hotel.getStarRating()));

        Picasso.get().load(hotel.getHotelImage()).into(holder.imgHotel);

        holder.imgLike.setVisibility(hotel.isLiked() ? View.VISIBLE : View.GONE);
        holder.imgDisLike.setVisibility(hotel.isLiked() ? View.GONE : View.VISIBLE);

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgLike.setVisibility(View.GONE);
                holder.imgDisLike.setVisibility(View.VISIBLE);
                Toast.makeText(v.getContext(), "Added to favorites list", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgDisLike.setVisibility(View.GONE);
                holder.imgLike.setVisibility(View.VISIBLE);
                Toast.makeText(v.getContext(), "Removed from favorites list", Toast.LENGTH_SHORT).show();
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


    public class ViewHodel extends RecyclerView.ViewHolder {
        ImageView imgHotel, imgLike, imgDisLike;
        TextView txtHotelName, txtHotelAddress, txtPricePerNight,txtStarRating;
        public ViewHodel(@NonNull View itemView) {
            super(itemView);

            txtHotelName = itemView.findViewById(R.id.txtHotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPricePerNight = itemView.findViewById(R.id.txtPricePerNight);
            txtStarRating = itemView.findViewById(R.id.txtStarRating);

            imgHotel = itemView.findViewById(R.id.imgHotel);
            imgLike = itemView.findViewById(R.id.imgLike);
            imgDisLike = itemView.findViewById(R.id.imgDisLike);
        }
    }
}
