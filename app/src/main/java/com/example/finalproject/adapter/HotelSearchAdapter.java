package com.example.finalproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.booking.HotelDetailActivity;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.Location;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HotelSearchAdapter extends RecyclerView.Adapter<HotelSearchAdapter.ViewHolder> {
    Context context;
    List<Hotel> list;

    public HotelSearchAdapter(Context context, List<Hotel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HotelSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_hotel_item, parent, false);
        return new HotelSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelSearchAdapter.ViewHolder holder, int position) {
        Hotel hotel = list.get(position);

        holder.txtHotelName.setText(list.get(position).getHotelName());
        Picasso.get().load(hotel.getHotelImage()).into(holder.imgHotel);
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
        return list != null ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHotelName;
        ImageView imgHotel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txtHotelName = itemView.findViewById(R.id.txtHotelName);
        }
    }
}
