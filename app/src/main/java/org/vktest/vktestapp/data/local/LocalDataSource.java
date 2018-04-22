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
        void onSuccess(PhotoEntity entity, Bitmap bitmap);
        void onError();
    }


    interface GetPhotoEntityCallback{
        void onSuccess(PhotoEntity entity);
        void onError();
    }

    void getPhoto(long id, String src, GetPhotoCallback callback);

    interface Callback{
        void onSuccess();
        void onError();
    }

    void putAlbums(AlbumEntity... albumEntities);

    void putPhoto(PhotoEntity photoEntity, @Nullable Bitmap smallBitmap,
                  @Nullable Bitmap largeBitmap);

    void getGalleryPhotos(long albumId, long latestPhotoId, PhotoCallback callback);
}
