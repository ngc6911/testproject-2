package org.vktest.vktestapp.presentation.ui.imagegallery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.presenters.ImageGalleryPresenter;
import org.vktest.vktestapp.presentation.ui.adapter.ImageGalleryAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageGalleryFragment extends MvpFragment implements ImageGalleryView{

    @BindView(R.id.rcv_gallery)
    RecyclerView rcvGallery;

    ImageGalleryAdapter galleryAdapter = new ImageGalleryAdapter();

    public static ImageGalleryFragment newInstance() {
        return new ImageGalleryFragment();
    }

    @InjectPresenter
    ImageGalleryPresenter mImageGalleryPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_gallery_layout, container, false);
        ButterKnife.bind(this, v);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        rcvGallery.setAdapter(galleryAdapter);
        rcvGallery.setLayoutManager(staggeredGridLayoutManager);
        return v;
    }

    @Override
    public void renderPhotos(List<Photo> photos) {
        galleryAdapter.addPhotos(photos);
    }

    @Override
    public void renderError(int errStringId) {

    }
}
