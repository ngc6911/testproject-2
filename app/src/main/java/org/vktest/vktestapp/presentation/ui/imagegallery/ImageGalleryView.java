package org.vktest.vktestapp.presentation.ui.imagegallery;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import org.vktest.vktestapp.presentation.base.BaseView;
import org.vktest.vktestapp.presentation.models.Photo;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ImageGalleryView extends BaseView {
    void renderPhotos(List<Photo> photos);
}
