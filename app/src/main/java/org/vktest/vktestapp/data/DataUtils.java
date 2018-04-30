package org.vktest.vktestapp.data;

import android.content.res.Configuration;

import org.vktest.vktestapp.data.local.db.entities.AlbumEntity;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.data.remote.api.VKAlbum;
import org.vktest.vktestapp.data.remote.api.VKAlbumsList;
import org.vktest.vktestapp.data.remote.api.VKPhoto;
import org.vktest.vktestapp.presentation.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    public static Photo photoEntityToModel(PhotoEntity entity) {
        Photo photo = new Photo(
                entity.getAlbumId(),
                entity.getId(),
                entity.getFullsizeFilename(),
                entity.getThumbFilename(),
                entity.getDate());

        photo.setLikesCount(entity.getLikesCount());
        photo.setRepostsCount(entity.getRepostsCount());
        photo.setText(entity.getText());
        return photo;
    }

    public static int maxFetchCountFrom(List<AlbumEntity> entities, int maxFetch){
        int canFetchCountTotal = 0;
        for(AlbumEntity entity: entities) {
            int canFetchCount = entity.getTotalItems() - entity.getItemsFetchedCount();


            if(canFetchCountTotal + canFetchCount >= maxFetch){
                return maxFetch;
            } else {
                canFetchCountTotal += canFetchCount;
            }
        }

        return canFetchCountTotal;
    }



    public static List<AlbumEntity> filterAlbumsToFetchFrom(List<AlbumEntity> entities, int maxFetch){
        final List<AlbumEntity> entitiesToFetch = new ArrayList<>();
        int canFetchCountTotal = 0;
        for(AlbumEntity entity: entities) {
            int canFetchCount = entity.getTotalItems() - entity.getItemsFetchedCount();

            if(canFetchCount > 0){
                entitiesToFetch.add(entity);
            }

            if(canFetchCountTotal + canFetchCount >= maxFetch){
                return entitiesToFetch;
            }
        }

        return entitiesToFetch;
    }

    public static List<Photo> photoEntitiesToModels(List<PhotoEntity> entities) {
        List<Photo> models = new ArrayList<>(entities.size());
        for(PhotoEntity entity: entities){
            models.add(photoEntityToModel(entity));
        }
        return models;
    }

    public static PhotoEntity photoRemoteToEntity(VKPhoto vkPhoto){
        PhotoEntity entity = new PhotoEntity();
        entity.setId(vkPhoto.getId());
        entity.setOwnerId(vkPhoto.getOwnerId());
        entity.setAlbumId(vkPhoto.getAlbumId());
        entity.setDate(vkPhoto.getDate());
        entity.setRepostsCount(vkPhoto.getReposts().getCount());
        entity.setLikesCount(vkPhoto.getLikes().getCount());
        entity.setText(vkPhoto.getText());
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
