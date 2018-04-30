package org.vktest.vktestapp.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import org.vktest.vktestapp.data.local.LocalDataSource;
import org.vktest.vktestapp.data.local.cache.ImageCache;
import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.data.remote.AuthDataSource;
import org.vktest.vktestapp.data.remote.RemoteDataSource;
import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKPhoto;
import org.vktest.vktestapp.presentation.models.Photo;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RepositoryImpl implements Repository {

    private AuthDataSource mAuthDataSource;
    private RemoteDataSource mRemoteDS;
    private LocalDataSource mLocalDS;

    private Context mContext;
    private ImageCache mImageCache;

    private boolean isFetching;

    @Inject
    public RepositoryImpl(AuthDataSource remoteAuthDS, LocalDataSource localDataSource,
                          RemoteDataSource remoteDataSource, ImageCache cache, Context context) {
        mContext = context;
        mAuthDataSource = remoteAuthDS;
        mRemoteDS = remoteDataSource;
        mLocalDS = localDataSource;
        mImageCache = cache;
    }

    @Override
    public void autrorize(Activity activity) {
        mAuthDataSource.startAuthorization(activity);
    }

    @Override
    public void checkAuthorizationState(AutorizationCallback callback) {
        mAuthDataSource.checkAuthorization(mContext, new AuthDataSource.AutorizationCallback() {
            @Override
            public void onSuccess() {
                fetchAlbums(callback);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    @Override
    public void checkAuthorizationResult(int requestCode, int resultCode, Intent data,
                                         Repository.AutorizationCallback callback) {
        mAuthDataSource.checkAuthorizationResult(requestCode,
                resultCode, data, new AuthDataSource.AutorizationCallback() {
                    @Override
                    public void onSuccess() {
                        fetchAlbums(callback);
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
    }

    @Override
    public void getLargePhoto(Photo photo, GetPhotosCallback callback) {
        Bitmap bitmap = mImageCache.getBitmap(photo.getPhotoBitmapPath());
        if (bitmap != null) {
            callback.onSuccess(photo);
        } else {
            mLocalDS.getPhoto(photo.getPhotoId(), photo.getPhotoBitmapPath(), new LocalDataSource.GetPhotoCallback() {
                @Override
                public void onSuccess(PhotoEntity entity, Bitmap bitmap) {
                    mImageCache.putBitmap(entity.getFullsizeFilename(), bitmap);
                    callback.onSuccess(DataUtils.photoEntityToModel(entity));
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        }
    }

    private void init(GetPhotosListCallback getPhotosListCallback){
        mLocalDS.getAlbums(new LocalDataSource.GetAlbumEntitiesCallback() {
            @Override
            public void onSuccess(List<AlbumEntity> albumEntities) {
                List<AlbumEntity> toFetchEntities =
                        DataUtils.filterAlbumsToFetchFrom(albumEntities, RemoteDataSource.PHOTOS_FETCH_COUNT);

                fetchPhotos(toFetchEntities, DataUtils.maxFetchCountFrom(toFetchEntities,
                        RemoteDataSource.PHOTOS_FETCH_COUNT), new LocalDataSource.PhotosCallback() {

                    @Override
                    public void onSuccess(PhotoEntity photoEntity) {
                        forwardDataToView(photoEntity, getPhotosListCallback);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void getFirstPhotos(GetPhotosListCallback getPhotosListCallback) {
        mLocalDS.getGalleryPhotos(new LocalDataSource.PhotoCallback() {
            @Override
            public void onPhotoLoaded(List<PhotoEntity> photoEntities) {
                forwardDataToView(photoEntities, getPhotosListCallback);
            }

            @Override
            public void onNoLocalPhotos() {
                init(getPhotosListCallback);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void getNextPhotos(Photo lastPhoto, GetPhotosListCallback getPhotosListCallback) {
        final LocalDataSource.PhotoCallback photoCallback = new LocalDataSource.PhotoCallback() {
                    @Override
                    public void onPhotoLoaded(List<PhotoEntity> photoEntities) {
                        forwardDataToView(photoEntities, getPhotosListCallback);
                    }

                    @Override
                    public void onNoLocalPhotos() {
                        mLocalDS.getAlbums(new LocalDataSource.GetAlbumEntitiesCallback() {
                            @Override
                            public void onSuccess(List<AlbumEntity> albumEntities) {
                                List<AlbumEntity> toFetchEntities =
                                        DataUtils.filterAlbumsToFetchFrom(albumEntities, RemoteDataSource.PHOTOS_FETCH_COUNT);

                                fetchPhotos(toFetchEntities,
                                        DataUtils.maxFetchCountFrom(toFetchEntities, RemoteDataSource.PHOTOS_FETCH_COUNT),
                                        new LocalDataSource.PhotosCallback() {
                                    @Override
                                    public void onSuccess(PhotoEntity photoEntity) {
                                        forwardDataToView(photoEntity, getPhotosListCallback);
                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                };

        mLocalDS.getGalleryPhotos(
                lastPhoto.getAlbumId(),
                lastPhoto.getPhotoId(), photoCallback);
    }

    private void forwardDataToView(List<PhotoEntity> entities, GetPhotosListCallback callback){
        for(PhotoEntity photoEntity: entities){
            forwardDataToView(photoEntity, callback);
        }
    }

    private void forwardDataToView(PhotoEntity photoEntity, GetPhotosListCallback callback){
        mImageCache.putBitmap(
                photoEntity.getThumbFilename(), photoEntity.getThumbBitmap());
        callback.onSuccess(DataUtils.photoEntityToModel(photoEntity));
    }

    private void fetchPhotos(List<AlbumEntity> albumEntities, int totalPhotos,
                             LocalDataSource.PhotosCallback callback) {
        mRemoteDS.getPhotos(albumEntities, totalPhotos, mContext,
                new RemoteDataSource.GetPhotosListCallback() {
            @Override
            public void onNewPhoto(VKPhoto vkPhoto, Bitmap thumbBitmap, Bitmap fullsizeBitmap) {
                cachePhoto(vkPhoto, thumbBitmap, fullsizeBitmap, callback);
            }

            @Override
            public void onAlbumFetchFinish(AlbumEntity album) {
                mLocalDS.updateAlbum(album.getId(), album.getItemsFetchedCount());
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private void cachePhoto(VKPhoto vkPhoto, Bitmap thumbBitmap, Bitmap fullsizeBitmap,
                            LocalDataSource.PhotosCallback callback){
        PhotoEntity entity = DataUtils.photoRemoteToEntity(vkPhoto);
        entity.setThumbBitmap(thumbBitmap);
        entity.setFullSizeBitmap(fullsizeBitmap);
        mLocalDS.putPhoto(entity, new LocalDataSource.GetPhotoEntityCallback() {
            @Override
            public void onSuccess(PhotoEntity entity) {
                callback.onSuccess(entity);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }


    private void fetchAlbums(AutorizationCallback autorizationCallback) {
        mRemoteDS.getAlbums(new RemoteDataSource.GetAlbumsCallback() {
            @Override
            public void onSuccess(VKAlbumsList albumsList) {

                mLocalDS.putAlbums(new LocalDataSource.Callback() {
                    @Override
                    public void onSuccess() {
                        autorizationCallback.onSuccess();
                    }

                    @Override
                    public void onError() {
                        autorizationCallback.onError();
                    }
                }, DataUtils.vkAlbumsListToAlbumEntities(albumsList));
            }

            @Override
            public void onError() {
                autorizationCallback.onError();
            }
        });
    }
}
