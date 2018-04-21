package org.vktest.vktestapp.data.remote.api;

import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

public class VKPhoto {

    private Long id;
    private Long albumId;
    private Long ownerId;

    private List<Size> sizes;

    private String text;
    private Date date;

    private Likes likes;
    private Reposts reposts;

    public VKPhoto(Long id, Long albumId, Long ownerId) {
        this.id = id;
        this.albumId = albumId;
        this.ownerId = ownerId;
    }

    @Nullable
    public Size getSizeClassData(SizeClass sizeClass){
        for(Size size: sizes){
            if(size.type == sizeClass){
                return size;
            }
        }

        return null;
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

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
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

    public Likes getLikes() {
        return likes;
    }

    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    public Reposts getReposts() {
        return reposts;
    }

    public void setReposts(Reposts reposts) {
        this.reposts = reposts;
    }

    public class Likes {
        Integer userLikes;
        Integer count;

        public Integer getUserLikes() {
            return userLikes;
        }

        public void setUserLikes(Integer userLikes) {
            this.userLikes = userLikes;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    public class Reposts {
        Integer count;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    public class Size {
        String src;
        Integer width;
        Integer height;
        SizeClass type;

        public Size(String src, Integer width, Integer height, SizeClass type) {
            this.src = src;
            this.width = width;
            this.height = height;
            this.type = type;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public SizeClass getType() {
            return type;
        }

        public void setType(SizeClass type) {
            this.type = type;
        }
    }

    public enum SizeClass{
        s, m, x, o, p, q, r, y, z, w
    }
}
