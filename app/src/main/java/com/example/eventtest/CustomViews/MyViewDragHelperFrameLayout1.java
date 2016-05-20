package com.example.eventtest.CustomViews;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by bjhl on 16/5/11. ViewDragHelper 实现
 */
public class MyViewDragHelperFrameLayout1 extends FrameLayout {

    public static enum State {
        OPened, Moving, Closed
    }

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop = 24;
    private int mLastMotionX;
    private int mLastMotionY;

    int screenwidth;
    boolean isOriginPostion = true;

    private ViewDragHelper mViewDragHelper;


    public MyViewDragHelperFrameLayout1(Context context) {
        super(context);
        initView(context);
    }

    public MyViewDragHelperFrameLayout1(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenwidth = wm.getDefaultDisplay().getWidth();
        final float density = getResources().getDisplayMetrics().density;
        mScroller = new Scroller(context);
        mViewDragHelper = ViewDragHelper.create(this, callback);
//        mMyFrameLayout1 = this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        System.err.println("onLayout" + " left " + left + " top " + top + " right " + right + " bottom " + bottom
            + " getWidth " + getWidth());
        // layout(this.left, getTop(), this.left + getWidth(), getBottom());
        if (changed && !isOriginPostion) {
            offsetLeftAndRight(getWidth() - 10);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child instanceof RelativeLayout;
        }

        /**
         * Restrict the motion of the dragged child view along the horizontal axis.
         * The default implementation does not allow horizontal motion; the extending
         * class must override this method and provide the desired clamping.
         *
         * @param child Child view being dragged
         * @param left  Attempted motion along the X axis
         * @param dx    Proposed change in position for left
         * @return The new clamped position for left
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        /**
         * Restrict the motion of the dragged child view along the vertical axis.
         * The default implementation does not allow vertical motion; the extending
         * class must override this method and provide the desired clamping.
         *
         * @param child Child view being dragged
         * @param top   Attempted motion along the Y axis
         * @param dy    Proposed change in position for top
         * @return The new clamped position for top
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);
        }

        /**
         * Called to determine the Z-order of child views.
         *
         * @param index the ordered position to query for
         * @return index of the view that should be ordered at position <code>index</code>
         */
        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth();
//            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return super.getViewVerticalDragRange(child);
        }
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }
        @Override
        public boolean onEdgeLock(int edgeFlags) {
            return super.onEdgeLock(edgeFlags);
        }
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //相对于父布局
            if (releasedChild.getLeft()< getWidth()/2){
                mViewDragHelper.smoothSlideViewTo(releasedChild, 0,0);
//                mViewDragHelper.settleCapturedViewAt(0,0);
                ViewCompat.postInvalidateOnAnimation(MyViewDragHelperFrameLayout1.this);
            } else {
//                mViewDragHelper.settleCapturedViewAt(getWidth()-5,0);
                mViewDragHelper.smoothSlideViewTo(releasedChild, getWidth()-5,0);
                ViewCompat.postInvalidateOnAnimation(MyViewDragHelperFrameLayout1.this);
                isOriginPostion = false;
            }
        }
    };

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
