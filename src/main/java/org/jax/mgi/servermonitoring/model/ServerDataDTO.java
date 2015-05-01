package org.jax.mgi.servermonitoring.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@XmlRootElement
@ApiModel
public class ServerDataDTO implements Serializable {
	private Long id;
    
	private String serverName;
	private String dataType;
	private String dataName;
	private String dataValue;
	
	public ServerDataDTO() { }

    public ServerDataDTO(ServerData data) {
    	this.id = data.getId();
		this.serverName = data.getServerName().getName();
		this.dataType = data.getDataType().getType();
		this.dataName = data.getDataName().getName();
		this.dataValue = data.getDataValue();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
}
