package com.graffitab.ui.adapters.followersactivity.viewholders;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.graffitab.R;
import com.graffitab.ui.adapters.followersactivity.OnFollowerActivityClickListener;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.model.activity.GTActivityContainer;
import com.squareup.picasso.Picasso;

/**
 * Created by david on 16/03/2017.
 */

public class ActivityContainerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.topTimelineSeparator) public View topTimelineSeparator;
    @BindView(R.id.bottomTimelineSeparator) public View bottomTimelineSeparator;
    @BindView(R.id.dateField) public TextView dateField;
    @BindView(R.id.actionLbl) public TextView actionLbl;
    @BindView(R.id.avatar) public ImageView avatar;

    public TextView descriptionLbl;
    public ImageView itemImage;

    protected GTActivityContainer item;
    protected OnFollowerActivityClickListener clickListener;

    public ActivityContainerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
        safelyBindViews();
    }

    public void setItem(GTActivityContainer activityContainer) {
        this.item = activityContainer;
        dateField.setText(DateUtils.getRelativeTimeSpanString(item.date.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
    }

    public GTActivityContainer getItem() {
        return item;
    }

    public void setClickListener(OnFollowerActivityClickListener listener) {
        this.clickListener = listener;
    }

    public void loadAvatar(GTUser user) {
        ImageUtils.setAvatar(avatar, user);
    }

    public void loadStreamable(GTStreamable streamable) {
        Picasso.with(itemImage.getContext()).load(streamable.asset.thumbnail).into(itemImage);
    }

    // Setup

    protected void setupViews() {
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onRowSelected(item, getAdapterPosition());
            }
        });
    }

    private void safelyBindViews() {
        if (itemView.findViewById(R.id.descriptionLbl) != null) descriptionLbl = (TextView) itemView.findViewById(R.id.descriptionLbl);
        if (itemView.findViewById(R.id.itemImage) != null) itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
    }
}
