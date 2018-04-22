package org.vktest.vktestapp.data;

import android.content.res.Configuration;

import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.data.remote.api.VKAlbum;
import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKPhoto;
import org.vktest.vktestapp.presentation.models.Photo;

public class DataUtils {

    public static Photo photoEntityToModel(PhotoEntity entity) {
        return new Photo(
                entity.getAlbumId(),
                entity.getId(),
                entity.getBigPhotoFilename(),
                entity.getSmallPhotoFilename(),
                entity.getDate());
    }


    public static PhotoEntity photoRemoteToEntity(VKPhoto vkPhoto, VKPhoto.SizeClass sizeClass){
        PhotoEntity entity = new PhotoEntity();
        entity.setId(vkPhoto.getId());
        entity.setOwnerId(vkPhoto.getOwnerId());
        entity.setAlbumId(vkPhoto.getAlbumId());
        entity.setDate(vkPhoto.getDate());
        entity.setFetched(false);
        entity.setRepostsCount(vkPhoto.getReposts().getCount());
        entity.setLikesCount(vkPhoto.getLikes().getCount());
        entity.setText(vkPhoto.getText());

        VKPhoto.Size thumbSize = vkPhoto.getSizeClassData(sizeClass);
        VKPhoto.Size fullSize = null;

        for(VKPhoto.Size size: vkPhoto.getSizes()) {
            if(fullSize == null){
                fullSize = size;
            } else {
                if(fullSize.getWidth() < size.getWidth()){
                    fullSize = size;
                }
            }
        }

        if (thumbSize != null) {
            entity.setSmallImageURI(thumbSize.getSrc());
        }

        if (fullSize != null) {
            entity.setLargeImageURI(fullSize.getSrc());
        }
        return entity;
    }

    public static AlbumEntity vkAlbumToAlbumEntity(VKAlbum vkAlbum){
        AlbumEntity entity = new AlbumEntity();
        entity.setId(vkAlbum.getId());
        entity.setOwnerId(vkAlbum.getOwnerId());
        entity.setTitle(vkAlbum.getTitle());
        entity.setDescription(vkAlbum.getDescription());
        entity.setTotalItems(vkAlbum.getSize());
        return entity;
    }

    public static AlbumEntity[] vkAlbumsListToAlbumEntities(VKAlbumsList albumsList){
        AlbumEntity[] entities = new AlbumEntity[albumsList.getItems().size()];

        for(int i=0; i<entities.length; i++){
            entities[i] = vkAlbumToAlbumEntity(albumsList.getItems().get(i));
        }

        return entities;
    }

    public static VKPhoto.SizeClass getThumbSizeClass(int screenSize){
        switch (screenSize){
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return VKPhoto.SizeClass.s;

            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return VKPhoto.SizeClass.m;

            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return VKPhoto.SizeClass.x;

            default:
                return VKPhoto.SizeClass.m;

        }
    }

}
