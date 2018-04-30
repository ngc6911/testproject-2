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
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.presenters.ImageGalleryPresenter;
import org.vktest.vktestapp.presentation.ui.adapter.ImageGalleryAdapter;
import org.vktest.vktestapp.presentation.ui.adapter.OnAdapterItemActionListener;
import org.vktest.vktestapp.presentation.ui.helpers.PositionSensitiveLayoutManager;
import org.vktest.vktestapp.presentation.ui.imageviewer.ImageViewerActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageGalleryFragment extends MvpAppCompatFragment implements ImageGalleryView,
        OnAdapterItemActionListener<Photo> {

    @BindView(R.id.rcv_gallery)
    RecyclerView rcvGallery;

    PositionSensitiveLayoutManager mPositionSensitiveLayoutManager;

    public static ImageGalleryFragment newInstance() {
        return new ImageGalleryFragment();
    }

    @Inject
    BitmapHelper bitmapHelper;

    @InjectPresenter
    ImageGalleryPresenter mImageGalleryPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestApp.getsAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.fragment_gallery_layout, container, false);
        ButterKnife.bind(this, v);

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(bitmapHelper);
        adapter.setOnAdapterItemActionListener(this);

        mPositionSensitiveLayoutManager =
                new PositionSensitiveLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        rcvGallery.setAdapter(adapter);
        rcvGallery.setLayoutManager(mPositionSensitiveLayoutManager);
        rcvGallery.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    @Override
    public void renderPhoto(Photo photo) {

    }

    @Override
    public void renderDatasetChanges(int position) {
        RecyclerView.Adapter adapter = rcvGallery.getAdapter();
        if (adapter != null) {
            adapter.notifyItemInserted(position);
        }
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
        if(mPositionSensitiveLayoutManager.getVisibleCount() > 0) {

            mImageGalleryPresenter.getPhotos(lastItem);
        }
    }
}
