package org.jax.mgi.servermonitoring.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import javax.ws.rs.core.Response;

import org.jax.mgi.servermonitoring.model.ServerDataDTO;
import org.jax.mgi.servermonitoring.service.ServerDataBean;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/serverdata")
@Api(value = "/serverdata", description = "ServerDataRESTService: Server Data Service")
@RequestScoped
public class ServerDataRESTService {

	@Inject
	private Validator validator;
	
	@Inject
	private ServerDataBean serverDataManager;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "createServerData: Creates a Server Data Entry")
	public Response createServerData(@ApiParam(value = "New member to create", required = true) ServerDataDTO data) {
				
		Response.ResponseBuilder builder = null;
		
		try {
			// Validates member using bean validation
			validateData(data);
			
			serverDataManager.createEntry(data);

			builder = Response.ok();
		} catch (ConstraintViolationException ce) {
			// Handle bean validation issues
			builder = createViolationResponse(ce.getConstraintViolations());
		} catch (ValidationException e) {
			// Handle the unique constrain violation
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("email", "Email taken");
			builder = Response.status(Response.Status.CONFLICT).entity(responseObj);
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		return builder.build();
	}
	
	private void validateData(ServerDataDTO data) throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<ServerDataDTO>> violations = validator.validate(data);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}
	
	private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {

		Map<String, String> responseObj = new HashMap<>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}
}