package com.example.finalproject.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.BlogCommentAdapter;
import com.example.finalproject.adapter.ContentAdapter;
import com.example.finalproject.model.Blog;
import com.example.finalproject.model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlogAllCommentActivity extends AppCompatActivity {
    ImageView imgBlog, imgBack;
    TextView txtTitleBlog, txtAuthor, txtDate, txtAll;
    FirebaseDatabase firebaseDatabase;
    String blogID;
    ArrayList<String> contentList;
    RecyclerView rvComments;
    BlogCommentAdapter blogCommentAdapter;
    private ArrayList<Rating> ratingArrayRvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_all_comment);
        addView();
        getDataFromPreviousActivity();
        addEvent();
        fetchComments();
    }

    private void getDataFromPreviousActivity() {
        Intent intent = getIntent();
        blogID = intent.getStringExtra("blogID");
    }

    private void fetchComments() {
        DatabaseReference reference = firebaseDatabase.getReference("Blogs").child(blogID).child("Ratings");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratingArrayRvList = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Rating rating = dataSnapshot.getValue(Rating.class);
                    if (rating != null) {
                        ratingArrayRvList.add(rating);

                    }

                }
                blogCommentAdapter = new BlogCommentAdapter(BlogAllCommentActivity.this, ratingArrayRvList);
                rvComments.setAdapter(blogCommentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading ratings: " + error.getMessage());
            }
        });
    }


    private void addEvent() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void addView() {

        imgBack = findViewById(R.id.imgBack);

        ratingArrayRvList = new ArrayList<>();

        rvComments = findViewById(R.id.rvComments);
        contentList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
}