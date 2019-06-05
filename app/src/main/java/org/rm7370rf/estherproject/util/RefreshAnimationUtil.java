package org.rm7370rf.estherproject.util;

import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class RefreshAnimationUtil {
    private ProgressBar topProgressBar,
                        progressBar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public class RefreshType {
        public static final int FIRST = 1;
        public static final int AFTER_START = 2;
        public static final int BY_REQUEST = 3;
    }

    public void setTopProgressBar(ProgressBar topProgressBar) {
        this.topProgressBar = topProgressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void start(int refreshType) {
        switch (refreshType) {
            case RefreshType.FIRST:
                if(progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if(recyclerView != null) {
                    recyclerView.setVisibility(View.GONE);
                }
                break;
            case RefreshType.AFTER_START:
                if(topProgressBar != null) {
                    topProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void stop(int refreshType) {
        switch (refreshType) {
            case RefreshType.FIRST:
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                if(recyclerView != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                break;
            case RefreshType.AFTER_START:
                if (topProgressBar != null) {
                    topProgressBar.setVisibility(View.GONE);
                }
                break;
            case RefreshType.BY_REQUEST:
                if(swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
        }
    }
}
