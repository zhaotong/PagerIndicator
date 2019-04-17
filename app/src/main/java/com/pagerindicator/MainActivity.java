package com.pagerindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pagerindicator.tablayout.ArgbEvaluator;
import com.pagerindicator.tablayout.DefaultTabView;
import com.pagerindicator.tablayout.ITabIndicator;
import com.pagerindicator.tablayout.ITabView;
import com.pagerindicator.tablayout.IconTabView;
import com.pagerindicator.tablayout.LineIndicator;
import com.pagerindicator.tablayout.TabAdapter;
import com.pagerindicator.tablayout.TabLayout;

public class MainActivity extends AppCompatActivity {

    private String[] channels = new String[]{"HONEYCOMB", "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT"};


    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return channels.length;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                final TextView textView = new TextView(container.getContext());
                textView.setText(channels[position]);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(24);

                float startValue = 0f;
                float endValue = 1.0f;

                ValueAnimator animator = ValueAnimator.ofFloat(startValue, endValue);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        int color = ArgbEvaluator.evaluator(value, Color.WHITE, Color.BLACK);
                        textView.setTextColor(color);
//                        Log.d("MainActivity", "onAnimationUpdate:  value =  "+value+"  color  =  "+color);
                    }
                });
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.setDuration(3000);
                animator.start();

                container.addView(textView);
                return textView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return channels[position];
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return mPagerAdapter != null ? mPagerAdapter.getCount() : 0;
            }

            @Override
            public ITabView getTabView(Context context, int index) {
                DefaultTabView tabView = new DefaultTabView(context);
                tabView.setText(mPagerAdapter.getPageTitle(index));
                return tabView;
            }

            @Override
            public ITabIndicator getIndicator(Context context) {
                LineIndicator indicator=new LineIndicator(context);



                indicator.setWidthMode(LineIndicator.WIDTH_MODE_CUSTOM);
                indicator.setIndicatorWidth(dpToPx(18));


                return indicator;
            }
        });


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = viewPager.getCurrentItem();
                int count = viewPager.getAdapter().getCount();
                if (cur + 1 < count - 1) {
                    viewPager.setCurrentItem(cur + 1);
                } else {
                    viewPager.setCurrentItem(0);
                }
            }
        });
    }

    private int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }
}
