package com.example.finalproject.booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.example.finalproject.model.Discount;
import com.example.finalproject.model.Hotel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private FirebaseDatabase db;
    private Hotel hotel;
    private Float subTotal, vat, total, discount = (float) 0;
    private List<Discount> discounts;
    Button btnConfirm, btnApplyDiscount;
    TextView txtSubtotal, txtDiscount, txtVAT, txtTotal;
    EditText edtInputDiscount, edtFullNamePayment, edtPhonePayment, edtAddressPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addView();

        db = FirebaseDatabase.getInstance();
        getListDiscount();
        getDemoHotel();

        btnApplyDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnApplyDiscount.getText().toString().equals("Apply")) {
                    if (edtInputDiscount.getText().toString().isEmpty()) {
                        Toast.makeText(PaymentActivity.this, "discount field cannot be left blank", Toast.LENGTH_LONG).show();
                    } else {
                        btnApplyDiscount.setText("Clear");
                        for (Discount element : discounts) {
                            if (element.getCode().contentEquals(edtInputDiscount.getText())) {
                                Toast.makeText(PaymentActivity.this, "Apply discount code successfully", Toast.LENGTH_LONG).show();
                                discount = element.getCost();
                                totalCalculate(hotel);
                                break;
                            }
                        }
                        if (discount == 0) {
                            Toast.makeText(PaymentActivity.this, "Invalid discount", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    btnApplyDiscount.setText("Apply");
                    edtInputDiscount.setText("");
                    discount = (float) 0;
                    totalCalculate(hotel);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtFullNamePayment.getText().toString().isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Fullname field cannot be left blank", Toast.LENGTH_LONG).show();
                } else if (edtPhonePayment.getText().toString().isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Phone number field cannot be left blank", Toast.LENGTH_LONG).show();
                } else if (edtAddressPayment.getText().toString().isEmpty()) {
                    Toast.makeText(PaymentActivity.this, "Address field cannot be left blank", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(PaymentActivity.this, ViewBookingActivity.class);
                    intent.putExtra("hotel_item", hotel);
                    intent.putExtra("subTotal", subTotal);
                    intent.putExtra("vat", vat);
                    intent.putExtra("total", total);
                    intent.putExtra("discount", discount);
                    intent.putExtra("full_name", edtFullNamePayment.getText().toString());
                    intent.putExtra("phone", edtPhonePayment.getText().toString());
                    intent.putExtra("address", edtAddressPayment.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void addView() {
        btnConfirm=findViewById(R.id.btnUpdate);
        btnApplyDiscount=findViewById(R.id.btnApplyDiscount);
        txtSubtotal=findViewById(R.id.txtSubtotal);
        txtDiscount=findViewById(R.id.txtDiscount);
        txtVAT=findViewById(R.id.txtVAT);
        txtTotal=findViewById(R.id.txtTotal);
        edtInputDiscount=findViewById(R.id.edtInputDiscount);
        edtFullNamePayment=findViewById(R.id.edtFullNamePayment);
        edtAddressPayment=findViewById(R.id.edtAddressPayment);
        edtPhonePayment=findViewById(R.id.edtPhonePayment);

        discounts = new ArrayList<>();
    }

    private void getDemoHotel() {
        ref = db.getReference("Hotels");
        ref.child("hotel1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    hotel = task.getResult().getValue(Hotel.class);
                    if (hotel != null) {
                        totalCalculate(hotel);
                    }
                }
            }
        });
    }

    private void getListDiscount() {
        ref = db.getReference("Discount");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Discount discount = dataSnapshot.getValue(Discount.class);
                    discounts.add(discount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void totalCalculate(Hotel hotel) {
        subTotal = hotel.getPricePerNight();
        vat = (float) (0.1 * subTotal);

        total = subTotal - discount + vat;

        txtSubtotal.setText(String.format("%,.0f", subTotal));
        txtDiscount.setText(String.format("%,.0f", discount));
        txtVAT.setText(String.format("%,.0f", vat));
        txtTotal.setText(String.format("%,.0f", total));
    }
}