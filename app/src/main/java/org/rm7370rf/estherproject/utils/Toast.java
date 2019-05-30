package org.rm7370rf.estherproject.utils;

import android.content.Context;

public class Toast {
    public static void show(Context context, String text) {
        android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }
}
