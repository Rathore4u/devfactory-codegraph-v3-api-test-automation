package com.codegraph.v3.restapi.automation.e2e;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.aurea.automation.codegraph.oa3.models.ErrorResponse;
import com.aurea.automation.codegraph.oa3.models.ExtendedAttribute;
import com.aurea.automation.codegraph.oa3.models.StackBuildRequest;
import com.aurea.automation.codegraph.oa3.models.StackBuildRequestDto;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.Responses;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.Properties;
import org.apache.commons.lang.RandomStringUtils;

public class StackBuildStepDefsV1 {

    public void prepareParameterisedStackContentWithoutBuildUpdatesWebhookUrl(
            String buildContentTag, String bucket, String id,
            String key, String repositoryUrl, String revision, String s3AccessKey, String s3SecretKey,
            String s3Url, String stackId) {
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bucket)
                .id(id)
                .key(key)
                .repositoryUrl(repositoryUrl)
                .revision(revision)
                .s3AccessKey(s3AccessKey)
                .s3SecretKey(s3SecretKey)
                .s3Url(s3Url)
                .stackId(stackId);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Prepare build content (.*) without buildUpdatesWebhookUrl using stack (.*)$")
    public void prepareStackContentWithoutBuildWebhookUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContentWithoutBuildUpdatesWebhookUrl(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
        StackBuildRequestDto buildRequestDto = FeatureRegistry.getCurrentFeature().getData(StackBuildRequestDto.class, buildContentTag);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Prepare build content (.*) with empty buildUpdatesWebhookUrl using stack (.*)$")
    public void prepareStackContentWithEmptyBuildWebhookUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildStepDefs.prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                "", bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
        StackBuildRequestDto buildRequestDto = FeatureRegistry.getCurrentFeature().getData(StackBuildRequestDto.class, buildContentTag);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Prepare build content (.*) with null buildUpdatesWebhookUrl using stack (.*)$")
    public void prepareStackContentWithNullBuildWebhookUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildStepDefs.prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                null, bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
        StackBuildRequestDto buildRequestDto = FeatureRegistry.getCurrentFeature().getData(StackBuildRequestDto.class, buildContentTag);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Prepare build content (.*) with incorrect buildUpdatesWebhookUrl using stack (.*)$")
    public void prepareStackContentWithIncorrectBuildWebhookUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildStepDefs.prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                ConstantsUtils.GOOGLE_URL, bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
        StackBuildRequestDto buildRequestDto = FeatureRegistry.getCurrentFeature().getData(StackBuildRequestDto.class, buildContentTag);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @Then("^Verify stack build response (.*) has message of incorrect buildUpdatesWebhookUrl$")
    public void verifyStackBuildRequestHasIncorrectbuildUpdatesWebhookUrl(String stackBuildTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.REQUIRED_PARAMETERS_MUST));
        assertTrue(errorResponse.getValidation().get(0).getField().equals("/" + ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL));
        assertTrue(errorResponse.getValidation().get(0).getMessage().contains(ConstantsUtils.SHOULD_BE_EQUAL_TEXT));
    }

    @When("^Prepare build content (.*) with non URL buildUpdatesWebhookUrl using stack (.*)$")
    public void prepareStackContentWithNonUrlBuildWebhookUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildStepDefs.prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                "auto-qa-dev-api-codegraph-eng.devfactory.com/stackbuilds/" + requestId + "/updates", bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
        StackBuildRequestDto buildRequestDto = FeatureRegistry.getCurrentFeature().getData(StackBuildRequestDto.class, buildContentTag);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Prepare build content (.*) with one space buildUpdatesWebhookUrl using stack (.*)$")
    public void prepareStackContentWithOneSpaceBuildWebhookUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildStepDefs.prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                " ", bc.getProperty(ConstantsUtils.KEY), bc.getProperty(ConstantsUtils.REPOSITORY_URL),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
        StackBuildRequestDto buildRequestDto = FeatureRegistry.getCurrentFeature().getData(StackBuildRequestDto.class, buildContentTag);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Prepare stack build content (.*) without buildUpdatesWebhookUrl$")
    public void prepareParameterisedStackContentWithoutRepositoryUrl(
            String buildContentTag, String bucket, String id, String buildUpdatesWebhookUrl,
            String key, String revision, String s3AccessKey, String s3SecretKey,
            String s3Url, String stackId) {
        StackBuildRequestDto buildRequestDto = new StackBuildRequestDto();
        buildRequestDto
                .bucket(bucket)
                .id(id)
                .buildUpdatesWebhookUrl(buildUpdatesWebhookUrl)
                .key(key)
                .revision(revision)
                .s3AccessKey(s3AccessKey)
                .s3SecretKey(s3SecretKey)
                .s3Url(s3Url)
                .stackId(stackId);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @When("^Prepare content of stack build (.*) using stack (.*) when repositoryUrl not provided$")
    public void stackContentWithEmptyRepositoryUrl(String buildContentTag, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        prepareParameterisedStackContentWithoutRepositoryUrl(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY),
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
    }

    @When("^Prepare build content (.*) with branch name (.*) in repositoryUrl using stack (.*)$")
    public void prepareStackContentWithoutBranchInRepoUrl(String buildContentTag, String branchName, String stackTag) {
        ExtendedAttribute stackData = Responses.getResponse(stackTag).as(ExtendedAttribute.class);
        String requestId = ConstantsUtils.AUTOMATION_TEXT_PREFIX + RandomStringUtils.randomAlphanumeric(7);
        Properties bc = ConstantsUtils.prepareStackBuildContent(requestId);
        StackBuildStepDefs.prepareParameterisedStackContent(buildContentTag, bc.getProperty(ConstantsUtils.BUCKET), bc.getProperty(ConstantsUtils.ID),
                bc.getProperty(ConstantsUtils.BUILD_UPDATES_WEBHOOK_URL), bc.getProperty(ConstantsUtils.KEY), branchName,
                bc.getProperty(ConstantsUtils.REVISION), bc.getProperty(ConstantsUtils.S3_ACCESS_KEY), bc.getProperty(ConstantsUtils.S3_SECRETE_KEY),
                bc.getProperty(ConstantsUtils.S3_URL), stackData.getStackId());
        StackBuildRequestDto buildRequestDto = FeatureRegistry.getCurrentFeature().getData(StackBuildRequestDto.class, buildContentTag);
        FeatureRegistry.getCurrentFeature().setData(StackBuildRequestDto.class, buildContentTag, buildRequestDto);
    }

    @Then("^Verify stack build response (.*) has message of invalid repository format$")
    public void verifyStackBuildRequestHasInvalidRepoFormatMessage(String stackBuildTag) {
        ErrorResponse errorResponse = Responses.getResponse(stackBuildTag).as(ErrorResponse.class);
        assertTrue(errorResponse.getUserMessage().equals(ConstantsUtils.REQUIRED_PARAMETERS_MUST));
        assertTrue(errorResponse.getValidation().get(0).getField().equals("/" + ConstantsUtils.REPOSITORY_URL));
        assertTrue(errorResponse.getValidation().get(0).getMessage().equals(ConstantsUtils.INVALID_REPO_URL_FORMAT));
    }

    @Then("Verify stack build (.*) status is failure")
    public void verifyStackBuildStatusIsFailure(String stackBuildTag) {
        Response resp = Responses.getResponse(stackBuildTag);
        assertEquals(SC_OK, resp.getStatusCode());
        StackBuildRequest buildRequest = resp.as(StackBuildRequest.class);
        assertEquals(ConstantsUtils.testValidation(ConstantsUtils.REQUEST_ERROR, buildRequest.getStatus()),
                ConstantsUtils.REQUEST_ERROR, buildRequest.getStatus());
        assertTrue(buildRequest.getDumpFile().isEmpty());
        assertFalse(buildRequest.getMessage().isEmpty());
    }
}
