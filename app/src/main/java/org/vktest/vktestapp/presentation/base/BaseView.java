package org.vktest.vktestapp.presentation.base;

import com.arellomobile.mvp.MvpView;

public interface BaseView extends MvpView{
    void renderError(int errStringId);
}
