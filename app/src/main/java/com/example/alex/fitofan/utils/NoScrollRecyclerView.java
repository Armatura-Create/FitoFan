package com.example.alex.fitofan.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollRecyclerView extends RecyclerView {

    public NoScrollRecyclerView(Context context){
        super(context);
    }

    public NoScrollRecyclerView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public NoScrollRecyclerView(Context context, AttributeSet attrs, int style){
        super(context, attrs, style);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){

        if(ev.getAction() == MotionEvent.ACTION_MOVE)
            return true;

        return super.dispatchTouchEvent(ev);
    }
}
