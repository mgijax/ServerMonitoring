package org.jax.mgi.servermonitoring.data;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jax.mgi.servermonitoring.model.DataName;
import org.jax.mgi.servermonitoring.model.DataProperty;
import org.jax.mgi.servermonitoring.model.DataType;
import org.jax.mgi.servermonitoring.model.ServerName;
import org.jax.mgi.servermonitoring.service.DataPointService;

@Named
@RequestScoped
public class ServerNameProducer {

	@Inject
	private FacesContext facesContext;

	@Inject
	private DataPointService dataPointService;

	private List<ServerName> servers;

	private HashMap<String, List<DataType>> dataTypeMap = new HashMap<String, List<DataType>>();
	private HashMap<String, List<DataName>> dataNameMap = new HashMap<String, List<DataName>>();
	private HashMap<String, List<DataProperty>> dataPropMap = new HashMap<String, List<DataProperty>>();

	@Produces
	@Named
	private ServerName selectedServername;

	@Produces
	@Named
	public List<ServerName> getServers() {
		return servers;
	}

	@Named
	public String goToServer(ServerName server) {
		selectedServername = server;
		return "server";
	}

	@PostConstruct
	public void getServerList() {
		String serverName = getParam("serverName");
		
		System.out.println("Post Construct: " + serverName);
		if(serverName != null && !serverName.equals("")) {
			selectedServername = dataPointService.getServer(serverName);
		}

		servers = dataPointService.getServerList();
	}

	private String getParam(String string) {
		return (String) facesContext.getExternalContext().getRequestParameterMap().get(string);
	}

	@Produces
	@Named
	public List<DataType> getDataTypes() {
		if(!dataTypeMap.containsKey(selectedServername.getName())) {
			dataTypeMap.put(selectedServername.getName(), dataPointService.getDataTypes(selectedServername));
		}
		return dataTypeMap.get(selectedServername.getName());
	}

	@Produces
	@Named
	public List<DataName> getDataNames(DataType dataType) {
		if(!dataNameMap.containsKey(selectedServername.getName() + "::" + dataType.getType())) {
			dataNameMap.put(selectedServername.getName() + "::" + dataType.getType(), dataPointService.getDataNames(selectedServername, dataType));
		}
		return dataNameMap.get(selectedServername.getName() + "::" + dataType.getType());
	}

	@Produces
	@Named
	public List<DataProperty> getDataProperties(DataType dataType, DataName dataName) {
		if(!dataPropMap.containsKey(selectedServername.getName() + "::" + dataType.getType() + "::" + dataName.getName())) {
			dataPropMap.put(selectedServername.getName() + "::" + dataType.getType() + "::" + dataName.getName(), dataPointService.getDataProperties(selectedServername, dataType, dataName));
		}
		return dataPropMap.get(selectedServername.getName() + "::" + dataType.getType() + "::" + dataName.getName());
	}
}
