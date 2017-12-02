package com.capstone.ffrs.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.capstone.ffrs.fragment.PaidMatchFragment;
import com.capstone.ffrs.fragment.PendingRequestFragment;

/**
 * Created by HuanPMSE61860 on 10/30/2017.
 */

public class HistoryPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HistoryPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PaidMatchFragment();
        } else {
            return new PendingRequestFragment();
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
                return "Trận đấu đã đặt";
            case 1:
                return "Yêu cầu đang chờ";
            default:
                return null;
        }
    }
}
