package org.vktest.vktestapp.data;

import android.app.Activity;
import android.content.Intent;

import org.vktest.vktestapp.presentation.models.Photo;

public interface Repository {

    interface AutorizationCallback{
        void onSuccess();
        void onPending(int pendingMessageResId);
        void onError();
    }

    interface GetPhotosCallback {
        void onSuccess(Photo photos);
        void onError();
    }

    void autrorize(Activity activity);
    void checkAuthorizationState(AutorizationCallback callback);
    void checkAuthorizationResult(int requestCode, int resultCode, Intent data,
                                  AutorizationCallback callback);

    void getPhotos(Photo lastPhoto, GetPhotosCallback getPhotosCallback);
    void getLargePhoto(Photo photo, GetPhotosCallback callback);
}
