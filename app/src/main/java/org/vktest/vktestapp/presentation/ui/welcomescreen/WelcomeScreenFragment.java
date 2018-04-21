package org.vktest.vktestapp.presentation.ui.welcomescreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.presentation.presenters.WelcomeScreenPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeScreenFragment extends MvpAppCompatFragment implements WelcomeScreenView {

    @BindView(R.id.bttn_login)
    Button bttnLogin;

    public static WelcomeScreenFragment newInstance() {
        return new WelcomeScreenFragment();
    }

    @InjectPresenter
    WelcomeScreenPresenter mWelcomeScreenPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_welcome_screen_layout, container, false);

        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void renderLoginSuccess() {

    }

    @OnClick(R.id.bttn_login)
    public void onBttnLoginClick(View v){
        mWelcomeScreenPresenter.initLogin(getActivity());
    }

    @Override
    public void renderLoginStart() {

    }

    @Override
    public void renderLoginEnd() {

    }

}
