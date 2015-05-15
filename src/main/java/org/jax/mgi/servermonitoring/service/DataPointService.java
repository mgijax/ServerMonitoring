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
import org.jax.mgi.servermonitoring.model.DataSensor;
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
		DataPoint dataPoint = saveDataPoint(data);
		serverDataSrc.fire(dataPoint);
	}

	public List<DataPointDTO> listDataPoints(DataPointDTO data, int amount) {

		DataSensor dataSensor = getDataSensor(data, false);
		String query = "select dp from DataPoint dp where dp.dataSensor.id = :dataSensor order by dp.dataTimeStamp desc";
		Query q = em.createQuery(query).setParameter("dataSensor", dataSensor.getId());
		
		if(amount > 0) {
			q.setMaxResults(amount);
		}

		List<DataPoint> list = q.getResultList();
		
		List<DataPointDTO> dtos = new ArrayList<DataPointDTO>();
		for(DataPoint d: list) {
			dtos.add(new DataPointDTO(d));
		}
		return dtos;
	}

	private DataPoint saveDataPoint(DataPointDTO data) {
		DataSensor dataSensor = getDataSensor(data, true);
		DataPoint dataPoint = new DataPoint(dataSensor, data.getDataValue(), new Date());
		em.persist(dataPoint);
		return dataPoint;
	}
	
	private DataSensor getDataSensor(DataPointDTO data, boolean createNew) {
		ServerName serverName = getServerName(data, createNew);
		DataType dataType = getDataType(data, createNew);
		DataName dataName = getDataName(data, createNew);
		DataProperty dataProperty = getDataProperty(data, createNew);
		try {
			if(debug) log.info("getDataSensor: " + "select ds from DataSensor ds where ds.serverName.id = " + serverName.getName() + " and ds.dataType.id = " + dataType.getType() + " and ds.dataName.id = " + dataName.getName() + " and ds.dataProperty.id = " + dataProperty.getProperty());
			return (DataSensor)em.createQuery("select ds from DataSensor ds where ds.serverName.id = :serverName and ds.dataType.id = :dataType and ds.dataName.id = :dataName and ds.dataProperty.id = :dataProperty")
					.setParameter("serverName", serverName.getId())
					.setParameter("dataType", dataType.getId())
					.setParameter("dataName", dataName.getId())
					.setParameter("dataProperty", dataProperty.getId())
					.getSingleResult();
		} catch(NoResultException e) {
			if(createNew) {
				DataSensor dataSensor = new DataSensor(serverName, dataType, dataName, dataProperty);
				em.persist(dataSensor);
				return dataSensor;
			} else {
				return null;
			}
		}
	}

	private DataName getDataName(DataPointDTO data, boolean createNew) {
		try {
			if(debug) log.info("getDataName: " + "select dn from DataName dn where name = " + data.getDataName());
			return (DataName)em.createQuery("select dn from DataName dn where name = :name")
				.setParameter("name", data.getDataName()).getSingleResult();
		} catch(NoResultException e) {
			if(createNew) {
				DataName dataName = new DataName(data.getDataName());
				em.persist(dataName);
				return dataName;
			} else {
				return null;
			}
		}
	}

	private DataType getDataType(DataPointDTO data, boolean createNew) {
		try {
			if(debug) log.info("getDataType: " + "select dt from DataType dt where type = :type");
			return (DataType)em.createQuery("select dt from DataType dt where type = :type")
					.setParameter("type", data.getDataType()).getSingleResult();
		} catch(NoResultException e) {
			if(createNew) {
				DataType dataType = new DataType(data.getDataType());
				em.persist(dataType);
				return dataType;
			} else {
				return null;
			}
		}
	}
	
	private DataProperty getDataProperty(DataPointDTO data, boolean createNew) {
		try {
			if(debug) log.info("getDataProperty: " + "select dp from DataProperty dp where property = :property");
			return (DataProperty)em.createQuery("select dp from DataProperty dp where property = :property")
					.setParameter("property", data.getDataProperty()).getSingleResult();
		} catch(NoResultException e) {
			if(createNew) {
				DataProperty dataProperty = new DataProperty(data.getDataProperty());
				em.persist(dataProperty);
				return dataProperty;
			} else {
				return null;
			}
		}
	}

	public ServerName getServerName(String serverName) {
		DataPointDTO dto = new DataPointDTO();
		dto.setServerName(serverName);
		return getServerName(dto, false);
	}
	
	private ServerName getServerName(DataPointDTO data, boolean createNew) {
		try {
			if(debug) log.info("getServerName: " + "select sn from ServerName sn where name = :name");
			return (ServerName)em.createQuery("select sn from ServerName sn where name = :name")
					.setParameter("name", data.getServerName()).getSingleResult();
		} catch(NoResultException e) {
			if(createNew) {
				ServerName serverName = new ServerName(data.getServerName());
				em.persist(serverName);
				return serverName;
			} else {
				return null;
			}
		}
	}

	public List<ServerName> getServerList() {
		try {
			if(debug) log.info("getServerList: " + "select s from ServerName s");
			return em.createQuery("select s from ServerName s").getResultList();
		} catch(NoResultException e) {
			return null;
		}
	}

	public List<DataPoint> getLastUpdates() {
		//select a, COUNT(p) FROM Artist a JOIN a.paintings p GROUP BY a
		// select max(datatimestamp), sn.name from datapoint dp, servername sn where dp.servername_id = sn.id group by sn.name
		try {
			if(debug) log.info("Query: " + "select max(dp.id) from DataPoint dp, DataSensor ds, ServerName sn where sn.id = ds.serverName.id and ds.id = dp.dataSensor.id group by ds.serverName.id");
			List<Long> list = em.createQuery("select max(dp.id) from DataPoint dp, DataSensor ds, ServerName sn where sn.id = ds.serverName.id and ds.id = dp.dataSensor.id group by ds.serverName.id").getResultList();

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
		if(debug) log.info("getDataTypes: " + "select distinct ds.dataType from DataSensor ds where ds.serverName = :serverName");
		return em.createQuery("select distinct ds.dataType from DataSensor ds where ds.serverName.id = :serverName").setParameter("serverName", selectedServername.getId()).getResultList();
	}

	public List<DataName> getDataNames(ServerName selectedServername, DataType dataType) {
		if(debug) log.info("getDataNames: " + "select distinct ds.dataName from DataSensor ds where ds.serverName = :serverName and ds.dataType = :dataType");
		return em.createQuery("select distinct ds.dataName from DataSensor ds where ds.serverName.id = :serverName and ds.dataType.id = :dataType")
			.setParameter("serverName", selectedServername.getId())
			.setParameter("dataType", dataType.getId())
			.getResultList();
	}

	public List<DataProperty> getDataProperties(ServerName selectedServername, DataType dataType, DataName dataName) {
		if(debug) log.info("getDataProperties: " + "select distinct ds.dataProperty from DataSensor ds where ds.serverName = :serverName and ds.dataType = :dataType and ds.dataName = :dataName");
		return em.createQuery("select distinct ds.dataProperty from DataSensor ds where ds.serverName.id = :serverName and ds.dataType.id = :dataType and ds.dataName.id = :dataName")
				.setParameter("serverName", selectedServername.getId())
				.setParameter("dataType", dataType.getId())
				.setParameter("dataName", dataName.getId())
				.getResultList();
	}

}
