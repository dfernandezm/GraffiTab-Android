package com.graffitab.ui.adapters.locations.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.ui.adapters.locations.OnLocationClickListener;
import com.graffitab.utils.google.GoogleUtils;
import com.graffitabsdk.model.GTLocation;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class LocationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.thumbnail) public ImageView thumbnail;
    @BindView(R.id.address) public TextView address;
    @BindView(R.id.menuButton) public ImageView menu;

    protected GTLocation item;
    protected OnLocationClickListener clickListener;

    public LocationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
    }

    public void setItem(GTLocation location) {
        this.item = location;

        address.setText(location.address);

        loadImage();
    }

    public void loadImage() {
        String url = GoogleUtils.getStreetViewUrl(item.latitude, item.longitude);
        Picasso.with(thumbnail.getContext()).load(url).into(thumbnail);
    }

    public GTLocation getItem() {
        return item;
    }

    public void setClickListener(OnLocationClickListener listener) {
        this.clickListener = listener;
    }

    // Setup

    protected void setupViews() {
        menu.setClickable(true);
        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onMenuSelected(item, getAdapterPosition());
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onRowSelected(item, getAdapterPosition());
            }
        });
    }
}
