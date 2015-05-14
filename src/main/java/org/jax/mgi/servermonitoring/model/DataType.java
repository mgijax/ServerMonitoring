package org.jax.mgi.servermonitoring.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@ApiModel
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "type"), indexes={@Index(name="datatype_type_index", columnList="type"), @Index(name="datatype_id_index", columnList="id")})
public class DataType implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
	private String type;

	public DataType() { }
	
	public DataType(String dataType) {
		this.type = dataType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String toString() {
		return "DataType[Id: " + id + " Type: " + type + "]";
	}

}
