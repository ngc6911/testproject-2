package org.vktest.vktestapp.presentation.presenters;

import com.arellomobile.mvp.InjectViewState;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.Repository;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.ui.imagegallery.ImageGalleryView;

import javax.inject.Inject;

@InjectViewState
public class ImageGalleryPresenter extends BasePresenter<ImageGalleryView> {

    @Inject
    Repository mRepository;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        TestApp.getsAppComponent().inject(this);
        getPhotos(null);
    }

    public void getPhotos(Photo lastPhoto){
        mRepository.getPhotos(lastPhoto, new Repository.GetPhotosCallback() {
            @Override
            public void onSuccess(Photo photos) {
                getViewState().renderPhoto(photos);
            }

            @Override
            public void onError() {
                getViewState().renderError(R.string.err_toast_txt_common);
            }
        });
    }
}
