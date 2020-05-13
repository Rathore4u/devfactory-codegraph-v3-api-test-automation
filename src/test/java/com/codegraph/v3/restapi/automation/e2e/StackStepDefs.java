package com.codegraph.v3.restapi.automation.e2e;

import com.aurea.automation.codegraph.oa3.models.*;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import com.codegraph.v3.restapi.automation.utilities.Options;
import com.codegraph.v3.restapi.automation.utilities.StackUtil;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class StackStepDefs {

    private Response getStackResponse() {
        return FeatureRegistry.getCurrentFeature().getData(Response.class,
                "stackResponse");
    }

    private void setStackResponse(Response resp) {
        FeatureRegistry.getCurrentFeature().setData(Response.class, "stackResponse", resp);
    }

    @When("^Prepare stack content (.*) with three templates (.*), (.*) and (.*)$")
    public void prepareStackContent(String layerDefTag, String layerTag1, String layerTag2, String layerTag3) {
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        LayerDefinitionResponse layer3Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag3);
        String definition = ConstantsUtils.prepareDefinition(layer1Data, layer2Data, layer3Data);

        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + "name")
                .definition(definition)
                .description(randomInteger + "description");
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @When("^Logged in user requests to create stack (.*) with content (.*)$")
    public void createStack(String stackTag, String layerDefTag) {
        Attribute layerDefData = FeatureRegistry.getCurrentFeature().getData(Attribute.class, layerDefTag);
        setStackResponse(StackUtil.createStackUsingPOST(layerDefData, Options.storeAs(stackTag)));
        FeatureRegistry.getCurrentFeature()
                .setData(ExtendedAttribute.class, stackTag,
                        getStackResponse().as(ExtendedAttribute.class));
    }

    @Then("^Verify if stack has correct response$")
    public void verifyStackResponse() {
        ExtendedAttribute stackData = getStackResponse().as(
                ExtendedAttribute.class);
        assertTrue(!stackData.getName().isEmpty());
        assertTrue(!stackData.getDescription().isEmpty());
        assertTrue(!stackData.getDefinition().isEmpty());
    }

    @When("^Fetch stack (.*) by id$")
    public void fetchStackById(String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        setStackResponse(StackUtil.getStackDetailsUsingGET(stackData.getStackId(), Options.storeAs(stackTag)));
    }

    @Then("^Verify if fetched stack has correct response code$")
    public void verifyFetchedStackResponse() {
        getStackResponse().then().assertThat().statusCode(SC_OK);
    }

    @Then("^Verify if created stack has correct response code$")
    public void verifyCreatedStackResponse() {
        getStackResponse().then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Prepare stack content (.*) without definition$")
    public void prepareStackContentWithoutDefinition(String layerDefTag) {
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + "name")
                .description(randomInteger + "description");
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @When("^Prepare content \"(.*)\" without description field and with three templates (.*), (.*) and (.*)$")
    public void prepareStackContentWithoutDescription(String layerDefTag, String layerTag1, String layerTag2, String layerTag3) {
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        LayerDefinitionResponse layer3Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag3);
        String definition = ConstantsUtils.prepareDefinition(layer1Data, layer2Data, layer3Data);

        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + "name")
                .definition(definition);
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @When("^Prepare content \"(.*)\" without name field and with three templates (.*), (.*) and (.*)$")
    public void prepareStackContentWithoutName(String layerDefTag, String layerTag1, String layerTag2, String layerTag3) {
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        LayerDefinitionResponse layer3Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag3);
        String definition = ConstantsUtils.prepareDefinition(layer1Data, layer2Data, layer3Data);

        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .definition(definition)
                .description(randomInteger + "description");
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @Then("^Verify if created stack has field (.*) missing message$")
    public void verifyCreatedStackResponseHasErrorMessage(String field) {
        getStackResponse().then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
        ErrorResponse errorResponse = getStackResponse().as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.REQUIRED_PARAMETERS_MUST));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.FIELD_IS_REQUIRED));
        assertTrue(errorResponse.getValidation().get(0).getField().contains(field));
    }

    @Then("^Verify stack response is unauthorised$")
    public void verifyStackRequestIsUnauthorised() {
        getStackResponse().then().assertThat().statusCode(SC_UNAUTHORIZED);
    }

    @Then("^Verify stack UnAuthorized access message$")
    public void verifyAuthenticationRequiredMessageForStackRequest() {
        assertTrue(getStackResponse().getBody().as(String.class).contains(
                ConstantsUtils.AUTHENTICATION_REQUIRED_MESSAGE));
    }

    @Then("^Verify stack Not a valid token message$")
    public void verifyAuthenticationInvalidTokenMessageForStackRequest() {
        assertTrue(getStackResponse().getBody().as(String.class).contains(
                ConstantsUtils.INVALID_TOKEN_MESSAGE));
    }

    @When("^Logged in user fetch list of stacks (.*)")
    public void fetchAllStacks(String stackList) {
        StackUtil.listAllStacksUsingGET(Options.storeAs(stackList));
    }

    @When("^Logged in user create a stack (.*) with layer definition (.*)$")
    public void createSingleStack(String stack, String layerData) {
        Response layerDef = Responses.getResponse(layerData);
        Attribute body = new Attribute();
        String stackName = "StackName-" + RandomStringUtils.randomAlphanumeric(5);
        body.setName(stackName);
        body.setDescription(ConstantsUtils.DESCRIPTION);
        body.setDefinition("schemaVersion: \"1.0\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:\n      - name: task-1\n" +
                "        version: 1\n        template: " + layerDef.as(LayerDefinitionResponse.class).getName() + "\n        arguments:\n          parameters:\n" +
                "          - name: failure-probability\n            value: \"0\"\n          - name: sleep-for-ms\n" +
                "            value: \"10\"\n          - name: codeGraphName\n            value: \"codegraphName\"\n" +
                "          - name: repoUrl\n            value: \"{{workflow.parameters.repoUrl}}\"\n" +
                "          - name: branch\n            value: \"{{workflow.parameters.branch}}\"\n" +
                "          - name: commit\n            value: \"{{workflow.parameters.commit}}\"");
        StackUtil.createStackUsingPOST(body, Options.storeAs(stack));
    }

    @Then("^Verify if stack (.*) has correct response$")
    public void verifyStackResponses(String response) {
        Response stackDef = Responses.getResponse(response);
        stackDef.then().assertThat().statusCode(SC_OK);
        assertTrue(!stackDef.as(LayerDefinitionCollectionResponse.class).getKind().isEmpty());
        assertTrue(!stackDef.as(LayerDefinitionCollectionResponse.class).getSelfLink().isEmpty());
        for (ExtendedAttribute attribute : stackDef.as(LayerDefinitionCollectionResponse.class).getContents()) {
            assertTrue(!attribute.getStackId().isEmpty());
            assertTrue(attribute.getVersion() > 0);
            assertTrue(!attribute.getKind().isEmpty());
            assertTrue(!attribute.getSelfLink().isEmpty());
            assertTrue(!attribute.getId().isEmpty());
            assertTrue(!attribute.getName().isEmpty());
            assertTrue(!attribute.getDescription().isEmpty());
            assertTrue(!attribute.getDefinition().isEmpty());
        }
    }

    @When("^Logged in user get list of stacks (.*) with (.*) value (.*)$")
    public void fetchAllStacksWithParam(String stackList, String param, String value) {
        StackUtil.listAllStacksUsingGET(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(param, value))),
                Options.storeAs(stackList));
    }

    @Then("^Verify if all stack (.*) have active (.*) list in response")
    public void verifyStackResponsesActiveFieldValue(String response, String value) {
        Response stackDef = Responses.getResponse(response);
        stackDef.then().assertThat().statusCode(SC_OK);
        for (ExtendedAttribute attribute : stackDef.as(LayerDefinitionCollectionResponse.class).getContents()) {
            assertTrue(attribute.getActive().equals(value));
        }
    }

    @Then("^Verify if stack (.*) list have active fields are either true or false list in response$")
    public void verifyStackResponsesActiveFieldFalse(String response) {
        Response stackDef = Responses.getResponse(response);
        stackDef.then().assertThat().statusCode(SC_OK);
        for (ExtendedAttribute attribute : stackDef.as(LayerDefinitionCollectionResponse.class).getContents()) {
            assertTrue(attribute.getActive().equals(ConstantsUtils.TRUE_TEXT) ||
                    attribute.getActive().equals(ConstantsUtils.FALSE_TEXT));
        }
    }

    @Then("Verify Invalid Boolean Parameter Message for stack (.*)")
    public void verifyInvalidBooleanParameterMessage(String stackList) {
        Response stackDef = Responses.getResponse(stackList);
        stackDef.then().assertThat().statusCode(SC_BAD_REQUEST);
        assertTrue(stackDef.getBody().as(String.class).contains(
                ConstantsUtils.BOOLEAN_CONVERSION_FAILED_MESSAGE));
    }

    @Then("Verify Invalid String Parameter Message for stack (.*)")
    public void verifyInvalidStringParameterMessage(String stackList) {
        Response stackDef = Responses.getResponse(stackList);
        stackDef.then().assertThat().statusCode(SC_BAD_REQUEST);
        assertTrue(stackDef.getBody().as(String.class).contains(
                ConstantsUtils.STRING_CONVERSION_FAILED_MESSAGE));
    }

    @Then("^Verify if count of stack (.*) is (\\d+)")
    public void verifyLayerDefinitionCount(String stackList, Integer count) {
        Response stackDef = Responses.getResponse(stackList);
        assertSame(stackDef.as(LayerDefinitionCollectionResponse.class).getContents().size(), count);
    }

    @Then("^Verify blank stack (.*) response with status code (\\d+)")
    public void verifyBlankStackResponses(String response, int statusCode) {
        Response stackDef = Responses.getResponse(response);
        stackDef.then().assertThat().statusCode(statusCode);
        assertTrue(!stackDef.as(LayerDefinitionCollectionResponse.class).getKind().isEmpty());
        assertTrue(!stackDef.as(LayerDefinitionCollectionResponse.class).getSelfLink().isEmpty());
        assertSame(stackDef.as(LayerDefinitionCollectionResponse.class).getContents().size(), ConstantsUtils.ZERO);
    }

    @When("Logged in user get list of stacks (.*) with limit (\\d+) and offset (\\d+)")
    public void fetchAllStacksWithLimitOffset(String stackList, Integer limitValue, Integer offsetValue) {
        StackUtil.listAllStacksUsingGET(
                Options.custom(op -> op
                        .limitQuery(limitValue)
                        .offsetQuery(offsetValue)),
                Options.storeAs(stackList));
    }

    @Then("^Verify distinct value (\\d+) of stackId in stack list (.*) (.*) (.*)")
    public void verifyLayerDefinitionCountCompare(Integer count, String stackList1, String stackList2, String stackList3) {
        Response stackDef1 = Responses.getResponse(stackList1);
        Response stackDef2 = Responses.getResponse(stackList2);
        Response stackDef3 = Responses.getResponse(stackList3);
        List<String> stackList = new ArrayList<>();
        for (ExtendedAttribute attribute : stackDef1.as(LayerDefinitionCollectionResponse.class).getContents()) {
            stackList.add(attribute.getStackId());
        }
        for (ExtendedAttribute attribute : stackDef2.as(LayerDefinitionCollectionResponse.class).getContents()) {
            stackList.add(attribute.getStackId());
        }
        for (ExtendedAttribute attribute : stackDef3.as(LayerDefinitionCollectionResponse.class).getContents()) {
            stackList.add(attribute.getStackId());
        }
        assertEquals(count.longValue(), stackList.stream().distinct().count());
    }

    @Then("^Verify if count of stack (.*) is same as (.*)")
    public void verifyLayerDefinitionOfTwoStack(String stackList, String stackList1) {
        Response stackDef = Responses.getResponse(stackList);
        Response stackDef1 = Responses.getResponse(stackList1);
        assertSame(stackDef.as(LayerDefinitionCollectionResponse.class).getContents().size(),
                stackDef1.as(LayerDefinitionCollectionResponse.class).getContents().size());
    }

    @When("Fetch list of stacks (.*) with offset value (\\d+) greater then stacks in DB")
    public void fetchAllStacksWithMaxStackCount(String stackList, Integer count) {
        Response allList = StackUtil.listAllStacksUsingGET(
                Options.custom(op -> op
                        .limitQuery(ConstantsUtils.MAX_COUNT)));
        StackUtil.listAllStacksUsingGET(
                Options.custom(op -> op
                        .offsetQuery(count + allList.as(LayerDefinitionCollectionResponse.class).getContents().size())),
                Options.storeAs(stackList));
    }

    @When("Logged in user get list of stacks (.*) with limit (\\d+), offset (\\d+) and active (.*)")
    public void fetchAllStacksWithLimitOffsetActive(String stackList,
                                                    int limitValue,
                                                    int offsetValue,
                                                    String activeValue) {
        StackUtil.listAllStacksUsingGET(
                Options.custom(op -> op
                        .limitQuery(limitValue)
                        .offsetQuery(offsetValue)
                        .activeQuery(activeValue)),
                Options.storeAs(stackList));
    }

    @When("^Delete stack (.*) by id$")
    public void deleteStackById(String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        setStackResponse(StackUtil.deactivateStackUsingDELETE(stackData.getStackId(), Options.storeAs(stackTag), Options.logRequest(), Options.logResponse()));
    }

    @When("^Delete stack (.*) without id$")
    public void deleteStackWithoutId(String stackTag) {
        setStackResponse(StackUtil.deactivateStackUsingDELETE("", Options.storeAs(stackTag), Options.logRequest(), Options.logResponse()));
    }

    @Then("^Verify stack delete response has not allowed status$")
    public void verifyStackDeleteRequestIsUnauthorised() {
        getStackResponse().then().assertThat().statusCode(SC_METHOD_NOT_ALLOWED);
    }

    @Then("^Verify stack delete response has error method not allowed$")
    public void verifyMethodNotAllowedMessageForStackRequest() {
        assertTrue(getStackResponse().getBody().as(String.class).contains(
                ConstantsUtils.METHOD_NOT_ALLOWED));
    }

    @When("^Delete stack (.*) using id instead of stack id$")
    public void deleteStackUsingIdNotStackId(String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        setStackResponse(StackUtil.deactivateStackUsingDELETE(stackData.getId(), Options.storeAs(stackTag), Options.logRequest(), Options.logResponse()));
    }

    @Then("^Verify if specified stack id of stack (.*) does not exists$")
    public void verifyIdDoneNotExists(String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        getStackResponse().then().assertThat().statusCode(SC_NOT_FOUND);
        ErrorResponse errorResponse = getStackResponse().as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals("A Stack with the specified id " + stackData.getId() + " does not exist."));
    }

    @When("^Delete stack (.*) using name instead of stack id$")
    public void deleteStackUsingNameNotStackId(String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        setStackResponse(StackUtil.deactivateStackUsingDELETE(stackData.getName(), Options.storeAs(stackTag), Options.logRequest(), Options.logResponse()));
    }

    @Then("^Verify if specified stack name as id of stack (.*) does not exists$")
    public void verifyStackNameAsIdDoneNotExists(String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        getStackResponse().then().assertThat().statusCode(SC_NOT_FOUND);
        ErrorResponse errorResponse = getStackResponse().as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals("A Stack with the specified id " + stackData.getName() + " does not exist."));
    }

    @Then("^Verify stack is deleted$")
    public void verifyStackDeleted() {
        getStackResponse().then().assertThat().statusCode(SC_NO_CONTENT);
        assertTrue(getStackResponse().getBody().asString().isEmpty());
    }

    @Then("^Verify if specified stack stackid of stack (.*) does not exists$")
    public void verifyStackIdDoneNotExists(String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        getStackResponse().then().assertThat().statusCode(SC_NOT_FOUND);
        ErrorResponse errorResponse = getStackResponse().as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals("A Stack with the specified id " + stackData.getStackId() + " does not exist."));
    }

    @Then("^Verify if stack has version (\\d+)$")
    public void verifyStackVersion(Integer version) {
        ExtendedAttribute stackData = getStackResponse().as(
                ExtendedAttribute.class);
        assertTrue(stackData.getVersion().equals(version));
    }

    @When("^Fetch stack (.*) by version \"(.*)\"$")
    public void fetchStackByVersion(String stackTag, String version) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        setStackResponse(StackUtil.getStackDetailsUsingGET(
                stackData.getStackId(),
                Options.custom(op -> op.versionQuery(version)),
                Options.storeAs(stackTag),
                Options.logRequest(),
                Options.logResponse()));
    }

    @When("^Fetch stack (.*) by non existing id$")
    public void fetchStackByNonExistingId(String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        setStackResponse(StackUtil.getStackDetailsUsingGET(stackData.getId(), Options.storeAs(stackTag), Options.logRequest(), Options.logResponse()));
    }

    @When("^Logged in user requests to update stack (.*) with new definition using layers (.*), (.*) and (.*)$")
    public void updateStack(String stackTag, String layerTag1, String layerTag2, String layerTag3) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        LayerDefinitionResponse layer3Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag3);
        String definition = ConstantsUtils.prepareDefinition(layer1Data, layer2Data, layer3Data);
        StackUtil.updateStackUsingPATCH(stackData.getStackId(),
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

    @Then("^Verify version (.*) of stack (.*) does not exists$")
    public void verifyVersionDoseNotExists(String version, String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        getStackResponse().then().assertThat().statusCode(SC_NOT_FOUND);
        ErrorResponse errorResponse = getStackResponse().as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals("Stack with the specified id does not have the specified versionId . Stack:" + stackData.getStackId() + ", version: " + version));
    }

    @Then("Verify Invalid String to Integer conversion Message")
    public void verifyInvalidStringToIntParameterMessage() {
        getStackResponse().then().assertThat().statusCode(SC_BAD_REQUEST);
        assertTrue(getStackResponse().getBody().as(String.class).contains(
                ConstantsUtils.STRING_CONVERSION_TO_INT_FAILED_MESSAGE));
    }


    @When("Get list of stacks (.*) with (.*) value (.*) and (.*) with (.*) value (.*)")
    public void fetchAllStacksWithTwoParam(String stackList, String param1, String value1, String param2, String value2) {
        StackUtil.listAllStacksUsingGET(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(param1, value1))),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(param2, value2))),
                Options.storeAs(stackList));
    }
  
    @When("^Prepare stack content (.*) with non-existing layer$")
    public void prepareStackContentWithNonExistingLayers(String layerDefTag) {
        String definition = ConstantsUtils.prepareDefinitionWithWrongLayers();
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + "name")
                .definition(definition)
                .description(randomInteger + "description");
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @When("^Prepare stack content (.*) having three templates (.*), (.*) and (.*) with non-existing dependencies$")
    public void prepareStackContentWithNonExistingDependencies(String layerDefTag, String layerTag1, String layerTag2, String layerTag3) {
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        LayerDefinitionResponse layer3Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag3);
        String definition = ConstantsUtils.prepareDefinitionWithNonExisitngDependencies(layer1Data, layer2Data, layer3Data);
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + "name")
                .definition(definition)
                .description(randomInteger + "description");
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @When("^Prepare stack content (.*) having three templates (.*), (.*) and (.*) with cyclic dependencies$")
    public void prepareStackContentWithCyclicDependencies(String layerDefTag, String layerTag1, String layerTag2, String layerTag3) {
        LayerDefinitionResponse layer1Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag1);
        LayerDefinitionResponse layer2Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag2);
        LayerDefinitionResponse layer3Data = FeatureRegistry.getCurrentFeature().getData(LayerDefinitionResponse.class, layerTag3);
        String definition = ConstantsUtils.prepareDefinitionWithCyclicDependencies(layer1Data, layer2Data, layer3Data);
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        layerDefData
                .name(randomInteger + "name")
                .definition(definition)
                .description(randomInteger + "description");
        FeatureRegistry.getCurrentFeature().setData(Attribute.class, layerDefTag, layerDefData);
    }

    @Then("^Verify response of non-existing layer$")
    public void verifyNonExistingLayerResponse() {
        ErrorResponse errorResponse = getStackResponse().as(ErrorResponse.class);
        getStackResponse().then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.STACK_TASK_VALIDATION_ERROR));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.STACK_LAYER_DOES_NOT_EXISTS));
        assertTrue(errorResponse.getValidation().get(0).getField().contains("/definition"));
    }

    @Then("^Verify response of non-existing dependencies$")
    public void verifyNonExistingDependenciesResponse() {
        ErrorResponse errorResponse = getStackResponse().as(ErrorResponse.class);
        getStackResponse().then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.STACK_TASK_VALIDATION_ERROR));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.NON_EXISTING_DEPENDENCY_ERROR));
        assertTrue(errorResponse.getValidation().get(0).getField().contains("/definition"));
    }

    @Then("^Verify response of cyclic dependencies$")
    public void verifyCyclicDependenciesResponse() {
        ErrorResponse errorResponse = getStackResponse().as(ErrorResponse.class);
        getStackResponse().then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.STACK_HAS_DEPENDENCY_CYCLE));
    }

    @When("Logged in user can update a Stack definition (.*) and response (.*)")
    public void updateStackDefinition1(String stackDef, String response) {
        Response stackDefData = Responses.getResponse(stackDef);
        String stackId = stackDefData.as(ExtendedAttribute.class).getStackId();
        StackUtil.updateStackUsingPATCH(stackId,
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op(ConstantsUtils.REPLACE_TEXT)
                                        .path(ConstantsUtils.DEFINITION_PATH)
                                        .value(stackDefData.as(ExtendedAttribute.class).getDefinition())
                        ))),
                Options.storeAs(response));
    }

    @Then("Verify stack version (.*) having bad request error")
    public void verifyLayerDefinitionVersionIsBadRequest(String response) {
        Response stackDef = Responses.getResponse(response);
        stackDef.then().assertThat().statusCode(SC_BAD_REQUEST);
        assertTrue(stackDef.getBody().as(String.class).contains(
                ConstantsUtils.BAD_REQUEST));
    }
}
