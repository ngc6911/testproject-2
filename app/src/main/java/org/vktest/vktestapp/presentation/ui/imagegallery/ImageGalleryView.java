package org.vktest.vktestapp.presentation.ui.imagegallery;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.vktest.vktestapp.presentation.base.BaseView;
import org.vktest.vktestapp.presentation.models.Photo;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ImageGalleryView extends BaseView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void renderPhoto(Photo photo);

    void renderDatasetChanges(int position);
}
