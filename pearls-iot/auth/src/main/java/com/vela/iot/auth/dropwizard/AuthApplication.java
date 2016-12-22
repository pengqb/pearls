package com.vela.iot.auth.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import com.vela.iot.auth.dropwizard.gw.ActiveResource;

public class AuthApplication extends Application<AuthConfiguration> {

	public static void main(String[] args) throws Exception {
		new AuthApplication().run(args);
	}

	@Override
	public String getName() {
		return "active";
	}

	@Override
	public void run(AuthConfiguration configuration,
			Environment environment) throws Exception {
		final ActiveResource resource = new ActiveResource();		
		environment.jersey().register(resource);
	}

}
