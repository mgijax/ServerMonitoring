package org.jax.mgi.servermonitoring.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@ApiModel
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "property"), indexes={@Index(name="dataproperty_property_index", columnList="property"), @Index(name="dataproperty_id_index", columnList="id")})
public class DataProperty implements Serializable {

	@Id
    @GeneratedValue
    private Long id;
    
	private String property;

	public DataProperty() { }
	
    public DataProperty(String propertyName) {
		this.property = propertyName;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
	public String toString() {
		return "DataProperty[Id: " + id + " Property: " + property + "]";
	}
}
