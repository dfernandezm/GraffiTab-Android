package com.graffitab.ui.adapters.streamables.viewholders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.graffitab.application.MyApplication;
import com.graffitab.utils.device.DeviceUtils;
import com.graffitabsdk.model.GTStreamable;

import butterknife.ButterKnife;

/**
 * Created by georgichristov on 17/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class SwimlaneStreamableViewHolder extends StreamableViewHolder {

    public SwimlaneStreamableViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setItem(GTStreamable streamable) {
        super.setItem(streamable);

        streamableView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtils.pxToDip(MyApplication.getInstance(), item.asset.thumbnailHeight)));
    }
}
