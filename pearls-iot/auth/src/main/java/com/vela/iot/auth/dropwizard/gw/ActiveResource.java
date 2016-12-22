package com.vela.iot.auth.dropwizard.gw;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

@Path("/gw")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActiveResource {

	public ActiveResource() {
	}

	// @GET
	// @Timed
	// @Path("/active")
	// public ActiveResult activeAction(
	// @QueryParam("devSn") Optional<String> devSn,
	// @QueryParam("devKey") Optional<String> devKey) {
	// ActiveResult result = new ActiveResult();
	// result.setPrimaryToken("werwerwerwerwerwerwerw");
	// result.setAccessToken("werwerwerwerwerwerwerw");
	// return result;
	// }

	@POST
	@Timed
	@Path("/active")
	public ActiveResult activeAction(
			@PathParam("devSn") Optional<String> devSn,
			@PathParam("devKey") Optional<String> devKey) {
		ActiveResult result = new ActiveResult();
		result.setPrimaryToken("werwerwerwerwerwerwerw");
		result.setAccessToken("werwerwerwerwerwerwerw");
		return result;
	}
}
