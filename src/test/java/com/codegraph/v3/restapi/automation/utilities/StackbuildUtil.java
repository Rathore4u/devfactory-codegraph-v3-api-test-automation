package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.api.StackbuildApi;
import com.aurea.automation.codegraph.oa3.models.StackBuildRequestDto;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

/**
 * Utility class for api calls in stackbuild group
 */

@UtilityClass
public class StackbuildUtil {
    private static StackbuildApi api() {
        return ApiUtils.apiClient().stackbuild();
    }


    /**
     * Path   : "/stackbuilds"
     * Method : post
     * OpId   : createStackBuildRequestUsingPOST
     * This method validates and adds a stack build request
     *
     * @return StackBuildRequest
     */
    public static Response createStackBuildRequestUsingPOST(
            StackBuildRequestDto body,
            Options.OptionsBuilder<StackbuildApi.CreateStackBuildRequestUsingPOSTOper>... options) {
        return Options.execute(api()
                .createStackBuildRequestUsingPOST()
                .body(body), options);

    }


    /**
     * Path   : "/stackbuilds/{id}"
     * Method : get
     * OpId   : getStackBuildStatus
     * This endpoint allows clients to retrieve the data of a stack build request with specified id
     *
     * @return StackBuildRequest
     */
    public static Response getStackBuildStatus(
            String id,
            Options.OptionsBuilder<StackbuildApi.GetStackBuildStatusOper>... options) {
        return Options.execute(api()
                .getStackBuildStatus()
                .idPath(id), options);

    }


    /**
     * Path   : "/stackbuilds/{id}/status"
     * Method : get
     * OpId   : getStackBuildStatus1
     * This endpoint allows clients to retrieve the status of a Stack build request with specified id
     *
     * @return StackBuildStatus
     */
    public static Response getStackBuildStatus1(
            String id,
            Options.OptionsBuilder<StackbuildApi.GetStackBuildStatus1Oper>... options) {
        return Options.execute(api()
                .getStackBuildStatus1()
                .idPath(id), options);

    }


}
