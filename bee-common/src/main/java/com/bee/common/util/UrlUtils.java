package com.bee.common.util;

import com.bee.common.constants.Constants;
import com.bee.common.entity.UrlConfig;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;

/**
 * Created by jeoy.zhou on 1/5/16.
 */
public class UrlUtils {

    public static UrlConfig getUrlConfig(String url) {
        if(StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("UrlUtils:getUrlConfig attribute[url] is null");
        }
        Matcher matcher = Constants.BEE_SERVICE_URL_PATTERN.matcher(url);
        if(!matcher.find() || matcher.groupCount() != 2) {
            throw new IllegalArgumentException(String.format("UrlUtils:getUrlConfig attribute[url:%s] is invalid", url));
        }
        return new UrlConfig(matcher.group(1), matcher.group(2));
    }
}
