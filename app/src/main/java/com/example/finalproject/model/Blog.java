package com.example.finalproject.model;

import java.util.List;

public class Blog {
    String id, blogTitle, blogAuthor, blogImage, date;
    private List<String> content;

    public Blog() {
    }

    public Blog(String id, String blogTitle, String blogAuthor, String blogImage, String date) {
        this.id = id;
        this.blogTitle = blogTitle;
        this.blogAuthor = blogAuthor;
        this.blogImage = blogImage;
        this.date = date;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogAuthor() {
        return blogAuthor;
    }

    public void setBlogAuthor(String blogAuthor) {
        this.blogAuthor = blogAuthor;
    }

    public String getBlogImage() {
        return blogImage;
    }

    public void setBlogImage(String blogImage) {
        this.blogImage = blogImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
