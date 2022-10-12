package com.erdioran.testCases;

import com.aventstack.extentreports.ExtentTest;
import com.erdioran.base.BaseTest;
import com.erdioran.base.DriverManager;
import com.erdioran.utils.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import static com.erdioran.base.Common.getText;
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
    public void proxyCheck() throws IOException, InterruptedException {
        ExtentTest test = ExtentTestManager.getNode();
        test.info("Excel Example");
        ExtentTestManager.info("Excel Example");

        pageLoad("http://myip.dnsomatic.com/");
        Thread.sleep(25000);
    }

    @Test(description = "Proxy Check2", priority = 2)
    public void proxyCheck2() throws IOException, InterruptedException {
        ExtentTest test = ExtentTestManager.getNode();
        test.info("Proxy Check");
        ExtentTestManager.info("Proxy Check");

        pageLoad("http://myip.dnsomatic.com/");
        Thread.sleep(25000);


    }


}
