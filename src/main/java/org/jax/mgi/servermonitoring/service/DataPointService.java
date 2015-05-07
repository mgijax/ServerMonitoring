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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jax.mgi.servermonitoring.model.DataName;
import org.jax.mgi.servermonitoring.model.DataProperty;
import org.jax.mgi.servermonitoring.model.DataType;
import org.jax.mgi.servermonitoring.model.DataPoint;
import org.jax.mgi.servermonitoring.model.DataPointDTO;
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

	public void createEntry(DataPointDTO data) throws Exception {
		DataPoint serverData = getServerData(data);
		em.persist(serverData);
		serverDataSrc.fire(serverData);
	}

	public List<DataPointDTO> listData() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DataPoint> criteria = cb.createQuery(DataPoint.class);
		Root<DataPoint> data = criteria.from(DataPoint.class);
		criteria.select(data);
		List<DataPoint> list = em.createQuery(criteria).getResultList();
		List<DataPointDTO> dtos = new ArrayList<DataPointDTO>();
		for(DataPoint d: list) {
			dtos.add(new DataPointDTO(d));
		}
		return dtos;
	}

	private DataPoint getServerData(DataPointDTO data) {
		ServerName serverName = getServerName(data);
		DataType dataType = getDataType(data);
		DataName dataName = getDataName(data);
		DataProperty dataProperty = getDataProperty(data);
		return new DataPoint(serverName, dataType, dataName, dataProperty, data.getDataValue(), new Date());
	}

	private DataName getDataName(DataPointDTO data) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<DataName> criteria = cb.createQuery(DataName.class);
			Root<DataName> dataName = criteria.from(DataName.class);
			criteria.select(dataName).where(cb.equal(dataName.get("name"), data.getDataName()));
			return em.createQuery(criteria).getSingleResult();
		} catch(NoResultException e) {
			DataName dataName = new DataName(data.getDataName());
			em.persist(dataName);
			return dataName;
		}
	}

	private DataType getDataType(DataPointDTO data) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<DataType> criteria = cb.createQuery(DataType.class);
			Root<DataType> dataType = criteria.from(DataType.class);
			criteria.select(dataType).where(cb.equal(dataType.get("type"), data.getDataType()));
			return em.createQuery(criteria).getSingleResult();
		} catch(NoResultException e) {
			DataType dataType = new DataType(data.getDataType());
			em.persist(dataType);
			return dataType;
		}
	}
	
	private DataProperty getDataProperty(DataPointDTO data) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<DataProperty> criteria = cb.createQuery(DataProperty.class);
			Root<DataProperty> dataProperty = criteria.from(DataProperty.class);
			criteria.select(dataProperty).where(cb.equal(dataProperty.get("property"), data.getDataProperty()));
			return em.createQuery(criteria).getSingleResult();
		} catch(NoResultException e) {
			DataProperty dataProperty = new DataProperty(data.getDataProperty());
			em.persist(dataProperty);
			return dataProperty;
		}
	}

	private ServerName getServerName(DataPointDTO data) {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<ServerName> criteria = cb.createQuery(ServerName.class);
			Root<ServerName> serverName = criteria.from(ServerName.class);
			criteria.select(serverName).where(cb.equal(serverName.get("name"), data.getServerName()));
			return em.createQuery(criteria).getSingleResult();
		} catch(NoResultException e) {
			ServerName serverName = new ServerName(data.getServerName());
			em.persist(serverName);
			return serverName;
		}
	}
}
