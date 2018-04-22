package org.vktest.vktestapp.di.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

import org.vktest.vktestapp.AppExecutors;
import org.vktest.vktestapp.data.remote.AuthDataSource;
import org.vktest.vktestapp.data.remote.AuthHelper;
import org.vktest.vktestapp.data.remote.RemoteDS;
import org.vktest.vktestapp.data.remote.RemoteDataSource;
import org.vktest.vktestapp.data.remote.api.API;
import org.vktest.vktestapp.data.remote.api.ApiTokenInterceptor;
import org.vktest.vktestapp.data.remote.api.DateDeserializer;

import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = { BaseModule.class })
public class RemoteDataSourceModule {

    @Provides
    @Singleton
    public API provideAPI(Retrofit retrofit){
        return retrofit.create(API.class);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .create();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(API.BASE).build();
    }

    @Provides
    @Singleton
    public ApiTokenInterceptor provideApiTokenInterceptor(){
        ApiTokenInterceptor interceptor = new ApiTokenInterceptor(VKSdk.getApiVersion());
        VKAccessToken vkAccessToken = VKAccessToken.currentToken();
        if (vkAccessToken != null) {
            interceptor.setAuthToken(vkAccessToken.accessToken);
        }

        return interceptor;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(ApiTokenInterceptor apiTokenInterceptor){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(apiTokenInterceptor)
//                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public AuthDataSource provideAuthDataSource(AppExecutors executors,
                                                ApiTokenInterceptor interceptor){
        return new AuthHelper(executors, interceptor);
    }

    @Provides
    @Singleton
    public RemoteDataSource provideRemoteDataSourceModule(AppExecutors appExecutors, API api,
                                                          OkHttpClient okHttpClient){
        return new RemoteDS(appExecutors, api, okHttpClient);
    }
}
