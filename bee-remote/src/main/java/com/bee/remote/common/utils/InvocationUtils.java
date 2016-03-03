package com.bee.remote.common.utils;

/**
 * Created by jeoy.zhou on 3/2/16.
 */
public final class InvocationUtils {

    public static String getRemoteCallFullName(String methodName, Object[] parameters) {
        if (parameters != null) {
            StringBuilder str = new StringBuilder(methodName).append("(");
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] == null) {
                    str.append("null,");
                } else {
                    str.append(parameters[i].getClass().getName()).append(",");
                }
            }
            if (parameters.length > 0) {
                str.deleteCharAt(str.length() - 1);
            }
            str.append(")");
            return str.toString();
        } else {
            return methodName;
        }
    }
}
