package com.graffitab.ui.listeners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.graffitab.R;

public class EndlessListScrollListener implements OnScrollListener {

	public RelativeLayout mFooterView;
	
	private ProgressBar mProgressBarLoadMore;
	private FrameLayout frameLayout;
	
	private LayoutInflater mInflater;
    private boolean isLoading;
    private boolean hasMorePages;
    private int pageNumber = 0;
    private ListScrollListener refreshList;
    private boolean isRefreshing;
    private boolean shouldStart;

    public EndlessListScrollListener( ListView listView, ListScrollListener refreshList, boolean shouldStart ) {
    	this.isLoading = false;
        this.hasMorePages = true;
        this.refreshList = refreshList;
        this.shouldStart = shouldStart;
    	this.mInflater = (LayoutInflater)listView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	this.mFooterView = (RelativeLayout) mInflater.inflate(R.layout.row_endless_footer, listView, false);
    	this.mProgressBarLoadMore = (ProgressBar) mFooterView.findViewById(R.id.load_more_progressBar);

    	frameLayout = new FrameLayout(listView.getContext());
    	listView.addFooterView(frameLayout);
    }

    @Override
    public void onScroll( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount ) {
    	if (refreshList != null)
    		refreshList.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    	
    	if ( shouldStart && (firstVisibleItem + visibleItemCount) >= totalItemCount && !isLoading ) {
            isLoading = true;
            
            if ( hasMorePages && !isRefreshing ) {
                isRefreshing = true;
                refreshList.onListRefresh( pageNumber );
                
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

    public interface ListScrollListener {
        public void onListRefresh(int pageNumber);
        
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }
}