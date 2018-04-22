package org.vktest.vktestapp.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import org.vktest.vktestapp.data.local.LocalDataSource;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.data.local.cache.ImageCache;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.data.remote.AuthDataSource;
import org.vktest.vktestapp.data.remote.RemoteDataSource;
import org.vktest.vktestapp.data.remote.api.VKAlbum;
import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKPhoto;
import org.vktest.vktestapp.data.remote.api.VKPhotosList;
import org.vktest.vktestapp.presentation.models.Photo;

import java.util.ArrayList;
import java.util.Iterator;
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
                callback.onSuccess();
            }

            @Override
            public void onPending() {
                //TODO: further send here some resource string id with current app start stage
                callback.onPending(-1);
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
                        callback.onSuccess();
                    }

                    @Override
                    public void onPending() {

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
                    mImageCache.putBitmap(entity.getBigPhotoFilename(), bitmap);
                    callback.onSuccess(DataUtils.photoEntityToModel(entity));
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        }
    }

    @Override
    public void getPhotos(@Nullable Photo lastPhoto, GetPhotosCallback getPhotosCallback) {
        final LocalDataSource.PhotoCallback callback = new LocalDataSource.PhotoCallback() {

            private int successCounter = 0;
            private int successLimit;
            private final List<PhotoEntity> entities = new ArrayList<>();

                    @Override
                    public void onSuccess(PhotoEntity photoEntity, Bitmap smallBitmap) {
                        Photo photo = DataUtils.photoEntityToModel(photoEntity);

                        mImageCache.putBitmap(photo.getPhotoThumbBitmapPath(), smallBitmap);
                        getPhotosCallback.onSuccess(photo);
                    }

                    @Override
                    public void onNetworkPhotosFetchRequired(long albumId, int alreadyFetchedCount) {
                        fetchPhotos(albumId, alreadyFetchedCount,  this);
                    }

                    @Override
                    public void onNetworkAlbumsFetchRequired(int alreadyFetchedCount) {
                        fetchAlbums(alreadyFetchedCount, this);
                    }

                    @Override
                    public void onError() {
                        getPhotosCallback.onError();
                    }
                };

        if(lastPhoto == null){
            callback.onNetworkAlbumsFetchRequired(0);
        } else {
            mLocalDS.getGalleryPhotos(
                    lastPhoto.getAlbumId(),
                    lastPhoto.getPhotoId(), callback);
        }

    }

    private void fetchPhotos(long albumId, int alreadyFetched, LocalDataSource.PhotoCallback callback){
        mRemoteDS.getPhotos(albumId, alreadyFetched / RemoteDataSource.PHOTOS_FETCH_COUNT,
                new RemoteDataSource.GetPhotosCallback() {
                    @Override
                    public void onSuccess(VKPhotosList photos) {
                        int screenSize = mContext.getResources().getConfiguration().screenLayout
                                & Configuration.SCREENLAYOUT_SIZE_MASK;

                        for(VKPhoto photo: photos.getItems()){
                            final PhotoEntity entity = DataUtils.photoRemoteToEntity(photo,
                                    DataUtils.getThumbSizeClass(screenSize));

                            fetchBitmaps(entity, callback);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void fetchAlbums(int alreadyFetched, LocalDataSource.PhotoCallback callback){
        mRemoteDS.getAlbums(
                alreadyFetched / RemoteDataSource.ALBUMS_FETCH_COUNT, new RemoteDataSource.GetAlbumsCallback() {
                    @Override
                    public void onSuccess(VKAlbumsList albumsList) {
                        mLocalDS.putAlbums(DataUtils.vkAlbumsListToAlbumEntities(albumsList));

                        int fetchCount = 0;
                        Iterator<VKAlbum> albumIterator = albumsList.getItems().iterator();
                        while (fetchCount < RemoteDataSource.PHOTOS_FETCH_COUNT &&
                                albumIterator.hasNext()){

                            VKAlbum album = albumIterator.next();

                            callback.onNetworkPhotosFetchRequired(album.getId(), 0);

                            if(album.getSize() >= RemoteDataSource.PHOTOS_FETCH_COUNT) {
                                fetchCount += RemoteDataSource.PHOTOS_FETCH_COUNT;
                            } else {
                                fetchCount += album.getSize();
                            }

                        }
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
    }


    private void fetchBitmaps(PhotoEntity entity, LocalDataSource.PhotoCallback callback) {
        mRemoteDS.fetchBitmap(entity, new RemoteDataSource.FetchPhotoCallback() {
            @Override
            public void onSuccess(Bitmap small, Bitmap large) {
                mLocalDS.putPhoto(entity, small, large);
                callback.onSuccess(entity, small);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

}
