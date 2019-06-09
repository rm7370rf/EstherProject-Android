package org.rm7370rf.estherproject.util;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ekalips.fancybuttonproj.FancyButton;

import org.rm7370rf.estherproject.R;

import java.util.ArrayList;
import java.util.List;

public class FieldDialog extends Dialog {
    private List<EditText> editTextList = new ArrayList<>();

    public FieldDialog(@NonNull Activity activity) {
        super(activity);
    }

    public TextView getTextView(int resource) {
        return getView().findViewById(resource);
    }

    public EditText getEditText(int resource) {
        EditText editText = getView().findViewById(resource);
        editTextList.add(editText);
        return editText;
    }

    public List<EditText> getEditTextList(List<Integer> resourceList) {
        List<EditText> list = new ArrayList<>();

        for (int resource : resourceList) {
            EditText editText = getView().findViewById(resource);
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
        FancyButton positiveBtn = getView().findViewById(R.id.positiveBtn),
                    negativeBtn = getView().findViewById(R.id.negativeBtn);

        if(name != null) {
            positiveBtn.setText(name);
        }

        positiveBtn.setOnClickListener(v -> {
            hideKeyboard();
            listener.onClick(positiveBtn);
        });
        negativeBtn.setOnClickListener(v-> hide());
    }

    public void hideKeyboard() {
        for (EditText et : editTextList) {
            Utils.hideKeyboard(et);
        }
    }

    public interface OnClickListener {
        void onClick(FancyButton button);
    }
}
