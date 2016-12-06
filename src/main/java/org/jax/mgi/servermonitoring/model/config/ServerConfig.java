package org.jax.mgi.servermonitoring.model.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@SuppressWarnings("serial")
@Entity
@XmlRootElement
@ApiModel
@Table(indexes={@Index(name="serverconfig_id_index", columnList="id")})
public class ServerConfig implements Serializable {

	@Id
	@GeneratedValue
	private Long id;
	private String clientName;
	private String clientArch;

	@OneToMany(mappedBy="serverConfig")
	private List<ServerConfigType> types = new ArrayList<ServerConfigType>();

	private Date lastUpdate;

	public ServerConfig(String clientName, String clientArch) {
		this.clientName = clientName;
		this.clientArch = clientArch;

		int frequency = 60;
		ServerConfigType type = new ServerConfigType("System");
		ServerConfigName name = new ServerConfigName("Load", frequency);
		type.getNames().add(name);
		name = new ServerConfigName("Uptime", frequency);
		type.getNames().add(name);
		name = new ServerConfigName("Info", 86400);
		type.getNames().add(name);
		name = new ServerConfigName("Users", frequency);
		type.getNames().add(name);
		types.add(type);
		
		type = new ServerConfigType("Memory");
		name = new ServerConfigName("Ram", frequency);
		type.getNames().add(name);
		name = new ServerConfigName("Swap", frequency);
		type.getNames().add(name);
		types.add(type);

		type = new ServerConfigType("Disk");
		name = new ServerConfigName("Speed", frequency);
		name.getProperties().add(new ServerConfigProperty("/var/tmp/WatchDog_SpeedFile"));
		type.getNames().add(name);
		name = new ServerConfigName("Size", frequency);
		name.getProperties().add(new ServerConfigProperty("/var/tmp/WatchDog_SpeedFile"));
		type.getNames().add(name);
		types.add(type);

		type = new ServerConfigType("Network");
		name = new ServerConfigName("Errors", frequency);
		name.getProperties().add(new ServerConfigProperty("eth0"));
		name.getProperties().add(new ServerConfigProperty("lo"));
		type.getNames().add(name);
		name = new ServerConfigName("Bandwidth", frequency);
		name.getProperties().add(new ServerConfigProperty("eth0"));
		name.getProperties().add(new ServerConfigProperty("lo"));
		type.getNames().add(name);
		types.add(type);

	}

	public ServerConfig() { }

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientArch() {
		return clientArch;
	}
	public void setClientArch(String clientArch) {
		this.clientArch = clientArch;
	}
	public List<ServerConfigType> getTypes() {
		return types;
	}
	public void setTypes(List<ServerConfigType> types) {
		this.types = types;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
