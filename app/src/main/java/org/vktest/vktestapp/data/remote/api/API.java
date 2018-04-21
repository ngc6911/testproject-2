package org.vktest.vktestapp.data.remote.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    String BASE = "https://api.vk.com/method/";

    @GET("photos.getAlbums")
    Call<VKBaseResponse<VKAlbumsList>> getUserAlbums(@Query("owner_id") Long ownerId,
                                                     @Query("count") Integer count,
                                                     @Query("offset") Integer offset,
                                                     @Query("need_system") Integer needSystem);

    @GET("photos.get")
    Call<VKBaseResponse<VKPhotosList>> getPhotosFromAlbum(@Query("owner_id") Long ownerId,
                                                          @Query("album_id") Long albumId,
                                                          @Query("extended") Integer extended,
                                                          @Query("rev") Integer order,
                                                          @Query("photo_sizes") Integer photoSizes,
                                                          @Query("count") Integer count,
                                                          @Query("offset") Integer offset);
}
