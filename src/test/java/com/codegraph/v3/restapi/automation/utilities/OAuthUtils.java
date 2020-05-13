package com.codegraph.v3.restapi.automation.utilities;

import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

@UtilityClass
public class OAuthUtils {

    public static final String X_AUTH_TOKEN = "Authorization";

    public static RequestSpecification getOAuthRequestSpecification(String basic) {
        return given()
                .contentType(JSON).and()
                .header(AUTHORIZATION, basic);
    }
}
