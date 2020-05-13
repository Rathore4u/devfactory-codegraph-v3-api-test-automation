package com.codegraph.v3.restapi.automation.e2e;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.aurea.automation.codegraph.oa3.models.ErrorResponse;
import com.aurea.automation.codegraph.oa3.models.ExtendedAttribute;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionCollectionResponse;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionResponse;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.data.ValidationMessages;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import com.codegraph.v3.restapi.automation.utilities.LayerdefinitionUtil;
import com.codegraph.v3.restapi.automation.utilities.Options;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;

public class AllLayerDefinitionStepDefs {

    @Then("^Get all layer definitions (.*) details")
    public void getAllLayerDefinitions(String allDef) {
        LayerdefinitionUtil.listAllLayerDefinitions(Options.storeAs(allDef));
    }

    @Then("^Get all layer definitions (.*) details with active (.*)")
    public void getAllLayerDefinitionsWithActive(String allDef, String isEnabled) {
        LayerdefinitionUtil.listAllLayerDefinitions(Options.storeAs(allDef),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(ConstantsUtils.PARAM_ACTIVE, isEnabled))));
    }

    @Then("^Verify all layer definitions (.*) details")
    public void verifyAllLayerDefinitions(String allDef) {
        Response layerDef1 = Responses.getResponse(allDef);
        layerDef1.then().assertThat().statusCode(SC_OK);
        assertEquals(layerDef1.as(LayerDefinitionCollectionResponse.class).getContents().get(0).getVersion().toString(),
                "1");
        assertTrue(!layerDef1.as(LayerDefinitionCollectionResponse.class).getContents().get(0).getId().isEmpty());
        assertTrue(!layerDef1.as(LayerDefinitionCollectionResponse.class).getContents().get(0).getLayerId().isEmpty());
        assertTrue(
                !layerDef1.as(LayerDefinitionCollectionResponse.class).getContents().get(0).getLayerCode().isEmpty());
        assertTrue(
                !layerDef1.as(LayerDefinitionCollectionResponse.class).getContents().get(0).getDefinition().isEmpty());
        assertTrue(
                !layerDef1.as(LayerDefinitionCollectionResponse.class).getContents().get(0).getDescription().isEmpty());
    }

    @Then("Verify message \"(.*)\" having status code (\\d+) for \"(.*)\"")
    public void verifyErrorMessages(String message, int statusCode, String response) {
        Response layerDef1 = Responses.getResponse(response);
        layerDef1.then().assertThat().statusCode(statusCode);
        assertEquals(layerDef1.as(ErrorResponse.class).getUserMessage(),
                message);
    }

    @Then("^Verify if layer definition details (.*) which are Active (.*) are visible$")
    public void verifyActiveFieldValue(String response, String active) {
        Response layerDef1 = Responses.getResponse(response);
        layerDef1.then().assertThat().statusCode(SC_OK);
        for (ExtendedAttribute attribute : layerDef1.as(LayerDefinitionCollectionResponse.class).getContents()) {
            assertTrue(attribute.getActive().equals(ConstantsUtils.TRUE_TEXT) ||
                    attribute.getActive().equals(ConstantsUtils.FALSE_TEXT));
        }
    }

    @When("^Get layer definition list (.*) with limit (\\d+) and offset (\\d+)$")
    public void getLayerDefinitionList(String response, Integer limit, Integer offset) {
        LayerdefinitionUtil.listAllLayerDefinitions(Options.storeAs(response),
                Options.custom(op -> op
                        .limitQuery(limit)
                        .offsetQuery(offset)),
                Options.logRequest());
    }

    @When("Fetch layer definition list count and layer definition (.*) with offset (\\d+) and limit (\\d+)")
    public void getLayerDefinitionListMaxCount(String response, Integer offset, Integer limit) {
        LayerdefinitionUtil.listAllLayerDefinitions(Options.storeAs(response),
                Options.custom(op -> op
                        .limitQuery(limit)
                        .offsetQuery(ConstantsUtils.MAX_COUNT + offset)),
                Options.logRequest());
    }

    @Then("^Verify count of layer definition list (.*) is (\\d+)$")
    public void verifyLayerDefinitionCount(String response, Integer count) {
        Response layerDef1 = Responses.getResponse(response);
        layerDef1.then().assertThat().statusCode(SC_OK);
        assertSame(layerDef1.as(LayerDefinitionCollectionResponse.class).getContents().size(), count);
    }

    @Then("Verify user message contains message \"(.*)\" having status code (\\d+) for \"(.*)\"")
    public void verifyErrorMessagesContains(String message, int statusCode, String response) {
        Response layerDef1 = Responses.getResponse(response);
        layerDef1.then().assertThat().statusCode(statusCode);
        assertTrue(layerDef1.as(ErrorResponse.class).getUserMessage().contains(message));
    }

    @When("^Logged in user get layer definition list (.*) with (.*) value (.*)$")
    public void fetchAllLayerDefinitionWithParam(String layerDef, String param, String value) {
        LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(param, value))),
                Options.storeAs(layerDef));
    }

    @When("Logged in user get layer definition list (.*) with limit (\\d+), offset (\\d+) and active (.*)")
    public void fetchAllLayerDefinitionWithLimitOffsetActive(String layerDef,
            int limitValue,
            int offsetValue,
            String activeValue) {
        LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op
                        .limitQuery(limitValue)
                        .offsetQuery(offsetValue)
                        .activeQuery(activeValue)),
                Options.storeAs(layerDef));
    }

    @When("Get layer definition list (.*) with (.*) value (.*) and (.*) value (.*)")
    public void fetchAllLayerDefinitionWithTwoParam(String stackList, String param1, String value1,
            String param2, String value2) {
        LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(param1, value1))),
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(param2, value2))),
                Options.storeAs(stackList));
    }

    @Then("^Verify content of all layer definitions (.*) details")
    public void verifyContentOfAllLayerDefinitions(String allDef) {
        Response layerDef1 = Responses.getResponse(allDef);
        layerDef1.then().assertThat().statusCode(SC_OK);
        for (ExtendedAttribute attribute : layerDef1.as(LayerDefinitionCollectionResponse.class).getContents()) {
            assertTrue(attribute.getVersion() > ConstantsUtils.ZERO);
            assertFalse(attribute.getId().isEmpty());
            assertFalse(attribute.getLayerId().isEmpty());
            assertFalse(attribute.getName().isEmpty());
            assertFalse(attribute.getDefinition().isEmpty());
            assertFalse(attribute.getDescription().isEmpty());
            assertTrue(attribute.getActive().equals(ConstantsUtils.TRUE_TEXT)
                    || attribute.getActive().equals(ConstantsUtils.FALSE_TEXT));
        }
    }

    @When("^User to get layer definition list with max count (.*) with (.*) value (\\d+)$")
    public void fetchAllLayerDefinitionWithMaxCount(String layerDef, String param, int value) {
        LayerdefinitionUtil.listAllLayerDefinitions(
                Options.custom(op -> op.reqSpec(spec -> spec.addParam(param,
                        ConstantsUtils.MAX_COUNT + value))),
                Options.storeAs(layerDef));
    }

    @Then("^Verify for (.*) unique layerId field is returned in the list")
    public void verifyLayerDefinitionCountCompare(String layerDef) {
        Response layerDef1 = Responses.getResponse(layerDef);
        assertEquals(ValidationMessages.MESSAGE_STATUSCODE + SC_OK, SC_OK, layerDef1.statusCode());
        int totalCountOfList = layerDef1.as(LayerDefinitionCollectionResponse.class).getContents().size();
        List<String> list = new ArrayList<>();
        for (ExtendedAttribute attribute : layerDef1.as(LayerDefinitionCollectionResponse.class).getContents()) {
            list.add(attribute.getLayerId());
        }
        assertEquals(ValidationMessages.MESSAGE_UNIQUE_LAYER_ID, totalCountOfList, list.stream().distinct().count());
    }

    @Then("^Verify no deleted layer (.*) is visible in layer definition list (.*)$")
    public void verifyDeleteLayerDef(String deletedLayer, String layerDefList) {
        Response layerDef = Responses.getResponse(layerDefList);
        Response deletedLayerDef = Responses.getResponse(deletedLayer);
        assertEquals(ValidationMessages.MESSAGE_STATUSCODE + SC_OK, SC_OK, layerDef.statusCode());
        for (ExtendedAttribute attribute : layerDef.as(LayerDefinitionCollectionResponse.class).getContents()) {
            assertNotEquals(ValidationMessages.MESSAGE_DATA
                            + deletedLayerDef.as(LayerDefinitionResponse.class).getLayerId(),
                    deletedLayerDef.as(LayerDefinitionResponse.class).getLayerId(), attribute.getLayerId());
            assertEquals(ValidationMessages.MESSAGE_DATA + ConstantsUtils.TRUE_TEXT,
                    ConstantsUtils.TRUE_TEXT, attribute.getActive());
            assertTrue(ValidationMessages.MESSAGE_DATA_GREATER, attribute.getVersion() > ConstantsUtils.ZERO);
            assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, attribute.getId().isEmpty());
            assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, attribute.getLayerId().isEmpty());
            assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, attribute.getName().isEmpty());
            assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, attribute.getDefinition().isEmpty());
            assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, attribute.getDescription().isEmpty());
        }
    }

    @Then("Verify layer definition details (.*) which have active true are visible")
    public void verifyActiveFieldOnly(String response) {
        Response layerDef1 = Responses.getResponse(response);
        assertEquals(ValidationMessages.MESSAGE_STATUSCODE + SC_OK, SC_OK, layerDef1.statusCode());
        for (ExtendedAttribute attribute : layerDef1.as(LayerDefinitionCollectionResponse.class).getContents()) {
            assertEquals(ValidationMessages.MESSAGE_DATA + ConstantsUtils.TRUE_TEXT,
                    ConstantsUtils.TRUE_TEXT, attribute.getActive());
        }
    }

    @Then("Verify that total number of returned layer definition (.*) entries"
            + " are not greater than LayerNum deleted (.*)")
    public void verifyLayerDefinitionCountdlcl(String layerDef, String layerDefActive) {
        Response layerDef1 = Responses.getResponse(layerDef);
        Response layerDefActive1 = Responses.getResponse(layerDefActive);
        assertEquals(ValidationMessages.MESSAGE_STATUSCODE + SC_OK, SC_OK, layerDef1.statusCode());
        List<String> listInActive = new ArrayList<>();
        for (ExtendedAttribute attribute : layerDef1.as(LayerDefinitionCollectionResponse.class).getContents()) {
            if (attribute.getActive().equals(ConstantsUtils.FALSE_TEXT)) {
                listInActive.add(attribute.getLayerId());
            }
        }
        int count = layerDefActive1.as(LayerDefinitionCollectionResponse.class).getContents().size();
        assertTrue(ValidationMessages.MESSAGE_DATA_GREATER + count, count > listInActive.size());
    }
}
