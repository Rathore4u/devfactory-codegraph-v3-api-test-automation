package com.codegraph.v3.restapi.automation.e2e;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.data.EnvUrls;
import com.codegraph.v3.restapi.automation.data.UrlConstantsUtils;
import com.codegraph.v3.restapi.automation.data.ValidationMessages;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import com.codegraph.v3.restapi.automation.utilities.OperationHandlerUtil;
import com.codegraph.v3.restapi.automation.utilities.Options;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ActuatorStepDefs {

    private String url = null;
    private String actuatorSelfHref = "_links.self.href";
    private String actuatorInfoHref = "_links.info.href";
    private String actuatorHealthHref = "_links.health.href";
    private String actuatorSelfTemplated = "_links.self.templated";
    private String actuatorInfoTemplated = "_links.info.templated";
    private String actuatorHealthTemplated = "_links.health.templated";
    private String status = "status";
    private String statusStateUp = "UP";
    private String name = "name";
    private String nameData = "codegraph-eng";
    private String version = "version";
    private String gitCommitId = "git.commit.id";
    private String gitCommitTime = "git.commit.time";
    private String gitBranch = "git.branch";
    private String self = "self";
    private String health = "health";
    private String info = "info";

    @When("User can collect product related links (.*) from Actuator for"
            + " (staging|dev|qa|production)? environment")
    public void getEnvData(String res, String env) {
        if (env.equals(EnvUrls.ENV_STAGING)) {
            url = EnvUrls.STAGING_ENVIRONMENT + UrlConstantsUtils.ACTUATOR_ENDPOINT;
        } else if (env.equals(EnvUrls.ENV_DEV)) {
            url = EnvUrls.DEV_ENVIRONMENT + UrlConstantsUtils.ACTUATOR_ENDPOINT;
        } else if (env.equals(EnvUrls.ENV_QA)) {
            url = EnvUrls.QA_ENVIRONMENT + UrlConstantsUtils.ACTUATOR_ENDPOINT;
        } else if (env.equals(EnvUrls.ENV_PROD)) {
            url = EnvUrls.PROD_ENVIRONMENT + UrlConstantsUtils.ACTUATOR_ENDPOINT;
        }
        Response response = getData(url);
        FeatureRegistry.getCurrentFeature().setData(String.class, res, response.body().asString());
    }

    @Then("Verify product related links (.*) from Actuator for (staging|dev|qa|production)? environment")
    public void verifyEnvData(String res, String env) {
        String actuatorLink = null;
        String response = FeatureRegistry.getCurrentFeature().getData(String.class, res);
        JsonPath jsonPathEvaluator = JsonPath.from(response);
        if (env.equals(EnvUrls.ENV_STAGING)) {
            actuatorLink = EnvUrls.STAGING_ENVIRONMENT + UrlConstantsUtils.ACTUATOR_ENDPOINT;
        } else if (env.equals(EnvUrls.ENV_DEV)) {
            actuatorLink = EnvUrls.DEV_ENVIRONMENT + UrlConstantsUtils.ACTUATOR_ENDPOINT;
        } else if (env.equals(EnvUrls.ENV_QA)) {
            actuatorLink = EnvUrls.QA_ENVIRONMENT + UrlConstantsUtils.ACTUATOR_ENDPOINT;
        } else if (env.equals(EnvUrls.ENV_PROD)) {
            actuatorLink = EnvUrls.PROD_ENVIRONMENT + UrlConstantsUtils.ACTUATOR_ENDPOINT;
        }
        assertEquals(ValidationMessages.MESSAGE_DATA + actuatorLink,
                actuatorLink, jsonPathEvaluator.get(actuatorSelfHref));
        assertEquals(ValidationMessages.MESSAGE_DATA + actuatorLink + UrlConstantsUtils.HEALTH_ENDPOINT,
                actuatorLink + UrlConstantsUtils.HEALTH_ENDPOINT, jsonPathEvaluator.get(actuatorHealthHref));
        assertEquals(ValidationMessages.MESSAGE_DATA + actuatorLink + UrlConstantsUtils.INFO_ENDPOINT,
                actuatorLink + UrlConstantsUtils.INFO_ENDPOINT, jsonPathEvaluator.get(actuatorInfoHref));
        assertEquals(ValidationMessages.MESSAGE_DATA + ConstantsUtils.FALSE_TEXT, false,
                jsonPathEvaluator.get(actuatorSelfTemplated));
        assertEquals(ValidationMessages.MESSAGE_DATA + ConstantsUtils.FALSE_TEXT, false,
                jsonPathEvaluator.get(actuatorHealthTemplated));
        assertEquals(ValidationMessages.MESSAGE_DATA + ConstantsUtils.FALSE_TEXT, false,
                jsonPathEvaluator.get(actuatorInfoTemplated));
    }

    @When("From product related links collected (.*), user can open (self|health|info)? link"
            + " for (?:staging|dev|qa|production) environment with response (.*)")
    public void getEnvDataLinks(String res, String link, String data) {
        Response responseNew = null;
        JsonPath jsonPathEvaluator = JsonPath.from(FeatureRegistry.getCurrentFeature().getData(String.class, res));
        if (link.equals(self)) {
            responseNew = getData(jsonPathEvaluator.get(actuatorSelfHref));
        } else if (link.equals(health)) {
            responseNew = getData(jsonPathEvaluator.get(actuatorHealthHref));
        } else if (link.equals(info)) {
            responseNew = getData(jsonPathEvaluator.get(actuatorInfoHref));
        }
        FeatureRegistry.getCurrentFeature().setData(String.class, data, responseNew.body().asString());
    }

    @Then("Verify collected health link (.*) from Actuator for (?:staging|dev|qa|production) environment")
    public void verifyEnvDataHealth(String res) {
        JsonPath jsonPathEvaluator = JsonPath.from(FeatureRegistry.getCurrentFeature().getData(String.class, res));
        assertEquals(ValidationMessages.MESSAGE_DATA + statusStateUp,
                statusStateUp, jsonPathEvaluator.get(status));
    }

    @Then("Verify collected info link (.*) from Actuator for (?:staging|dev|qa|production) environment")
    public void verifyEnvDataInfo(String res) {
        JsonPath jsonPathEvaluator = JsonPath.from(FeatureRegistry.getCurrentFeature().getData(String.class, res));
        assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, jsonPathEvaluator.get(name).toString().isEmpty());
        assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, jsonPathEvaluator.get(version).toString().isEmpty());
        assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY,
                jsonPathEvaluator.get(gitCommitTime).toString().isEmpty());
        assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, jsonPathEvaluator.get(gitCommitId).toString().isEmpty());
        assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, jsonPathEvaluator.get(gitBranch).toString().isEmpty());
    }

    @When("Get data from info link (.*)")
    public void getDataInfo(String resp) {
        OperationHandlerUtil.handleUsingGET1(Options.storeAs(resp), Options.logRequest(), Options.logResponse());
    }

    @When("Get data from health link (.*)")
    public void getDataHealth(String resp) {
        OperationHandlerUtil.handleUsingGET(Options.storeAs(resp), Options.logRequest(), Options.logResponse());
    }

    @Then("Verify collected health link (.*) from Actuator")
    public void verifyEnvDataHealthData(String res) {
        Response healthData = Responses.getResponse(res);
        JsonPath jsonPathEvaluator = JsonPath.from(healthData.body().asString());
        assertEquals(ValidationMessages.MESSAGE_DATA + statusStateUp,
                statusStateUp, jsonPathEvaluator.get(status));
    }

    @Then("Verify collected info link (.*) from Actuator")
    public void verifyEnvDataInfodata(String res) {
        Response healthData = Responses.getResponse(res);
        JsonPath jsonPathEvaluator = JsonPath.from(healthData.body().asString());
        assertEquals(ValidationMessages.MESSAGE_DATA + nameData,
                nameData, jsonPathEvaluator.get(name));
        assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, jsonPathEvaluator.get(version).toString().isEmpty());
        assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY,
                jsonPathEvaluator.get(gitCommitTime).toString().isEmpty());
        assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, jsonPathEvaluator.get(gitCommitId).toString().isEmpty());
        assertFalse(ValidationMessages.MESSAGE_KEY_EMPTY, jsonPathEvaluator.get(gitBranch).toString().isEmpty());
    }

    public static Response getData(String url) {
        RestAssured.baseURI = url;
        RequestSpecification httpRequest = RestAssured.given();
        return httpRequest.get();
    }
}
