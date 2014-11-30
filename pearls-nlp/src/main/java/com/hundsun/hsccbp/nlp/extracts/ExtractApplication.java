package com.hundsun.hsccbp.nlp.extracts;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class ExtractApplication extends Application<ExtractConfig> {

	/**
	 * @param args
	 */
	public static void main(final String[] args) throws Exception {
		new ExtractApplication().run(args);
	}

	@Override
	public String getName() {
		return "nlp-extract";
	}

	@Override
	public void run(final ExtractConfig configuration, final Environment environment)
			throws Exception {
		final ExtractResource resource = new ExtractResource(configuration);
		environment.jersey().register(resource);
	}

}
