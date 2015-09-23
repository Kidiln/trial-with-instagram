package com.jcb.instalist.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *
 * Pager adapter showing fragments. Fragments for Images and Videos
 * Created by jacobkoikkara on 9/21/15.
 */
public class AppPagerAdapter extends FragmentPagerAdapter {

    private static final int COUNT = 2;

    public AppPagerAdapter(FragmentManager fragmentManager) {

        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return PictureFragment.newInstance();
            case 1:
                return VideoFragment.newInstance();
            default:
                return PictureFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
