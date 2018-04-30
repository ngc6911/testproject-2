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

    @Query("SELECT * FROM photos WHERE photos.album_id = :albumId ORDER BY id ASC LIMIT :limit")
    List<PhotoEntity> getPhotosByAlbum(Long albumId, int limit);

    @Query("SELECT * FROM photos WHERE id > :lastId AND album_id >= :albumId " +
            "ORDER BY album_id, id ASC LIMIT :limit")
    List<PhotoEntity> getPhotosByAlbum(Long albumId, Long lastId, int limit);

    @Query("SELECT * FROM photos WHERE id = :id")
    PhotoEntity getPhoto(long id);

    @Query("SELECT * FROM photos ORDER BY album_id, date ASC LIMIT :limit")
    List<PhotoEntity> getAllPhotos(int limit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPhoto(PhotoEntity photoEntity);

}
