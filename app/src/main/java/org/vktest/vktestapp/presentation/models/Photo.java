package org.vktest.vktestapp.presentation.models;

import java.util.Date;

public class Photo {

    private long photoId;
    private long albumId;
    private String photoBitmapPath;
    private String photoThumbBitmapPath;
    private Date date;

    private String text = "";
    private int likesCount = 0;
    private int repostsCount = 0;

    public Photo(long albumId, long photoId, String photoBitmap, String photoThumbBitmapPath, Date date) {
        this.photoId = photoId;
        this.albumId = albumId;
        this.photoBitmapPath = photoBitmap;
        this.photoThumbBitmapPath = photoThumbBitmapPath;
        this.date = date;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setRepostsCount(int repostsCount) {
        this.repostsCount = repostsCount;
    }

    public long getPhotoId() {
        return photoId;
    }

    public String getPhotoBitmapPath() {
        return photoBitmapPath;
    }

    public String getPhotoThumbBitmapPath() {
        return photoThumbBitmapPath;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getRepostsCount() {
        return repostsCount;
    }
}
