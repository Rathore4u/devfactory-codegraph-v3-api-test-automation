
package com.codegraph.v3.restapi.automation.e2e;

import com.aurea.automation.codegraph.oa3.models.Attribute;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionResponse;
import com.aurea.automation.codegraph.oa3.models.PatchOperationString;
import com.aurea.automation.codegraph.oa3.models.ErrorResponse;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.utilities.LayerdefinitionUtil;
import com.codegraph.v3.restapi.automation.utilities.Options;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.apache.http.HttpStatus.SC_UNPROCESSABLE_ENTITY;
import static org.junit.Assert.*;

public class LayerDefinitionStepDefsV2 {

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

    private void prepareLayer(int randomInteger, String definition, String layerTag) {
        Attribute layerDefData = new Attribute();
        layerDefData
                .name(randomInteger + "name")
                .definition(definition)
                .description(randomInteger + "description");
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(layerDefData));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag,
                        getLayerDefinitionResponse().as(LayerDefinitionResponse.class));
    }

    @When("^Logged in user requests to create layer definition (.*) with undefined layer-code and defined container$")
    public void createLayerDefinitionWithDefinedContainer(String layerTag) {
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        String definition = "schemaVersion: \"1.0\"\ndaemon: false\nversion: 1"
                + "\nname: " + randomInteger + "name\ninputs: \n parameters: \n - name: commit"
                + "\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms"
                + "\n - name: repoUrl\n - name: codeGraphName \ncontainer: \n image: "
                + "registry2.swarm.devfactory.com/v2/devfactory/mock-layer-definition:latest\n env: \n  - name: "
                + "repoUrl\n    value: test\n  - name: commit\n    value: test\n  - name: NEO4J_BOLT_PORT\n    value: port"
                + "\n  - name: branch\n    value: test\n  - name: NEO4J_BOLT_HOST\n    value: hostname\n  - name: FP"
                + "\n    value: any\n  - name: MINTIME\n    value: 1\n  - name: MAXTIME\n    value: 5";
        prepareLayer(randomInteger, definition, layerTag);
    }

    @When("^Logged in user requests to create layer definition (.*) with undefined layer-code and undefined container$")
    public void createLayerDefinitionWithUnDefinedContainer(String layerTag) {
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        String definition = "schemaVersion: \"1.0\"\nname: " + randomInteger + "name\ndescription: " + randomInteger + "description" +
                "\narguments: \n parameters: \n - name: " + randomInteger + "name\n value: " + randomInteger + "name\n " +
                "datatype: string\n default: 1\n graphs: []";
        prepareLayer(randomInteger, definition, layerTag);
    }

    @When("^Logged in user requests to create layer definition (.*) with defined layer-code from file (.*) and defined container$")
    public void createLayerDefinitionWithDefinedContainerANdDefinedLayerCode(String layerTag, String fileTag) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        Attribute layerDefData = new Attribute();
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        String definition = "schemaVersion: \"1.0\"\ndaemon: false\nversion: 1"
                + "\nname: " + randomInteger + "name\ninputs: \n parameters: \n - name: commit"
                + "\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms"
                + "\n - name: repoUrl\n - name: codeGraphName \ncontainer: \n image: "
                + "registry2.swarm.devfactory.com/v2/devfactory/mock-layer-definition:latest\n env: \n  - name: "
                + "repoUrl\n    value: test\n  - name: commit\n    value: test\n  - name: NEO4J_BOLT_PORT\n    value: port"
                + "\n  - name: branch\n    value: test\n  - name: NEO4J_BOLT_HOST\n    value: hostname\n  - name: FP"
                + "\n    value: any\n  - name: MINTIME\n    value: 1\n  - name: MAXTIME\n    value: 5";
        layerDefData
                .name(randomInteger + "name")
                .definition(definition)
                .description(randomInteger + "description")
                .layerCode(layerCode.replace("\"", "").replace(
                        "[", "").replace("]", ""));
        setLayerDefinitionResponse(LayerdefinitionUtil.createLayerDefinitionUsingPOST(layerDefData));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag,
                        getLayerDefinitionResponse().as(LayerDefinitionResponse.class));
    }

    @Then("^Verify layer definition is missing layerCode or container$")
    public void verifyLayerDefinitionNotCreated() {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
        assertTrue(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage().equals(ConstantsUtils.LAYER_CODE_OR_CONTAINER_REQUIRED_MESSAGE));
    }

    @Then("^Verify Layer code custom container image can't be defined together$")
    public void verifyLayerDefinitionNotCreatedAsBothEntitesPresent() {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
        assertTrue(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage().equals(ConstantsUtils.LAYER_CODE_AND_CONTAINER_CANT_BE_TOGETHER_MESSAGE));
    }

    @Then("^Verify if (.*) can not be patched$")
    public void verifyLayerDefinitionCanBeUpdatedForSpecificFields(String fieldName) {
        getLayerDefinitionResponse().then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
        assertTrue(getLayerDefinitionResponse().as(ErrorResponse.class).getUserMessage().equals("Field " + fieldName + " not found or can't be patched"));
    }

    @When("^Logged in user requests to update layer definition (.*) with layerCode from file (.*)$")
    public void updateLayerDefinitionToReplaceLayerCode(String layerTag, String fileTag) {
        String layerCode = FeatureRegistry.getCurrentFeature().getData(String.class, fileTag);
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("layerCode")
                                        .value(layerCode)
                        )))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag, getLayerDefinitionResponse().as(
                        LayerDefinitionResponse.class));
        FeatureRegistry.getCurrentFeature()
                .setData(Response.class, ConstantsUtils.LAYER_DEFINITION_RESPONSE, getLayerDefinitionResponse());
    }

    @When("^Logged in user requests to update layer definition (.*) with defined container$")
    public void updateLayerDefinitionToAddDefinedContainer(String layerTag) {
        LayerDefinitionResponse layerDefData = FeatureRegistry.getCurrentFeature().getData(
                LayerDefinitionResponse.class, layerTag);
        int randomInteger = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        String definition = "schemaVersion: \"1.0\"\ndaemon: false\nversion: 1"
                + "\nname: " + randomInteger + "name\ninputs: \n parameters: \n - name: commit"
                + "\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms"
                + "\n - name: repoUrl\n - name: codeGraphName \ncontainer: \n image: "
                + "registry2.swarm.devfactory.com/v2/devfactory/mock-layer-definition:latest\n env: \n  - name: "
                + "repoUrl\n    value: test\n  - name: commit\n    value: test\n  - name: NEO4J_BOLT_PORT\n    value: port"
                + "\n  - name: branch\n    value: test\n  - name: NEO4J_BOLT_HOST\n    value: hostname\n  - name: FP"
                + "\n    value: any\n  - name: MINTIME\n    value: 1\n  - name: MAXTIME\n    value: 5";
        setLayerDefinitionResponse(LayerdefinitionUtil.updateLayerDefinitionDetails(layerDefData.getLayerId(),
                Options.custom(op -> op.body(
                        Arrays.asList(
                                new PatchOperationString()
                                        .op("replace")
                                        .path("definition")
                                        .value(definition)
                        )))));
        FeatureRegistry.getCurrentFeature()
                .setData(LayerDefinitionResponse.class, layerTag, getLayerDefinitionResponse().as(
                        LayerDefinitionResponse.class));
        FeatureRegistry.getCurrentFeature()
                .setData(Response.class, ConstantsUtils.LAYER_DEFINITION_RESPONSE, getLayerDefinitionResponse());
    }
}
