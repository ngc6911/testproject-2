package org.vktest.vktestapp.data.local;

import android.graphics.Bitmap;

import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;

import java.util.List;

public interface LocalDataSource {

    int PHOTOS_PAGE_SIZE = 5;

    interface PhotoCallback{
        void onPhotoLoaded(List<PhotoEntity> photoEntities);
        void onNoLocalPhotos();
        void onError();
    }


    interface PhotosCallback{
        void onSuccess(PhotoEntity photoEntities);
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

    interface GetAlbumEntitiesCallback{
        void onSuccess(List<AlbumEntity> albumEntities);
        void onError();
    }

    void getPhoto(long id, String src, GetPhotoCallback callback);

    interface Callback{
        void onSuccess();
        void onError();
    }

    void putAlbums(Callback callback, AlbumEntity... albumEntities);

    void updateAlbum(long id, int fetched);

    void putPhoto(PhotoEntity photoEntity, GetPhotoEntityCallback callback);

    void getAlbums(GetAlbumEntitiesCallback albumEntitiesCallback);

    void getGalleryPhotos(long albumId, long latestPhotoId, PhotoCallback callback);
    void getGalleryPhotos(PhotoCallback callback);
}
