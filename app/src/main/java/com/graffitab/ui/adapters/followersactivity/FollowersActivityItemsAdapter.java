package com.graffitab.ui.adapters.followersactivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.graffitab.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by david on 19/03/2017.
 */

public class FollowersActivityItemsAdapter<T> extends RecyclerView.Adapter<FollowersActivityItemsAdapter.PhotoDetailViewHolder> {

    private List<T> items;
    private ImageLoader<T> imageLoader;

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setImageLoader(ImageLoader<T> imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public FollowersActivityItemsAdapter.PhotoDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_activity_item_image, parent, false);
        return new PhotoDetailViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(FollowersActivityItemsAdapter.PhotoDetailViewHolder holder, int position) {
       imageLoader.loadImage(holder.innerItemImage, items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class PhotoDetailViewHolder<T> extends RecyclerView.ViewHolder {

        @BindView(R.id.innerItemImage)
        ImageView innerItemImage;

        public PhotoDetailViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public interface ImageLoader<T> {
        void loadImage(ImageView imageView, T activityDetailItem);
    }
}
