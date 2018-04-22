package org.vktest.vktestapp.data.local.cache;

import android.graphics.Bitmap;
import android.widget.ImageView;

import org.vktest.vktestapp.presentation.models.Photo;

public interface BitmapHelper {
    void setBitmapToImageView(Photo photo, ImageView view, boolean isThumb);

    void setCurrentPhoto(int position);
    int getCurrentPosition();
    Photo getCurrentPhoto();
    void addPhoto(Photo photo);
    Photo getPhoto(int position);
    int getPhotoCount();

    interface OnHelperDatasetChangesListener {
        void onDatasetChanges();
    }

    void addOnHelperDatasetChangesListener(OnHelperDatasetChangesListener listener);
}
