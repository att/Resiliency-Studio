
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

import org.junit.After;
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
import com.att.tta.rs.data.es.repository.TeamUserRepository;
import com.att.tta.rs.model.Application;
import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.ApplicationServiceImpl;
import com.att.tta.rs.service.TeamUserServiceImpl;
import com.att.tta.rs.util.MessageWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { com.att.tta.rs.Application.class })
public class ApplicationRestControllerTest {

	@Mock
	private ApplicationRepository applicationRepository;

	@Mock
	private TeamUserRepository teamUserRepository;

	@InjectMocks
	ApplicationServiceImpl appService;

	@InjectMocks
	TeamUserServiceImpl userDetailsService;

	@InjectMocks
	ApplicationRestController appController;

	@Autowired
	HttpServletRequest req;

	private static final String TEAMNAME = "TEST";
	private static final String ALPHATEAMNAME = "alpha";
	private static final String APPNAME = "TestApp";
	private static final String USERNAME = "TestUser";
	private static final String APPCATEGORY = "Application";
	private static final String TESTAPPID = "testAppID";
	private static final String OTHERTEAM = "otherTeam";
	
	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		appController.applicationService = appService;
		appController.userDetailsService = userDetailsService;
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
	 * This Test method validates GetAllApplication method.
	 * 
	 */
	@Test
	public void testGetAllApplication() {

		Application app = createApplication(APPNAME, TEAMNAME);
		List<Application> applications = new ArrayList<>();
		applications.add(app);

		when(applicationRepository.findAll()).thenReturn(applications);

		@SuppressWarnings("unchecked")
		List<Application> appList = (List<Application>) appController.getAllApplication().getBody();

		assertEquals(1, appList.size());
		assertEquals(APPNAME, appList.get(0).getApplicationName());
		assertEquals(TEAMNAME, appList.get(0).getTeamName());
	}

	/**
	 * This Test method validates GetAllApplication method when no application
	 * present..
	 * 
	 */
	@Test
	public void testGetAllApplicationErrCond() {
		List<Application> applications = new ArrayList<>();

		when(applicationRepository.findAll()).thenReturn(applications);

		MessageWrapper apiError = (MessageWrapper) appController.getAllApplication().getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals(" No Applications found !!!  ", apiError.getStatusMessage());
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

		when(applicationRepository.countByTeamName(TEAMNAME)).thenReturn((long) 1);
		when(applicationRepository.count()).thenReturn((long) 1);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);

		String appCount = (String) appController.countByTeamName(req).getBody();
		assertEquals("1", appCount);

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);

		appCount = (String) appController.countByTeamName(req).getBody();
		assertEquals("1", appCount);
	}

	/**
	 * This Test method validates getAllApplicationsForTeam method to validate
	 * the Application list for given team.
	 * 
	 */
	@Test
	public void testGetAllApplicationsForTeam() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		Application app = createApplication(APPNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		List<Application> applications = new ArrayList<>();
		applications.add(app);
		Page<Application> appPage = new PageImpl<>(applications);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findByTeamName(TEAMNAME, new PageRequest(0, 9999))).thenReturn(appPage);

		@SuppressWarnings("unchecked")
		List<Application> appList = (List<Application>) appController.getAllApplicationsForTeam(req).getBody();
		assertEquals(1, appList.size());
		assertEquals(APPNAME, appList.get(0).getApplicationName());
		assertEquals(TEAMNAME, appList.get(0).getTeamName());

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);
		applications.add(createApplication("TestSecondApp", "TEST1"));
		when(applicationRepository.findAll()).thenReturn(applications);

		@SuppressWarnings("unchecked")
		List<Application> alphaAppList = (List<Application>) appController.getAllApplicationsForTeam(req).getBody();
		assertEquals(2, alphaAppList.size());
		assertEquals(APPNAME, alphaAppList.get(0).getApplicationName());
		assertEquals(TEAMNAME, alphaAppList.get(0).getTeamName());
	}

	/**
	 * This Test method validates getAllApplicationsForTeam method to validate
	 * the Application list for given team when no Application present.
	 * 
	 */
	@Test
	public void testGetAllApplicationsForTeamErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		List<Application> applications = new ArrayList<>();
		Page<Application> appPage = new PageImpl<>(applications);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findByTeamName(TEAMNAME, new PageRequest(0, 9999))).thenReturn(appPage);

		MessageWrapper apiError = (MessageWrapper) appController.getAllApplicationsForTeam(req).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals(" No Applications found !!! for Team " + TEAMNAME, apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getApplication method to validate the
	 * Application Object for given App ID.
	 * 
	 */
	@Test
	public void testgetApplication() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		Application app = createApplication(APPNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findOneForTeam(app.getId(), TEAMNAME)).thenReturn(app);

		Application getApp = (Application) appController.getApplication(req, app.getId()).getBody();
		assertEquals(TESTAPPID, getApp.getId());
		assertEquals(APPNAME, getApp.getApplicationName());
		assertEquals(TEAMNAME, getApp.getTeamName());

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);
		when(applicationRepository.findOne(app.getId())).thenReturn(app);

		getApp = (Application) appController.getApplication(req, app.getId()).getBody();
		assertEquals(TESTAPPID, getApp.getId());
		assertEquals(APPNAME, getApp.getApplicationName());
		assertEquals(TEAMNAME, getApp.getTeamName());
	}

	/**
	 * This Test method validates error condition of getApplication method when
	 * there is no application exist for given App ID.
	 * 
	 */
	@Test
	public void testgetApplicationErrCndtn() {
		String appID = "notExistAppID";
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findOneForTeam(appID, TEAMNAME)).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) appController.getApplication(req, appID).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Application Not found for id " + appID, apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getApplicationsByName method to validate the
	 * Application Object for given App Name.
	 * 
	 */
	@Test
	public void testGetApplicationsByName() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		Application app = createApplication(APPNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findByApplicationNameAndTeamName(app.getApplicationName(), TEAMNAME))
				.thenReturn(app);

		Application getApp = (Application) appController.getApplicationsByName(req, app.getApplicationName()).getBody();
		assertEquals(APPNAME, getApp.getApplicationName());
		assertEquals(TEAMNAME, getApp.getTeamName());

		List<Application> applications = new ArrayList<>();
		applications.add(app);
		Page<Application> appPage = new PageImpl<>(applications);

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);
		when(applicationRepository.findByApplicationName(app.getApplicationName(), new PageRequest(0, 999)))
				.thenReturn(appPage);

		getApp = (Application) appController.getApplicationsByName(req, app.getApplicationName()).getBody();
		assertEquals(APPNAME, getApp.getApplicationName());
		assertEquals(TEAMNAME, getApp.getTeamName());
	}

	/**
	 * This Test method validates error condition of getApplicationsByName
	 * method when there is no application exist for given App Name.
	 * 
	 */
	@Test
	public void testGetApplicationsByNameErrCndtn() {
		String noExistAppName = "notExistAppName";
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findOneForTeam(noExistAppName, TEAMNAME)).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) appController.getApplicationsByName(req, noExistAppName).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Application Not found with name: " + noExistAppName + " for Team: " + TEAMNAME,
				apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getApplicationByNameAndCategory method to
	 * validate the Application Object for given App Name & Category.
	 * 
	 */
	@Test
	public void testGetApplicationByNameAndCategory() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		Application app = createApplication(APPNAME, TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findByApplicationNameAndCategoryAndTeamName(app.getApplicationName(),
				app.getCategory(), TEAMNAME)).thenReturn(app);

		Application getApp = (Application) appController
				.getApplicationByNameAndCategory(req, app.getApplicationName(), app.getCategory()).getBody();
		assertEquals(APPNAME, getApp.getApplicationName());
		assertEquals(TEAMNAME, getApp.getTeamName());
		assertEquals(APPCATEGORY, getApp.getCategory());
	}

	/**
	 * This Test method validates error condition of
	 * getApplicationByNameAndCategory method when there is no application exist
	 * for given App Name & Category.
	 * 
	 */
	@Test
	public void testGetApplicationByNameAndCategoryErrCndtn() {
		String noExistAppName = "nonExistAppName";
		String category = "nonExistCategory";

		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findByApplicationNameAndCategoryAndTeamName(noExistAppName, category, TEAMNAME))
				.thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) appController.getApplicationByNameAndCategory(req, noExistAppName, category)
				.getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("application with name " + noExistAppName + " and category " + category + " not found " + " for Team "
				+ TEAMNAME, apiError.getStatusMessage());
	}

	/**
	 * This test method validates addApplication error condition when
	 * Application Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testAddApplicationErrCndtn() {
		Application addApp = null;
		UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
		MessageWrapper apiError = (MessageWrapper) appController.addApplication(req, addApp, ucBuilder).getBody();
		assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
		assertEquals("NULL request received to add application", apiError.getStatusMessage());

		addApp = createApplication(APPNAME, TEAMNAME);
		addApp.setApplicationName("");
		apiError = (MessageWrapper) appController.addApplication(req, addApp, ucBuilder).getBody();
		assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
		assertEquals("Application name is empty", apiError.getStatusMessage());

		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		addApp = createApplication(APPNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findByApplicationNameAndTeamName(APPNAME, TEAMNAME)).thenReturn(addApp);

		apiError = (MessageWrapper) appController.addApplication(req, addApp, ucBuilder).getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("Application with name " + addApp.getApplicationName() + " already exist",
				apiError.getStatusMessage());
	}

	/**
	 * This test method validates addApplication functionality when valid
	 * Application Object is passed.
	 * 
	 */
	@Test
	public void testAddApplication() {

		UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();

		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		Application addApp = createApplication(APPNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findByApplicationNameAndTeamName(APPNAME, TEAMNAME)).thenReturn(null);
		when(applicationRepository.save(addApp)).thenReturn(addApp);

		ResponseEntity<Object> response = appController.addApplication(req, addApp, ucBuilder);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	/**
	 * This test method validates updateApplication error condition when
	 * Application Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testUpdateApplicationErrCndtn() {
		Application addApp = createApplication(APPNAME, TEAMNAME);
		setSecuirtyContext(OTHERTEAM, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, OTHERTEAM);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findOneForTeam(addApp.getId(), TEAMNAME)).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) appController.updateApplication(req, addApp.getId(), addApp)
				.getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("Application can be changed only by the owning Team -->" + addApp.getTeamName() + "<--",
				apiError.getStatusMessage());

		/*
		 * Validate Error condition when Application object is not present
		 */
		setSecuirtyContext(TEAMNAME, USERNAME);
		teamUser.setTeamName(TEAMNAME);

		apiError = (MessageWrapper) appController.updateApplication(req, addApp.getId(), addApp).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Application with id " + addApp.getId() + " not found", apiError.getStatusMessage());

	}

	/**
	 * This test method validates updateApplication functionality when valid
	 * Application Object is passed.
	 * 
	 */
	@Test
	public void testUpdateApplication() {

		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		Application addApp = createApplication(APPNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findOneForTeam(addApp.getId(), TEAMNAME)).thenReturn(addApp);
		when(applicationRepository.findOne(addApp.getId())).thenReturn(addApp);
		when(applicationRepository.save(addApp)).thenReturn(addApp);

		ResponseEntity<Object> response = appController.updateApplication(req, addApp.getId(), addApp);
		Application getApp = (Application) response.getBody();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(TEAMNAME, getApp.getTeamName());
		assertEquals(APPNAME, getApp.getApplicationName());
	}

	/**
	 * This test method validates deleteApplication error condition when
	 * Application Object is passed with invalid values.
	 * 
	 */
	@Test
	public void testDeleteApplicationErrCndtn() {
		Application addApp = createApplication(APPNAME, TEAMNAME);
		setSecuirtyContext(OTHERTEAM, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, OTHERTEAM);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findOneForTeam(addApp.getId(), OTHERTEAM)).thenReturn(addApp);

		MessageWrapper apiError = (MessageWrapper) appController.deleteApplication(req, addApp.getId()).getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("Application can be deleted only by the owning Team " + addApp.getTeamName(),
				apiError.getStatusMessage());

		/*
		 * Validate Error condition when Application object is not present
		 */
		when(applicationRepository.findOneForTeam(addApp.getId(), TEAMNAME)).thenReturn(null);
		setSecuirtyContext(TEAMNAME, USERNAME);
		teamUser.setTeamName(TEAMNAME);

		apiError = (MessageWrapper) appController.deleteApplication(req, addApp.getId()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Unable to delete. Application with id " + addApp.getId() + " not found",
				apiError.getStatusMessage());

	}

	/**
	 * This test method validates deleteApplication functionality when valid
	 * Application Object is passed.
	 * 
	 */
	@Test
	public void testdeleteApplication() {

		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		Application addApp = createApplication(APPNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(applicationRepository.findOneForTeam(addApp.getId(), TEAMNAME)).thenReturn(addApp);
		when(applicationRepository.findOne(addApp.getId())).thenReturn(addApp);
		when(applicationRepository.save(addApp)).thenReturn(addApp);

		ResponseEntity<Object> response = appController.deleteApplication(req, addApp.getId());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(applicationRepository, times(1)).delete(addApp);
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
	 * This method returns an Application Object.
	 * 
	 * @return
	 */
	private static Application createApplication(String appName, String teamName) {
		Application app = new Application();
		app.setApplicationName(appName);
		app.setTeamName(teamName);
		app.setId(TESTAPPID);
		app.setCategory(APPCATEGORY);
		return app;
	}

	@After
	public void tearDown() throws Exception {
		/*
		 * This method is for clean up part.
		 */
	}

}
