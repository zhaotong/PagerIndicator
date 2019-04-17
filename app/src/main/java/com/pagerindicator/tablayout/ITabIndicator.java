package com.pagerindicator.tablayout;

public interface ITabIndicator {

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    void onScrolled(ITabView view, ITabView nextView);

    void setColor(int colors[]);
}
