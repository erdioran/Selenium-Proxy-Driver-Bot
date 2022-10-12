package com.erdioran.base;

import com.aventstack.extentreports.ExtentTest;
import com.erdioran.utils.ConfigManager;
import com.erdioran.utils.ExtentTestManager;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;

import static com.erdioran.utils.ProxyManager.getProxyApi1;

public abstract class BaseTest {


    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    public int count = 0;
    public List<String> myList;

    @BeforeMethod(alwaysRun = true)
    public void startBrowserAndLogin(Method method, ITestResult result, ITestContext context) {


        ThreadContext.put("testName", method.getName());
        LOGGER.info("Executing test method : [{}] in class [{}]", result.getMethod().getMethodName(),
                result.getTestClass().getName());
        String nodeName =
                StringUtils.isNotBlank(result.getMethod().getDescription()) ? result.getMethod().getDescription() : method.getName();
        ExtentTest node = ExtentTestManager.getTest().createNode(nodeName);
        ExtentTestManager.setNode(node);
        ExtentTestManager.info("Test Started");
        String status = (String) context.getAttribute("previousTestStatus");
        boolean isNewBrowserPerTest = Boolean.parseBoolean(ConfigManager.getConfigProperty("new.browser.per.test"));
        boolean isCleanUpTest = context.getName().contains("Clean");

     //   List<String> myList = getProxyApi1();

        if (!isNewBrowserPerTest) {
            if (status == null || status.equalsIgnoreCase("failed")) {
                LOGGER.info("Launching fresh browser");
                DriverManager.launchBrowser(ConfigManager.getBrowser(), myList.get(count));
            } else {
                LOGGER.info("Skip log in");
            }
        } else if (isCleanUpTest) {
            LOGGER.info("Clean up test. Skip log in");
        } else {
            DriverManager.launchBrowser(ConfigManager.getBrowser(), myList.get(count));
        }
        System.out.println(count);
        count++;
    }


    @AfterMethod(alwaysRun = true)
    public void CloseBrowser(ITestResult result, ITestContext context) {
        if (!result.isSuccess()) {
            context.setAttribute("previousTestStatus", "failed");
        } else {
            context.setAttribute("previousTestStatus", "passed");
        }
        DriverManager.quitDriver();

    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        DriverManager.quitDriver();
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        myList = getProxyApi1();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        DriverManager.quitDriver();
    }


}
