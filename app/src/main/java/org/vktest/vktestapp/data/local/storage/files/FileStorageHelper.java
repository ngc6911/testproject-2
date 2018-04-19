package org.vktest.vktestapp.data.local.storage.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.vktest.vktestapp.data.local.storage.FileStorageType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

public class FileStorageHelper implements FileStorage {

    private Context mContext;

    @Inject
    public FileStorageHelper(Context context) {
        mContext = context;
    }

    @Override
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void write(Bitmap bitmap, File file) throws IOException {
        if(file.createNewFile()){

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } else {
            throw new IOException(String.format("File %s already exists",
                    file.getAbsolutePath()));
        }
    }


    @Override
    public void putBitmap(Bitmap bitmap, String fileName,
                          FileStorageType fileStorageType) throws IOException {
        File file;
        switch (fileStorageType){
            case External:
                file = new File(mContext
                        .getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
                break;
            case Internal:
                file = new File(mContext.getFilesDir(), fileName);
                break;
            default:
                throw new IOException("Unrecognized storage type");
        }

        write(bitmap, file);
    }

    @Override
    public Bitmap getBitmap(String fileName, FileStorageType fileStorageType) throws IOException {
        File file;
        switch (fileStorageType){
            case External:
                file = new File(mContext
                        .getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
                break;
            case Internal:
                file = new File(mContext.getFilesDir(), fileName);
                break;
            default:
                throw new IOException("Unrecognized storage type");
        }

        return BitmapFactory.decodeStream(new FileInputStream(file));
    }
}
