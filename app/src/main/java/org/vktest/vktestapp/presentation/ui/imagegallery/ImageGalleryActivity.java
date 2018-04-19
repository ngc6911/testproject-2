package org.vktest.vktestapp.presentation.ui.imagegallery;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.presentation.base.BaseActivity;
import org.vktest.vktestapp.presentation.base.Utils;

public class ImageGalleryActivity extends BaseActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment == null) {
            Utils.addFragmentToActivity(getFragmentManager(), ImageGalleryFragment.newInstance(),
                    R.id.content_frame);
        }
    }

}
