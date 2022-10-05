package com.erdioran.testCases;

import com.aventstack.extentreports.ExtentTest;
import com.erdioran.base.BaseTest;
import com.erdioran.utils.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.erdioran.base.Common.pageLoad;
import static com.erdioran.utils.ProxyManager.*;

public class ProxyCheck extends BaseTest {

    public static final Logger LOGGER = LogManager.getLogger(ProxyCheck.class);


    @BeforeMethod()
    public void beforeTest(ITestContext context) {
        ExtentTest test = ExtentTestManager.getNode();
        test.assignCategory("Case1 Tests");

    }


    @Test(description = "Proxy Check", priority = 1)
    public void proxyCheck() {
        ExtentTest test = ExtentTestManager.getNode();
        test.info("Excel Example");
        ExtentTestManager.info("Excel Example");

        pageLoad("https://github.com/");

        List<String> myList = getProxyApi1();

    }


}
