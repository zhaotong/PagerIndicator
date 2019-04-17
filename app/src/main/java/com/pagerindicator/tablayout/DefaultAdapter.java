package com.pagerindicator.tablayout;

import android.content.Context;

/**
 * Author: tone
 * Date: 2019/4/12 10:39 AM
 * Description:
 */
public class DefaultAdapter extends TabAdapter {

    @Override
    public int getCount() {
        return mPagerAdapter != null ? mPagerAdapter.getCount() : 0;
    }

    @Override
    public ITabView getTabView(Context context, int index) {
        DefaultTabView tabView = new DefaultTabView(context);
//        IconTabView tabView= new IconTabView(context);
//        ClipTabView tabView= new ClipTabView(context);
        tabView.setText(mPagerAdapter.getPageTitle(index));
        return tabView;
    }

    @Override
    public ITabIndicator getIndicator(Context context) {
//        return new LineIndicator(context);
        return new RoundIndicator(context);
    }
}
