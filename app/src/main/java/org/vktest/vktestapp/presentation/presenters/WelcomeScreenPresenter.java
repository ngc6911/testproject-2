package org.vktest.vktestapp.presentation.presenters;

import android.app.Activity;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;

import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.Repository;
import org.vktest.vktestapp.presentation.ui.welcomescreen.WelcomeScreenView;

import javax.inject.Inject;

@InjectViewState
public class WelcomeScreenPresenter extends BasePresenter<WelcomeScreenView>{

    @Inject
    Repository mRepository;

    public WelcomeScreenPresenter() {
        TestApp.getsAppComponent().inject(this);
        checkAuth();
    }

    public void initLogin(Activity activity){
        mRepository.autrorize(activity);
    }

    private void checkAuth(){
        mRepository.checkAuthorizationState(new Repository.AutorizationCallback() {
            @Override
            public void onSuccess() {
                getViewState().renderLoginEnd();
                getViewState().renderLoginSuccess();
            }

            @Override
            public void onPending(int pendingMessageResId) {

            }

            @Override
            public void onError() {

            }
        });
    }

    public void onVKApiResult(int requestCode, int resultCode, Intent data){
        mRepository.checkAuthorizationResult(requestCode, resultCode, data,
                new Repository.AutorizationCallback() {
                    @Override
                    public void onSuccess() {
                        getViewState().renderLoginSuccess();
                    }

                    @Override
                    public void onPending(int pendingMessageResId) {
                        getViewState().renderLoginStart();
                    }

                    @Override
                    public void onError() {
                        getViewState().renderLoginEnd();
                    }
                });
    }


}

