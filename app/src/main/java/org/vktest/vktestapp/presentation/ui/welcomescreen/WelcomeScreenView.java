package org.vktest.vktestapp.presentation.ui.welcomescreen;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface WelcomeScreenView extends MvpView {
    void renderLoginSuccess();
    void renderLoginStart();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void renderLoginEnd();
}
