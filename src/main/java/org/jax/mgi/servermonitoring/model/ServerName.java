package org.jax.mgi.servermonitoring.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@ApiModel
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"), indexes={@Index(name="servername_name_index", columnList="name"), @Index(name="servername_id_index", columnList="id")})
public class ServerName implements Serializable {

	@Id
    @GeneratedValue
    private Long id;
	
	private String name;
	
	@OneToMany(mappedBy="serverName", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<DataPoint> dataPoints;

	public ServerName() { }
	
    public ServerName(String serverName) {
		this.name = serverName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return "ServerName[Id: " + id + " Name: " + name + "]";
	}
}
