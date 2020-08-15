package com.aye10032.Utils;

import java.util.Arrays;

public class ExceptionUtils {

    public static String printStack(Throwable e) {
        StringBuilder builder = new StringBuilder();
        builder.append(e).append("\n");
        Arrays.stream(e.getStackTrace()).forEach(element -> builder.append("\n").append(element));
        return builder.toString();
    }

    public static String printStack(StackTraceElement[] e) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(e).forEach(element -> builder.append("\n").append(element));
        return builder.toString();
    }


}
