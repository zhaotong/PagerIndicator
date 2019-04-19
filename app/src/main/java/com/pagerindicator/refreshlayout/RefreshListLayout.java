package com.pagerindicator.refreshlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Author: tone
 * Date: 2019/4/18 10:49 AM
 * Description:
 */
public class RefreshListLayout extends SwipeRefreshLayout {
    public RefreshListLayout(@NonNull Context context) {
        super(context);
    }

    public RefreshListLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
