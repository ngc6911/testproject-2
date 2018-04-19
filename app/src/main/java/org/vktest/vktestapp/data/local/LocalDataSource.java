package org.vktest.vktestapp.data.local;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;

public interface LocalDataSource {

    interface PhotoCallback{
        void onSuccess(PhotoEntity photoEntity, Bitmap smallBitmap);
        void onNetworkPhotosFetchRequired(long albumId, int alreadyFetchedCount);
        void onNetworkAlbumsFetchRequired(int alreadyFetchedCount);
        void onError();
    }

    interface GetPhotoCallback{
        void onSuccess(Bitmap bitmap);
        void onError();
    }

    interface Callback{
        void onSuccess();
        void onError();
    }

    void putAlbums(AlbumEntity... albumEntities);

    void putPhoto(PhotoEntity photoEntity, @Nullable Bitmap smallBitmap,
                  @Nullable Bitmap largeBitmap, @Nullable Callback callback);

    void getPhoto(String path, GetPhotoCallback callback);

    void getGalleryPhotos(long albumId, long latestPhotoId, PhotoCallback callback);
    void getGalleryPhotos(PhotoCallback callback);
}
