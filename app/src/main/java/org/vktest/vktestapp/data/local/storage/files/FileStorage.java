package org.vktest.vktestapp.data.local.storage.files;

import android.graphics.Bitmap;

import org.vktest.vktestapp.data.local.storage.FileStorageType;

import java.io.IOException;

public interface FileStorage {
    boolean isExternalStorageWritable();

    boolean isExternalStorageReadable();

    void putBitmap(Bitmap bitmap, String fileName,
                   FileStorageType fileStorageType) throws IOException;

    Bitmap getBitmap(String fileName, FileStorageType fileStorageType) throws IOException;
}
