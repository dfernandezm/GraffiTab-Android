package com.graffitab.ui.adapters.followersactivity.viewholders;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.graffitab.R;
import com.graffitab.ui.adapters.followersactivity.OnFollowerActivityClickListener;
import com.graffitab.ui.adapters.followersactivity.innerviews.FollowersActivityItemsAdapter;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTStreamable;
import com.graffitabsdk.model.GTUser;
import com.graffitabsdk.model.activity.GTActivity;
import com.graffitabsdk.model.activity.GTActivityContainer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Generification
 *
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

    private RecyclerView activityDetailRecyclerView;
    protected FollowersActivityItemsAdapter followersActivityItemsAdapter;

    protected GTActivityContainer item;
    protected OnFollowerActivityClickListener clickListener;
    private List<GTStreamable> streamables;

    public ActivityContainerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        setupViews();
        safelyBindViews();
    }

    public ActivityContainerViewHolder(View itemView, List<GTStreamable> streamables) {
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
        if (itemView.findViewById(R.id.activityDetailRecyclerView) != null) {
            bindStreamablesRecyclerView();
        }
    }

    private void bindStreamablesRecyclerView() {
        activityDetailRecyclerView = (RecyclerView) itemView.findViewById(R.id.activityDetailRecyclerView);
        LinearLayoutManager activityDetailLinearLayoutManager = new LinearLayoutManager(itemView.getContext());
        activityDetailLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        activityDetailRecyclerView.setLayoutManager(activityDetailLinearLayoutManager);
        followersActivityItemsAdapter = new FollowersActivityItemsAdapter();
        activityDetailRecyclerView.setAdapter(followersActivityItemsAdapter);
    }

    protected List<GTStreamable> getStreamablesFromActivityContainer(GTActivityContainer item) {
        List<GTStreamable> streamables = new ArrayList<>();
        for (GTActivity activity: item.activities) {
            streamables.add(extractStreamable(activity));
        }
        return streamables;
    }

    protected GTStreamable extractStreamable(GTActivity gtActivity) {
        // For subclasses to override
        throw new UnsupportedOperationException("Called extractStreamable on generic Activity Container");
    }

}
