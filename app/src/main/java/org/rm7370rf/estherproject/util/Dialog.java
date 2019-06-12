package org.rm7370rf.estherproject.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;

import moxy.MvpAppCompatDialogFragment;

public class Dialog extends MvpAppCompatDialogFragment {
    protected int layoutId;

    public Dialog() { }

    public void setLayout(int layoutId) {
        this.layoutId = layoutId;
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layoutId, null);
    }


    @Override
    public android.app.Dialog onCreateDialog(final Bundle savedInstanceState) {
        RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        final android.app.Dialog dialog = new android.app.Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }

    public void show(FragmentManager manager) {
        super.show(manager, String.valueOf(layoutId));
    }

    public interface OnCompleteListener {
        void onComplete();
    }
}