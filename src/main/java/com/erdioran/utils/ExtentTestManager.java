package com.erdioran.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtentTestManager {

    private static final Logger LOGGER = LogManager.getLogger(ExtentTestManager.class);
    private static final InheritableThreadLocal<ExtentTest> extentTestMap = new InheritableThreadLocal<>();
    private static final InheritableThreadLocal<ExtentTest> extentNode = new InheritableThreadLocal<>();
    private static final ExtentReports extent = ExtentManager.getExtentReports();

    public static synchronized ExtentTest getTest() {
        return extentTestMap.get();
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.createTest(testName, desc);
        extentTestMap.set(test);
        return extentTestMap.get();
    }

    public static synchronized ExtentTest getNode() {
        return extentNode.get();
    }

    public static synchronized void setNode(ExtentTest extentTestNode) {
        extentNode.set(extentTestNode);
    }

    public static synchronized void info(String message) {
        LOGGER.info(message);
        extentNode.get().info(message);
    }

    public static synchronized void pass(String message) {
        LOGGER.info(message);
        getNode().info(message);
    }

    public static synchronized void fail(String message) {
        LOGGER.info(message);
        extentNode.get().info(message);
    }

    public static synchronized void note(String message) {
        LOGGER.info(message);
        extentNode.get().info(message);
    }
}
