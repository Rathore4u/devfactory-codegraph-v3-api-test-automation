package com.codegraph.v3.restapi.automation.e2e;

import com.aurea.automation.codegraph.oa3.models.ErrorResponse;
import com.aurea.automation.codegraph.oa3.models.ExtendedAttribute;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionCollectionResponse;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import com.codegraph.v3.restapi.automation.utilities.Options;
import com.codegraph.v3.restapi.automation.utilities.StackUtil;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class StackVersionStepDefs {

    @Then("Verify all stack definition (.*) has correct response")
    public void verifyAllStackVersionResponses(String response) {
        Response stackDef = Responses.getResponse(response);
        LayerDefinitionCollectionResponse resp = stackDef.as(LayerDefinitionCollectionResponse.class);
        stackDef.then().assertThat().statusCode(SC_OK);
        for (int i = 0; i < stackDef.as(LayerDefinitionCollectionResponse.class).getContents().size(); i++) {
            assertFalse(resp.getContents().get(i).getStackId().isEmpty());
            assertEquals(resp.getContents().get(i).getVersion().toString(), Integer.toString(i + 1));
            assertFalse(resp.getContents().get(i).getKind().isEmpty());
            assertFalse(resp.getContents().get(i).getSelfLink().isEmpty());
            assertFalse(resp.getContents().get(i).getId().isEmpty());
            assertFalse(resp.getContents().get(i).getName().isEmpty());
            assertFalse(resp.getContents().get(i).getDescription().isEmpty());
            assertFalse(resp.getContents().get(i).getDefinition().isEmpty());
        }
    }

    @When("Get all stack definition (.*) versions with response (.*)")
    public void getStackDataByVersion(String stackTag, String response) {
        Response stackDef = Responses.getResponse(stackTag);
        StackUtil.getStackVersions(
                stackDef.as(ExtendedAttribute.class).getStackId(),
                Options.storeAs(response));
    }

    @When("Get all stack definition versions with non-existing stackId with response (.*)")
    public void getStackDataByVersion(String response) {
        StackUtil.getStackVersions(
                ConstantsUtils.INVALID_VALUE,
                Options.storeAs(response),
                Options.logRequest());
    }

    @When("Delete a stack definition (.*) by id with response (.*)")
    public void deleteStackDefinition(String stackTag, String response) {
        Response stackDef = Responses.getResponse(stackTag);
        StackUtil.deactivateStackUsingDELETE(stackDef.as(ExtendedAttribute.class).getStackId(),
                Options.storeAs(response));
    }

    @Then("Verify user message for (.*) having message with status code (\\d+) for (.*)")
    public void verifyErrorMessagesContains(String stack, int statusCode, String response) {
        Response stackDef = Responses.getResponse(stack);
        Response responseDef = Responses.getResponse(response);
        assertEquals(responseDef.getStatusCode(), statusCode);
        assertEquals(responseDef.as(ErrorResponse.class).getUserMessage(), "A Stack with the specified id "
                + stackDef.as(ExtendedAttribute.class).getStackId() + " does not exist.");
    }

    @When("Fetch list of stacks (.*) with limit (\\d+) and response (.*)")
    public void fetchAllStacksWithLimit(String stackList, int limitValue, String response) {
        Response stackDef = Responses.getResponse(stackList);
        StackUtil.getStackVersions(
                stackDef.as(ExtendedAttribute.class).getStackId(),
                Options.custom(op -> op.limitQuery(limitValue)),
                Options.storeAs(response));
    }

    @Then("Verify stack definition (.*) list having size (\\d+)")
    public void verifyAllStackVersionResponsesWithCount(String response, int size) {
        Response stackDef = Responses.getResponse(response);
        LayerDefinitionCollectionResponse resp = stackDef.as(LayerDefinitionCollectionResponse.class);
        stackDef.then().assertThat().statusCode(SC_OK);
        for (int i = 0; i < size; i++) {
            assertFalse(resp.getContents().get(i).getStackId().isEmpty());
            assertEquals(resp.getContents().get(i).getVersion().toString(), Integer.toString(i + 1));
            assertFalse(resp.getContents().get(i).getKind().isEmpty());
            assertFalse(resp.getContents().get(i).getSelfLink().isEmpty());
            assertFalse(resp.getContents().get(i).getId().isEmpty());
            assertFalse(resp.getContents().get(i).getName().isEmpty());
            assertFalse(resp.getContents().get(i).getDescription().isEmpty());
            assertFalse(resp.getContents().get(i).getDefinition().isEmpty());
        }
    }

    @When("Get stack definition list (.*) with (.*) value (.*) and response (.*)$")
    public void fetchAllStacksWithParam(String stackList, String param, String value, String response) {
        Response stackDef = Responses.getResponse(stackList);
        StackUtil.getStackVersions(
                stackDef.as(ExtendedAttribute.class).getStackId(),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(param, value))),
                Options.storeAs(response));
    }

    @Then("Verify stack definition (.*) list with offset")
    public void verifyAllStackVersionResponsesWithOffset(String response) {
        Response stackDef = Responses.getResponse(response);
        stackDef.then().assertThat().statusCode(SC_OK);
        assertEquals(ConstantsUtils.FOUR, stackDef.as(LayerDefinitionCollectionResponse.class)
                .getContents().get(0).getVersion());
        assertEquals(ConstantsUtils.FIVE, stackDef.as(LayerDefinitionCollectionResponse.class)
                .getContents().get(1).getVersion());
    }
}
