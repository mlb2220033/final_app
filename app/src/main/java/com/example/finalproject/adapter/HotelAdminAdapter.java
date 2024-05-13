package com.example.finalproject.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.booking.DashboardAdminActivity;
import com.example.finalproject.booking.ProfileMainActivity;
import com.example.finalproject.databinding.RowHotelAdminBinding;
import com.example.finalproject.model.Hotel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HotelAdminAdapter extends RecyclerView.Adapter<HotelAdminAdapter.HolderHotel> {
    private Context context;
    private ArrayList<Hotel> hotelArrayList;
    private RowHotelAdminBinding binding;

    public HotelAdminAdapter(Context context, ArrayList<Hotel> hotelArrayList) {
        this.context = context;
        this.hotelArrayList = hotelArrayList;
    }

    @NonNull
    @Override
    public HolderHotel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowHotelAdminBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderHotel(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdminAdapter.HolderHotel holder, int position) {
        //get data
        Hotel hotel =hotelArrayList.get(position);
        String id = hotel.getHotelID();
        String hotelName = hotel.getHotelName();
        long timestamp = hotel.getTimestamp();
        Float startRating = hotel.getStarRating();

        //set data
        holder.txtHotelAdmin.setText(hotelName);

        //handle click, delete hotel

        //handle click, delete hotel
        holder.btnDeleteHotelAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the custom layout for the dialog
                View dialogView = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);

                // Create a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();

                // Find views in your custom dialog layout
                AppCompatButton btnConfirmDelete = dialogView.findViewById(R.id.btnConfirmDelete);
                AppCompatButton btnCancelDelete = dialogView.findViewById(R.id.btnCancelDelete);

                // Set click listeners for buttons
                btnConfirmDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call deleteHotel method when confirm delete button is clicked
                        Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                        deleteHotel(hotel, holder);
                        alertDialog.dismiss();

                    }
                });


                btnCancelDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Dismiss the dialog when cancel button is clicked
                        alertDialog.dismiss();
                    }
                });

                // Show the dialog
                alertDialog.show();
            }
        });
    }

    private void deleteHotel(Hotel hotel, HolderHotel holder) {
        String hotelID = hotel.getHotelID();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hotels");
        reference.child(hotelID)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Notify adapter about the data change
                        hotelArrayList.remove(hotel);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Successfully deleted...", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public int getItemCount() {
        return hotelArrayList.size();
    }

    //View holder class to hold Ui views for row_hotel_admin.xml
    class HolderHotel extends RecyclerView.ViewHolder{

        //ui views
        TextView txtHotelAdmin;
        ImageButton btnDeleteHotelAdmin;

        public HolderHotel(@NonNull View itemView) {
            super(itemView);

            // init ui views
            txtHotelAdmin = binding.txtHotelAdmin;
            btnDeleteHotelAdmin = binding.btnDeleteHotelAdmin;

        }
    }

}
