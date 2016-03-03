package com.bee.config.utills;

import com.bee.common.constants.Constants;
import com.bee.common.constants.PropertiesKeyConstants;
import com.bee.config.ConfigManager;
import com.bee.config.loader.ConfigManagerLoader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by jeoy.zhou on 12/8/15.
 */
public class InterfaceUtils {

    private static final Logger LOGGER = Logger.getLogger(InterfaceUtils.class);

    private static String[] INTERFACE_PACKAGE;

    static {
        ConfigManager configManager = ConfigManagerLoader.getConfigManager();
        String packageStr = configManager.getConfig(PropertiesKeyConstants.PROVIDER_INTERFACE_PACKAGE);
        if(StringUtils.isBlank(packageStr)) {
            packageStr = Constants.DEFAULT_INTERFACEUTILS_PACKAGE;
        }
        INTERFACE_PACKAGE = Constants.COMMA_SPLIT_PATTERN.split(packageStr);
    }

    public static Class<?> getInterface(Class<?> clz) {
        Class<?>[] interfaces = clz.getInterfaces();
        if(interfaces != null && interfaces.length > 0) {
            return interfaces[0];
        }
        List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(clz);
        if(CollectionUtils.isNotEmpty(allInterfaces)) {
            for(Class<?> tmp : allInterfaces) {
                if(InterfaceUtils.isValidType(tmp)) {
                    return tmp;
                }
            }
        }
        return clz;
    }

    private static boolean isValidType(Class<?> clz) {
        String className = clz.getName();
        for(String pkg : INTERFACE_PACKAGE) {
            if(className.startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }
}
