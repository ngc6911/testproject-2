package org.vktest.vktestapp.data.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.vk.sdk.VKAccessToken;

import org.vktest.vktestapp.AppExecutors;
import org.vktest.vktestapp.data.remote.api.API;
import org.vktest.vktestapp.data.remote.api.VKAlbum;
import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKBaseResponse;
import org.vktest.vktestapp.data.remote.api.VKPhoto;
import org.vktest.vktestapp.data.remote.api.VKPhotosList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;

@Singleton
public class RemoteDS implements RemoteDataSource{

    private AppExecutors mAppExecutors;
    private API mApi;

    private OkHttpClient mOkHttpClient;

    @Inject
    public RemoteDS(AppExecutors appExecutors, API api, OkHttpClient okHttpClient) {
        mAppExecutors = appExecutors;
        mApi = api;
        mOkHttpClient = okHttpClient;
    }

    @Override
    public void loadInitialData(VKPhoto.SizeClass sizeClass, GetInitialDataCallback callback) {
        mAppExecutors.getNetworkIO().execute(() -> {
            try {
                Response<VKBaseResponse<VKAlbumsList>> response = mApi.getUserAlbums(
                        Long.parseLong(VKAccessToken.currentToken().userId),
                        ALBUMS_FETCH_COUNT,
                        null,
                        1).execute();

                VKBaseResponse<VKAlbumsList> vkAlbumsListResponse = response.body();

                if (vkAlbumsListResponse != null && vkAlbumsListResponse.isSuccessful()) {
                    mAppExecutors.getMainThread()
                            .execute(() -> callback.onAlbumsFetched(vkAlbumsListResponse.getSuccess()));

                } else if(vkAlbumsListResponse != null && !vkAlbumsListResponse.isSuccessful()){
                    throw new IOException(vkAlbumsListResponse.getError().getError_msg());
                } else {
                    throw new IOException();
                }

                List<VKAlbum> albums = vkAlbumsListResponse.getSuccess().getItems();


                int initialPhotosCount = 0;
                List<VKPhoto> prefetchedPhotos = new ArrayList<>(PHOTOS_FETCH_COUNT);
                while (albums.iterator().hasNext() &&
                        initialPhotosCount < PHOTOS_FETCH_COUNT){

                    VKAlbum album = albums.iterator().next();

                    VKPhotosList photos;
                    if(initialPhotosCount + album.getSize() < PHOTOS_FETCH_COUNT){
                         photos = getPhotosSync(null, album.getId(), null, null);
                    } else {
                        photos = getPhotosSync(null, album.getId(),
                                PHOTOS_FETCH_COUNT - initialPhotosCount,null);
                    }
                    initialPhotosCount += photos.getItems().size();
                    prefetchedPhotos.addAll(photos.getItems());
                }

                for (VKPhoto photo: prefetchedPhotos){
                    VKPhoto.Size sizeClassData = photo.getSizeClassData(sizeClass);
                    if (sizeClassData != null) {
                        Bitmap bitmap = fetchBitmapSync(sizeClassData.getSrc());
                        mAppExecutors.getMainThread().execute(() ->
                                callback.onPhotoFetched(photo, bitmap));
                    } else {
                        throw new IOException("Bad size class data fetched");
                    }
                }

                mAppExecutors.getMainThread()
                        .execute(callback::onFinish);

            } catch (IOException e) {
                e.printStackTrace();
                mAppExecutors.getMainThread().execute(callback::onError);
            }
        });
    }

    @Override
    public void getAlbums(Integer offset, GetAlbumsCallback callback) {
        mAppExecutors.getNetworkIO().execute(() -> {
            try {
                Response<VKBaseResponse<VKAlbumsList>> response = mApi.getUserAlbums(
                        Long.parseLong(VKAccessToken.currentToken().userId),
                        ALBUMS_FETCH_COUNT,
                        offset,
                        1).execute();

                VKBaseResponse<VKAlbumsList> vkAlbumsListResponse = response.body();

                if (vkAlbumsListResponse != null && vkAlbumsListResponse.isSuccessful()) {
                    mAppExecutors.getMainThread()
                            .execute(() -> callback.onSuccess(vkAlbumsListResponse.getSuccess()));

                } else if(vkAlbumsListResponse != null && !vkAlbumsListResponse.isSuccessful()){
                    mAppExecutors.getMainThread().execute(callback::onError);
                }


            } catch (IOException e) {
                e.printStackTrace();
                callback.onError();
            }
        });
    }

    private Bitmap fetchBitmapSync(String src) throws IOException {
        Request r = new Request.Builder().url(src).build();

            okhttp3.Response response = mOkHttpClient.newCall(r).execute();
            ResponseBody body = response.body();

            if (body != null) {
                InputStream inputStream = body.byteStream();
                return BitmapFactory.decodeStream(inputStream);
            } else {
                throw new IOException();
            }
    }

    private VKPhotosList getPhotosSync(Long ownerId, Long albumId,
                                       @Nullable Integer count,
                                       @Nullable Integer offset) throws IOException{

        Response<VKBaseResponse<VKPhotosList>> responseCall = mApi.getPhotosFromAlbum(
                ownerId,
                albumId,
                1,
                1,
                count,
                offset).execute();

        VKBaseResponse<VKPhotosList> vkPhotoVKListResponse = responseCall.body();

        if (vkPhotoVKListResponse == null) {
            throw new IOException();
        } else if (!vkPhotoVKListResponse.isSuccessful()){
            throw new IOException(vkPhotoVKListResponse.getError().getError_msg());
        } else {
            return vkPhotoVKListResponse.getSuccess();
        }
    }

    @Override
    public void getPhotos(long albumId, int offset, GetPhotosCallback callback) {

    }

    @Override
    public void getPhotos(RemoteDataSource.GetPhotosCallback callback) {
        mAppExecutors.getNetworkIO().execute(() -> {
            try {
                Response<VKBaseResponse<VKPhotosList>> responseCall = mApi.getUserPhotos(
                        Long.parseLong(VKAccessToken.currentToken().userId),
                        1,
                        null,
                        null,
                        1,
                        0,
                        1,
                        0).execute();

                VKBaseResponse<VKPhotosList> vkPhotoVKListResponse = responseCall.body();
                if (vkPhotoVKListResponse != null && vkPhotoVKListResponse.isSuccessful()) {
                    mAppExecutors.getMainThread()
                            .execute(() -> callback.onSuccess(vkPhotoVKListResponse.getSuccess()));

                } else if(vkPhotoVKListResponse != null && !vkPhotoVKListResponse.isSuccessful()){
                    mAppExecutors.getMainThread().execute(callback::onError);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void fetchBitmap(VKPhoto vkPhoto, FetchPhotoCallback callback) {
        mAppExecutors.getNetworkIO().execute(() -> {
            Request r = new Request.Builder().url(vkPhoto.getSizes().get(4).getSrc()).build();
            try {
                okhttp3.Response response = mOkHttpClient.newCall(r).execute();
                ResponseBody body = response.body();
                if (body != null) {
                    InputStream inputStream = body.byteStream();
                    Bitmap b = BitmapFactory.decodeStream(inputStream);
                    mAppExecutors.getMainThread().execute(() -> callback.onSuccess(vkPhoto, b));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
