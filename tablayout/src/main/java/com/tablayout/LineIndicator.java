package com.tablayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Author: tone
 * Date: 2019/4/12 5:34 PM
 * Description: 直线指示器
 */
public class LineIndicator extends View implements ITabIndicator {

    public static final int WIDTH_MODE_MATCH = 0;   // 直线宽度 == view宽度
    public static final int WIDTH_MODE_WRAP = 1;    // 直线宽度 == view内容宽度
    public static final int WIDTH_MODE_CUSTOM = 2;  // 直线宽度 == 默认

    public static final int GRAVITY_TOP = 1;  // 直线在上边
    public static final int GRAVITY_BOTTOM = 0;  // 直线在下边


    private int[] colors;
    private int widthMode;

    // 控制动画
    private Interpolator mStartInterpolator = new AccelerateInterpolator(3f);
    private Interpolator mEndInterpolator = new DecelerateInterpolator(3f);

    private float mIndicatorHeight;
    private float mIndicatorWidth;
    private float mRoundRadius;//圆角
    private int gravity;//TOP  BOTTOM
    private float mXOffset, mYOffset;

    private ITabView curView, nextView;

    private Paint paint;
    private RectF rect;

    public LineIndicator(Context context) {
        this(context, null);
    }

    public LineIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        int colorAccent = typedValue.data;
        if (colorAccent != 0) {
            colors = new int[]{colorAccent};
        } else
            colors = new int[]{Color.parseColor("#FFFF6447"), Color.parseColor("#FFFF2B67")};

        widthMode = WIDTH_MODE_MATCH;

        mIndicatorHeight = dpToPx(3);
        mIndicatorWidth = dpToPx(24);
        mRoundRadius = mIndicatorHeight / 2;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);

        rect = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIndicatorHeight == 0)
            return;
        canvas.drawRoundRect(rect, mRoundRadius, mRoundRadius, paint);

    }

    private int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    @Override
    public void setColor(int[] colors) {
        this.colors = colors;
        if (colors != null && colors.length > 0) {
            if (colors.length > 1) {
                float positions[] = new float[colors.length];
                Shader shader = new LinearGradient(0, 0, rect.width(), rect.height(), colors, positions, Shader.TileMode.CLAMP);
                paint.setShader(shader);
            } else {
                paint.setColor(colors[0]);
            }
        }
        postInvalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (curView == null || nextView == null)
            return;

        if (mIndicatorHeight == 0)
            return;

        float leftX;
        float nextLeftX;
        float rightX;
        float nextRightX;
        if (widthMode == WIDTH_MODE_MATCH) {
            leftX = curView.getLeft() + mXOffset;
            nextLeftX = nextView.getLeft() + mXOffset;
            rightX = curView.getRight() - mXOffset;
            nextRightX = nextView.getRight() - mXOffset;
        } else if (widthMode == WIDTH_MODE_WRAP) {
            leftX = curView.getContentLeft() + mXOffset;
            nextLeftX = nextView.getContentLeft() + mXOffset;
            rightX = curView.getContentRight() - mXOffset;
            nextRightX = nextView.getContentRight() - mXOffset;
        } else {

            mIndicatorWidth = Math.min(curView.getWidth(), mIndicatorWidth);
            mIndicatorWidth = Math.min(nextView.getWidth(), mIndicatorWidth);

            leftX = curView.getLeft() + (curView.getWidth() - mIndicatorWidth) / 2;
            nextLeftX = nextView.getLeft() + (nextView.getWidth() - mIndicatorWidth) / 2;
            rightX = leftX + mIndicatorWidth;
            nextRightX = nextLeftX + mIndicatorWidth;
        }

        rect.left = leftX + (nextLeftX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        rect.right = rightX + (nextRightX - rightX) * mEndInterpolator.getInterpolation(positionOffset);

        if (gravity == GRAVITY_BOTTOM) {
            rect.top = getHeight() - mIndicatorHeight - mYOffset;
            rect.bottom = getHeight() - mYOffset;
        } else {
            rect.bottom = getTop() + mIndicatorHeight + mYOffset;
            rect.top = getTop() + mYOffset;
        }


        if (colors != null && colors.length > 0) {
            if (colors.length > 1) {
                paint.reset();
                Shader shader = new LinearGradient(rect.left, 0, rect.right, 0, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(shader);
            } else {
                paint.reset();
                paint.setColor(colors[0]);
            }
        }

        postInvalidate();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onScrolled(ITabView view, ITabView nextView) {
        this.curView = view;
        this.nextView = nextView;
    }


    public void setOffset(float x, float y) {
        this.mXOffset = x;
        this.mYOffset = y;
    }

    public void setWidthMode(int widthMode) {
        this.widthMode = widthMode;
    }

    public void setIndicatorHeight(float mIndicatorHeight) {
        this.mIndicatorHeight = mIndicatorHeight;
    }


    public void setIndicatorWidth(float mIndicatorWidth) {
        if (widthMode != WIDTH_MODE_CUSTOM)
            return;
        this.mIndicatorWidth = mIndicatorWidth;
    }

    public void setStartInterpolator(Interpolator interpolator) {
        if (interpolator == null)
            mStartInterpolator = new AccelerateInterpolator();
        else
            this.mStartInterpolator = interpolator;
    }

    public void setEndInterpolator(Interpolator interpolator) {
        if (interpolator == null)
            mEndInterpolator = new DecelerateInterpolator();
        else
            this.mEndInterpolator = interpolator;
    }

    public void setRoundRadius(float mRoundRadius) {
        this.mRoundRadius = mRoundRadius;
        postInvalidate();
    }


    public void setGravity(int gravity) {
        this.gravity = gravity;
    }
}
