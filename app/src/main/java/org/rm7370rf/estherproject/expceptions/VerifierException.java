package org.rm7370rf.estherproject.expceptions;

import android.content.Context;

public class VerifierException extends Exception {
    public VerifierException(Context context, int resource) {
        super(context.getString(resource));
    }
}
