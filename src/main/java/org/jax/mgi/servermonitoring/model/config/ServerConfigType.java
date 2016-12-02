package org.jax.mgi.servermonitoring.model.config;

import java.io.Serializable;
import java.util.ArrayList;
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
@Table(indexes={@Index(name="serverconfigtype_id_index", columnList="id")})
public class ServerConfigType implements Serializable {

	@Id
    @GeneratedValue
    private Long id;
	
	private String type; // System, Memory, Disk, Database, Web, Network
	private boolean active = false;
	
	@ManyToOne
	private ServerConfig serverConfig;
	
	@OneToMany(mappedBy="serverConfigType")
	private List<ServerConfigName> names = new ArrayList<ServerConfigName>();

	public ServerConfigType() { }
	
	public ServerConfigType(String type) {
		active = true;
		this.type = type;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public ServerConfig getServerConfig() {
		return serverConfig;
	}
	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}
	public List<ServerConfigName> getNames() {
		return names;
	}
	public void setNames(List<ServerConfigName> names) {
		this.names = names;
	}
}
