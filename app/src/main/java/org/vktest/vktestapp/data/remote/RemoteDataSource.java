package org.vktest.vktestapp.data.remote;

import android.graphics.Bitmap;

import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKPhoto;
import org.vktest.vktestapp.data.remote.api.VKPhotosList;

public interface RemoteDataSource {

    int PHOTOS_FETCH_COUNT = 20;
    int ALBUMS_FETCH_COUNT = 5;

    interface GetAlbumsCallback {
        void onSuccess(VKAlbumsList albumsList);
        void onError();
    }

    interface GetInitialDataCallback {
        void onAlbumsFetched(VKAlbumsList albumsList);
        void onPhotoFetched(VKPhoto photo, Bitmap thumb_bitmap);
        void onFinish();
        void onError();
    }

    void getAlbums(Integer offset, GetAlbumsCallback albumsCallback);

    void loadInitialData(VKPhoto.SizeClass sizeClass, GetInitialDataCallback callback);

    void getPhotos(GetPhotosCallback callback);

    void getPhotos(long albumId, int offset, GetPhotosCallback callback);

    void fetchBitmap(VKPhoto vkPhoto, FetchPhotoCallback callback);

    interface GetPhotosCallback {
        void onSuccess(VKPhotosList photos);
        void onError();
    }

    interface FetchPhotoCallback {
        void onSuccess(VKPhoto photo, Bitmap photoBitmap);
        void onError();
    }
}
