package com.pagerindicator.tablayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Author: tone
 * Date: 2019/4/12 9:10 AM
 * Description:
 */
public class TabLayout extends HorizontalScrollView {

    public static final int MODE_SCROLLABLE = 0;//滑动
    public static final int MODE_FIXED = 1;//固定

    private static final int DEFAULT_HEIGHT = 48; // dp
    private static final int TAB_MIN_WIDTH_MARGIN = 6; //dps


    private int tabWidth;

    private LinearLayout mTabContainer;
    private ITabIndicator mIndicator;
    private TabAdapter adapter;

    private ViewPager mViewPager;
    private TabLayoutOnPageChangeListener onPageChangeListener;

    private int mode = MODE_SCROLLABLE;
    private int tabMargin;


    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            populate();
        }

        @Override
        public void onInvalidated() {

        }
    };


    public TabLayout(@NonNull Context context) {
        this(context, null);
    }

    public TabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
        tabMargin = 0;
    }


    private void initView(Context context) {
        removeAllViews();

        FrameLayout frameLayout = new FrameLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(frameLayout, params);

        if (adapter == null) {
            adapter = new DefaultAdapter();
        }
        adapter.registerDataSetObserver(dataSetObserver);

        //添加指示器
        mIndicator = adapter.getIndicator(getContext());
        if (mIndicator instanceof View) {
            frameLayout.addView((View) mIndicator, params);
        }

        //添加tab container
        mTabContainer = new LinearLayout(context);
        mTabContainer.setOrientation(LinearLayout.HORIZONTAL);
        frameLayout.addView(mTabContainer, params);

        if (mViewPager != null)
            adapter.setPagerAdapter(mViewPager.getAdapter());

        //添加tab
        populate();

    }

    private int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int idealHeight = dpToPx(DEFAULT_HEIGHT) + getPaddingTop() + getPaddingBottom();
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(idealHeight, MeasureSpec.getSize(heightMeasureSpec)), MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.UNSPECIFIED:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(idealHeight, MeasureSpec.EXACTLY);
                break;
        }
        tabWidth = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() > 0) {
            View child = getChildAt(0);
            if (child.getMeasuredWidth() != getMeasuredWidth() && mode == MODE_FIXED) {
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), child.getLayoutParams().height);
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }


    public ITabIndicator getIndicator() {
        return mIndicator;
    }

    public void setupWithViewPager(@Nullable ViewPager viewPager) {

        if (mViewPager != null && onPageChangeListener != null) {
            mViewPager.removeOnPageChangeListener(onPageChangeListener);
        }
        if (viewPager != null) {
            mViewPager = viewPager;
            if (onPageChangeListener == null)
                onPageChangeListener = new TabLayoutOnPageChangeListener();
            mViewPager.addOnPageChangeListener(onPageChangeListener);
        }
        if (adapter != null)
            adapter.setPagerAdapter(viewPager.getAdapter());

        notifyDataSetChanged();
    }

    public void setAdapter(TabAdapter adapter) {
        this.adapter = adapter;
        initView(getContext());
    }


    private void populate() {
        if (mTabContainer != null) {
            mTabContainer.removeAllViews();
            //添加title
            int count = adapter.getCount();
            LinearLayout.LayoutParams tabParams;
            for (int i = 0; i < count; i++) {
                ITabView view = adapter.getTabView(getContext(), i);
                if (view instanceof View) {
                    if (mode == MODE_FIXED) {
                        tabParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                        tabParams.weight = 1;
                        tabParams.gravity = Gravity.CENTER_VERTICAL;
                    } else {
                        tabParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        tabParams.gravity = Gravity.CENTER_VERTICAL;
                        tabParams.leftMargin = dpToPx(tabMargin);
                        tabParams.rightMargin = dpToPx(tabMargin);
                    }
                    mTabContainer.addView((View) view, tabParams);
                    final int finalI = i;
                    ((View) view).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mViewPager != null) {
                                mViewPager.setCurrentItem(finalI);
                            }
                        }
                    });
                }
            }
        }
    }

    public void notifyDataSetChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }


    public void setMode(int mode) {
        this.mode = mode;
        applyMode();
    }

    private void applyMode() {
        switch (mode) {
            case MODE_FIXED:
                mTabContainer.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
            case MODE_SCROLLABLE:
                mTabContainer.setGravity(GravityCompat.START);
                break;
        }
        updateTabViews(true);
    }

    void updateTabViews(final boolean requestLayout) {
        for (int i = 0; i < mTabContainer.getChildCount(); i++) {
            View child = mTabContainer.getChildAt(i);
            updateTabViewLayoutParams((LinearLayout.LayoutParams) child.getLayoutParams());
            if (requestLayout) {
                child.requestLayout();
            }
        }
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
        if (mode == MODE_FIXED) {
            lp.width = 0;
            lp.weight = 1;
        } else {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            lp.weight = 0;
        }
    }

    class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private float mLastPositionOffsetSum;
        private int mScrollState;
        private float mScrollStartX = 0.5f;//启动滑动因子，默认到达中心开始滑动
        private int mLastPosition;
        private ITabView selectedTabView;


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int count = adapter.getCount();
            if (position >= 0 && position < count) {

                int currentPosition = Math.min(count - 1, position);
                int nextPosition = Math.min(count - 1, position + 1);

                View curView = mTabContainer.getChildAt(currentPosition);
                View nextView = mTabContainer.getChildAt(nextPosition);

                if (curView == null || nextView == null)
                    return;

                if (curView instanceof ITabView && nextView instanceof ITabView) {
                    ITabView tabView = (ITabView) curView;
                    float scrollTo = tabView.getCenterX() - tabWidth * mScrollStartX;
                    ITabView tabView2 = (ITabView) nextView;
                    float nextScrollTo = tabView2.getCenterX() - tabWidth * mScrollStartX;

                    scrollTo((int) (scrollTo + (nextScrollTo - scrollTo) * positionOffset), 0);
                    //滑动指示器
                    if (mIndicator != null) {
                        mIndicator.onScrolled(tabView, tabView2);
                        mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }


                    float currentPositionOffsetSum = position + positionOffset;
                    boolean leftToRight = mLastPositionOffsetSum <= currentPositionOffsetSum;

                    if (mScrollState != ViewPager.SCROLL_STATE_IDLE) {
                        if (currentPositionOffsetSum == mLastPositionOffsetSum || currentPosition == nextPosition) {
                            return;
                        }

                        //左滑
                        if (leftToRight) {
                            //tabView是当前选中的view
                            tabView.onScrolled(currentPosition, positionOffset, ITabView.ORIENTATION_LEFT);
                            tabView2.onScrolled(nextPosition, 1f - positionOffset, ITabView.ORIENTATION_LEFT);
                        } else {
                            //tabView2是当前选中的view
                            tabView.onScrolled(currentPosition, positionOffset, ITabView.ORIENTATION_RIGHT);
                            tabView2.onScrolled(nextPosition, 1f - positionOffset, ITabView.ORIENTATION_RIGHT);
                        }
                    } else {
                        onPageSelected(currentPosition);
                    }
                    mLastPositionOffsetSum = currentPositionOffsetSum;
                }
            }


        }

        @Override
        public void onPageSelected(int position) {

            int count = adapter.getCount();
            if (position >= 0 && position < count) {
                for (int i = 0; i < count; i++) {
                    View view = mTabContainer.getChildAt(i);
                    if (view instanceof ITabView) {
                        ITabView tabView = (ITabView) view;
                        if (position == i) {
                            tabView.onScrolled(i, 0, ITabView.ORIENTATION_LEFT);
                            tabView.onSelected(i);
                            selectedTabView = tabView;
                        } else {
                            tabView.onScrolled(i, 1f, ITabView.ORIENTATION_LEFT);
                            tabView.onUnselected(i);
                        }
                    }
                }

                mLastPosition = position;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }
    }

}
