package com.example.eventtest.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.example.eventtest.BuildConfig;
import com.example.eventtest.R;

/**
 * Created by bjhl on 16/5/14.
 * 可选择水平，还是垂直移动
 */
public class MyScrollViewGroup extends ViewGroup {

    int desireWidth, desireHeight;
    Orientation orientation;
    Scroller mScroller;
    VelocityTracker velocityTracker;
    int minFlingVelocity;
    int maxFlingVelocity;
    int mPointerId;
    int mTouchSlop;
    float downX, downY, x ,y;

    public MyScrollViewGroup(Context context) {
        super(context);
        initView();
    }

    public MyScrollViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SlideGroup);
        try {
            orientation = Orientation.valueOf(a.getInt(R.styleable.SlideGroup_orientation, 0));
        } finally {
            a.recycle();
        }
    }
    private void initView(){
        mScroller = new Scroller(getContext());
        minFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
        maxFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
        mTouchSlop =ViewConfiguration.getTouchSlop();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 计算所有child view 要占用的空间
        desireWidth = 0;
        desireHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = getChildAt(i);
            if (v.getVisibility() != View.GONE) {
                LayoutParams lp = (LayoutParams) v.getLayoutParams();
                measureChildWithMargins(v, widthMeasureSpec, 0, heightMeasureSpec, 0);

                // 只是在这里增加了垂直或者水平方向的判断
                if (orientation == Orientation.HORIZONTAL) {
                    desireWidth += v.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                    desireHeight = Math.max(desireHeight, v.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                } else {
                    desireWidth = Math.max(desireWidth, v.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                    desireHeight += v.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                }
            }
        }

        // count with padding
        desireWidth += getPaddingLeft() + getPaddingRight();
        desireHeight += getPaddingTop() + getPaddingBottom();

        // see if the size is big enough
        desireWidth = Math.max(desireWidth, getSuggestedMinimumWidth());
        desireHeight = Math.max(desireHeight, getSuggestedMinimumHeight());

        setMeasuredDimension(resolveSize(desireWidth, widthMeasureSpec), resolveSize(desireHeight, heightMeasureSpec));
        /**
         * public static int resolveSize(int size, int measureSpec) { int result = size; int specMode =
         * MeasureSpec.getMode(measureSpec); int specSize = MeasureSpec.getSize(measureSpec); switch (specMode) { case
         * MeasureSpec.UNSPECIFIED: result = size; break; case MeasureSpec.AT_MOST: result = Math.min(size, specSize);
         * break; case MeasureSpec.EXACTLY: result = specSize; break; } return result; }
         **/
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        if (BuildConfig.DEBUG)
            Log.d("onlayout", "parentleft: " + parentLeft + "   parenttop: " + parentTop + "   parentright: "
                + parentRight + "   parentbottom: " + parentBottom);

        int left = parentLeft;
        int top = parentTop;

        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = getChildAt(i);
            if (v.getVisibility() != View.GONE) {
                LayoutParams lp = (LayoutParams) v.getLayoutParams();
                final int childWidth = v.getMeasuredWidth();
                final int childHeight = v.getMeasuredHeight();
                final int gravity = lp.gravity;
                final int horizontalGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
                final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

                if (orientation == Orientation.HORIZONTAL) {
                    // layout horizontally, and only consider vertical gravity

                    left += lp.leftMargin;
                    top = parentTop + lp.topMargin;
                    if (gravity != -1) {
                        switch (verticalGravity) {
                            case Gravity.TOP:
                                break;
                            case Gravity.CENTER_VERTICAL:
                                top =
                                    parentTop + (parentBottom - parentTop - childHeight) / 2 + lp.topMargin
                                        - lp.bottomMargin;
                                break;
                            case Gravity.BOTTOM:
                                top = parentBottom - childHeight - lp.bottomMargin;
                                break;
                        }
                    }

                    if (BuildConfig.DEBUG) {
                        Log.d("onlayout", "child[width: " + childWidth + ", height: " + childHeight + "]");
                        Log.d("onlayout", "child[left: " + left + ", top: " + top + ", right: " + (left + childWidth)
                            + ", bottom: " + (top + childHeight));
                    }
                    v.layout(left, top, left + childWidth, top + childHeight);
                    left += childWidth + lp.rightMargin;
                } else {
                    // layout vertical, and only consider horizontal gravity

                    left = parentLeft;
                    top += lp.topMargin;
                    switch (horizontalGravity) {
                        case Gravity.LEFT:
                            break;
                        case Gravity.CENTER_HORIZONTAL:
                            left =
                                parentLeft + (parentRight - parentLeft - childWidth) / 2 + lp.leftMargin
                                    - lp.rightMargin;
                            break;
                        case Gravity.RIGHT:
                            left = parentRight - childWidth - lp.rightMargin;
                            break;
                    }
                    v.layout(left, top, left + childWidth, top + childHeight);
                    top += childHeight + lp.bottomMargin;
                }
            }
        }
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    /**
     * onInterceptTouchEvent()用来询问是否要拦截处理。 onTouchEvent()是用来进行处理。
     *
     * 例如：parentLayout----childLayout----childView 事件的分发流程：
     * parentLayout::onInterceptTouchEvent()---false?--->
     * childLayout::onInterceptTouchEvent()---false?--->
     * childView::onTouchEvent()---false?--->
     * childLayout::onTouchEvent()---false?---> parentLayout::onTouchEvent()
     *
     *
     *
     * 如果onInterceptTouchEvent()返回false，且分发的子View的onTouchEvent()中返回true，
     * 那么onInterceptTouchEvent()将收到所有的后续事件。
     *
     * 如果onInterceptTouchEvent()返回true，原本的target将收到ACTION_CANCEL，该事件
     * 将会发送给我们自己的onTouchEvent()。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        if (BuildConfig.DEBUG)
            Log.d("onInterceptTouchEvent", "action: " + action);

        if (action == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
            // 该事件可能不是我们的
            return false;
        }

        boolean isIntercept = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 如果动画还未结束，则将此事件交给onTouchEvet()处理，
                // 否则，先分发给子View
                isIntercept = !mScroller.isFinished();
                // 如果此时不拦截ACTION_DOWN时间，应该记录下触摸地址及手指id，当我们决定拦截ACTION_MOVE的event时，
                // 将会需要这些初始信息（因为我们的onTouchEvent将可能接收不到ACTION_DOWN事件）
                mPointerId = ev.getPointerId(0);
//			if (!isIntercept) {
                downX = x = ev.getX();
                downY = y = ev.getY();
//			}
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = ev.findPointerIndex(mPointerId);
                if (BuildConfig.DEBUG)
                    Log.d("onInterceptTouchEvent", "pointerIndex: " + pointerIndex
                            + ", pointerId: " + mPointerId);
                float mx = ev.getX(pointerIndex);
                float my = ev.getY(pointerIndex);

                if (BuildConfig.DEBUG)
                    Log.d("onInterceptTouchEvent", "action_move [touchSlop: "
                            + mTouchSlop + ", deltaX: " + (x - mx) + ", deltaY: "
                            + (y - my) + "]");

                // 根据方向进行拦截，（其实这样，如果我们的方向是水平的，里面有一个ScrollView，那么我们是支持嵌套的）
                if (orientation == Orientation.HORIZONTAL) {
                    if (Math.abs(x - mx) >= mTouchSlop) {
                        // we get a move event for ourself
                        isIntercept = true;
                    }
                } else {
                    if (Math.abs(y - my) >= mTouchSlop) {
                        isIntercept = true;
                    }
                }

                //如果不拦截的话，我们不会更新位置，这样可以通过累积小的移动距离来判断是否达到可以认为是Move的阈值。
                //这里当产生拦截的话，会更新位置（这样相当于损失了mTouchSlop的移动距离，如果不更新，可能会有一点点跳的感觉）
                if (isIntercept) {
                    x = mx;
                    y = my;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 这是触摸的最后一个事件，无论如何都不会拦截
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                solvePointerUp(ev);
                break;
        }
        return isIntercept;
    }

    private void solvePointerUp(MotionEvent event) {
        // 获取离开屏幕的手指的索引
        int pointerIndexLeave = event.getActionIndex();
        int pointerIdLeave = event.getPointerId(pointerIndexLeave);
        if (mPointerId == pointerIdLeave) {
            // 离开屏幕的正是目前的有效手指，此处需要重新调整，并且需要重置VelocityTracker
            int reIndex = pointerIndexLeave == 0 ? 1 : 0;
            mPointerId = event.getPointerId(reIndex);
            // 调整触摸位置，防止出现跳动
            x = event.getX(reIndex);
            y = event.getY(reIndex);
            if (velocityTracker != null)
                velocityTracker.clear();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 获取索引为0的手指id
                mPointerId = event.getPointerId(0);
                x = event.getX();
                y = event.getY();
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                // 获取当前手指id所对应的索引，虽然在ACTION_DOWN的时候，我们默认选取索引为0
                // 的手指，但当有第二个手指触摸，并且先前有效的手指up之后，我们会调整有效手指

                // 屏幕上可能有多个手指，我们需要保证使用的是同一个手指的移动轨迹，
                // 因此此处不能使用event.getActionIndex()来获得索引
                final int pointerIndex = event.findPointerIndex(mPointerId);
                float mx = event.getX(pointerIndex);
                float my = event.getY(pointerIndex);

                moveBy((int) (x - mx), (int) (y - my));

                x = mx;
                y = my;
                break;
            case MotionEvent.ACTION_UP:
                //先判断是否是点击事件
                final int pi = event.findPointerIndex(mPointerId);

                if((isClickable() || isLongClickable()) && ((event.getX(pi) - downX) < mTouchSlop || (event.getY(pi) - downY) < mTouchSlop)) {
                    //这里我们得到了一个点击事件
                    if(isFocusable() && isFocusableInTouchMode() && !isFocused())
                        requestFocus();
                    if(event.getEventTime() - event.getDownTime() >= ViewConfiguration.getLongPressTimeout() && isLongClickable()) {
                        //是一个长按事件
                        performLongClick();
                    } else {
                        performClick();
                    }
                } else {
                    velocityTracker.computeCurrentVelocity(1000, maxFlingVelocity);
                    float velocityX = velocityTracker.getXVelocity(mPointerId);
                    float velocityY = velocityTracker.getYVelocity(mPointerId);

                    completeMove(-velocityX, -velocityY);
                    if (velocityTracker != null) {
                        velocityTracker.recycle();
                        velocityTracker = null;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 获取离开屏幕的手指的索引
                int pointerIndexLeave = event.getActionIndex();
                int pointerIdLeave = event.getPointerId(pointerIndexLeave);
                if (mPointerId == pointerIdLeave) {
                    // 离开屏幕的正是目前的有效手指，此处需要重新调整，并且需要重置VelocityTracker
                    int reIndex = pointerIndexLeave == 0 ? 1 : 0;
                    mPointerId = event.getPointerId(reIndex);
                    // 调整触摸位置，防止出现跳动
                    x = event.getX(reIndex);
                    y = event.getY(reIndex);
                    if (velocityTracker != null)
                        velocityTracker.clear();
                }
                break;
        }
        return true;
    }
    //此处的moveBy是根据水平或是垂直排放的方向，来选择是水平移动还是垂直移动
    public void moveBy(int deltaX, int deltaY) {
        if (BuildConfig.DEBUG)
            Log.d("moveBy", "deltaX: " + deltaX + "    deltaY: " + deltaY);
        if (orientation == Orientation.HORIZONTAL) {
            if (Math.abs(deltaX) >= Math.abs(deltaY))
                scrollBy(deltaX, 0);
        } else {
            if (Math.abs(deltaY) >= Math.abs(deltaX))
                scrollBy(0, deltaY);
        }
    }

    private void completeMove(float velocityX, float velocityY) {
        if (orientation == Orientation.HORIZONTAL) {
            int mScrollX = getScrollX();
            int maxX = desireWidth - getWidth();
            if (mScrollX > maxX) {
                // 超出了右边界，弹回
                mScroller.startScroll(mScrollX, 0, maxX - mScrollX, 0);
                invalidate();
            } else if (mScrollX < 0) {
                // 超出了左边界，弹回
                mScroller.startScroll(mScrollX, 0, -mScrollX, 0);
                invalidate();
            } else if (Math.abs(velocityX) >= minFlingVelocity && maxX > 0) {
                mScroller.fling(mScrollX, 0, (int) velocityX, 0, 0, maxX, 0, 0);
                invalidate();
            }
        } else {
            int mScrollY = getScrollY();
            int maxY = desireHeight - getHeight();

            if (mScrollY > maxY) {
                // 超出了下边界，弹回
                mScroller.startScroll(0, mScrollY, 0, maxY - mScrollY);
                invalidate();
            } else if (mScrollY < 0) {
                // 超出了上边界，弹回
                mScroller.startScroll(0, mScrollY, 0, -mScrollY);
                invalidate();
            } else if (Math.abs(velocityY) >= minFlingVelocity && maxY > 0) {
                mScroller.fling(0, mScrollY, 0, (int) velocityY, 0, 0, 0, maxY);
                invalidate();
            }
        }
    }
    /**
    computeScroll（）是在ViewGroup的drawChild（）中调用的，上面的代码中，我们通过调用computeScrollOffset（）来判断滑动是否已停止，如果没有，那么我们可以通过getCurrX（）和getCurrY（）来获得新位置，
     然后通过调用scrollTo（）来实现滑动，这里需要注意的是postInvalidate（）的调用，它会将重绘的这个Event加入UI线程的消息队列，等scrollTo（）执行完成后，就会处理这个事件，
     然后再次调用ViewGroup的draw（）-->drawChild（）-->computeScroll（）-->scrollTo（）如此就实现了连续绘制的效果。
    **/
     @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (orientation == Orientation.HORIZONTAL) {
                scrollTo(mScroller.getCurrX(), 0);
                postInvalidate();
            } else {
                scrollTo(0, mScroller.getCurrY());
                postInvalidate();
            }
        }
    }


    public static class LayoutParams extends MarginLayoutParams {
        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.SlideGroup);

            gravity = ta.getInt(R.styleable.SlideGroup_layout_gravity, -1);

            ta.recycle();
        }

        public LayoutParams(int width, int height) {
            this(width, height, -1);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    public static enum Orientation {
        HORIZONTAL(0), VERTICAL(1);

        private int value;

        private Orientation(int i) {
            value = i;
        }

        public int value() {
            return value;
        }

        public static Orientation valueOf(int i) {
            switch (i) {
                case 0:
                    return HORIZONTAL;
                case 1:
                    return VERTICAL;
                default:
                    throw new RuntimeException("[0->HORIZONTAL, 1->VERTICAL]");
            }
        }
    }
}
