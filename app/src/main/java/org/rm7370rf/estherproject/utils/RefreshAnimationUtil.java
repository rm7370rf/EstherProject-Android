package org.rm7370rf.estherproject.utils;

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

    public RefreshAnimationUtil(ProgressBar topProgressBar, ProgressBar progressBar, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView) {
        this.topProgressBar = topProgressBar;
        this.progressBar = progressBar;
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.recyclerView = recyclerView;
    }

    public void start(int refreshType) {
        switch (refreshType) {
            case RefreshType.FIRST:
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                break;
            case RefreshType.AFTER_START:
                topProgressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void stop(int refreshType) {
        switch (refreshType) {
            case RefreshType.FIRST:
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case RefreshType.AFTER_START:
                topProgressBar.setVisibility(View.GONE);
                break;
            case RefreshType.BY_REQUEST:
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }
}
