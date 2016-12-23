package com.example.testing.scrollerdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.util.ArrayList;

/**
 * 类ViewPager的滑动效果
 * Created by wangying on 2016/12/22.
 */

public class ExpendScrollerView extends LinearLayout {

    private static final String LOG_TAG = "ExpendScrollerView";

    private Scroller mScroller;

    // 当前View次序
    private int mCurrentChildViewOrder = 0;
    // 包含的子View的个数
    private int mChildViewCount = -1;
    // 是否为用户操作所引起的Scroller动画
    private boolean mIsRunByUser = false;

    private ArrayList<ScrollerChildViewLocation> mChildrenLocation = new ArrayList<ScrollerChildViewLocation>();

    public ExpendScrollerView(Context context) {
        this(context, null);
    }

    public ExpendScrollerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpendScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context, new AccelerateInterpolator(3000));
    }

    public void startScroll() {

        if (mCurrentChildViewOrder < (mChildrenLocation.size() - 1)) {
            mIsRunByUser = true;
            ScrollerChildViewLocation location = mChildrenLocation.get(mCurrentChildViewOrder);
            mScroller.startScroll(location.mLocationX, location.mLocationY, location.mWidth, 0);
            invalidate();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mChildViewCount = getChildCount();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < mChildViewCount; i++) {
            View childView = getChildAt(i);
            // 为ScrollerLayout中的每一个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            mChildrenLocation.clear();
            // 如果已经变化则重新获取各个子View的大小
            ScrollerChildViewLocation location;
            View view;
            for (int i = 0; i < mChildViewCount; i++) {
                view = getChildAt(i);
                location = new ScrollerChildViewLocation();
                location.mLocationX = view.getLeft();
                location.mLocationY = view.getTop();
                location.mWidth = view.getWidth();
                mChildrenLocation.add(location);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {
            int currentX = mScroller.getCurrX();
            int currentY = mScroller.getCurrY();
            Log.i(LOG_TAG, "x = " + currentX + " y = " + currentY);
            scrollTo(currentX, currentY);
            invalidate();
        } else {
            if (mCurrentChildViewOrder < mChildViewCount && mIsRunByUser) {
                mCurrentChildViewOrder++;
                mIsRunByUser = false;
            }
        }

    }
}
