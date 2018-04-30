package org.vktest.vktestapp.presentation.ui.imageviewer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.TestApp;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.models.Photo;
import org.vktest.vktestapp.presentation.presenters.ImageViewerPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageViewerFragment extends MvpAppCompatFragment implements ImageViewerView {

    public static final String FRAGMENT_PAGER_POSITION = "bundle-key-pager-position";

    public static ImageViewerFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_PAGER_POSITION, position);
        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @BindView(R.id.imgv_photo_fullsize)
    ImageView imgvPhotoFullsize;

    @BindView(R.id.txv_image_date)
    TextView txvImageDate;

    @BindView(R.id.txv_image_description)
    TextView txvImageDescription;

    @BindView(R.id.txv_likes_and_reposts)
    TextView txvLikesAndReposts;

    @Inject
    BitmapHelper bitmapHelper;

    @BindView(R.id.photo_info_block)
    View photoInfoBlock;

    @BindView(R.id.viewer_root)
    View viewRoot;

    @InjectPresenter
    ImageViewerPresenter imageViewerPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestApp.getsAppComponent().inject(this);
        int position = 0;
        if (getArguments() != null) {
            position = getArguments().getInt(FRAGMENT_PAGER_POSITION);
        }
        imageViewerPresenter.getFullsizePhoto(position);
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

    @OnClick(R.id.viewer_root)
    public void toggleInfo(View v){
        if(photoInfoBlock.getVisibility() == View.VISIBLE){
            photoInfoBlock.setVisibility(View.GONE);
        } else {
            photoInfoBlock.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void renderPhoto(Photo photo) {
        txvImageDate.setText(photo.getFormattedDate());
        if(photo.hasText()){
            txvImageDescription.setVisibility(View.VISIBLE);
            txvImageDescription.setText(photo.getText());
        }

        String template = getResources().getString(R.string.txv_likes_and_reposts_template);
        txvLikesAndReposts.setText(String.format(template,
                photo.getLikesCount(), photo.getRepostsCount()));


        bitmapHelper.setBitmapToImageView(
                photo,
                imgvPhotoFullsize,
                false);
    }

    @Override
    public void renderDatasetChanges() {

    }

    @Override
    public void renderError(int errStringId) {

    }
}
