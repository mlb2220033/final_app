package com.example.finalproject.model;

import android.content.Context;
import android.widget.Toast;

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

}

