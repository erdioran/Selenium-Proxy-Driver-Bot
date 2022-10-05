package com.erdioran.listener;

import com.erdioran.utils.ConfigManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);
    private int retryCounter = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess()) {
            if (ExceptionUtils.getStackTrace(result.getThrowable()).contains("AssertionError") && System.getProperty(
                    "retry.on.assertion.fail", "false").equals("true")) {
                LOGGER.info("[{}] failed due to Assertion. Will not retry", result.getMethod().getMethodName());
            } else if (retryCounter <= ConfigManager.getMaxRetryCount()) {
                LOGGER.warn("Test method : [{}] is failed due to : {}", result.getMethod().getMethodName(),
                        ExceptionUtils.getStackTrace(result.getThrowable()));
                LOGGER.warn("Retrying failed test method : [{}]", result.getMethod().getMethodName());
                LOGGER.warn("Retry count : " + retryCounter);
                retryCounter++;
                result.setStatus(ITestResult.FAILURE);
                return true;
            } else {
                result.setStatus(ITestResult.FAILURE);
                LOGGER.info("[{}] didn't pass after [{}] retries", result.getMethod().getMethodName(), retryCounter - 1);
            }
        } else {
            result.setStatus(ITestResult.SUCCESS);
            LOGGER.info("[{}] passed after [{}] retries", result.getMethod().getMethodName(), retryCounter);
        }
        return false;
    }
}

