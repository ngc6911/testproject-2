package org.vktest.vktestapp.data.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;

import java.util.List;

@Dao
public interface PhotosDao {

    @Query("SELECT * FROM photos WHERE photos.album_id = :albumId ORDER BY date DESC LIMIT 10")
    List<PhotoEntity> getPhotosByAlbum(Long albumId);

    @Query("SELECT * FROM photos WHERE album_id = :albumId AND id > :lastId ORDER BY date DESC LIMIT 10")
    List<PhotoEntity> getPhotosByAlbum(Long albumId, Long lastId);

    @Query("SELECT * FROM photos WHERE id = :id")
    PhotoEntity getPhoto(long id);

    @Query("SELECT * FROM photos ORDER BY album_id, date DESC")
    List<PhotoEntity> getAllPhotos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPhoto(PhotoEntity... photoEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPhoto(PhotoEntity photoEntity);

    @Delete
    void deletePhoto(PhotoEntity... repos);

}
