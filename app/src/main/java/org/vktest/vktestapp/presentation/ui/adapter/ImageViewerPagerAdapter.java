package org.vktest.vktestapp.presentation.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.ui.imageviewer.ImageViewerFragment;

public class ImageViewerPagerAdapter extends FragmentStatePagerAdapter {

    private BitmapHelper mBitmapHelper;

    public ImageViewerPagerAdapter(BitmapHelper bitmapHelper, FragmentManager fm) {
        super(fm);
        mBitmapHelper = bitmapHelper;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageViewerFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mBitmapHelper.getPhotoCount();
    }

}
