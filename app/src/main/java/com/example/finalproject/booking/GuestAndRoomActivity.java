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

public class GuestAndRoomActivity extends AppCompatActivity {
    TextView txtRoom, txtAdults, txtChildren, txtConfirm, txtMessage;
    ImageView imgBack, imgChildrenPlus, imgChildrenMinus, imgAdultsMinus, imgAdultsPlus, imgRoomPlus, imgRoomMinus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_and_room);
        addViews();
        addEvents();

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
                int roomCount = Integer.parseInt(txtRoom.getText().toString());
                int adultsCount = Integer.parseInt(txtAdults.getText().toString());
                int childrenCount = Integer.parseInt(txtChildren.getText().toString());

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