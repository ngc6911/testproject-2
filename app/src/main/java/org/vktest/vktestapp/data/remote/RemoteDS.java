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

    @Override
    public void getPhotos(long albumId, int offset, GetPhotosCallback callback) {

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
