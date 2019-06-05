package org.rm7370rf.estherproject.other;

public class RefreshType {
    public static final int FIRST = 1;
    public static final int AFTER_START = 2;
    public static final int BY_REQUEST = 3;

    public static boolean isFirst(int refreshType) {
        return (refreshType == FIRST);
    }

    public static boolean isAfterStart(int refreshType) {
        return (refreshType == AFTER_START);
    }

    public static boolean isByRequest(int refreshType) {
        return (refreshType == BY_REQUEST);
    }
}
