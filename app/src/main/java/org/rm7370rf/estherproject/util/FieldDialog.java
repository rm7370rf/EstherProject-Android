package org.rm7370rf.estherproject.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;

import java.util.ArrayList;
import java.util.List;

public class FieldDialog extends AlertDialog.Builder {
    protected Activity activity;
    protected View view;
    protected List<EditText> editTextList = new ArrayList<>();
    protected AlertDialog dialog;

    protected FieldDialog(@NonNull Context context) {
        super(context);

    }

    public void setLayout(int resource) {
        view = activity.getLayoutInflater().inflate(resource, null);
        setView(view);
    }

    public TextView getTextView(int resource) {
        return view.findViewById(resource);
    }

    public EditText getEditText(int resource) {
        EditText editText = view.findViewById(resource);
        editTextList.add(editText);
        return editText;
    }

    public List<EditText> getEditTextList(List<Integer> resourceList) {
        List<EditText> list = new ArrayList<>();

        for (int resource : resourceList) {
            EditText editText = view.findViewById(resource);
            list.add(editText);
            editTextList.add(editText);
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

        positiveBtn.setOnClickListener(v -> {
            hideKeyboard();
            listener.onClick(positiveBtn);
        });
        negativeBtn.setOnClickListener(v-> dialog.cancel());
    }

    public void hideKeyboard() {
        for (EditText et : editTextList) {
            Utils.hideKeyboard(et);
        }

    }

    public void hide() {
        dialog.hide();
    }

    @Override
    public AlertDialog show() {
        dialog = create();
        return dialog;
    }

    public interface OnClickListener {
        void onClick(FancyButton button);
    }
}
