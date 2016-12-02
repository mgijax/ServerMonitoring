package org.jax.mgi.servermonitoring.rest;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.servermonitoring.model.config.ServerInfoDTO;
import org.jax.mgi.servermonitoring.service.ServerInfoService;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/registerserver")
@Api(value = "/registerserver", description = "This REST Service Endpoint is for creating and listing Data Points. Posting to the Data Point Endpoint is used "
		+ "to input data points collected from each server that runs the Watch Dog client software. Getting from the Data Point Endpoint will give a list "
		+ "of data points based on the paramaters provided.")
@RequestScoped
public class ServerInfoRESTService {

	@Inject
	private Logger log;
	
	@Inject
	private Validator validator;
	
	@Inject
	private ServerInfoService serverInfoService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates new server", notes="Notes")
	public ServerInfoDTO registerServer(@ApiParam(value = "New Data Point to create", required = true, name="serverInfo") ServerInfoDTO data) {
		validateData(data);
		return serverInfoService.registerServer(data);
	}
	
	private void validateData(ServerInfoDTO data) throws ConstraintViolationException, ValidationException {
		Set<ConstraintViolation<ServerInfoDTO>> violations = validator.validate(data);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}
}