package org.vktest.vktestapp.data.local;

import android.graphics.Bitmap;

import org.vktest.vktestapp.AppExecutors;
import org.vktest.vktestapp.data.local.db.ImagesDatabase;
import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.data.local.storage.FileStorageType;
import org.vktest.vktestapp.data.local.storage.files.FileStorage;
import org.vktest.vktestapp.data.local.storage.settings.Settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
    public void getPhoto(long id, String src, GetPhotoCallback callback) {
        mAppExecutors.getDiskIO().execute(() -> {
            PhotoEntity entity = mImagesDatabase.photosDao().getPhoto(id);

            try {
                final Bitmap bitmap = mFileStorage.getBitmap(src, mSettings.getStorageMode());
                mAppExecutors.getMainThread().execute(() -> callback.onSuccess(entity, bitmap));
            } catch (IOException e) {
                e.printStackTrace();
                if(e.getClass() == FileNotFoundException.class){
                    mAppExecutors.getMainThread().execute(() -> callback.onSuccess(entity, null));
                } else {
                    mAppExecutors.getMainThread().execute(callback::onError);
                }
            }
        });
    }

    @Override
    public void updateAlbum(long id, int fetchedCount) {
        mAppExecutors.getDiskIO().execute(() -> {
            mImagesDatabase.albumsDao().updateFetchedCount(id, fetchedCount);
        });
    }

    @Override
    public void putAlbums(Callback callback, AlbumEntity... albumEntities) {
        mAppExecutors.getDiskIO().execute(() ->
        {
            for(AlbumEntity entity: albumEntities){
                mImagesDatabase.albumsDao()
                        .updateAlbum(entity.getId(), entity.getTitle(),
                                entity.getDescription(), entity.getTotalItems());
            }
            mImagesDatabase.albumsDao().putAlbums(albumEntities);
            mAppExecutors.getMainThread().execute(callback::onSuccess);
        });
    }

    @Override
    public void getAlbums(GetAlbumEntitiesCallback albumEntitiesCallback) {
        mAppExecutors.getDiskIO().execute(() -> {
            final List<AlbumEntity> albumEntities = mImagesDatabase.albumsDao().getAlbums();
            mAppExecutors.getMainThread()
                    .execute(() -> albumEntitiesCallback.onSuccess(albumEntities));

        });
    }

    @Override
    public void putPhoto(PhotoEntity photoEntity, GetPhotoEntityCallback callback) {
        mAppExecutors.getDiskIO().execute(() -> {
            mImagesDatabase.photosDao().addPhoto(photoEntity);
            try {
                if (photoEntity.getFullSizeBitmap() != null) {
                    mFileStorage.putBitmap(photoEntity.getFullSizeBitmap(),
                            photoEntity.getFullsizeFilename(), mSettings.getStorageMode());
                }

                if(photoEntity.getThumbBitmap() != null) {
                    mFileStorage.putBitmap(photoEntity.getThumbBitmap(),
                            photoEntity.getThumbFilename(), mSettings.getStorageMode());
                }

                photoEntity.getFullSizeBitmap().recycle();
                photoEntity.setFullSizeBitmap(null);

                mAppExecutors.getMainThread().execute(() -> callback.onSuccess(photoEntity));

            } catch (IOException e) {
                e.printStackTrace();
                mAppExecutors.getMainThread().execute(callback::onError);
            }
        });
    }

    public void getGalleryPhotos(PhotoCallback callback){
        mAppExecutors.getDiskIO().execute(() -> {
            final List<PhotoEntity> entities =
                    mImagesDatabase.photosDao().getAllPhotos(PHOTOS_PAGE_SIZE);
            try {
                if(!entities.isEmpty()) {
                    setThumbs(entities, callback);
                } else {
                    mAppExecutors.getMainThread().execute(callback::onNoLocalPhotos);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mAppExecutors.getMainThread().execute(callback::onError);
            }

        });
    }

    @Override
    public void getGalleryPhotos(long albumId, long latestPhotoId, PhotoCallback callback) {
        mAppExecutors.getDiskIO().execute(() -> {
            final List<PhotoEntity> albumPhotos =
                    mImagesDatabase.photosDao()
                            .getPhotosByAlbum(albumId, latestPhotoId, PHOTOS_PAGE_SIZE);
            if(albumPhotos.size() == 0){
                mAppExecutors.getMainThread().execute(callback::onNoLocalPhotos);
            } else {

                try {
                    setThumbs(albumPhotos, callback);
                } catch (IOException e) {
                    mAppExecutors.getMainThread().execute(callback::onError);
                    e.printStackTrace();
                }
            }
        });
    }

    private void setThumbs(List<PhotoEntity> albumPhotos, PhotoCallback callback) throws IOException {
        for(PhotoEntity entity: albumPhotos){
                Bitmap bitmapThumb = mFileStorage.getBitmap(entity.getThumbFilename(),
                    mSettings.getStorageMode());
                entity.setThumbBitmap(bitmapThumb);
            }

        mAppExecutors.getMainThread().execute(() -> callback.onPhotoLoaded(albumPhotos));
        }
}
