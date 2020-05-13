package com.codegraph.v3.restapi.automation.e2e;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.apache.http.HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.aurea.automation.codegraph.oa3.models.Attribute;
import com.aurea.automation.codegraph.oa3.models.ExtendedAttribute;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionCollectionResponse;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionResponse;
import com.aurea.automation.codegraph.oa3.models.PatchOperationString;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.data.UrlConstantsUtils;
import com.codegraph.v3.restapi.automation.data.ValidationMessages;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import com.codegraph.v3.restapi.automation.utilities.LayerdefinitionUtil;
import com.codegraph.v3.restapi.automation.utilities.OAuthUtils;
import com.codegraph.v3.restapi.automation.utilities.Options;
import com.xo.restapi.automation.context.UserContext;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.Arrays;
import org.apache.commons.lang.RandomStringUtils;

@SuppressWarnings("PMD.AvoidPrintStackTrace")
public class LayerDefinitionStepDefsNew {

    private Response getLayerDefinitionResponse() {
        return FeatureRegistry.getCurrentFeature().getData(Response.class, ConstantsUtils.LAYER_DEFINITION_RESPONSE);
    }

    private void setLayerDefinitionResponse(Response resp) {
        FeatureRegistry.getCurrentFeature().setData(Response.class, ConstantsUtils.LAYER_DEFINITION_RESPONSE, resp);
    }

    private Response getLayerDefinitionVersionResponse() {
        return FeatureRegistry.getCurrentFeature().getData(Response.class,
                ConstantsUtils.LAYER_DEFINITION_VERSION_RESPONSE);
    }

    private void setLayerDefinitionVersionResponse(Response resp) {
        FeatureRegistry.getCurrentFeature().setData(Response.class,
                ConstantsUtils.LAYER_DEFINITION_VERSION_RESPONSE, resp);
    }

    @When("^Create content \"(.*)\" having layerCode (.*)$")
    public void createLayerDefinition(String layerTag, String fileTag) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setName(layerName);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setDefinition("schemaVersion: '1.0'\ndaemon: false\nversion: 1\nname: " + layerName
                + "\ninputs:\n parameters:\n - name: commit\n - name: failure-probability\n"
                + " - name: branch\n - name: sleep-for-ms\n - name: repoUrl\n - name: codeGraphName");
        body.setLayerCode(layerCode.replace("\"", "")
                .replace("[", "").replace("]", ""));
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(body));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag, getLayerDefinitionResponse().as(
                        LayerDefinitionResponse.class));
    }

    @When("^User requests to update layer definition (.*) with version (\\d+)$")
    public void updateLayerDefinition(String layerTag, Integer version) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionVersionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/definition")
                                        .value("schemaVersion: '1.0'\ndaemon: false\nversion: " + version.toString()
                                                + "\nname: " + layerDefData.getName() + "\ninputs:\n parameters:"
                                                + "\n - name: commit\n - name: failure-probability\n - name: branch"
                                                + "\n - name: sleep-for-ms\n - name: repoUrl\n - name: codeGraphName")
                        )))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag, getLayerDefinitionResponse().as(
                        LayerDefinitionResponse.class));
    }

    @Then("^Layer definition \"(.*)\" is created")
    public void verifyCreatedLayer(String layerTag) {

        LayerDefinitionResponse layerDef = FeatureRegistry.getCurrentFeature()
                .getData(LayerDefinitionResponse.class, layerTag);
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_CREATED);
        assertEquals(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getName(),
                layerDef.getName());
        assertTrue(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getDefinition().length() > 1);
        assertTrue(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getDescription().length() > 1);
        assertTrue(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getLayerCode().length() > 1);
    }

    @When("^Send request to update description of a layer definition \"(.*)\"$")
    public void updateLayerDefinitionDescription(String layerTag) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/description")
                                        .value("Updated desc")
                        )))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag, getLayerDefinitionResponse().as(
                        LayerDefinitionResponse.class));
    }

    @When("^Send request to update name of a layer definition \"(.*)\" with new definition \"(.*)\"$")
    public void updateLayerDefinitionName(String layerTag, String response) {
        String layerName = "Updated layerName-" + RandomStringUtils.randomAlphanumeric(5);
        LayerDefinitionResponse layerDefData = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/name")
                                        .value(layerName)
                        ))), Options.storeAs(response)));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag, getLayerDefinitionResponse().as(
                        LayerDefinitionResponse.class));
    }

    @Then("^Verify update description of a layer definition \"(.*)\" and with version (\\d+)$")
    public void verifyUpdateLayerDefinitionDescription(String layerTag, Integer version) {
        LayerDefinitionResponse updatedLayerDef = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_OK);
        assertEquals(updatedLayerDef.getDescription(), "Updated desc");
        assertEquals(updatedLayerDef.getVersion(), version);
    }

    @Then("Verify layerdefinition \"(.*)\" has updated name of a layer definition \"(.*)\" and with version (\\d+)")
    public void verifyUpdateLayerDefinitionName(String layerTagOld, String layerTagNew, Integer version) {
        LayerDefinitionResponse updatedLayerDef = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_OK);
        assertNotEquals(updatedLayerDef.getName(),
                Responses.getResponse(layerTagOld).as(LayerDefinitionResponse.class).getName());
        assertEquals(updatedLayerDef.getVersion(), version);
    }

    @Then("^Verify on update layer definition versions is increased to (\\d+)$")
    public void verifyLayerDefinitionVersionIncrease(Integer version) {
        LayerDefinitionResponse updatedLayerDef = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_OK);
        assertSame(updatedLayerDef.getVersion(), version);
    }

    @When("^Send request to update layerCode of a layer definition \"(.*)\" with new layerCode \"(.*)\"$")
    public void updateLayerDefinitionLayerCode(String layerTag, String fileTag) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        LayerDefinitionResponse layerDefData = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/layercode")
                                        .value(layerCode.replace("\"", "").replace("[", "").replace("]", ""))
                        ))),
                Options.storeAs(layerTag)));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, fileTag, getLayerDefinitionResponse().as(
                        LayerDefinitionResponse.class));
    }

    @Then("^Verify update layerCode of a layer definition \"(.*)\" with new layerCode \"(.*)\"$")
    public void verifyLayerDefinitionLayerCode(String layerTagOld, String layerTagNew) {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_OK);
        LayerDefinitionResponse layerDefDataOld = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTagOld);
        LayerDefinitionResponse layerDefDataNew = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTagNew);
        assertNotEquals(layerDefDataNew.getLayerCode(), layerDefDataOld.getLayerCode());
    }

    @When("User requests to get layer definition with invalid LayerDefId (.*)")
    public void fetchLayerDefinitionByInvalidId(String invalidDefId) {
        setLayerDefinitionResponse(LayerdefinitionUtil.getLayerDefinitionDetails1(invalidDefId));
    }

    @When("^User update layerCode of a layer definition \"(.*)\" with invalid (?:layerCode|token) (.*)$")
    public void createLayerDefinitionWithInvalidLayerCode(String layerTag, String invalidDefId) {
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(invalidDefId,
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/name")
                                        .value("NewLayer")
                        )))));
    }

    @When("^User update layerCode of a layer definition \"(.*)\" with blank token$")
    public void createLayerDefinitionWithBlankToken(String layerTag) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/name")
                                        .value("NewLayer")
                        )))));
    }

    @When("^User update layerCode of a layer definition \"(.*)\" with wrong path$")
    public void createLayerDefinitionWithWrongPath(String layerTag) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/TEST")
                                        .value("NewLayer")
                        )))));
    }

    @Then("Verify layer definition \"(.*)\" and with version (\\d+)")
    public void verifyLayerDefinitionVersion1(String layerTag, Integer version) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_OK);
        assertEquals(layerDefData.getVersion(), version);
    }

    @When("^User update layerCode of a layer definition \"(.*)\" with empty patchRequests$")
    public void createLayerDefinitionWithEmptyPatchRequest(String layerTag) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(Arrays.asList()))));
    }

    @When("^Update layerCode of a layer definition \"(.*)\" with invalid (?:layerCode|token) (.*)$")
    public void updateLayerDefinitionWithInvalidLayerCode(String layerTag, String invalidDefId) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/layercode")
                                        .value(invalidDefId)
                        )))));
    }

    @When("^Logged in user requests to update layer definition (.*) with invalid definition (.*)$")
    public void updateLayerDefinitionWithInvalidDefinition(String layerTag, String invalidDefinitionValue) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/definition")
                                        .value(invalidDefinitionValue)
                        )))));
    }

    @When("^Logged in user requests to update layer definition (.*) empty field values$")
    public void updateLayerDefinitionWithEmptyValues(String layerTag) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("")
                                        .path("")
                                        .value("")
                        )))));
    }

    @When("^Logged in user requests to update layer definition (.*) with path as ID$")
    public void updateLayerDefinitionWithPathId(String layerTag) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/id")
                                        .value("ABCD0102")
                        )))));
    }

    @When("^Logged in user requests to update layer definition (.*) with path as version (.*)$")
    public void updateLayerDefinitionWithPathVersion(String layerTag, String version) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("/version")
                                        .value(version)
                        )))));
    }

    @Then("^Compare layerIds of received from layer list (.*) and layer list (.*)$")
    public void verifyTwoResponses(String layer1, String layer2) {
        LayerDefinitionCollectionResponse layerDef1 = FeatureRegistry.getCurrentFeature()
                .getData(LayerDefinitionCollectionResponse.class, layer1);
        LayerDefinitionCollectionResponse layerDef2 = FeatureRegistry.getCurrentFeature()
                .getData(LayerDefinitionCollectionResponse.class, layer2);
        for (int i = 0; i < layerDef2.getContents().size(); i++) {
            assertTrue(layerDef1.getContents().get(i + 1).getLayerId()
                    .equals(layerDef2.getContents().get(i).getLayerId()));
        }
    }

    @Then("Verify Invalid String Parameter Message")
    public void verifyInvalidStringParameterMessage() {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_BAD_REQUEST);
        assertTrue(getLayerDefinitionResponse().getBody().as(String.class).contains(
                ConstantsUtils.STRING_CONVERSION_FAILED_MESSAGE));
    }

    @Then("Verify Invalid Boolean Parameter Message")
    public void verifyInvalidBooleanParameterMessage() {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_BAD_REQUEST);
        assertTrue(getLayerDefinitionResponse().getBody().as(String.class).contains(
                ConstantsUtils.BOOLEAN_CONVERSION_FAILED_MESSAGE));
    }

    @Then("^Verify if all active fields are either true or false$")
    public void verifyActiveFieldValuesAreTrueOrFalse() {
        LayerDefinitionCollectionResponse layerDefVersion = getLayerDefinitionResponse().as(
                LayerDefinitionCollectionResponse.class);
        for (ExtendedAttribute attribute : layerDefVersion.getContents()) {
            assertTrue(attribute.getActive().equals(ConstantsUtils.TRUE_TEXT)
                    || attribute.getActive().equals(ConstantsUtils.FALSE_TEXT));
        }
    }

    @When("^Fetch layer definition list (.*) with limit (.*) and offset (.*)$")
    public void getLayerDefinitionListByLimitAndOffset(String listTag, String limit, String offset) {
        setLayerDefinitionResponse(LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_LIMIT, limit))),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_OFFSET, offset)))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionCollectionResponse.class, listTag,
                        getLayerDefinitionResponse().as(LayerDefinitionCollectionResponse.class));
    }

    @When("^Fetch layer definition list (.*) with only limit \"(.*)\"$")
    public void getLayerDefinitionListByLimit(String listTag, String limit) {
        setLayerDefinitionResponse(LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_LIMIT, limit)))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionCollectionResponse.class, listTag,
                        getLayerDefinitionResponse().as(LayerDefinitionCollectionResponse.class));
    }

    @When("^Fetch layer definition list (.*) with only offset \"(.*)\"$")
    public void getLayerDefinitionListByOffset(String listTag, String offset) {
        setLayerDefinitionResponse(LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_OFFSET, offset)))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionCollectionResponse.class, listTag,
                        getLayerDefinitionResponse().as(LayerDefinitionCollectionResponse.class));
    }

    @When("^Fetch layer definition list (.*) with offset (.*) and active (.*)$")
    public void getLayerDefinitionListByOffsetAndActiveStatus(String listTag, String offset, String isEnabled) {
        setLayerDefinitionResponse(LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_OFFSET, offset))),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_ACTIVE, isEnabled)))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionCollectionResponse.class, listTag,
                        getLayerDefinitionResponse().as(LayerDefinitionCollectionResponse.class));
    }

    @When("^Fetch layer definition list (.*) with limit (.*) and active (.*)$")
    public void getLayerDefinitionListByLimitAndActiveStatus(String listTag, String limit, String isEnabled) {
        setLayerDefinitionResponse(LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_LIMIT, limit))),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_ACTIVE, isEnabled)))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionCollectionResponse.class, listTag,
                        getLayerDefinitionResponse().as(LayerDefinitionCollectionResponse.class));
    }

    @Then("^Compare layerIds when offset is (.*) in layer list (.*) and offset it (.*) layer list (.*)$")
    public void verifyOffsetWorksInTwoResponse(String offset1, String layer1, String offset2, String layer2) {
        LayerDefinitionCollectionResponse layerDef1 = FeatureRegistry.getCurrentFeature()
                .getData(LayerDefinitionCollectionResponse.class, layer1);
        LayerDefinitionCollectionResponse layerDef2 = FeatureRegistry.getCurrentFeature()
                .getData(LayerDefinitionCollectionResponse.class, layer2);
        assertTrue(layerDef1.getContents().get(Integer.parseInt(offset2)).getLayerId().equals(
                layerDef2.getContents().get(Integer.parseInt(offset1)).getLayerId()));
    }

    @When("^Fetch layer definition list (.*) with only active \"(.*)\"$")
    public void getLayerDefinitionListByOnlyActiveStatus(String listTag, String isEnabled) {
        setLayerDefinitionResponse(LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_ACTIVE, isEnabled)))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionCollectionResponse.class, listTag,
                        getLayerDefinitionResponse().as(LayerDefinitionCollectionResponse.class));
    }

    @Then("^Verify layer definition response is unauthorised$")
    public void verifyLayerDefinitionIsUnauthorised() {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_UNAUTHORIZED);
    }

    @Then("^Verify layer definition UnAuthorized access message$")
    public void verifyAuthenticationRequiredMessageForLayerDefList() {
        assertTrue(getLayerDefinitionResponse().getBody().as(String.class).contains(
                ConstantsUtils.AUTHENTICATION_REQUIRED_MESSAGE));
    }

    @When("Send request to update name of a layer definition (.*) with existing name (.*)")
    public void updateLayerDefinitionWithSameName(String layerTag, String response) {
        LayerDefinitionResponse layerDefData = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.NAME_PATH)
                                        .value(layerDefData.getName())
                        ))), Options.storeAs(response)));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag, getLayerDefinitionResponse().as(
                        LayerDefinitionResponse.class));
    }

    @When("Send request to update name of a layer definition without content type (.*)")
    public void getLayerDefinitionWithoutHeader(String response) {
        LayerDefinitionResponse layerDefData = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        Response resp = given()
                .body(ConstantsUtils.PATCH_REQUEST)
                .contentType(ConstantsUtils.CONTENT_TYPE_TEXT)
                .header(OAuthUtils.X_AUTH_TOKEN, ConstantsUtils.TOKEN_STRING + UserContext.getToken())
                .patch(UrlConstantsUtils.LAYERSDEFINATION_ENDPOINT + ConstantsUtils.SLASH + layerDefData.getLayerId());
        assertEquals(ValidationMessages.MESSAGE_STATUSCODE + SC_UNSUPPORTED_MEDIA_TYPE,
                SC_UNSUPPORTED_MEDIA_TYPE, resp.statusCode());
        FeatureRegistry.getCurrentFeature()
                .setData(String.class, response, resp.body().asString());
    }

    @When("Verify the error without content type (.*)")
    public void verifyLayerDefinitionWithoutHeader(String response) {
        String resp = FeatureRegistry.getCurrentFeature().getData(String.class, response);
        assertTrue(ValidationMessages.MESSAGE_ERROR + ConstantsUtils.EC_415_ERROR,
                resp.contains(ConstantsUtils.EC_415_ERROR));
        assertTrue(ValidationMessages.MESSAGE_ERROR + ConstantsUtils.EC_415_MESSAGE,
                resp.contains(ConstantsUtils.EC_415_MESSAGE));
    }

    @When("Logged in user requests to update layer definition (.*) with path as empty (.*)")
    public void updateLayerDefinitionWithPathEmpty(String layerTag, String response) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.BLANK)
                                        .value(layerDefData.getName())
                        ))), Options.storeAs(response)));
    }
}
