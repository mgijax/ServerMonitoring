package org.jax.mgi.servermonitoring.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jax.mgi.servermonitoring.model.ServerName;
import org.jax.mgi.servermonitoring.service.DataPointService;

@RequestScoped
public class ServerNameProducer {

    @Inject
    private DataPointService dataPointService;

    private List<ServerName> servers;
    
    @Produces
    @Named
    public List<ServerName> getServers() {
    	return servers;
    }
    
    @PostConstruct
    public void retrieveAllMembersOrderedByName() {
    	servers = dataPointService.getServerList();
    }
}
