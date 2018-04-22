package org.vktest.vktestapp.presentation.ui.imageviewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.base.BaseActivity;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.presenters.ImageGalleryPresenter;
import org.vktest.vktestapp.presentation.presenters.ImageViewerPresenter;
import org.vktest.vktestapp.presentation.ui.adapter.ImageViewerPagerAdapter;
import org.vktest.vktestapp.presentation.ui.imagegallery.ImageGalleryView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageViewerActivity extends BaseActivity implements ImageViewerView{

    @BindView(R.id.pager)
    ViewPager pager;

    @Inject
    BitmapHelper bitmapHelper;

    @InjectPresenter
    ImageViewerPresenter mImageGalleryPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_layout);
        TestApp.getsAppComponent().inject(this);
        ButterKnife.bind(this);

        ImageViewerPagerAdapter pagerAdapter = new ImageViewerPagerAdapter(
                bitmapHelper,
                getSupportFragmentManager());

        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(bitmapHelper.getCurrentPosition());
    }

    @Override
    public void renderDatasetChanges() {
        if (pager.getAdapter() != null) {
            pager.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void renderPhoto(Photo photo) {
    }
}
