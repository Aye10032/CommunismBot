package com.aye10032.foundation.utils;

import java.util.Arrays;

public class ExceptionUtils {

    public static String printStack(Throwable e) {
        StringBuilder builder = new StringBuilder();
        builder.append(e).append("\n");
        Arrays.stream(e.getStackTrace()).forEach(element -> builder.append("\n").append(element));
        if (e.getCause() == null) {
            return builder.toString();
        } else {
            builder.append("\n").append(printStack(e.getCause()));
        }
        return builder.toString();
    }

    public static String printStack(StackTraceElement[] e) {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(e).forEach(element -> builder.append("\n").append(element));
        return builder.toString();
    }


}
