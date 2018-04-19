package org.vktest.vktestapp.presentation.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.vktest.vktestapp.R;
import org.vktest.vktestapp.presentation.models.Photo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.VHolder> {

    private List<Photo> photosList = new ArrayList<>();

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_image_gallery, null);
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
//        holder.imgvPhoto.setImageBitmap(photosList.get(position).getPhotoBitmapPath());
    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public void addPhotos(List<Photo> photos){
        photosList.addAll(photos);
        notifyDataSetChanged();
    }

    class VHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imgv_photo)
        ImageView imgvPhoto;

        public VHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
