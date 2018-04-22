package org.vktest.vktestapp.data.local.cache;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import org.vktest.vktestapp.presentation.models.Photo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class LruImageCacheWrapper implements ImageCache, BitmapHelper {

    private LruCache<String, Bitmap> lruCache;
    private List<Photo> photosList = new ArrayList<>();
    private int currentPosition;

    private List<OnHelperDatasetChangesListener> mOnHelperDatasetChangesListeners = new ArrayList<>();

    public LruImageCacheWrapper() {

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
    public void setBitmapToImageView(Photo photo, ImageView view, boolean isThumb) {
        final String src = isThumb ? photo.getPhotoThumbBitmapPath() : photo.getPhotoBitmapPath();
        view.setImageBitmap(lruCache.get(src));
    }

    @Override
    public void addPhoto(Photo photo) {
        if(!photosList.contains(photo)) {
            photosList.add(photo);

            for (OnHelperDatasetChangesListener listener : mOnHelperDatasetChangesListeners) {
                listener.onDatasetChanges();
            }
        }
    }

    @Override
    public Photo getPhoto(int position) {
        return photosList.get(position);
    }

    @Override
    public Photo getCurrentPhoto() {
        return photosList.get(currentPosition);
    }

    @Override
    public void setCurrentPhoto(int position) {
        currentPosition = position;
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        lruCache.put(key, bitmap);
    }

    @Override
    public Bitmap getBitmap(String key) {
        return lruCache.get(key);
    }

    @Override
    public int getPhotoCount() {
        return photosList.size();
    }

    @Override
    public void addOnHelperDatasetChangesListener(OnHelperDatasetChangesListener listener) {
        mOnHelperDatasetChangesListeners.add(listener);
    }
}
