package org.vktest.vktestapp.data.remote;

import android.graphics.Bitmap;

import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKPhotosList;

public interface RemoteDataSource {

    int PHOTOS_FETCH_COUNT = 20;
    int ALBUMS_FETCH_COUNT = 5;

    interface GetAlbumsCallback {
        void onSuccess(VKAlbumsList albumsList);
        void onError();
    }

    void getAlbums(Integer offset, GetAlbumsCallback albumsCallback);

    void getPhotos(long albumId, int offset, GetPhotosCallback callback);

    void fetchBitmap(PhotoEntity entity, FetchPhotoCallback callback);

    interface GetPhotosCallback {
        void onSuccess(VKPhotosList photos);
        void onError();
    }

    interface FetchPhotoCallback {
        void onSuccess(Bitmap thumb, Bitmap fullsize);
        void onError();
    }
}
