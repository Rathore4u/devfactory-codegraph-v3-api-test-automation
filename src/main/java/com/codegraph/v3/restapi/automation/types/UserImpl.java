package com.codegraph.v3.restapi.automation.types;

import com.xo.restapi.automation.types.User;

public class UserImpl extends User {

    public UserImpl(String token) {

        setToken(token);
    }
}
