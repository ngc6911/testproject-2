package org.vktest.vktestapp;

import android.app.Application;

import org.vktest.vktestapp.di.AppComponent;
import org.vktest.vktestapp.di.DaggerAppComponent;
import org.vktest.vktestapp.di.modules.BaseModule;
import org.vktest.vktestapp.di.modules.RemoteDataSourceModule;
import org.vktest.vktestapp.di.modules.RepositoryModule;

public class TestApp extends Application {

    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sAppComponent = DaggerAppComponent.builder()
                .baseModule(new BaseModule(this))
                .remoteDataSourceModule(new RemoteDataSourceModule())
                .repositoryModule(new RepositoryModule())
                .build();
    }

    public static AppComponent getsAppComponent() {
        return sAppComponent;
    }
}
