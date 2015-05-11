package org.jax.mgi.servermonitoring.controller;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jax.mgi.servermonitoring.model.ServerName;
import org.jax.mgi.servermonitoring.service.DataPointService;

@Model
public class DataPointController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private DataPointService dataPointService;
    
    @Produces
    @Named
    private ServerName servers;

}
