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
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Author: tone
 * Date: 2019/4/12 5:34 PM
 * Description: 指示器
 */
public class RoundIndicator extends View implements ITabIndicator {


    private int[] colors;


    // 控制动画
    private Interpolator mStartInterpolator = new AccelerateInterpolator();
    private Interpolator mEndInterpolator = new DecelerateInterpolator();


    private int mVerticalPadding;
    private int mHorizontalPadding;
    private float mRoundRadius;//圆角
    private boolean useShadow = true;

    private ITabView curView, nextView;

    private Paint paint;
    private RectF rect;

    public RoundIndicator(Context context) {
        this(context, null);
    }

    public RoundIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        colors = new int[]{Color.parseColor("#FFFF6447"), Color.parseColor("#FFFF2B67")};

        mVerticalPadding = dpToPx(4);
        mHorizontalPadding = dpToPx(12);


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);


        rect = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (useShadow) {
            //关闭硬件加速 绘制阴影
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            paint.setShadowLayer(30, 0, 15, Color.parseColor("#FFA35065"));
        }
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
                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            } else {
                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(colors[0]);
            }
        }
        postInvalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (curView == null || nextView == null)
            return;


        rect.left = curView.getContentLeft() - mHorizontalPadding + (nextView.getContentLeft() - curView.getContentLeft()) * mStartInterpolator.getInterpolation(positionOffset);
        rect.top = curView.getContentTop() - mVerticalPadding;
        rect.right = curView.getContentRight() + mHorizontalPadding + (nextView.getContentRight() - curView.getContentRight()) * mEndInterpolator.getInterpolation(positionOffset);
        rect.bottom = curView.getContentBottom() + mVerticalPadding;


        mRoundRadius = rect.height() / 2;

        if (colors != null && colors.length > 0) {
            if (colors.length > 1) {
                paint.reset();
                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
                Shader shader = new LinearGradient(rect.left, 0, rect.right, 0, colors, null, Shader.TileMode.CLAMP);
                paint.setShader(shader);
            } else {
                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
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

    public void setHorizontalPadding(int padding) {
        this.mHorizontalPadding = padding;
    }

    public void setVerticalPadding(int padding) {
        this.mVerticalPadding = padding;
    }

    public void setStartInterpolator(Interpolator interpolator) {
        if (interpolator == null)
            mStartInterpolator = new AccelerateInterpolator();
        else
            this.mStartInterpolator = interpolator;
    }

    public void setUseShadow(boolean useShadow) {
        this.useShadow = useShadow;
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
}
