package com.erdioran.base;

import com.erdioran.listener.SeleniumListener;
import com.erdioran.utils.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.Assert;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.erdioran.utils.ProxyManager.*;


public class DriverManager {

    private static final Logger LOGGER= LogManager.getLogger(DriverManager.class);

    private static final InheritableThreadLocal<WebDriver> WEB_DRIVER_THREAD_LOCAL = new InheritableThreadLocal<>();


    private DriverManager() {
    }

    public static WebDriver getDriver() {
        return WEB_DRIVER_THREAD_LOCAL.get();
    }

    public static void setDriver(WebDriver driver) {
        WEB_DRIVER_THREAD_LOCAL.set(driver);
    }

    public static void quitDriver() {
        try {
            if (WEB_DRIVER_THREAD_LOCAL.get() != null) {
                WEB_DRIVER_THREAD_LOCAL.get().quit();
            }
        } catch (Exception e) {
            LOGGER.warn(e);
        }
    }

    public static void launchBrowser(String browser) {
        List<String> myList = getProxyApi1();


        WebDriver driver;

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            HashMap<String, Object> chromePrefs = new HashMap<>();
            if (ConfigManager.isHeadless()) {    //headless mode, background tests

                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.setHeadless(true);
                chromeOptions.addArguments("--window-size=1920,1080");
            } else {
                chromeOptions.addArguments("--proxy-server=http://"+ myList.get(2));
                chromeOptions.addArguments("--window-size=1920,1080");
            }
            chromeOptions.addArguments("--disable-gpu");  // for windows headless mode
            chromeOptions.setExperimentalOption("prefs", chromePrefs);
            driver = new ChromeDriver(chromeOptions);
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");  //For after version 47
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            firefoxOptions.setProfile(firefoxProfile);
            if (ConfigManager.isHeadless()) { //headless mode, background tests
                firefoxOptions.setHeadless(true);
                firefoxOptions.addArguments("--window-size=1920,1080");
            } else {
                firefoxOptions.addArguments("--window-size=1920,1080");
            }
            String firefoxBrowserPath;
            if (ConfigManager.getBooleanValue("auto.resolve.firefox.browser.path", true)) {
                firefoxBrowserPath = Paths.get(System.getProperty("user.home"), "AppData", "Local", "Mozilla Firefox", "firefox.exe")
                        .toString();
                firefoxOptions.setBinary(firefoxBrowserPath);
            } else {
                Assert.assertTrue(StringUtils.isNotBlank(ConfigManager.getConfigProperty("firefox.browser.path")),
                        "Please set absolute path to firefox.exe in src/test/resources/app.properties");
                firefoxOptions.setBinary(ConfigManager.getConfigProperty("firefox.browser.path"));
            }
            driver = new FirefoxDriver(firefoxOptions);
        } else if (browser.equalsIgnoreCase("opera")) {
            WebDriverManager.firefoxdriver().setup();
            System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");  //For after version 47
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            firefoxOptions.setProfile(firefoxProfile);
            if (ConfigManager.isHeadless()) { //headless mode, background tests
                firefoxOptions.setHeadless(true);
                firefoxOptions.addArguments("--window-size=1920,1080");
            } else {
                firefoxOptions.addArguments("--window-size=1920,1080");
            }
            String firefoxBrowserPath;
            if (ConfigManager.getBooleanValue("auto.resolve.firefox.browser.path", true)) {
                firefoxBrowserPath = Paths.get(System.getProperty("user.home"), "AppData", "Local", "Mozilla Firefox", "firefox.exe")
                        .toString();
                firefoxOptions.setBinary(firefoxBrowserPath);
            } else {
                Assert.assertTrue(StringUtils.isNotBlank(ConfigManager.getConfigProperty("firefox.browser.path")),
                        "Please set absolute path to firefox.exe in src/test/resources/app.properties");
                firefoxOptions.setBinary(ConfigManager.getConfigProperty("firefox.browser.path"));
            }
            driver = new FirefoxDriver(firefoxOptions);
        }
        else {
            throw new IllegalArgumentException(
                    String.format("%s is invalid value. Enter valid browser value in config.properties", browser));
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigManager.getIntValue("implicit.wait.time", 0)));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigManager.getIntValue("page.load.time.out", 60)));
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(ConfigManager.getIntValue("page.load.time.out", 30)));
        EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
        eventFiringWebDriver.register(new SeleniumListener());
        DriverManager.setDriver(eventFiringWebDriver);
    }

}





