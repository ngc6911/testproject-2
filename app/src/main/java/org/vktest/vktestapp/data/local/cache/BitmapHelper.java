package org.vktest.vktestapp.data.local.cache;

import android.widget.ImageView;

import org.vktest.vktestapp.presentation.models.Photo;

import java.util.List;

public interface BitmapHelper {
    void setBitmapToImageView(long id, String src, ImageView view);
    List<Photo> getPhotosList();
    void setCurrentPhoto(int position);
    int getCurrentPosition();
    Photo getCurrentPhoto();
}
