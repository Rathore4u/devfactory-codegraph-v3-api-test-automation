package com.codegraph.v3.restapi.automation.e2e.commonsteps;

import com.codegraph.v3.restapi.automation.data.ConstantsUtils;
import com.codegraph.v3.restapi.automation.utilities.UsersConfig;
import com.xo.restapi.automation.actions.UserActionUtils;
import com.xo.restapi.automation.configs.BaseUrlUtil;
import com.xo.restapi.automation.configs.RestAssuredConfigUtils;
import com.xo.restapi.automation.context.UserContext;
import com.xo.restapi.automation.context.UserData;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import lombok.Getter;

public class LoginStepdefs {

    @Getter
    private UserData userData = new UserData();

    private static final String DEFAULT = "default";

    @Given("^User Login and Receive API access token in (.*) environment$")
    public void accountManagerLogsInToSystem(String environment) {
        userLogsIntoEnv(environment);
    }

    @Given("^User Login and Receive API access token$")
    public void loggedIntoTheSystem() {
        userLogsIntoEnv(BaseUrlUtil.getCurrentEnvironment());
    }

    private void userLogsIntoEnv(String environment) {
        RestAssuredConfigUtils.setBaseUri(environment);
        String token = UsersConfig.getInstance().getApiToken();
        userData.put("token", token);
        userData.put("environment", environment);
        UserContext.setUserData(userData);
        UserActionUtils.perform("token", UserContext::getToken);
    }

    @When("^Invalid token (.*) is activated")
    public void switchToInvalidToken(String invalidToken) {
        userData.remove("token");
        userData.put("token", invalidToken);
        UserContext.setUserData(userData);
        UserActionUtils.perform("token", UserContext::getUser);
    }

    @When("^Blank Token in activated")
    public void switchToBlankToken() {
        userData.remove("token");
        userData.put("token", "");
        UserContext.setUserData(userData);
        UserActionUtils.perform("token", UserContext::getUser);
    }

    @When("^Token is removed")
    public void switchToRemovedToken() {
        userData.remove("token");
        UserContext.setUserData(userData);
        UserActionUtils.perform("token", UserContext::getUser);
    }

    @When("^Expired Token in activated")
    public void switchToExpiredToken() {
        userData.remove("token");
        userData.put("token", ConstantsUtils.EXPIRED_TOKEN);
        UserContext.setUserData(userData);
        UserActionUtils.perform("token", UserContext::getUser);
    }

    @When("^Wrong Token in activated")
    public void switchToWrongToken() {
        userData.remove("token");
        userData.put("token", ConstantsUtils.WRONG_TOKEN);
        UserContext.setUserData(userData);
        UserActionUtils.perform("token", UserContext::getUser);
    }
}
