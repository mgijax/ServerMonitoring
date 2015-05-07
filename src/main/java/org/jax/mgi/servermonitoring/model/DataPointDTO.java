package org.jax.mgi.servermonitoring.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@XmlRootElement
@ApiModel
public class DataPointDTO implements Serializable {
    
	private String serverName;
	private String dataType;
	private String dataName;
	private String dataProperty;
	private String dataValue;
	
	public DataPointDTO() { }

    public DataPointDTO(DataPoint data) {
		this.serverName = data.getServerName().getName();
		this.dataType = data.getDataType().getType();
		this.dataName = data.getDataName().getName();
		this.dataProperty = data.getDataProperty().getProperty();
		this.dataValue = data.getDataValue();
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
	public String getDataProperty() {
		return dataProperty;
	}
	public void setDataProperty(String dataProperty) {
		this.dataProperty = dataProperty;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
}
