package com.codegraph.v3.restapi.automation.e2e;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.aurea.automation.codegraph.oa3.models.LayerDefinitionResponse;
import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.data.UrlConstantsUtils;
import com.codegraph.v3.restapi.automation.e2e.commonsteps.FeatureRegistry;
import com.codegraph.v3.restapi.automation.utilities.LayerdefinitionUtil;
import com.codegraph.v3.restapi.automation.utilities.OAuthUtils;
import com.codegraph.v3.restapi.automation.utilities.Options;
import com.codegraph.v3.restapi.automation.utilities.UsersConfig;
import com.xo.restapi.automation.context.TestContext;
import com.xo.restapi.automation.context.UserContext;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.junit.Assert;

@SuppressWarnings("PMD.AvoidPrintStackTrace")
public class FilesStepDefs {
    private static final String FILES = "files";
    private final TestContext textContext;
    private Response uploadResponse;
    private List<String> s3Keys;

    private static final Logger log = Logger.getLogger(FilesStepDefs.class.getName());

    public FilesStepDefs(TestContext context) {
        textContext = context;
    }

    private Response getLayerDefinitionResponse() {
        return FeatureRegistry.getCurrentFeature().getData(Response.class,
                ConstantsUtils.LAYER_DEFINITION_RESPONSE);
    }

    private static final Pattern PATTERN = Pattern.compile("/1/");

    private List<String> getS3Keys() {
        List<String> keys = new ArrayList<>();
        try {
            BasicAWSCredentials awsCred =
                    new BasicAWSCredentials(UsersConfig.getInstance().getS3Key(),
                            UsersConfig.getInstance().getS3Secret());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCred))
                    .withRegion(UsersConfig.getInstance().getRegion())
                    .build();
            ListObjectsV2Request req = new ListObjectsV2Request()
                    .withBucketName(UsersConfig.getInstance().getS3TempBucket());
            ListObjectsV2Result result;
            do {
                result = s3Client.listObjectsV2(req);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    keys.add(objectSummary.getKey());
                }
                String token = result.getNextContinuationToken();
                req.setContinuationToken(token);
            } while (result.isTruncated());
        } catch (SdkClientException e) {
            e.printStackTrace();
            log.log(Level.WARNING, ConstantsUtils.ERROR_NO_KEY, e);
        }
        return keys;
    }

    @Given("^Download jar file to local machine at (.*)$")
    public void downloadJarFile(String localFilePath) throws IOException {
        try {
            String jarUrl = "https://scm.devfactory.com/nexus/content/repositories/snapshots/com/devfactory/"
                    + "codegraph/v3/builder/codegraph-samples/hello-world/0.2.11-SNAPSHOT/"
                    + "hello-world-0.2.11-20181009.210430-1.jar";
            InputStream in = new URL(jarUrl).openStream();
            Files.copy(in, Paths.get(localFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Given("^Download neo4j jar file to local machine at (.*)$")
    public void downloadneo4jJarFile(String localFilePath) throws IOException {
        try {
            String jarUrl = "https://scm.devfactory.com/nexus/content/repositories/snapshots/com/devfactory/"
                    + "codegraph/v3/builder/codegraph-samples/neo4j-procedure/0.2.11-SNAPSHOT/"
                    + "neo4j-procedure-0.2.11-20181026.154420-17.jar";
            InputStream in = new URL(jarUrl).openStream();
            Files.copy(in, Paths.get(localFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @When("^Upload File \"([^\"]*)\" with tag (.*)$")
    public void uploadJarFile(String fileName, String fileTag) {
        uploadResponse = LayerdefinitionUtil.uploadJarToS3UsingPOST(new File(fileName),
                Options.storeAs(fileTag));
        FeatureRegistry.getCurrentFeature().setData(String.class, fileTag, uploadResponse.body().as(String.class));
    }

    @Then("^File uploaded successfully$")
    public void fileUploaded() {
        uploadResponse.then().assertThat().statusCode(SC_CREATED);
        List<String> files = uploadResponse.jsonPath().getList("$");
        textContext.addValue("file", files.get(0));
    }

    @When("^Open S3 storage and Check Files")
    public void openS3CheckFiles() {
        s3Keys = getS3Keys();
    }

    @Then("Uploaded Jar is found")
    public void findJarInS3() {
        Assert.assertTrue(isFound(textContext.getValue("file")));
    }

    @Given("^Create tmp file (.*)$")
    public void createTempFile(String fileName) throws IOException {
        String fileData = "Test";
        Files.write(Paths.get(fileName), fileData.getBytes());
    }

    private boolean isFound(String file) {
        for (String key : s3Keys) {
            if (key.contains(file)) {
                return true;
            }
        }
        return false;
    }

    @Then("File upload fails with error {int} and contains text {string}")
    public void fileUploadFailsWithError(int errorCode, String errorText) {
        uploadResponse.then().assertThat().statusCode(errorCode);
        Assert.assertTrue(uploadResponse.getBody().asString().contains(errorText));
    }

    @Then("^No files with \"([^\"]*)\" extensions exist$")
    public void noFilesWithExtensionsExist(String extension) {
        for (String key : s3Keys) {
            Assert.assertFalse(key.contains(extension));
        }
    }

    @When("^Upload File \"([^\"]*)\" without token$")
    public void uploadFileWithoutToken(String fileName) {
        uploadResponse =
                given()
                        .header(OAuthUtils.X_AUTH_TOKEN, "Bearer ")
                        .multiPart(FILES, new File(fileName))
                        .when().post(UrlConstantsUtils.FILES_ENDPOINT);
    }

    @When("User tries to upload without file")
    public void userTriesToUploadWithoutFile() {
        uploadResponse =
                given()
                        .header(OAuthUtils.X_AUTH_TOKEN, "Bearer " + UserContext.getToken())
                        .when().post(UrlConstantsUtils.FILES_ENDPOINT);
    }

    private List<String> getPermanentS3Keys() {
        List<String> keys = new ArrayList<>();
        try {
            BasicAWSCredentials awsCred =
                    new BasicAWSCredentials(UsersConfig.getInstance().getS3Key(),
                            UsersConfig.getInstance().getS3Secret());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCred))
                    .withRegion(UsersConfig.getInstance().getRegion())
                    .build();
            ListObjectsV2Request req = new ListObjectsV2Request()
                    .withBucketName(UsersConfig.getInstance().getS3LayersBucket());
            ListObjectsV2Result result;
            do {
                result = s3Client.listObjectsV2(req);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    keys.add(objectSummary.getKey());
                }
                String token = result.getNextContinuationToken();
                req.setContinuationToken(token);
            } while (result.isTruncated());
        } catch (SdkClientException e) {
            log.log(Level.WARNING, ConstantsUtils.ERROR_NO_KEY, e);
        }
        return keys;
    }

    @When("^Open S3 permanent storage and Check Files")
    public void openPermanentS3CheckFiles() {
        s3Keys = getPermanentS3Keys();
    }

    @Then("Uploaded layer Jar is found")
    public void findLayerJarInS3() {
        LayerDefinitionResponse layerDef = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        Assert.assertTrue(isFound(PATTERN.split(layerDef.getLayerCode())[1]));
    }

    @Then("Uploaded layer Jar is not found")
    public void layerJarIsNotFoundInS3() {
        LayerDefinitionResponse layerDef = getLayerDefinitionResponse().as(LayerDefinitionResponse.class);
        Assert.assertFalse(isFound(PATTERN.split(layerDef.getLayerCode())[1]));
    }

    public static boolean isExistS3(String file, String bucketName) {
        try {
            BasicAWSCredentials awsCred =
                    new BasicAWSCredentials(UsersConfig.getInstance().getS3Key(),
                            UsersConfig.getInstance().getS3Secret());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCred))
                    .withRegion(UsersConfig.getInstance().getRegion())
                    .build();
            ObjectListing objects = s3Client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                if (objectSummary.getKey().contains(file)) {
                    return true;
                }
            }
            return false;
        } catch (SdkClientException e) {
            log.log(Level.WARNING, ConstantsUtils.ERROR_NO_KEY, e);
        }
        return false;
    }
}
