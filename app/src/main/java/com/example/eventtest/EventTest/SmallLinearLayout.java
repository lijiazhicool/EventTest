package com.example.eventtest.EventTest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by bjhl on 16/5/11.
 */
public class SmallLinearLayout extends LinearLayout {
    private final static String TAG = SmallLinearLayout.class.getSimpleName();
    public SmallLinearLayout(Context context) {
        super(context);
    }

    public SmallLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.err.println(TAG+" dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                System.err.println(TAG+" dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.err.println(TAG+" dispatchTouchEvent ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.err.println(TAG+" onInterceptTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                System.err.println(TAG+" onInterceptTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.err.println(TAG+" onInterceptTouchEvent ACTION_UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.err.println(TAG+" onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                System.err.println(TAG+" onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                System.err.println(TAG+" onTouchEvent ACTION_UP");
                break;
        }
        return super.onTouchEvent(ev);
    }
}
