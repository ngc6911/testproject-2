package org.vktest.vktestapp.data.local.cache;

import android.graphics.Bitmap;

public interface ImageCache {
    Bitmap getBitmap(String key);
    void putBitmap(String key, Bitmap bitmap);
}
