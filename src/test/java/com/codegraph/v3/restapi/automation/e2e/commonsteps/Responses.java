package com.codegraph.v3.restapi.automation.e2e.commonsteps;

import io.restassured.response.Response;

/**
 * Utilities to store and retrieve responses from feature registry
 */
public class Responses {
    /**
     * A simpler way to get a response stored in feature registry.
     * <p>
     * @param name the name used to store the response
     * @return the response
     */
    public static Response getResponse(String name) {
        return FeatureRegistry.getCurrentFeature().getData(Response.class,name);
    }

    /**
     * A simpler way to store a response in the feature registry.
     * <p>
     * @param name the name
     * @param response the response
     * @return the store response
     */
    public static Response setResponse(String name,Response response) {
        FeatureRegistry.getCurrentFeature().setData(Response.class,name,response);
        return response;
    }
}
