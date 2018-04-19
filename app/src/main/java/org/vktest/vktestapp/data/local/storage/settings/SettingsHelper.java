package org.vktest.vktestapp.data.local.storage.settings;

import android.content.SharedPreferences;

import org.vktest.vktestapp.data.local.storage.FileStorageType;

import javax.inject.Inject;

public class SettingsHelper implements Settings {

    public static final String SP_KEY_FILESTORAGE_MODE = "key-filestorage";

    private SharedPreferences mSharedPreferences;

    @Inject
    public SettingsHelper(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public void setStorageMode(FileStorageType type){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(SP_KEY_FILESTORAGE_MODE, type.ordinal());
        editor.commit();
    }

    @Override
    public FileStorageType getStorageMode(){
        return FileStorageType.values()[
                mSharedPreferences.getInt(
                        SP_KEY_FILESTORAGE_MODE, FileStorageType.Internal.ordinal())
                ];
    }


}
