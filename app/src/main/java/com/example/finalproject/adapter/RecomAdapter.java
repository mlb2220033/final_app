package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalproject.model.RecomModel;
import com.example.finalproject.databinding.ViewholderRecomBinding;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;

public class RecomAdapter extends RecyclerView.Adapter<RecomAdapter.Viewholder> {
    ArrayList<RecomModel> items;
    Context context;

    public RecomAdapter(ArrayList<RecomModel> items) {
        this.items = items;
    }
    @NonNull
    @Override
    public RecomAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderRecomBinding binding=ViewholderRecomBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecomAdapter.Viewholder holder, int position) {
        holder.binding.txtTitle.setText(items.get(position).getTitle());
        holder.binding.txtType.setText(items.get(position).getType());
        holder.binding.txtAddress.setText(items.get(position).getAddress());
        holder.binding.txtPrice.setText(items.get(position).getPrice()+"$");
        holder.binding.txtRating.setText("("+items.get(position).getRating()+")");
        holder.binding.ratingBar.setRating((float) items.get(position).getRating());

        RequestOptions requestOptions=new RequestOptions();
        requestOptions= requestOptions.transform(new CenterCrop());

        Glide.with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.imgPic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderRecomBinding binding;
        public Viewholder(ViewholderRecomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

