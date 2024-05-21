package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.BlogAdapter;
import com.example.finalproject.adapter.HotelFacilitiesItemAdapter;
import com.example.finalproject.model.Blog;
import com.example.finalproject.model.HotelFacilities;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends AppCompatActivity {
    RecyclerView rvBlog;
    ArrayList<Blog> blogList;
    BlogAdapter blogAdapter;
    FirebaseDatabase firebaseDatabase;
    String hotelID;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        addViews();
        addEvents();
        getBlogData();
    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getBlogData() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("Blogs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Blog blog = dataSnapshot.getValue(Blog.class);
                    blogList.add(blog);
                }
                blogAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addViews() {
        imgBack = findViewById(R.id.imgBack);

        rvBlog = findViewById(R.id.rvBlog);
        rvBlog.setHasFixedSize(true);
        rvBlog.setLayoutManager(new LinearLayoutManager(this));

        blogList = new ArrayList<>();
        blogAdapter = new BlogAdapter(blogList, this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        rvBlog.setAdapter(blogAdapter);
    }
}