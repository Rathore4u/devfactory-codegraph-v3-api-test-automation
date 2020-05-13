package com.codegraph.v3.restapi.automation.e2e.commonsteps;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * FeatureRegistery
 * <p>
 * Used to keep state between scenarios of the same feature.
 * <p>
 * NOTE: Be aware that sharing state between scenarios isn't something that
 * should be done but nevertheless we will do it because it maps seamless to the
 * E2E structure we have in place now.
 */
@RequiredArgsConstructor
public class FeatureRegistry {

    @Getter
    private static FeatureRegistry currentFeature;

    @Getter
    private final String id;

    private final Map<Class, Map<String, ?>> data = new HashMap<>();

    public <T> T getData(Class<T> clazz, String name) {
        Map<String, ?> caseData = data.computeIfAbsent(clazz, str -> new HashMap<>());
        return (T) caseData.get(name);
    }

    public <T> void setData(Class<T> clazz, String name, T value) {
        Map<String, T> caseData = (Map<String, T>) data.computeIfAbsent(clazz, str -> new HashMap<>());
        caseData.put(name, value);
    }

    /**
     * Start a new feature with that id
     */
    public static void startFeature(String id) {
        if (currentFeature == null || !id.equals(currentFeature.getId())) {
            currentFeature = new FeatureRegistry(id);
        }
    }

}
