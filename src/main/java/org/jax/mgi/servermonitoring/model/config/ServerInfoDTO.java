package org.jax.mgi.servermonitoring.model.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@XmlRootElement
@ApiModel
public class ServerInfoDTO implements Serializable {
	
	private String clientName;
	private String clientArch;
	private List<ServerInfoTypeDTO> types = new ArrayList<ServerInfoTypeDTO>();
	
	public ServerInfoDTO() { }
	
	public ServerInfoDTO(ServerConfig serverConfig) {
		this.clientName = serverConfig.getClientName();
		this.clientArch = serverConfig.getClientArch();

		for(ServerConfigType t: serverConfig.getTypes()) {
			types.add(new ServerInfoTypeDTO(t));
		}
	}
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientArch() {
		return clientArch;
	}
	public void setClientArch(String clientArch) {
		this.clientArch = clientArch;
	}
	public List<ServerInfoTypeDTO> getTypes() {
		return types;
	}
	public void setTypes(List<ServerInfoTypeDTO> types) {
		this.types = types;
	}

}
