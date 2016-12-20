package com.vela.iot.active.dropwizard;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/active")
@Produces(MediaType.APPLICATION_JSON)
public class ActiveResource {
	private final String template;
	private final String defaultName;
	private final AtomicLong counter;
	
	public ActiveResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }
	
	@GET
	@Timed
	//TODO 如果有多个方法，这里该怎么约定路径？
	public Saying sayHello(@QueryParam("name") Optional<String> name){
		final String value = String.format(template, name.or(defaultName));
		return new Saying(counter.incrementAndGet(),value);
	}
}
