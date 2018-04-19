package org.vktest.vktestapp.presentation.ui.welcomescreen;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.presentation.base.BaseActivity;
import org.vktest.vktestapp.presentation.base.Utils;
import org.vktest.vktestapp.presentation.presenters.WelcomeScreenPresenter;
import org.vktest.vktestapp.presentation.ui.imagegallery.ImageGalleryActivity;

public class WelcomeScreenActivity extends BaseActivity implements WelcomeScreenView{

    @InjectPresenter
    WelcomeScreenPresenter mWelcomeScreenPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_layout);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment == null) {
            Utils.addFragmentToActivity(getFragmentManager(), WelcomeScreenFragment.newInstance(),
                    R.id.content_frame);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWelcomeScreenPresenter.onVKApiResult(requestCode, resultCode, data);
    }

    @Override
    public void renderLoginSuccess() {
        Intent intent = new Intent(this, ImageGalleryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void renderLoginStart() {

    }

    @Override
    public void renderLoginEnd() {

    }
}
