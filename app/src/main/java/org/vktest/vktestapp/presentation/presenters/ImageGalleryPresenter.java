package org.vktest.vktestapp.presentation.presenters;

import com.arellomobile.mvp.InjectViewState;

import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.Repository;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.ui.imagegallery.ImageGalleryView;

import java.util.List;

import javax.inject.Inject;

@InjectViewState
public class ImageGalleryPresenter extends BasePresenter<ImageGalleryView> {

    @Inject
    Repository mRepository;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        TestApp.getsAppComponent().inject(this);
    }
}
