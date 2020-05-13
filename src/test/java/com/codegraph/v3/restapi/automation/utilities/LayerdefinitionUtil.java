package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.api.LayerdefinitionApi;
import com.aurea.automation.codegraph.oa3.models.Attribute;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;

/**
 * Utility class for api calls in layerdefinition group
 */

@UtilityClass
public class LayerdefinitionUtil {
    private static LayerdefinitionApi api() {
        return ApiUtils.apiClient().layerdefinition();
    }


    /**
     * Path   : "/files"
     * Method : post
     * OpId   : uploadJarToS3UsingPOST
     * This method receives jars and uploads them to s3
     *
     * @return String
     */
    public static Response uploadJarToS3UsingPOST(
            java.io.File files,
            Options.OptionsBuilder<LayerdefinitionApi.UploadJarToS3UsingPOSTOper>... options) {
        return Options.execute(api()
                .uploadJarToS3UsingPOST()
                .filesMultiPart(files), options);

    }


    /**
     * Path   : "/layerdefinitions"
     * Method : get
     * OpId   : listAllLayerDefinitions
     * This endpoint allows clients to retrieve all Layer Definition optionally indicating to get inactive ones
     * and specifying offset and limits
     *
     * @return LayerDefinitionCollectionResponse
     */
    public static Response listAllLayerDefinitions(
            Options.OptionsBuilder<LayerdefinitionApi.ListAllLayerDefinitionsOper>... options) {
        return Options.execute(api()
                .listAllLayerDefinitions(), options);

    }


    /**
     * Path   : "/layerdefinitions"
     * Method : post
     * OpId   : createLayerDefinitionUsingPOST
     * This method validates and adds a Layer Definition
     *
     * @return LayerDefinitionResponse
     */
    public static Response createLayerDefinitionUsingPOST(
            Attribute body,
            Options.OptionsBuilder<LayerdefinitionApi.CreateLayerDefinitionUsingPOSTOper>... options) {
        return Options.execute(api()
                .createLayerDefinitionUsingPOST()
                .body(body), options);

    }


    /**
     * Path   : "/layerdefinitions/{idOrName}"
     * Method : get
     * OpId   : getLayerDefinitionDetails1
     * This endpoint allows clients to retrieve details for the Layer Definition with the specified id
     *
     * @return LayerDefinitionResponse
     */
    public static Response getLayerDefinitionDetails1(
            String idOrName,
            Options.OptionsBuilder<LayerdefinitionApi.GetLayerDefinitionDetails1Oper>... options) {
        return Options.execute(api()
                .getLayerDefinitionDetails1()
                .idOrNamePath(idOrName), options);

    }


    /**
     * Path   : "/layerdefinitions/{idOrName}"
     * Method : delete
     * OpId   : getLayerDefinitionDetails
     * This endpoint allows clients to retrieve details for the Layer Definition with the specified id
     *
     * @return ResponseEntity
     */
    public static Response deleteLayerDefinitionDetails(
            String idOrName,
            Options.OptionsBuilder<LayerdefinitionApi.DeleteLayerDefinitionDetailOper>... options) {
        return Options.execute(api()
                .deleteLayerDefinitionDetail()
                .idOrNamePath(idOrName), options);
    }


    /**
     * Path   : "/layerdefinitions/{idOrName}"
     * Method : patch
     * OpId   : updateLayerDefinitionDetails
     * Update the name, description, or definition of a Layer Definition
     *
     * @return LayerDefinitionResponse
     */
    public static Response updateLayerDefinitionDetails(
            String idOrName,
            Options.OptionsBuilder<LayerdefinitionApi.UpdateLayerDefinitionDetailsOper>... options) {
        return Options.execute(api()
                .updateLayerDefinitionDetails()
                .idOrNamePath(idOrName), options);

    }


    /**
     * Path   : "/layerdefinitions/{idOrName}/versions"
     * Method : get
     * OpId   : getLayerDefinitionVersions
     * This endpoint allows clients to retrieve versions for the Layer Definition with the specified id
     *
     * @return LayerDefinitionCollectionResponse
     */
    public static Response getLayerDefinitionVersions(
            String idOrName,
            Options.OptionsBuilder<LayerdefinitionApi.GetLayerDefinitionVersionsOper>... options) {
        return Options.execute(api()
                .getLayerDefinitionVersions()
                .idOrNamePath(idOrName), options);

    }
}
