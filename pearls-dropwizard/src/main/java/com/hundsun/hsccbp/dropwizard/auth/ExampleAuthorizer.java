package com.hundsun.hsccbp.dropwizard.auth;

import io.dropwizard.auth.Authorizer;

import com.hundsun.hsccbp.dropwizard.core.User;

public class ExampleAuthorizer implements Authorizer<User> {

    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles() != null && user.getRoles().contains(role);
    }
}
