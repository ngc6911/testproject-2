package org.vktest.vktestapp.di;

import org.vktest.vktestapp.di.modules.RepositoryModule;
import org.vktest.vktestapp.presentation.presenters.ImageGalleryPresenter;
import org.vktest.vktestapp.presentation.presenters.WelcomeScreenPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { RepositoryModule.class })
public interface AppComponent {

    void inject(WelcomeScreenPresenter welcomeScreenPresenter);
    void inject(ImageGalleryPresenter welcomeScreenPresenter);
}
