package org.vktest.vktestapp.data.local.storage.settings;

import org.vktest.vktestapp.data.local.storage.FileStorageType;

public interface Settings {

    public static final String APP_SHARED_PREFERENCES_NAME = "name-base-prefs";

    void setStorageMode(FileStorageType type);

    FileStorageType getStorageMode();
}
