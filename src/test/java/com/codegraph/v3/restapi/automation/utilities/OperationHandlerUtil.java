package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.api.OperationHandlerApi;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

/**
 * Utility class for api calls in operation-handler group
 */

@UtilityClass
public class OperationHandlerUtil {
    private static OperationHandlerApi api() {
        return ApiUtils.apiClient().operationHandler();
    }


    /**
     * Path   : "/actuator/health"
     * Method : get
     * OpId   : handleUsingGET
     * undefined
     *
     * @return Object
     */
    public static Response handleUsingGET(Options.OptionsBuilder<OperationHandlerApi.HandleUsingGETOper>... options) {
        return Options.execute(api()
                .handleUsingGET(), options);

    }


    /**
     * Path   : "/actuator/info"
     * Method : get
     * OpId   : handleUsingGET1
     * undefined
     *
     * @return Object
     */
    public static Response handleUsingGET1(
            Options.OptionsBuilder<OperationHandlerApi.HandleUsingGET1Oper>... options) {
        return Options.execute(api()
                .handleUsingGET1(), options);

    }


}
