package com.example.finalproject.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.HotelFacilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelFacilitiesItemAdapter extends RecyclerView.Adapter<HotelFacilitiesItemAdapter.ViewHodel> {
    List<HotelFacilities> hotelFacItemList;
    List<String> list = new ArrayList<>();

    public HotelFacilitiesItemAdapter(List<HotelFacilities> hotelFacItemList) {
        this.hotelFacItemList = hotelFacItemList;
    }

    @NonNull
    @Override
    public HotelFacilitiesItemAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.facilities_detail_item, parent, false);
        return new HotelFacilitiesItemAdapter.ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelFacilitiesItemAdapter.ViewHodel holder, int position) {
        HotelFacilities hotelFac = hotelFacItemList.get(position);

        holder.txtFacName.setText(hotelFac.getFacName());
        Picasso.get().load(hotelFac.getFacIcon()).into(holder.imgFacIcon);
        boolean isExpandable = hotelFac.isExpandable();

        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable) {
            holder.imgArrow.setImageResource(R.drawable.ic_arr_down);
        } else {
            holder.imgArrow.setImageResource(R.drawable.ic_arr_up);
        }

        NestedAdapter nestedAdapter = new NestedAdapter(list);

        holder.rvFacItem.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvFacItem.setHasFixedSize(true);
        holder.rvFacItem.setAdapter(nestedAdapter);

        holder.llFacDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotelFac.setExpandable(!hotelFac.isExpandable());
                list = hotelFac.getNestedList();
                Log.d("Facilities_Ad", hotelFac.toString());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return hotelFacItemList.size();
    }


    public class ViewHodel extends RecyclerView.ViewHolder {
        LinearLayout llFacDetail;
        RelativeLayout expandableLayout;
        ImageView imgArrow, imgFacIcon;
        TextView txtFacName;
        RecyclerView rvFacItem;

        public ViewHodel(@NonNull View itemView) {

            super(itemView);

            llFacDetail = itemView.findViewById(R.id.llFacDetail);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            txtFacName = itemView.findViewById(R.id.txtFacName);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            imgFacIcon = itemView.findViewById(R.id.imgFacIcon);
            rvFacItem = itemView.findViewById(R.id.rvFacItem);
        }
    }
}
