package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.Hotel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class HotelAdapter extends FirebaseRecyclerAdapter<Hotel,HotelAdapter.ViewHodel> {

    public HotelAdapter(@NonNull FirebaseRecyclerOptions<Hotel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HotelAdapter.ViewHodel holder, int position, @NonNull Hotel hotel) {

        holder.txtHotelName.setText(hotel.getHotelName());
        holder.txtHotelAddress.setText(hotel.getHotelAddress());
        holder.txtPricePerNight.setText(String.valueOf(hotel.getPricePerNight()));
        holder.txtStarRating.setText(String.valueOf(hotel.getStarRating()));

        Picasso.get().load(hotel.getHotelImage()).into(holder.imgHotel);

        holder.imgLike.setVisibility(hotel.isLiked() ? View.VISIBLE : View.GONE);
        holder.imgDisLike.setVisibility(hotel.isLiked() ? View.GONE : View.VISIBLE);

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgLike.setVisibility(View.GONE);
                holder.imgDisLike.setVisibility(View.VISIBLE);

//                updateIsLikeInDatabase(song.getSongID(), true);

                Toast.makeText(v.getContext(), "Added to favorites list", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgDisLike.setVisibility(View.GONE);
                holder.imgLike.setVisibility(View.VISIBLE);

//                updateIsLikeInDatabase(song.getSongID(), false);

                Toast.makeText(v.getContext(), "Removed from favorites list", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @NonNull
    @Override
    public HotelAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_list,parent,false);
        return new ViewHodel(view);
    }

    public class ViewHodel extends RecyclerView.ViewHolder {
        ImageView imgHotel, imgLike, imgDisLike;
        TextView txtHotelName, txtHotelAddress, txtPricePerNight,txtStarRating;
        public ViewHodel(@NonNull View itemView) {
            super(itemView);

            txtHotelName = itemView.findViewById(R.id.txtHotelName);
            txtHotelAddress = itemView.findViewById(R.id.txtHotelAddress);
            txtPricePerNight = itemView.findViewById(R.id.txtPricePerNight);
            txtStarRating = itemView.findViewById(R.id.txtStarRating);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            imgLike = itemView.findViewById(R.id.imgLike);
            imgDisLike = itemView.findViewById(R.id.imgDisLike);
        }
    }
}
