package com.android.jidian.extension.view.commonPlug.xLinearLayoutManager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class XLinearLayoutManager extends LinearLayoutManager {

    public XLinearLayoutManager(Context context) {
        super(context);
    }

    public XLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public XLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("bug", "crash in RecyclerView");
        }
    }

    @Override
    public void scrollToPosition(int position) {
        try {
            super.scrollToPosition(position);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("bug", "crash in RecyclerView");
        }
    }

}