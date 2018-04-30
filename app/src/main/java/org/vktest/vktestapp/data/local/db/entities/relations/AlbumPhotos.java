package org.vktest.vktestapp.data.local.db.entities.relations;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;

import java.util.List;

public class AlbumPhotos {
    @Embedded
    AlbumEntity album;

    @Relation(entity = PhotoEntity.class, parentColumn = "id", entityColumn = "album_id")
    List<PhotoEntity> photos;

    public AlbumEntity getAlbum() {
        return album;
    }

    public void setAlbum(AlbumEntity album) {
        this.album = album;
    }

    public List<PhotoEntity> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoEntity> photos) {
        this.photos = photos;
    }

    public int getNonfetchedCount(){
        return album.getTotalItems() - photos.size();
    }
}
