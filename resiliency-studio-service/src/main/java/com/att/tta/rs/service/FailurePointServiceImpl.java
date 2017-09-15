/*******************************************************************************
 *   BSD License
 *    
 *   Copyright (c) 2017, AT&T Intellectual Property.  All other rights reserved.
 *    
 *   Redistribution and use in source and binary forms, with or without modification, are permitted
 *   provided that the following conditions are met:
 *    
 *   1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *      and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *      conditions and the following disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   3. All advertising materials mentioning features or use of this software must display the
 *      following acknowledgement:  This product includes software developed by the AT&T.
 *   4. Neither the name of AT&T nor the names of its contributors may be used to endorse or
 *      promote products derived from this software without specific prior written permission.
 *    
 *   THIS SOFTWARE IS PROVIDED BY AT&T INTELLECTUAL PROPERTY ''AS IS'' AND ANY EXPRESS OR
 *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 *   MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 *   SHALL AT&T INTELLECTUAL PROPERTY BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  LOSS OF USE, DATA, OR PROFITS;
 *   OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *   CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *   ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 *   DAMAGE.
 *******************************************************************************/
package com.att.tta.rs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.ajsc.logging.AjscEelfManager;
import com.att.eelf.configuration.EELFLogger;
import com.att.tta.rs.data.es.repository.FailurePointRepository;
import com.att.tta.rs.model.FailurePoint;
import com.att.tta.rs.model.Server;

/**
 * Implementation class for service {@link FailurePointService}
 * 
 * @author mb6872
 *
 */
@Service("failurePointService")
@Transactional
public class FailurePointServiceImpl implements FailurePointService {
	private static EELFLogger logger = AjscEelfManager.getInstance().getLogger(FailurePointServiceImpl.class);

	private FailurePointRepository failurePointRepository;

	@Autowired
	public void setFailurePointRepository(FailurePointRepository failurePointRepository) {
		this.failurePointRepository = failurePointRepository;
	}

	@Override
	public FailurePoint findOne(String id) {
		return failurePointRepository.findOne(id);
	}

	@Override
	public Iterable<FailurePoint> findAll() {
		return failurePointRepository.findAll();
	}

	@Override
	public FailurePoint findByName(String name) {
		return failurePointRepository.findByName(name);
	}

	@Override
	public List<FailurePoint> findByCategory(String category) {
		return failurePointRepository.findByCategory(category);
	}

	@Override
	public List<FailurePoint> findByRole(String role) {
		return failurePointRepository.findByRole(role);
	}

	@Override
	public List<FailurePoint> findByComponent(String component) {
		return failurePointRepository.findByComponent(component);
	}

	@Override
	public List<FailurePoint> findByProcessName(String processName) {
		return failurePointRepository.findByProcessName(processName);
	}

	@Override
	public long count() {
		return failurePointRepository.count();
	}

	@Override
	public boolean isFailurePointExist(FailurePoint failurePoint) {
		return findByName(failurePoint.getName()) != null;
	}

	@Override
	public FailurePoint update(FailurePoint failurePoint) {
		if (failurePoint != null && !"".equals(failurePoint.getId().trim())
				&& (failurePointRepository.findOne(failurePoint.getId()) != null)) {
			return save(failurePoint);
		}
		return null;
	}

	@Override
	public FailurePoint insert(FailurePoint failurePoint) {
		if (this.isFailurePointExist(failurePoint)) {
			logger.debug("FailurePoint already exists with name " + failurePoint.getName());
			return null;
		}
		return save(failurePoint);
	}

	@Override
	public FailurePoint save(FailurePoint failurePoint) {
		return failurePointRepository.save(failurePoint);
	}

	@Override
	public void delete(FailurePoint failurePoint) {
		failurePointRepository.delete(failurePoint);
	}

	@Override
	public void deleteAllFailurePoint() {
		failurePointRepository.deleteAll();
	}

	@Override
	public Map<String, List<FailurePoint>> findByCategoryAndRole(String category, List<Server> serverList) {
		HashMap<String, List<FailurePoint>> serverFailurePointMap = new HashMap<>();
		for (Server server : serverList) {
			for (String role : server.getRoles()) {
				List<FailurePoint> fpList = failurePointRepository.findByCategoryAndRole(category, role);
				setFPLst(fpList, server, serverFailurePointMap, role);
			}
		}
		logger.debug("Total Scenarios possible -->" + serverFailurePointMap.size());
		return serverFailurePointMap;
	}

	/**
	 * @param fpList
	 * @param server
	 * @param serverFailurePointMap
	 * @param role
	 */
	private void setFPLst(List<FailurePoint> fpList, Server server,
			HashMap<String, List<FailurePoint>> serverFailurePointMap, String role) {
		if (fpList != null && !fpList.isEmpty()) {
			logger.debug("Server instance name -->" + server.getInstanceName());
			logger.debug("Role in query is -->" + role);

			if (serverFailurePointMap.containsKey(server.getInstanceName())) {
				List<FailurePoint> tempFPList = serverFailurePointMap.get(server.getInstanceName());
				List<FailurePoint> newList = new ArrayList<>();
				newList.addAll(fpList);
				newList.addAll(tempFPList);
				serverFailurePointMap.put(server.getInstanceName(), newList);
			} else
				serverFailurePointMap.put(server.getInstanceName(), fpList);
		}
	}
}