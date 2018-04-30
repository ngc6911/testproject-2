package org.vktest.vktestapp.data.local.cache;

import android.widget.ImageView;

import org.vktest.vktestapp.presentation.models.Photo;

import java.util.List;

public interface BitmapHelper {
    void setBitmapToImageView(Photo photo, ImageView view, boolean isThumb);

    void setCurrentPhoto(int position);
    int getCurrentPosition();
    void addPhoto(Photo photo);
    Photo getPhoto(int position);
    int getPhotoCount();

    interface OnHelperDatasetChangesListener {
        void onDatasetChanges(int position);
    }

    void addOnHelperDatasetChangesListener(OnHelperDatasetChangesListener listener);
}
