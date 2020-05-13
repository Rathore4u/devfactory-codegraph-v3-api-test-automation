package com.codegraph.v3.restapi.automation.e2e.commonsteps;

import com.codegraph.v3.restapi.automation.factories.CodeGraphV3UserFactory;
import com.xo.restapi.automation.context.UserContext;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

@SuppressWarnings({"PMD.ClassNamingConventions", "PMD.UseUtilityClass"})
public final class Hooks {
    @Before
    public static void beforeScenario() {
        UserContext.setFactory(new CodeGraphV3UserFactory());
    }

    @Given("We are running E2E ([0-9a-zA-Z-]+)")
    public void startFeature(String id) {
        FeatureRegistry.startFeature(id);
    }
}
