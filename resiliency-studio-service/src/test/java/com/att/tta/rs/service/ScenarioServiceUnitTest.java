
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.att.tta.rs.data.es.repository.EventRepository;
import com.att.tta.rs.data.es.repository.ScenarioRepository;
import com.att.tta.rs.model.EventRecorder;
import com.att.tta.rs.model.EventStatus;
import com.att.tta.rs.model.MonkeyType;
import com.att.tta.rs.model.Scenario;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
public class ScenarioServiceUnitTest {

	private static final Logger logger = LoggerFactory.getLogger(ScenarioServiceUnitTest.class);

	@InjectMocks
	ScenarioServiceImpl scenarioService;

	@Mock
	private ScenarioRepository scenarioRepository;

	@Mock
	EventRepository eventRepository;

	private static final String TEAMNAME = "TEST";
	private static final String ALPHATEAMNAME = "alpha";
	private static final String SCENARIONAME = "TestScenario";

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * This Test method validates the Successful insertion of Scenario Object
	 * into Elastic Search.
	 * 
	 */
	@Test
	public void testCreateScenario() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		when(scenarioRepository.findScenarioByNameAndTeamName(scenario.getName(), scenario.getTeamName()))
				.thenReturn(null);
		when(scenarioRepository.save(scenario)).thenReturn(scenario);

		Scenario insertedScenario = scenarioService.insert(scenario, TEAMNAME);
		assertEquals(SCENARIONAME, insertedScenario.getName());
		assertEquals(MonkeyType.CHAOS, insertedScenario.getMonkeyType());
		assertEquals(TEAMNAME, insertedScenario.getTeamName());

		Scenario alphaScenario = createScenario(SCENARIONAME, ALPHATEAMNAME);
		when(scenarioRepository.findScenarioByName(alphaScenario.getName())).thenReturn(null);
		when(scenarioRepository.save(alphaScenario)).thenReturn(alphaScenario);

		insertedScenario = scenarioService.insert(alphaScenario, ALPHATEAMNAME);
		assertEquals(SCENARIONAME, insertedScenario.getName());
		assertEquals(MonkeyType.CHAOS, insertedScenario.getMonkeyType());
		assertEquals(ALPHATEAMNAME, insertedScenario.getTeamName());
	}

	/**
	 * This Test method validates the error condition on insertion of Scenario
	 * Object with same existing Scenario name.
	 * 
	 */
	@Test
	public void testDuplicateCreateScenario() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		when(scenarioRepository.findScenarioByNameAndTeamName(scenario.getName(), scenario.getTeamName()))
				.thenReturn(scenario);

		Scenario insertedScenario = scenarioService.insert(scenario, TEAMNAME);
		assertNull("Scenario already exists with name " + scenario.getName(), insertedScenario);
	}

	/**
	 * This Test method validates the Successful Update of Scenario Object into
	 * Elastic Search.
	 * 
	 */
	@Test
	public void testUpdateScenario() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		when(scenarioRepository.findOne(scenario.getId())).thenReturn(scenario);
		when(scenarioRepository.save(scenario)).thenReturn(scenario);

		Scenario updateScenario = scenarioService.update(scenario, TEAMNAME);
		assertEquals(SCENARIONAME, updateScenario.getName());
		assertEquals(MonkeyType.CHAOS, updateScenario.getMonkeyType());
		assertEquals(TEAMNAME, updateScenario.getTeamName());
	}

	/**
	 * This Test method validates the error condition of update of non-exist
	 * Scenario Object
	 * 
	 */
	@Test
	public void testUpdateScenarioErrCndtn() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		when(scenarioRepository.findOne(scenario.getId())).thenReturn(null);

		Scenario updatedScenario = scenarioService.insert(scenario, TEAMNAME);
		assertNull("Scenario doesnt not exist with name " + scenario.getName(), updatedScenario);
	}

	/**
	 * This Test method validates the Successful Deletion of Scenario Object
	 * from Elastic Search.
	 * 
	 */
	@Test
	public void testDeleteScenario() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		scenarioService.delete(scenario);
		verify(scenarioRepository, times(1)).delete(scenario);
	}

	/**
	 * This Test method validates the Successful Deletion of Scenarios Objects
	 * from Elastic Search.
	 * 
	 */
	@Test
	public void testDeleteAllScenarios() {
		scenarioService.deleteAllScenarios();
		verify(scenarioRepository, times(1)).deleteAll();
	}

	/**
	 * This Test method validates the count method.
	 * 
	 */
	@Test
	public void testCount() {
		when(scenarioRepository.count()).thenReturn((long) 2);
		long sceCount = scenarioService.count();
		assertEquals(2, sceCount);
	}

	/**
	 * This Test method validates the countByTeamName method.
	 * 
	 */
	@Test
	public void testCountByTeamName() {
		when(scenarioRepository.count()).thenReturn((long) 2);
		when(scenarioRepository.countByTeamName(TEAMNAME)).thenReturn((long) 1);
		long sceCount = scenarioService.countByTeamName(TEAMNAME);
		long alphaSceCount = scenarioService.countByTeamName(ALPHATEAMNAME);
		assertEquals(1, sceCount);
		assertEquals(2, alphaSceCount);
	}

	/**
	 * This Test method validates the FindAll method.
	 * 
	 */
	@Test
	public void testFindAll() {
		List<Scenario> sceList = new ArrayList<>();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		sceList.add(scenario);
		Page<Scenario> scePage = new PageImpl<>(sceList);

		when(scenarioRepository.findAll()).thenReturn(scePage);
		List<Scenario> scenarios = Lists.newArrayList(scenarioService.findAll());
		assertEquals(1, scenarios.size());
		assertEquals(MonkeyType.CHAOS, scenarios.get(0).getMonkeyType());
		assertEquals(TEAMNAME, scenarios.get(0).getTeamName());
	}

	/**
	 * This Test method validates the findScenarioListByScenarioNameByTeamName
	 * method.
	 * 
	 */
	@Test
	public void testFindScenarioListByScenarioNameByTeamName() {
		List<Scenario> sceList = new ArrayList<>();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		sceList.add(scenario);
		Page<Scenario> scePage = new PageImpl<>(sceList);

		when(scenarioRepository.findScenariosByNameAndTeamName(scenario.getName(), scenario.getTeamName(),
				new PageRequest(0, 9999))).thenReturn(scePage);

		List<Scenario> scenarios = Lists.newArrayList(scenarioService.findScenarioListByScenarioNameByTeamName(
				scenario.getName(), scenario.getTeamName(), new PageRequest(0, 9999)));
		assertEquals(1, scenarios.size());
		assertEquals(MonkeyType.CHAOS, scenarios.get(0).getMonkeyType());
		assertEquals(TEAMNAME, scenarios.get(0).getTeamName());

		Scenario alphaScenario = createScenario(SCENARIONAME, ALPHATEAMNAME);
		sceList.add(alphaScenario);
		scePage = new PageImpl<>(sceList);

		when(scenarioRepository.findScenariosByName(scenario.getName(), new PageRequest(0, 9999))).thenReturn(scePage);
		scenarios = Lists.newArrayList(scenarioService.findScenarioListByScenarioNameByTeamName(alphaScenario.getName(),
				alphaScenario.getTeamName(), new PageRequest(0, 9999)));
		assertEquals(2, scenarios.size());
		assertEquals(MonkeyType.CHAOS, scenarios.get(1).getMonkeyType());
		assertEquals(ALPHATEAMNAME, scenarios.get(1).getTeamName());
	}

	/**
	 * This Test method validates the findByScenarioNameByTeamName method.
	 * 
	 */
	@Test
	public void testFindByScenarioNameByTeamName() {
		List<Scenario> sceList = new ArrayList<>();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		sceList.add(scenario);
		Page<Scenario> scePage = new PageImpl<>(sceList);

		when(scenarioRepository.findScenariosByNameAndTeamName(scenario.getName(), scenario.getTeamName(),
				new PageRequest(0, 9999))).thenReturn(scePage);

		List<Scenario> scenarios = Lists.newArrayList(scenarioService.findByScenarioNameByTeamName(scenario.getName(),
				scenario.getTeamName(), new PageRequest(0, 9999)));
		assertEquals(1, scenarios.size());
		assertEquals(MonkeyType.CHAOS, scenarios.get(0).getMonkeyType());
		assertEquals(TEAMNAME, scenarios.get(0).getTeamName());

		Scenario alphaScenario = createScenario(SCENARIONAME, ALPHATEAMNAME);
		sceList.add(alphaScenario);
		scePage = new PageImpl<>(sceList);

		when(scenarioRepository.findByName(alphaScenario.getName(), new PageRequest(0, 9999))).thenReturn(scePage);
		scenarios = Lists.newArrayList(scenarioService.findByScenarioNameByTeamName(alphaScenario.getName(),
				alphaScenario.getTeamName(), new PageRequest(0, 9999)));
		assertEquals(2, scenarios.size());
		assertEquals(MonkeyType.CHAOS, scenarios.get(1).getMonkeyType());
		assertEquals(ALPHATEAMNAME, scenarios.get(1).getTeamName());
	}

	/**
	 * This Test method validates the findByApplicationNameByTeamName method.
	 * 
	 */
	@Test
	public void testFindByApplicationNameByTeamName() {
		List<Scenario> sceList = new ArrayList<>();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		sceList.add(scenario);
		Page<Scenario> scePage = new PageImpl<>(sceList);

		when(scenarioRepository.findByApplicationNameAndTeamName(scenario.getApplicationName(), TEAMNAME,
				new PageRequest(0, 9999))).thenReturn(scePage);
		List<Scenario> scenarios = Lists
				.newArrayList(scenarioService.findByApplicationNameByTeamName(scenario.getApplicationName(), TEAMNAME));
		assertEquals(1, scenarios.size());
		assertEquals(MonkeyType.CHAOS, scenarios.get(0).getMonkeyType());
		assertEquals(TEAMNAME, scenarios.get(0).getTeamName());
	}

	/**
	 * This Test method validates the CreateEvent method.
	 * 
	 */
	@Test
	public void testCreateEvent() {
		String statusString = "statusString";
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		EventRecorder event = createEvent(scenario, statusString);
		when(eventRepository.save(event)).thenReturn(event);

		EventRecorder eventRecorder = scenarioService.createEvent(scenario, statusString, scenario.getTeamName());
		assertNull("Event Object is null.", eventRecorder);
	}

	/**
	 * This Test method validates the RecordEvent method.
	 * 
	 */
	@Test
	public void testRecordEvent() {
		String statusString = "updatedStatusString";
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		EventRecorder event = createEvent(scenario, statusString);
		when(eventRepository.findOneForTeam(event.getId(), event.getTeamName())).thenReturn(event);
		when(eventRepository.save(event)).thenReturn(event);

		EventRecorder eventRecorder = scenarioService.recordEvent(event.getId(), statusString, EventStatus.SUBMITTED,
				TEAMNAME);
		assertEquals(EventStatus.SUBMITTED, eventRecorder.getEventStatusType());
		assertEquals(event.getTeamName(), eventRecorder.getTeamName());
	}

	/**
	 * This Test method validates the FinalizeEvent method.
	 * 
	 */
	@Test
	public void testFinalizeEvent() {
		String statusString = "finalStatusString";
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		EventRecorder event = createEvent(scenario, statusString);
		when(eventRepository.findOneForTeam(event.getId(), event.getTeamName())).thenReturn(event);
		when(eventRepository.save(event)).thenReturn(event);

		EventRecorder eventRecorder = scenarioService.finalizeEvent(event.getId(), statusString, EventStatus.COMPLETED,
				TEAMNAME);
		
		assertEquals(EventStatus.COMPLETED, eventRecorder.getEventStatusType());
		assertEquals(event.getTeamName(), eventRecorder.getTeamName());
	}

	/**
	 * This method returns a Scenario Object.
	 * 
	 * @return Scenario
	 */
	private static Scenario createScenario(String scenarioName, String teamName) {
		Scenario obj = new Scenario();
		obj.setId("testScenarioID");
		obj.setName(scenarioName);
		obj.setTeamName(teamName);

		obj.setMonkeyStrategy("sceMS");
		obj.setMonkeyType(MonkeyType.CHAOS);

		obj.setApplicationName("sceAppName");
		obj.setEnvironmentName("QA");
		return obj;
	}

	/**
	 * This method returns a EventRecorder Object.
	 * 
	 * @return EventRecorder
	 */
	private EventRecorder createEvent(Scenario scenario, String statusString) {
		EventRecorder evt = new EventRecorder();
		Map<String, String> map = new HashMap<>();
		try {
			for (Field field : scenario.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Object value = field.get(scenario);
				if (value != null) {
					map.put(field.getName(), value.toString());
				}
			}
			org.joda.time.DateTime dt = new org.joda.time.DateTime();
			map.put("@timestamp", dt.toString());
			map.put("status", statusString);

			evt.setId("evetID");
			evt.setEventStatus(statusString);
			evt.addFields(map);
			evt.setTeamName(TEAMNAME);
			evt.setEventStatusType(EventStatus.SUBMITTED);
		} catch (Exception e) {
			logger.error("Failed to Create event Object" + e);
		}
		return evt;
	}
}