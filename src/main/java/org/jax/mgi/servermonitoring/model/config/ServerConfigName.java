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
@Table(indexes={@Index(name="serverconfigname_id_index", columnList="id")})
public class ServerConfigName implements Serializable {


	@Id
    @GeneratedValue
    private Long id;
	
	// System(Load, Uptime, Users, Info)
	// Memory(Raw, Swap)
	// Disk(Speed(Volume), Size(Volume))
	// Database(QueryTimes, QuerySize)
	// Web(Apache, PWI)
	// Network(Errors(Interface), Bandwidth(Interface))
	private String name;
	private int frequency = 60;
	private boolean active = false;
	
	@ManyToOne
	private ServerConfigType serverConfigType;
	
	@OneToMany(mappedBy="serverConfigName")
	private List<ServerConfigProperty> properties = new ArrayList<ServerConfigProperty>();

	public ServerConfigName() { }
	
	public ServerConfigName(String name, int frequency) {
		active = true;
		this.name = name;
		this.frequency = frequency;
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
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public ServerConfigType getServerConfigType() {
		return serverConfigType;
	}
	public void setServerConfigType(ServerConfigType serverConfigType) {
		this.serverConfigType = serverConfigType;
	}
	public List<ServerConfigProperty> getProperties() {
		return properties;
	}
	public void setProperties(List<ServerConfigProperty> properties) {
		this.properties = properties;
	}
}
