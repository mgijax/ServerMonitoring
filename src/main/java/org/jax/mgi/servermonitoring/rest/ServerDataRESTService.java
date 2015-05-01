package org.jax.mgi.servermonitoring.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jax.mgi.servermonitoring.model.ServerDataDTO;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/serverdata")
@Api(value = "/serverdata", description = "ServerDataRESTService: Server Data Service")
@RequestScoped
public class ServerDataRESTService {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "createServerData: Creates a Server Data Entry")
	public Response createServerData(@ApiParam(value = "New member to create", required = true) ServerDataDTO data) {
		Response.ResponseBuilder builder = null;
		
		return builder.build();
	}
}