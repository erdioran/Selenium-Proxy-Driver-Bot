package com.erdioran.base;

import com.erdioran.utils.ConfigManager;
import com.erdioran.utils.Helper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;


import java.time.Duration;


public class Common {

    private static final Logger LOGGER = LogManager.getLogger(Common.class);

    public static void pageLoad(String url) {

        DriverManager.getDriver().get(url);

    }

    public static WebElement findObject(By by, int interval, int maxWait) {
        WebElement element = null;
        int counter = 0;
        while (counter <= maxWait) {
            try {
                if (checkIfWebElementExists(by)) {
                    LOGGER.info("Found element [{}] after waiting for [{}] seconds", by.toString(), counter);
                    counter = maxWait + 1;
                    element = DriverManager.getDriver().findElement(by);
                } else {
                    LOGGER.info("Trying to find element [{}] | Attempt [{}]", by.toString(), counter);
                    Helper.sleepInSeconds(interval);
                    counter++;
                }
            } catch (Exception e) {
                LOGGER.warn("Trying to find again... Exception while finding web element : [{}] | {}", by.toString(), e.getMessage());
                LOGGER.debug(ExceptionUtils.getStackTrace(e));
            }
        }
        if (element == null) {
            LOGGER.info("WebElement [{}] not found", by.toString());
        }
        if (element != null) {
            return element;
        } else {
            return DriverManager.getDriver().findElement(by);
        }
    }

    public static WebElement findObject(By by) {
        return findObject(by, 1, ConfigManager.getExplicitWaitTime());
    }

    public static WebElement findObject(WebElement element) {
        Wait<WebDriver> wait = getFluentWait();
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }


    public static void click(By by) {
        waitForIntervalsAndClick(by, 1, ConfigManager.getExplicitWaitTime(), false);
    }

    public static void click(By by, int maxWaitTime) {
        waitForIntervalsAndClick(by, 1, maxWaitTime, false);
    }

    public static void click(WebElement webElement) {
        findObject(webElement).click();
    }

    public static void click(String string) {
        findObject(By.linkText(string)).click();
    }

    public static void waitForIntervalsAndClick(By by, int interval, int maxWait, boolean useJsToClick) {
        boolean elementFound = false;
        int counter = 0;
        while (counter <= maxWait) {
            try {
                WebElement element = DriverManager.getDriver().findElement(by);
                element.click();
                elementFound = true;
                counter = maxWait + 1;
            } catch (Exception e) {
                LOGGER.info("Web element [{}] | Click attempt : [{}]", by.toString(), counter);
                if (ExceptionUtils.getStackTrace(e).contains("ElementClickInterceptedException")) {
                    LOGGER.warn("ElementClickInterceptedException, scrolling element [{}] into view", by.toString());
                    try {
                        scrollIntoView(by);
                        DriverManager.getDriver().findElement(by).click();
                        elementFound = true;
                        counter = maxWait + 1;
                    } catch (Exception ex) {
                        LOGGER.warn("Failed to click even after scrolling. Retrying | [{}]", ex.getMessage());
                        if (useJsToClick) {
                            LOGGER.info("Trying to click [{}] using javascript", by.toString());
                            elementFound = true;
                            counter = maxWait + 1;
                        }
                    }
                } else {
                    Helper.sleepInSeconds(interval);
                    elementFound = false;
                }
            }
            if (counter == maxWait) {
                break;
            }
            counter++;
        }
        if (!elementFound) {
            DriverManager.getDriver().findElement(by).click();
        }
    }

    public static boolean checkIfWebElementExists(By by) {
        try {
            WebElement element = DriverManager.getDriver().findElement(by);
            if (element.isDisplayed() && element.isEnabled()) {
                LOGGER.info("WebElement : [{}] | Exists", by.toString());
                if (StringUtils.isNotBlank(element.getAttribute("id"))) {
                    LOGGER.info("Actual web element id : [{}]", element.getAttribute("id"));
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        LOGGER.info("WebElement : [{}] | does not exist", by.toString());
        return false;
    }

    public static boolean checkIfWebElementIsDisplayed(By by) {
        try {
            WebElement element = DriverManager.getDriver().findElement(by);
            if (element.isDisplayed()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public static Wait<WebDriver> getFluentWait(int intervalInSeconds, int maxWaitTimeInSeconds) {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(intervalInSeconds))
                .pollingEvery(Duration.ofSeconds(maxWaitTimeInSeconds))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ElementNotInteractableException.class)
                .ignoring(ElementNotVisibleException.class);
    }

    public static Wait<WebDriver> getFluentWait() {
        return getFluentWait(1, ConfigManager.getExplicitWaitTime());
    }


    public static void scrollIntoView(By by) {
        scrollIntoView(DriverManager.getDriver().findElement(by));
    }

    public static void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            LOGGER.debug(e);
        }
    }


    public static boolean elementDipslayed(By element) {
        return DriverManager.getDriver().findElement(element).isDisplayed();

    }

    public static String getAttribute(By by,String attribute) {
        WebElement element = findObject(by);
        return element.getAttribute(attribute);
    }


    public static String getText(By by) {
        return DriverManager.getDriver().findElement(by).getText();
    }

}