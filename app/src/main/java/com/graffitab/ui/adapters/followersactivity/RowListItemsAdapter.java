package com.graffitab.ui.adapters.followersactivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.graffitab.R;

import java.util.List;

/**
 * Created by david on 19/03/2017.
 */

public class RowListItemsAdapter<T> extends RecyclerView.Adapter<RowListItemsAdapter.PhotoDetailViewHolder> {

    private List<T> items;
    private ImageLoader<T> imageLoader;
    private int imageItemLayoutId;

    public RowListItemsAdapter(int imageItemLayoutId) {
        this.imageItemLayoutId = imageItemLayoutId;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setImageLoader(ImageLoader<T> imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public RowListItemsAdapter.PhotoDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(imageItemLayoutId, parent, false);
        return new PhotoDetailViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RowListItemsAdapter.PhotoDetailViewHolder holder, int position) {
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
