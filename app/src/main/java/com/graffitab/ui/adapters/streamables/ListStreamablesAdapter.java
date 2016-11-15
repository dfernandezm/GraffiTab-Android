package com.graffitab.ui.adapters.streamables;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.graffitab.R;
import com.graffitab.constants.Constants;
import com.graffitab.ui.adapters.BaseItemAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by georgichristov on 15/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public class ListStreamablesAdapter extends BaseItemAdapter<Integer> {

    private GridView gridView;

    public ListStreamablesAdapter(Context context, GridView gridView, List<Integer> items) {
        super(context, items);

        this.gridView = gridView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if ( convertView == null ) {
            convertView = mInflater.inflate( R.layout.row_streamable_list, null );
            holder = new ViewHolder(convertView);
            convertView.setTag( holder );
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            if ( holder == null ) {
                convertView = mInflater.inflate( R.layout.row_streamable_list, null );
                holder = new ViewHolder(convertView);
                convertView.setTag( holder );
            }
        }

        final Integer item = itemsList.get(position);

        holder.streamableView.setBackgroundColor(Color.parseColor(Constants.PALLETE[position % Constants.PALLETE.length]));

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.avatar) ImageView avatar;
        @BindView(R.id.nameField) TextView nameField;
        @BindView(R.id.usernameField) TextView usernameField;
        @BindView(R.id.dateField) TextView dateField;
        @BindView(R.id.streamableView) ImageView streamableView;
        @BindView(R.id.likesField) TextView likesField;
        @BindView(R.id.commentsField) TextView commentsField;
        @BindView(R.id.likeStatusImage) ImageView likeStatusImage;
        @BindView(R.id.likeButton) View likeButton;
        @BindView(R.id.commentButton) View commentButton;
        @BindView(R.id.shareButton) View shareButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
