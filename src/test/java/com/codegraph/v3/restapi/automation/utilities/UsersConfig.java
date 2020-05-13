package com.codegraph.v3.restapi.automation.utilities;

import com.codegraph.v3.restapi.automation.models.config.AppConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

import static org.apache.commons.lang.StringUtils.EMPTY;

public final class UsersConfig {

    private static final String CONFIG_YML = "config.yml";
    private static final String DASH = "-";
    private static final String DEFAULT = "default";
    private static final String REST_API_DEFAULT_ENV = "REST_API_DEFAULT_ENV";
    private transient AppConfig appConfig;
    private transient String filename;
    private static UsersConfig instance;

    private UsersConfig() {
    }

    private boolean fileStartsWith(final String env) {
        return filename.startsWith(env);
    }

    static void init(final String environment) {
        if (instance != null && instance.fileStartsWith(environment)) {
            return;
        }
        instance = new UsersConfig();
        updateFileName(environment);

        loadFromYaml();
    }

    private static void loadFromYaml() {
        final InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(instance.filename);
        Yaml yaml = new Yaml();
        instance.appConfig = yaml.loadAs(inputStream, AppConfig.class);
    }

    private static void updateFileName(String environment) {
        instance.filename = EMPTY;
        String defaultEnv = System.getenv(REST_API_DEFAULT_ENV);
        String selectedEnvironment = "";
        if (null != defaultEnv && !defaultEnv.isEmpty()
                && (environment.isEmpty() || environment.equalsIgnoreCase(DEFAULT))) {
            selectedEnvironment = defaultEnv;
        } else {
            selectedEnvironment = environment;
        }
        if (selectedEnvironment.isEmpty() || selectedEnvironment.equalsIgnoreCase(DEFAULT)) {
            instance.filename = CONFIG_YML;
        } else {
            instance.filename = selectedEnvironment + DASH + CONFIG_YML;
        }
    }

    public static UsersConfig getInstance() {
        if (null == instance) {
            init(EMPTY);
        }
        return instance;
    }

    public String getApiToken() {
        return appConfig.getToken();
    }

    public String getS3Key() {
        return appConfig.s3Config.getKey();
    }

    public String getS3Secret() {
        return appConfig.s3Config.getSecret();
    }

    public String getS3LayersBucket() {
        return appConfig.s3Config.getLayersBucket();
    }

    public String getS3TempBucket() {
        return appConfig.s3Config.getTmpBucket();
    }

    public String getRegion() {
        return appConfig.s3Config.getRegion();
    }
}
