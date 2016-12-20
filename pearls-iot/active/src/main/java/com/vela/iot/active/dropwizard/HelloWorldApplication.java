package com.vela.iot.active.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class HelloWorldApplication extends Application<ActiveConfiguration> {

	public static void main(String[] args) throws Exception {
		new HelloWorldApplication().run(args);
	}

	@Override
	public String getName() {
		return "active";
	}

	@Override
	public void run(ActiveConfiguration configuration,
			Environment environment) throws Exception {
		final ActiveResource resource = new ActiveResource(
				configuration.getTemplate(), configuration.getDefaultName());
		final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
				configuration.getTemplate());
		environment.healthChecks().register("template", healthCheck);
		environment.jersey().register(resource);
	}

}
