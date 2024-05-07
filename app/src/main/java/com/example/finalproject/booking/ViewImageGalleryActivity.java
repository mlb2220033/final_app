package com.example.finalproject.booking;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.finalproject.R;
import com.example.finalproject.adapter.ImageGalleryAdapter;
import com.example.finalproject.adapter.ImageGalleryThumbnailAdapter;
import com.example.finalproject.model.Hotel;
import com.example.finalproject.model.ImageGalleryItem;
import com.example.finalproject.model.RecyclerViewInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewImageGalleryActivity extends AppCompatActivity implements RecyclerViewInterface {

    private ViewPager2 viewPagerImageGallery;
    private RecyclerView glr_img_hotel_thumbnail;
    private ImageView btnBack;
    private List<ImageGalleryItem> list;
    private ImageGalleryAdapter adapter;
    private ImageGalleryThumbnailAdapter adapterThumbnail;
    private DatabaseReference ref;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_image_gallery);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseDatabase.getInstance();
        String id = getIntent().getStringExtra("hotel_id");

        setUpUI();

        getImagesListUrl(id);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpUI() {
        viewPagerImageGallery = findViewById(R.id.viewPagerImageGallery);
        glr_img_hotel_thumbnail = findViewById(R.id.glr_img_hotel_thumbnail);
        btnBack = findViewById(R.id.btnBack);
        list = new ArrayList<>();
        adapter = new ImageGalleryAdapter(list);
        adapterThumbnail = new ImageGalleryThumbnailAdapter(list, this);
        viewPagerImageGallery.setAdapter(adapter);
        glr_img_hotel_thumbnail.setAdapter(adapterThumbnail);
    }

    private void getImagesListUrl(String hotelId) {
        ref = db.getReference("Hotels");
        ref.child(hotelId).child("hotelImages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()) {
                    String url = item.getValue(String.class);
                    list.add(new ImageGalleryItem(url));
                }
                adapter.notifyDataSetChanged();
                adapterThumbnail.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewImageGalleryActivity.this, "get list fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        viewPagerImageGallery.setCurrentItem(position, true);
    }
}