package com.erdioran.listener;

import com.erdioran.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import java.util.Arrays;

public class SeleniumListener extends AbstractWebDriverEventListener {

    private static final Logger LOGGER = LogManager.getLogger(SeleniumListener.class);

    private static String printFormattedElement(WebElement element) {
        String trim = element.toString().split("->")[1].trim();
        return trim.substring(0, trim.length() - 1);
    }

    @Override
    public void beforeAlertAccept(WebDriver driver) {
        LOGGER.info("Trying to accept an alert : {}", driver.switchTo().alert().getText());
    }

    @Override
    public void afterAlertAccept(WebDriver driver) {
        LOGGER.info("Accepted an alert : {}", driver.switchTo().alert().getText());
    }

    @Override
    public void afterAlertDismiss(WebDriver driver) {
        LOGGER.info("Dismissed an alert");
    }

    @Override
    public void beforeAlertDismiss(WebDriver driver) {
        LOGGER.info("Trying to dismiss an alert");
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        LOGGER.info("Trying to navigate to url : " + url);
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        LOGGER.info("Navigated to : " + url);
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
        LOGGER.info("Trying to navigate back");
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        LOGGER.info("Navigated back");
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
        LOGGER.info(String.format("Trying to navigate forward. Current url : %s", driver.getCurrentUrl()));
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        LOGGER.info(String.format("Navigated forward. Current url : %s", driver.getCurrentUrl()));
    }

    @Override
    public void beforeNavigateRefresh(WebDriver driver) {
        LOGGER.info(String.format("Trying to refresh the current page. Current url : %s", driver.getCurrentUrl()));
    }

    @Override
    public void afterNavigateRefresh(WebDriver driver) {
        LOGGER.info(String.format("Refreshed the current page. Current url : %s", driver.getCurrentUrl()));
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        LOGGER.debug(String.format("Trying to find web element : %s ", by.toString()));
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        LOGGER.debug(String.format("Found web element : %s ", by.toString()));
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        LOGGER.debug(String.format("Trying to click web element : %s ", printFormattedElement(element)));
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        LOGGER.info("Clicked : [{}] ", printFormattedElement(element));
        Helper.sleepMs(700);
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        LOGGER
                .debug("Value before entering text : [{}] | Web element : [{}]", Arrays.toString(keysToSend), printFormattedElement(element));
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
        if (!Arrays.toString(keysToSend).equalsIgnoreCase("null")) {
            LOGGER.info("Text entered : {} | [{}]", Arrays.toString(keysToSend), printFormattedElement(element));
        }
        Helper.sleepMs(700);
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
        LOGGER.debug("Trying to execute script : [{}]", script);
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        LOGGER.debug("Executed script : [{}]", script);
    }

    @Override
    public void afterSwitchToWindow(String windowName, WebDriver driver) {
        LOGGER.info("Switched to window : [{}]", windowName);
        Helper.sleepMs(700);
    }

    @Override
    public void beforeSwitchToWindow(String windowName, WebDriver driver) {
        LOGGER.info("Trying to switch to window : [{}]", windowName);
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        LOGGER.debug("Exception occurred : ", throwable);
        throw new RuntimeException(throwable);
    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> target) {
        LOGGER.debug("Trying to take screenshot");
    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
        LOGGER.debug("Successfully captured screenshot");
    }

    @Override
    public void beforeGetText(WebElement element, WebDriver driver) {
        LOGGER.debug("Trying to get text of web element : [{}]", printFormattedElement(element));
    }

    @Override
    public void afterGetText(WebElement element, WebDriver driver, String text) {
        LOGGER.info("Captured text : [{}] | web element : [{}]", text, printFormattedElement(element));
    }

}

