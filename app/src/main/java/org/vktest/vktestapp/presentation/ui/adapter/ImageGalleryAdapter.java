package org.vktest.vktestapp.presentation.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.data.local.cache.BitmapHelper;
import org.vktest.vktestapp.presentation.models.Photo;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.VHolder> {

    @Inject
    BitmapHelper bitmapHelper;

    private OnAdapterItemActionListener<Photo> onAdapterItemActionListener;

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_image_gallery, null);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        final Photo photo = bitmapHelper.getPhotosList().get(position);

        holder.itemView.setOnClickListener(v ->
        {
            bitmapHelper.setCurrentPhoto(position);
            onAdapterItemActionListener.onItemClick(photo, position);
        });

        bitmapHelper.setBitmapToImageView(
                bitmapHelper.getPhotosList().get(position).getPhotoId(),
                bitmapHelper.getPhotosList().get(position).getPhotoThumbBitmapPath(),
                holder.imgvPhoto);

        if(bitmapHelper.getPhotosList().size() - position <= 5){
            onAdapterItemActionListener.onScrollToBottom(photo);
        }
    }

    @Override
    public int getItemCount() {
        return bitmapHelper.getPhotosList().size();
    }

    public void addPhoto(Photo photo){
        if (!bitmapHelper.getPhotosList().contains(photo)){
            bitmapHelper.getPhotosList().add(photo);
            notifyItemInserted(bitmapHelper.getPhotosList().size() - 1);
        }
    }

    class VHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imgv_photo)
        ImageView imgvPhoto;

        public VHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnAdapterItemActionListener(OnAdapterItemActionListener<Photo> onAdapterItemActionListener) {
        this.onAdapterItemActionListener = onAdapterItemActionListener;
    }

    public void unsetOnAdapterItemActionListener() {
        this.onAdapterItemActionListener = null;
    }
}
