package org.vktest.vktestapp.data.remote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public interface AuthDataSource {

    interface AutorizationCallback{
        void onSuccess();
        void onError();
    }

    void checkAuthorization(Context context, AutorizationCallback callback);
    void startAuthorization(Activity activity);
    void checkAuthorizationResult(int requestCode, int resultCode, Intent data,
                                  AutorizationCallback callback);

}
