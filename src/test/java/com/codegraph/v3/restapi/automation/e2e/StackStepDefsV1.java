package com.codegraph.v3.restapi.automation.e2e;

import com.aurea.automation.codegraph.oa3.models.Attribute;
import com.aurea.automation.codegraph.oa3.models.ErrorResponse;
import com.aurea.automation.codegraph.oa3.models.ExtendedAttribute;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionCollectionResponse;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionResponse;
import com.aurea.automation.codegraph.oa3.models.PatchOperationString;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import com.codegraph.v3.restapi.automation.utilities.ApiUtils;
import com.codegraph.v3.restapi.automation.utilities.LayerdefinitionUtil;
import com.codegraph.v3.restapi.automation.utilities.Options;
import com.codegraph.v3.restapi.automation.utilities.StackUtil;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.maven.surefire.shade.common.org.apache.maven.shared.utils.io.IOUtil;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.apache.http.HttpStatus.SC_UNPROCESSABLE_ENTITY;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.junit.Assert.assertTrue;

public class StackStepDefsV1 {

    @When("^User requests to create stack (.*) with content (.*)$")
    public void createStack(String stackTag, String layerDefTag) {
        Attribute layerDefData = FeatureRegistry.getCurrentFeature().getData(Attribute.class, layerDefTag);
        StackUtil.createStackUsingPOST(layerDefData, Options.storeAs(stackTag));
    }

    @When("^Logged in user requests to update stack (.*) with stack (.*) having new name$")
    public void updateStackWithName(String stackTagOrg, String stackTagOrgUpdated) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.NAME_PATH)
                                        .value(stackData.getName() + ConstantsUtils.UPDATED_TEXT)
                        ))),
                Options.logRequest(),
                Options.logResponse());
    }

    @When("^Retrieve stack (.*) by id$")
    public void getStackById(String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        StackUtil.getStackDetailsUsingGET(stackData.getStackId(), Options.storeAs(stackTag));
    }

    @Then("^Verify if received stack (.*) has correct response code$")
    public void verifyFetchedStackResponse(String stackTag) {
        Responses.getResponse(stackTag).then().assertThat().statusCode(SC_OK);
    }

    @Then("^Verify if stack (.*) has updated name from content (.*)$")
    public void verifyNewNameInStackResponse(String stackTag, String layerDefTag) {
        Attribute layerDefData = FeatureRegistry.getCurrentFeature().getData(Attribute.class, layerDefTag);
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        assertTrue(stackData.getName().equals(layerDefData.getName() + ConstantsUtils.UPDATED_TEXT));
    }

    @When("^Logged in user requests to update stack (.*) with stack (.*) having new description$")
    public void updateStackWithDescription(String stackTagOrg, String stackTagOrgUpdated) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.DESCRIPTION_PATH)
                                        .value(stackData.getDescription() + ConstantsUtils.UPDATED_TEXT)
                        ))),
                Options.logRequest(),
                Options.logResponse());
    }

    @Then("^Verify if stack (.*) has updated description from content (.*)$")
    public void verifyNewDescriptionInStackResponse(String stackTag, String layerDefTag) {
        Attribute layerDefData = FeatureRegistry.getCurrentFeature().getData(Attribute.class, layerDefTag);
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        assertTrue(stackData.getDescription().equals(layerDefData.getDescription() + ConstantsUtils.UPDATED_TEXT));
    }

    @When("^Logged in user updates stack (.*) with stack (.*) having new definition using layers (.*), (.*) and (.*)$")
    public void updateStack(String stackTagOrg, String stackTagOrgUpdated, String layerTag1, String layerTag2, String layerTag3) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        LayerDefinitionResponse layer3Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag3);
        String definition = ConstantsUtils.prepareDefinition(layer1Data, layer2Data, layer3Data);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.DEFINITION_PATH)
                                        .value(definition)
                        ))),
                Options.logRequest(),
                Options.logResponse());
    }

    @Then("^Verify if stack (.*) has updated version (\\d+)$")
    public void verifyStackVersion(String stackTag, Integer version) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        assertTrue(stackData.getVersion().equals(version));
    }

    @When("^Logged in user requests to update stack (.*) with stack (.*) having path (.*) having value (.*)$")
    public void updateStackWithKind(String stackTagOrg, String stackTagUpdated, String path, String value) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path("/" + path)
                                        .value(value)
                        ))),
                Options.logRequest(),
                Options.logResponse());
    }

    @Then("^Verify if received stack (.*) has unprocessable response code$")
    public void verifyStackUpdateIsUnProcessable(String stackTag) {
        Responses.getResponse(stackTag).then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
    }

    @Then("^Verify if received stack (.*) has error message for path (.*)$")
    public void verifyErrorMessage(String stackTag, String path) {
        ErrorResponse errorResponse = Responses.getResponse(stackTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals("Field /" + path + " not found or can't be patched"));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.ONLY_NON_EXISTING_CAN_BE_PATCHED));
    }

    @Then("^Verify if stack (.*) does not have updated version (\\d+)$")
    public void verifyStackVersionIsNotUpdated(String stackTag, Integer version) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        assertTrue(!stackData.getVersion().equals(version));
    }

    @When("^Logged in user requests to update non existing id (.*) for stack (.*) with new name$")
    public void updateNonExistingWithName(String id, String stackTagOrgUpdated) {
        StackUtil.updateStackUsingPATCH(id,
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.NAME_PATH)
                                        .value(ConstantsUtils.NON_EXISTING)
                        ))),
                Options.logRequest(),
                Options.logResponse());
    }

    @Then("^Verify response code of stack (.*) for non existing id (.*)$")
    public void verifyStackIdIsNotFound(String stackTag, String id) {
        Responses.getResponse(stackTag).then().assertThat().statusCode(SC_NOT_FOUND);
        ErrorResponse errorResponse = Responses.getResponse(stackTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals("A Stack with the specified id " + id + " does not exist."));
    }

    @Then("^Verify stack update response (.*) is unauthorised$")
    public void verifyStackUpdateRequestIsUnauthorised(String stackTag) {
        Responses.getResponse(stackTag).then().assertThat().statusCode(SC_UNAUTHORIZED);
    }

    @Then("^Verify stack update response (.*) UnAuthorized access message$")
    public void verifyAuthenticationRequiredMessageForStackUpdateRequest(String stackTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.AUTHENTICATION_REQUIRED_MESSAGE));
    }

    @Then("^Verify response of stack (.*) has invalid token message$")
    public void verifyAuthenticationInvalidTokenMessageForStackRequest(String stackTag) {
        assertTrue(Responses.getResponse(stackTag).getBody().as(String.class).contains(
                ConstantsUtils.INVALID_TOKEN_MESSAGE));
    }

    @When("^Logged in user requests to update name and description of stack (.*) with stack (.*)$")
    public void updateStackWithNameAndDescription(String stackTagOrg, String stackTagOrgUpdated) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.NAME_PATH)
                                        .value(stackData.getName() + ConstantsUtils.UPDATED_TEXT),
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.DESCRIPTION_PATH)
                                        .value(stackData.getDescription() + ConstantsUtils.UPDATED_TEXT)
                        ))));
    }

    @When("^Logged in user requests to update stack (.*) with stack (.*) having new name without op field$")
    public void updateStackWithNameWithoutOpField(String stackTagOrg, String stackTagOrgUpdated) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .path(ConstantsUtils.NAME_PATH)
                                        .value(stackData.getName() + ConstantsUtils.UPDATED_TEXT)
                        ))));
    }

    @Then("^Verify if received stack (.*) has unknown operation type$")
    public void verifyUnknownOpErrorMessage(String stackTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.UNKNOWN_OPERATION_TYPE));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.UNKNOWN_OPERATION_TYPE));
    }

    @When("^Logged in user requests to update stack (.*) with stack (.*) having new name without path field$")
    public void updateStackWithNameWithoutPathField(String stackTagOrg, String stackTagOrgUpdated) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .value(stackData.getName() + ConstantsUtils.UPDATED_TEXT)
                        ))));
    }

    @Then("^Verify if received stack (.*) has corrupted path operation for (.*)$")
    public void verifyCorruptedPathOperation(String stackTag, String field) {
        ErrorResponse errorResponse = Responses.getResponse(stackTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.CORRUPTED_PATH_OPERATION));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(field + ConstantsUtils.SHOULD_BE_DEFINED_FOR_OP));
    }

    @When("^Logged in user requests to update stack (.*) with stack (.*) having new name without value field$")
    public void updateStackWithNameWithoutValueField(String stackTagOrg, String stackTagOrgUpdated) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.NAME_PATH)
                        ))));
    }

    @When("^Logged in user requests to update stack (.*) with stack (.*) having non-existing value of op field$")
    public void updateStackWithNonExistingValueOfOp(String stackTagOrg, String stackTagOrgUpdated) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.NON_EXISTING)
                                        .path(ConstantsUtils.NAME_PATH)
                                        .value(stackData.getName() + ConstantsUtils.UPDATED_TEXT)
                        ))));
    }


    @When("^Logged in user requests to update stack (.*) with stack (.*) having non-existing value of path field$")
    public void updateStackWithNonExistingValueOfPath(String stackTagOrg, String stackTagOrgUpdated) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagOrgUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path("/" + ConstantsUtils.NON_EXISTING)
                                        .value(stackData.getName() + ConstantsUtils.UPDATED_TEXT)
                        ))));
    }

    @When("^Logged in user requests to update stack (.*) with stack (.*) to have new definition with wrong schema using layers (.*), (.*) and (.*)$")
    public void updateStackWithWrongSchema(String stackTagOrg, String stackTagUpdated, String layerTag1, String layerTag2, String layerTag3) {
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        LayerDefinitionResponse layer3Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag3);
        String definition = ConstantsUtils.prepareDefinitionWithWrongSchema(layer1Data, layer2Data, layer3Data);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
                Options.storeAs(stackTagUpdated),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.DEFINITION_PATH)
                                        .value(definition)
                        ))));
    }

    @Then("^Verify if received stack (.*) has invalid schema version message$")
    public void verifyInvalidSchemaVersionMessage(String stackTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.PROVIDED_SCHEMA_INVALID));
    }

    @When("^Deactivate stack (.*) by id$")
    public void deleteStackById(String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        StackUtil.deactivateStackUsingDELETE(stackData.getStackId(), Options.logRequest(), Options.logResponse());
    }

    @Then("^Verify response code of stack (.*) for non existing stack (.*)$")
    public void verifyStackIdIsNotExists(String stackTag, String stackTagOrg) {
        Responses.getResponse(stackTag).then().assertThat().statusCode(SC_NOT_FOUND);
        ExtendedAttribute stackData = Responses.getResponse(stackTagOrg).as(ExtendedAttribute.class);
        ErrorResponse errorResponse = Responses.getResponse(stackTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals("A Stack with the specified id " + stackData.getStackId() + " does not exist."));
    }

    @Then("^Verify if stack (.*) has updated name and description from content (.*)$")
    public void verifyNewNameAndDescriptionInStackResponse(String stackTag, String layerDefTag) {
        Attribute layerDefData = FeatureRegistry.getCurrentFeature().getData(Attribute.class, layerDefTag);
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        assertTrue(stackData.getName().equals(layerDefData.getName() + ConstantsUtils.UPDATED_TEXT + ConstantsUtils.UPDATED_TEXT));
        assertTrue(stackData.getDescription().equals(layerDefData.getDescription() + ConstantsUtils.UPDATED_TEXT + ConstantsUtils.UPDATED_TEXT));
    }

    @When("^DB are at least (\\d+) stacks$")
    public void createStackData(Integer count) {
        Response allList = StackUtil.listAllStacksUsingGET(
                Options.custom(op -> op
                        .limitQuery(ConstantsUtils.MAX_COUNT)));
        if (allList.as(LayerDefinitionCollectionResponse.class).getContents().size() <= count) {
            for (int counter = 0; counter < count; counter++) {
                Response uploadResponse = LayerdefinitionUtil.uploadJarToS3UsingPOST(new File(ConstantsUtils.JAR));
                String layerCode = uploadResponse.as(String.class);
                Attribute layerDefData = new Attribute();
                String layerName = "LayerName-" + RandomStringUtils.randomAlphanumeric(5);
                layerDefData
                        .name(layerName)
                        .definition(ConstantsUtils.prepareLayerDefinition(layerName))
                        .description("description")
                        .layerCode(layerCode.replace("\"", "").replace(
                                "[", "").replace("]", ""));
                uploadResponse = LayerdefinitionUtil.createLayerDefinitionUsingPOST(layerDefData, Options.logRequest(),
                        Options.logResponse());
                Attribute body = new Attribute();
                String stackName = "StackName-" + RandomStringUtils.randomAlphanumeric(5);
                body.setName(stackName);
                body.setDescription(ConstantsUtils.DESCRIPTION);
                body.setDefinition(ConstantsUtils.prepareSingleStackDefinition(uploadResponse.as(LayerDefinitionResponse.class).getName()));
                StackUtil.createStackUsingPOST(body, Options.logRequest(),
                        Options.logResponse());
            }
        }
    }

    @When("^Prepare stack content (.*) with two templates (.*) and (.*)$")
    public void prepareStackContent(String layerDefTag, String layerTag1, String layerTag2) {
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        String definition = ConstantsUtils.prepareMultiStackDefinition(layer1Data, layer2Data);
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + "name")
                .definition(definition)
                .description(randomInteger + "description");
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @When("^Prepare stack content (.*) with blank definition$")
    public void prepareStackContentWith(String layerDefTag) {
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + ConstantsUtils.NAME_PATH.replace("/", ""))
                .definition("")
                .description(randomInteger + ConstantsUtils.DESCRIPTION_PATH.replace("/", ""));
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @When("^Prepare content of stack without schema version in definition \"(.*)\" with three templates (.*), (.*) and (.*)")
    public void prepareStackContentWithoutSchemaVersionInDefinition(String layerDefTag, String layerTag1, String layerTag2, String layerTag3) {
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        LayerDefinitionResponse layer3Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag3);
        String definition = ConstantsUtils.prepareDefinitionWithoutSchemaVersion(layer1Data, layer2Data, layer3Data);

        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + ConstantsUtils.NAME_PATH.replace("/", ""))
                .definition(definition)
                .description(randomInteger + ConstantsUtils.DESCRIPTION_PATH.replace("/", ""));
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @Then("^Verify stack build response (.*) has schema version missing message$")
    public void verifyAuthenticationInvalidTokenMessageForStackBuildRequest(String stackBuildTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.SCHEMA_VERSION_MISSING));
    }

    @When("^Prepare content of stack without specs \"(.*)\"")
    public void prepareStackContentWithoutSpecs(String layerDefTag) {
        String definition = ConstantsUtils.prepareDefinitionWithoutSpec();
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + ConstantsUtils.NAME_PATH.replace("/", ""))
                .definition(definition)
                .description(randomInteger + ConstantsUtils.DESCRIPTION_PATH.replace("/", ""));
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @Then("^Verify if build response (.*) has schema invalid definition message$")
    public void verifyInvalidDefinitionMessage(String stackTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.INVALID_STACK_DEFINITION));
    }

    @When("^Prepare content of stack without tasks \"(.*)\"")
    public void prepareStackContentWithoutTasks(String layerDefTag) {
        String definition = ConstantsUtils.prepareDefinitionWithEmptyTask();
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + ConstantsUtils.NAME_PATH.replace("/", ""))
                .definition(definition)
                .description(randomInteger + ConstantsUtils.DESCRIPTION_PATH.replace("/", ""));
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @When("^Prepare content of stack having definition in json format \"(.*)\"")
    public void createWrongRequest(String stackTag) throws Exception {
        String wrongRequest = IOUtil.toString(getClass().getClassLoader().getResourceAsStream("jsonFormatDefinition.txt"));
        Response resp = ApiUtils.apiClient().stack().createStackUsingPOST().reqSpec(spec -> spec.setBody(wrongRequest))
                .execute(Function.identity());
        FeatureRegistry.getCurrentFeature().setData(Response.class, stackTag, resp);
    }

    @Then("^Verify if response (.*) has bad request message$")
    public void verifyBadRequestMessage(String stackTag) {
        Response errorResponse = FeatureRegistry.getCurrentFeature().getData(Response.class, stackTag);
        errorResponse.then().assertThat().statusCode(SC_BAD_REQUEST);
        assertTrue(errorResponse.getBody().as(String.class).contains(ConstantsUtils.BAD_REQUEST));
    }

}
