package org.vktest.vktestapp.data;

import android.app.Activity;
import android.content.Intent;

import org.vktest.vktestapp.presentation.models.Photo;

import java.util.List;

public interface Repository {

    interface BaseCallback {
        void onError();
    }

    interface AutorizationCallback extends BaseCallback{
        void onSuccess();
        void onPending(int pendingMessageResId);
    }

    interface GetPhotosCallback extends BaseCallback {
        void onSuccess(Photo photos);
    }

    interface GetPhotosListCallback extends BaseCallback {
        void onSuccess(Photo photos);
    }

    void autrorize(Activity activity);
    void checkAuthorizationState(AutorizationCallback callback);
    void checkAuthorizationResult(int requestCode, int resultCode, Intent data,
                                  AutorizationCallback callback);

    void getFirstPhotos(GetPhotosListCallback getPhotosListCallback);
    void getNextPhotos(Photo lastPhoto, GetPhotosListCallback getPhotosListCallback);
    void getLargePhoto(Photo photo, GetPhotosCallback callback);
}
