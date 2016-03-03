package com.bee.common.extension.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeoy.zhou on 12/5/15.
 */
public class ExtensionNull implements Serializable {

    public static final ExtensionNull EMPTY = new ExtensionNull();

    public static final List<ExtensionNull> LIST_EMPTY = new ArrayList<ExtensionNull>();

    private ExtensionNull(){}

}
