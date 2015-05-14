package org.jax.mgi.servermonitoring.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@ApiModel
@Table(indexes = {
		@Index(name="datasensor_full_index", columnList = "dataproperty_id,dataname_id,datatype_id,servername_id")
})
public class DataSensor implements Serializable {
	
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
	
	@OneToMany(mappedBy="dataSensor")
	private List<DataPoint> dataPoints;
	
	public DataSensor() { }
	
	public DataSensor(ServerName serverName, DataType dataType, DataName dataName, DataProperty dataProperty) {
		this.serverName = serverName;
		this.dataType = dataType;
		this.dataName = dataName;
		this.dataProperty = dataProperty;
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
	
}
