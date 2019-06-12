package org.rm7370rf.estherproject.expception;

import android.content.Context;

public class VerifierException extends Exception {
    private int resource;

    public VerifierException(int resource) {
        super();
        this.resource = resource;
    }

    public int getResource() {
        return resource;
    }
}
