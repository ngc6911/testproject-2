package org.vktest.vktestapp.data.local.cache;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import org.vktest.vktestapp.data.local.LocalDataSource;
import org.vktest.vktestapp.presentation.models.Photo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LruImageCacheWrapper implements ImageCache, BitmapHelper {

    private LruCache<String, Bitmap> lruCache;
    private LocalDataSource mLocalDataSource;

    private List<Photo> photosList = new ArrayList<>();
    private Photo currentPhoto = null;
    private int currentPosition;

    @Inject
    public LruImageCacheWrapper(LocalDataSource localDataSource) {

        mLocalDataSource = localDataSource;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 10;

        lruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    @Override
    public void setBitmapToImageView(String src, ImageView view) {
        final Bitmap bitmap = lruCache.get(src);
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
        } else {
            mLocalDataSource.getPhoto(src, new LocalDataSource.GetPhotoCallback() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    lruCache.put(src, bitmap);
                    setBitmapToImageView(src, view); //If bitmap is still null, there will be onError call, huh?
                }

                @Override
                public void onError() {
                    //WOW! Here we can insert into view some stub image
                    //like "sorry, ur picture is gone!!!" or something
                }
            });
        }
    }

    @Override
    public void setCurrentPhoto(Photo photo, int position) {
        currentPhoto = photo;
        currentPosition = position;
    }

    @Override
    public Photo getCurrentPhoto() {
        return currentPhoto;
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public List<Photo> getPhotosList() {
        return photosList;
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        lruCache.put(key, bitmap);
    }
}
