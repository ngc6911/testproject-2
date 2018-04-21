package org.vktest.vktestapp.presentation.ui.imagegallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.presentation.base.BaseActivity;
import org.vktest.vktestapp.presentation.base.Utils;

public class ImageGalleryActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment == null) {
            Utils.addFragmentToActivity(getSupportFragmentManager(), ImageGalleryFragment.newInstance(),
                    R.id.content_frame);
        }
    }

}
