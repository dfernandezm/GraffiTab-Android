package com.graffitab.ui.fragments.locations;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.graffitab.R;
import com.graffitab.application.MyApplication;
import com.graffitab.ui.activities.home.me.locations.CreateLocationActivity;
import com.graffitab.ui.activities.home.streamables.explorer.ExplorerActivity;
import com.graffitab.ui.adapters.locations.GenericLocationsRecyclerViewAdapter;
import com.graffitab.ui.adapters.locations.OnLocationClickListener;
import com.graffitab.ui.dialog.DialogBuilder;
import com.graffitab.ui.dialog.handlers.OnYesNoHandler;
import com.graffitab.ui.fragments.GenericItemListFragment;
import com.graffitab.ui.views.recyclerview.components.AdvancedEndlessRecyclerViewAdapter;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewItemDecoration;
import com.graffitab.ui.views.recyclerview.components.AdvancedRecyclerViewLayoutConfiguration;
import com.graffitab.utils.display.DisplayUtils;
import com.graffitabsdk.config.GTSDK;
import com.graffitabsdk.model.GTLocation;
import com.graffitabsdk.network.common.response.GTResponse;
import com.graffitabsdk.network.common.response.GTResponseHandler;
import com.graffitabsdk.network.common.result.GTLocationDeletedResult;

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
        ExplorerActivity.openForLocation(getActivity(), location.latitude, location.longitude);
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
                    CreateLocationActivity.openForLocation(getActivity(), location);
                }
                else if (which == R.id.action_copy_address) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Address", location.address);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), getString(R.string.other_copied), Toast.LENGTH_SHORT).show();
                }
                else if (which == R.id.action_remove) {
                    DialogBuilder.buildYesNoDialog(getContext(), getString(R.string.app_name), getString(R.string.other_confirm_delete), getString(R.string.other_delete), getString(R.string.other_cancel), new OnYesNoHandler() {

                        @Override
                        public void onClickYes() {
                            deleteLocation(location, adapterPosition);
                        }

                        @Override
                        public void onClickNo() {}
                    });
                }
            }
        });
        builder.show();
    }

    // Locations

    private void deleteLocation(final GTLocation location, final int adapterPosition) {
        removeItemAtIndex(adapterPosition);
        GTSDK.getMeManager().deleteLocation(location.id, new GTResponseHandler<GTLocationDeletedResult>() {

            @Override
            public void onSuccess(GTResponse<GTLocationDeletedResult> gtResponse) {
                Log.i(getClass().getSimpleName(), "Location deleted");
            }

            @Override
            public void onFailure(GTResponse<GTLocationDeletedResult> gtResponse) {
                Log.i(getClass().getSimpleName(), "Could not delete location");
            }
        });
    }

    // Configuration

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new AdvancedRecyclerViewItemDecoration(DisplayUtils.isLandscape(getActivity()) ? 2 : 1, 20);
    }

    @Override
    public AdvancedEndlessRecyclerViewAdapter getAdapterForViewType() {
        GenericLocationsRecyclerViewAdapter a = new GenericLocationsRecyclerViewAdapter(MyApplication.getInstance(), items, getRecyclerView().getRecyclerView());
        a.setClickListener(this);
        return a;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManagerForViewType() {
        return new GridLayoutManager(MyApplication.getInstance(), 1);
    }

    @Override
    public AdvancedRecyclerViewLayoutConfiguration getLayoutConfiguration() {
        return new AdvancedRecyclerViewLayoutConfiguration(DisplayUtils.isLandscape(MyApplication.getInstance()) ? 2 : 1);
    }
}
