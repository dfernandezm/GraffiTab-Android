package com.graffitab.ui.adapters.streamables.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.utils.device.DeviceUtils;
import com.graffitab.utils.image.ImageUtils;
import com.graffitabsdk.model.GTStreamable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class TrendingStreamableViewHolder extends StreamableViewHolder {

    @BindView(R.id.avatar) public ImageView avatar;
    @BindView(R.id.usernameField) public TextView usernameField;

    public TrendingStreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTStreamable streamable) {
        super.setItem(streamable);

        usernameField.setText(streamable.user.username);
        streamableView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtils.pxToDip(MyApplication.getInstance(), item.asset.thumbnailHeight)));

        loadAvatar();
    }

    @Override
    public void loadStreamableImage() {
        streamableView.loadImage(item.asset.link);
    }

    public void loadAvatar() {
        ImageUtils.setAvatar(avatar, item.user);
    }

    @Override
    protected void setupViews() {
        super.setupViews();

        View.OnClickListener profileListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onClickUserProfile();
            }
        };

        avatar.setClickable(true);
        avatar.setOnClickListener(profileListener);
        usernameField.setClickable(true);
        usernameField.setOnClickListener(profileListener);

        streamableView.setLikeImageScale(0.45f);
    }
}
