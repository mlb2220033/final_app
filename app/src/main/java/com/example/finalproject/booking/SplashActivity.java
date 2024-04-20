package com.example.finalproject.booking;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.finalproject.R;
import com.example.finalproject.adapter.SplashAdapter;

public class SplashActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_SPLASH_DISPLAYED = "splashDisplayed";
    Button btnBack, btnNext, btnSkip;
    SplashAdapter splashAdapter;
    ViewPager mSLideSplash;
    LinearLayout mDotLayout;
    TextView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        addViews();
        addEvents();
        splashDisplayed();
    }

    private void splashDisplayed() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean splashDisplayed = preferences.getBoolean(PREF_SPLASH_DISPLAYED, false);

        if (!splashDisplayed) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_SPLASH_DISPLAYED, true);
            editor.apply();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            return;
        }
    }

    private void addEvents() {
        btnBack.setVisibility(View.GONE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getitem(1) > 0){
                    mSLideSplash.setCurrentItem(getitem(-1),true);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getitem(0) < 3)
                    mSLideSplash.setCurrentItem(getitem(1),true);
                else {
                    Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        mSLideSplash = (ViewPager) findViewById(R.id.slideSplash);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);
        splashAdapter = new SplashAdapter(this);
        mSLideSplash.setAdapter(splashAdapter);
        setUpindicator(0);
        mSLideSplash.addOnPageChangeListener(viewListener);
    }
    private void addViews() {
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
    }

    public void setUpindicator(int position){
        dots = new TextView[4];
        mDotLayout.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.Neutral_500,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.Primary_200,getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }
        @Override
        public void onPageSelected(int position) {
            setUpindicator(position);
            if (position > 0){
                btnBack.setVisibility(View.VISIBLE);
            }else {
                btnBack.setVisibility(View.INVISIBLE);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
    private int getitem(int i){
        return mSLideSplash.getCurrentItem() + i;
    }
}