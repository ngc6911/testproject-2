package org.vktest.vktestapp.presentation.ui.imageviewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.presenters.ImageGalleryPresenter;
import org.vktest.vktestapp.presentation.ui.imagegallery.ImageGalleryView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageViewerFragment extends MvpAppCompatFragment implements ImageGalleryView {

    public static Fragment newInstance() {
        return new ImageViewerFragment();
    }
    
    @BindView(R.id.imgv_photo_fullsize)
    ImageView imgvPhotoFullsize;

    @Inject
    BitmapHelper bitmapHelper;

    @InjectPresenter
    ImageGalleryPresenter imageGalleryPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestApp.getsAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_viewer_layout, container, false);
        
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void renderPhoto(Photo photo) {
        bitmapHelper.setBitmapToImageView(photo.getPhotoBitmapPath(), imgvPhotoFullsize);
    }

    @Override
    public void renderError(int errStringId) {

    }
}
