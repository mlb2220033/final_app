package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.BookingHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryBookingAdapter extends RecyclerView.Adapter<HistoryBookingAdapter.HolderHistoryBooking> {
    private Context context;
    private ArrayList<BookingHistory> historyList;

    public HistoryBookingAdapter(Context context, ArrayList<BookingHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HolderHistoryBooking onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_history_booking, parent, false);
        return new HolderHistoryBooking(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderHistoryBooking holder, int position) {
        // Get data
        BookingHistory bookingHistory = historyList.get(position);
        String bookingID = bookingHistory.getHotel_id();
        float price = bookingHistory.getCost();
        String status = bookingHistory.getStatus();
        long timestamp = bookingHistory.getTime_stamp();
        int roomCount = bookingHistory.getRoom_count();
        String roomType = bookingHistory.getType_room();

        // Convert timestamp to a human-readable date string
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateString = sdf.format(new Date(timestamp));

        // Set data
        holder.tvBooking.setText("Hotel ID: " + bookingID);
        holder.tvStatus.setText(status);
        holder.tvDate.setText(dateString);
        holder.tvType.setText(roomCount + ", " + roomType);
        holder.tvCost.setText(String.format("VND %,.0f", price));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    // View holder
    class HolderHistoryBooking extends RecyclerView.ViewHolder {
        // Views of layout
        private TextView tvBooking, tvCost, tvDate, tvStatus, tvType;

        public HolderHistoryBooking(@NonNull View itemView) {
            super(itemView);

            // Initialize views of layout
            tvBooking = itemView.findViewById(R.id.tvBooking);
            tvCost = itemView.findViewById(R.id.tvCost);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvType = itemView.findViewById(R.id.tvType);
        }
    }
}
