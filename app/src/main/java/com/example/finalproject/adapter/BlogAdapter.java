package com.example.finalproject.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.booking.BlogDetailActivity;
import com.example.finalproject.booking.HotelDetailActivity;
import com.example.finalproject.model.Blog;
import com.example.finalproject.model.Hotel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHodel> {
    private ArrayList<Blog> blogList;
    private Activity activity;

    public BlogAdapter(ArrayList<Blog> blogList, Activity activity) {
        this.blogList = blogList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BlogAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.blog_item, parent, false);
        return new BlogAdapter.ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogAdapter.ViewHodel holder, int position) {
        Blog blog = blogList.get(position);

        holder.txtAuthor.setText(blog.getBlogAuthor());
        holder.txtTitleBlog.setText(blog.getBlogTitle());

        Picasso.get().load(blog.getBlogImage()).into(holder.imgBlog);

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, blog.getBlogTitle());
                    String shareMessage = " Let me recommend you this Blog\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BlogDetailActivity.class);

                String blogID = blog.getId();
                intent.putExtra("blogID", blogID);


                activity.startActivityForResult(intent, 1);

            }
        });

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHodel extends RecyclerView.ViewHolder {
        ImageView imgBlog;
        TextView txtAuthor, txtTitleBlog;
        FloatingActionButton btnShare;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);

            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtTitleBlog = itemView.findViewById(R.id.txtTitleBlog);

            imgBlog = itemView.findViewById(R.id.imgBlog);
            btnShare = itemView.findViewById(R.id.btnShare);
        }
    }
}
