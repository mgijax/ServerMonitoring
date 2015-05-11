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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jax.mgi.servermonitoring.model.DataName;
import org.jax.mgi.servermonitoring.model.DataPoint;
import org.jax.mgi.servermonitoring.model.DataPointDTO;
import org.jax.mgi.servermonitoring.model.DataProperty;
import org.jax.mgi.servermonitoring.model.DataType;
import org.jax.mgi.servermonitoring.model.ServerName;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class DataPointService {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Inject
	private Event<DataPoint> serverDataSrc;

	public void createDataPoint(DataPointDTO data) throws Exception {
		DataPoint serverData = getDataPoint(data);
		em.persist(serverData);
		serverDataSrc.fire(serverData);
	}

	public List<DataPointDTO> listDataPoints(String serverName, String dataType, String dataName, String dataProperty) {
		
		String query = "select dp from DataPoint dp where";
		
		if(serverName != null) {
			query += " dp.serverName.name = :serverName AND";
		}
		if(dataType != null) {
			query += " dp.dataType.type = :dataType AND";
		}
		if(dataName != null) {
			query += " dp.dataName.name = :dataName AND";
		}
		if(dataProperty != null) {
			query += " dp.dataProperty.property = :dataProperty AND";
		}
		query += " 1 = 1";
		
		Query q = em.createQuery(query);
		
		if(serverName != null) {
			q.setParameter("serverName", serverName);
		}
		if(dataType != null) {
			q.setParameter("dataType", dataType);
		}
		if(dataName != null) {
			q.setParameter("dataName", dataName);
		}
		if(dataProperty != null) {
			q.setParameter("dataProperty", dataProperty);
		}

		log.info("Query: " + query);
		
		List<DataPoint> list = q.getResultList();
		
		List<DataPointDTO> dtos = new ArrayList<DataPointDTO>();
		for(DataPoint d: list) {
			dtos.add(new DataPointDTO(d));
		}
		return dtos;
	}

	private DataPoint getDataPoint(DataPointDTO data) {
		ServerName serverName = getServerName(data);
		DataType dataType = getDataType(data);
		DataName dataName = getDataName(data);
		DataProperty dataProperty = getDataProperty(data);
		return new DataPoint(serverName, dataType, dataName, dataProperty, data.getDataValue(), new Date());
	}

	private DataName getDataName(DataPointDTO data) {
		try {
			return (DataName)em.createQuery("select dn from DataName dn where name = :name")
				.setParameter("name", data.getDataName()).getSingleResult();
		} catch(NoResultException e) {
			DataName dataName = new DataName(data.getDataName());
			em.persist(dataName);
			return dataName;
		}
	}

	private DataType getDataType(DataPointDTO data) {
		try {
			return (DataType)em.createQuery("select dt from DataType dt where type = :type")
					.setParameter("type", data.getDataType()).getSingleResult();
		} catch(NoResultException e) {
			DataType dataType = new DataType(data.getDataType());
			em.persist(dataType);
			return dataType;
		}
	}
	
	private DataProperty getDataProperty(DataPointDTO data) {
		try {
			return (DataProperty)em.createQuery("select dp from DataProperty dp where property = :property")
					.setParameter("property", data.getDataProperty()).getSingleResult();
		} catch(NoResultException e) {
			DataProperty dataProperty = new DataProperty(data.getDataProperty());
			em.persist(dataProperty);
			return dataProperty;
		}
	}

	private ServerName getServerName(DataPointDTO data) {
		try {
			return (ServerName)em.createQuery("select sn from ServerName sn where name = :name")
					.setParameter("name", data.getServerName()).getSingleResult();
		} catch(NoResultException e) {
			ServerName serverName = new ServerName(data.getServerName());
			em.persist(serverName);
			return serverName;
		}
	}

	public List<ServerName> getServerList() {
		try {
			return em.createQuery("select s from ServerName s").getResultList();
		} catch(NoResultException e) {
			return null;
		}
	}

	public List<DataPoint> getLastUpdates() {
		//select a, COUNT(p) FROM Artist a JOIN a.paintings p GROUP BY a
		// select max(datatimestamp), sn.name from datapoint dp, servername sn where dp.servername_id = sn.id group by sn.name
		try {
			List<Long> list = em.createQuery("select max(dp.id) from DataPoint dp group by dp.serverName").getResultList();
			
			List<DataPoint> dps = em.createQuery("select dp from DataPoint dp where dp.id in (:ids)").setParameter("ids", list).getResultList();
			
//			for(Object[] o: list) {
//				dps.add((DataPoint)o[0]);
//			}
			return dps;
		} catch(NoResultException e) {
			return null;
		}
	}
}
