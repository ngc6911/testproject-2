package org.vktest.vktestapp.data;

import android.app.Activity;
import android.content.Intent;

import org.vktest.vktestapp.presentation.models.Photo;

import java.util.List;

public interface Repository {

    interface AutorizationCallback{
        void onSuccess();
        void onPending(int pendingMessageResId);
        void onError();
    }

    interface GetPhotosCallback {
        void onSuccess(List<Photo> photos);
        void onSuccess(Photo photos);
        void onError();
    }

    void autrorize(Activity activity);
    void checkAuthorizationState(AutorizationCallback callback);
    void checkAuthorizationResult(int requestCode, int resultCode, Intent data,
                                  AutorizationCallback callback);
    void getPhotos(GetPhotosCallback getPhotosCallback);

    void getPhotos(Photo lastPhoto, GetPhotosCallback getPhotosCallback);

}
