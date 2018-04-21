package org.vktest.vktestapp.presentation.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Photo {

    private long photoId;
    private long albumId;
    private String photoBitmapPath;
    private String photoThumbBitmapPath;
    private Date date;

    private boolean fetched = false;

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

    public boolean isFetched() {
        return fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
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

    public String getFormattedDate() {
        SimpleDateFormat sd = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy ",
                Locale.getDefault());
        return sd.format(getDate());
    }

    public String getText() {
        return text;
    }

    public boolean hasText(){
        return text != null && !text.isEmpty();
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getRepostsCount() {
        return repostsCount;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Photo){
            Photo p = (Photo) obj;
            return photoId == p.photoId;
        } else {
            return false;
        }
    }
}
