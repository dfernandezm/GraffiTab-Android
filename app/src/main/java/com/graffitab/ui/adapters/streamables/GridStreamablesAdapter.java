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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class GridStreamablesAdapter extends BaseItemAdapter<Integer> {

    private GridView gridView;

    public GridStreamablesAdapter(Context context, GridView gridView, List<Integer> items) {
        super(context, items);

        this.gridView = gridView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if ( convertView == null ) {
            convertView = mInflater.inflate( R.layout.row_streamable_grid, null );
            holder = new ViewHolder(convertView);
            convertView.setTag( holder );
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            if ( holder == null ) {
                convertView = mInflater.inflate( R.layout.row_streamable_grid, null );
                holder = new ViewHolder(convertView);
                convertView.setTag( holder );
            }
        }

        final Integer item = itemsList.get(position);

        holder.streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));

        // Calculate cell dimensions.
        convertView.setLayoutParams(new GridView.LayoutParams(gridView.getColumnWidth(), gridView.getColumnWidth()));

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.streamableView) ImageView streamableView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
