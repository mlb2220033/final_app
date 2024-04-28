package com.example.finalproject.booking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Payment2Activity extends AppCompatActivity {
    CardView cardvCard, cardvEwallet, cardvBank;
    ImageView imgCardCre, imgEwallet, imgBank;
    ConstraintLayout layoutCardCre, layoutEwallet, layoutBank;
    TextView txtCardCre, txtEwallet, txtBank;

    ImageView btnBack;
    CardView cardView;

    // Biến để lưu trữ CardView được chọn hiện tại
    CardView selectedCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cardvCard = findViewById(R.id.cardvCard);
        cardvEwallet = findViewById(R.id.cardvEwallet);
        cardvBank = findViewById(R.id.cardvBank);

        imgCardCre = findViewById(R.id.imgCardCre);
        imgEwallet = findViewById(R.id.imgEwallet);
        imgBank = findViewById(R.id.imgBank);

        layoutCardCre = findViewById(R.id.layoutCardCre);
        layoutEwallet = findViewById(R.id.layoutEwallet);
        layoutBank = findViewById(R.id.layoutBank);

        txtCardCre = findViewById(R.id.txtCardCre);
        txtEwallet = findViewById(R.id.txtEwallet);
        txtBank = findViewById(R.id.txtBank);

        // Credit/Debit
        cardvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem CardView hiện tại có phải là CardView được chọn không
                if (selectedCardView != cardvCard) {
                    // Thiết lập lại trạng thái của CardView trước đó
                    resetCardView();

                    // Thiết lập trạng thái mới cho CardView được chọn
                    selectedCardView = cardvCard;
                    cardvCard.setCardBackgroundColor(Color.parseColor("#0E21A0"));
                    layoutCardCre.setBackgroundColor(Color.parseColor("#0E21A0"));
                    txtCardCre.setTextColor(Color.parseColor("#FFFFFF"));
                    imgCardCre.setImageResource(R.drawable.ic_card_c);
                }

                /// Show Danh sach the o dayyyyyyyyy
            }
        });

        // Ewallet
        cardvEwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCardView != cardvEwallet) {
                    resetCardView();
                    selectedCardView = cardvEwallet;
                    cardvEwallet.setCardBackgroundColor(Color.parseColor("#0E21A0"));
                    layoutEwallet.setBackgroundColor(Color.parseColor("#0E21A0"));
                    txtEwallet.setTextColor(Color.parseColor("#FFFFFF"));
                    imgEwallet.setImageResource(R.drawable.ic_wallet_c);
                }

                /// Show Danh sach  o dayyyyyyyyy
            }
        });

        // Bank
        cardvBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCardView != cardvBank) {
                    resetCardView();
                    selectedCardView = cardvBank;
                    cardvBank.setCardBackgroundColor(Color.parseColor("#0E21A0"));
                    layoutBank.setBackgroundColor(Color.parseColor("#0E21A0"));
                    txtBank.setTextColor(Color.parseColor("#FFFFFF"));
                    imgBank.setImageResource(R.drawable.ic_bank_c);
                }

                /// Show Danh sach the dayyyyyyyyy
            }
        });

        cardView= findViewById(R.id.cardview);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Payment2Activity.this,R.style.AppBottomSheetDialogTheme);
                View view1 = LayoutInflater.from(Payment2Activity.this).inflate(R.layout.add_new_card,null);
                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();

            }
        });

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment2Activity.this, PaymentActivity.class);
                startActivity(intent);

            }
        });

        Button btnConfirm = findViewById(R.id.btnUpdate);

        // Set click listener for btnConfirm
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuccessDialog();

                /// có fail nữa nhe
            }
        });
    }

    // Phương thức để thiết lập lại trạng thái của các CardView khi có một CardView mới được chọn
    private void resetCardView() {
        if (selectedCardView != null) {
            selectedCardView.setCardBackgroundColor(Color.parseColor("#E4EDFF")); // Màu nền mặc định
            if (selectedCardView == cardvCard) {
                layoutCardCre.setBackgroundResource(R.drawable.oval_pr100_background);// Màu nền mặc định
                txtCardCre.setTextColor(Color.parseColor("#808080")); // Màu chữ mặc định
                imgCardCre.setImageResource(R.drawable.ic_card); // Hình ảnh mặc định
            } else if (selectedCardView == cardvEwallet) {
                layoutEwallet.setBackgroundResource(R.drawable.oval_pr100_background);
                txtEwallet.setTextColor(Color.parseColor("#808080"));
                imgEwallet.setImageResource(R.drawable.ic_wallet);
            } else if (selectedCardView == cardvBank) {
                layoutBank.setBackgroundResource(R.drawable.oval_pr100_background);
                txtBank.setTextColor(Color.parseColor("#808080"));
                imgBank.setImageResource(R.drawable.ic_bank);
            }
        }
    }

    private void showSuccessDialog() {

        ConstraintLayout DialogSuccessPayment= findViewById(R.id.DialogSuccessPayment);
        View view  = LayoutInflater.from(Payment2Activity.this).inflate(R.layout.successful_payment, DialogSuccessPayment);
        Button successDone = view.findViewById(R.id.SuccessDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(Payment2Activity.this);
        builder.setView(view);
        final  AlertDialog alertDialog = builder.create();

        successDone.findViewById(R.id.SuccessDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(Payment2Activity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        if(alertDialog != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        }
        alertDialog.show();
    }
}
