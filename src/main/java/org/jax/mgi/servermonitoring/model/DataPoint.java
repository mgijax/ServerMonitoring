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
@Table(indexes = {
		@Index(name="datapont_datasensor_id_index", columnList="datasensor_id"),
		@Index(name="datapont_datatimestamp_index", columnList="datatimestamp"),
})
public class DataPoint implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private DataSensor dataSensor;

	@NotNull
	@NotEmpty
	private String dataValue;
	private Date dataTimeStamp;

	public DataPoint() { }
	
	public DataPoint(DataSensor dataSensor, String dataValue, Date dataTimeStamp) {
		this.dataSensor = dataSensor;
		this.dataValue = dataValue;
		this.dataTimeStamp = dataTimeStamp;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public DataSensor getDataSensor() {
		return dataSensor;
	}
	public void setDataSensor(DataSensor dataSensor) {
		this.dataSensor = dataSensor;
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
