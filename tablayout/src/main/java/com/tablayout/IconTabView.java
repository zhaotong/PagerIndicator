package com.tablayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Author: tone
 * Date: 2019/4/12 3:54 PM
 * Description:
 */
public class IconTabView extends LinearLayout implements ITabView {


    public static final int ICON_GRAVITY_TOP = 3;  // 上边
    public static final int ICON_GRAVITY_BOTTOM = 2;  // 下边
    public static final int ICON_GRAVITY_LEFT = 0;  // 下边
    public static final int ICON_GRAVITY_RIGHT = 1;  // 下边


    protected int mSelectedColor;
    protected int mNormalColor;
    private float mMinScale = 0.75f;
    private int iconPadding;


    private ImageView icon;
    private TextView text;
    private CharSequence mText;


    private int iconGravity;//图标位置

    public IconTabView(Context context) {
        this(context, null);
    }

    public IconTabView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        int padding = dpToPx(6);
        setPadding(padding * 3, padding, padding * 3, padding);

        mSelectedColor = Color.WHITE;
        mNormalColor = Color.BLACK;

        iconPadding = dpToPx(4);
        iconGravity = ICON_GRAVITY_LEFT;

        if (iconGravity == ICON_GRAVITY_LEFT || iconGravity == ICON_GRAVITY_RIGHT)
            setOrientation(HORIZONTAL);
        else {
            setOrientation(VERTICAL);
        }
        setGravity(Gravity.CENTER);
        removeAllViews();

        icon = new ImageView(context);
        icon.setScaleType(ImageView.ScaleType.CENTER);
        LayoutParams layoutParams = new LayoutParams(dpToPx(18), dpToPx(18));
        if (iconGravity == ICON_GRAVITY_LEFT) {
            layoutParams.rightMargin = iconPadding;
        } else if (iconGravity == ICON_GRAVITY_RIGHT) {
            layoutParams.leftMargin = iconPadding;
        } else if (iconGravity == ICON_GRAVITY_TOP) {
            layoutParams.bottomMargin = iconPadding;
        } else if (iconGravity == ICON_GRAVITY_BOTTOM) {
            layoutParams.topMargin = iconPadding;
        }

        addView(icon, layoutParams);

        text = new TextView(context);
        addView(text);

    }

    private int dpToPx(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
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
        if (iconGravity == ICON_GRAVITY_BOTTOM || iconGravity == ICON_GRAVITY_TOP) {
            return getCenterX() - Math.max(text.getWidth(), icon.getWidth()) / 2;
        }
        return getCenterX() - (text.getWidth() + icon.getWidth() + iconPadding) / 2;
    }

    @Override
    public int getContentTop() {
        if (iconGravity == ICON_GRAVITY_BOTTOM || iconGravity == ICON_GRAVITY_TOP) {
            return getCenterY() - (text.getHeight() + icon.getHeight() + iconPadding) / 2;
        }
        return getCenterY() - Math.min(text.getHeight(), icon.getHeight()) / 2;
    }

    @Override
    public int getContentRight() {
        if (iconGravity == ICON_GRAVITY_BOTTOM || iconGravity == ICON_GRAVITY_TOP) {
            return getCenterX() + Math.max(text.getWidth(), icon.getWidth()) / 2;
        }
        return getCenterX() + (text.getWidth() + icon.getWidth() + iconPadding) / 2;
    }

    @Override
    public int getContentBottom() {
        if (iconGravity == ICON_GRAVITY_BOTTOM || iconGravity == ICON_GRAVITY_TOP) {
            return getCenterY() + (text.getHeight() + icon.getHeight() + iconPadding) / 2;
        }
        return getCenterY() + Math.min(text.getHeight(), icon.getHeight()) / 2;
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
        text.setTextColor(color);
//        icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);

    }

    public void setImageBitmap(Bitmap bitmap) {
        icon.setImageBitmap(bitmap);
    }

    public void setImageDrawable(Drawable drawable) {
        icon.setImageDrawable(drawable);
    }

    public void setImageResource(int resId) {
        icon.setImageResource(resId);
    }

    public void setImageURI(Uri uri) {
        icon.setImageURI(uri);
    }

    public void setText(CharSequence mText) {
        this.mText = mText;
        text.setText(mText);
        requestLayout();
    }

    public void setIconPadding(int iconPadding) {
        this.iconPadding = iconPadding;
        LayoutParams layoutParams = (LayoutParams) icon.getLayoutParams();
        if (iconGravity == ICON_GRAVITY_LEFT) {
            layoutParams.rightMargin = iconPadding;
        } else if (iconGravity == ICON_GRAVITY_RIGHT) {
            layoutParams.leftMargin = iconPadding;
        } else if (iconGravity == ICON_GRAVITY_TOP) {
            layoutParams.bottomMargin = iconPadding;
        } else if (iconGravity == ICON_GRAVITY_BOTTOM) {
            layoutParams.topMargin = iconPadding;
        }
        icon.setLayoutParams(layoutParams);
    }

    public void setIconGravity(int iconGravity) {
        this.iconGravity = iconGravity;
        if (iconGravity == ICON_GRAVITY_LEFT || iconGravity == ICON_GRAVITY_RIGHT)
            setOrientation(HORIZONTAL);
        else {
            setOrientation(VERTICAL);
        }
        requestLayout();
    }

    public void setScale(float scale) {
        this.mMinScale = scale;
    }
}
