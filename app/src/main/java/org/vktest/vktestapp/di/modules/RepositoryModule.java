package org.vktest.vktestapp.di.modules;

import android.content.Context;

import org.vktest.vktestapp.data.Repository;
import org.vktest.vktestapp.data.RepositoryImpl;
import org.vktest.vktestapp.data.local.LocalDataSource;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.data.local.cache.ImageCache;
import org.vktest.vktestapp.data.local.cache.LruImageCacheWrapper;
import org.vktest.vktestapp.data.remote.AuthDataSource;
import org.vktest.vktestapp.data.remote.RemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = { RemoteDataSourceModule.class, LocalDataSourceModule.class })
public class RepositoryModule {

    @Provides
    @Singleton
    public ImageCache provideImageCache(LruImageCacheWrapper wrapper){
        return wrapper;
    }

    @Provides
    @Singleton
    public BitmapHelper provideBitmapHelper(LruImageCacheWrapper wrapper){
        return wrapper;
    }

    @Provides
    @Singleton
    public Repository provideRepositoryModule(AuthDataSource authDataSource,
                                              LocalDataSource localDataSource,
                                              RemoteDataSource remoteDataSource,
                                              ImageCache cache,
                                              Context context){
        return new RepositoryImpl(authDataSource, localDataSource, remoteDataSource, cache, context);
    }
}
