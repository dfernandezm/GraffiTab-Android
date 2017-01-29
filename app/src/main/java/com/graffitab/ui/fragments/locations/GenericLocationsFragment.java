package com.graffitab.ui.fragments.locations;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.activities.home.me.locations.CreateLocationActivity;
import com.graffitab.ui.activities.home.streamables.explorer.ExplorerActivity;
import com.graffitab.ui.adapters.locations.GenericLocationsRecyclerViewAdapter;
import com.graffitab.ui.adapters.locations.OnLocationClickListener;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitabsdk.model.GTLocation;

/**
 * Created by georgichristov on 14/11/2016
 * --
 * Copyright © GraffiTab Inc. 2016
 */
public abstract class GenericLocationsFragment extends GenericItemListFragment<GTLocation> implements OnLocationClickListener {

    @Override
    public int emptyViewImageResource() {
        return -1;
    }

    @Override
    public String emptyViewTitle() {
        return getString(R.string.other_empty_no_locations);
    }

    @Override
    public String emptyViewSubtitle() {
        return getString(R.string.other_empty_no_locations_description);
    }

    @Override
    public void onRowSelected(GTLocation location, int adapterPosition) {
        startActivity(new Intent(getActivity(), ExplorerActivity.class));
    }

    @Override
    public void onMenuSelected(final GTLocation location, final int adapterPosition) {
        BottomSheet.Builder builder = new BottomSheet.Builder(getActivity(), R.style.BottomSheet_StyleDialog)
                .title(R.string.locations_menu_title)
                .sheet(R.menu.menu_locations);

        builder = builder.listener(new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == R.id.action_edit) {
                    startActivity(new Intent(getActivity(), CreateLocationActivity.class));
                }
                else if (which == R.id.action_copy_address) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Address", location.address);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), getString(R.string.other_copied), Toast.LENGTH_SHORT).show();
                }
                else if (which == R.id.action_remove) {
                    adapter.removeItem(adapterPosition, getRecyclerView().getRecyclerView());
                }
            }
        });
        builder.show();
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new AdvancedRecyclerViewItemDecoration(1, 0);
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        GenericLocationsRecyclerViewAdapter a = new GenericLocationsRecyclerViewAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
        a.setClickListener(this);
        return a;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        return new LinearLayoutManager(MyApplication.getInstance());
    }

    @Override
    public AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration() {
        return null;
    }
}
