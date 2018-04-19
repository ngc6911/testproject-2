package org.vktest.vktestapp.data.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.relations.AlbumPhotos;

import java.util.List;

@Dao
public interface AlbumsDao {

    @Query("SELECT * FROM albums ORDER BY id")
    List<AlbumEntity> getAlbums();

    @Query("SELECT COUNT(*) FROM albums ORDER BY id")
    int getAlbumsCount();

    @Query("SELECT * FROM albums WHERE id = :albumId")
    AlbumPhotos getAlbumById(Long albumId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void putAlbums(AlbumEntity... entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void putAlbum(AlbumEntity entity);

}
