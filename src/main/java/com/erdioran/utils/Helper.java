package com.erdioran.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Helper {

    private static final Logger LOGGER = LogManager.getLogger(Helper.class);

    private Helper() {
    }

    public static void sleepInSeconds(int sleepInSeconds) {
        try {
            LOGGER.debug("wait for seconds : " + sleepInSeconds);
            Thread.sleep(sleepInSeconds * 1000L);
        } catch (Exception e) {
        }
    }

    public static void sleepMs(int sleepInMiliSeconds) {
        try {
            LOGGER.debug("wait for seconds : " + sleepInMiliSeconds);
            Thread.sleep(sleepInMiliSeconds);
        } catch (Exception e) {
        }
    }

}
