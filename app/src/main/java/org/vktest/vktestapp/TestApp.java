package org.vktest.vktestapp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

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
