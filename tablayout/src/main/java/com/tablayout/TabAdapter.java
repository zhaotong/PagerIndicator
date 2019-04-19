package com.tablayout;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;


public abstract class TabAdapter {


    protected PagerAdapter mPagerAdapter;

    public final DataSetObservable mDataSetObservable = new DataSetObservable();

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            notifyDataSetInvalidated();
        }
    };

    public TabAdapter() {

    }

    public PagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }


    public abstract int getCount();

    public abstract ITabView getTabView(Context context, int index);

    public abstract ITabIndicator getIndicator(Context context);


    public void setPagerAdapter(PagerAdapter adapter) {
        if (adapter != null) {
            mPagerAdapter = adapter;
            mPagerAdapter.registerDataSetObserver(dataSetObserver);
        }
    }

    public final void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public final void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public final void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public final void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }
}
