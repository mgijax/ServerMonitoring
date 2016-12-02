package org.jax.mgi.servermonitoring.model.config;

public class ServerInfoPropertyDTO {

	private String property;
	
	public ServerInfoPropertyDTO(ServerConfigProperty property) {
		this.property = property.getProperty();
	}

	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
}
