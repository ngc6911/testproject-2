package org.vktest.vktestapp.data.remote.api;

import android.net.Uri;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiTokenInterceptor implements Interceptor {

    private String apiVersion;
    private String authToken;

    public ApiTokenInterceptor(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Uri uri = Uri.parse(API.BASE);

        if (request.method().equals("GET") &&
                chain.request().url().host().equals(uri.getAuthority())) {

            HttpUrl url = request.url().newBuilder()
                    .addQueryParameter("v", apiVersion)
                    .addQueryParameter("access_token", authToken)
                    .build();
            request = request.newBuilder().url(url).build();
        }
        return chain.proceed(request);
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
