package com.example.finalproject.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

public class NonScrollableRecyclerView extends RecyclerView {

    public NonScrollableRecyclerView(Context context) {
        super(context);
    }

    public NonScrollableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        // Do not allow touch events to be intercepted, preventing scrolling
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Do not handle touch events
        return false;
    }

    @Override
    public void onScrollStateChanged(int state) {
        // Do not change the scroll state
    }

    @Override
    public void scrollToPosition(int position) {
        // Do not scroll to position
    }

    @Override
    public void smoothScrollToPosition(int position) {
        // Do not smoothly scroll to position
    }
}
