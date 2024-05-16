package com.example.finalproject.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;
import com.example.finalproject.model.DataHolder;

public class GuestAndRoomActivity extends AppCompatActivity {
    TextView txtRoom, txtAdults, txtChildren, txtConfirm, txtMessage;
    ImageView imgBack, imgChildrenPlus, imgChildrenMinus, imgAdultsMinus, imgAdultsPlus, imgRoomPlus, imgRoomMinus;
    int roomCount;
    int adultsCount;
    int childrenCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_and_room);

        addViews();
        restoreData();
        addEvents();

    }

    private void restoreData() {
        if (DataHolder.room_numbers != null && DataHolder.guests != null) {
            txtRoom.setText(String.valueOf(DataHolder.room_numbers));
            txtAdults.setText(String.valueOf(DataHolder.adults));
            txtChildren.setText(String.valueOf(DataHolder.children));
        }
    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomCount = Integer.parseInt(txtRoom.getText().toString());
                adultsCount = Integer.parseInt(txtAdults.getText().toString());
                childrenCount = Integer.parseInt(txtChildren.getText().toString());
                int guestsCount = adultsCount + childrenCount;
                if (roomCount == 0) {
                    showMessage("Invalid room count");
                } else if (adultsCount == 0) {
                    showMessage("Invalid adults count");
                } else if (adultsCount < roomCount) {
                    showMessage("The number of rooms cannot be greater than the number of guests");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("roomCount", roomCount);
                    intent.putExtra("adultsCount", adultsCount);
                    intent.putExtra("childrenCount", childrenCount);

                    DataHolder.adults = adultsCount;
                    DataHolder.children = childrenCount;
                    DataHolder.guests = guestsCount;
                    DataHolder.room_numbers = roomCount;
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }

            private void showMessage(String message) {
                txtMessage.setVisibility(View.VISIBLE);
                txtMessage.setText(message);
            }
        });


        imgChildrenPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementCounter(txtChildren);
            }
        });

        imgChildrenMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementCounter(txtChildren);
            }
        });

        imgAdultsPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementCounter(txtAdults);
            }
        });

        imgAdultsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementCounter(txtAdults);
            }
        });

        imgRoomPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementCounter(txtRoom);
            }
        });

        imgRoomMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementCounter(txtRoom);
            }
        });
    }

    private void incrementCounter(TextView textView) {
        int count = Integer.parseInt(textView.getText().toString());
        count++;
        textView.setText(String.valueOf(count));
    }

    private void decrementCounter(TextView textView) {
        int count = Integer.parseInt(textView.getText().toString());
        if (count > 0) {
            count--;
            textView.setText(String.valueOf(count));
        }
    }

    private void addViews() {
        imgBack = findViewById(R.id.imgBack);
        txtConfirm = findViewById(R.id.txtConfirm);
        txtMessage = findViewById(R.id.txtMessage);

        txtAdults = findViewById(R.id.txtAdults);
        txtChildren = findViewById(R.id.txtChildren);
        txtRoom = findViewById(R.id.txtRoom);

        imgChildrenPlus = findViewById(R.id.imgChildrenPlus);
        imgChildrenMinus = findViewById(R.id.imgChildrenMinus);

        imgAdultsMinus = findViewById(R.id.imgAdultsMinus);
        imgAdultsPlus = findViewById(R.id.imgAdultsPlus);

        imgRoomPlus = findViewById(R.id.imgRoomPlus);
        imgRoomMinus = findViewById(R.id.imgRoomMinus);

    }
}