package org.vktest.vktestapp.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import org.vktest.vktestapp.AppExecutors;
import org.vktest.vktestapp.data.local.LocalDS;
import org.vktest.vktestapp.data.local.LocalDataSource;
import org.vktest.vktestapp.data.local.cache.LruImageCacheWrapper;
import org.vktest.vktestapp.data.local.db.ImagesDatabase;
import org.vktest.vktestapp.data.local.storage.files.FileStorage;
import org.vktest.vktestapp.data.local.storage.files.FileStorageHelper;
import org.vktest.vktestapp.data.local.storage.settings.Settings;
import org.vktest.vktestapp.data.local.storage.settings.SettingsHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = { BaseModule.class })
public class LocalDataSourceModule {

    @Provides
    public SharedPreferences provideSharedPreferences(Context context){
        return context.getSharedPreferences(Settings.APP_SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }

    @Provides
    public Settings provideSettings(SharedPreferences sharedPreferences){
        return new SettingsHelper(sharedPreferences);
    }

    @Provides
    public FileStorage provideFileStorage(Context context){
        return new FileStorageHelper(context);
    }

    @Provides
    @Singleton
    public ImagesDatabase provideImagesDatabase(Context context){
        return Room.databaseBuilder(context, ImagesDatabase.class,
                ImagesDatabase.DB_NAME).build();
    }

    @Provides
    @Singleton
    public LruImageCacheWrapper provideBitmapCache(){
        return new LruImageCacheWrapper();
    }

    @Provides
    @Singleton
    public LocalDataSource provideLocalDataSourceModule(AppExecutors appExecutors,
                                                        ImagesDatabase imagesDatabase,
                                                        FileStorage storage, Settings settings){
        return new LocalDS(appExecutors, storage, settings, imagesDatabase);
    }
}
