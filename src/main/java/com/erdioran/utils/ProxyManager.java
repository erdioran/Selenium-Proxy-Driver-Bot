package com.erdioran.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.erdioran.utils.ConfigManager.*;
import static com.erdioran.utils.DataManager.*;
import static com.erdioran.utils.ExcelManager.*;

public class ProxyManager {


    public static final Logger LOGGER = LogManager.getLogger(ProxyManager.class);

    public static List<String> getProxyApi1() {

        int limit = getReadApiLimit();
        List<String> proxyListApi = new ArrayList<String>();

        Response response =
                RestAssured.given().
                        when().
                        get(getData("url.proxyApi1")).
                        then().
                        log().ifValidationFails().
                        assertThat().
                        extract().response();


        if (response.statusCode() != 200 || response.getBody().jsonPath().getList("ip").size() < limit) {

            LOGGER.info("The getProxyApi1 method couldn't return the desired value or the service isn't working correctly. The getProxyApi2 method is executed.");
            proxyListApi = getProxyApi2();
        } else {
            for (int i = 0; i < limit; i++) {
                String proxyResponse = response.getBody().jsonPath().getList("ip").get(i) + ":" + response.getBody().jsonPath().getList("port").get(i);
                setExcel("LoginData",proxyResponse, 1, 2);
                proxyListApi.add(proxyResponse);
            }
            LOGGER.info("responseBody: " + proxyListApi);
        }


        return proxyListApi;
    }

    public static List<String> getProxyApi2() {

        int limit = getReadApiLimit();
        List<String> proxyListApi = new ArrayList<String>();

        Response response =
                RestAssured.given().
                        when().
                        get(getData("url.proxyApi2")).
                        then().
                        log().ifValidationFails().
                        assertThat().
                        statusCode(200).
                        extract().response();


        if (response.statusCode() != 200 || response.getBody().jsonPath().getList("http").size() < limit) {
            LOGGER.info("The getProxyApi1 and getProxyApi2 methods didn't return values up to the requested limit. " + proxyListApi.size() + " values were received.");
        } else {
            for (int i = 0; i < limit; i++) {
                // To get the whole list, the following is used. But this will degrade performance a lot.
                // for(int i = 1; i<response.getBody().jsonPath().getList("http").size();i++)
                String proxyResponse = (String) response.getBody().jsonPath().getList("http").get(i);
                setExcel("LoginData",proxyResponse, 1, 2);
                proxyListApi.add(proxyResponse);
            }
            LOGGER.info("responseBody: " + proxyListApi);
        }

        return proxyListApi;
    }

}
