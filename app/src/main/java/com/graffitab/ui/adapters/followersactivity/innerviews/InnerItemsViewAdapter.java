package com.graffitab.ui.adapters.followersactivity.innerviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.graffitab.R;
import com.graffitabsdk.model.GTStreamable;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by david on 19/03/2017.
 */

public class InnerItemsViewAdapter extends RecyclerView.Adapter<InnerItemsViewAdapter.PhotoDetailViewHolder> {

    private List<GTStreamable> items;

    public void setItems(List<GTStreamable> items) {
        this.items = items;
    }

    @Override
    public InnerItemsViewAdapter.PhotoDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_activity_like_image, parent, false);
        return new PhotoDetailViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(InnerItemsViewAdapter.PhotoDetailViewHolder holder, int position) {
        GTStreamable itemStreamable = items.get(position);
        holder.bindImage(itemStreamable);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class PhotoDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView innerItemImage;

        public PhotoDetailViewHolder(View v) {
            super(v);
            innerItemImage = (ImageView) v.findViewById(R.id.innerItemImage);
        }

        @Override
        public void onClick(View view) {
            //TODO: Launch single streamable Activity
        }

        public void bindImage(GTStreamable streamable) {
            Picasso.with(innerItemImage.getContext()).load(streamable.asset.thumbnail).into(innerItemImage);
        }
    }
}
