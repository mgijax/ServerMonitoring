package org.jax.mgi.servermonitoring.model.config;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@ApiModel
@Table(indexes={@Index(name="serverconfigproperty_id_index", columnList="id")})
public class ServerConfigProperty implements Serializable {
	
	@Id
    @GeneratedValue
    private Long id;
	
	@ManyToOne
	private ServerConfigName serverConfigName;
	
	// Volumes and Interfaces
	private String property;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ServerConfigName getServerConfigName() {
		return serverConfigName;
	}
	public void setServerConfigName(ServerConfigName serverConfigName) {
		this.serverConfigName = serverConfigName;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
}
