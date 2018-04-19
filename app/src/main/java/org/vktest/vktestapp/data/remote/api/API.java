package org.vktest.vktestapp.data.remote.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    String BASE = "https://api.vk.com/method/";

    @Deprecated
    @GET("photos.getAll")
    Call<VKBaseResponse<VKPhotosList>> getUserPhotos(@Query("owner_id") Long ownerId,
                                                @Query("extended") Integer extended,
                                                @Query("offset") Integer offset,
                                                @Query("count") Integer count,
                                                @Query("photo_sizes") Integer photoSizes,
                                                @Query("no_service_albums") Integer noServiceAlbums,
                                                @Query("need_hidden") Integer needHidden,
                                                @Query("skip_hidden") Integer skipHidden);


    @GET("photos.getAlbums")
    Call<VKBaseResponse<VKAlbumsList>> getUserAlbums(@Query("owner_id") Long ownerId,
                                                     @Query("count") Integer count,
                                                     @Query("offset") Integer offset,
                                                     @Query("need_system") Integer needSystem);

    @GET("photos.get")
    Call<VKBaseResponse<VKPhotosList>> getPhotosFromAlbum(@Query("owner_id") Long ownerId,
                                                          @Query("album_id") Long albumId,
                                                          @Query("rev") Integer order,
                                                          @Query("photo_sizes") Integer photoSizes,
                                                          @Query("count") Integer count,
                                                          @Query("offset") Integer offset);
}
