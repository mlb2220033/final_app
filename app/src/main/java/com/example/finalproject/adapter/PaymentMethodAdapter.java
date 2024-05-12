package com.example.finalproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.BankingBrand;
import com.example.finalproject.model.DataHolder;
import com.example.finalproject.model.PaymentMethod;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder> {

    private List<PaymentMethod> list;
    private int selectedPosition = -1;

    public PaymentMethodAdapter(List<PaymentMethod> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentMethodViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodViewHolder holder, int position) {
        holder.setupUI(list.get(position));
        holder.radCVV.setChecked(position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentMethodViewHolder extends RecyclerView.ViewHolder {

        private TextView txtType;
        private ImageView imgCard;
        private RadioButton radCVV;

        public PaymentMethodViewHolder(@NonNull View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.txtType);
            imgCard = itemView.findViewById(R.id.imgCard);
            radCVV = itemView.findViewById(R.id.radCVV);

            radCVV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int copyOfLastCheckedPosition = selectedPosition;
                    selectedPosition = getBindingAdapterPosition();
                    DataHolder.paymentMethod = list.get(selectedPosition);
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(selectedPosition);
                }
            });
        }

        void setupUI(PaymentMethod payment) {
            txtType.setText(payment.getPaymentName());
            if (payment.getPaymentType().equals("credit/debit")) {
                if (payment.getPaymentName().equals("Visa")) {
                    imgCard.setImageResource(R.drawable.ic_visa);
                } else if (payment.getPaymentName().equals("MasterCard")) {
                    imgCard.setImageResource(R.drawable.ic_mastercard);
                }
            } else if (payment.getPaymentType().equals("e-wallet")) {
                imgCard.setImageResource(R.drawable.ic_momo);
            } else {
                String bankingcode = payment.getCardNumber().substring(0, 6);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("BankingCode");
                ref.child(bankingcode).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        BankingBrand brand = snapshot.getValue(BankingBrand.class);
                        Picasso.get().load(brand.getImg_url()).into(imgCard);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            if (payment.getPaymentType().equals("credit/debit") || payment.getPaymentType().equals("banking")) {
                String secure_cardNumb = payment.getCardNumber().substring(0, 10);
                radCVV.setText(String.format("****** %s", secure_cardNumb));
            } else {
                String secure_cardNumb = payment.getCardNumber().substring(0, 6);
                radCVV.setText(String.format("**** %s", secure_cardNumb));
            }
        }
    }
}
