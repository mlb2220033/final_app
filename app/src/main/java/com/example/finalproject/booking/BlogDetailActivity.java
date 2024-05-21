package com.example.finalproject.booking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.adapter.BlogCommentAdapter;
import com.example.finalproject.adapter.ContentAdapter;
import com.example.finalproject.adapter.ReviewHotelAdapter;
import com.example.finalproject.model.Blog;
import com.example.finalproject.model.Rating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BlogDetailActivity extends AppCompatActivity {
    ImageView imgBlog, imgBack;
    EditText edtComment;
    TextView txtTitleBlog, txtAuthor, txtDate, txtAll;
    FirebaseDatabase firebaseDatabase;
    String blogID, userName, comment;
    ArrayList<String> contentList;
    RecyclerView rvBlogContent, rvBlogComment;
    BlogCommentAdapter blogCommentAdapter;
    AppCompatButton btnSendReview;
    private ArrayList<Rating> ratingArrayRvList;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        addView();
        getDataFromPreviousActivity();
        addEvent();
        getBlogData();
        fetchComments();
        loadMyInfo();
    }

    private void loadMyInfo() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");

        myRef.child("" + firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName = "" + snapshot.child("full name").getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


    }

    private void fetchComments() {
        DatabaseReference reference = firebaseDatabase.getReference("Blogs").child(blogID).child("Ratings");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ratingArrayRvList = new ArrayList<>();
                int count = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (count < 2) {
                        Rating rating = dataSnapshot.getValue(Rating.class);
                        if (rating != null) {
                            ratingArrayRvList.add(rating);
                            count++;
                        }
                    } else {
                        break;
                    }
                }
                blogCommentAdapter = new BlogCommentAdapter(BlogDetailActivity.this, ratingArrayRvList);
                rvBlogComment.setAdapter(blogCommentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading ratings: " + error.getMessage());
            }
        });
    }

    private void getDataFromPreviousActivity() {
        Intent intent = getIntent();
        blogID = intent.getStringExtra("blogID");
    }

    private void getBlogData() {
        firebaseDatabase.getReference().child("Blogs").child(blogID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Blog blog = snapshot.getValue(Blog.class);
                        if (blog == null) {

                            return;
                        }

                        txtAuthor.setText(blog.getBlogAuthor());
                        txtTitleBlog.setText(blog.getBlogTitle());
                        txtDate.setText(blog.getDate());
                        Picasso.get().load(blog.getBlogImage()).into(imgBlog);

                        Object contentObj = blog.getContent();
                        if (contentObj instanceof ArrayList) {
                            ArrayList<String> blogContent = (ArrayList<String>) contentObj;
                            contentList = blogContent;
                            updateRecyclerView();
                        } else {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updateRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvBlogContent.setLayoutManager(layoutManager);

        ContentAdapter adapter = new ContentAdapter(contentList);
        rvBlogContent.setAdapter(adapter);
    }

    private void addEvent() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlogDetailActivity.this, BlogAllCommentActivity.class);
                intent.putExtra("blogID", blogID);
                startActivity(intent);
            }
        });
        btnSendReview.setOnClickListener(v -> {
            validateData();
        });
    }

    private void validateData() {

        comment = edtComment.getText().toString();

        if (TextUtils.isEmpty(comment.trim())) {
            Toast.makeText(this, "Please enter your comment", Toast.LENGTH_SHORT).show();
            edtComment.setError("Comment is required");
            edtComment.requestFocus();
        } else {
            addCommentToDatabase();
        }
    }

    private void addCommentToDatabase() {
        firebaseAuth = FirebaseAuth.getInstance();

        String userID = firebaseAuth.getCurrentUser().getUid();

        DatabaseReference timeRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@android.support.annotation.NonNull DataSnapshot snapshot) {
                long offset = snapshot.getValue(Long.class);
                long estimatedServerTimeMs = System.currentTimeMillis() + offset;

                Map<String, Object> newRatingValues = new HashMap<>();
                newRatingValues.put("comment", comment);
                newRatingValues.put("uid", userID);
                newRatingValues.put("userName", userName);
                newRatingValues.put("timeRating", estimatedServerTimeMs);

                DatabaseReference ratingsRef = FirebaseDatabase.getInstance().getReference().child("Blogs").child(blogID).child("Ratings").child(userID);
                ratingsRef.setValue(newRatingValues)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(BlogDetailActivity.this, "Rating added successfully", Toast.LENGTH_SHORT).show();
                            clearReview();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(BlogDetailActivity.this, "Failed to add rating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@android.support.annotation.NonNull DatabaseError error) {

            }
        });
    }

    private void clearReview() {
        ((EditText) findViewById(R.id.edtComment)).setText("");
    }

    private void addView() {
        txtAuthor = findViewById(R.id.txtAuthor);
        txtTitleBlog = findViewById(R.id.txtTitleBlog);
        txtDate = findViewById(R.id.txtDate);
        txtAll = findViewById(R.id.txtAll);
        imgBack = findViewById(R.id.imgBack);
        imgBlog = findViewById(R.id.imgBlog);
        edtComment = findViewById(R.id.edtComment);
        ratingArrayRvList = new ArrayList<>();

        rvBlogContent = findViewById(R.id.rvBlogContent);
        rvBlogComment = findViewById(R.id.rvBlogComment);
        contentList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        btnSendReview = findViewById(R.id.btnSendReview);
    }
}
