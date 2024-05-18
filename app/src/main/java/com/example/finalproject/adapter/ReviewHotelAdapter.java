package com.example.finalproject.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.Rating;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReviewHotelAdapter extends RecyclerView.Adapter<ReviewHotelAdapter.ReviewHotelViewHolder> {

    private Context context;
    private ArrayList<Rating> ratingArrayList;

    public ReviewHotelAdapter(Context context, ArrayList<Rating> ratingArrayList) {
        this.context = context;
        this.ratingArrayList = ratingArrayList;
    }

    @NonNull
    @Override
    public ReviewHotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rating, parent, false);
        return new ReviewHotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHotelViewHolder holder, int position) {
        Rating rating = ratingArrayList.get(position);
        holder.txtUserName.setText(rating.getUserName());
        holder.txtCommentRating.setText(rating.getComment());
        holder.txtRating.setRating(rating.getStarRating());

        // Convert timestamp to a human-readable date string
        long timeInMillis = rating.getTimeRating();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateString = sdf.format(new Date(timeInMillis));
        holder.txtTimeRating.setText(dateString);
    }


    @Override
    public int getItemCount() {
        return ratingArrayList.size();
    }

    static class ReviewHotelViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtCommentRating, txtTimeRating;
        RatingBar txtRating;

        public ReviewHotelViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtCommentRating = itemView.findViewById(R.id.txtCommentRating);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtTimeRating = itemView.findViewById(R.id.txtTimeRating);
        }
    }
}
