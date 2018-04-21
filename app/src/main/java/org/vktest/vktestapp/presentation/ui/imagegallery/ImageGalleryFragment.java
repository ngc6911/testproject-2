package org.vktest.vktestapp.presentation.ui.imagegallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.presenters.ImageGalleryPresenter;
import org.vktest.vktestapp.presentation.ui.adapter.ImageGalleryAdapter;
import org.vktest.vktestapp.presentation.ui.adapter.OnAdapterItemActionListener;
import org.vktest.vktestapp.presentation.ui.imageviewer.ImageViewerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageGalleryFragment extends MvpAppCompatFragment implements ImageGalleryView,
        OnAdapterItemActionListener<Photo> {

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

        TestApp.getsAppComponent().inject(galleryAdapter);
        galleryAdapter.setOnAdapterItemActionListener(this);

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rcvGallery.setAdapter(galleryAdapter);
        rcvGallery.setLayoutManager(staggeredGridLayoutManager);
        rcvGallery.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @Override
    public void renderPhoto(Photo photo) {
        galleryAdapter.addPhoto(photo);
    }

    @Override
    public void renderError(int errStringId) {

    }

    @Override
    public void onItemClick(Photo item, int position) {
        Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onScrollToBottom(Photo lastItem) {
        mImageGalleryPresenter.getPhotos(lastItem);
    }
}
