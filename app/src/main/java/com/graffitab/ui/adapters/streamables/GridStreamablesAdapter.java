package com.graffitab.ui.adapters.streamables;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.adapters.BaseItemAdapter;

import java.util.List;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GridStreamablesAdapter extends BaseItemAdapter<Integer> {

//    private ImageLoader imageLoader;
    private GridView gridView;

    public GridStreamablesAdapter(Context context, GridView gridView, List<Integer> items) {
        super(context, items);

        this.gridView = gridView;
//        this.imageLoader = new ImageLoader(context, DisplayUtils.dpToPx(context, colWidth), DisplayUtils.dpToPx(context, colHeight));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if ( convertView == null ) {
            convertView = mInflater.inflate( R.layout.row_streamable_grid, null );

            holder = buildViewHolderForConvertView(convertView);

            convertView.setTag( holder );
        }
        else {
            holder = (ViewHolder) convertView.getTag();

            if ( holder == null ) {
                convertView = mInflater.inflate( R.layout.row_streamable_grid, null );

                holder = buildViewHolderForConvertView(convertView);

                convertView.setTag( holder );
            }
        }

        final Integer item = itemsList.get(position);

        // Configure thumbnail.
//        if (item instanceof GTStreamableTag)
//            imageLoader.loadImage(GTImageRequestBuilder.buildGetFullGraffiti(((GTStreamableTag) item).graffitiId), holder.streamableView, null);

        holder.streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));

        // Calculate cell dimensions.
        convertView.setLayoutParams(new GridView.LayoutParams(gridView.getColumnWidth(), gridView.getColumnWidth()));

        return convertView;
    }

    private ViewHolder buildViewHolderForConvertView(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.streamableView = (ImageView) convertView.findViewById(R.id.streamableView);

        return holder;
    }

    private class ViewHolder {
        private ImageView streamableView;
    }
}
