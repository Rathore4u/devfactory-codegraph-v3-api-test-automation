package com.codegraph.v3.restapi.automation.models.config;

import lombok.Getter;

@Getter
public class AppConfig {
    public ApiToken apiToken;
    public S3Config s3Config;

    public String getToken() {
        return apiToken.getToken();
    }

    public S3Config getS3() {
        return s3Config;
    }
}

