package org.vktest.vktestapp.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.LruCache;

import org.vktest.vktestapp.data.local.LocalDataSource;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.data.remote.AuthDataSource;
import org.vktest.vktestapp.data.remote.RemoteDataSource;
import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKPhoto;
import org.vktest.vktestapp.data.remote.api.VKPhotosList;
import org.vktest.vktestapp.presentation.models.Photo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RepositoryImpl implements Repository {

    private AuthDataSource mAuthDataSource;
    private RemoteDataSource mRemoteDS;
    private LocalDataSource mLocalDS;

    private Context mContext;
    private LruCache<String, Bitmap> photosMemoryCache;

    @Inject
    public RepositoryImpl(AuthDataSource remoteAuthDS, LocalDataSource localDataSource,
                          RemoteDataSource remoteDataSource, Context context) {
        mContext = context;
        mAuthDataSource = remoteAuthDS;
        mRemoteDS = remoteDataSource;
        mLocalDS = localDataSource;
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
                        initData(callback);
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
    public void getPhotos(Photo lastPhoto, GetPhotosCallback getPhotosCallback) {
        mLocalDS.getGalleryPhotos(
                lastPhoto.getAlbumId(),
                lastPhoto.getPhotoId(), new LocalDataSource.PhotoCallback() {
                    @Override
                    public void onSuccess(PhotoEntity photoEntity, Bitmap smallBitmap) {
                        Photo photo = DataUtils.photoEntityToModel(photoEntity);

                        photosMemoryCache.put(photo.getPhotoThumbBitmapPath(), smallBitmap);
                        getPhotosCallback.onSuccess(photo);
                    }

                    @Override
                    public void onNetworkPhotosFetchRequired(long albumId, int alreadyFetchedCount) {
                        fetchPhotos(albumId, alreadyFetchedCount,  this);
                    }

                    @Override
                    public void onNetworkAlbumsFetchRequired(int alreadyFetchedCount) {
                        fetchAlbums(alreadyFetchedCount, getPhotosCallback);
                    }

                    @Override
                    public void onError() {
                        getPhotosCallback.onError();
                    }
                });
    }

    private void fetchPhotos(long albumId, int alreadyFetched, LocalDataSource.PhotoCallback callback){
        mRemoteDS.getPhotos(albumId, alreadyFetched / RemoteDataSource.PHOTOS_FETCH_COUNT,
                new RemoteDataSource.GetPhotosCallback() {
                    @Override
                    public void onSuccess(VKPhotosList photos) {
                        for(VKPhoto photo: photos.getItems()){
                            fetchPhotoBitmap(photo, callback);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void fetchAlbums(int alreadyFetched, GetPhotosCallback getPhotosCallback){
        mRemoteDS.getAlbums(
                alreadyFetched / RemoteDataSource.ALBUMS_FETCH_COUNT, new RemoteDataSource.GetAlbumsCallback() {
                    @Override
                    public void onSuccess(VKAlbumsList albumsList) {
                        mLocalDS.putAlbums(DataUtils.vkAlbumsListToAlbumEntities(albumsList));

                    }

                    @Override
                    public void onError() {

                    }
                });
    }


    private void fetchPhotoBitmap(VKPhoto photo, LocalDataSource.PhotoCallback callback){
        mRemoteDS.fetchBitmap(photo, new RemoteDataSource.FetchPhotoCallback() {
            @Override
            public void onSuccess(VKPhoto photo, Bitmap photoBitmap) {
                int screenSize = mContext.getResources().getConfiguration().screenLayout
                        & Configuration.SCREENLAYOUT_SIZE_MASK;

                PhotoEntity entity = DataUtils.photoRemoteToEntity(photo,
                        DataUtils.getThumbSizeClass(screenSize));

                mLocalDS.putPhoto(entity, photoBitmap, null, null);
                callback.onSuccess(entity, photoBitmap);
            }

            @Override
            public void onError() {

            }
        });
    }





    private void fetchPhotoBitmaps(VKPhotosList vkPhotos, GetPhotosCallback getPhotosCallback){
        List<Photo> photos = new ArrayList<>();
        for(VKPhoto photo: vkPhotos.getItems()) {
            mRemoteDS.fetchBitmap(photo, new RemoteDataSource.FetchPhotoCallback() {

                @Override
                public void onSuccess(VKPhoto photo, Bitmap bitmap) {
//                    photos.add(DataUtils.photoRemoteToEntity(photo));


                    if(photos.size() == vkPhotos.getCount()){
                        getPhotosCallback.onSuccess(photos);
                    }
                }

                @Override
                public void onError() {

                }
            });
        }

    }





    @Override
    public void getPhotos(GetPhotosCallback getPhotosCallback) {


        mRemoteDS.getPhotos(new RemoteDataSource.GetPhotosCallback() {
            @Override
            public void onSuccess(VKPhotosList photos) {
                fetchPhotoBitmaps(photos, getPhotosCallback);
            }

            @Override
            public void onError() {
                getPhotosCallback.onError();
            }
        });
    }

    private void initData(Repository.AutorizationCallback callback){
        int screenSize = mContext.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;

        mRemoteDS.loadInitialData(DataUtils.getThumbSizeClass(screenSize),
                new RemoteDataSource.GetInitialDataCallback() {
                    @Override
                    public void onAlbumsFetched(VKAlbumsList albumsList) {
                        mLocalDS.putAlbums(DataUtils.vkAlbumsListToAlbumEntities(albumsList));
                    }

                    @Override
                    public void onPhotoFetched(VKPhoto photo, Bitmap thumb_bitmap) {
                        PhotoEntity entity = DataUtils.photoRemoteToEntity(photo,
                                DataUtils.getThumbSizeClass(screenSize));
                        photosMemoryCache.put(entity.getSmallImageURI(), thumb_bitmap);

                        mLocalDS.putPhoto(entity, thumb_bitmap, null, null);
                    }

                    @Override
                    public void onFinish() {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError() {
                        callback.onError();
                    }
                });
    }

}
