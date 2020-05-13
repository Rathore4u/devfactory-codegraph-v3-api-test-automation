package com.codegraph.v3.restapi.automation.e2e;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.aurea.automation.codegraph.oa3.models.Attribute;
import com.aurea.automation.codegraph.oa3.models.ErrorResponse;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionResponse;
import com.aurea.automation.codegraph.oa3.models.PatchOperationString;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import com.codegraph.v3.restapi.automation.utilities.LayerdefinitionUtil;
import com.codegraph.v3.restapi.automation.utilities.Options;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.RandomStringUtils;

public class LayerDefinitionStepsDefsExt {

    private static final Pattern CHANGE_DOUBLE_QUOTE = Pattern.compile("\"", Pattern.LITERAL);
    private static final Pattern CHANGE_OPEN = Pattern.compile("[", Pattern.LITERAL);
    private static final Pattern CHANGE_CLOSE = Pattern.compile("]", Pattern.LITERAL);

    @When("^Send request to update layerCode of a layer definition (.*) with invalid neo4jProcedures (.*)")
    public void updateLayerDefinitionLayerCodeInvalidNeo4J(String layerTag, String neo4jProcedures) {
        Response layerDefData = Responses.getResponse(layerTag);
        String layerId = layerDefData.as(LayerDefinitionResponse.class).getLayerId();
        LayerdefinitionUtil.updateLayerDefinitionDetails(layerId,
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/neo4jProcedures")
                                        .value(ConstantsUtils.INVALID_VALUE)
                        ))),
                Options.storeAs(layerTag));
    }

    @When("^Send request with multiple operations to update layerCode of a layer definition (.*)"
            + " with neo4jProcedures (.*) and response (.*)")
    public void updateLayerDefinitionLayerCodeMultiNeo4J(String layerTag, String neo4jProcedures, String response) {
        String neo4jProcedureJar = FeatureRegistry.getCurrentFeature().getData(String.class, neo4jProcedures);
        Response layerDefData = Responses.getResponse(layerTag);
        String layerId = layerDefData.as(LayerDefinitionResponse.class).getLayerId();
        String layerName = "Updated layerName-" + RandomStringUtils.randomAlphanumeric(5);
        LayerdefinitionUtil.updateLayerDefinitionDetails(layerId,
                Options.custom(op -> op.body(
                        Arrays.asList(new PatchOperationString()
                                        .op("replace")
                                        .path("/description")
                                        .value(ConstantsUtils.UPDATED_DESCRIPTION),
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/name")
                                        .value(layerName),
                                new PatchOperationString()
                                        .op("remove")
                                        .path("/neo4jProcedures")
                                        .value(neo4jProcedureJar.replace("\"", "")
                                                .replace("[", "").replace("]", ""))
                        ))),
                Options.logRequest(),
                Options.logResponse(),
                Options.storeAs(response));
    }

    @Then("^Verify layer definition (.*) with multiple operations updated with response (.*)")
    public void verifyLayerDefinitionVersion1(String layerTag, String response) {
        Response layerDefDataOld = Responses.getResponse(layerTag);
        Response layerDefDataNew = Responses.getResponse(response);
        assertEquals(SC_OK, layerDefDataNew.getStatusCode());
        assertNotEquals(layerDefDataOld.as(LayerDefinitionResponse.class).getName(),
                layerDefDataNew.as(LayerDefinitionResponse.class).getName());
        assertEquals(ConstantsUtils.UPDATED_DESCRIPTION,
                layerDefDataNew.as(LayerDefinitionResponse.class).getDescription());
        assertFalse(layerDefDataNew.getBody().as(String.class).contains(ConstantsUtils.NEO4J));
    }

    @When("^Send request to update layer definition (.*) with missing value parameters (.*) and response (.*)")
    public void updateLayerDefinitionMissingValueParams(String layerTag, String params, String response) {
        Response layerDefData = Responses.getResponse(layerTag);
        String layerId = layerDefData.as(LayerDefinitionResponse.class).getLayerId();
        LayerdefinitionUtil.updateLayerDefinitionDetails(layerId,
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path(params)
                        ))),
                Options.logRequest(),
                Options.logResponse(),
                Options.storeAs(response));
    }

    @When("^Send request to update layer definition (.*) with missing path parameters (.*) and response (.*)")
    public void updateLayerDefinitionMissingPathParams(String layerTag, String params, String response) {
        Response layerDefData = Responses.getResponse(layerTag);
        String layerId = layerDefData.as(LayerDefinitionResponse.class).getLayerId();
        LayerdefinitionUtil.updateLayerDefinitionDetails(layerId,
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .value(params)
                        ))),
                Options.logRequest(),
                Options.logResponse(),
                Options.storeAs(response));
    }

    @When("^Send request to update layer definition (.*) with missing operation type parameters (.*) and response (.*)")
    public void updateLayerDefinitionMissingOpParams(String layerTag, String params, String response) {
        Response layerDefData = Responses.getResponse(layerTag);
        String layerId = layerDefData.as(LayerDefinitionResponse.class).getLayerId();
        LayerdefinitionUtil.updateLayerDefinitionDetails(layerId,
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .path("/name")
                                        .value(params)
                        ))),
                Options.logRequest(),
                Options.logResponse(),
                Options.storeAs(response));
    }

    @When("^Send request to update layer definition (.*) with parameters operation type (.*), path (.*)"
            + ", value (.*) and response (.*)")
    public void updateLayerDefinitionWithParams(String layerTag, String ops, String path, String value,
            String response) {
        Response layerDefData = Responses.getResponse(layerTag);
        String layerId = layerDefData.as(LayerDefinitionResponse.class).getLayerId();
        LayerdefinitionUtil.updateLayerDefinitionDetails(layerId,
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ops)
                                        .path(path)
                                        .value(value)
                        ))),
                Options.logRequest(),
                Options.logResponse(),
                Options.storeAs(response));
    }

    @When("^Logged in user to create content to request (.*) having layerCode (.*) and neo4jProcedures (.*)")
    public void createLayerDefinitionWithNeo4J(String layerTag, String fileTag, String neo4jProcedures) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        String neo4jProcedureJar = FeatureRegistry.getCurrentFeature().getData(String.class, neo4jProcedures);
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setName(layerName);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setDefinition("schemaVersion: '1.0'\ndaemon: false\nversion: 1\nname: " + layerName
                + "\ninputs:\n parameters:\n - name: commit\n - name: failure-probability\n - name: branch\n"
                + " - name: sleep-for-ms\n - name: repoUrl\n - name: codeGraphName");
        body.setLayerCode(layerCode.replace("\"", "")
                .replace("[", "").replace("]", ""));
        body.setNeo4jProcedures(neo4jProcedureJar.replace("\"", "")
                .replace("[", "").replace("]", ""));
        LayerdefinitionUtil.createLayerDefinitionUsingPOST(body, Options.storeAs(layerTag),
                Options.logRequest());
    }

    @When("^Send request to update layerCode of a layer definition (.*) with neo4jProcedures (.*)")
    public void updateLayerDefinitionLayerCodeNeo4J(String layerTag, String neo4jProcedures) {
        String neo4jProcedureJar = FeatureRegistry.getCurrentFeature().getData(String.class, neo4jProcedures);
        Response layerDefData = Responses.getResponse(layerTag);
        String layerId = layerDefData.as(LayerDefinitionResponse.class).getLayerId();
        LayerdefinitionUtil.updateLayerDefinitionDetails(layerId,
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/neo4jProcedures")
                                        .value(neo4jProcedureJar.replace("\"", "")
                                                .replace("[", "").replace("]", ""))
                        ))),
                Options.storeAs(layerTag));
    }

    @Then("^Verify update neo4jProcedures of a layer definition (.*) with new neo4jProcedures (.*)$")
    public void verifyLayerDefinitionNeo4jProcedures(String layeDef, String neo4jProcedures) {
        Response layerDefData = Responses.getResponse(layeDef);
        layerDefData.then().assertThat().statusCode(SC_OK);
        assertEquals(layerDefData.as(LayerDefinitionResponse.class).getVersion(), ConstantsUtils.TWO);
    }

    @When("^Get Layer Definition (.*) by Id")
    public void getLayerDefinitionId(String fileName) {
        Response layerDefData = Responses.getResponse(fileName);
        LayerdefinitionUtil.getLayerDefinitionDetails1(layerDefData.as(LayerDefinitionResponse.class).getLayerId(),
                Options.storeAs(fileName));
    }

    @Then("Verify error message \"(.*)\" having status code (\\d+) for (.*)")
    public void verifyErrorMessages(String message, int statusCode, String response) {
        Response layerDef1 = Responses.getResponse(response);
        assertEquals(layerDef1.getStatusCode(), statusCode);
        assertEquals(layerDef1.as(ErrorResponse.class).getUserMessage(),
                message);
    }

    @Then("Verify user message contains error message \"(.*)\" having status code (\\d+) for (.*)")
    public void verifyErrorMessagesContains(String message, int statusCode, String response) {
        Response layerDef1 = Responses.getResponse(response);
        assertEquals(layerDef1.getStatusCode(), statusCode);
        assertTrue(layerDef1.as(ErrorResponse.class).getUserMessage().contains(message));
    }

    @Then("Verify validation error message \"(.*)\" having status code (\\d+) for (.*)")
    public void verifyValidationErrorMessages(String message, int statusCode, String response) {
        Response layerDef1 = Responses.getResponse(response);
        assertEquals(layerDef1.getStatusCode(), statusCode);
        assertEquals(layerDef1.as(ErrorResponse.class).getValidation().get(0).getMessage(), message);
    }

    @When("Get Layer Definition (.*) by name (.*)")
    public void getLayerDefinitionById(String layerDefination, String response) {
        Response layerDef = Responses.getResponse(layerDefination);
        LayerdefinitionUtil.getLayerDefinitionDetails1(layerDef.as(LayerDefinitionResponse.class).getName(),
                Options.storeAs(response),
                Options.logRequest());
    }

    @When("Get Layer Definition (.*) by using capital letters layer name (.*)")
    public void getLayerDefinitionCapitalLetter(String layerDefination, String response) {
        Response layerDef = Responses.getResponse(layerDefination);
        LayerdefinitionUtil.getLayerDefinitionDetails1(layerDef.as(LayerDefinitionResponse.class)
                        .getName().toUpperCase(),
                Options.storeAs(response),
                Options.logRequest());
    }

    @When("Get Layer Definition by using wrong layer name (.*)")
    public void getLayerDefinitionByWrongName(String response) {
        LayerdefinitionUtil.getLayerDefinitionDetails1(ConstantsUtils.INVALID_VALUE,
                Options.storeAs(response),
                Options.logRequest());
    }

    @Then("Verify Layer definition (.*) is fetched by name (.*) and status code (\\d+)")
    public void verifyDefinitionLayerCreated(String layerName, String layerTag, int statusCode) {
        Response getlayerName = Responses.getResponse(layerName);
        Response layerDef = Responses.getResponse(layerTag);
        layerDef.then().assertThat().statusCode(statusCode);
        assertEquals(layerDef.as(LayerDefinitionResponse.class).getName(),
                getlayerName.as(LayerDefinitionResponse.class).getName());
        assertFalse(layerDef.as(LayerDefinitionResponse.class).getDefinition().isEmpty());
        assertFalse(layerDef.as(LayerDefinitionResponse.class).getDescription().isEmpty());
        assertFalse(layerDef.as(LayerDefinitionResponse.class).getLayerCode().isEmpty());
        assertTrue(layerDef.as(LayerDefinitionResponse.class).getVersion() > ConstantsUtils.ZERO);
        assertFalse(layerDef.as(LayerDefinitionResponse.class).getId().isEmpty());
        assertFalse(layerDef.as(LayerDefinitionResponse.class).getLayerCodeEntryPoint().isEmpty());
    }

    @Then("Verify neo4jProcedures of a layer definition (.*) with neo4jProcedures property")
    public void verifyCreatedLayerDefinitionNeo4jProcedures(String layeDef) {
        Response layerDefData = Responses.getResponse(layeDef);
        layerDefData.then().assertThat().statusCode(SC_CREATED);
        LayerDefinitionResponse layerDetails = layerDefData.as(LayerDefinitionResponse.class);
        assertFalse(layerDetails.getName().isEmpty());
        assertFalse(layerDetails.getDefinition().isEmpty());
        assertFalse(layerDetails.getDescription().isEmpty());
        assertFalse(layerDetails.getLayerCode().isEmpty());
        assertFalse(layerDetails.getNeo4jProcedures().isEmpty());
    }

    @When("Create content to request (.*) having layerCode (.*) and invalid neo4jProcedures (.*)")
    public void createLayerDefinitionWithInvalidNeo4J(String layerTag, String fileTag, String neoFile) {
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setName(layerName);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setDefinition(ConstantsUtils.prepareLayerDefinition(layerName));
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        layerCode = CHANGE_DOUBLE_QUOTE.matcher(layerCode).replaceAll(Matcher.quoteReplacement(ConstantsUtils.BLANK));
        layerCode = CHANGE_OPEN.matcher(layerCode).replaceAll(Matcher.quoteReplacement(ConstantsUtils.BLANK));
        layerCode = CHANGE_CLOSE.matcher(layerCode).replaceAll(Matcher.quoteReplacement(ConstantsUtils.BLANK));
        body.setLayerCode(layerCode);
        body.setNeo4jProcedures(neoFile);
        LayerdefinitionUtil.createLayerDefinitionUsingPOST(body, Options.storeAs(layerTag),
                Options.logRequest());
    }

    @When("User requests to delete a layer definition (.*) with response (.*)")
    public void deleteLayerDefinitionById(String layerTag, String response) {
        Response layerDef = Responses.getResponse(layerTag);
        LayerdefinitionUtil.deleteLayerDefinitionDetails(
                layerDef.as(LayerDefinitionResponse.class).getLayerId(),
                Options.storeAs(response),
                Options.logRequest(),
                Options.logResponse());
    }

    @Then("Verify deleted layer definition (.*) having status code (\\d+) for (.*)")
    public void verifyDeletedLayerDefinition(String message, int statusCode, String response) {
        Response layerDef1 = Responses.getResponse(response);
        assertEquals(layerDef1.getStatusCode(), statusCode);
        assertTrue(layerDef1.getContentType().isEmpty());
    }

    @Then("Verify deleted layer definition (.*) response (.*) having status code (\\d+)")
    public void verifyIdDeleted(String layerTag, String response, int statusCode) {
        verifyDeletedStatus(layerTag, response, statusCode, "A Layer Definition with the specified id ",
                " does not exist");
    }

    @When("Fetch Layer Definition (.*) having response (.*)")
    public void getLayerDefinitionById1(String layerTag, String response) {
        Response layerDef = Responses.getResponse(layerTag);
        LayerdefinitionUtil.getLayerDefinitionDetails1(layerDef.as(LayerDefinitionResponse.class).getLayerId(),
                Options.storeAs(response),
                Options.logRequest(),
                Options.logResponse());
    }

    @Then("Verify inactive layer definition (.*) response (.*) having status code (\\d+)")
    public void verifyIdDeletedAlready(String layerTag, String response, int statusCode) {
        verifyDeletedStatus(layerTag, response, statusCode, "Inactive Layer ",
                " cannot be deleted");
    }

    @When("User delete a layer definition with non-existing layer-id with response (.*)")
    public void deleteLayerDefinitionByInvalidId(String response) {
        LayerdefinitionUtil.deleteLayerDefinitionDetails(
                ConstantsUtils.INVALID_VALUE + RandomStringUtils.randomAlphanumeric(5),
                Options.storeAs(response),
                Options.logRequest(),
                Options.logResponse());
    }

    private void verifyDeletedStatus(String layerTag, String response, int statusCode, String preMsg, String sufMsg) {
        Response layerDef = Responses.getResponse(layerTag);
        Response responseDef = Responses.getResponse(response);
        assertEquals(responseDef.getStatusCode(), statusCode);
        assertTrue(responseDef.getBody().as(String.class)
                .contains(preMsg + layerDef.as(LayerDefinitionResponse.class).getLayerId() + sufMsg));
    }
}
