package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class NestedAdapter extends RecyclerView.Adapter<NestedAdapter.NestedViewHodel> {
    private List<String> list;

    public NestedAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NestedViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.facilities_detail_item_name, parent, false);
        return new NestedAdapter.NestedViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHodel holder, int position) {
        holder.txtFacItemName.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }


    public class NestedViewHodel extends RecyclerView.ViewHolder {
        TextView txtFacItemName;

        public NestedViewHodel(@NonNull View itemView) {
            super(itemView);

            txtFacItemName = itemView.findViewById(R.id.txtFacItemName);
        }
    }
}
