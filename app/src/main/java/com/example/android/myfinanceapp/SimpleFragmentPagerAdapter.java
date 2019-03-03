package com.example.android.myfinanceapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fragmentManager){
        super(fragmentManager);
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new GraphFragment();
            case 1:
                return new HistoryFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Graph";
            case 1:
                return "History";
            default:
                return null;
        }
    }
}
