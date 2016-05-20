package com.example.eventtest.EventTest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by bjhl on 16/5/11.
 */
public class BigLinearLayout extends LinearLayout {
    private final static String TAG = BigLinearLayout.class.getSimpleName();
    public BigLinearLayout(Context context) {
        super(context);
    }

    public BigLinearLayout(Context context, AttributeSet attrs) {
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
        /**如果 return true，事件会分发给当前 View 并由 dispatchTouchEvent 方法进行消费，同时事件会停止向下传递；**/
//        return true;


        /**如果 return false，事件分发分为两种情况：[返回 false，表示对获取到的事件停止向下传递，同时也不对该事件进行消费；]
         如果当前 View 获取的事件直接来自 Activity，则会将事件返回给 Activity 的 onTouchEvent 进行消费；
         如果当前 View 获取的事件来自外层父控件，则会将事件返回给父 View 的  onTouchEvent 进行消费。**/
//        return false;


        /**如果返回系统默认的 super.dispatchTouchEvent(ev)，事件会自动的分发给当前 View 的 onInterceptTouchEvent 方法。**/
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
        /**如果 onInterceptTouchEvent 返回 true，则表示将事件进行拦截，并将拦截到的事件交由当前 View 的 onTouchEvent 进行处理；**/
//        return true;


        /**如果 onInterceptTouchEvent 返回 false，则表示将事件放行，当前 View 上的事件会被传递到子 View 上，再由子 View 的 dispatchTouchEvent 来开始这个事件的分发**/
//        return false;

        /**如果 onInterceptTouchEvent 返回 super.onInterceptTouchEvent(ev)，跟false轨迹一样 **/
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
        /**如果返回了 true 则会接收并消费该事件 **/
        return true;

        /** 该方法返回了 false，那么这个事件会从当前 View 向上传递，并且都是由上层 View 的 onTouchEvent 来接收，如果传递到上面的 onTouchEvent 也返回 false，这个事件就会“消失”，而且接收不到下一次事件。 **/
//        return false;

        /**返回 super.onTouchEvent(ev) 默认处理事件的逻辑和返回 false 时相同**/
//        return super.onTouchEvent(ev);
    }
}
