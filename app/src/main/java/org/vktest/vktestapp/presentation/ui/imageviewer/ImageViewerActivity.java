package org.vktest.vktestapp.presentation.ui.imageviewer;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.presentation.base.BaseActivity;

public class ImageViewerActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_base_layout);
    }

}
