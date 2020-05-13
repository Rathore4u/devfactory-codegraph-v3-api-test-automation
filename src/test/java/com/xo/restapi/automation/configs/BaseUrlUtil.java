package com.xo.restapi.automation.configs;

import io.restassured.RestAssured;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BaseUrlUtil {

    public static String getCurrentEnvironment() {
        return System.getProperty("ENV","default");
    }

    public static String getBaseUrl(String env) {
        String url = ConfigReader.getTestConfig(env).getApi().getBaseUrl();
        if (ConfigReader.getTestConfig(env).getRestAssuredSettings().isDebugMode()) {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        }
        if (url.endsWith("/")) {
            url = url.substring(url.length() - 1);
        }
        return url;
    }

    public static String getBaseUrl() {
        return getBaseUrl(getCurrentEnvironment());
    }
}
