package com.codegraph.v3.restapi.automation.e2e;

import com.aurea.automation.codegraph.oa3.models.ErrorResponse;
import com.aurea.automation.codegraph.oa3.models.ExtendedAttribute;
import com.aurea.automation.codegraph.oa3.models.StackBuildRequest;
import com.aurea.automation.codegraph.oa3.models.StackBuildRequestDto;
import com.aurea.automation.codegraph.oa3.models.StackBuildStatus;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import com.codegraph.v3.restapi.automation.utilities.Options;
import com.codegraph.v3.restapi.automation.utilities.StackUtil;
import com.codegraph.v3.restapi.automation.utilities.StackbuildUtil;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Properties;

import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.apache.http.HttpStatus.SC_UNPROCESSABLE_ENTITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StackBuildStepDefs {

    @When("^Get stack (.*) by id$")
    public void getStackById(String stackTag) {
        ExtendedAttribute stackData = FeatureRegistry.getCurrentFeature().getData(ExtendedAttribute.class, stackTag);
        StackUtil.getStackDetailsUsingGET(stackData.getStackId(), Options.storeAs(stackTag));
    }

    @When("^Prepare parametereised stack build content (.*)$")
    public static void prepareParameterisedStackContent(
            String buildContentTag, String bucket, String id, String buildUpdatesWebhookUrl,
            String key, String repositoryUrl, String revision, String s3AccessKey, String s3SecretKey,
            String s3Url, String stackId) {
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bucket)
                .id(id)
                .buildUpdatesWebhookUrl(buildUpdatesWebhookUrl)
                .key(key)
                .repositoryUrl(repositoryUrl)
                .revision(revision)
                .s3AccessKey(s3AccessKey)
                .s3SecretKey(s3SecretKey)
                .s3Url(s3Url)
                .stackId(stackId);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Prepare stack build content (.*) using stack (.*)$")
    public void prepareStackContent(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
        StackBuildRequestDto buildRequestDto = FeatureRegistry.getCurrentFeature().getData(StackBuildRequestDto.class, buildContentTag);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Logged in User requests to create stack build (.*) with content (.*)$")
    public void requestToCreateStackBuild(String stackBuildTag, String buildContentTag) {
        StackBuildRequestDto buildRequestDto = FeatureRegistry.getCurrentFeature().getData(StackBuildRequestDto.class, buildContentTag);
        StackbuildUtil.createStackBuildRequestUsingPOST(buildRequestDto,
                Options.storeAs(stackBuildTag),
                Options.logRequest(),
                Options.logResponse());
    }

    @Then("^Verify stack build (.*) created successfully$")
    public void verifyStackBuildCreated(String stackBuildTag) {
        Response resp = Responses.getResponse(stackBuildTag);
        StackBuildRequestDto buildRequestDto = resp.as(StackBuildRequestDto.class);
        assertTrue(!buildRequestDto.getId().isEmpty());
        assertTrue(!buildRequestDto.getStackId().isEmpty());
        assertTrue(!buildRequestDto.getRepositoryUrl().isEmpty());
        assertTrue(!buildRequestDto.getRepositoryUrl().isEmpty());
        assertTrue(!buildRequestDto.getRevision().isEmpty());
        assertTrue(!buildRequestDto.getS3Url().isEmpty());
        assertTrue(!buildRequestDto.getBucket().isEmpty());
        assertTrue(!buildRequestDto.getKey().isEmpty());
    }

    @Then("^Verify stack build response (.*) is unauthorised$")
    public void verifyStackBuildRequestIsUnauthorised(String stackBuildTag) {
        Responses.getResponse(stackBuildTag).then().assertThat().statusCode(SC_UNAUTHORIZED);
    }

    @Then("^Verify stack build response (.*) UnAuthorized access message$")
    public void verifyAuthenticationRequiredMessageForStackBuildRequest(String stackBuildTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.AUTHENTICATION_REQUIRED_MESSAGE));
    }

    @Then("^Verify stack build response (.*) has invalid token message$")
    public void verifyAuthenticationInvalidTokenMessageForStackBuildRequest(String stackBuildTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.INVALID_TOKEN_MESSAGE));
    }

    @Then("^Verify stack build response (.*) has conflict with stack build (.*)$")
    public void verifyStackBuildRequestHasConflict(String stackBuildTag, String stackBuildTagOrg) {
        Responses.getResponse(stackBuildTag).then().assertThat().statusCode(SC_CONFLICT);
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        StackBuildRequestDto buildRequestDto = Responses.getResponse(stackBuildTagOrg).as(StackBuildRequestDto.class);
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals("Stack Build request " + buildRequestDto.getId() + " already exists"));
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with empty bucket$")
    public void stackContentWithEmptyBucket(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, "", bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with empty id$")
    public void stackContentWithEmptyId(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), "",
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with empty key$")
    public void stackContentWithEmptyKey(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), "", bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with empty repositoryUrl$")
    public void stackContentWithEmptyRepositoryUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), "",
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with empty revision$")
    public void stackContentWithEmptyRevision(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                "", bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with empty s3AccessKey$")
    public void stackContentWithEmptyS3AccessKey(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), "", bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with empty s3SecretKey$")
    public void stackContentWithEmptyS3SecretKey(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), "",
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Prepare content of stack build (.*) with empty stack ID$")
    public void stackContentWithEmptyStackId(String buildContentTag) {
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), "");
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with empty s3Url$")
    public void stackContentWithEmptyS3Url(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                "", stackData.getStackId());
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with inconsistent s3Url$")
    public void stackContentWithInconsistentS3Url(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                "https://google.com", stackData.getStackId());
    }

    @When("^Prepare content of stack build (.*) using stack (.*) with incorrectly formatted s3Url$")
    public void stackContentWithIncorrectlyFormattedS3Url(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                "google.com", stackData.getStackId());
    }

    @Then("^Verify stack build response (.*) status is unprocessable$")
    public void verifyStackBuildRequestIsUnprocessable(String stackBuildTag) {
        Responses.getResponse(stackBuildTag).then().assertThat().statusCode(SC_UNPROCESSABLE_ENTITY);
    }

    @Then("^Verify stack build response (.*) has message of unprocessable error for field (.*)$")
    public void verifyStackBuildRequestHasUnprocessableField(String stackBuildTag, String field) {
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.REQUIRED_PARAMETERS_MUST));
        assertTrue(errorResponse.getValidation().get(0).getField().equals("/" + field));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.FIELD_IS_REQUIRED));
    }

    @Then("^Verify stack build response (.*) has message of wrong s3 url message$")
    public void verifyStackBuildRequestHaWrongUrlMsg(String stackBuildTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.WRONG_S3_URL));
        assertTrue(errorResponse.getValidation().get(0).getField().equals("/s3Url"));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.WRONG_S3_URL));
    }

    @When("^Create stack build content (.*) using stack (.*) without bucket")
    public void prepareStackContentWithoutBucket(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .id(bc.getProperty(ConstantsUtils.ID))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .key(bc.getProperty(ConstantsUtils.KEY))
                .repositoryUrl(bc.getProperty(ConstantsUtils.REPOSITORY_URL))
                .revision(bc.getProperty(ConstantsUtils.REVISION))
                .s3AccessKey(bc.getProperty(ConstantsUtils.S3_ACCESS_KEY))
                .s3SecretKey(bc.getProperty(ConstantsUtils.S3_SECRETE_KEY))
                .s3Url(bc.getProperty(ConstantsUtils.S3_URL))
                .stackId(stackData.getStackId());
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Create stack build content (.*) using stack (.*) without id$")
    public void prepareStackContentWithoutId(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bc.getProperty(ConstantsUtils.BUCKET))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .key(bc.getProperty(ConstantsUtils.KEY))
                .repositoryUrl(bc.getProperty(ConstantsUtils.REPOSITORY_URL))
                .revision(bc.getProperty(ConstantsUtils.REVISION))
                .s3AccessKey(bc.getProperty(ConstantsUtils.S3_ACCESS_KEY))
                .s3SecretKey(bc.getProperty(ConstantsUtils.S3_SECRETE_KEY))
                .s3Url(bc.getProperty(ConstantsUtils.S3_URL))
                .stackId(stackData.getStackId());
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Create stack build content (.*) using stack (.*) without key$")
    public void prepareStackContentWithoutKey(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bc.getProperty(ConstantsUtils.BUCKET))
                .id(bc.getProperty(ConstantsUtils.ID))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .repositoryUrl(bc.getProperty(ConstantsUtils.REPOSITORY_URL))
                .revision(bc.getProperty(ConstantsUtils.REVISION))
                .s3AccessKey(bc.getProperty(ConstantsUtils.S3_ACCESS_KEY))
                .s3SecretKey(bc.getProperty(ConstantsUtils.S3_SECRETE_KEY))
                .s3Url(bc.getProperty(ConstantsUtils.S3_URL))
                .stackId(stackData.getStackId());
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Create stack build content (.*) using stack (.*) without repositoryUrl$")
    public void prepareStackContentWithoutRepositoryUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bc.getProperty(ConstantsUtils.BUCKET))
                .id(bc.getProperty(ConstantsUtils.ID))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .key(bc.getProperty(ConstantsUtils.KEY))
                .revision(bc.getProperty(ConstantsUtils.REVISION))
                .s3AccessKey(bc.getProperty(ConstantsUtils.S3_ACCESS_KEY))
                .s3SecretKey(bc.getProperty(ConstantsUtils.S3_SECRETE_KEY))
                .s3Url(bc.getProperty(ConstantsUtils.S3_URL))
                .stackId(stackData.getStackId());
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Create stack build content (.*) using stack (.*) without revision$")
    public void prepareStackContentWithoutRevision(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bc.getProperty(ConstantsUtils.BUCKET))
                .id(bc.getProperty(ConstantsUtils.ID))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .key(bc.getProperty(ConstantsUtils.KEY))
                .repositoryUrl(bc.getProperty(ConstantsUtils.REPOSITORY_URL))
                .s3AccessKey(bc.getProperty(ConstantsUtils.S3_ACCESS_KEY))
                .s3SecretKey(bc.getProperty(ConstantsUtils.S3_SECRETE_KEY))
                .s3Url(bc.getProperty(ConstantsUtils.S3_URL))
                .stackId(stackData.getStackId());
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Create stack build content (.*) using stack (.*) without s3AccessKey$")
    public void prepareStackContentWithoutS3AccessKey(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bc.getProperty(ConstantsUtils.BUCKET))
                .id(bc.getProperty(ConstantsUtils.ID))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .key(bc.getProperty(ConstantsUtils.KEY))
                .repositoryUrl(bc.getProperty(ConstantsUtils.REPOSITORY_URL))
                .revision(bc.getProperty(ConstantsUtils.REVISION))
                .s3SecretKey(bc.getProperty(ConstantsUtils.S3_SECRETE_KEY))
                .s3Url(bc.getProperty(ConstantsUtils.S3_URL))
                .stackId(stackData.getStackId());
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Create stack build content (.*) using stack (.*) without s3SecretKey$")
    public void prepareStackContentWithoutS3SecretKey(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bc.getProperty(ConstantsUtils.BUCKET))
                .id(bc.getProperty(ConstantsUtils.ID))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .key(bc.getProperty(ConstantsUtils.KEY))
                .repositoryUrl(bc.getProperty(ConstantsUtils.REPOSITORY_URL))
                .revision(bc.getProperty(ConstantsUtils.REVISION))
                .s3AccessKey(bc.getProperty(ConstantsUtils.S3_ACCESS_KEY))
                .s3Url(bc.getProperty(ConstantsUtils.S3_URL))
                .stackId(stackData.getStackId());
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Create stack build content (.*) using stack (.*) without s3Url$")
    public void prepareStackContentWithoutS3Url(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bc.getProperty(ConstantsUtils.BUCKET))
                .id(bc.getProperty(ConstantsUtils.ID))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .key(bc.getProperty(ConstantsUtils.KEY))
                .repositoryUrl(bc.getProperty(ConstantsUtils.REPOSITORY_URL))
                .revision(bc.getProperty(ConstantsUtils.REVISION))
                .s3AccessKey(bc.getProperty(ConstantsUtils.S3_ACCESS_KEY))
                .s3SecretKey(bc.getProperty(ConstantsUtils.S3_SECRETE_KEY))
                .stackId(stackData.getStackId());
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Create stack build content (.*) without stackId$")
    public void prepareStackContentWithoutStack(String buildContentTag) {
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bc.getProperty(ConstantsUtils.BUCKET))
                .id(bc.getProperty(ConstantsUtils.ID))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .key(bc.getProperty(ConstantsUtils.KEY))
                .repositoryUrl(bc.getProperty(ConstantsUtils.REPOSITORY_URL))
                .revision(bc.getProperty(ConstantsUtils.REVISION))
                .s3AccessKey(bc.getProperty(ConstantsUtils.S3_ACCESS_KEY))
                .s3SecretKey(bc.getProperty(ConstantsUtils.S3_SECRETE_KEY))
                .s3Url(bc.getProperty(ConstantsUtils.S3_URL));
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Create content of stack build (.*) with non existing stack ID$")
    public void stackContentWithNonExistingStackId(String buildContentTag) {
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), ConstantsUtils.NON_EXISTING);
    }

    @When("^Create content of stack build (.*) using stack (.*) with invalid s3AccessKey$")
    public void stackContentWithInvalidS3AccessKey(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), ConstantsUtils.NON_EXISTING, bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Create content of stack build (.*) using stack (.*) with invalid s3SecretKey$")
    public void stackContentWithInvalidS3SecretKey(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), ConstantsUtils.NON_EXISTING,
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Create content of stack build (.*) using stack (.*) with invalid bucket$")
    public void stackContentWithInvalidBucket(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContent(buildContentTag, ConstantsUtils.NON_EXISTING, bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @Then("^Verify if specified id of stack (.*) does not exists$")
    public void verifyStackNameAsIdDoneNotExists(String stackBuildTag) {
        Responses.getResponse(stackBuildTag).then().assertThat().statusCode(SC_NOT_FOUND);
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.STACK_NOT_EXISTS));
    }

    @Then("^Verify stack build response (.*) has message of Bad S3 credentials provided$")
    public void verifyStackBuildRequestHasBadS3CredentialsMsg(String stackBuildTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.BAD_S3_CREDENTIALS));
        assertTrue(errorResponse.getValidation().get(0).getField().equals(ConstantsUtils.S3_ACCESS_KEY));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.BAD_S3_CREDENTIALS));
        assertTrue(errorResponse.getValidation().get(1).getField().equals(ConstantsUtils.S3_SECRETE_KEY));
        assertTrue(errorResponse.getValidation().get(1).getMessage().equals(ConstantsUtils.BAD_S3_CREDENTIALS));
    }

    @Then("^Verify stack build response (.*) has message of No such bucket$")
    public void verifyStackBuildRequestHasNoSuchMsg(String stackBuildTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        Response status = Responses.getResponse(stackBuildTag);
        assertEquals(status.getStatusCode(), SC_UNPROCESSABLE_ENTITY);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.NO_SUCH_BUCKET));
        assertTrue(errorResponse.getValidation().get(0).getField().equals("/" + ConstantsUtils.BUCKET));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.NO_SUCH_BUCKET));
    }

    @When("Fetch stack (.*) by id having response (.*)")
    public void getStackInfo(String stackTag, String response) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        StackUtil.getStackDetailsUsingGET(stackData.getStackId(), Options.storeAs(response),
                Options.logResponse(),
                Options.logRequest());
    }

    @When("^Create stack build content (.*) using stack (.*) with empty repositoryUrl$")
    public void prepareStackContentEmptyRepositoryUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bc.getProperty(ConstantsUtils.BUCKET))
                .id(bc.getProperty(ConstantsUtils.ID))
                .buildUpdatesWebhookUrl(bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL))
                .repositoryUrl("")
                .key(bc.getProperty(ConstantsUtils.KEY))
                .revision(bc.getProperty(ConstantsUtils.REVISION))
                .s3AccessKey(bc.getProperty(ConstantsUtils.S3_ACCESS_KEY))
                .s3SecretKey(bc.getProperty(ConstantsUtils.S3_SECRETE_KEY))
                .s3Url(bc.getProperty(ConstantsUtils.S3_URL))
                .stackId(stackData.getStackId());
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("Get stack build (.*) by id having response (.*)")
    public void getStackBuildInfo(String stackBuild, String response) {
        StackBuildRequestDto stackData = Responses.getResponse(stackBuild).as(StackBuildRequestDto.class);
        StackbuildUtil.getStackBuildStatus(stackData.getId(), Options.storeAs(response),
                Options.logResponse(),
                Options.logRequest());
    }

    @Then("^Verify stack build (.*) status is pending$")
    public void verifyStackBuildStatusPending(String stackBuildTag) {
        Response resp = Responses.getResponse(stackBuildTag);
        StackBuildRequest buildRequest = resp.as(StackBuildRequest.class);
        assertTrue(buildRequest.getDumpFile().isEmpty());
        assertTrue(buildRequest.getStatus().equals(ConstantsUtils.PENDING_TEXT));
        assertTrue(buildRequest.getMessage().isEmpty());
    }

    @When("^Poll stack (.*) until response (.*) status is (?:success|failure)")
    public void pollStatusOfStackBuild(String stackBuild, String getResponse) throws InterruptedException {
        for (int counter = 0; counter < 100; counter++) {
            StackBuildRequestDto stackData = Responses.getResponse(stackBuild).as(StackBuildRequestDto.class);
            Response response = StackbuildUtil.getStackBuildStatus(stackData.getId(), Options.storeAs(getResponse),
                    Options.logResponse());
            if (response.as(StackBuildRequest.class).getStatus().equals(ConstantsUtils.REQUEST_SUCCESS)
                    || response.as(StackBuildRequest.class).getStatus().equals(ConstantsUtils.REQUEST_ERROR)) {
                break;
            }
            // This polling needed as changing status takes few minute as written in test cases.
            Thread.sleep(ConstantsUtils.XXL);
        }
    }

    @Then("^Verify stack build (.*) status is success$")
    public void verifyStackBuildStatusIsSuccess(String stackBuildTag) {
        Response resp = Responses.getResponse(stackBuildTag);
        StackBuildRequest buildRequest = resp.as(StackBuildRequest.class);
        assertTrue(ConstantsUtils.testValidation(ConstantsUtils.REQUEST_SUCCESS, buildRequest.getStatus()),
                buildRequest.getStatus().equals(ConstantsUtils.REQUEST_SUCCESS));
        assertFalse(buildRequest.getDumpFile().isEmpty());
        assertTrue(buildRequest.getDumpFile().contains(ConstantsUtils.S3));
        assertTrue(!buildRequest.getMessage().isEmpty());
    }

    @When("^Get stack build by non-existing id having response (.*)$")
    public void getStackBuildInfoForNonExistingID(String response) {
        StackbuildUtil.getStackBuildStatus(ConstantsUtils.NON_EXISTING, Options.storeAs(response));
    }

    @When("Fetch stackbuild status (.*) by id having response (.*)")
    public void fetchStackBuildStatus(String stackBuild, String response) {
        StackBuildRequestDto stackData = Responses.getResponse(stackBuild).as(StackBuildRequestDto.class);
        StackbuildUtil.getStackBuildStatus1(stackData.getId(), Options.storeAs(response),
                Options.logResponse(),
                Options.logRequest());
    }

    @Then("Verify stack build status response (.*)")
    public void verifyStackBuildStatus(String stackBuildTag) {
        Response resp = Responses.getResponse(stackBuildTag);
        resp.then().assertThat().statusCode(SC_OK);
        StackBuildStatus buildRequestDto = resp.as(StackBuildStatus.class);
        assertFalse(buildRequestDto.getKind().isEmpty());
        assertFalse(buildRequestDto.getSelfLink().isEmpty());
        assertFalse(buildRequestDto.getStackName().isEmpty());
        assertFalse(buildRequestDto.getStatus().isEmpty());
    }

    @When("Get stack build (.*) by invalid stack id having response (.*)")
    public void getStackBuildInvalidStackBuild(String stackBuild, String response) {
        StackBuildRequestDto stackData = Responses.getResponse(stackBuild).as(StackBuildRequestDto.class);
        StackbuildUtil.getStackBuildStatus(ConstantsUtils.INVALID_VALUE, Options.storeAs(response),
                Options.logResponse(),
                Options.logRequest());
    }

    @Then("^Verify if stack build (.*) with specified id does not exists$")
    public void verifyStackBuildWithIdDoneNotExists(String stackBuildTag) {
        Responses.getResponse(stackBuildTag).then().assertThat().statusCode(SC_NOT_FOUND);
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.STACK_BUILD_WITH_ID_DOES_NOT_EXISTS));
    }

    @Then("^Verify if specified stack build id (.*) does not exists$")
    public void verifyStackBuildID(String stackBuildTag) {
        Responses.getResponse(stackBuildTag).then().assertThat().statusCode(SC_NOT_FOUND);
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.STACK_BUILD_NOT_EXISTS));
    }

    @Then("Verify stack build (.*) data by ID")
    public void verifyStackBuildData(String stackBuildTag) {
        Response resp = Responses.getResponse(stackBuildTag);
        StackBuildRequest buildRequestDto = resp.as(StackBuildRequest.class);
        resp.then().assertThat().statusCode(SC_OK);
        assertFalse(buildRequestDto.getId().isEmpty());
        assertFalse(buildRequestDto.getRepositoryUrl().isEmpty());
        assertFalse(buildRequestDto.getRepositoryUrl().isEmpty());
        assertFalse(buildRequestDto.getRevision().isEmpty());
        assertFalse(buildRequestDto.getBucket().isEmpty());
        assertFalse(buildRequestDto.getStatus().isEmpty());
    }

    @Then("Verify DB dump file in  stack build (.*) is uploaded to the s3-storage as zip file")
    public void verifyStackBuildDBDumpS3(String stackBuildTag) {
        Response resp = Responses.getResponse(stackBuildTag);
        StackBuildRequest buildRequest = resp.as(StackBuildRequest.class);
        assertTrue(buildRequest.getStatus().equals(ConstantsUtils.REQUEST_SUCCESS));
        String filename = buildRequest.getDumpFile()
                .replace(ConstantsUtils.S3 + buildRequest.getBucket()
                        + ConstantsUtils.SLASH, ConstantsUtils.BLANK);
        assertTrue(FilesStepDefs.isExistS3(filename, buildRequest.getBucket()));
    }
}
