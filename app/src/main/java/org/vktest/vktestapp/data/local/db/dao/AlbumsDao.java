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

    @Query("UPDATE albums SET title = :title, description = :description, " +
            "total_items = :totalItems WHERE id = :id")
    void updateAlbum(long id, String title, String description, int totalItems);

    @Query("UPDATE albums SET fetched_items = :fetchedItems WHERE id = :id")
    void updateFetchedCount(long id, int fetchedItems);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void putAlbums(AlbumEntity... entities);
}
