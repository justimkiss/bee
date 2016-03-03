package com.bee.remote.provider.service.method;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by jeoy.zhou on 12/10/15.
 */
public class ServiceParamDesc {

    private String[] paramNames;
    private int hashCode;

    public ServiceParamDesc(String[] paramNames) {
        this.paramNames = paramNames;

        StringBuilder sb = new StringBuilder();
        for (String paramName : paramNames) {
            sb.append(paramName).append("@");
        }
        this.hashCode = sb.toString().hashCode();
    }

    public int getLength() {
        return this.paramNames.length;
    }

    public String[] getParamNames() {
        return this.paramNames;
    }

    public int hashCode() {
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ServiceParamDesc) {
            return this.hashCode == obj.hashCode();
        }
        return false;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
