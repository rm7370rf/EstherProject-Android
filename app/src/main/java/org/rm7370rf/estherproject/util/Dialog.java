package org.rm7370rf.estherproject.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.FragmentManager;

import moxy.MvpDialogFragment;

public class Dialog extends MvpDialogFragment {
    protected int layoutId;

    public Dialog() { }

    public void setLayout(int layoutId) {
        this.layoutId = layoutId;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layoutId, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    public void show(FragmentManager manager) {
        super.show(manager, String.valueOf(layoutId));
    }
}