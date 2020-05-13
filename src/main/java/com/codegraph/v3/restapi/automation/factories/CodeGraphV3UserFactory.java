package com.codegraph.v3.restapi.automation.factories;

import com.codegraph.v3.restapi.automation.types.UserImpl;
import com.xo.restapi.automation.factories.UserFactory;
import com.xo.restapi.automation.types.User;

public class CodeGraphV3UserFactory implements UserFactory {

    @Override
    public User getUser(String username, String token) {
        return new UserImpl(token);
    }
}
