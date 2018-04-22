package org.vktest.vktestapp.presentation.ui.imageviewer;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.vktest.vktestapp.presentation.base.BaseView;
import org.vktest.vktestapp.presentation.models.Photo;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ImageViewerView extends BaseView {
    void renderPhoto(Photo photo);
    void renderDatasetChanges();
}
