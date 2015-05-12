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
	
	private boolean debug = false;

	public void createDataPoint(DataPointDTO data) throws Exception {
		DataPoint serverData = getDataPoint(data);
		em.persist(serverData);
		serverDataSrc.fire(serverData);
	}

	public List<DataPointDTO> listDataPoints(DataPointDTO data, int amount) {
		
		String query = "select dp from DataPoint dp where";
		
		if(data.getServerName() != null && !data.getServerName().equals("")) {
			query += " dp.serverName.name = :serverName AND";
		}
		if(data.getDataType() != null && !data.getDataType().equals("")) {
			query += " dp.dataType.type = :dataType AND";
		}
		if(data.getDataName() != null && !data.getDataName().equals("")) {
			query += " dp.dataName.name = :dataName AND";
		}
		if(data.getDataProperty() != null && !data.getDataProperty().equals("")) {
			query += " dp.dataProperty.property = :dataProperty AND";
		}
		query += " 1 = 1";
		query += " order by dp.dataTimeStamp desc";
		
		Query q = em.createQuery(query);
		
		if(data.getServerName() != null && !data.getServerName().equals("")) {
			q.setParameter("serverName", data.getServerName());
		}
		if(data.getDataType() != null && !data.getDataType().equals("")) {
			q.setParameter("dataType", data.getDataType());
		}
		if(data.getDataName() != null && !data.getDataName().equals("")) {
			q.setParameter("dataName", data.getDataName());
		}
		if(data.getDataProperty() != null && !data.getDataProperty().equals("")) {
			q.setParameter("dataProperty", data.getDataProperty());
		}
		
		if(amount > 0) {
			q.setMaxResults(amount);
		}

		if(debug) log.info("Query: " + query);
		
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
			if(debug) log.info("Query: " + "select dn from DataName dn where name = :name");
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
			if(debug) log.info("Query: " + "select dt from DataType dt where type = :type");
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
			if(debug) log.info("Query: " + "select dp from DataProperty dp where property = :property");
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
			if(debug) log.info("Query: " + "select sn from ServerName sn where name = :name");
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
			log.info("Query: " + "select s from ServerName s");
			return em.createQuery("select s from ServerName s").getResultList();
		} catch(NoResultException e) {
			return null;
		}
	}

	public List<DataPoint> getLastUpdates() {
		//select a, COUNT(p) FROM Artist a JOIN a.paintings p GROUP BY a
		// select max(datatimestamp), sn.name from datapoint dp, servername sn where dp.servername_id = sn.id group by sn.name
		try {
			if(debug) log.info("Query: " + "select max(dp.id) from DataPoint dp group by dp.serverName");
			List<Long> list = em.createQuery("select max(dp.id) from DataPoint dp group by dp.serverName").getResultList();
			
			if(debug) log.info("Query: " + "select dp from DataPoint dp where dp.id in (:ids)");
			List<DataPoint> dps = em.createQuery("select dp from DataPoint dp where dp.id in (:ids)").setParameter("ids", list).getResultList();
			
//			for(Object[] o: list) {
//				dps.add((DataPoint)o[0]);
//			}
			return dps;
		} catch(NoResultException e) {
			return null;
		}
	}

	public List<DataType> getDataTypes(ServerName selectedServername) {
		if(debug) log.info("Query: " + "select distinct dp.dataType from DataPoint dp where dp.serverName = :serverName");
		return em.createQuery("select distinct dp.dataType from DataPoint dp where dp.serverName = :serverName").setParameter("serverName", selectedServername).getResultList();
	}

	public List<DataName> getDataNames(ServerName selectedServername, DataType dataType) {
		if(debug) log.info("Query: " + "select distinct dp.dataName from DataPoint dp where dp.serverName = :serverName and dp.dataType = :dataType");
		return em.createQuery("select distinct dp.dataName from DataPoint dp where dp.serverName = :serverName and dp.dataType = :dataType")
			.setParameter("serverName", selectedServername)
			.setParameter("dataType", dataType)
			.getResultList();
	}

	public List<DataProperty> getDataProperties(ServerName selectedServername, DataType dataType, DataName dataName) {
		if(debug) log.info("Query: " + "select distinct dp.dataProperty from DataPoint dp where dp.serverName = :serverName and dp.dataType = :dataType and dp.dataName = :dataName");
		return em.createQuery("select distinct dp.dataProperty from DataPoint dp where dp.serverName = :serverName and dp.dataType = :dataType and dp.dataName = :dataName")
				.setParameter("serverName", selectedServername)
				.setParameter("dataType", dataType)
				.setParameter("dataName", dataName)
				.getResultList();
	}
	
	
}
