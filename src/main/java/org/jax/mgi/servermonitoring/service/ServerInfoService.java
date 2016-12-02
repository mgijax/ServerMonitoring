package org.jax.mgi.servermonitoring.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jax.mgi.servermonitoring.model.ServerName;
import org.jax.mgi.servermonitoring.model.config.ServerInfoDTO;

@Stateless
public class ServerInfoService {

	@Inject
	private DataPointService dataPointService;

	public ServerInfoDTO registerServer(ServerInfoDTO data) {
		ServerName serverName = dataPointService.getServerName(data, true);
		return new ServerInfoDTO(serverName.getServerConfig());
	}

}
