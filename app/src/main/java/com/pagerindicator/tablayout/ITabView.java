package com.pagerindicator.tablayout;


public interface ITabView {

    public final int ORIENTATION_LEFT = 0;
    public final int ORIENTATION_RIGHT = 1;

    void onSelected(int index);

    void onUnselected(int index);

    //positionOffset>0 leftToRight
    void onScrolled(int position, float positionOffset, int orientation);

    int getContentLeft();

    int getContentTop();

    int getContentRight();

    int getContentBottom();

    int getLeft();

    int getTop();

    int getRight();

    int getBottom();

    int getCenterX();

    int getCenterY();

    int getWidth();

    int getHeight();

}
