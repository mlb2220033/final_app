package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.ImageGalleryItem;
import com.example.finalproject.model.RecyclerViewInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageGalleryThumbnailAdapter extends RecyclerView.Adapter<ImageGalleryThumbnailAdapter.ImageGalleryThumbnailViewHolder> {

    private List<ImageGalleryItem> list;
    private final RecyclerViewInterface recyclerViewInterface;

    public ImageGalleryThumbnailAdapter(List<ImageGalleryItem> list, RecyclerViewInterface recyclerViewInterface) {
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ImageGalleryThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageGalleryThumbnailAdapter.ImageGalleryThumbnailViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.image_gallery_item_thumbnail_layout,
                parent,
                false
        ), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageGalleryThumbnailViewHolder holder, int position) {
        holder.setImgGalleryItem(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ImageGalleryThumbnailViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_thumbnail_img_gallery_item;
        private ProgressBar progress_loadImg_thumbnail_item;


        public ImageGalleryThumbnailViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            img_thumbnail_img_gallery_item = itemView.findViewById(R.id.img_thumbnail_img_gallery_item);
            progress_loadImg_thumbnail_item = itemView.findViewById(R.id.progress_loadImg_thumbnail_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });

        }

        void setImgGalleryItem(ImageGalleryItem item) {
            Picasso.get().load(item.getUrlImage()).into(img_thumbnail_img_gallery_item, new Callback() {
                @Override
                public void onSuccess() {
                    progress_loadImg_thumbnail_item.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }
}
