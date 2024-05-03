package com.example.finalproject.booking;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.finalproject.R;

public class DialogNoInternet extends Dialog {
    private Context mContext;

    public DialogNoInternet(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_no_internet_layout);

        final AppCompatButton btnRetry = findViewById(R.id.btnRetry);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                exitApplication(); // Gọi phương thức để thoát ứng dụng
            }
        });
    }

    private void exitApplication() {
        if (mContext instanceof Activity) {
            ((Activity) mContext).finishAffinity();
        }
    }
}
