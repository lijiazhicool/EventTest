package com.example.eventtest.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by bjhl on 16/5/11.
 */
public class MyFrameLayout extends FrameLayout {

    public static enum State {
        OPened, Moving, Closed
    }

    private boolean mIsBeingDragged = false;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop=24;
    private int mLastMotionX;
    private int mLastMotionY;
    private static final int SNAP_VELOCITY = 1000;

    int downX, downY;
    int screenwidth;
    int left = 0;

    State currentState;

    boolean isOriginPostion = true;

    int maxAcceleration;
    private static final float MAX_ACCELERATION = 2000.0f;

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenwidth = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final float density = getResources().getDisplayMetrics().density;
        maxAcceleration = (int) (MAX_ACCELERATION * density + 0.5f);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        System.err.println("onLayout" + " left " + left + " top " + top + " right " + right + " bottom " + bottom+" getWidth "+getWidth());
        // need this since otherwise this View jumps back to its original position
        // ignoring its displacement
        // when (re-)doing layout, e.g. when a fragment transaction is committed
        if (changed && currentState == State.Closed){// TODO: 关键代码
            offsetLeftAndRight(screenwidth-10-left);
        }

    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        System.err.println("onSizeChanged" + " w " + w + " h " + h + " oldw " + oldw + " oldh " + oldh +" getWidth "+getWidth());
//        super.onSizeChanged(w, h, oldw, oldh);
//        if (oldh != h && w ==oldw) {
//            if (currentState == State.OPened) {
//                layout(screenwidth - getWidth(), getTop(), screenwidth + getWidth() - 5, getBottom());
//            } else if (currentState == State.Closed) {
//                layout(screenwidth - 5, getTop(), screenwidth + getWidth() - 5, getBottom());
//            }
//        }
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = (int) x;
                mLastMotionY = (int) y;
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final float dx = x - mLastMotionX;
                final float xDiff = Math.abs(dx);
                final float yDiff = Math.abs(y - mLastMotionY);
                if (xDiff > mTouchSlop && xDiff > yDiff) {
                    mIsBeingDragged = true;
                }
                break;

        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 计算移动的距离
                int offX = x - (int) mLastMotionX;
                int offY = y - (int) mLastMotionY;
                if (Math.abs(offX) > Math.abs(offY)) {
                    if (getLeft() + offX < screenwidth - getWidth()) {
                        layout(screenwidth - getWidth(), getTop(), screenwidth, getBottom());
                        currentState = State.OPened;
                    } else if (getRight() + offX > screenwidth + getWidth()) {
                        layout(screenwidth, getTop(), screenwidth + getWidth(), getBottom());
                        currentState = State.Closed;
                    } else {
                        isOriginPostion =true;
                        currentState = State.Moving;
                        offY = 0;
                        left = getLeft() + offX;
                        // 1. 调用layout方法来重新放置它的位置
                        layout(getLeft() + offX, getTop() + offY, getRight() + offX, getBottom() + offY);
                        // 2. 调用layout方法来重新放置它的位置
                        // offsetLeftAndRight(offX);
                        // offsetTopAndBottom(offY);
                        // 3. 调用layout方法来重新放置它的位置
//                         ViewGroup.MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
//                         mlp.leftMargin = getLeft() + offX;
//                         setLayoutParams(mlp);
                        // 4.scrollTo和scrollBy
                        // ((View) getParent()).scrollBy(-offX,- offY);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 平滑过度
                if (screenwidth - getLeft() < getWidth() / 2) {
                    left = screenwidth - 5;
                    isOriginPostion =false;
                    smoothScrollTo(getLeft(),left);
                    currentState = State.Closed;
                } else {
                    isOriginPostion =true;
                    left = screenwidth - getWidth();
                    smoothScrollTo(getLeft(),left);
                    currentState = State.OPened;
                }
                break;
        }
        return true;
    }

   private void smoothScrollTo(int srcX, int desX){
       mScroller.startScroll(srcX, 0,desX-srcX,0,Math.abs(desX-srcX)*3);
       invalidate();
   }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            layout(mScroller.getCurrX(),getTop(),mScroller.getCurrX()+getWidth(),getBottom());
            postInvalidate();
        }
    }

    public void setState(State state){
        if (state == currentState){
            return;
        }
        currentState = state;
        if (state ==State.Closed){
            smoothScrollTo(getLeft(),screenwidth - 5);
        } else {
            smoothScrollTo(getLeft(),screenwidth - getWidth());
        }
    }

}
