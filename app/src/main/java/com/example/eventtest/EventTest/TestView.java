package com.example.eventtest.EventTest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by bjhl on 16/5/11.
 */
public class TestView extends View {
    private final static String TAG = TestView.class.getSimpleName();

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
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
    public boolean onTouchEvent(MotionEvent ev) {
        getLocations(ev);
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

    private void getLocations(MotionEvent ev){
        float rawX = ev.getRawX();
        float rawY = ev.getRawY();
        float x = ev.getX();
        float y = ev.getY();
        System.err.println("Android坐标系"+" rawX " + rawX+" rawY " + rawY);
        System.err.println("视图坐标系"+" x " + x+" y " + y);
        System.err.println("layout" + " left " + getLeft() + " top " + getTop() + " right " + getRight() + " bottom " + getBottom()+" getWidth "+getWidth());


        int []location=new int[2];
        getLocationInWindow(location);
        int xx=location[0];
        int yy=location[1];
        System.err.println("getLocationInWindow"+" x " + xx+" y " + yy);


        int []location1=new int[2];
        getLocationOnScreen(location1);
        int xxx=location1[0];
        int yyy=location1[1];
        System.err.println("getLocationOnScreen"+" x " + xxx+" y " + yyy);
    }
}
