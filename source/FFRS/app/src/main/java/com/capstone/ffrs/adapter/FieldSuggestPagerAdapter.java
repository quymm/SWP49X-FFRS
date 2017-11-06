package com.capstone.ffrs.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.capstone.ffrs.fragment.FieldSearchFragment;
import com.capstone.ffrs.fragment.MatchSearchFragment;

public class FieldSuggestPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public FieldSuggestPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FieldSearchFragment();
        } else {
            return new MatchSearchFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Tìm sân";
            case 1:
                return "Tìm đối thủ";
            default:
                return null;
        }
    }

}