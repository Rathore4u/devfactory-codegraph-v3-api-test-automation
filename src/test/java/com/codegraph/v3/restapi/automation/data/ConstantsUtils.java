package com.codegraph.v3.restapi.automation.data;

import com.aurea.automation.codegraph.oa3.models.LayerDefinitionResponse;

import java.util.Properties;

public final class ConstantsUtils {
    public static final String DESCRIPTION = "This is my description";
    public static final String UPDATED_DESCRIPTION = "This is my updated description";
    public static final String LAYER_DEFINITION_RESPONSE = "layerDefinitionResponse";
    public static final String LAYER_DEFINITION_VERSION_RESPONSE = "layerDefinitionVersionResponse";
    public static final String STRING_CONVERSION_FAILED_MESSAGE = "Failed to convert value of type 'java.lang.String' to required type 'int'; nested exception is java.lang.NumberFormatException: For input string: ";
    public static final String STRING_CONVERSION_TO_INT_FAILED_MESSAGE = "Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException: For input string: ";
    public static final String BOOLEAN_CONVERSION_FAILED_MESSAGE = "Failed to convert value of type 'java.lang.String' to required type 'boolean'; nested exception is java.lang.IllegalArgumentException: Invalid boolean value";
    public static final String AUTHENTICATION_REQUIRED_MESSAGE = "Full authentication is required to access this resource";
    public static final String PARAM_LIMIT = "limit";
    public static final String PARAM_OFFSET = "offset";
    public static final String PARAM_ACTIVE = "active";
    public static final String TRUE_TEXT = "true";
    public static final String FALSE_TEXT = "false";
    public static final String REPLACE_TEXT = "replace";
    public static final String DEFINITION_PATH = "/definition";
    public static final String NAME_PATH = "/name";
    public static final String DESCRIPTION_PATH = "/description";
    public static final String LAYER_CODE_OR_CONTAINER_REQUIRED_MESSAGE = "Layer code or container image is required";
    public static final String LAYER_CODE_AND_CONTAINER_CANT_BE_TOGETHER_MESSAGE = "Layer code and custom container image can’t be defined together";
    public static final String BAD_REQUEST = "Bad Request";
    public static final String REQUIRED_PARAMETERS_MUST = "Required parameters must be provided";
    public static final String FIELD_IS_REQUIRED = "field is required";
    public static final String INVALID_TOKEN_MESSAGE = "Not a valid token";
    public static final String EXPIRED_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlJqWXdRamsxTXpFMk5VTXdRVE0yTVVRNVJEQXlOek5FTVRJd1JUSkdRemMyTlRsQ05qSXpOUSJ9.eyJodHRwczovL2RldmZhY3RvcnkuY29tL3JvbGVzIjoiYWRtaW4iLCJuaWNrbmFtZSI6ImZvZ29uZSIsIm5hbWUiOiJCb3JpcyBWYW5pbiIsInBpY3R1cmUiOiJodHRwczovL2F2YXRhcnMwLmdpdGh1YnVzZXJjb250ZW50LmNvbS91Lzk5ODc4MT92PTQiLCJ1cGRhdGVkX2F0IjoiMjAxOS0wMi0xMVQxNjoxODowNS41MTdaIiwiZW1haWwiOiJmb2dvbmVAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImlzcyI6Imh0dHBzOi8vYXV0aC5kZXZmYWN0b3J5LmNvbS8iLCJzdWIiOiJnaXRodWJ8OTk4NzgxIiwiYXVkIjoiQkNsZ3pBczU1Ymp6VGk3SWU3SE83Mk85WjV3Tml0MkciLCJpYXQiOjE1NDk5MDE4ODcsImV4cCI6MTY0OTkwMTg4NiwiYXRfaGFzaCI6ImJncGRvN2h0OS1LczlVTE1ucTRCTHciLCJub25jZSI6InRiSG1sQ0pxdjYxTjEzLi40bDEzTC15UkMuSlpRZm1OIn0.rR9Ur0XmJpr64Yrj8V5Ogf8SZchpD3b5caiepHzAFJzCITRU1_ciA17YXwxW7sIYk36ew8JfUPiqWBfX4SecVi9hpOX7TU-A-4JNGcqzWj2Jjfjfi7SETpIgCTDh56oIO8MXjBnYWJOawzlGFXY4_Iaj96BrN3uHC1CFQt-mG8IAX_KnW9PnXNG9Jusk60obOVck8almv_z_moas9dDHtl04rZSwJRqvu9mY4SHaggB-KqluXQp5iBIYMIfq3GK1kEr-kX2ratYDcEmBpm9Hs7d8K-crQHPkLtIJKYr_kF9xYQ6W7nsyg1V8GgN5YeMqWgWjVhv6-o9k0RcBxidYw3";
    public static final String WRONG_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlJqWXdRamsxTXpFMk5VTXdRVE0yTVVRNVJEQXlOek5FTVRJd1JUSkdRemMyTlRsQ05qSXpOUSJ9.eyJodHRwczovL2RldmZhY3RvcnkuY29tL3JvbGVzIjoidXNlciIsIm5pY2tuYW1lIjoiZW5ncWFzZXJ2aWNlIiwibmFtZSI6ImVuZy5xYW1hbnVhbHRlc3RpbmdAYXVyZWEuY29tIiwicGljdHVyZSI6Imh0dHBzOi8vYXZhdGFyczAuZ2l0aHVidXNlcmNvbnRlbnQuY29tL3UvNDYxNjM3Nzk_dj00IiwidXBkYXRlZF9hdCI6IjIwMTktMDQtMTlUMDI6NTI6NDIuMzgxWiIsImVtYWlsIjoiZW5nLnFhbWFudWFsdGVzdGluZ0BhdXJlYS5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6Ly9hdXRoLmRldmZhY3RvcnkuY29tLyIsInN1YiI6ImdpdGh1Ynw0NjE2Mzc3OSIsImF1ZCI6Im8xaTcyZzZ2RzEyQncwOWtVSTRjd0ljRUNOcHdlNllPIiwiaWF0IjoxNTU1NjQyMzYyLCJleHAiOjE1NTU2NDU5NjIsImF0X2hhc2giOiJhbjdPeWRDN2lIaXNWbG9wbXpEeFNnIiwibm9uY2UiOiJjMFRhM1I0SmtteDhQZVM3QnI3OEEwVWEwUERybkxTZyJ9.wAu3GWjoDycH3o84nbodjmAm2_8nDP1XDo8DzC0xRZLAm-pLVLUsuRs3punMrAgErJQWN73IvOXjY3JLYghtEO9Vkp5yE_1DBCf6HuFan75K-xTZivOO5EvgWG5NGyLLuGjZ5xA-_QdiXeDyoWvOqPTtubLkW2g3QJ_le8djO7cSshCqSnOAMBVfP7Xcpd3ZPIIX0wcXU9NUFAM7cdqr7yIPxRutvQZBHn12AMEmMJi4wEHGVdYmf-ZhN_NDTebL_t4HNIHcL7m3fHTiOjhM5dmSIVew5jEFv5uyeP7RJxBp_OqSHoOXECFShXvE5fUqVdXfUl-JioIbQ8ayMoA";
    public static final String METHOD_NOT_ALLOWED = "Method Not Allowed";
    public static final Integer MAX_COUNT = 99999;
    public static final String INVALID_VALUE = "invalidvaluefortest";
    public static final Integer ZERO = 0;
    public static final Integer ONE = 1;
    public static final Integer TWO = 2;
    public static final Integer FOUR = 4;
    public static final Integer FIVE = 5;
    public static final Integer EIGHT = 8;
    public static final Integer XL = 30000;
    public static final Integer XXL = 60000;
    public static final String STACK_TASK_VALIDATION_ERROR = "Stack has task validation error.";
    public static final String STACK_LAYER_DOES_NOT_EXISTS = "Task task-1 based on layer with name nonExistingLayerName does not exist.";
    public static final String STACK_HAS_DEPENDENCY_CYCLE = "Stack has dependency cycle on task task-1";
    public static final String NON_EXISTING_DEPENDENCY_ERROR = "Task task-1 has dependency on task-2 which does not exist.";
    public static final String WRONG_S3_URL = "Wrong S3 URL provided";
    public static final String BAD_S3_CREDENTIALS = "Bad S3 credentials provided";
    public static final String NO_SUCH_BUCKET = "No such bucket";
    public static final String BUCKET = "bucket";
    public static final String ID = "id";
    public static final String BUILD_UPDATES_WEBHOOK_URL = "buildUpdatesWebhookUrl";
    public static final String KEY = "key";
    public static final String REPOSITORY_URL = "repositoryUrl";
    public static final String REVISION = "revision";
    public static final String S3_ACCESS_KEY = "s3AccessKey";
    public static final String S3_SECRETE_KEY = "s3SecretKey";
    public static final String S3_URL = "s3Url";
    public static final String NON_EXISTING = "NonExisting";
    public static final String AUTOMATION_TEXT_PREFIX = "automation-test-";
    public static final String STACK_NOT_EXISTS = "A Stack with the specified id NonExisting does not exist.";
    public static final String STACK_BUILD_NOT_EXISTS = "A Stack build request with the specified id does not exist";
    public static final String ONLY_NON_EXISTING_CAN_BE_PATCHED = "Only next fields can be patched: /name,/definition,/description";
    public static final String UPDATED_TEXT = "updated";
    public static final String UNKNOWN_OPERATION_TYPE = "Unknown operation type";
    public static final String CORRUPTED_PATH_OPERATION = "Corrupted path operation";
    public static final String SHOULD_BE_DEFINED_FOR_OP = " should be defined for replace operation";
    public static final String PROVIDED_SCHEMA_INVALID = "The provided schemaVersion is not valid";
    public static final String STACK_BUILD_WITH_ID_DOES_NOT_EXISTS = "A Stack build request with the specified id does not exist";
    public static final String REQUEST_SUCCESS = "request-success";
    public static final String REQUEST_ERROR = "request-error";
    public static final String PENDING_TEXT = "PENDING";
    public static final String JAR = "hello-world-0.2.11-20181009.210430-1.jar";
    public static final String S3 = "s3://";
    public static final String SLASH = "/";
    public static final String BLANK = "";
    public static final String NEO4J = "neo4jProcedures";
    public static final String ERROR_NO_KEY = "No key found";
    public static final String GOOGLE_URL = "http://google.com";
    public static final String SHOULD_BE_EQUAL_TEXT = "should be equal to";
    public static final String INVALID_REPO_URL_FORMAT = "Repo url should have format schema:host/repo.git?branch=somebranch";
    public static final String SCHEMA_VERSION_MISSING = "A schemaVersion is not provided";
    public static final String INVALID_STACK_DEFINITION = "The provided stack definition is not valid.";
    public static final String EC_415_ERROR = "Unsupported Media Type";
    public static final String CONTENT_TYPE_TEXT = "text/plain;charset=UTF-8";
    public static final String EC_415_MESSAGE = "Content type 'text/plain;charset=UTF-8' not supported";
    public static final String TOKEN_STRING = "Bearer ";
    public static final String PATCH_REQUEST = "    {\n"
            + "        \"op\": \"replace\",\n"
            + "        \"path\": \"/name\",\n"
            + "        \"value\": \"existing layername\"\n"
            + "    }\n";

    public static Properties prepareStackBuildContent(String requestId) {
        Properties stackBuildContent = new Properties();
        stackBuildContent.setProperty("bucket", "codegraphv3-neo4j-dumps-staging");
        stackBuildContent.setProperty("id", requestId);
        stackBuildContent.setProperty("buildUpdatesWebhookUrl", "https://auto-qa-dev-api-codegraph-eng.devfactory.com/stackbuilds/" + requestId + "/updates");
        stackBuildContent.setProperty("key", "automation-test/" + requestId);
        stackBuildContent.setProperty("repositoryUrl", "https://github.com/trilogy-group/aurea-java-brp-cs-ruletest.git?branch=master");
        stackBuildContent.setProperty("revision", "41afa4cec2cb2beadae41bc4a613882930011b5d");
        stackBuildContent.setProperty("s3AccessKey", "AKIAIVFPXDJXG77YNB3Q");
        stackBuildContent.setProperty("s3SecretKey", "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");
        stackBuildContent.setProperty("s3Url", "http://s3.amazonaws.com");
        return stackBuildContent;
    }

    public static String prepareDefinition(LayerDefinitionResponse layer1Data, LayerDefinitionResponse layer2Data, LayerDefinitionResponse layer3Data) {
        String definition = "schemaVersion: \"1.0\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:\n      - name: task-1\n" +
                "        version: 1\n        template: " + layer1Data.getName() + "\n        arguments:\n          parameters:\n" +
                "          - name: message\n            value: MyValue\n      - name: task-2\n        version: 1\n" +
                "        template: " + layer2Data.getName() + "\n        dependencies: [task-1]\n        arguments:\n" +
                "          parameters:\n          - name: message\n            value: MyValue\n      - name: task-3\n" +
                "        version: 1\n        template: " + layer3Data.getName() + "\n        arguments:\n          parameters:\n" +
                "          - name: failure-probability\n            value: \"0\"\n          - name: sleep-for-ms\n" +
                "            value: \"10\"\n          - name: codeGraphName\n            value: \"codegraphName\"\n" +
                "          - name: repoUrl\n            value: \"{{workflow.parameters.repoUrl}}\"\n" +
                "          - name: branch\n            value: \"{{workflow.parameters.branch}}\"\n" +
                "          - name: commit\n            value: \"{{workflow.parameters.commit}}\"";
        return definition;
    }

    public static String prepareDefinitionWithWrongLayers() {
        String definition = "schemaVersion: \"1.0\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:\n      - name: task-1\n" +
                "        version: 1\n        template: nonExistingLayerName\n        arguments:\n          parameters:\n" +
                "          - name: message\n            value: MyValue\n      - name: task-2\n        version: 1\n" +
                "        template: nonExistingLayerName\n        dependencies: [task-1]\n        arguments:\n" +
                "          parameters:\n          - name: message\n            value: MyValue\n      - name: task-3\n" +
                "        version: 1\n        template: nonExistingLayerName\n        arguments:\n          parameters:\n" +
                "          - name: failure-probability\n            value: \"0\"\n          - name: sleep-for-ms\n" +
                "            value: \"10\"\n          - name: codeGraphName\n            value: \"codegraphName\"\n" +
                "          - name: repoUrl\n            value: \"{{workflow.parameters.repoUrl}}\"\n" +
                "          - name: branch\n            value: \"{{workflow.parameters.branch}}\"\n" +
                "          - name: commit\n            value: \"{{workflow.parameters.commit}}\"";
        return definition;
    }

    public static String prepareDefinitionWithNonExisitngDependencies(LayerDefinitionResponse layer1Data, LayerDefinitionResponse layer2Data, LayerDefinitionResponse layer3Data) {
        String definition = "schemaVersion: \"1.0\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:\n      - name: task-1\n" +
                "        version: 1\n        template: " + layer1Data.getName() + "\n        dependencies: [task-2]\n        arguments:\n          parameters:\n" +
                "          - name: message\n            value: MyValue";
        return definition;
    }

    public static String prepareDefinitionWithCyclicDependencies(LayerDefinitionResponse layer1Data, LayerDefinitionResponse layer2Data, LayerDefinitionResponse layer3Data) {
        String definition = "schemaVersion: \"1.0\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:\n      - name: task-1\n" +
                "        version: 1\n        template: " + layer1Data.getName() + "\n        dependencies: [task-2]\n        arguments:\n          parameters:\n" +
                "          - name: message\n            value: MyValue\n      - name: task-2\n        version: 1\n" +
                "        template: " + layer2Data.getName() + "\n        dependencies: [task-3]\n        arguments:\n" +
                "          parameters:\n          - name: message\n            value: MyValue\n      - name: task-3\n" +
                "        version: 1\n        template: " + layer3Data.getName() + "\n        dependencies: [task-1]\n        arguments:\n          parameters:\n" +
                "          - name: failure-probability\n            value: \"0\"\n          - name: sleep-for-ms\n" +
                "            value: \"10\"\n          - name: codeGraphName\n            value: \"codegraphName\"\n" +
                "          - name: repoUrl\n            value: \"{{workflow.parameters.repoUrl}}\"\n" +
                "          - name: branch\n            value: \"{{workflow.parameters.branch}}\"\n" +
                "          - name: commit\n            value: \"{{workflow.parameters.commit}}\"";
        return definition;
    }

    public static String prepareDefinitionWithWrongSchema(LayerDefinitionResponse layer1Data,
                                                          LayerDefinitionResponse layer2Data, LayerDefinitionResponse layer3Data) {
        String definition = "schemaVersion: \"test\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:\n      - name: task-1\n" +
                "        version: 1\n        template: " + layer1Data.getName() + "\n        arguments:\n          parameters:\n" +
                "          - name: message\n            value: MyValue\n      - name: task-2\n        version: 1\n" +
                "        template: " + layer2Data.getName() + "\n        dependencies: [task-1]\n        arguments:\n" +
                "          parameters:\n          - name: message\n            value: MyValue\n      - name: task-3\n" +
                "        version: 1\n        template: " + layer3Data.getName() + "\n        arguments:\n          parameters:\n" +
                "          - name: failure-probability\n            value: \"0\"\n          - name: sleep-for-ms\n" +
                "            value: \"10\"\n          - name: codeGraphName\n            value: \"codegraphName\"\n" +
                "          - name: repoUrl\n            value: \"{{workflow.parameters.repoUrl}}\"\n" +
                "          - name: branch\n            value: \"{{workflow.parameters.branch}}\"\n" +
                "          - name: commit\n            value: \"{{workflow.parameters.commit}}\"";
        return definition;
    }

    public static String prepareLayerDefinition(String layerName) {
        String definition = "schemaVersion: '1.0'\ndaemon: false\nversion: 1"
                + "\nname: " + layerName + "\ninputs:\n parameters:\n - name: commit"
                + "\n - name: failure-probability\n - name: branch\n - name: sleep-for-ms"
                + "\n - name: repoUrl\n - name: codeGraphName";
        return definition;
    }

    public static String prepareSingleStackDefinition(String layerName) {
        String definition = "schemaVersion: \"1.0\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:\n      - name: task-1\n" +
                "        version: 1\n        template: " + layerName + "\n        arguments:\n          parameters:\n" +
                "          - name: failure-probability\n            value: \"0\"\n          - name: sleep-for-ms\n" +
                "            value: \"10\"\n          - name: codeGraphName\n            value: \"codegraphName\"\n" +
                "          - name: repoUrl\n            value: \"{{workflow.parameters.repoUrl}}\"\n" +
                "          - name: branch\n            value: \"{{workflow.parameters.branch}}\"\n" +
                "          - name: commit\n            value: \"{{workflow.parameters.commit}}\"";
        return definition;
    }

    public static String prepareMultiStackDefinition(LayerDefinitionResponse layer1Data, LayerDefinitionResponse layer2Data) {
        String definition = "schemaVersion: \"1.0\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:\n      - name: task-1\n" +
                "        version: 1\n        template: " + layer1Data.getName() + "\n        arguments:\n          parameters:\n" +
                "          - name: message\n            value: MyValue\n      - name: task-3\n" +
                "        version: 1\n        template: " + layer2Data.getName() + "\n        arguments:\n          parameters:\n" +
                "          - name: failure-probability\n            value: \"0\"\n          - name: sleep-for-ms\n" +
                "            value: \"10\"\n          - name: codeGraphName\n            value: \"codegraphName\"\n" +
                "          - name: repoUrl\n            value: \"https://github.com/trilogy-group/devfactory-codeserver-examplerepository\"\n" +
                "          - name: branch\n            value: \"master\"\n" +
                "          - name: commit\n            value: \"398657be250a25bcb1d9e45985ddf991febeeaa9\"";
        return definition;
    }

    public static String testValidation(String expectedValue, String actualValue) {
        return "Expected the value to be " + expectedValue + " but was " + actualValue + " in response";
    }

    public static String prepareDefinitionWithoutSchemaVersion(LayerDefinitionResponse layer1Data, LayerDefinitionResponse layer2Data, LayerDefinitionResponse layer3Data) {
        return "apiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:\n      - name: task-1\n" +
                "        version: 1\n        template: " + layer1Data.getName() + "\n        arguments:\n          parameters:\n" +
                "          - name: message\n            value: MyValue\n      - name: task-2\n        version: 1\n" +
                "        template: " + layer2Data.getName() + "\n        dependencies: [task-1]\n        arguments:\n" +
                "          parameters:\n          - name: message\n            value: MyValue\n      - name: task-3\n" +
                "        version: 1\n        template: " + layer3Data.getName() + "\n        arguments:\n          parameters:\n" +
                "          - name: failure-probability\n            value: \"0\"\n          - name: sleep-for-ms\n" +
                "            value: \"10\"\n          - name: codeGraphName\n            value: \"codegraphName\"\n" +
                "          - name: repoUrl\n            value: \"{{workflow.parameters.repoUrl}}\"\n" +
                "          - name: branch\n            value: \"{{workflow.parameters.branch}}\"\n" +
                "          - name: commit\n            value: \"{{workflow.parameters.commit}}\"";
    }

    public static String prepareDefinitionWithoutSpec() {
        return "schemaVersion: \"1.0\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nunknownElement: []";
    }

    public static String prepareDefinitionWithEmptyTask() {
        return "schemaVersion: \"1.0\"\napiVersion: graphbuilder.df.com/v1alpha1\nkind: Stack\nmetadata:\n " +
                " name: MyStackName\nstackInputs:\n  parameters:\n  - name: repoUrl\n  - name: commit\n" +
                "  - name: branch\n  - name: message\nspec:\n  entrypoint: the-dag\n  arguments:\n    parameters:\n" +
                "    - name: description\n      value: This is a description\n    - name: repoUrl\n" +
                "      value: MyValue\n    - name: branch\n      value: MyValue\n    - name: commit\n" +
                "      value: MyValue\n  templates:\n  - name: the-dag\n    dag:\n      tasks:[]";
    }
}
