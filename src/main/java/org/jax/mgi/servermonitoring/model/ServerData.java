package org.jax.mgi.servermonitoring.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@ApiModel
public class ServerData implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
	private ServerName serverName;
	private DataType dataType;
	private DataName dataName;

	@NotNull
	@NotEmpty
	private String dataValue;
	private Date dataTimeStamp;

	public ServerName getServerName() {
		return serverName;
	}
	public void setServerName(ServerName serverName) {
		this.serverName = serverName;
	}
	public DataType getDataType() {
		return dataType;
	}
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	public DataName getDataName() {
		return dataName;
	}
	public void setDataName(DataName dataName) {
		this.dataName = dataName;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public Date getDataTimeStamp() {
		return dataTimeStamp;
	}
	public void setDataTimeStamp(Date dataTimeStamp) {
		this.dataTimeStamp = dataTimeStamp;
	}
}
