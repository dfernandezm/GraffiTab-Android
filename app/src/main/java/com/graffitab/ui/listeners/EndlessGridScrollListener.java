package com.graffitab.ui.listeners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.graffitab.R;

import jp.co.recruit_mp.android.headerfootergridview.HeaderFooterGridView;

public class EndlessGridScrollListener implements OnScrollListener {

	public RelativeLayout mFooterView;
	
	private HeaderFooterGridView gridView;
	private ProgressBar mProgressBarLoadMore;
	private FrameLayout frameLayout;
	
	private LayoutInflater mInflater;
	
    private boolean isLoading;
    private boolean hasMorePages;
    private int pageNumber = 0;
    private GridScrollListener refreshList;
    private boolean isRefreshing;
    private boolean shouldStart;

    public EndlessGridScrollListener(HeaderFooterGridView gridView, GridScrollListener refreshList, boolean shouldStart) {
        this.gridView = gridView;
        this.isLoading = false;
        this.hasMorePages = true;
        this.refreshList = refreshList;
        this.shouldStart = shouldStart;
        this.mInflater = (LayoutInflater)gridView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	this.mFooterView = (RelativeLayout) mInflater.inflate(R.layout.row_endless_footer, gridView, false);
    	this.mProgressBarLoadMore = (ProgressBar) mFooterView.findViewById(R.id.load_more_progressBar);

    	frameLayout = new FrameLayout(gridView.getContext());
    	gridView.addFooterView(frameLayout);
    }

    @Override
    public void onScroll( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount ) {
        if (refreshList != null)
        	refreshList.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        
    	if ( shouldStart && gridView.getLastVisiblePosition() + 1 == totalItemCount && !isLoading ) {
            isLoading = true;
            
            if ( hasMorePages && !isRefreshing ) {
                isRefreshing = true;
                refreshList.onGridRefresh( pageNumber );
                
            	mProgressBarLoadMore.setVisibility(View.VISIBLE);
            }
        } else {
            isLoading = false;
        }
    }

    @Override
    public void onScrollStateChanged( AbsListView view, int scrollState ) {

    }

    public void startUpdates() {
        this.shouldStart = true;
        
        frameLayout.removeAllViews();
        frameLayout.addView(mFooterView);
    }
    
    public void noMorePages() {
        this.hasMorePages = false;
        
    	mProgressBarLoadMore.setVisibility(View.GONE);
    	frameLayout.removeAllViews();
    }

    public void notifyMorePages() {
    	hasMorePages = true;
        isRefreshing = false;
        pageNumber = pageNumber + 1;
    }

    public interface GridScrollListener {
        public void onGridRefresh(int pageNumber);
        
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }
}