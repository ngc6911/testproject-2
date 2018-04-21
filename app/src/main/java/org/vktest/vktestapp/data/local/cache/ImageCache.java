package org.vktest.vktestapp.data.local.cache;

import android.graphics.Bitmap;

public interface ImageCache {
    void putBitmap(String key, Bitmap bitmap);
}
