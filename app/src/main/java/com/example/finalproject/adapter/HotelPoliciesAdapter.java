package com.example.finalproject.adapter;

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
import com.example.finalproject.model.HotelPolicies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelPoliciesAdapter extends RecyclerView.Adapter<HotelPoliciesAdapter.ViewHodel> {

    List<HotelPolicies> hotelPolList;
    List<String> list = new ArrayList<>();

    public HotelPoliciesAdapter(List<HotelPolicies> hotelPolList) {
        this.hotelPolList = hotelPolList;
    }

    @NonNull
    @Override
    public HotelPoliciesAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.policies_item, parent, false);
        return new HotelPoliciesAdapter.ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelPoliciesAdapter.ViewHodel holder, int position) {
        HotelPolicies hotelPol = hotelPolList.get(position);

        holder.txtPolName.setText(hotelPol.getPolName());
        Picasso.get().load(hotelPol.getPolIcon()).into(holder.imgPolIcon);

        holder.rvPolItem.setVisibility(View.VISIBLE);

        NestedAdapter nestedAdapter = new NestedAdapter(hotelPol.getNestedList());

        holder.rvPolItem.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvPolItem.setHasFixedSize(true);
        holder.rvPolItem.setAdapter(nestedAdapter);

        holder.llPol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotelPol.setExpandable(!hotelPol.isExpandable());
                list = hotelPol.getNestedList();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });


    }


    @Override
    public int getItemCount() {
        return hotelPolList.size();
    }

    public class ViewHodel extends RecyclerView.ViewHolder {
        ImageView imgPolIcon;
        TextView txtPolName;

        LinearLayout llPol;
        RecyclerView rvPolItem;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);

            txtPolName = itemView.findViewById(R.id.txtPolName);
            imgPolIcon = itemView.findViewById(R.id.imgPolIcon);
            llPol = itemView.findViewById(R.id.llPol);
            rvPolItem = itemView.findViewById(R.id.rvPolItem);
        }
    }
}
