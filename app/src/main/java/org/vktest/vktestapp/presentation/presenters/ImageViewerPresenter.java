package org.vktest.vktestapp.presentation.presenters;

import com.arellomobile.mvp.InjectViewState;

import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.Repository;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.ui.imageviewer.ImageViewerView;

import javax.inject.Inject;

@InjectViewState
public class ImageViewerPresenter extends BasePresenter<ImageViewerView> {

    @Inject
    Repository repository;

    @Inject
    BitmapHelper bitmapHelper;

    public ImageViewerPresenter() {
        TestApp.getsAppComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        bitmapHelper.addOnHelperDatasetChangesListener(() -> getViewState().renderDatasetChanges());
    }

    public void getFullsizePhoto(){

        repository.getLargePhoto(bitmapHelper.getCurrentPhoto(), new Repository.GetPhotosCallback() {
            @Override
            public void onSuccess(Photo photos) {
                getViewState().renderPhoto(photos);
            }

            @Override
            public void onError() {

            }
        });
    }
}
