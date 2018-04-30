package org.vktest.vktestapp.data.remote;

import android.content.Context;
import android.graphics.Bitmap;

import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKPhoto;

import java.util.List;

public interface RemoteDataSource {

    int PHOTOS_FETCH_COUNT = 10;

    interface GetAlbumsCallback {
        void onSuccess(VKAlbumsList albumsList);
        void onError();
    }

    void getAlbums(GetAlbumsCallback albumsCallback);

    void getPhotos(List<AlbumEntity> albums, int totalPhotos,
                   Context context, GetPhotosListCallback callback);

    interface GetPhotosListCallback {
        void onNewPhoto(VKPhoto vkPhoto, Bitmap thumbBitmap, Bitmap fullsizeBitmap);
        void onAlbumFetchFinish(AlbumEntity album);
        void onError();
    }

    interface GetNewPhotosCallback {
        void onNewPhoto(VKPhoto vkPhoto, Bitmap thumbBitmap, Bitmap fullsizeBitmap);
        void onFinish();
        void onError();
    }
}
