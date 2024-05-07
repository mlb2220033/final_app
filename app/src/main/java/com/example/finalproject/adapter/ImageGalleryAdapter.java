package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalproject.R;
import com.example.finalproject.model.ImageGalleryItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ImageGalleryViewHolder> {

    private List<ImageGalleryItem> list;

    public ImageGalleryAdapter(List<ImageGalleryItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ImageGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageGalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.image_gallery_item_layout,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageGalleryViewHolder holder, int position) {
        holder.setImgGalleryItem(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ImageGalleryViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgGalleryItem;
        private ProgressBar progress_loadImgGallery_item;

        ImageGalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGalleryItem = itemView.findViewById(R.id.imgGalleryItem);
            progress_loadImgGallery_item = itemView.findViewById(R.id.progress_loadImgGallery_item);
        }

        void setImgGalleryItem(ImageGalleryItem item) {
            Picasso.get().load(item.getUrlImage()).into(imgGalleryItem, new Callback() {
                @Override
                public void onSuccess() {
                    progress_loadImgGallery_item.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }
}
