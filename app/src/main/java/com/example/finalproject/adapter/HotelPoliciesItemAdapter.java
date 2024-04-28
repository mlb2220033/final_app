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

public class HotelPoliciesItemAdapter extends RecyclerView.Adapter<HotelPoliciesItemAdapter.ViewHodel> {
    List<HotelPolicies> hotelPolList;
    List<String> list = new ArrayList<>();

    public HotelPoliciesItemAdapter(List<HotelPolicies> hotelPolList) {
        this.hotelPolList = hotelPolList;
    }

    @NonNull
    @Override
    public HotelPoliciesItemAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pocities_detail_item, parent, false);
        return new HotelPoliciesItemAdapter.ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelPoliciesItemAdapter.ViewHodel holder, int position) {
        HotelPolicies hotelPol = hotelPolList.get(position);

        holder.txtPolName.setText(hotelPol.getPolName());
        Picasso.get().load(hotelPol.getPolIcon()).into(holder.imgPolIcon);
        boolean isExpandable = hotelPol.isExpandable();

        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        if (isExpandable) {
            holder.imgArrow.setImageResource(R.drawable.ic_arr_down);
        } else {
            holder.imgArrow.setImageResource(R.drawable.ic_arr_up);
        }

        NestedAdapter nestedAdapter = new NestedAdapter(list);

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
        ImageView imgPolIcon, imgArrow;
        TextView txtPolName;

        LinearLayout llPol;
        RelativeLayout expandableLayout;
        RecyclerView rvPolItem;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);

            txtPolName = itemView.findViewById(R.id.txtPolName);
            imgPolIcon = itemView.findViewById(R.id.imgPolIcon);
            imgArrow = itemView.findViewById(R.id.imgArrow);
            llPol = itemView.findViewById(R.id.llPol);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            rvPolItem = itemView.findViewById(R.id.rvPolItem);
        }
    }
}
