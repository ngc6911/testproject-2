package org.vktest.vktestapp.presentation.presenters;

import com.arellomobile.mvp.InjectViewState;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.Repository;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.ui.imagegallery.ImageGalleryView;

import javax.inject.Inject;

@InjectViewState
public class ImageGalleryPresenter extends BasePresenter<ImageGalleryView> {

    @Inject
    BitmapHelper mBitmapHelper;

    @Inject
    Repository mRepository;

    public ImageGalleryPresenter() {
        TestApp.getsAppComponent().inject(this);
        mBitmapHelper.addOnHelperDatasetChangesListener(() -> getViewState().renderDatasetChanges());
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getPhotos(null);
    }

    public void getPhotos(Photo lastPhoto){
        mRepository.getPhotos(lastPhoto, new Repository.GetPhotosCallback() {
            @Override
            public void onSuccess(Photo photo) {
                mBitmapHelper.addPhoto(photo);
                getViewState().renderPhoto(photo);
            }

            @Override
            public void onError() {
                getViewState().renderError(R.string.err_toast_txt_common);
            }
        });
    }
}
