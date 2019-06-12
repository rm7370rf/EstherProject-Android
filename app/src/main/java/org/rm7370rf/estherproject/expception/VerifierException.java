package org.rm7370rf.estherproject.expception;

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
