package org.vktest.vktestapp.presentation.ui.imageviewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.base.BaseActivity;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.ui.imagegallery.ImageGalleryView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageViewerActivity extends BaseActivity implements ImageGalleryView{

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.photo_info_block)
    View photoInfoBlock;

    @BindView(R.id.txv_image_date)
    TextView txvImageDate;

    @BindView(R.id.txv_image_description)
    TextView txvImageDescription;

    @BindView(R.id.txv_likes_and_reposts)
    TextView txvLikesAndReposts;

    @Inject
    BitmapHelper bitmapHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_layout);
        TestApp.getsAppComponent().inject(this);
        ButterKnife.bind(this);
        ScreenSlidePagerAdapter screenSlidePagerAdapter =
                new ScreenSlidePagerAdapter(getSupportFragmentManager());

        pager.setAdapter(screenSlidePagerAdapter);
        pager.setCurrentItem(bitmapHelper.getCurrentPosition());
    }

    @Override
    public void renderPhoto(Photo photo) {
        txvImageDate.setText(photo.getFormattedDate());
        if(photo.hasText()){
            txvImageDescription.setVisibility(View.VISIBLE);
            txvImageDescription.setText(photo.getText());
        }

        String template = getResources().getString(R.string.txv_likes_and_reposts_template);
        txvLikesAndReposts.setText(String.format(template,
                photo.getLikesCount(), photo.getRepostsCount()));
    }

    @OnClick(R.id.photo_info_block)
    public void toggleInfo(View v){
        if(photoInfoBlock.getVisibility() == View.VISIBLE){
            photoInfoBlock.setVisibility(View.GONE);
        } else {
            photoInfoBlock.setVisibility(View.VISIBLE);
        }
    }

    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return ImageViewerFragment.newInstance();
        }

        @Override
        public int getCount() {
            return bitmapHelper.getPhotosList().size();
        }
    }

}
