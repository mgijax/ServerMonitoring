/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jax.mgi.servermonitoring.service;

import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.jax.mgi.servermonitoring.model.DataName;
import org.jax.mgi.servermonitoring.model.DataType;
import org.jax.mgi.servermonitoring.model.ServerData;
import org.jax.mgi.servermonitoring.model.ServerDataDTO;
import org.jax.mgi.servermonitoring.model.ServerName;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ServerDataBean {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<ServerData> serverDataSrc;

	public void createEntry(ServerDataDTO data) throws Exception {
		log.info("Creating: " + data);

		ServerData serverData = getServerData(data);

		em.persist(serverData);
		serverDataSrc.fire(serverData);
	}

	private ServerData getServerData(ServerDataDTO data) {
		ServerName serverName = getServerName(data);
		DataType dataType = getDataType(data);
		DataName dataName = getDataName(data);
		return new ServerData(serverName, dataType, dataName, data.getDataValue(), data.getDataTimeStamp());
	}

	private DataName getDataName(ServerDataDTO data) {
		// TODO Auto-generated method stub
		return null;
	}

	private DataType getDataType(ServerDataDTO data) {
		// TODO Auto-generated method stub
		return null;
	}

	private ServerName getServerName(ServerDataDTO data) {
		
		return null;
	}

}
