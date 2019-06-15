package org.rm7370rf.estherproject.util;

import android.content.Context;

import org.rm7370rf.estherproject.expception.VerifierException;

public class Toast {
    public static void show(Context context, String text) {
        android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, Throwable e) {
        String text = (e instanceof VerifierException) ? context.getString(((VerifierException) e).getResource()) : e.getLocalizedMessage();
        android.widget.Toast.makeText(context, text, android.widget.Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }
}
