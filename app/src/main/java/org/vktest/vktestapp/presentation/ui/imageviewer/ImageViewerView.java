package org.vktest.vktestapp.presentation.ui.imageviewer;

import android.graphics.Bitmap;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ImageViewerView extends MvpView{
    void renderBitmap(Bitmap bitmap);
}
