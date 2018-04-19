package org.vktest.vktestapp.data.local.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.Locale;

@Entity(tableName = "photos", foreignKeys = @ForeignKey(
                                                        entity = AlbumEntity.class,
                                                        parentColumns = "id",
                                                        childColumns = "album_id",
                                                        onDelete = ForeignKey.CASCADE))
public class PhotoEntity {

    @PrimaryKey
    private Long id;

    @ColumnInfo(name = "album_id")
    private Long albumId;

    @ColumnInfo(name = "owner_id")
    private Long ownerId;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "small_image_uri")
    private String smallImageURI;

    @ColumnInfo(name = "large_image_uri")
    private String largeImageURI;

    @ColumnInfo(name = "likes_count")
    private Integer likesCount;

    @ColumnInfo(name = "reposts_count")
    private Integer repostsCount;

    @ColumnInfo(name = "is_fetched")
    private Boolean isFetched;

    public Boolean getFetched() {
        return isFetched;
    }

    public void setFetched(Boolean fetched) {
        isFetched = fetched;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLargeImageURI() {
        return largeImageURI;
    }

    public void setLargeImageURI(String largeImageURI) {
        this.largeImageURI = largeImageURI;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(Integer repostsCount) {
        this.repostsCount = repostsCount;
    }

    public String getSmallImageURI() {
        return smallImageURI;
    }

    public void setSmallImageURI(String smallImageURI) {
        this.smallImageURI = smallImageURI;
    }

    public String getSmallPhotoFilename(){
        return String.format(Locale.getDefault(), "thumb_%d_%d_%d.png",
                getOwnerId(), getAlbumId(), getId());
    }

    public String getBigPhotoFilename(){
        return String.format(Locale.getDefault(), "%d_%d_%d.png",
                getOwnerId(), getAlbumId(), getId());
    }
}
