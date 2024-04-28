package com.example.finalproject.model;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Calendar;

public class MyUtils {

    public static final String USER_TYPE_GOOGLE ="Google";
    public static final String USER_TYPE_EMAIL ="Email";
    public static final String USER_TYPE_PHONE ="Phone";
    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static long timestamp(){
        return System.currentTimeMillis();
    }

    public static String formattedTimestampData(Long timestamp){
        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        String date = DateFormat.format("dd/MM/yyyy",calendar).toString();

        return date;
    }

}

