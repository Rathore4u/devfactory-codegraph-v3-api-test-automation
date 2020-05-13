package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.api.WebMvcEndpointHandlerMappingApi;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

/**
 * Utility class for api calls in web-mvc-endpoint-handler-mapping group
 */

@UtilityClass
public class WebMvcEndpointHandlerMappingUtil {
    private static WebMvcEndpointHandlerMappingApi api() {
        return ApiUtils.apiClient().webMvcEndpointHandlerMapping();
    }


    /**
     * Path   : "/actuator"
     * Method : get
     * OpId   : linksUsingGET
     * undefined
     *
     * @return Object
     */
    public static Response linksUsingGET(
            Options.OptionsBuilder<WebMvcEndpointHandlerMappingApi.LinksUsingGETOper>... options) {
        return Options.execute(api().linksUsingGET(), options);

    }


}
