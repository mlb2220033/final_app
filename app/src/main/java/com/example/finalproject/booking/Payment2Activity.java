package com.example.finalproject.booking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.PaymentMethodAdapter;
import com.example.finalproject.adapter.PopularAdapter;
import com.example.finalproject.model.BankingBrand;
import com.example.finalproject.model.BookingHistory;
import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.ImageGalleryItem;
import com.example.finalproject.model.PaymentMethod;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Payment2Activity extends AppCompatActivity {
    CardView cardvCard, cardvEwallet, cardvBank;
    RecyclerView lvCard;
    ImageView imgCardCre, imgEwallet, imgBank;
    ConstraintLayout layoutCardCre, layoutEwallet, layoutBank;
    TextView txtCardCre, txtEwallet, txtBank;

    ImageView btnBack;
    CardView cardView;

    private DatabaseReference ref;
    private FirebaseDatabase db;
    private FirebaseAuth auth;

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

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        cardvCard = findViewById(R.id.cardvCard);
        cardvEwallet = findViewById(R.id.cardvEwallet);
        cardvBank = findViewById(R.id.cardvBank);
        lvCard = findViewById(R.id.lvCard);

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
                DataHolder.paymentMethod = null;
                loadCardList("credit/debit");
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
                DataHolder.paymentMethod = null;
                loadCardList("e-wallet");
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
                DataHolder.paymentMethod = null;
                loadCardList("banking");
            }
        });

        cardView = findViewById(R.id.cardview);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedCardView != null) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Payment2Activity.this,R.style.AppBottomSheetDialogTheme);
                    if (selectedCardView != cardvEwallet) {
                        View view1 = LayoutInflater.from(Payment2Activity.this).inflate(R.layout.add_new_card,null);
                        Button btnAddIn4Card = view1.findViewById(R.id.btnAddIn4Card);
                        EditText edtCardName = view1.findViewById(R.id.edtCardName);
                        EditText edtCardNumber = view1.findViewById(R.id.edtCardNumber);
                        EditText edtCVV = view1.findViewById(R.id.edtCVV);
                        TextView txtTitleAddCard = view1.findViewById(R.id.txtTitleAddCard);
                        if (selectedCardView == cardvCard) {
                            txtTitleAddCard.setText("This method only applies to Visa or MasterCard cards");
                        } else {
                            txtTitleAddCard.setText("This method only applies to domestic banks");
                        }
                        bottomSheetDialog.setContentView(view1);
                        btnAddIn4Card.findViewById(R.id.btnAddIn4Card).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!edtCardName.getText().toString().isEmpty() && !edtCardNumber.getText().toString().isEmpty() && !edtCVV.getText().toString().isEmpty()) {
                                    String name = edtCardName.getText().toString();
                                    String number = edtCardNumber.getText().toString().trim();
                                    String cvv = edtCVV.getText().toString().trim();

                                    if (selectedCardView == cardvCard) {
                                        String first_numb_character = number.substring(0, 1);
                                        //Định dạng thẻ visa hoặc mastercard trong thực tế là số thẻ phải bắt đầu từ 5 hoặc 4
                                        if (!first_numb_character.equals("4") && !first_numb_character.equals("5") || number.length() != 16) {
                                            Toast.makeText(Payment2Activity.this, "Payment card format is incorrect", Toast.LENGTH_SHORT).show();
                                        } else {
                                            ref = db.getReference("Users");
                                            String uid = auth.getUid();
                                            if (first_numb_character.equals("4")) {
                                                PaymentMethod method = new PaymentMethod(name, number, cvv, "credit/debit", "Visa");
                                                ref.child(uid).child("payment-method").child(number).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (bottomSheetDialog.isShowing()) {
                                                            if (snapshot.exists()) {
                                                                Toast.makeText(Payment2Activity.this, "payment card already exists", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                ref.child(uid).child("payment-method").child(number).setValue(method);
                                                                Toast.makeText(Payment2Activity.this, "Success", Toast.LENGTH_SHORT).show();
                                                                loadCardList("credit/debit");
                                                                bottomSheetDialog.cancel();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            } else if (first_numb_character.equals("5")) {
                                                PaymentMethod method = new PaymentMethod(name, number, cvv, "credit/debit", "MasterCard");
                                                ref.child(uid).child("payment-method").child(number).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if (bottomSheetDialog.isShowing()) {
                                                            if (snapshot.exists()) {
                                                                Toast.makeText(Payment2Activity.this, "payment card already exists", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                ref.child(uid).child("payment-method").child(number).setValue(method);
                                                                Toast.makeText(Payment2Activity.this, "Success", Toast.LENGTH_SHORT).show();
                                                                loadCardList("credit/debit");
                                                                bottomSheetDialog.cancel();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        }
                                    } else if (selectedCardView == cardvBank) {
                                        String banking_bin_code = number.substring(0,6);
                                        if (number.length() != 16) {
                                            Toast.makeText(Payment2Activity.this, "Payment card format is incorrect", Toast.LENGTH_SHORT).show();
                                        } else {
                                            ref = db.getReference("BankingCode");
                                            ref.child(banking_bin_code).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        BankingBrand brand = snapshot.getValue(BankingBrand.class);
                                                        PaymentMethod method = new PaymentMethod(name, number, cvv, "banking", brand.getBrand_name());
                                                        DatabaseReference ref_tmp = db.getReference("Users");
                                                        String uid = auth.getUid();
                                                        ref_tmp.child(uid).child("payment-method").child(number).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if (bottomSheetDialog.isShowing()) {
                                                                    if (snapshot.exists()) {
                                                                        Toast.makeText(Payment2Activity.this, "payment card already exists", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        ref_tmp.child(uid).child("payment-method").child(number).setValue(method);
                                                                        Toast.makeText(Payment2Activity.this, "Success", Toast.LENGTH_SHORT).show();
                                                                        loadCardList("banking");
                                                                        bottomSheetDialog.cancel();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });

                                                    } else {
                                                        Toast.makeText(Payment2Activity.this, "This bank is not supported yet", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }
                                } else {
                                    Toast.makeText(Payment2Activity.this, "Information fields cannot be left blank", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        View view1 = LayoutInflater.from(Payment2Activity.this).inflate(R.layout.add_new_e_wallet,null);
                        Button btnAddIn4Wallet = view1.findViewById(R.id.btnAddIn4Wallet);
                        EditText edtWalletOwner = view1.findViewById(R.id.edtWalletOwner);
                        EditText edtNumberWallet = view1.findViewById(R.id.edtNumberWallet);
                        bottomSheetDialog.setContentView(view1);
                        
                        btnAddIn4Wallet.findViewById(R.id.btnAddIn4Wallet).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!edtWalletOwner.getText().toString().isEmpty() && !edtNumberWallet.getText().toString().isEmpty()) {
                                    String number = edtNumberWallet.getText().toString().trim();
                                    String name = edtWalletOwner.getText().toString();
                                    if (number.length() != 10) {
                                        Toast.makeText(Payment2Activity.this, "The phone number is not in the correct format", Toast.LENGTH_SHORT).show();
                                    } else {
                                        ref = db.getReference("Users");
                                        String uid = auth.getUid();
                                        PaymentMethod method = new PaymentMethod(name, number, "e-wallet", "MoMo");

                                        ref.child(uid).child("payment-method").child(number).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (bottomSheetDialog.isShowing()) {
                                                    if (snapshot.exists()) {
                                                        Toast.makeText(Payment2Activity.this, "number already exists", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        ref.child(uid).child("payment-method").child(number).setValue(method);
                                                        Toast.makeText(Payment2Activity.this, "Success", Toast.LENGTH_SHORT).show();
                                                        loadCardList("e-wallet");
                                                        bottomSheetDialog.cancel();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(Payment2Activity.this, "Information fields cannot be left blank", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    bottomSheetDialog.show();
                } else {
                    Toast.makeText(Payment2Activity.this, "Must select payment method first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnConfirm = findViewById(R.id.btnUpdate);

        // Set click listener for btnConfirm
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataHolder.paymentMethod != null) {
                    try {
                        ref = db.getReference("Users");
                        String uid = auth.getUid();
                        Long currentTime = Calendar.getInstance().getTimeInMillis();
                        String id = currentTime.toString();
                        BookingHistory history = new BookingHistory(id, DataHolder.hotel_id,
                                DataHolder.type_room,
                                DataHolder.total_cost,
                                currentTime,
                                DataHolder.check_in,
                                DataHolder.check_out,
                                DataHolder.room_numbers,
                                DataHolder.day_numbers,
                                "Paid");
                        ref.child(uid).child("booking-history").child(id).setValue(history);
                        showSuccessDialog();
                    } catch (Exception e) {
                        showFailDialog();
                    }
                } else {
                    Toast.makeText(Payment2Activity.this, "You need to choose a payment method", Toast.LENGTH_SHORT).show();
                }
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

    private void showFailDialog() {
        ConstraintLayout DialogFailPayment = findViewById(R.id.DialogFailPayment);
        View view  = LayoutInflater.from(Payment2Activity.this).inflate(R.layout.fail_payment, DialogFailPayment);
        Button CloseDialog = view.findViewById(R.id.CloseDialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(Payment2Activity.this);
        builder.setView(view);
        final  AlertDialog alertDialog = builder.create();

        CloseDialog.findViewById(R.id.CloseDialog).setOnClickListener(new View.OnClickListener() {
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

    private void loadCardList(String type) {
        ref = db.getReference("Users");
        String uid = auth.getUid();
        ArrayList<PaymentMethod> list = new ArrayList<>();
        if (!uid.isEmpty()) {
            ref.child(uid).child("payment-method").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot item: snapshot.getChildren()) {
                        PaymentMethod payment = item.getValue(PaymentMethod.class);
                        if (payment.getPaymentType().equals(type)) {
                            list.add(payment);
                        }
                    }
                    if(!list.isEmpty()){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(Payment2Activity.this);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        lvCard.setLayoutManager(layoutManager);
                        lvCard.setAdapter(new PaymentMethodAdapter(list));
                    } else {
                        ArrayList<PaymentMethod> list_tmp = new ArrayList<>();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(Payment2Activity.this);
                        layoutManager.setOrientation(RecyclerView.VERTICAL);
                        lvCard.setLayoutManager(layoutManager);
                        lvCard.setAdapter(new PaymentMethodAdapter(list_tmp));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
