package org.vktest.vktestapp.data.local.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import org.vktest.vktestapp.data.local.db.dao.AlbumsDao;
import org.vktest.vktestapp.data.local.db.dao.PhotosDao;
import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;

import static org.vktest.vktestapp.data.local.db.ImagesDatabase.VERSION;

@Database(entities = { AlbumEntity.class, PhotoEntity.class }, version = VERSION)
@TypeConverters(Converters.class)
public abstract class ImagesDatabase extends RoomDatabase {

    public static final int VERSION = 1;
    public static final String DB_NAME = "images-data";

    public abstract AlbumsDao albumsDao();
    public abstract PhotosDao photosDao();
}
