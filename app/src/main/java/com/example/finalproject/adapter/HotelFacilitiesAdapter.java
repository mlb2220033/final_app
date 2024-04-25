package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.HotelFacilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelFacilitiesAdapter extends RecyclerView.Adapter<HotelFacilitiesAdapter.ViewHodel> {
    ArrayList<HotelFacilities> hotelFacList;
    Context context;

    public HotelFacilitiesAdapter(ArrayList<HotelFacilities> hotelFacList, Context context) {
        this.hotelFacList = hotelFacList;
        this.context = context;
    }

    @NonNull
    @Override
    public HotelFacilitiesAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.facilities_item, parent, false);
        return new HotelFacilitiesAdapter.ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelFacilitiesAdapter.ViewHodel holder, int position) {
        HotelFacilities hotelFac = hotelFacList.get(position);

        holder.txtFacName.setText(hotelFac.getFacName());

        Picasso.get().load(hotelFac.getFacIcon()).into(holder.imgFacIcon);

    }

    @Override
    public int getItemCount() {
        return hotelFacList.size();
    }


    public class ViewHodel extends RecyclerView.ViewHolder {
        ImageView imgFacIcon;
        TextView txtFacName;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);

            txtFacName = itemView.findViewById(R.id.txtFacName);
            imgFacIcon = itemView.findViewById(R.id.imgFacIcon);
        }
    }
}
