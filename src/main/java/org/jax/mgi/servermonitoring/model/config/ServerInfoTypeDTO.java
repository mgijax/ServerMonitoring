package org.jax.mgi.servermonitoring.model.config;

import java.util.ArrayList;
import java.util.List;

public class ServerInfoTypeDTO {

	private String type;
	private List<ServerInfoNameDTO> names = new ArrayList<ServerInfoNameDTO>();
	
	public ServerInfoTypeDTO(ServerConfigType type) {
		this.type = type.getType();
		for(ServerConfigName name: type.getNames()) {
			this.names.add(new ServerInfoNameDTO(name));
		}
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<ServerInfoNameDTO> getNames() {
		return names;
	}
	public void setNames(List<ServerInfoNameDTO> names) {
		this.names = names;
	}

}
