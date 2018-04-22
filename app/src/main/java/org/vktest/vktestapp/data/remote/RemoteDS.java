package org.vktest.vktestapp.data.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vk.sdk.VKAccessToken;

import org.vktest.vktestapp.AppExecutors;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.data.remote.api.API;
import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKBaseResponse;
import org.vktest.vktestapp.data.remote.api.VKPhotosList;

import java.io.IOException;
import java.io.InputStream;

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
        mAppExecutors.getNetworkIO().execute(() -> {
            try {
                Response<VKBaseResponse<VKPhotosList>> r =
                        mApi.getPhotosFromAlbum(
                                null, albumId, 1,
                                0, 1,
                                PHOTOS_FETCH_COUNT, offset).execute();

                VKBaseResponse<VKPhotosList> vkPhotosListResponse = r.body();

                if (vkPhotosListResponse != null && vkPhotosListResponse.isSuccessful()) {
                    mAppExecutors.getMainThread()
                            .execute(() -> callback.onSuccess(vkPhotosListResponse.getSuccess()));

                } else if(vkPhotosListResponse != null && !vkPhotosListResponse.isSuccessful()){
                    mAppExecutors.getMainThread().execute(callback::onError);
                }

            } catch (IOException e) {
                callback.onError();
                e.printStackTrace();
            }
        });
    }

    private Bitmap fetchBitmapSync(String src) throws IOException{
        Request r = new Request.Builder().url(src).build();
        okhttp3.Response response = mOkHttpClient.newCall(r).execute();
        ResponseBody body = response.body();

        final Bitmap b;
        if (body != null) {
            InputStream inputStream = body.byteStream();
            b = BitmapFactory.decodeStream(inputStream);
            return b;
        } else {
            throw new IOException(String.format("response body is null for GET from %s: ", src));
        }
    }

    @Override
    public void fetchBitmap(PhotoEntity photoEntity, FetchPhotoCallback callback) {
        mAppExecutors.getNetworkIO().execute(() -> {
            try {
                Bitmap small = fetchBitmapSync(photoEntity.getSmallImageURI());
                Bitmap large = fetchBitmapSync(photoEntity.getLargeImageURI());
                mAppExecutors.getMainThread().execute(() -> callback.onSuccess(small, large));
            } catch (IOException e) {
                e.printStackTrace();
                callback.onError();
            }
        });
    }
}
