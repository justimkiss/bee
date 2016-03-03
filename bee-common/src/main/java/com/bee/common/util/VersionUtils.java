package com.bee.common.util;

/**
 * Created by jeoy.zhou on 12/9/15.
 */
public class VersionUtils {

    public static int compareVersion(String version1, String version2) {
        String[] s1 = version1.split("\\.|-");
        String[] s2 = version2.split("\\.|-");

        int len1 = s1.length;
        int len2 = s2.length;
        int compareCount = len1;
        if (len1 <= len2) {
            compareCount = len1;
        } else if (len1 > len2) {
            compareCount = len2;
        }
        for (int i = 0; i < compareCount; i++) {
            int v1 = 0;
            try {
                v1 = Integer.parseInt(s1[i]);
            } catch (RuntimeException e) {
                return s1[i].compareToIgnoreCase(s2[i]);
            }
            int v2 = 0;
            try {
                v2 = Integer.parseInt(s2[i]);
            } catch (RuntimeException e) {
                return s1[i].compareToIgnoreCase(s2[i]);
            }
            int r = v1 - v2;
            if (r > 0) {
                return 1;
            }
            if (r < 0) {
                return -1;
            }
        }
        return len2 - len1;
    }
}
