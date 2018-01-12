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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.ajsc.logging.AjscEelfManager;
import com.att.eelf.configuration.EELFLogger;
import com.att.tta.rs.data.es.repository.MonkeyStrategyRepository;
import com.att.tta.rs.model.FailurePoint;
import com.att.tta.rs.model.MonkeyStrategy;
import com.att.tta.rs.model.MonkeyType;
import com.att.tta.rs.model.Server;
import com.att.tta.rs.model.SoftwareComponent;
import com.att.tta.rs.util.AppUtil;

/**
 * Implementation class for {@link MonkeyStrategyService}
 * 
 * @author mb6872
 *
 */
@Service("monkeyStrategyService")
@Transactional
public class MonkeyStrategyServiceImpl implements MonkeyStrategyService {
	private static EELFLogger logger = AjscEelfManager.getInstance().getLogger(MonkeyStrategyServiceImpl.class);
	private MonkeyStrategyRepository monkeyStrategyRepository;
	
	private static final String CUROSTYPE = "Linux";
	private static final String CHANGEDOSTYPE = "unix";
	private static final String MONEKYTYPE = "Chaos Monkey";
	private static final String FAILURETENET = "Fault";
	private static final String HARDWAREFAILURECAT = "hardware";
	private static final String SOFTWARECAT = "software";
	private static final String APPCAT = "Application";
	
	@Autowired
	public void setMonkeyStrategyRepository(MonkeyStrategyRepository monkeyStrategyRepository) {
		this.monkeyStrategyRepository = monkeyStrategyRepository;
	}

	private MonkeyStrategy save(MonkeyStrategy monkeyStrategy) {
		return monkeyStrategyRepository.save(monkeyStrategy);
	}

	@Override
	public MonkeyStrategy findOneForTeam(String id, String teamName) {
		if (teamName.trim().equals(AppUtil.getSuperUser()))
			return monkeyStrategyRepository.findOne(id);
		else {
			MonkeyStrategy mk = monkeyStrategyRepository.findOne(id);
			if (mk == null) {
				logger.error("Monkey Strategy cannot be found for id -->" + id);
				return null;
			}
			if (mk.getTeamName().equals(teamName))
				return mk;
			else {
				return null;
			}
		}
	}

	@Override
	public long count() {
		return monkeyStrategyRepository.count();
	}

	@Override
	public void delete(MonkeyStrategy monkeyStrategy) {
		monkeyStrategyRepository.delete(monkeyStrategy);
	}

	@Override
	public void deleteAllMonkeyStrategy() {
		monkeyStrategyRepository.deleteAll();
	}

	@Override
	public boolean isMonkeyStrategyExistAndTeamName(MonkeyStrategy monkeyStrategy, String teamName) {
		return findByMonkeyStrategyAndTeamName(monkeyStrategy, teamName).iterator().hasNext();
	}

	@Override
	public MonkeyStrategy update(MonkeyStrategy monkeyStrategy) {
		if (monkeyStrategy != null && !"".equals(monkeyStrategy.getId().trim())
				&& (monkeyStrategyRepository.findOne(monkeyStrategy.getId()) != null)) {
			return save(monkeyStrategy);
		}
		return null;
	}

	@Override
	public MonkeyStrategy insertForTeam(MonkeyStrategy monkeyStrategy, String teamName) {
		if (this.isMonkeyStrategyExistAndTeamName(monkeyStrategy, teamName)) {
			logger.debug("Monkey Strategy already exists with name " + monkeyStrategy.getMonkeyStrategyName());
			return null;
		}
		return save(monkeyStrategy);
	}

	@Override
	public Iterable<MonkeyStrategy> findAllForTeam(String teamName) {
		if (teamName.trim().equals(AppUtil.getSuperUser()))
			return monkeyStrategyRepository.findAll();
		else
			return monkeyStrategyRepository.findByTeamName(teamName, new PageRequest(0, 9999));
	}

	@Override
	public MonkeyStrategy findByMonkeyStrategyNameAndTeamNameAndVersion(String monkeyStrategyName, String teamName,
			String monkeyStrategyVersion) {
		return monkeyStrategyRepository.findByMonkeyStrategyNameAndTeamNameAndMonkeyStrategyVersion(monkeyStrategyName,
				teamName, monkeyStrategyVersion);
	}

	@Override
	public Long countByTeamName(String teamName) {
		if (teamName.trim().equals(AppUtil.getSuperUser()))
			return monkeyStrategyRepository.count();
		else
			return monkeyStrategyRepository.countByTeamName(teamName);

	}

	@Override
	public Iterable<MonkeyStrategy> findDefaultMonkeyStrategy(MonkeyType monkeyType, String monkeyStrategy,
			String defaultFlag, String teamName) {
		return monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndDefaultFlagAndTeamName(
				monkeyType.monkeyType(), monkeyStrategy, defaultFlag, teamName);
	}

	@Override
	public MonkeyStrategy findDefaultMonkeyStrategy(String monkeyStrategyName, MonkeyType monkeyType, String teamName) {
		Iterable<MonkeyStrategy> monkeyIt = monkeyStrategyRepository
				.findByMonkeyTypeAndMonkeyStrategyNameAndDefaultFlagAndTeamName(monkeyType.monkeyType(),
						monkeyStrategyName, "Y", teamName);
		if (monkeyIt != null && monkeyIt.iterator().hasNext()) {
			return monkeyIt.iterator().next();
		}
		return null;
	}

	@Override
	public MonkeyStrategy findByMonkeyStrategyNameAndTeamName(String monkeyStrategyName, String teamName) {
		return monkeyStrategyRepository.findByMonkeyStrategyNameAndTeamName(monkeyStrategyName, teamName);
	}

	@Override
	public Iterable<MonkeyStrategy> findByMonkeyStrategyAndTeamName(MonkeyStrategy monkeyStrategy, String teamName) {
		return monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), monkeyStrategy.getMonkeyStrategyName(), teamName);
	}

	@Override
	public Map<String, List<FailurePoint>> findMonkeyStrategyByOSTypeAndProcessName(String category,
			List<Server> serverList) {
		HashMap<String, List<FailurePoint>> serverFailurePointMap = new HashMap<>();
		for (Server server : serverList) {
			String role = "";
			if (null != server.getRoles() && !server.getRoles().isEmpty()) {
				role = server.getRoles().get(0);
			}
			List<FailurePoint> fpList = new ArrayList<>();
			String osType = server.getOperatingSystem();
			if(null != osType){
				if (CUROSTYPE.equalsIgnoreCase(osType)) {
					osType = CHANGEDOSTYPE;
				}
				osType = osType != null ? osType.toLowerCase() : osType;
				fpList.addAll(setFailurePointMetaData(monkeyStrategyRepository
						.findByOsTypeAndDefaultFlagAndGenericAndFlavorAndFailureCategoryAndFailureSubCategory(osType, "Y",
								"Y", "all", HARDWAREFAILURECAT, "all"),
						null, null, role));

				if (server.getSwComps() != null && !server.getSwComps().isEmpty()) {
					findMonkeyStrategyByProcessName(server, fpList, osType, role);
				}
			}
			serverFailurePointMap.put(server.getInstanceName(), fpList);
		}
		logger.debug("Total Recommended Scenarios -->" + serverFailurePointMap.size());
		return serverFailurePointMap;
	}

	private void findMonkeyStrategyByProcessName(Server server, List<FailurePoint> fpList, String osType, String role) {
		List<MonkeyStrategy> monkeydefaultSoftwareList = monkeyStrategyRepository
				.findByOsTypeAndDefaultFlagAndGenericAndFlavorAndFailureCategoryAndFailureSubCategory(osType, "Y", "Y",
						"all", SOFTWARECAT, "all");
		for (SoftwareComponent softwareComponent : server.getSwComps()) {
			String processName = softwareComponent.getProcessName();
			String componentName = softwareComponent.getServerComponentName();
			if (null != processName && !"".equalsIgnoreCase(processName)) {
				processName = processName.toLowerCase();
				componentName = componentName != null ? componentName.toLowerCase() : componentName;
				fpList.addAll(setFailurePointMetaData(monkeydefaultSoftwareList, processName, componentName, role));
				List<MonkeyStrategy> monkeyProcessList = monkeyStrategyRepository
						.findByOsTypeAndDefaultFlagAndGenericAndFlavorAndFailureCategoryAndFailureSubCategory(osType,
								"Y", "Y", "all", SOFTWARECAT, processName);
				fpList.addAll(setFailurePointMetaData(monkeyProcessList, processName, componentName, role));
			}
		}
	}

	private List<FailurePoint> setFailurePointMetaData(List<MonkeyStrategy> monkeyList, String processName,
			String componentName, String role) {
		FailurePoint failurePoint;
		List<FailurePoint> fpList = new ArrayList<>();
		for (MonkeyStrategy monkeyStrategy : monkeyList) {
			failurePoint = new FailurePoint();
			failurePoint.setRole(role);
			failurePoint.setCategory(APPCAT);
			failurePoint.setFailureMode(monkeyStrategy.getMonkeyStrategyName());
			if (MONEKYTYPE.equalsIgnoreCase(monkeyStrategy.getMonkeyType().monkeyType())) {
				failurePoint.setFailureTenet(FAILURETENET);
			} else {
				failurePoint.setFailureTenet(monkeyStrategy.getMonkeyType().monkeyType());
			}
			failurePoint.setOsType(monkeyStrategy.getOsType());
			failurePoint.setFlavor(monkeyStrategy.getFlavor());
			failurePoint.setFailureCategory(monkeyStrategy.getFailureCategory());
			failurePoint.setFailureSubCategory(monkeyStrategy.getFailureSubCategory());
			failurePoint.setMonkeyType(monkeyStrategy.getMonkeyType());
			failurePoint.setMonkeyStrategy(monkeyStrategy.getMonkeyStrategyName());

			if (null != processName) {
				failurePoint.setProcessName(processName);
			}
			if (null != componentName) {
				failurePoint.setComponent(componentName);
			}

			fpList.add(failurePoint);
		}
		return fpList;
	}

	@Override
	public Iterable<MonkeyStrategy> findAllMonkeyStrategies() {
		return monkeyStrategyRepository.findAll();
	}
}
