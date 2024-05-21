package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHodel> {
    private ArrayList<String> contentList;

    public ContentAdapter(ArrayList<String> contentList) {
        this.contentList = contentList;
    }

    @NonNull
    @Override
    public ContentAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_content_item, parent, false);
        return new ContentAdapter.ViewHodel(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ContentAdapter.ViewHodel holder, int position) {
        String content = contentList.get(position);
        holder.txtContent.setText(content);
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public class ViewHodel extends RecyclerView.ViewHolder {

        TextView txtContent;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
        }
    }
}
