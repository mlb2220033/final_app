package com.example.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.booking.PaymentActivity;
import com.example.finalproject.booking.ViewBookingActivity;
import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.Room;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHodel>{
    ArrayList<Room> roomList;
    Context context;

    public RoomAdapter(ArrayList<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.room_list,parent,false);
        return new RoomAdapter.ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.ViewHodel holder, int position) {

        Room room = roomList.get(position);

        holder.txtRoomName.setText(room.getRoomName());
        holder.txtroomArea.setText(room.getRoomArea());
        holder.txtroomBed.setText(room.getRoomBed());
        holder.txtRoomPrice.setText(String.format("%,.2f", room.getRoomPrice()) + " VNĐ");
        holder.txtroomPerson.setText(String.valueOf(room.getRoomPerson()));

        Picasso.get().load(room.getRoomImage()).into(holder.imgRoom);

        holder.btnReserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataHolder.room_numbers != null && DataHolder.guests != null && DataHolder.duration != null) {
                    Context activityContext = v.getContext();
                    Intent intent = new Intent(activityContext, PaymentActivity.class);
                    DataHolder.type_room = room.getRoomName();
                    DataHolder.room_price = room.getRoomPrice();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activityContext.startActivity(intent);
                } else {
                    Toast.makeText(v.getContext(), "Please select room, guests and duration.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }


    public class ViewHodel extends RecyclerView.ViewHolder {
        ImageView imgRoom;
        Button btnReserver;
        TextView txtRoomName, txtroomArea, txtroomBed, txtRoomPrice, txtroomPerson;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);

            txtRoomName = itemView.findViewById(R.id.txtRoomName);
            txtroomArea = itemView.findViewById(R.id.txtroomArea);
            txtroomBed = itemView.findViewById(R.id.txtroomBed);
            txtRoomPrice = itemView.findViewById(R.id.txtRoomPrice);
            txtroomPerson = itemView.findViewById(R.id.txtroomPerson);
            imgRoom = itemView.findViewById(R.id.imgRoom);
            btnReserver = itemView.findViewById(R.id.btnReserver);
        }
    }
}
