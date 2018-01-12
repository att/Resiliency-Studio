
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
import java.util.HashMap;
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

import com.att.tta.rs.data.es.repository.ApplicationRepository;
import com.att.tta.rs.data.es.repository.MonkeyStrategyRepository;
import com.att.tta.rs.data.es.repository.TeamUserRepository;
import com.att.tta.rs.model.Application;
import com.att.tta.rs.model.Environment;
import com.att.tta.rs.model.FailurePoint;
import com.att.tta.rs.model.MonkeyStrategy;
import com.att.tta.rs.model.MonkeyType;
import com.att.tta.rs.model.Server;
import com.att.tta.rs.model.SoftwareComponent;
import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.ApplicationServiceImpl;
import com.att.tta.rs.service.MonkeyStrategyServiceImpl;
import com.att.tta.rs.service.TeamUserServiceImpl;
import com.att.tta.rs.util.MessageWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { com.att.tta.rs.Application.class })
public class MonkeyStrategyRestControllerTest {

	@Mock
	private MonkeyStrategyRepository monkeyStrategyRepository;

	@Mock
	private ApplicationRepository applicationRepository;

	@Mock
	private TeamUserRepository teamUserRepository;

	@InjectMocks
	MonkeyStrategyServiceImpl monkeyStrategyService;

	@InjectMocks
	TeamUserServiceImpl userDetailsService;

	@InjectMocks
	ApplicationServiceImpl appService;

	@InjectMocks
	MonkeyStrategyRestController monkeyStrategyController;

	@Autowired
	HttpServletRequest req;

	private static final String TEAMNAME = "TEST";
	private static final String MONKEYSTRATEGYNAME = "TestMonkeyStrategy";
	private static final String ALPHATEAMNAME = "alpha";
	private static final String USERNAME = "TestUser";
	private static final String HARDWAREFAILURECAT = "hardware";
	private static final String SOFTWARECAT = "software";
	private static final String TESTMSID = "testMSID";
	private static final String OTHERTEAM = "otherTeam";
	private static final String WEBSERVER = "WebServer1";
	
	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		monkeyStrategyController.monkeyStrategyService = monkeyStrategyService;
		monkeyStrategyController.userDetailsService = userDetailsService;
		monkeyStrategyController.applicationService = appService;
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
	 * This Test method validates listAllMonkeyStrategies method.
	 * 
	 */
	@Test
	public void testListAllMonkeyStrategies() {

		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
		monkeyStrategies.add(monkeyStrategy);

		when(monkeyStrategyRepository.findAll()).thenReturn(monkeyStrategies);

		@SuppressWarnings("unchecked")
		List<MonkeyStrategy> monkeyStrategiesList = (List<MonkeyStrategy>) monkeyStrategyController
				.listAllMonkeyStrategies().getBody();

		assertEquals(1, monkeyStrategiesList.size());
		assertEquals(MONKEYSTRATEGYNAME, monkeyStrategiesList.get(0).getMonkeyStrategyName());
		assertEquals(TEAMNAME, monkeyStrategiesList.get(0).getTeamName());
	}

	/**
	 * This Test method validates listAllMonkeyStrategies method when no
	 * application present..
	 * 
	 */
	@Test
	public void testListAllMonkeyStrategiesErrCond() {
		List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
		when(monkeyStrategyRepository.findAll()).thenReturn(monkeyStrategies);

		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController.listAllMonkeyStrategies().getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals(" No monkeyStrategies found ", apiError.getStatusMessage());
	}

	/**
	 * This Test method validates listAllMonkeyStrategy method to validate the
	 * Application list for given team.
	 * 
	 */
	@Test
	public void testListAllMonkeyStrategy() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
		monkeyStrategies.add(monkeyStrategy);
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategies);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByTeamName(TEAMNAME, new PageRequest(0, 9999)))
				.thenReturn(monkeyStrategyPage);

		@SuppressWarnings("unchecked")
		List<MonkeyStrategy> msList = (List<MonkeyStrategy>) monkeyStrategyController.listAllMonkeyStrategy(req)
				.getBody();
		assertEquals(1, msList.size());
		assertEquals(MONKEYSTRATEGYNAME, msList.get(0).getMonkeyStrategyName());
		assertEquals(TEAMNAME, msList.get(0).getTeamName());

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);
		monkeyStrategies.add(createMonkeyStrategy("TestSecondMonkeyStrategy", "TEST1"));
		when(monkeyStrategyRepository.findAll()).thenReturn(monkeyStrategies);

		@SuppressWarnings("unchecked")
		List<MonkeyStrategy> alphaMSList = (List<MonkeyStrategy>) monkeyStrategyController.listAllMonkeyStrategy(req)
				.getBody();
		assertEquals(2, alphaMSList.size());
		assertEquals(MONKEYSTRATEGYNAME, alphaMSList.get(0).getMonkeyStrategyName());
		assertEquals(TEAMNAME, alphaMSList.get(0).getTeamName());
	}

	/**
	 * This Test method validates ListAllMonkeyStrategy method to validate the
	 * Application list for given team when no Application present.
	 * 
	 */
	@Test
	public void testListAllMonkeyStrategyErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategies);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByTeamName(TEAMNAME, new PageRequest(0, 9999)))
				.thenReturn(monkeyStrategyPage);

		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController.listAllMonkeyStrategy(req).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("No monkeyStrategies found for Team " + TEAMNAME, apiError.getStatusMessage());
	}

	/**
	 * This Test method validates listAllMonkeyTypes method.
	 * 
	 */
	@Test
	public void testListAllMonkeyTypes() {
		@SuppressWarnings("unchecked")
		List<String> monkeyTypeList = (List<String>) monkeyStrategyController.listAllMonkeyTypes().getBody();

		assertEquals(8, monkeyTypeList.size());
		assertEquals(MonkeyType.CHAOS, monkeyTypeList.get(0));
	}

	/**
	 * This Test method validates getMonkeyStrategy method to validate the
	 * Monkey Strategy Object for given Monkey Strategy ID.
	 * 
	 */
	@Test
	public void testGetMonkeyStrategy() {
		setSecuirtyContext(TEAMNAME, USERNAME);

		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findOne(monkeyStrategy.getId())).thenReturn(monkeyStrategy);

		MonkeyStrategy getMonkeyStrategy = (MonkeyStrategy) monkeyStrategyController
				.getMonkeyStrategy(req, monkeyStrategy.getId()).getBody();
		assertEquals(TESTMSID, getMonkeyStrategy.getId());
		assertEquals(MONKEYSTRATEGYNAME, getMonkeyStrategy.getMonkeyStrategyName());
		assertEquals(TEAMNAME, getMonkeyStrategy.getTeamName());

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);

		getMonkeyStrategy = (MonkeyStrategy) monkeyStrategyController.getMonkeyStrategy(req, monkeyStrategy.getId())
				.getBody();
		assertEquals(TESTMSID, getMonkeyStrategy.getId());
		assertEquals(MONKEYSTRATEGYNAME, getMonkeyStrategy.getMonkeyStrategyName());
		assertEquals(TEAMNAME, getMonkeyStrategy.getTeamName());
	}

	/**
	 * This Test method validates error condition of GetMonkeyStrategy method
	 * when there is no Monkey Strategy exist for given MS ID.
	 * 
	 */
	@Test
	public void testGetMonkeyStrategyErrCndtn() {
		String msID = "notExistMSID";
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findOne(msID)).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController.getMonkeyStrategy(req, msID).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("MonkeyStrategy not found for id " + msID, apiError.getStatusMessage());

		/*
		 * This condition is - when Monkey Strategy found with given ID but team
		 * name is different.
		 */
		teamUser.setTeamName(OTHERTEAM);
		setSecuirtyContext(OTHERTEAM, USERNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		when(monkeyStrategyRepository.findOne(monkeyStrategy.getId())).thenReturn(monkeyStrategy);

		apiError = (MessageWrapper) monkeyStrategyController.getMonkeyStrategy(req, monkeyStrategy.getId()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("MonkeyStrategy not found for id " + monkeyStrategy.getId(), apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getMonkeyStrategyByName method to get the
	 * Monkey Strategy Object for given Monkey Strategy Name.
	 * 
	 */
	@Test
	public void testGetMonkeyStrategyByName() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByMonkeyStrategyNameAndTeamName(monkeyStrategy.getMonkeyStrategyName(),
				TEAMNAME)).thenReturn(monkeyStrategy);

		MonkeyStrategy getMonkeyStrategy = (MonkeyStrategy) monkeyStrategyController
				.getMonkeyStrategyByName(req, monkeyStrategy.getMonkeyStrategyName()).getBody();
		assertEquals(MONKEYSTRATEGYNAME, getMonkeyStrategy.getMonkeyStrategyName());
		assertEquals(TEAMNAME, getMonkeyStrategy.getTeamName());
	}

	/**
	 * This Test method validates error condition of GetMonkeyStrategyByName
	 * method when there is no Monkey Strategy exist for given Monkey Strategy
	 * Name.
	 * 
	 */
	@Test
	public void testGetMonkeyStrategyByNameErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByMonkeyStrategyNameAndTeamName(MONKEYSTRATEGYNAME, TEAMNAME))
				.thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController
				.getMonkeyStrategyByName(req, MONKEYSTRATEGYNAME).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Monkey Strategy not found for Team : " + MONKEYSTRATEGYNAME + ", " + TEAMNAME,
				apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getDefaultMonkeyStrategy method to get the
	 * Default Monkey Strategy Object for given Monkey Strategy Name and Monkey
	 * Type.
	 * 
	 */
	@Test
	public void testGetDefaultMonkeyStrategy() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
		monkeyStrategies.add(monkeyStrategy);
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategies);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndDefaultFlagAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), MONKEYSTRATEGYNAME, "Y", TEAMNAME))
						.thenReturn(monkeyStrategyPage);

		MonkeyStrategy getMonkeyStrategy = (MonkeyStrategy) monkeyStrategyController.getDefaultMonkeyStrategy(req,
				monkeyStrategy.getMonkeyStrategyName(), monkeyStrategy.getMonkeyType().monkeyType()).getBody();
		assertEquals(MONKEYSTRATEGYNAME, getMonkeyStrategy.getMonkeyStrategyName());
		assertEquals(TEAMNAME, getMonkeyStrategy.getTeamName());
	}

	/**
	 * This Test method validates error condition of GetDefaultMonkeyStrategy
	 * method when there is no default Monkey Strategy exist for given Monkey
	 * Strategy Name and Monkey Type.
	 * 
	 */
	@Test
	public void testGetDefaultMonkeyStrategyErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndDefaultFlagAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), MONKEYSTRATEGYNAME, "Y", TEAMNAME)).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController
				.getDefaultMonkeyStrategy(req, MONKEYSTRATEGYNAME, monkeyStrategy.getMonkeyType().monkeyType())
				.getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Default Monkey Strategy not found with name & monkeyType : " + MONKEYSTRATEGYNAME + " ,"
				+ monkeyStrategy.getMonkeyType() + " for Team " + TEAMNAME, apiError.getStatusMessage());
	}

	/**
	 * This Test method validates GetMonkeyStrategyByNameAndVersion method to
	 * get the Default Monkey Strategy Object for given Monkey Strategy Name and
	 * Monkey Version.
	 * 
	 */
	@Test
	public void testGetMonkeyStrategyByNameAndVersion() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByMonkeyStrategyNameAndTeamNameAndMonkeyStrategyVersion(MONKEYSTRATEGYNAME,
				TEAMNAME, monkeyStrategy.getMonkeyStrategyVersion())).thenReturn(monkeyStrategy);

		MonkeyStrategy getMonkeyStrategy = (MonkeyStrategy) monkeyStrategyController.getMonkeyStrategyByNameAndVersion(
				req, monkeyStrategy.getMonkeyStrategyName(), monkeyStrategy.getMonkeyStrategyVersion()).getBody();
		assertEquals(MONKEYSTRATEGYNAME, getMonkeyStrategy.getMonkeyStrategyName());
		assertEquals(TEAMNAME, getMonkeyStrategy.getTeamName());
	}

	/**
	 * This Test method validates error condition of
	 * GetMonkeyStrategyByNameAndVersion method when there is no Monkey Strategy
	 * exist for given Monkey Strategy Name and Monkey Version.
	 * 
	 */
	@Test
	public void testGetMonkeyStrategyByNameAndVersionErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByMonkeyStrategyNameAndTeamNameAndMonkeyStrategyVersion(MONKEYSTRATEGYNAME,
				TEAMNAME, monkeyStrategy.getMonkeyStrategyVersion())).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController
				.getMonkeyStrategyByNameAndVersion(req, MONKEYSTRATEGYNAME, monkeyStrategy.getMonkeyStrategyVersion())
				.getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("monkeyStrategy " + MONKEYSTRATEGYNAME + " and verion " + monkeyStrategy.getMonkeyStrategyVersion()
				+ " not found " + " for Team " + TEAMNAME, apiError.getStatusMessage());
	}

	/**
	 * This Test method validates CountByTeamName method to validate the Count
	 * of team.
	 * 
	 */
	@Test
	public void testCountByTeamName() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(monkeyStrategyRepository.countByTeamName(TEAMNAME)).thenReturn((long) 1);
		when(monkeyStrategyRepository.count()).thenReturn((long) 2);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);

		String appCount = (String) monkeyStrategyController.countByTeamName(req).getBody();
		assertEquals("1", appCount);

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);

		appCount = (String) monkeyStrategyController.countByTeamName(req).getBody();
		assertEquals("2", appCount);
	}

	/**
	 * This test method validates addMonkeyStrategy error condition when
	 * MonkeyStrategy Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testAddMonkeyStrategyErrCndtn() {
		MonkeyStrategy monkeyStrategy = null;
		UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController
				.addMonkeyStrategy(req, monkeyStrategy, ucBuilder).getBody();
		assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
		assertEquals("NULL request received to add monkeyStrategy", apiError.getStatusMessage());

		monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		monkeyStrategy.setMonkeyStrategyName("");
		apiError = (MessageWrapper) monkeyStrategyController.addMonkeyStrategy(req, monkeyStrategy, ucBuilder)
				.getBody();
		assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
		assertEquals("Monkey Strategy name is empty", apiError.getStatusMessage());

		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		monkeyStrategy.setDefaultFlag("Y");

		List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
		monkeyStrategies.add(monkeyStrategy);
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategies);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndDefaultFlagAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), MONKEYSTRATEGYNAME, monkeyStrategy.getDefaultFlag(),
				TEAMNAME)).thenReturn(monkeyStrategyPage);

		apiError = (MessageWrapper) monkeyStrategyController.addMonkeyStrategy(req, monkeyStrategy, ucBuilder)
				.getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("MonkeyStrategy with name " + monkeyStrategy.getMonkeyStrategyName() + " already exist",
				apiError.getStatusMessage());
	}

	/**
	 * This test method validates addMonkeyStrategy functionality when valid
	 * MonkeyStrategy Object is passed.
	 * 
	 */
	@Test
	public void testAddMonkeyStrategy() {

		UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();

		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		monkeyStrategy.setDefaultFlag("N");

		List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
		monkeyStrategies.add(monkeyStrategy);
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategies);

		List<MonkeyStrategy> smonkeyStrategies = new ArrayList<>();
		Page<MonkeyStrategy> smonkeyStrategyPage = new PageImpl<>(smonkeyStrategies);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndDefaultFlagAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), MONKEYSTRATEGYNAME, "Y", TEAMNAME))
						.thenReturn(monkeyStrategyPage);
		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), monkeyStrategy.getMonkeyStrategyName(), TEAMNAME))
						.thenReturn(smonkeyStrategyPage);
		when(monkeyStrategyRepository.save(monkeyStrategy)).thenReturn(monkeyStrategy);

		ResponseEntity<Object> response = monkeyStrategyController.addMonkeyStrategy(req, monkeyStrategy, ucBuilder);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	/**
	 * This test method validates updateMonkeyStrategy error condition when
	 * MonkeyStrategy Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testUpdateMonkeyStrategyErrCndtn() {
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		setSecuirtyContext(OTHERTEAM, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, OTHERTEAM);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);

		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController
				.updateMonkeyStrategy(req, monkeyStrategy.getId(), monkeyStrategy).getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("MonkeyStrategy can be changed only by the owning Team:" + monkeyStrategy.getTeamName(),
				apiError.getStatusMessage());

		/*
		 * Validate Error condition when Application object is not present
		 */
		teamUser.setTeamName(TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);

		apiError = (MessageWrapper) monkeyStrategyController
				.updateMonkeyStrategy(req, monkeyStrategy.getId(), monkeyStrategy).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("MonkeyStrategy with id " + monkeyStrategy.getId() + " not found", apiError.getStatusMessage());

		/*
		 * Validate if there is already default monkey strategy exist with same
		 * name.
		 * 
		 */
		monkeyStrategy.setDefaultFlag("Y");

		MonkeyStrategy secondMonkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		secondMonkeyStrategy.setDefaultFlag("Y");
		secondMonkeyStrategy.setId("secondMonkeyStrategyID");
		List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
		monkeyStrategies.add(secondMonkeyStrategy);
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategies);

		when(monkeyStrategyRepository.findOne(monkeyStrategy.getId())).thenReturn(monkeyStrategy);
		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndDefaultFlagAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), MONKEYSTRATEGYNAME, "Y", TEAMNAME))
						.thenReturn(monkeyStrategyPage);

		apiError = (MessageWrapper) monkeyStrategyController
				.updateMonkeyStrategy(req, monkeyStrategy.getId(), monkeyStrategy).getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("Default monkeyStrategy with name " + monkeyStrategy.getMonkeyStrategyName() + " already exist.",
				apiError.getStatusMessage());

	}

	/**
	 * This test method validates updateMonkeyStrategy functionality when valid
	 * MonkeyStrategy Object is passed.
	 * 
	 */
	@Test
	public void testUpdateMonkeyStrategy() {

		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);

		List<MonkeyStrategy> monkeyStrategies = new ArrayList<>();
		monkeyStrategies.add(monkeyStrategy);
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategies);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findOne(monkeyStrategy.getId())).thenReturn(monkeyStrategy);
		when(monkeyStrategyRepository.save(monkeyStrategy)).thenReturn(monkeyStrategy);
		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndDefaultFlagAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), MONKEYSTRATEGYNAME, "Y", TEAMNAME))
						.thenReturn(monkeyStrategyPage);

		ResponseEntity<Object> response = monkeyStrategyController.updateMonkeyStrategy(req, monkeyStrategy.getId(),
				monkeyStrategy);
		MonkeyStrategy getMS = (MonkeyStrategy) response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(TEAMNAME, getMS.getTeamName());
		assertEquals(MONKEYSTRATEGYNAME, getMS.getMonkeyStrategyName());
	}

	/**
	 * This test method validates deletemonkeystrategy error condition when
	 * monkeystrategy Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testDeletemonkeystrategyErrCndtn() {
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		/*
		 * Validate Error condition when Application object is not present
		 */
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findOne(monkeyStrategy.getId())).thenReturn(null);
		setSecuirtyContext(TEAMNAME, USERNAME);

		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController
				.deletemonkeystrategy(req, monkeyStrategy.getId()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Unable to delete as Monkeystrategy with id: " + monkeyStrategy.getId() + " not found",
				apiError.getStatusMessage());
	}

	/**
	 * This test method validates Deletemonkeystrategy functionality when valid
	 * monkeystrategy Object is passed.
	 * 
	 */
	@Test
	public void testDeletemonkeystrategy() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(monkeyStrategyRepository.findOne(monkeyStrategy.getId())).thenReturn(monkeyStrategy);

		ResponseEntity<Object> response = monkeyStrategyController.deletemonkeystrategy(req, monkeyStrategy.getId());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(monkeyStrategyRepository, times(1)).delete(monkeyStrategy);
	}

	/**
	 * This test method validates
	 * getFailurePointByApplicationEnvironmentAndTeamName error condition.
	 * 
	 */
	@Test
	public void testGetFailurePointByApplicationEnvironmentAndTeamNameErrCndtn() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		Application app = new Application();
		app.setTeamName("TEST");
		app.setApplicationName("testApp");

		Environment qaEnvronment = new Environment();
		qaEnvronment.setServerList(new ArrayList<Server>());
		qaEnvronment.setName("QA");
		app.addEnvironment(qaEnvronment);

		/*
		 * This condition validates if no application exists with given App Name
		 */
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findByApplicationNameAndTeamName(app.getApplicationName(), TEAMNAME))
				.thenReturn(null);
		when(applicationRepository.findByApplicationNameAndTeamName(app.getApplicationName(), app.getTeamName(),
				new PageRequest(0, 999))).thenReturn(null);
		MessageWrapper apiError = (MessageWrapper) monkeyStrategyController
				.getFailurePointByApplicationEnvironmentAndTeamName(req, app.getApplicationName(), "QA").getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals(" Application Not found for name " + app.getApplicationName(), apiError.getStatusMessage());

		/*
		 * This condition validates if no environment available in the
		 * application object for given Environment Name
		 */
		when(applicationRepository.findByApplicationNameAndTeamName(app.getApplicationName(), TEAMNAME))
				.thenReturn(app);
		apiError = (MessageWrapper) monkeyStrategyController
				.getFailurePointByApplicationEnvironmentAndTeamName(req, app.getApplicationName(), "QA1").getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Environment QA1 not found for application with name " + app.getApplicationName(),
				apiError.getStatusMessage());

		/*
		 * Validate Error condition when No Monkey Strategy found for given
		 * application name and Environment Name
		 */

		apiError = (MessageWrapper) monkeyStrategyController
				.getFailurePointByApplicationEnvironmentAndTeamName(req, app.getApplicationName(), "QA").getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("No Monkey Strategy found for applicationname " + app.getApplicationName() + "and environment QA",
				apiError.getStatusMessage());
	}

	/**
	 * This test method validates
	 * getFailurePointByApplicationEnvironmentAndTeamName functionality.
	 * 
	 */
	@Test
	public void testGetFailurePointByApplicationEnvironmentAndTeamName() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		setSecuirtyContext(TEAMNAME, USERNAME);

		Application app = createApplication();
		String osType = app.getEnvironment("QA").getServerList().get(0).getOperatingSystem();

		MonkeyStrategy msHW = createMonkeyStrategy("hardwareMonkeyStrategy", TEAMNAME);
		MonkeyStrategy msSW = createMonkeyStrategy("softwareMonkeyStrategy", TEAMNAME);

		List<MonkeyStrategy> msHWList = new ArrayList<>();
		msHWList.add(msHW);

		List<MonkeyStrategy> msSWList = new ArrayList<>();
		msSWList.add(msSW);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findByApplicationNameAndTeamName(app.getApplicationName(), TEAMNAME))
				.thenReturn(app);
		when(monkeyStrategyRepository
				.findByOsTypeAndDefaultFlagAndGenericAndFlavorAndFailureCategoryAndFailureSubCategory(osType, "Y", "Y",
						"all", HARDWAREFAILURECAT, "all")).thenReturn(msHWList);
		when(monkeyStrategyRepository
				.findByOsTypeAndDefaultFlagAndGenericAndFlavorAndFailureCategoryAndFailureSubCategory(osType, "Y", "Y",
						"all", SOFTWARECAT, "all")).thenReturn(msSWList);

		@SuppressWarnings("unchecked")
		HashMap<String, List<FailurePoint>> response = (HashMap<String, List<FailurePoint>>) monkeyStrategyController
				.getFailurePointByApplicationEnvironmentAndTeamName(req, app.getApplicationName(), "QA").getBody();

		assertEquals(true, response.containsKey(WEBSERVER));
		assertEquals(2, response.get(WEBSERVER).size());
		assertEquals("hardwareMonkeyStrategy", response.get(WEBSERVER).get(0).getFailureMode());
		assertEquals("softwareMonkeyStrategy", response.get(WEBSERVER).get(1).getFailureMode());
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

	/**
	 * This method returns a MonkeyStrategy Object.
	 * 
	 * @return
	 */
	private static MonkeyStrategy createMonkeyStrategy(String msName, String teamName) {
		MonkeyStrategy obj = new MonkeyStrategy();
		obj.setId(TESTMSID);
		obj.setMonkeyStrategyName(msName);
		obj.setTeamName(teamName);
		obj.setMonkeyStrategyScript("testmonkeyStrategyScript");
		obj.setMonkeyType(MonkeyType.CHAOS);
		obj.setDefaultFlag("N");
		obj.setMonkeyStrategyVersion("1");
		obj.setScriptTypeCategory("UNIX Script");
		return obj;
	}

	/**
	 * This method returns an Application Object.
	 * 
	 * @return
	 */
	private static Application createApplication() {
		Application application = new Application();
		application.setTeamName("TEST");
		application.setApplicationName("testApp");

		Environment qaEnvronment = new Environment();
		qaEnvronment.setName("QA");

		Server server1 = new Server();
		server1.setHostName("http://qawebserver/");
		server1.setInstanceName(WEBSERVER);
		server1.setOperatingSystem("unix");

		SoftwareComponent swc1 = new SoftwareComponent();
		swc1.setProcessName("httpd");
		swc1.setServerComponentName("web server");

		List<SoftwareComponent> swcList = new ArrayList<>();
		swcList.add(swc1);
		server1.setSwComps(swcList);

		List<Server> webTierServerList = new ArrayList<>();
		webTierServerList.add(server1);
		qaEnvronment.setServerList(webTierServerList);
		application.addEnvironment(qaEnvronment);

		return application;
	}
}
