package com.example.eventtest;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.example.eventtest.CustomViews.MyViewPage;
import com.example.eventtest.CustomViews.PagerAdapter;
import com.example.eventtest.CustomViews.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    boolean open = true;
    private AwesomePagerAdapter awesomeAdapter;

    private LayoutInflater mInflater;
    private List<View> mListViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyViewPage mViewPager = (MyViewPage) findViewById(R.id.viewpage);
        awesomeAdapter = new AwesomePagerAdapter();

        mListViews = new ArrayList<View>();
        mInflater = getLayoutInflater();
        mListViews.add(mInflater.inflate(R.layout.item_home, null));
        mListViews.add(mInflater.inflate(R.layout.item_home, null));
        mListViews.add(mInflater.inflate(R.layout.item_home, null));
//        mViewPager.setAdapter(awesomeAdapter);



        // MyScrollViewGroup myViewPage = (MyScrollViewGroup)findViewById(R.id.mvp);
        // myViewPage.setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // System.err.println("MyViewPage click");
        // }
        // });
        // ((ImageView)findViewById(R.id.iv1)).setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // System.err.println("ImageView click");
        // }
        // });
        // MainFragment mainFragment = new MainFragment();
        // getFragmentManager().beginTransaction()
        // .add(R.id.frame, mainFragment, "mainFragment")
        // .commit();
        // findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // if (open) {
        // open =false;
        // }else {
        // open =true;
        // }
        // }
        // });

    }

    // @Override
    // public boolean dispatchTouchEvent(MotionEvent ev) {
    // switch (ev.getAction()){
    // case MotionEvent.ACTION_DOWN:
    // System.err.println(TAG+" dispatchTouchEvent ACTION_DOWN");
    // break;
    // case MotionEvent.ACTION_MOVE:
    // System.err.println(TAG+" dispatchTouchEvent ACTION_MOVE");
    // break;
    // case MotionEvent.ACTION_UP:
    // System.err.println(TAG+" dispatchTouchEvent ACTION_UP");
    // break;
    // }
    // return super.dispatchTouchEvent(ev);
    // }
    //
    // @Override
    // public boolean onTouchEvent(MotionEvent ev) {
    // switch (ev.getAction()){
    // case MotionEvent.ACTION_DOWN:
    // System.err.println(TAG+" onTouchEvent ACTION_DOWN");
    // break;
    // case MotionEvent.ACTION_MOVE:
    // System.err.println(TAG+" onTouchEvent ACTION_MOVE");
    // break;
    // case MotionEvent.ACTION_UP:
    // System.err.println(TAG+" onTouchEvent ACTION_UP");
    // break;
    // }
    // return super.onTouchEvent(ev);
    // }
    class AwesomePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mListViews.size();
        }

        /**
         * 从指定的position创建page
         *
         * @param position The page position to be instantiated.
         * @return 返回指定position的page，这里不需要是一个view，也可以是其他的视图容器.
         */
        @Override
        public Object instantiateItem(View collection, int position) {

            ((ViewPager) collection).addView(mListViews.get(position), 0);

            return mListViews.get(position);
        }

        /**
         * <span style="font-family:'Droid Sans';">从指定的position销毁page</span>
         *
         *
         * <span style="font-family:'Droid Sans';">参数同上</span>
         */
        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView(mListViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

    }

}
