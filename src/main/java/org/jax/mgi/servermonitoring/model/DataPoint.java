package org.jax.mgi.servermonitoring.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@ApiModel
@Table(indexes = {@Index(name="datapont_dataproperty_id_index", columnList = "dataproperty_id"), @Index(name="datapont_dataname_id_index", columnList="dataname_id"), @Index(name="datapont_datatype_id_index", columnList="datatype_id"), @Index(name="datapont_servername_id_index", columnList="servername_id")})
public class DataPoint implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private ServerName serverName;
	@ManyToOne
	private DataType dataType;
	@ManyToOne
	private DataName dataName;
	@ManyToOne
	private DataProperty dataProperty;

	@NotNull
	@NotEmpty
	private String dataValue;
	private Date dataTimeStamp;

	public DataPoint() { }

	public DataPoint(ServerName serverName, DataType dataType, DataName dataName, DataProperty dataProperty, String dataValue, Date dataTimeStamp) {
		this.serverName = serverName;
		this.dataType = dataType;
		this.dataName = dataName;
		this.dataProperty = dataProperty;
		this.dataValue = dataValue;
		this.dataTimeStamp = dataTimeStamp;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public DataProperty getDataProperty() {
		return dataProperty;
	}
	public void setDataProperty(DataProperty dataProperty) {
		this.dataProperty = dataProperty;
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
