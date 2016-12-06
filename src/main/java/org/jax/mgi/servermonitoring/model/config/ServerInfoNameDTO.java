package org.jax.mgi.servermonitoring.model.config;

import java.util.ArrayList;
import java.util.List;

public class ServerInfoNameDTO {

	private String name;
	private int frequency;
	
	private List<ServerInfoPropertyDTO> properties = new ArrayList<ServerInfoPropertyDTO>();

	public ServerInfoNameDTO(ServerConfigName name) {
		this.name = name.getName();
		this.frequency = name.getFrequency();
		for(ServerConfigProperty p: name.getProperties()) {
			properties.add(new ServerInfoPropertyDTO(p));
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public List<ServerInfoPropertyDTO> getProperties() {
		return properties;
	}
	public void setProperties(List<ServerInfoPropertyDTO> properties) {
		this.properties = properties;
	}
}
