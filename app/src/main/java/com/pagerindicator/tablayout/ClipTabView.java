package com.pagerindicator.tablayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Author: tone
 * Date: 2019/4/12 3:54 PM
 * Description:
 */
public class ClipTabView extends View implements ITabView {


    private ColorStateList mTextColor;
    private CharSequence mText = "";

    private float textSize;

    private float positionOffset;
    private int orientation;
    private Paint mPaint;


    private Interpolator mStartInterpolator = new AccelerateInterpolator();
    private Interpolator mEndInterpolator = new DecelerateInterpolator();

    public ClipTabView(Context context) {
        this(context, null);
    }

    public ClipTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textSize = TypedValue.applyDimension(COMPLEX_UNIT_SP, 14f, getResources().getDisplayMetrics());

        mPaint.setTextSize(textSize);
        mTextColor = createColorStateList(Color.BLACK, Color.WHITE);

        mPaint.setColor(getNormalColor());

        int padding = dpToPx(6);
        setPadding(padding * 3, padding, padding * 3, padding);
    }

    private int dpToPx(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch (measureWidthMode) {
            case MeasureSpec.AT_MOST:
                measureWidth = (int) Math.min(measureWidth, mPaint.measureText(mText.toString()) + getPaddingLeft() + getPaddingRight());
                break;
            case MeasureSpec.UNSPECIFIED:
                measureWidth = (int) (mPaint.measureText(mText.toString()) + getPaddingLeft() + getPaddingRight());
                break;
        }
        switch (measureHeightMode) {
            case MeasureSpec.AT_MOST:
                measureHeight = (int) Math.min(measureHeight, (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent) + getPaddingTop() + getPaddingBottom());
                break;
            case MeasureSpec.UNSPECIFIED:
                measureHeight = (int) ((mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent) + getPaddingTop() + getPaddingBottom());
                break;
        }
        setMeasuredDimension(measureWidth, measureHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int width = (int) mPaint.measureText(mText.toString());
        int x = getWidth() / 2 - width / 2;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int y = (int) ((getHeight() - fontMetrics.bottom - fontMetrics.top) / 2);

        mPaint.setColor(getNormalColor());
        canvas.drawText(mText.toString(), x, y, mPaint);


        canvas.save();

        int left = x;
        int right = left + width;

        if (orientation == ORIENTATION_RIGHT) {
            if (!isSelected()) {
                canvas.clipRect(left + width * mEndInterpolator.getInterpolation(positionOffset), 0, right, getHeight());
            } else {
                canvas.clipRect(left , 0, left+width * mStartInterpolator.getInterpolation(1f-positionOffset), getHeight());
            }
        } else {
            if (isSelected()) {
                canvas.clipRect(left + width * mEndInterpolator.getInterpolation(positionOffset), 0, right, getHeight());
            } else {
                canvas.clipRect(left , 0, left+width * mStartInterpolator.getInterpolation(1f-positionOffset), getHeight());
            }
        }

        mPaint.setColor(getSelectedColor());
        canvas.drawText(mText.toString(), x, y, mPaint);

        canvas.restore();
    }

    @Override
    public void onSelected(int index) {

        setSelected(true);
    }

    @Override
    public void onUnselected(int index) {

        setSelected(false);
    }

    @Override
    public int getContentLeft() {
        return (int) (getCenterX() - mPaint.measureText(mText.toString()) / 2);
    }


    @Override
    public int getContentTop() {
        return (int) (getCenterY() - (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent) / 2);
    }

    @Override
    public int getContentRight() {
        return (int) (getCenterX() + mPaint.measureText(mText.toString()) / 2);
    }

    @Override
    public int getContentBottom() {
        return (int) (getCenterY() + (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent) / 2);
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
        this.positionOffset = positionOffset;
        this.orientation = orientation;
        postInvalidate();
    }


    public void setText(CharSequence mText) {
        this.mText = mText;
        requestLayout();
    }

    public void setTextColor(int mColor) {
        mTextColor = ColorStateList.valueOf(mColor);
        invalidate();
    }


    public void setTextColor(ColorStateList colors) {
        mTextColor = colors;
        invalidate();
    }

    private int getNormalColor() {
        return mTextColor.getColorForState(EMPTY_STATE_SET, 0);
    }

    private int getSelectedColor() {
        return mTextColor.getColorForState(SELECTED_STATE_SET, 0);
    }

    private ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        int i = 0;

        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i++;

        // Default enabled state
        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        i++;

        return new ColorStateList(states, colors);
    }

}
