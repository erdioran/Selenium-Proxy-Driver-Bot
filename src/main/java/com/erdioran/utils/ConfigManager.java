package com.erdioran.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigManager {

    private static final String TEST_CONFIG_FILE = Paths.get("src/test/resources/app.properties").toString();
    private static final Logger LOGGER = LogManager.getLogger(ConfigManager.class);
    private static Map<String, String> configMap = new HashMap<>();

    private ConfigManager() {
    }

    public static synchronized String getConfigProperty(String key) {
        if (configMap.size() == 0) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(TEST_CONFIG_FILE));
                configMap = new HashMap<>(properties.entrySet()
                        .stream()
                        .collect(Collectors.toMap(e -> e.getKey().toString(),
                                e -> e.getValue().toString())));
                LOGGER.debug("Loaded config properties : " + TEST_CONFIG_FILE);
            } catch (Exception e) {
                LOGGER.error(e);
                throw new RuntimeException(e);
            }
        }
        return configMap.get(key);
    }
    public static Boolean isHeadless() {
        return StringUtils.isNotBlank(System.getProperty("headless")) ? Boolean.parseBoolean(System.getProperty("headless"))
                : Boolean.parseBoolean(getConfigProperty("headless"));
    }

    public static Integer getExplicitWaitTime() {
        return getIntValue("explicit.wait.time", 45);
    }

    public static Integer getMaxRetryCount() {
        return Integer.parseInt(System.getProperty("max.retry.count", getStringValue("max.retry.count", String.valueOf(1))));
    }


    public static String getBrowser() {
        return System.getProperty("browser", getStringValue("browser", "chrome"));
    }


    public static Integer getIntValue(String key, Integer defaultValue) {
        return StringUtils.isNotBlank(getRunTimeValue(key)) ? Integer.parseInt(getRunTimeValue(key)) : defaultValue;
    }

    public static String getStringValue(String key, String defaultValue) {
        return getRunTimeValue(key, defaultValue);
    }

    public static Boolean getBooleanValue(String key, boolean defaultValue) {
        return StringUtils.isNotBlank(getRunTimeValue(key)) ? Boolean.parseBoolean(getRunTimeValue(key)) : defaultValue;
    }

    public static String getRunTimeValue(String key, String defaultValue) {
        Assert.assertTrue(StringUtils.isNotBlank(key), "Key can not be null");
        if (StringUtils.isNotBlank(System.getenv(key))) {
            return System.getenv(key);
        } else if (StringUtils.isNotBlank(System.getProperty(key))) {
            return System.getProperty(key);
        } else if (StringUtils.isNotBlank(ConfigManager.getConfigProperty(key))) {
            return ConfigManager.getConfigProperty(key);
        } else {
            return defaultValue;
        }
    }

    public static String getRunTimeValue(String key) {
        return getRunTimeValue(key, null);
    }


    public static Integer getReadApiLimit() {
        return getIntValue("read.api.limit", 100);
    }

}
