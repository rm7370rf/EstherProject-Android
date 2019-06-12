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

    public enum RefreshType {
        FIRST,
        AFTER_START,
        BY_REQUEST
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

    public void start(RefreshType refreshType) {
        switch (refreshType) {
            case FIRST:
                if(progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if(recyclerView != null) {
                    recyclerView.setVisibility(View.GONE);
                }
                break;
            case AFTER_START:
                if(topProgressBar != null) {
                    topProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void stop(RefreshType refreshType) {
        switch (refreshType) {
            case FIRST:
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                if(recyclerView != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                break;
            case AFTER_START:
                if (topProgressBar != null) {
                    topProgressBar.setVisibility(View.GONE);
                }
                break;
            case BY_REQUEST:
                if(swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
        }
    }
}
