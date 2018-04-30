package org.vktest.vktestapp.data.remote;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vk.sdk.VKAccessToken;

import org.vktest.vktestapp.AppExecutors;
import org.vktest.vktestapp.data.DataUtils;
import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.remote.api.API;
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
    public void getAlbums(GetAlbumsCallback callback) {
        mAppExecutors.getNetworkIO().execute(() -> {
            try {
                Response<VKBaseResponse<VKAlbumsList>> response = mApi.getUserAlbums(
                        Long.parseLong(VKAccessToken.currentToken().userId),
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
                mAppExecutors.getMainThread().execute(callback::onError);
            }
        });
    }

    @Override
    public void getPhotos(List<AlbumEntity> albums, int totalPhotos, Context context,
                          GetPhotosListCallback photosListCallback) {
        mAppExecutors.getNetworkIO().execute(() -> {
            int screenSize = context.getResources().getConfiguration().screenLayout
                    & Configuration.SCREENLAYOUT_SIZE_MASK;

            int totalFetchLeft = totalPhotos;
            for(AlbumEntity album: albums) {

                int canFetch = album.getTotalItems() - album.getItemsFetchedCount();
                canFetch = canFetch >= totalFetchLeft ? totalFetchLeft : canFetch;

                try {
                    Response<VKBaseResponse<VKPhotosList>> r = mApi.getPhotosFromAlbum(null,
                            album.getId(), 1, 0, 1,
                            canFetch, album.getItemsFetchedCount())
                            .execute();

                    VKBaseResponse<VKPhotosList> vkPhotosListResponse = r.body();
                    if (vkPhotosListResponse != null && vkPhotosListResponse.isSuccessful()) {
                        List<VKPhoto> vkPhotos = vkPhotosListResponse.getSuccess().getItems();

                        final VKPhoto.SizeClass size = DataUtils.getThumbSizeClass(screenSize);

                        for(VKPhoto vkPhoto: vkPhotos) {
                            final Bitmap thumb = fetchBitmapSync(
                                    vkPhoto.getThumbSizeClassData(size).getSrc());

                            final Bitmap fullSize = fetchBitmapSync(
                                    vkPhoto.getFullSize().getSrc());

                            mAppExecutors.getMainThread()
                                    .execute(() -> photosListCallback.onNewPhoto(vkPhoto, thumb, fullSize));
                        }

                        album.setItemsFetchedCount(album.getItemsFetchedCount() + canFetch);

                        mAppExecutors.getMainThread()
                                .execute(() -> photosListCallback.onAlbumFetchFinish(album));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    mAppExecutors.getMainThread().execute(photosListCallback::onError);
                }

                totalFetchLeft -= canFetch;
            }

        });

    }

    private Bitmap fetchBitmapSync(String src) throws IOException {
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
}
