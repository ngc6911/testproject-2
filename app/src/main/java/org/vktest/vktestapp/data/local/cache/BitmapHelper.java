package org.vktest.vktestapp.data.local.cache;

import android.widget.ImageView;

import org.vktest.vktestapp.presentation.models.Photo;

import java.util.List;

public interface BitmapHelper {
    void setBitmapToImageView(String src, ImageView view);
    List<Photo> getPhotosList();
    void setCurrentPhoto(Photo photo, int position);
    Photo getCurrentPhoto();
    int getCurrentPosition();
}
