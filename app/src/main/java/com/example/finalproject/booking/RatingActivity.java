package com.example.finalproject.booking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.finalproject.R;

public class RatingActivity extends Dialog {

    private float userRate = 0;

    public RatingActivity(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rating);

        final AppCompatButton btnRateNow = findViewById(R.id.btnRateNow);
        final AppCompatButton btnLater = findViewById(R.id.btnLater);
        final RatingBar ratingBar = findViewById(R.id.RatingBar);
        final ImageView imgRating = findViewById(R.id.imgRating);

        btnRateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                getContext().startActivity(intent);
                dismiss();
            }
        });

        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating <= 1) {
                    imgRating.setImageResource(R.drawable.one_star);
                } else if (rating <= 2) {
                    imgRating.setImageResource(R.drawable.two_star);
                } else if (rating <= 3) {
                    imgRating.setImageResource(R.drawable.three_star);
                } else if (rating <= 4) {
                    imgRating.setImageResource(R.drawable.four_star);
                } else if (rating <= 5) {
                    imgRating.setImageResource(R.drawable.five_star);
                }
                //animate
                animateImage(imgRating);
                //selected rating by user
                userRate = rating;
            }
        });
    }

    private void animateImage(ImageView imgRating) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        imgRating.startAnimation(scaleAnimation);
    }
}
