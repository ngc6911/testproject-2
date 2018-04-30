package org.vktest.vktestapp.presentation.presenters;

import com.arellomobile.mvp.InjectViewState;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.Repository;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.ui.imagegallery.ImageGalleryView;

import java.util.List;

import javax.inject.Inject;

@InjectViewState
public class ImageGalleryPresenter extends BasePresenter<ImageGalleryView>
        implements BitmapHelper.OnHelperDatasetChangesListener{

    @Inject
    BitmapHelper mBitmapHelper;

    @Inject
    Repository mRepository;

    public ImageGalleryPresenter() {
        TestApp.getsAppComponent().inject(this);
        mBitmapHelper.addOnHelperDatasetChangesListener(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mRepository.getFirstPhotos(new Repository.GetPhotosListCallback() {
            @Override
            public void onSuccess(Photo photos) {
                mBitmapHelper.addPhoto(photos);
            }

            @Override
            public void onError() {
                getViewState().renderError(R.string.err_toast_txt_common);
            }
        });
    }

    public void getPhotos(Photo lastPhoto){
        mRepository.getNextPhotos(lastPhoto, new Repository.GetPhotosListCallback() {
            @Override
            public void onSuccess(Photo photo) {
                mBitmapHelper.addPhoto(photo);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void onDatasetChanges(int position) {
        getViewState().renderDatasetChanges(position);
    }
}
