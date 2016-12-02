package org.jax.mgi.servermonitoring.model.config;

import java.io.Serializable;
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
	private List<ServerConfigType> types;
	
	private Date lastUpdate;
	
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
