package org.vktest.vktestapp.presentation.base;

import android.support.annotation.CallSuper;
import android.widget.Toast;

import com.arellomobile.mvp.MvpActivity;

public abstract class BaseActivity extends MvpActivity implements BaseView{

    @CallSuper
    @Override
    public void renderError(int errStringId) {
        Toast.makeText(this, errStringId, Toast.LENGTH_SHORT).show();
    }
}
