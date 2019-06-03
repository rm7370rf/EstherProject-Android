package org.rm7370rf.estherproject.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;

import java.util.ArrayList;
import java.util.List;

public class FieldDialog {
    private Activity activity;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private View view;

    public FieldDialog(Activity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
    }

    public void setLayout(int resource) {
        view = activity.getLayoutInflater().inflate(resource, null);
        builder.setView(view);
    }

    public Context getContext() {
        return activity;
    }

    public TextView getTextView(int resource) {
        return view.findViewById(resource);
    }

    public EditText getEditText(int resource) {
        return view.findViewById(resource);
    }

    public List<EditText> getEditTextList(List<Integer> resourceList) {
        List<EditText> list = new ArrayList<>();

        for (int resource : resourceList) {
            list.add(view.findViewById(resource));
        }

        return list;
    }

    public void setOnClickListener(FieldDialog.OnClickListener listener) {
        setOnClickListener(null, listener);
    }

    public void setOnClickListener(int resource, FieldDialog.OnClickListener listener) {
        setOnClickListener(getContext().getString(resource), listener);
    }

    public void setOnClickListener(String name, FieldDialog.OnClickListener listener) {
        FancyButton positiveBtn = view.findViewById(R.id.positiveBtn),
                    negativeBtn = view.findViewById(R.id.negativeBtn);

        if(name != null) {
            positiveBtn.setText(name);
        }

        positiveBtn.setOnClickListener(v -> listener.onClick(positiveBtn));
        negativeBtn.setOnClickListener(v-> alertDialog.cancel());
    }

    public void hide() {
        alertDialog.hide();
    }

    public void show() {
        alertDialog = builder.create();
        alertDialog.show();
    }

    public interface OnClickListener {
        void onClick(FancyButton button);
    }
}
