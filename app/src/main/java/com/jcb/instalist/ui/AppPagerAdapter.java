package com.jcb.instalist.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jcb.instalist.cache.ImageFetcher;

/**
 * Created by jacobkoikkara on 9/21/15.
 */
public class AppPagerAdapter extends FragmentPagerAdapter {

    private static final int COUNT = 2;

    private ImageFetcher mImageFetcher;

    public AppPagerAdapter(FragmentManager fragmentManager) {

        super(fragmentManager);
    }

    public AppPagerAdapter(FragmentManager fragmentManager, ImageFetcher imageFetcher) {

        super(fragmentManager);
        this.mImageFetcher = imageFetcher;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0 : return PictureFragment.newInstance(mImageFetcher);
            case 1 : return VideoFragment.newInstance(mImageFetcher);
            default: return PictureFragment.newInstance(mImageFetcher);
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
