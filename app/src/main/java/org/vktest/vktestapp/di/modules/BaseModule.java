package org.vktest.vktestapp.di.modules;

import android.app.Application;
import android.content.Context;

import com.vk.sdk.VKSdk;

import org.vktest.vktestapp.AppExecutors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseModule {

    private Context mContext;

    public BaseModule(Application appContext) {
        mContext = appContext;
        VKSdk.initialize(mContext);
    }

    @Provides
    @Singleton
    public Context provideContext(){
        return mContext;
    }

    @Provides
    @Singleton
    public AppExecutors provideAppExecutors(){
        return new AppExecutors();
    }
}
