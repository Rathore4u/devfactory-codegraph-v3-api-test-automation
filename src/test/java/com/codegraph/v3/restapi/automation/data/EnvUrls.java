package com.codegraph.v3.restapi.automation.data;

public final class EnvUrls {

    private EnvUrls() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static final String DEV_ENVIRONMENT = "http://dev-api-codegraph-eng.devfactory.com";
    public static final String QA_ENVIRONMENT = "http://qa-api-codegraph-eng.devfactory.com";
    public static final String STAGING_ENVIRONMENT = "http://staging-api-codegraph-eng.devfactory.com";
    public static final String PROD_ENVIRONMENT = "http://api-codegraph-eng.devfactory.com";

    public static final String ENV_DEV = "dev";
    public static final String ENV_QA = "qa";
    public static final String ENV_STAGING = "staging";
    public static final String ENV_PROD = "production";
}
