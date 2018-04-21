package org.vktest.vktestapp.di;

import org.vktest.vktestapp.data.RepositoryImpl;
import org.vktest.vktestapp.di.modules.RepositoryModule;
import org.vktest.vktestapp.presentation.presenters.ImageGalleryPresenter;
import org.vktest.vktestapp.presentation.presenters.WelcomeScreenPresenter;
import org.vktest.vktestapp.presentation.ui.adapter.ImageGalleryAdapter;
import org.vktest.vktestapp.presentation.ui.imageviewer.ImageViewerActivity;
import org.vktest.vktestapp.presentation.ui.imageviewer.ImageViewerFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { RepositoryModule.class })
public interface AppComponent {

    void inject(WelcomeScreenPresenter welcomeScreenPresenter);
    void inject(ImageGalleryPresenter imageGalleryPresenter);
    void inject(RepositoryImpl repository);
    void inject(ImageGalleryAdapter imageGalleryAdapter);
    void inject(ImageViewerFragment imageViewerFragment);
    void inject(ImageViewerActivity imageViewerActivity);
}
