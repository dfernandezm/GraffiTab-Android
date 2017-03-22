package com.graffitab.ui.adapters.followersactivity.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.graffitab.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by david on 19/03/2017.
 */

public class FollowersActivityItemsAdapter<T> extends RecyclerView.Adapter<FollowersActivityItemsAdapter.PhotoDetailViewHolder> {

    private List<T> items;
    private ImageExtractor<T> imageExtractor;

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void setImageExtractor(ImageExtractor<T> imageExtractor) {
        this.imageExtractor = imageExtractor;
    }

    @Override
    public FollowersActivityItemsAdapter.PhotoDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_activity_like_image, parent, false);
        return new PhotoDetailViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(FollowersActivityItemsAdapter.PhotoDetailViewHolder holder, int position) {
        holder.bindImage(imageExtractor.getImageUrl(items.get(position)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PhotoDetailViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView innerItemImage;

        public PhotoDetailViewHolder(View v) {
            super(v);
            innerItemImage = (ImageView) v.findViewById(R.id.innerItemImage);
        }

        @Override
        public void onClick(View view) {
            //TODO: Launch single streamable Activity
        }

        public void bindImage(String imageUrl) {
            Picasso.with(innerItemImage.getContext()).load(imageUrl).into(innerItemImage);
        }
    }

    public interface ImageExtractor<T> {
        String getImageUrl(T activityDetailItem);
    }
}
