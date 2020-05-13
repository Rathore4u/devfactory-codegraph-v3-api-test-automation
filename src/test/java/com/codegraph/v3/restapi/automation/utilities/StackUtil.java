package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.api.StackApi;
import com.aurea.automation.codegraph.oa3.models.Attribute;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

/**
 * Utility class for api calls in stack group
 */

@UtilityClass
public class StackUtil {
    private static StackApi api() {
        return ApiUtils.apiClient().stack();
    }


    /**
     * Path   : "/stacks"
     * Method : get
     * OpId   : listAllStacksUsingGET
     * Retrieves a list of Stacks
     *
     * @return List
     */
    public static Response listAllStacksUsingGET(
            Options.OptionsBuilder<StackApi.ListAllStacksUsingGETOper>... options) {
        return Options.execute(api()
                .listAllStacksUsingGET(), options);

    }


    /**
     * Path   : "/stacks"
     * Method : post
     * OpId   : createStackUsingPOST
     * This method validates and adds a Stack Definition
     *
     * @return StackVersion
     */
    public static Response createStackUsingPOST(Attribute body,
                                                Options.OptionsBuilder<StackApi.CreateStackUsingPOSTOper>... options) {
        return Options.execute(api()
                .createStackUsingPOST()
                .body(body), options);

    }


    /**
     * Path   : "/stacks/{id}"
     * Method : get
     * OpId   : getStackDetailsUsingGET
     * TRetrieve a Stack with the specified id
     *
     * @return StackVersion
     */
    public static Response getStackDetailsUsingGET(
            String id,
            Options.OptionsBuilder<StackApi.GetStackDetailsUsingGETOper>... options) {
        return Options.execute(api()
                .getStackDetailsUsingGET()
                .idPath(id), options);

    }


    /**
     * Path   : "/stacks/{id}"
     * Method : delete
     * OpId   : deactivateStackUsingDELETE
     * Deactivate the Stack with the specified id
     *
     * @return List
     */
    public static Response deactivateStackUsingDELETE(
            String id,
            Options.OptionsBuilder<StackApi.DeactivateStackUsingDELETEOper>... options) {
        return Options.execute(api()
                .deactivateStackUsingDELETE()
                .idPath(id), options);

    }


    /**
     * Path   : "/stacks/{id}"
     * Method : patch
     * OpId   : updateStackUsingPATCH
     * Update the name, description, or definition of a Stack
     *
     * @return StackVersion
     */
    public static Response updateStackUsingPATCH(
            String id,
            Options.OptionsBuilder<StackApi.UpdateStackUsingPATCHOper>... options) {
        return Options.execute(api()
                .updateStackUsingPATCH()
                .idPath(id), options);

    }


    /**
     * Path   : "/stacks/{id}/versions"
     * Method : get
     * OpId   : getStackVersions
     * This endpoint allows clients to retrieve versions for the Stack with specified id
     *
     * @return StackVersion
     */
    public static Response getStackVersions(
            String id,
            Options.OptionsBuilder<StackApi.GetStackVersionsOper>... options) {
        return Options.execute(api()
                .getStackVersions()
                .idPath(id), options);

    }


}
