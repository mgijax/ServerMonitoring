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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jax.mgi.servermonitoring.model.DataPointDTO;
import org.jax.mgi.servermonitoring.service.DataPointService;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/datapoint")
@Api(value = "/datapoint", description = "This REST Service Endpoint is for creating and listing Data Points. Posting to the Data Point Endpoint is used "
		+ "to input data points collected from each server that runs the Watch Dog client software. Getting from the Data Point Endpoint will give a list "
		+ "of data points based on the paramaters provided.")
@RequestScoped
public class DataPointRESTService {

	@Inject
	private Validator validator;
	
	@Inject
	private DataPointService dataPointManager;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create Data Point: Creates a Data Point Entry from the Data Point JSon model", notes="This takes in a new data point and if any of the values in the data point do not exist "
			+ "it will create them before saving the data point, this way new monitors can be developed with out the need to modify the server code. Data Time stamp is not required as the server will "
			+ "overwrite it with the time the server recieved the data point.")
	public Response createDataPoint(@ApiParam(value = "New Data Point to create", required = true, name="dataPoint") DataPointDTO data) {
				
		Response.ResponseBuilder builder = null;
		
		try {
			// Validates member using bean validation
			validateData(data);
			
			dataPointManager.createDataPoint(data);

			builder = Response.ok();
		} catch (Exception e) {
			// Handle generic exceptions
			Map<String, String> responseObj = new HashMap<>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}
		return builder.build();
	}
	
//	  "serverName": "string",
//	  "dataType": "string",
//	  "dataName": "string",
//	  "dataProperty": "string",
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "List Data Points: Gets a list of all the Data Point Entries in the system", notes="Each Paramater filters the list of the data points if not paramaters are provided then the "
			+ "system will just get the last amount of data points that it has recieved. If amount is not provided then its default is set to 720. This way the system does not go down due to too much "
			+ "data.", response=DataPointDTO.class, responseContainer="List")
	public List<DataPointDTO> listDataPoints(
			@ApiParam(value = "Server Name") @QueryParam(value = "serverName") String serverName,
			@ApiParam(value = "Data Type") @QueryParam(value = "dataType") String dataType,
			@ApiParam(value = "Data Name") @QueryParam(value = "dataName") String dataName,
			@ApiParam(value = "Data Property") @QueryParam(value = "dataProperty") String dataProperty,
			@ApiParam(value="Amount to return", defaultValue="720") @QueryParam(value = "amount") int amount) {
		if(amount == 0) amount = 720;
		DataPointDTO dto = new DataPointDTO(serverName, dataType, dataName, dataProperty);
		return dataPointManager.listDataPoints(dto, amount);
	}
	
	private void validateData(DataPointDTO data) throws ConstraintViolationException, ValidationException {
		Set<ConstraintViolation<DataPointDTO>> violations = validator.validate(data);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

}