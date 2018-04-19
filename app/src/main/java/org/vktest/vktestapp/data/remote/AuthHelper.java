package org.vktest.vktestapp.data.remote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.vktest.vktestapp.AppExecutors;
import org.vktest.vktestapp.data.remote.api.ApiTokenInterceptor;

import javax.inject.Singleton;

@Singleton
public class AuthHelper implements AuthDataSource {

    private AppExecutors mAppExecutors;
    private ApiTokenInterceptor mTokenInterceptor;

    private VKAccessTokenTracker mAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(@Nullable VKAccessToken oldToken,
                                           @Nullable VKAccessToken newToken) {
            if (newToken != null) {
                mTokenInterceptor.setAuthToken(newToken.accessToken);
            }
        }
    };

    public AuthHelper(AppExecutors appExecutors, ApiTokenInterceptor interceptor) {
        mAppExecutors = appExecutors;
        mTokenInterceptor = interceptor;
    }

    @Override
    public void startAuthorization(Activity activity) {
            mAppExecutors.getNetworkIO().execute(
                    () -> VKSdk.login(activity, VKScope.PHOTOS, VKScope.GROUPS));
    }

    @Override
    public void checkAuthorization(Context context, AuthDataSource.AutorizationCallback callback) {
        if(VKAccessToken.currentToken() != null) {
            mAppExecutors.getNetworkIO().execute(
                    () -> VKSdk.wakeUpSession(context, new VKCallback<VKSdk.LoginState>() {
                        @Override
                        public void onResult(VKSdk.LoginState res) {
                            switch (res) {
                                case LoggedIn:
                                    callback.onSuccess();
                                    break;
                                case Pending:
                                    break;
                                case LoggedOut:
                                    break;
                                case Unknown:
                                    break;
                            }
                        }

                        @Override
                        public void onError(VKError error) {

                        }
                    }));
        }
    }

    @Override
    public void checkAuthorizationResult(int requestCode, int resultCode, Intent data,
                                         AutorizationCallback callback) {
        mAppExecutors.getDiskIO().execute(
                () -> VKSdk.onActivityResult(requestCode, resultCode, data,
                        new VKCallback<VKAccessToken>() {
                            @Override
                            public void onResult(VKAccessToken res) {
                                mTokenInterceptor.setAuthToken(res.accessToken);

                                if(!mAccessTokenTracker.isTracking()){
                                    mAccessTokenTracker.startTracking();
                                }

                                mAppExecutors.getMainThread().execute(callback::onSuccess);
                            }

                            @Override
                            public void onError(VKError error) {
                                mAppExecutors.getMainThread().execute(callback::onError);
                            }
                        }));
    }
}
