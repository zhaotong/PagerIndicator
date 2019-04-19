package com.tablayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;

/**
 * Author: tone
 * Date: 2019/4/12 3:54 PM
 * Description:
 */
public class DefaultTabView extends AppCompatTextView implements ITabView {

    protected int mSelectedColor;
    protected int mNormalColor;

    private float mMinScale = 1f;

    public DefaultTabView(Context context) {
        this(context, null);
    }

    public DefaultTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setGravity(Gravity.CENTER);
        int padding = (int) (getResources().getDisplayMetrics().density * 6);
        setPadding(padding * 3, padding, padding * 3, padding);


        ColorStateList colors = ColorStateList.valueOf(getResources().getColor(R.color.tabs_text_color_selector));

        mSelectedColor = colors.getColorForState(SELECTED_STATE_SET, colors.getColorForState(getDrawableState(),0));
        mNormalColor = colors.getDefaultColor();

    }

    @Override
    public void onSelected(int index) {
        setTextColor(mSelectedColor);
        setSelected(true);
    }

    @Override
    public void onUnselected(int index) {
        setTextColor(mNormalColor);
        setSelected(false);
    }

    @Override
    public int getContentLeft() {
        return getLeft() + getPaddingLeft();
    }

    @Override
    public int getContentTop() {

        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getCenterY() - contentHeight / 2);

    }

    @Override
    public int getContentRight() {
        return getRight() - getPaddingRight();
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getCenterY() + contentHeight / 2);
    }

    @Override
    public int getCenterX() {
        return (int) (getLeft() + (getRight() - getLeft()) / 2f);
    }

    @Override
    public int getCenterY() {
        return (int) (getTop() + (getBottom() - getTop()) / 2f);
    }


    @Override
    public void onScrolled(int position, float positionOffset, int orientation) {
        setScaleY(1.0f + (mMinScale - 1.0f) * positionOffset);
        setScaleX(1.0f + (mMinScale - 1.0f) * positionOffset);
        int color = ArgbEvaluator.evaluator(positionOffset, mSelectedColor, mNormalColor);
        setTextColor(color);
    }


    @Override
    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            return;
        }
        super.setTextColor(colors);
        mSelectedColor = colors.getColorForState(SELECTED_STATE_SET, colors.getColorForState(getDrawableState(),0));
        mNormalColor = colors.getDefaultColor();
    }

    public void setTextColor(int mSelectedColor, int mNormalColor) {
        this.mSelectedColor = mSelectedColor;
        this.mNormalColor = mNormalColor;
    }

    public void setScale(float scale) {
        this.mMinScale = scale;
    }
}
