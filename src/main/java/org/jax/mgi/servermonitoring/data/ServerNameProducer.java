package org.jax.mgi.servermonitoring.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
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
	private DataPointService dataPointService;

	private List<ServerName> servers;
	
	private HashMap<String, List<DataType>> typeMap = new HashMap<String, List<DataType>>();
	private HashMap<String, List<DataName>> nameMap = new HashMap<String, List<DataName>>();
	private HashMap<String, List<DataProperty>> propertyMap = new HashMap<String, List<DataProperty>>();
	
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
		servers = dataPointService.getServerList();
	}

	@Produces
	@Named
	public List<DataType> getDataTypes() {
		//List<DataType> types = getDataTypes(selectedServername);
		//if(types.isEmpty()) {
			List<DataType> dataTypes = dataPointService.getDataTypes(selectedServername);
		//	types.addAll(dataTypes);
			return dataTypes;
		//}
		//return types;
	}

	@Produces
	@Named
	public List<DataName> getDataNames(DataType dataType) {
		//List<DataName> names = getDataNames(selectedServername, dataType);
		//if(names.isEmpty()) {
			List<DataName> dataNames = dataPointService.getDataNames(selectedServername, dataType);
		//	names.addAll(dataNames);
			return dataNames;
		//}
		//return names;
	}

	@Produces
	@Named
	public List<DataProperty> getDataProperties(DataType dataType, DataName dataName) {
		//List<DataProperty> properties = getDataProperties(selectedServername, dataType, dataName);
		//if(properties.isEmpty()) {
			List<DataProperty> dataProperties = dataPointService.getDataProperties(selectedServername, dataType, dataName);
		//	properties.addAll(dataProperties);
			return dataProperties;
		//}
		//return properties;
	}
	
	
	private List<DataProperty> getDataProperties(ServerName selectedServername, DataType dataType, DataName dataName) {
		if(!propertyMap.containsKey(selectedServername.getName() + "::" + dataType.getType() + "::" + dataName.getName())) {
			propertyMap.put(selectedServername.getName() + "::" + dataType.getType() + "::" + dataName.getName(), new ArrayList<DataProperty>());
		}
		return propertyMap.get(selectedServername.getName() + "::" + dataType.getType() + "::" + dataName.getName());
	}

	private List<DataName> getDataNames(ServerName selectedServername, DataType dataType) {
		if(!nameMap.containsKey(selectedServername.getName() + "::" + dataType.getType())) {
			nameMap.put(selectedServername.getName() + "::" + dataType.getType(), new ArrayList<DataName>());
		}
		return nameMap.get(selectedServername.getName() + "::" + dataType.getType());
	}
	
	private List<DataType> getDataTypes(ServerName selectedServername) {
		if(!typeMap.containsKey(selectedServername.getName())) {
			typeMap.put(selectedServername.getName(), new ArrayList<DataType>());
		}
		return typeMap.get(selectedServername.getName());
	}

}
