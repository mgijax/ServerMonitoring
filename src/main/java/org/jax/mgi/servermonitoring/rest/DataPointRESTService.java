package org.jax.mgi.servermonitoring.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jax.mgi.servermonitoring.model.DataPointDTO;
import org.jax.mgi.servermonitoring.service.DataPointService;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/datapoint")
@Api(value = "/datapoint", description = "Data Point REST Service: Server Data Point Service is used to input data points collected from each server that runs the Watch Dog python software")
@RequestScoped
public class DataPointRESTService {

	@Inject
	private Validator validator;
	
	@Inject
	private DataPointService serverDataManager;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create Data Point: Creates a Data Point Entry for the specific collection of data")
	public Response createServerData(@ApiParam(value = "New member to create", required = true) DataPointDTO data) {
				
		Response.ResponseBuilder builder = null;
		
		try {
			// Validates member using bean validation
			validateData(data);
			
			serverDataManager.createEntry(data);

			builder = Response.ok();
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		return builder.build();
	}
	
	@GET
	@ApiOperation(value = "List Data Points: Gets a list of all the Data Point Entries in the system", notes="These are the notes", response=DataPointDTO.class, responseContainer="List")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DataPointDTO> listData() {
		return serverDataManager.listData();
	}
	
	private void validateData(DataPointDTO data) throws ConstraintViolationException, ValidationException {
		Set<ConstraintViolation<DataPointDTO>> violations = validator.validate(data);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

}