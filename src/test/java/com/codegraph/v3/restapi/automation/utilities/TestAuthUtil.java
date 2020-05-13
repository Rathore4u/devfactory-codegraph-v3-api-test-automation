package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.api.TestAuthApi;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

/**
 * Utility class for api calls in test-auth group
 */

@UtilityClass
public class TestAuthUtil {
    private static TestAuthApi api() {
        return ApiUtils.apiClient().testAuth();
    }


    /**
     * Path   : "/test-auth"
     * Method : get
     * OpId   : getTestAuthUsingGET
     * This method tests authentication
     *
     * @return String
     */
    public static Response getTestAuthUsingGET(Options.OptionsBuilder<TestAuthApi.GetTestAuthUsingGETOper>... options) {
        return Options.execute(api()
                .getTestAuthUsingGET(), options);

    }


}
