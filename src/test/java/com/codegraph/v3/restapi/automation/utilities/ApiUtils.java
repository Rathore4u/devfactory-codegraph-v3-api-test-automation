package com.codegraph.v3.restapi.automation.utilities;

import com.aurea.automation.codegraph.oa3.ApiClient;
import com.xo.restapi.automation.context.UserContext;
import io.restassured.authentication.PreemptiveOAuth2HeaderScheme;
import io.restassured.builder.RequestSpecBuilder;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

import static com.aurea.automation.codegraph.oa3.GsonObjectMapper.gson;
import static com.xo.restapi.automation.configs.BaseUrlUtil.getBaseUrl;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static io.restassured.config.RestAssuredConfig.config;

@UtilityClass
public class ApiUtils {
    public static ApiClient apiClient() {
        return ApiClient.api(
                ApiClient.Config.apiConfig()
                        .reqSpecSupplier(DEFAULT_SUPPLIER));
    }

    /**
     * Provides the authenticated supplier.
     */
    protected static Supplier<RequestSpecBuilder> DEFAULT_SUPPLIER = () -> {
        PreemptiveOAuth2HeaderScheme authScheme = new PreemptiveOAuth2HeaderScheme();
        authScheme.setAccessToken(UserContext.getToken());

        return new RequestSpecBuilder()
                .setBaseUri(getBaseUrl())
                .setConfig(config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(gson())))
                .setAuth(authScheme);
    };

}
