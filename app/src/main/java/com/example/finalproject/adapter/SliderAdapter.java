package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalproject.model.SliderBanner;
import com.example.finalproject.R;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private ArrayList<SliderBanner> sliderBanners;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable= new Runnable() {
        @Override
        public void run() {
            sliderBanners.addAll(sliderBanners);
            notifyDataSetChanged();
        }
    };

    public SliderAdapter(ArrayList<SliderBanner> sliderBanners, ViewPager2 viewPager2){
        this.sliderBanners = sliderBanners;
        this.viewPager2 =viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        context=parent.getContext();
        return new SliderViewHolder(LayoutInflater.from(context).inflate(R.layout.banner,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewHolder holder, int position) {
        holder.setImage(sliderBanners.get(position));
        if(position == sliderBanners.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderBanners.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPic);

        }

        void setImage(SliderBanner sliderBanner) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transform(new CenterCrop());
            Glide.with(context)
                    .load(sliderBanner.getUrl())
                    .apply(requestOptions)
                    .into(imageView);
        }
    }
}

