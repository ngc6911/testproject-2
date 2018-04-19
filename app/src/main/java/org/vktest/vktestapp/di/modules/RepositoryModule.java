package org.vktest.vktestapp.di.modules;

import android.content.Context;

import org.vktest.vktestapp.data.Repository;
import org.vktest.vktestapp.data.RepositoryImpl;
import org.vktest.vktestapp.data.local.LocalDataSource;
import org.vktest.vktestapp.data.remote.AuthDataSource;
import org.vktest.vktestapp.data.remote.RemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = { RemoteDataSourceModule.class, LocalDataSourceModule.class  })
public class RepositoryModule {

    @Provides
    @Singleton
    public Repository provideRepositoryModule(AuthDataSource authDataSource,
                                              LocalDataSource localDataSource,
                                              RemoteDataSource remoteDataSource,
                                              Context context){
        return new RepositoryImpl(authDataSource, localDataSource, remoteDataSource, context);
    }
}
