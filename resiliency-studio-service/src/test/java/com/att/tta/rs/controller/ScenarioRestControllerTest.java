
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
package com.att.tta.rs.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.att.tta.rs.data.es.repository.ScenarioRepository;
import com.att.tta.rs.data.es.repository.TeamUserRepository;
import com.att.tta.rs.model.MonkeyType;
import com.att.tta.rs.model.Scenario;
import com.att.tta.rs.model.ScenarioAdapter;
import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.ApplicationServiceImpl;
import com.att.tta.rs.service.MonkeyStrategyServiceImpl;
import com.att.tta.rs.service.ScenarioServiceImpl;
import com.att.tta.rs.service.TeamUserServiceImpl;
import com.att.tta.rs.util.MessageWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { com.att.tta.rs.Application.class })
public class ScenarioRestControllerTest {

	@Mock
	private ScenarioRepository scenarioRepository;

	@Mock
	private TeamUserRepository teamUserRepository;

	@InjectMocks
	ScenarioServiceImpl scenarioService;

	@InjectMocks
	ApplicationServiceImpl applicationService;

	@InjectMocks
	MonkeyStrategyServiceImpl monkeyStrategyService;

	@InjectMocks
	TeamUserServiceImpl userDetailsService;

	@Autowired
	HttpServletRequest req;

	@InjectMocks
	ScenarioRestController scenarioController;

	private static final String TEAMNAME = "TEST";
	private static final String SCENARIONAME = "TestScenario";
	private static final String ALPHATEAMNAME = "alpha";
	private static final String USERNAME = "TestUser";
	private static final String TESTSCENARIOID = "testScenarioID";
	private static final String NOTFOUND = " not found";
	
	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		scenarioController.monkeyStrategyService = monkeyStrategyService;
		scenarioController.userDetailsService = userDetailsService;
		scenarioController.applicationService = applicationService;
		scenarioController.scenarioService = scenarioService;
	}

	/**
	 * This method set the Security Context with User Object.
	 */
	private void setSecuirtyContext(String teamName, String userName) {
		final List<GrantedAuthority> grantedAuths = new ArrayList<>();
		UserDetails user = new User(userName, teamName, grantedAuths);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
				user.getPassword(), user.getAuthorities());
		SecurityContext ctx = SecurityContextHolder.createEmptyContext();
		ctx.setAuthentication(authentication);
		req.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, ctx);
	}

	/**
	 * This Test method validates listScenarios method.
	 * 
	 */
	@Test
	public void testListScenarios() {

		Scenario sce = createScenario(SCENARIONAME, TEAMNAME);
		List<Scenario> sceList = new ArrayList<>();
		sceList.add(sce);

		when(scenarioRepository.findAll()).thenReturn(sceList);

		List<Scenario> scenarioList = (List<Scenario>) scenarioController.listScenarios().getBody();

		assertEquals(1, scenarioList.size());
		assertEquals(SCENARIONAME, scenarioList.get(0).getName());
		assertEquals(TEAMNAME, scenarioList.get(0).getTeamName());
	}

	/**
	 * This Test method validates ListScenarios method when no application
	 * present..
	 * 
	 */
	@Test
	public void testListScenariosErrCond() {
		List<Scenario> sceList = new ArrayList<>();
		when(scenarioRepository.findAll()).thenReturn(sceList);

		ResponseEntity<List<Scenario>> response = scenarioController.listScenarios();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	/**
	 * This Test method validates listAllScenarios method.
	 * 
	 */
	@Test
	public void testListAllScenarios() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		Scenario sce = createScenario(SCENARIONAME, TEAMNAME);
		List<Scenario> sceList = new ArrayList<>();
		sceList.add(sce);
		Page<Scenario> scePage = new PageImpl<>(sceList);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findAllForTeamName(TEAMNAME, new PageRequest(0, 9999))).thenReturn(scePage);

		List<Scenario> scenarioList = (List<Scenario>) scenarioController.listAllScenarios(req).getBody();

		assertEquals(1, scenarioList.size());
		assertEquals(SCENARIONAME, scenarioList.get(0).getName());
		assertEquals(TEAMNAME, scenarioList.get(0).getTeamName());
	}

	/**
	 * This Test method validates listAllScenarios method when no application
	 * present..
	 * 
	 */
	@Test
	public void testListAllScenariosErrCond() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		List<Scenario> sceList = new ArrayList<>();
		Page<Scenario> scePage = new PageImpl<>(sceList);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findAllForTeamName(TEAMNAME, new PageRequest(0, 9999))).thenReturn(scePage);

		ResponseEntity<List<Scenario>> response = scenarioController.listAllScenarios(req);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	/**
	 * This Test method validates CountByTeamName method to validate the Count
	 * of Scenarios.
	 * 
	 */
	@Test
	public void testCountByTeamName() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(scenarioRepository.countByTeamName(TEAMNAME)).thenReturn((long) 1);
		when(scenarioRepository.count()).thenReturn((long) 2);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);

		String appCount = (String) scenarioController.countByTeamName(req).getBody();
		assertEquals("1", appCount);

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);

		appCount = (String) scenarioController.countByTeamName(req).getBody();
		assertEquals("2", appCount);
	}

	/**
	 * This Test method validates GetScenario method to validate the Scenario
	 * Object for given Scenario ID.
	 * 
	 */
	@Test
	public void testGetScenario() {
		Scenario sce = createScenario(SCENARIONAME, TEAMNAME);
		when(scenarioRepository.findOne(sce.getId())).thenReturn(sce);

		Scenario scenario = (Scenario) scenarioController.getScenario(sce.getId()).getBody();
		assertEquals(TESTSCENARIOID, scenario.getId());
		assertEquals(SCENARIONAME, scenario.getName());
		assertEquals(TEAMNAME, scenario.getTeamName());
	}

	/**
	 * This Test method validates error condition of GetScenario method when
	 * there is no Scenario exist for given Scenario ID.
	 * 
	 */
	@Test
	public void testGetScenarioErrCndtn() {
		String sceID = "notExistSCEID";
		when(scenarioRepository.findOne(sceID)).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) scenarioController.getScenario(sceID).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Scenario not found with id " + sceID, apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getScenarioByName method to validate the
	 * Scenario Object for given Scenario Name.
	 * 
	 */
	@Test
	public void testGetScenarioByName() {
		Scenario sce = createScenario(SCENARIONAME, TEAMNAME);

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findScenarioByNameAndTeamName(sce.getName(), sce.getTeamName())).thenReturn(sce);

		Scenario scenario = (Scenario) scenarioController.getScenarioByName(req, sce.getName()).getBody();
		assertEquals(TESTSCENARIOID, scenario.getId());
		assertEquals(SCENARIONAME, scenario.getName());
		assertEquals(TEAMNAME, scenario.getTeamName());
	}

	/**
	 * This Test method validates error condition of getScenarioByName method
	 * when there is no Scenario exist for given Scenario name.
	 * 
	 */
	@Test
	public void testGetScenarioByNameErrCndtn() {
		Scenario sce = createScenario(SCENARIONAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findScenarioByNameAndTeamName(sce.getName(), sce.getTeamName())).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) scenarioController.getScenarioByName(req, sce.getName()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Scenario  not found with name " + sce.getName(), apiError.getStatusMessage());
	}

	/**
	 * This Test method validates listAllScenariosByApplicationName method to
	 * validate the Scenario Object for given Application Name.
	 * 
	 */
	@Test
	public void testListAllScenariosByApplicationName() {
		Scenario sce = createScenario(SCENARIONAME, TEAMNAME);
		List<Scenario> sceList = new ArrayList<>();
		sceList.add(sce);
		Page<Scenario> scePage = new PageImpl<>(sceList);

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findByApplicationNameAndTeamName(sce.getApplicationName(), sce.getTeamName(),
				new PageRequest(0, 9999))).thenReturn(scePage);

		@SuppressWarnings("unchecked")
		List<Scenario> scenarioList = (List<Scenario>) scenarioController
				.listAllScenariosByApplicationName(req, sce.getApplicationName()).getBody();

		assertEquals(1, scenarioList.size());
		assertEquals(SCENARIONAME, scenarioList.get(0).getName());
		assertEquals(TEAMNAME, scenarioList.get(0).getTeamName());
	}

	/**
	 * This Test method validates error condition of
	 * listAllScenariosByApplicationName method when there is no Scenario exist
	 * for given App name.
	 * 
	 */
	@Test
	public void testListAllScenariosByApplicationNameErrCndtn() {
		Scenario sce = createScenario(SCENARIONAME, TEAMNAME);
		List<Scenario> sceList = new ArrayList<>();
		Page<Scenario> scePage = new PageImpl<>(sceList);

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findByApplicationNameAndTeamName(sce.getApplicationName(), sce.getTeamName(),
				new PageRequest(0, 9999))).thenReturn(scePage);

		MessageWrapper apiError = (MessageWrapper) scenarioController
				.listAllScenariosByApplicationName(req, sce.getApplicationName()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("No Scenarios found under Application " + sce.getApplicationName(), apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getScenarioListByName method to validate the
	 * Scenario Object for given Application Name.
	 * 
	 */
	@Test
	public void testGetScenarioListByName() {
		Scenario sce = createScenario(SCENARIONAME, TEAMNAME);
		List<Scenario> sceList = new ArrayList<>();
		sceList.add(sce);
		Page<Scenario> scePage = new PageImpl<>(sceList);

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findScenariosByNameAndTeamName(sce.getName(), sce.getTeamName(),
				new PageRequest(0, 9999))).thenReturn(scePage);

		@SuppressWarnings("unchecked")
		List<Scenario> scenarioList = (List<Scenario>) scenarioController.getScenarioListByName(req, sce.getName())
				.getBody();

		assertEquals(1, scenarioList.size());
		assertEquals(SCENARIONAME, scenarioList.get(0).getName());
		assertEquals(TEAMNAME, scenarioList.get(0).getTeamName());
	}

	/**
	 * This Test method validates error condition of getScenarioListByName
	 * method when there is no Scenario exist for given Scenario name.
	 * 
	 */
	@Test
	public void testGetScenarioListByNameErrCndtn() {
		Scenario sce = createScenario(SCENARIONAME, TEAMNAME);
		List<Scenario> sceList = new ArrayList<>();
		Page<Scenario> scePage = new PageImpl<>(sceList);

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findScenariosByNameAndTeamName(sce.getName(), sce.getTeamName(),
				new PageRequest(0, 9999))).thenReturn(scePage);

		MessageWrapper apiError = (MessageWrapper) scenarioController.getScenarioListByName(req, sce.getName())
				.getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Scenario with name " + sce.getName() + NOTFOUND, apiError.getStatusMessage());
	}

	/**
	 * This test method validates addScenario error condition when Scenario
	 * Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testaddScenarioErrCndtn() {
		UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findScenarioByNameAndTeamName(scenario.getName(), scenario.getTeamName()))
				.thenReturn(scenario);

		MessageWrapper apiError = (MessageWrapper) scenarioController.addScenario(req, scenario, ucBuilder).getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("A scenario with name " + scenario.getName() + " already exist", apiError.getStatusMessage());
	}

	/**
	 * This test method validates addScenario functionality when valid Scenario
	 * Object is passed.
	 * 
	 */
	@Test
	public void testAddScenario() {

		UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findScenarioByNameAndTeamName(scenario.getName(), scenario.getTeamName()))
				.thenReturn(null);

		ResponseEntity<Object> response = scenarioController.addScenario(req, scenario, ucBuilder);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	/**
	 * This test method validates bulkAddScenario error condition when Scenario
	 * Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testBulkAddScenarioErrCndtn() {
		List<Scenario> sceList = new ArrayList<>();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		sceList.add(scenario);
		ScenarioAdapter sceAdapter = new ScenarioAdapter();
		sceAdapter.setScenarios(sceList);

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findScenarioByNameAndTeamName(scenario.getName(), scenario.getTeamName()))
				.thenReturn(scenario);

		MessageWrapper apiError = (MessageWrapper) scenarioController.bulkaddScenario(req, sceAdapter).getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("Scenarios with same name are already exist for selected application and environment.",
				apiError.getStatusMessage());
	}

	/**
	 * This test method validates bulkaddScenario functionality when valid
	 * Scenario Object is passed.
	 * 
	 */
	@Test
	public void testBulkaddScenario() {

		List<Scenario> sceList = new ArrayList<>();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		sceList.add(scenario);
		ScenarioAdapter sceAdapter = new ScenarioAdapter();
		sceAdapter.setScenarios(sceList);

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findScenarioByNameAndTeamName(scenario.getName(), scenario.getTeamName()))
				.thenReturn(null);

		ResponseEntity<Object> response = scenarioController.bulkaddScenario(req, sceAdapter);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		verify(scenarioRepository, times(1)).save(sceList);
	}

	/**
	 * This test method validates UpdateScenario error condition when Scenario
	 * Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testUpdateScenarioErrCndtn() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		/*
		 * If No Scenario exist with given Scenario ID
		 */
		when(scenarioRepository.findOne(scenario.getId())).thenReturn(null);
		MessageWrapper apiError = (MessageWrapper) scenarioController.updateScenario(req, scenario.getId(), scenario)
				.getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Scenario with id " + scenario.getId() + NOTFOUND, apiError.getStatusMessage());

		/*
		 * If Scenario with same name is already exist.
		 */
		when(scenarioRepository.findOne(scenario.getId())).thenReturn(scenario);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findScenarioByNameAndTeamName(scenario.getName(), scenario.getTeamName()))
				.thenReturn(scenario);

		apiError = (MessageWrapper) scenarioController.updateScenario(req, scenario.getId(), scenario).getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("A scenario with name " + scenario.getName() + " already exist", apiError.getStatusMessage());

		/*
		 * If Scenario is getting update with other team.
		 */
		Scenario scenario1 = createScenario(SCENARIONAME, "TEST1");
		when(scenarioRepository.findOne(scenario.getId())).thenReturn(scenario1);
		when(scenarioRepository.findScenarioByNameAndTeamName(scenario.getName(), scenario.getTeamName()))
				.thenReturn(null);

		apiError = (MessageWrapper) scenarioController.updateScenario(req, scenario.getId(), scenario).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Scenario is not owned by Team " + scenario.getTeamName(), apiError.getStatusMessage());

		/*
		 * When there is an error while updating Scenario Object.
		 */
		scenario = createScenario(SCENARIONAME, TEAMNAME);
		when(scenarioRepository.findOne(scenario.getId())).thenReturn(scenario);
		when(scenarioRepository.save(scenario)).thenReturn(null);
		apiError = (MessageWrapper) scenarioController.updateScenario(req, scenario.getId(), scenario).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Scenario with id " + scenario.getId() + " is not updated", apiError.getStatusMessage());

	}

	/**
	 * This test method validates UpdateScenario functionality when valid
	 * Scenario Object is passed.
	 * 
	 */
	@Test
	public void testUpdateScenario() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findOne(scenario.getId())).thenReturn(scenario);
		when(scenarioRepository.findScenarioByNameAndTeamName(scenario.getName(), scenario.getTeamName()))
				.thenReturn(null);
		when(scenarioRepository.save(scenario)).thenReturn(scenario);

		ResponseEntity<Object> response = scenarioController.updateScenario(req, scenario.getId(), scenario);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * This test method validates DeleteScenario error condition when Scenario
	 * Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testDeleteScenarioErrCndtn() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		when(scenarioRepository.findOne(scenario.getId())).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) scenarioController.deleteScenario(scenario.getId()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Unable to delete. Scenario with id " + scenario.getId() + NOTFOUND,
				apiError.getStatusMessage());
	}

	/**
	 * This test method validates DeleteScenario functionality when valid
	 * Scenario Object is passed.
	 * 
	 */
	@Test
	public void testDeleteScenario() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		when(scenarioRepository.findOne(scenario.getId())).thenReturn(scenario);

		ResponseEntity<Object> response = scenarioController.deleteScenario(scenario.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(scenarioRepository, times(1)).delete(scenario);
	}

	/**
	 * This test method validates deleteScenarioForApplication error condition
	 * when Scenario Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testDeleteScenarioForApplicationErrCndtn() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);

		List<Scenario> sceList = new ArrayList<>();
		Page<Scenario> scePage = new PageImpl<>(sceList);

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findByApplicationNameAndTeamName(scenario.getApplicationName(), scenario.getTeamName(),
				new PageRequest(0, 9999))).thenReturn(scePage);

		MessageWrapper apiError = (MessageWrapper) scenarioController
				.deleteScenarioForApplication(req, scenario.getApplicationName()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Unable to delete. Scenario for application " + scenario.getApplicationName() + NOTFOUND,
				apiError.getStatusMessage());
	}

	/**
	 * This test method validates deleteScenarioForApplication functionality
	 * when valid Scenario Object is passed.
	 * 
	 */
	@Test
	public void testDeleteScenarioForApplication() {
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		List<Scenario> sceList = new ArrayList<>();
		sceList.add(scenario);
		Page<Scenario> scePage = new PageImpl<>(sceList);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(scenarioRepository.findByApplicationNameAndTeamName(scenario.getApplicationName(), scenario.getTeamName(),
				new PageRequest(0, 9999))).thenReturn(scePage);

		ResponseEntity<Object> response = scenarioController.deleteScenarioForApplication(req,
				scenario.getApplicationName());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(scenarioRepository, times(1)).delete(scenario);
	}

	/**
	 * This test method validates deleteAllScenarios functionality when valid
	 * Scenario Object is passed.
	 * 
	 */
	@Test
	public void testDeleteAllScenarios() {
		ResponseEntity<Object> response = scenarioController.deleteAllScenarios();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(scenarioRepository, times(1)).deleteAll();
	}

	/**
	 * This test method validates bulkdeleteScenario error condition when
	 * Scenario Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testBulkDeleteScenarioErrCndtn() {
		List<Scenario> sceList = new ArrayList<>();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		scenario.setId("");
		sceList.add(scenario);
		ScenarioAdapter sceAdapter = new ScenarioAdapter();
		sceAdapter.setScenarios(sceList);

		MessageWrapper apiError = (MessageWrapper) scenarioController.bulkdeleteScenario(sceAdapter).getBody();
		assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
		assertEquals("Scenario Id cannot be empty.", apiError.getStatusMessage());
	}

	/**
	 * This test method validates bulkdeleteScenario functionality when valid
	 * Scenario Object is passed.
	 * 
	 */
	@Test
	public void testBulkDeleteScenario() {

		List<Scenario> sceList = new ArrayList<>();
		Scenario scenario = createScenario(SCENARIONAME, TEAMNAME);
		sceList.add(scenario);
		ScenarioAdapter sceAdapter = new ScenarioAdapter();
		sceAdapter.setScenarios(sceList);

		ResponseEntity<Object> response = scenarioController.bulkdeleteScenario(sceAdapter);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(scenarioRepository, times(1)).delete(scenario);
	}

	/**
	 * This method returns a Scenario Object.
	 * 
	 * @return Scenario
	 */
	private static Scenario createScenario(String scenarioName, String teamName) {
		Scenario obj = new Scenario();
		obj.setId(TESTSCENARIOID);
		obj.setName(scenarioName);
		obj.setTeamName(teamName);

		obj.setMonkeyStrategy("sceMS");
		obj.setMonkeyType(MonkeyType.CHAOS);

		obj.setApplicationName("sceAppName");
		obj.setEnvironmentName("QA");
		return obj;
	}

	/**
	 * This method returns an TeamUser Object.
	 * 
	 * @return
	 */
	private static TeamUser createTeamUserObject(String userName, String teamName) {
		TeamUser teamUser = new TeamUser();
		teamUser.setUserName(userName);
		teamUser.setTeamName(teamName);
		return teamUser;
	}

}
