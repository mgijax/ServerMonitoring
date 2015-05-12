package org.jax.mgi.servermonitoring.data;

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
    	return dataPointService.getDataTypes(selectedServername);
    }
    
    @Produces
    @Named
    public List<DataName> getDataNames(DataType dataType) {
    	return dataPointService.getDataNames(selectedServername, dataType);
    }
    
    @Produces
    @Named
    public List<DataProperty> getDataProperties(DataType dataType, DataName dataName) {
    	return dataPointService.getDataProperties(selectedServername, dataType, dataName);
    }
    
    
}
