
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
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import com.att.tta.rs.data.es.repository.ApplicationRepository;
import com.att.tta.rs.model.Application;
import com.att.tta.rs.model.DiscoveryApiElement;
import com.att.tta.rs.model.Environment;
import com.att.tta.rs.model.LogElement;
import com.att.tta.rs.model.MonitorElement;
import com.att.tta.rs.model.Server;
import com.att.tta.rs.model.SoftwareComponent;
import com.google.common.collect.Lists;

import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
public class ApplicationServiceUnitTest {

	@InjectMocks
	ApplicationServiceImpl appService;

	@Mock
	private ApplicationRepository applicationRepository;

	private static final String ALPHATEAMNAME = "alpha";
	
	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * This Test method validates the Successful insertion of Application Object
	 * into Elastic Search.
	 * 
	 */
	@Test
	public void testCreateApplication() {
		String appName = "testCreateApplication";
		Application application = createApplication();
		application.setApplicationName(appName);
		application.setTeamName("TEST");

		when(applicationRepository.findByApplicationNameAndTeamName(application.getApplicationName(),
				application.getTeamName())).thenReturn(null);
		when(applicationRepository.findByApplicationNameAndTeamName(application.getApplicationName(),
				application.getTeamName(), new PageRequest(0, 999))).thenReturn(null);
		when(applicationRepository.save(application)).thenReturn(application);

		Application insertedApp = appService.insertForTeam(application, "TEST");
		assertTrue("Application Successfully Saved.", insertedApp.getApplicationName().equalsIgnoreCase(appName));
		assertEquals("WebServer1", application.getEnvironment("QA").getServerList().get(0).getInstanceName());
	}

	/**
	 * This Test method validates the Successful Deletion of Application Object
	 * from Elastic Search.
	 * 
	 */
	@Test
	public void testDeleteApplication() {
		String appName = "testDeleteApplication";
		Application application = createApplication();
		application.setApplicationName(appName);
		application.setTeamName("TEST");

		appService.delete(application);
		verify(applicationRepository, times(1)).delete(application);
	}

	/**
	 * This Test method validates the Successful Update of Application Object
	 * into Elastic Search.
	 * 
	 */
	@Test
	public void testUpdateApplication() {

		Application application = createApplication();
		application.setApplicationName("testUpdateApplication");
		application.setCategory("Dev_Update");
		application.setRole("Role_Update");
		application.setId("testUpdateApplicationID");

		when(applicationRepository.findOne(application.getId())).thenReturn(application);
		when(applicationRepository.save(application)).thenReturn(application);

		Application updatedApp = appService.update(application);

		assertEquals("Role_Update", updatedApp.getRole());
		assertEquals("Dev_Update", updatedApp.getCategory());
	}

	/**
	 * This Test method validates the Error Condition while updating of
	 * non-exist Application Object into Elastic Search.
	 * 
	 */
	@Test
	public void testUpdateNonExistApplication() {
		Application application = createApplication();
		application.setApplicationName("testUpdateNonExistApplication");
		application.setCategory("Dev");
		application.setRole("Role");
		application.setId("testUpdateNonExistApplicationID");

		when(applicationRepository.findOne(application.getId())).thenReturn(null);

		Application updatedApp = appService.update(application);
		assertNull("Application doesn't exist so not able to update an Application ", updatedApp);
	}

	/**
	 * This Test method validates the error condition on insertion of
	 * Application Object with same existing Application name.
	 * 
	 */
	@Test
	public void testDuplicateApplication() {
		String teamName = ALPHATEAMNAME;
		Application app = createApplication();
		app.setApplicationName("testDuplicateApplication");
		app.setTeamName(teamName);

		List<Application> applicationList = new ArrayList<>();
		applicationList.add(app);
		Page<Application> appPage = new PageImpl<>(applicationList);

		when(applicationRepository.findByApplicationName(app.getApplicationName(), new PageRequest(0, 999)))
				.thenReturn(appPage);

		Application insertedApp = appService.insertForTeam(app, teamName);
		assertNull("Application with duplicate Name  is not allowed.", insertedApp);
	}

	/**
	 * This Test method validates the functionality to get the Application by
	 * given Application Name and Team Name.
	 * 
	 */
	@Test
	public void testApplicationServiceGetByApplicationName() {
		String teamName = "TEST";
		Application application = createApplication();
		application.setApplicationName("testApplicationServiceGetByApplicationName");
		application.setCategory("Dev");
		application.setTeamName(teamName);

		when(applicationRepository.findByApplicationNameAndTeamName(application.getApplicationName(), teamName))
				.thenReturn(application);

		Application getApp = appService.findByApplicationNameAndTeamName(application.getApplicationName(), teamName);
		assertThat(getApp, instanceOf(Application.class));
		assertEquals("testApplicationServiceGetByApplicationName", getApp.getApplicationName());
		assertEquals(teamName, getApp.getTeamName());
		assertEquals("Dev", getApp.getCategory());
	}

	/**
	 * This Test method validates the functionality to get the list of All
	 * Applications.
	 * 
	 */
	@Test
	public void testApplicationServiceGetAllApplications() {

		String teamName = ALPHATEAMNAME;
		Application newApp = createApplication();
		newApp.setApplicationName("testApplicationServiceGetAllApplications");
		newApp.setCategory("Dev");
		newApp.setTeamName(teamName);

		List<Application> applicationList = new ArrayList<>();
		applicationList.add(newApp);

		when(applicationRepository.findAll()).thenReturn(applicationList);
		List<Application> getAppList1 = Lists.newArrayList(appService.findAllForTeam(ALPHATEAMNAME));
		assertNotNull("Application List  isn't null", getAppList1);
		assertEquals("testApplicationServiceGetAllApplications", getAppList1.get(0).getApplicationName());
		assertEquals(ALPHATEAMNAME, getAppList1.get(0).getTeamName());

		teamName = "TEST";
		newApp.setTeamName(teamName);
		Page<Application> appPage = new PageImpl<>(applicationList);
		when(applicationRepository.findByTeamName(teamName, new PageRequest(0, 9999))).thenReturn(appPage);

		List<Application> getAppList = Lists.newArrayList(appService.findAllForTeam(teamName));
		assertNotNull("Application List  isn't null", getAppList);
		assertEquals("testApplicationServiceGetAllApplications", getAppList.get(0).getApplicationName());
		assertEquals("TEST", getAppList1.get(0).getTeamName());
	}

	/**
	 * This Test method validates the functionality to get the Application by
	 * given Application Name, Category and Team Name.
	 * 
	 */
	@Test
	public void testApplicationServiceGetApplicationByNameAndCategory() {

		String teamName = "TEST";
		Application app = createApplication();
		app.setApplicationName("testApplicationServiceGetApplicationByNameAndCategory");
		app.setCategory("Dev");

		when(applicationRepository.findByApplicationNameAndCategoryAndTeamName(app.getApplicationName(),
				app.getCategory(), teamName)).thenReturn(app);

		Application existingApp = appService.findByApplicationNameAndCategoryAndTeamName(
				"testApplicationServiceGetApplicationByNameAndCategory", "Dev", teamName);

		assertNotNull("Application  isn't null", existingApp);
		assertEquals(existingApp.getApplicationName(), "testApplicationServiceGetApplicationByNameAndCategory");
		assertEquals(existingApp.getCategory(), "Dev");
	}

	/**
	 * This method returns an Application Object.
	 * 
	 * @return
	 */
	private static Application createApplication() {
		Application application = new Application();
		application.setTeamName("TEST");

		Environment qaEnvronment = new Environment();
		qaEnvronment.setName("QA");

		Server server1 = new Server();
		server1.setHostName("http://qawebserver/");
		server1.setInstanceName("WebServer1");
		server1.setMemory("8GB");
		server1.setTier("App");
		server1.setIpAddress("127.0.0.1");
		server1.setOperatingSystem("Linux");
		server1.setUserName("you");
		server1.setPassword("yourpassword");

		DiscoveryApiElement disc1 = new DiscoveryApiElement();
		disc1.setDiscoveryApiDescription("Discovery 1");
		disc1.setDiscoveryApi("https://discovery/host?a=m");

		DiscoveryApiElement disc2 = new DiscoveryApiElement();
		disc2.setDiscoveryApiDescription("Discovery 2");
		disc2.setDiscoveryApi("https://discovery/host?b=n");

		List<DiscoveryApiElement> discList = new ArrayList<>();
		discList.add(disc1);
		discList.add(disc2);

		List<String> discStrList = new ArrayList<>();
		discStrList.add("discovery api");

		server1.setDiscoveryApi(discList);

		LogElement log1 = new LogElement();
		log1.setLogLocation("/tmp/server1");
		log1.setLogType("sl4j");

		LogElement log2 = new LogElement();
		log2.setLogLocation("/tmp2/server1");
		log2.setLogType("log4j");

		List<LogElement> logList = new ArrayList<>();
		logList.add(log1);
		logList.add(log2);
		server1.setLog(logList);

		MonitorElement mon1 = new MonitorElement();
		mon1.setCounterType("dummycounter1");
		mon1.setMonitorApi("http://monitorapi");

		MonitorElement mon2 = new MonitorElement();
		mon2.setCounterType("dummycounter2");
		mon2.setMonitorApi("http://monitorapi2");

		List<MonitorElement> monList = new ArrayList<>();
		monList.add(mon1);
		monList.add(mon1);
		server1.setMonitor(monList);

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
