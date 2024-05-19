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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalproject.R;
import com.example.finalproject.booking.HotelDetailActivity;
import com.example.finalproject.databinding.HotelListBinding;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.RecomModel;
import com.example.finalproject.databinding.ViewholderRecomBinding;
import com.google.android.gms.common.api.Api;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecomAdapter extends RecyclerView.Adapter<RecomAdapter.Viewholder> {
    ArrayList<Hotel> hotelRecommendList;
    Context context;

    public RecomAdapter(ArrayList<Hotel> hotelRecommendList) {
        this.hotelRecommendList = hotelRecommendList;
    }

    @NonNull
    @Override
    public RecomAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderRecomBinding binding = ViewholderRecomBinding.inflate(LayoutInflater.from(context), parent, false);
        return new RecomAdapter.Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Hotel hotelrec = hotelRecommendList.get(position);

        holder.txtHotelName.setText(hotelrec.getHotelName());
        holder.txtHotelAddress.setText(hotelrec.getHotelAddress());
        holder.txtPricePerNight.setText(String.format("%,.2f", hotelrec.getPricePerNight()) + " VNĐ");
        holder.txtStarRating.setText(String.valueOf(hotelrec.getStarRating()));

        if (hotelrec.getDistance() != 0.00) {
            holder.txtDistance.setText(String.format("%.2f km", hotelrec.getDistance()));
            holder.txtDistance.setVisibility(View.VISIBLE);
        } else {
            holder.txtDistance.setVisibility(View.GONE);
        }

        Picasso.get().load(hotelrec.getHotelImage()).into(holder.imgHotel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển đến HotelDetailActivity
                Intent intent = new Intent(context, HotelDetailActivity.class);

                // Chuyển dữ liệu của khách sạn sang HotelDetailActivity
                intent.putExtra("hotelID", hotelrec.getHotelID());
                intent.putExtra("txtHotelName", hotelrec.getHotelName());
                intent.putExtra("txtHotelAddress", hotelrec.getHotelAddress());
                intent.putExtra("txtHotelDescription", hotelrec.getHotelDescription());
                intent.putExtra("txtPricePerNight", hotelrec.getPricePerNight());
                intent.putExtra("imgHotel", hotelrec.getHotelImage());
                intent.putExtra("txtStarRating", hotelrec.getStarRating());
                intent.putExtra("txtHotelPhone", hotelrec.getHotelPhone());
                intent.putExtra("txtHotelGmail", hotelrec.getHotelGmail());

                // Bắt đầu Activity mới
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotelRecommendList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        TextView txtHotelName, txtHotelAddress, txtPricePerNight, txtStarRating, txtDistance;
        ViewholderRecomBinding binding;

        public Viewholder(ViewholderRecomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            txtHotelName = itemView.findViewById(R.id.txtHotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPricePerNight = itemView.findViewById(R.id.txtPricePerNight);
            txtStarRating = itemView.findViewById(R.id.txtStarRating);
            txtDistance = itemView.findViewById(R.id.txtDistance);

            imgHotel = itemView.findViewById(R.id.imgPic);
        }
    }
}
