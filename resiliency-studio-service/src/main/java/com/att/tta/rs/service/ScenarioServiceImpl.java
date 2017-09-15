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

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.tta.rs.data.es.repository.EventRepository;
import com.att.tta.rs.data.es.repository.ScenarioRepository;
import com.att.tta.rs.model.EventRecorder;
import com.att.tta.rs.model.EventStatus;
import com.att.tta.rs.model.EventStatusUpdate;
import com.att.tta.rs.model.Scenario;
import com.att.tta.rs.util.AppUtil;

/**
 * Implementation class for {@link ScenarioService}
 * 
 * @author ak983d
 *
 */

@Service("scenarioService")
@Transactional
public class ScenarioServiceImpl implements ScenarioService {

	private static final Logger logger = LoggerFactory.getLogger(ScenarioServiceImpl.class);

	private ScenarioRepository scenarioRepository;
	private static final String ERROR = "Cannot find event for event Id -->";

	@Autowired
	EventRepository eventRepository;

	@Autowired
	public void setScenarioRepository(ScenarioRepository scenarioRepository) {
		this.scenarioRepository = scenarioRepository;
	}

	@Override
	public Scenario save(Scenario scenario, String teamName) {
		if (this.isScenarioExistForTeamName(scenario, teamName)) {
			return null;
		}
		return scenarioRepository.save(scenario);
	}

	@Override
	public boolean save(List<Scenario> scenarios, String teamName) {
		scenarioRepository.save(scenarios);
		return true;
	}

	@Override
	public Scenario insert(Scenario scenario, String teamName) {
		if (this.isScenarioExistForTeamName(scenario, teamName)) {
			logger.debug("Scenario already exists with name " + scenario.getName());
			return null;
		}
		return save(scenario, teamName);
	}

	@Override
	public Scenario update(Scenario scenario, String teamName) {
		if (scenario != null && !"".equals(scenario.getId().trim())
				&& (scenarioRepository.findOne(scenario.getId()) != null)) {
			return save(scenario, teamName);
		}
		return null;
	}

	@Override
	public void delete(Scenario scenario) {
		scenarioRepository.delete(scenario);
	}

	@Override
	public void deleteAllScenarios() {
		scenarioRepository.deleteAll();
	}

	@Override
	public boolean isScenarioExistForTeamName(Scenario scenario, String teamName) {
		boolean exist = findByScenarioNameByTeamName(scenario.getName(), teamName) != null;
		logger.debug("Scenario with name " + scenario.getName() + " is already exist: " + exist);
		return exist;
	}

	@Override
	public long count() {
		return scenarioRepository.count();
	}

	@Override
	public Long countByTeamName(String teamName) {
		if (teamName.trim().equals(AppUtil.getSuperUser()))
			return scenarioRepository.count();
		else
			return scenarioRepository.countByTeamName(teamName);
	}

	@Override
	public Scenario findOne(String id) {
		return scenarioRepository.findOne(id);
	}

	@Override
	public Iterable<Scenario> findAll() {
		return scenarioRepository.findAll();
	}

	@Override
	public Page<Scenario> findScenarioListByScenarioNameByTeamName(String name, String teamName, Pageable page) {
		if (teamName.trim().equalsIgnoreCase(AppUtil.getSuperUser()))
			return scenarioRepository.findScenariosByName(name, page);
		else
			return scenarioRepository.findScenariosByNameAndTeamName(name, teamName, page);
	}

	@Override
	public Iterable<Scenario> findAllByTeamName(String teamName) {
		if (teamName.trim().equalsIgnoreCase(AppUtil.getSuperUser()))
			return scenarioRepository.findAll();
		else
			return scenarioRepository.findAllForTeamName(teamName, new PageRequest(0, 9999));
	}

	@Override
	public Page<Scenario> findByScenarioNameByTeamName(String name, String teamName, Pageable pageable) {
		if (teamName.trim().equalsIgnoreCase(AppUtil.getSuperUser()))
			return scenarioRepository.findByName(name, pageable);
		else
			return scenarioRepository.findScenariosByNameAndTeamName(name, teamName, pageable);
	}

	@Override
	public Scenario findByScenarioNameByTeamName(String name, String teamName) {
		Scenario scenario;
		if (AppUtil.getSuperUser().equalsIgnoreCase(teamName.trim()))
			scenario = scenarioRepository.findScenarioByName(name);
		else
			scenario = scenarioRepository.findScenarioByNameAndTeamName(name, teamName);
		return scenario;
	}

	@Override
	public List<Scenario> findByApplicationNameByTeamName(String applicationName, String teamName) {
		List<Scenario> scenarioList;
		Page<Scenario> test = scenarioRepository.findByApplicationNameAndTeamName(applicationName, teamName,
				new PageRequest(0, 9999));
		List<Scenario> pageList = test.getContent();
		if (pageList != null && !pageList.isEmpty())
			logger.debug("Total scenarios found after page  query" + pageList.size());
		else
			logger.debug("\n Nothing found in page query");

		scenarioList = pageList;
		return scenarioList;
	}

	@Override
	public EventRecorder createEvent(Scenario execScenario, String statusString, String teamName) {
		EventRecorder evt = new EventRecorder();
		EventRecorder evt2 = null;
		Map<String, String> map = new HashMap<>();
		try {
			for (Field field : execScenario.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(execScenario);
				if (value != null) {
					map.put(field.getName(), value.toString());
				}
			}
			org.joda.time.DateTime dt = new org.joda.time.DateTime();
			map.put("@timestamp", dt.toString());
			map.put("status", statusString);

			evt.addFields(map);
			evt.setTeamName(teamName);
			evt.setEventStatusType(EventStatus.SUBMITTED);
			evt2 = eventRepository.save(evt);
		} catch (Exception e) {
			logger.error("Failed to insert event in elastic search ", e);
		}
		return evt2;
	}

	@Override
	public EventRecorder recordEvent(String eventId, String statusString, EventStatus eventStatus, String teamName) {
		logger.debug("Event with ID: " + eventId + " is updating with Status-->" + statusString);

		EventRecorder evt = eventRepository.findOneForTeam(eventId, teamName);
		if (evt == null) {
			logger.error(ERROR + eventId);
			return null;
		}

		addStatus(evt, statusString);
		evt.setId(eventId);
		evt.setEventStatusType(eventStatus);
		evt = eventRepository.save(evt);
		return evt;
	}

	@Override
	public EventRecorder finalizeEvent(String eventId, String statusString, EventStatus eventStatus, String teamName,
			String execStatus) {
		logger.debug("Event ID: " + eventId + " is updating with final Status-->" + statusString);

		EventRecorder evt = eventRepository.findOneForTeam(eventId, teamName);
		if (evt == null) {
			logger.error(ERROR + eventId);
			return null;
		}

		finalStatus(evt, statusString);
		evt.setId(eventId);
		evt.setEventStatusType(eventStatus);
		evt.setExecStatus(execStatus);
		evt = eventRepository.save(evt);
		return evt;
	}

	@Override
	public EventRecorder finalizeEvent(String eventId, String statusString, EventStatus eventStatus, String teamName) {
		logger.debug("Event: " + eventId + " is updating with final Status-->" + statusString);

		EventRecorder evt = eventRepository.findOneForTeam(eventId, teamName);
		if (evt == null) {
			logger.error(ERROR + eventId);
			return null;
		}

		finalStatus(evt, statusString);
		evt.setId(eventId);
		evt.setEventStatusType(eventStatus);
		evt = eventRepository.save(evt);
		return evt;
	}

	private void addStatus(EventRecorder evt, String value) {
		String timeStampString = String.valueOf(new Date());
		EventStatusUpdate eventStatusUpdate = new EventStatusUpdate(timeStampString, value);
		evt.setEventStatusUpdate(eventStatusUpdate);
		if (evt.getEventStatus() == null) {
			evt.setEventStatus(value);
			evt.setEventStatusUpdate(eventStatusUpdate);
		} else {
			evt.setEventStatus(new StringBuilder().append(evt.getEventStatus()).append("\n").append(timeStampString)
					.append(value).toString());
			evt.setEventStatusUpdate(eventStatusUpdate);
		}
	}

	private void finalStatus(EventRecorder evt, String value) {
		String timeStampString = String.valueOf(new Date());
		EventStatusUpdate eventStatusUpdate = new EventStatusUpdate(timeStampString, value);
		eventStatusUpdate.setStopTryFlag(Boolean.TRUE);
		evt.setEventStatusUpdate(eventStatusUpdate);
		String statusStr;
		String tempStr;
		if (evt.getEventStatus() == null) {
			int maxLength = (value.length() < 32765) ? value.length() : 32765;
			statusStr = value.substring(0, maxLength);
			evt.setEventStatus(statusStr);
			evt.setEventStatusUpdate(eventStatusUpdate);
		} else {
			tempStr = new StringBuilder().append(evt.getEventStatus()).append("\n").append(timeStampString)
					.append(value).toString();
			int maxLength = (tempStr.length() < 32765) ? tempStr.length() : 32765;
			statusStr = tempStr.substring(0, maxLength);
			evt.setEventStatus(statusStr);
			evt.setEventStatusUpdate(eventStatusUpdate);
		}
	}
}
