
package com.codegraph.v3.restapi.automation.e2e;

import com.aurea.automation.codegraph.oa3.models.*;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.data.UrlConstantsUtils;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.utilities.LayerdefinitionUtil;
import com.codegraph.v3.restapi.automation.utilities.OAuthUtils;
import com.codegraph.v3.restapi.automation.utilities.Options;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@SuppressWarnings("PMD.AvoidPrintStackTrace")
public class LayerDefinitionStepDefs {

    private Response getLayerDefinitionResponse() {
        return FeatureRegistry.getCurrentFeature().getData(Response.class,
                ConstantsUtils.LAYER_DEFINITION_RESPONSE);
    }

    private void setLayerDefinitionResponse(Response resp) {
        FeatureRegistry.getCurrentFeature().setData(Response.class,
                ConstantsUtils.LAYER_DEFINITION_RESPONSE, resp);
    }

    private Response getLayerDefinitionVersionResponse() {
        return FeatureRegistry.getCurrentFeature().getData(Response.class,
                ConstantsUtils.LAYER_DEFINITION_VERSION_RESPONSE);
    }

    private void setLayerDefinitionVersionResponse(Response resp) {
        FeatureRegistry.getCurrentFeature().setData(Response.class,
                ConstantsUtils.LAYER_DEFINITION_VERSION_RESPONSE, resp);
    }

    @When("^Create content to request \"(.*)\" having layerCode (.*)")
    public void createLayerDefinition(String layerTag, String fileTag) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setName(layerName);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setDefinition("schemaVersion: '1.0'\ndaemon: false\nversion: 1\nname: " + layerName + "\ninputs:\n parameters:\n - name: commit\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms\n - name: repoUrl\n - name: codeGraphName");
        body.setLayerCode(layerCode.replace("\"", "").replace("[", "").replace("]", ""));
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(body,
                Options.storeAs(layerTag)));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag, getLayerDefinitionResponse().as(LayerDefinitionResponse.class));
    }

    @When("^Create content to request \"(.*)\" with container and layerCode (.*)$")
    public void createLayerDefinitionWithContainer(String layerTag, String fileTag) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setName(layerName);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setLayerCode(layerCode.replace("\"", "").replace("[", "").replace("]", ""));
        body.setDefinition("schemaVersion: \"1.0\"\ndaemon: false\nversion: 1"
                + "\nname: " + layerName + "\ninputs: \n parameters: \n - name: commit"
                + "\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms"
                + "\n - name: repoUrl\n - name: codeGraphName \ncontainer: \n image: "
                + "registry2.swarm.devfactory.com/v2/devfactory/mock-layer-definition:latest\n env: \n  - name: "
                + "repoUrl\n    value: test\n  - name: commit\n    value: test\n  - name: NEO4J_BOLT_PORT\n    value: port"
                + "\n  - name: branch\n    value: test\n  - name: NEO4J_BOLT_HOST\n    value: hostname\n  - name: FP"
                + "\n    value: any\n  - name: MINTIME\n    value: 1\n  - name: MAXTIME\n    value: 5");
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(body, Options.storeAs(layerTag)));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag, getLayerDefinitionResponse().as(LayerDefinitionResponse.class));
    }

    @Then("^Content \"(.*)\" is created")
    public void verifyCreatedLayer(String layerTag) {
        LayerDefinitionResponse layerDef = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag);
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_CREATED);
        assertEquals(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getName(),
                layerDef.getName());
        assertTrue(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getDefinition().length() > 1);
        assertTrue(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getDescription().length() > 1);
        assertTrue(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getLayerCode().length() > 1);
    }

    @When("^Create a Layer Definition \"(.*)\" and do not provide token$")
    public void createLayerDefinitionWithoutToken(String fileName) {
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setName(layerName);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setDefinition("schemaVersion: '1.0'\ndaemon: false\nversion: 1\nname: " + layerName
                + "\ninputs:\n parameters:\n - name: commit\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms\n - name: repoUrl\n - name: codeGraphName");
        setLayerDefinitionResponse(
                given()
                        .header(OAuthUtils.X_AUTH_TOKEN, "Bearer ")
                        .when().body(body).post(UrlConstantsUtils.LAYERSDEFINATION_ENDPOINT));
    }

    @Then("Verify message \"(.*)\" on creating layer definition without layerCode")
    public void verifyCreatedLayerWithoutLayerCode(String message) {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
        assertEquals(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage(),
                message);
    }

    @Then("Verify message \"(.*)\" on (?:creating|getting) layer definition without token")
    public void verifyCreatedLayerWithoutToken(String message) {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_UNAUTHORIZED);
        assertEquals(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage(),
                message);
    }

    @Then("^Verify message \"(.*)\" on creating layer definition")
    public void verifyCreatedLayerWithError(String message) {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_NOT_FOUND);
        assertEquals(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage(),
                message);
    }

    @When("^Create a Layer Definition \"(.*)\" without \"name\" field$")
    public void createLayerDefinitionWithoutName(String fileName) {
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setDefinition("schemaVersion: '1.0'\ndaemon: false\nversion: 1\nname: " + layerName
                + "\ninputs:\n parameters:\n - name: commit\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms\n - name: repoUrl\n - name: codeGraphName");
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(body,
                Options.logRequest(),
                Options.logResponse()));
    }

    @When("^Create a Layer Definition \"(.*)\" without \"definition\" field$")
    public void createLayerDefinitionWithoutDefinition(String fileName) {
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setName(layerName);
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(body,
                Options.logRequest(),
                Options.logResponse()));
    }

    @When("^Create a Layer Definition \"(.*)\" without \"description\" field$")
    public void createLayerDefinitionWithoutDescription(String fileTag) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setName(layerName);
        body.setDefinition("schemaVersion: '1.0'\ndaemon: false\nversion: 1\nname: " + layerName + "\ninputs:\n parameters:\n - name: commit\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms\n - name: repoUrl\n - name: codeGraphName");
        body.setLayerCode(layerCode);
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(body,
                Options.logRequest(),
                Options.logResponse()));
    }

    @When("^Create a Layer Definition \"(.*)\" without \"layerCode\" field$")
    public void createLayerDefinitionWithoutLayerCode(String fileName) {
        Attribute body = new Attribute();
        String layerName = "layerName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setName(layerName);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setDefinition("schemaVersion: '1.0'\ndaemon: false\nversion: 1\nname: " + layerName + "\ninputs:\n parameters:\n - name: commit\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms\n - name: repoUrl\n - name: codeGraphName");
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(body,
                Options.logRequest(),
                Options.logResponse()));
    }

    @Then("^Verify field is required message on creating layer definition without \"(.*)\"")
    public void verifyCreatedLayerWithoutParams(String paramName) {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
        List<ValidationItem> validation = getLayerDefinitionResponse().as(ErrorResponse.class).getValidation();
        assertEquals(validation.get(0).getField(),
                "/" + paramName);
        assertEquals(validation.get(0).getMessage(),
                "field is required");
        assertEquals(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage(),
                "Required parameters must be provided");
    }

    @When("^Create a Layer Definition \"(.*)\" with the same name and different \"layerCode\" (.*)$")
    public void createLayerDefinitionWithSameName(String layerTag, String fileTag) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        LayerDefinitionResponse layerDef = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag);
        Attribute body = new Attribute();
        body.setName(layerDef.getName());
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setDefinition("schemaVersion: '1.0'\ndaemon: false\nversion: 1\nname: " + layerDef.getName()
                + "\ninputs:\n parameters:\n - name: commit\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms\n - name: repoUrl\n - name: codeGraphName");
        body.setLayerCode(layerCode.replace("\"", "").replace("[", "").replace("]", ""));
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(body,
                Options.logRequest(),
                Options.logResponse()));
    }

    @Then("^Verify message on creating layer definition with the same name and different \"layerCode\" (.*)$")
    public void verifyCreatedLayerWithSameName(String fileTag) {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_CONFLICT);
        List<ValidationItem> validation = getLayerDefinitionResponse().as(ErrorResponse.class).getValidation();
        assertEquals(validation.get(0).getField(),
                "/name");
        assertEquals(validation.get(0).getMessage(),
                "field value should unique");
        assertEquals(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage(),
                "Layer definition name should be unique");
    }

    @When("^Logged in user requests to create layer definition (.*) having layerCode (.*) and version (\\d+)$")
    public void createLayerDef(String layerTag, String fileTag, Integer version) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + "name")
                .definition("schemaVersion: '1.0'\ndaemon: false\nversion: " + version.toString()
                        + "\nname: " + randomInteger + "name\ninputs:\n parameters:\n - name: commit"
                        + "\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms"
                        + "\n - name: repoUrl\n - name: codeGraphName")
                .description(randomInteger + "description")
                .layerCode(layerCode.replace("\"", "").replace(
                        "[", "").replace("]", ""));
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(
                layerDefData,
                Options.storeAs(layerTag),
                Options.logRequest()));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag,
                        getLayerDefinitionResponse().as(LayerDefinitionResponse.class));
    }

    @Then("^Layer definition is created successfully$")
    public void verifyLayerDefinitionCreated() {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_CREATED);
    }

    @When("^User requests to fetch layer definition (.*) having version$")
    public void getLayerDefinitionByVersion(String layerTag) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionVersionResponse(LayerdefinitionUtil.getLayerDefinitionVersions(
                layerDefData.getLayerId(),
                Options.logRequest()));
    }

    @Then("^Verify if layer definition version is fetched successfully$")
    public void verifyLayerDefinitionIsFetched() {
        getLayerDefinitionVersionResponse().then().assertThat().statusCode(SC_OK);
    }

    @Then("^Verify if fetched layer definition version has correct response$")
    public void verifyLayerDefinitionResponse() {
        LayerDefinitionCollectionResponse layerDefVersion = getLayerDefinitionVersionResponse().as(
                LayerDefinitionCollectionResponse.class);
        assertTrue(!layerDefVersion.getContents().isEmpty());
        assertTrue(!layerDefVersion.getKind().isEmpty());
        assertTrue(!layerDefVersion.getSelfLink().isEmpty());
    }

    @Then("^Verify if layer definition version is (\\d+)$")
    public void verifyLayerDefinitionVersion(Integer version) {
        LayerDefinitionCollectionResponse layerDefVersion = getLayerDefinitionVersionResponse().as(
                LayerDefinitionCollectionResponse.class);
        assertTrue(layerDefVersion.getContents().get(version - 1).toString().contains(
                "version: " + version.toString()));
    }

    @Then("^Verify layer definition version response is unauthorised$")
    public void verifyLayerDefinitionVersionIsUnauthorised() {
        getLayerDefinitionVersionResponse().then().assertThat().statusCode(SC_UNAUTHORIZED);
    }

    @Then("^Verify UnAuthorized access message$")
    public void verifyAuthenticationRequiredMessage() {
        assertTrue(getLayerDefinitionVersionResponse().getBody().as(String.class).contains(
                "Full authentication is required to access this resource"));
    }

    @Then("^Verify Layer ID of definition (.*) does not exists message$")
    public void verifyIdNotExistsMessage(String layerTag) {
        assertTrue(getLayerDefinitionVersionResponse().getBody().as(String.class)
                .contains("A Layer Definition with the specified id " + layerTag + " does not exist"));
    }

    @Then("^Verify Layer ID of definition (.*) doesn't exists message$")
    public void verifyIdNotExistMessage(String layerTag) {
        assertTrue(getLayerDefinitionResponse().getBody().as(String.class)
                .contains("A Layer Definition with the specified id " + layerTag + " does not exist"));
    }

    @When("User requests to fetch layer definition with invalid LayerDefId (.*)")
    public void getLayerDefinitionByInvalidId(String invalidDefId) {
        setLayerDefinitionVersionResponse(LayerdefinitionUtil.getLayerDefinitionVersions(invalidDefId));
    }

    @Then("^Verify layer definition version is not found$")
    public void verifyLayerDefinitionVersionIsNotFound() {
        getLayerDefinitionVersionResponse().then().assertThat().statusCode(SC_NOT_FOUND);
    }

    @When("^Logged in user requests to update layer definition (.*) with version (\\d+)$")
    public void updateLayerDefinition(String layerTag, Integer version) {
        LayerDefinitionResponse layerDefData = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
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
        FeatureRegistry.getCurrentFeature()
                .setData(Response.class, "layerDefinitionResponse", getLayerDefinitionResponse());
    }

    @When("^User requests to fetch layer definition (.*) having version with limit (.*) and offset (.*)$")
    public void getLayerDefinitionByIdAndLimitAndOffset(String layerTag, String limit, String offset) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionVersionResponse(LayerdefinitionUtil.getLayerDefinitionVersions(
                layerDefData.getLayerId(),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_LIMIT, limit))),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_OFFSET, offset))),
                Options.logRequest(),
                Options.logResponse()));
    }

    @Then("^Verify if count of layer definition versions is (\\d+)$")
    public void verifyLayerDefinitionVersionCount(Integer count) {
        LayerDefinitionCollectionResponse layerDefVersion = getLayerDefinitionVersionResponse().as(
                LayerDefinitionCollectionResponse.class);
        assertSame(layerDefVersion.getContents().size(), count);
    }

    @Then("^Verify if fetched layer definition version has blank content in response$")
    public void verifyLayerDefinitionBlankResponse() {
        LayerDefinitionCollectionResponse layerDefVersion = getLayerDefinitionVersionResponse().as(
                LayerDefinitionCollectionResponse.class);
        assertTrue(layerDefVersion.getContents().isEmpty());
        assertTrue(!layerDefVersion.getKind().isEmpty());
        assertTrue(!layerDefVersion.getSelfLink().isEmpty());
    }

    @When("^User requests to fetch layer definition (.*) having versions with limit (.*)$")
    public void getLayerDefinitionByIdAndLimit(String layerTag, String limit) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionVersionResponse(LayerdefinitionUtil.getLayerDefinitionVersions(
                layerDefData.getLayerId(),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_LIMIT, limit))),
                Options.logRequest(),
                Options.logResponse()));
    }

    @Then("^Verify layer definition version having bad request error$")
    public void verifyLayerDefinitionVersionIsBadRequest() {
        getLayerDefinitionVersionResponse().then().assertThat().statusCode(SC_BAD_REQUEST);
    }


    @When("^Get Layer Definition \"(.*)\" based on Id$")
    public void getLayerDefinitionById(String fileName) {
        LayerDefinitionResponse layerDef = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, fileName);
        setLayerDefinitionResponse(LayerdefinitionUtil.getLayerDefinitionDetails1(layerDef.getLayerId(),
                Options.storeAs(fileName),
                Options.logRequest(),
                Options.logResponse()));
    }

    @When("^Get Layer Definition \"(.*)\" based on version of layerdefinition.$")
    public void getLayerDefinitionByVersion1(String fileName) {
        LayerDefinitionResponse layerDef = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, fileName);
        setLayerDefinitionResponse(LayerdefinitionUtil.getLayerDefinitionDetails1(layerDef.getLayerId(),
                Options.custom(op -> op.versionQuery(layerDef.getVersion())),
                Options.logRequest(),
                Options.logResponse()));
    }

    @When("^Get Layer Definition \"(.*)\" based on non-existing version of layerdefinition.$")
    public void getLayerDefinitionByNonExistingVersion(String fileName) {
        LayerDefinitionResponse layerDef = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, fileName);
        setLayerDefinitionResponse(LayerdefinitionUtil.getLayerDefinitionDetails1(layerDef.getLayerId(),
                Options.custom(op -> op.versionQuery(99999)),
                Options.logRequest(),
                Options.logResponse()));
    }

    @Then("Verify message on getting layer definition \"(.*)\" for non-existing version")
    public void verifyCreatedLayerWithoutVersion(String fileName) {
        LayerDefinitionResponse layerDef = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, fileName);
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_NOT_FOUND);
        assertEquals(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage(),
                "Layer Definition with the specified id " +
                        layerDef.getLayerId() + "does not have the specified versionId 99999");
    }

    @When("^Get Layer Definition \"(.*)\" based on non-existing ID$")
    public void getLayerDefinitionByNonExistingId(String fileName) {
        setLayerDefinitionResponse(LayerdefinitionUtil.getLayerDefinitionDetails1("non-existing",
                Options.logRequest(),
                Options.logResponse()));
    }

    @When("^Get a Layer Definition \"(.*)\" by providing non-existing token.$")
    public void getLayerDefinitionWithInvalidToken(String fileName) {
        LayerDefinitionResponse layerDef = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, fileName);
        setLayerDefinitionVersionResponse(
                given()
                        .header(OAuthUtils.X_AUTH_TOKEN, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlJq")
                        .when().get(UrlConstantsUtils.LAYERSDEFINATION_ENDPOINT + "/" + layerDef.getLayerId()));
    }

    @When("^Get a Layer Definition \"(.*)\" and do not provide token$")
    public void getLayerDefinitionWithoutToken(String fileName) {
        LayerDefinitionResponse layerDef =
                FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, fileName);
        setLayerDefinitionResponse(LayerdefinitionUtil.getLayerDefinitionDetails1(layerDef.getLayerId(),
                Options.logRequest(),
                Options.logResponse()));
    }

    @When("^Verify created Layer Definition \"(.*)\"$")
    public void verifyLayerDefinitionById(String fileName) {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_OK);
        LayerDefinitionResponse layerDef =
                FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, fileName);
        assertEquals(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getDefinition(),
                layerDef.getDefinition());
        assertEquals(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getId(),
                layerDef.getId());
        assertEquals(getLayerDefinitionResponse().as(LayerDefinitionResponse.class).getVersion(),
                layerDef.getVersion());
    }

    @Then("Verify message \"(.*)\" with status code \"(\\d+)\"$")
    public void verifyErrorMessage(String message, int statusCode) {
        getLayerDefinitionResponse().then().assertThat().statusCode(statusCode);
        assertEquals(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage(),
                message);
    }

    @When("^User requests to delete all layer definition of list (.*)$")
    public void deleteLayerDefinitionById(String layerTag) {
        LayerDefinitionCollectionResponse layerDef = FeatureRegistry.getCurrentFeature()
                .getData(LayerDefinitionCollectionResponse.class, layerTag);
        for (int i = 0; i < layerDef.getContents().size(); i++) {
            ExtendedAttribute firstVersion = layerDef.getContents().get(i);
            setLayerDefinitionVersionResponse(LayerdefinitionUtil.deleteLayerDefinitionDetails(
                    firstVersion.getLayerId(),
                    Options.logRequest(),
                    Options.logResponse()));
        }
    }

    @When("^Fetch layer definition list (.*) with active (.*) limit (.*) and offset (.*)$")
    public void getLayerDefinitionList(String listTag, String isEnabled, String limit, String offset) {
        setLayerDefinitionResponse(LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_LIMIT, limit))),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_OFFSET, offset))),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_ACTIVE, isEnabled))),
                Options.logRequest(),
                Options.logResponse()));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionCollectionResponse.class, listTag,
                        getLayerDefinitionResponse().as(LayerDefinitionCollectionResponse.class));
    }

    @Then("^Verify if count of layer definition is (\\d+)$")
    public void verifyLayerDefinitionCount(Integer count) {
        LayerDefinitionCollectionResponse layerDefVersion = getLayerDefinitionResponse().as(
                LayerDefinitionCollectionResponse.class);
        assertSame(layerDefVersion.getContents().size(), count);
    }

    @Then("^Verify if all active fields are \"(.*)\"$")
    public void verifyActiveFieldValue(String active) {
        LayerDefinitionCollectionResponse layerDefVersion = getLayerDefinitionResponse().as(
                LayerDefinitionCollectionResponse.class);
        for (ExtendedAttribute attribute : layerDefVersion.getContents()) {
            assertTrue(attribute.getActive().equals(active));
        }
    }
}
