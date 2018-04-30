package org.vktest.vktestapp.data.local.cache;

import android.graphics.Bitmap;
import android.support.v7.util.DiffUtil;
import android.util.LruCache;
import android.widget.ImageView;

import org.vktest.vktestapp.data.local.LocalDataSource;
import org.vktest.vktestapp.data.local.db.entities.PhotoEntity;
import org.vktest.vktestapp.presentation.models.Photo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LruImageCacheWrapper implements ImageCache, BitmapHelper {

    private LruCache<String, Bitmap> lruCache;
    private final List<Photo> photosList = new ArrayList<>();
    private int currentPosition;
    private LocalDataSource mLocalDataSource;
    private List<OnHelperDatasetChangesListener> mOnHelperDatasetChangesListeners = new ArrayList<>();

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
    public void setBitmapToImageView(Photo photo, ImageView view, boolean isThumb) {
        final String src = isThumb ? photo.getPhotoThumbBitmapPath() : photo.getPhotoBitmapPath();
        Bitmap b = lruCache.get(src);
        if (b != null) {
            view.setImageBitmap(b);
        } else {
            mLocalDataSource.getPhoto(photo.getPhotoId(), src,
                    new LocalDataSource.GetPhotoCallback() {
                @Override
                public void onSuccess(PhotoEntity entity, Bitmap bitmap) {
                    lruCache.put(src, bitmap);
                    view.setImageBitmap(bitmap);
                }

                @Override
                public void onError() {

                }
            });
        }

    }

    @Override
    public void addPhoto(Photo photo) {
        synchronized (photosList){
            if(!photosList.contains(photo)){
                photosList.add(photo);

                for (OnHelperDatasetChangesListener listener : mOnHelperDatasetChangesListeners) {
                    listener.onDatasetChanges(photosList.size() - 1);
                }
            }
        }
    }

    @Override
    public Photo getPhoto(int position) {
        return photosList.get(position);
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
