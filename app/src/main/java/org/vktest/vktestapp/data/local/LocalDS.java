package org.vktest.vktestapp.data.local;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.vktest.vktestapp.AppExecutors;
import org.vktest.vktestapp.data.local.db.ImagesDatabase;
import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.data.local.db.entities.relations.AlbumPhotos;
import org.vktest.vktestapp.data.local.storage.FileStorageType;
import org.vktest.vktestapp.data.local.storage.files.FileStorage;
import org.vktest.vktestapp.data.local.storage.settings.Settings;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalDS implements LocalDataSource {

    private FileStorage mFileStorage;
    private Settings mSettings;
    private ImagesDatabase mImagesDatabase;

    private AppExecutors mAppExecutors;

    @Inject
    public LocalDS(AppExecutors appExecutors, FileStorage fileStorage, Settings settings,
                   ImagesDatabase imagesDatabase) {

        mAppExecutors = appExecutors;
        mFileStorage = fileStorage;
        mSettings = settings;
        mImagesDatabase = imagesDatabase;

        if(mFileStorage.isExternalStorageReadable()
                && mFileStorage.isExternalStorageWritable()) {
            mSettings.setStorageMode(FileStorageType.External);
        }
    }

    @Override
    public void putAlbums(AlbumEntity... albumEntities) {
        mAppExecutors.getDiskIO().execute(() ->
                mImagesDatabase.albumsDao().putAlbums(albumEntities));
    }

    @Override
    public void putPhoto(PhotoEntity photoEntity, @Nullable Bitmap smallBitmap,
                         @Nullable Bitmap largeBitmap, @Nullable Callback callback) {
        mAppExecutors.getDiskIO().execute(() -> {
            mImagesDatabase.photosDao().addPhoto(photoEntity);
            try {
                if (largeBitmap != null) {
                    mFileStorage.putBitmap(largeBitmap,
                            photoEntity.getBigPhotoFilename(), mSettings.getStorageMode());
                }

                if(smallBitmap != null) {
                    mFileStorage.putBitmap(smallBitmap,
                            photoEntity.getSmallPhotoFilename(), mSettings.getStorageMode());
                }

                if (callback != null) {
                    callback.onSuccess();
                }
            } catch (IOException e) {
                e.printStackTrace();

                if (callback != null) {
                    callback.onError();
                }
            }
        });
    }

    @Override
    public void getGalleryPhotos(long albumId, long latestPhotoId, PhotoCallback callback) {
        mAppExecutors.getDiskIO().execute(() -> {
            AlbumPhotos albumPhotos =
                    mImagesDatabase.albumsDao().getAlbumById(albumId);

            if(albumPhotos.getNonfetchedCount() == 0){
                int albumsCount = mImagesDatabase.albumsDao().getAlbumsCount();
                mAppExecutors.getMainThread().execute(() ->
                        callback.onNetworkAlbumsFetchRequired(albumsCount));
                return;
            } else {
                mAppExecutors.getMainThread().execute(() ->
                        callback.onNetworkPhotosFetchRequired(albumId, albumPhotos.getPhotos().size()));
            }

            for(PhotoEntity entity: albumPhotos.getPhotos()){
                try {
                    Bitmap bitmap = mFileStorage.getBitmap(entity.getSmallPhotoFilename(),
                            mSettings.getStorageMode());
                    mAppExecutors.getMainThread().execute(() -> callback.onSuccess(entity, bitmap));
                } catch (IOException e) {
                    mAppExecutors.getMainThread().execute(callback::onError);
                    e.printStackTrace();
                }
            }
        });
    }
}
