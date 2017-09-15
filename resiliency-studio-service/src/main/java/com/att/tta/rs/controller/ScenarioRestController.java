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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;

import com.att.ajsc.logging.AjscEelfManager;
import com.att.eelf.configuration.EELFLogger;
import com.att.tta.rs.model.Application;
import com.att.tta.rs.model.Environment;
import com.att.tta.rs.model.EventJobDTO;
import com.att.tta.rs.model.EventRecorder;
import com.att.tta.rs.model.EventStatus;
import com.att.tta.rs.model.MonkeyStrategy;
import com.att.tta.rs.model.Scenario;
import com.att.tta.rs.model.ScenarioAdapter;
import com.att.tta.rs.model.ScenarioExecutionAdapter;
import com.att.tta.rs.model.Server;
import com.att.tta.rs.model.SoftwareComponent;
import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.ApplicationService;
import com.att.tta.rs.service.MonkeyStrategyService;
import com.att.tta.rs.service.ScenarioService;
import com.att.tta.rs.service.TeamUserService;
import com.att.tta.rs.util.AppUtil;
import com.att.tta.rs.util.MessageWrapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

/**
 * This Class provides certain REST APIs to perform CRUD operations on Scenario
 * repository.
 * 
 * @author ak983d
 *
 */

@EnableWebMvc
@RestController
public class ScenarioRestController {
	private static EELFLogger logger = AjscEelfManager.getInstance().getLogger(ScenarioRestController.class);

	@Autowired
	org.springframework.core.env.Environment env;

	@Autowired
	ScenarioService scenarioService;

	@Autowired
	ApplicationService applicationService;

	@Autowired
	MonkeyStrategyService monkeyStrategyService;

	@Autowired
	TaskExecutor taskExecutor;

	@Autowired
	TeamUserService userDetailsService;

	private static final String AGENTURI = "/resiliency-studio-agent/api/execJob/";
	private static final String ALPHAERR = "Team Alpha cannot execute scenarios.";
	private static final String FAILED = "Failed";

	/**
	 * This API returns list of all Scenarios objects available in Scenario
	 * Repository.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/", method = RequestMethod.GET)
	public ResponseEntity<List<Scenario>> listScenarios() {
		List<Scenario> scenarios = Lists.newArrayList(scenarioService.findAll());
		if (scenarios.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(scenarios, HttpStatus.OK);
	}

	/**
	 * This API returns list of all Scenarios objects available in Scenario
	 * Repository under given team.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/team/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Scenario>> listAllScenarios(HttpServletRequest request) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		List<Scenario> events = Lists.newArrayList(scenarioService.findAllByTeamName(teamName));

		if (events.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	/**
	 * This API returns count of Scenarios for current team.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/count/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> countByTeamName(HttpServletRequest request) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Long count = scenarioService.countByTeamName(teamName);
		return new ResponseEntity<>(count.toString(), HttpStatus.OK);
	}

	/**
	 * This API returns Scenarios objects for given Scenario ID
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getScenario(@PathVariable("id") String id) {
		Scenario scenario = scenarioService.findOne(id);
		if (scenario == null) {
			String error = "Scenario not found with id " + id;
			logger.debug(error);
			MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(scenario, HttpStatus.OK);
	}

	/**
	 * This API returns list of all Scenarios objects available in Scenario
	 * Repository under given Application name and team name.
	 * 
	 * @param request
	 * @param applicationName
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/scenariosby-appname/{applicationName}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> listAllScenariosByApplicationName(HttpServletRequest request,
			@PathVariable("applicationName") String applicationName) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		List<Scenario> scenarios = Lists
				.newArrayList(scenarioService.findByApplicationNameByTeamName(applicationName, teamName));
		if (scenarios.isEmpty()) {
			String error = "No Scenarios found under Application " + applicationName;
			logger.debug(error);
			MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(scenarios, HttpStatus.OK);
	}

	/**
	 * This API returns Scenarios object for given Scenario name and team name.
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/scenariobyname/{name:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getScenarioByName(HttpServletRequest request, @PathVariable("name") String name) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Scenario scenario = scenarioService.findByScenarioNameByTeamName(name, teamName);
		if (scenario == null) {
			String error = "Scenario  not found with name " + name;
			logger.debug(error);
			MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(scenario, HttpStatus.OK);
	}

	/**
	 * This API returns list of Scenarios object for given Scenario name and
	 * team name.
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/listscenariobyname/{name:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getScenarioListByName(HttpServletRequest request, @PathVariable("name") String name) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Page<Scenario> scenarioPage = scenarioService.findScenarioListByScenarioNameByTeamName(name, teamName,
				new PageRequest(0, 9999));
		List<Scenario> scenarioList = scenarioPage.getContent();
		if (scenarioList == null || scenarioList.isEmpty()) {
			String error = "Scenario with name " + name + " not found";
			logger.debug(error);
			MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(scenarioList, HttpStatus.OK);
	}

	/**
	 * This API creates a Scenario.
	 * 
	 * @param request
	 * @param scenario
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/api/scenario/", method = RequestMethod.POST, produces = "application/json", consumes = MediaType.ALL_VALUE)
	public ResponseEntity<Object> addScenario(HttpServletRequest request, @RequestBody Scenario scenario,
			UriComponentsBuilder ucBuilder) {
		logger.debug("Creating scenario with name ->%s, application name ->%s", scenario.getName(),
				scenario.getApplicationName());

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();

		if (scenarioService.isScenarioExistForTeamName(scenario, teamName)) {
			final String error = "A scenario with name " + scenario.getName() + " already exist";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		scenario.setTeamName(teamName.trim());
		scenarioService.save(scenario, teamName);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/scenarios/{id}").buildAndExpand(scenario.getId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * This API updates a Scenario
	 * 
	 * @param request
	 * @param id
	 * @param toModifyScenario
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> updateScenario(HttpServletRequest request, @PathVariable("id") String id,
			@RequestBody Scenario toModifyScenario) {
		logger.debug("Updating Scenario with ID: " + id);

		Scenario currentScenario = scenarioService.findOne(id);
		if (currentScenario == null) {
			final String error = "Scenario with id " + id + " not found";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		toModifyScenario.setTeamName(currentScenario.getTeamName());

		if (scenarioService.isScenarioExistForTeamName(toModifyScenario, teamName)) {
			final String error = "A scenario with name " + toModifyScenario.getName() + " already exist";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (toModifyScenario.getTeamName() == null
				|| !toModifyScenario.getTeamName().trim().equalsIgnoreCase(teamName)) {
			final String error = "Scenario is not owned by Team " + toModifyScenario.getTeamName();
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		toModifyScenario.setId(id);
		Scenario modifiedScenario = scenarioService.update(toModifyScenario, teamName);

		if (modifiedScenario == null) {
			final String error = "Scenario with id " + id + " is not updated";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(modifiedScenario, HttpStatus.OK);
	}

	/**
	 * This API deletes a Scenario
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteScenario(@PathVariable("id") String id) {
		logger.debug("Deleting Scenario with id " + id);
		Scenario scenario = scenarioService.findOne(id);
		if (scenario == null) {
			final String error = "Unable to delete. Scenario with id " + id + " not found";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		scenarioService.delete(scenario);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * This API deletes all Scenarios under application name
	 * 
	 * @param request
	 * @param applicationName
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/scenariosby-appname/{applicationName}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteScenarioForApplication(HttpServletRequest request,
			@PathVariable("applicationName") String applicationName) {
		logger.debug("Deleting all Scenarios for application " + applicationName);
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		List<Scenario> scenarioList = scenarioService.findByApplicationNameByTeamName(applicationName, teamName);
		if (scenarioList == null) {
			final String error = "Unable to delete. Scenario for application " + applicationName + " not found";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		for (Scenario scenario : scenarioList) {
			scenarioService.delete(scenario);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * This API deletes all Scenarios.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Scenario> deleteAllScenarios() {
		logger.debug("Deleting All Scenarios");
		scenarioService.deleteAllScenarios();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * This API does bulk Scenario insertion
	 * 
	 * @param request
	 * @param scenarioAdapter
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/bulk/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> bulkaddScenario(HttpServletRequest request,
			@RequestBody ScenarioAdapter scenarioAdapter) {

		logger.debug("Bulk inserting scenarios Total #-->" + scenarioAdapter.getScenarios().size());
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		List<Scenario> toBeInertScenarios = new ArrayList<>();
		for (Scenario scenario : scenarioAdapter.getScenarios()) {
			scenario.setTeamName(teamName);
			if (scenarioService.isScenarioExistForTeamName(scenario, teamName)) {
				logger.debug("A scenario with name " + scenario.getName() + " already exists.");
			} else {
				toBeInertScenarios.add(scenario);
			}
		}

		if (toBeInertScenarios.isEmpty()) {
			final String error = "Scenarios with same name are already exist for selected application and environment.";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		} else {
			scenarioService.save(toBeInertScenarios, teamName);
			HttpHeaders headers = new HttpHeaders();
			return new ResponseEntity<>(headers, HttpStatus.CREATED);
		}
	}

	/**
	 * This API does bulk deletion of scenarios.
	 * 
	 * @param scenarioAdapter
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/api/scenarios/bulk/", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> bulkdeleteScenario(@RequestBody ScenarioAdapter scenarioAdapter) {

		logger.debug("Bulk inserting scenarios Total #-->" + scenarioAdapter.getScenarios().size());

		for (Scenario scenario : scenarioAdapter.getScenarios()) {
			if (scenario.getId() == null || "".trim().equals(scenario.getId())) {
				final String error = "Scenario Id cannot be empty.";
				logger.debug(error);
				final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
				return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
			}
			scenarioService.delete(scenario);
		}

		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}

	/**
	 * This API executes a Scenario.
	 * 
	 * @param request
	 * @param execScenario
	 * @return
	 */
	@RequestMapping(value = "/api/executescenario/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> executeScenario(HttpServletRequest request,
			@RequestBody final Scenario execScenario) {

		logger.debug("Executing scenario with name: " + execScenario.getName() + " for Monkey Strategy Id:"
				+ execScenario.getMonkeyStrategyId() + " for Scenario Id: " + execScenario.getId()
				+ " for application scenario: " + execScenario.getApplicationName() + " for application server: "
				+ execScenario.getServerName() + " for application server component: "
				+ execScenario.getSoftwareComponentName());

		TeamUser teamUser = userDetailsService.getCurrentTeamForUser(request);
		final String userId = teamUser.getUserName();
		final String teamName = teamUser.getTeamName();

		execScenario.setUserId(userId);
		String initialStatus = " executing scenario " + execScenario.getName() + " Monkey Strategy"
				+ execScenario.getMonkeyStrategy() + ",  application " + execScenario.getApplicationName()
				+ ", application server " + execScenario.getServerName() + ",  server component"
				+ execScenario.getSoftwareComponentName() + " submitted by user Id " + userId;

		final EventRecorder evt = scenarioService.createEvent(execScenario, initialStatus, teamName);

		if (AppUtil.getSuperUser().equals(teamName)) {
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, ALPHAERR, ALPHAERR);
			scenarioService.finalizeEvent(evt.getId(), ALPHAERR, EventStatus.REJECTED, teamName);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (evt == null) {
			final String error = "Unable to create event.";
			logger.error(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		final String eventId = evt.getId();
		logger.debug("Created Event Id -->" + eventId);

		executeSingleScenarioTask(execScenario, null, eventId, teamName, userId, evt);
		return new ResponseEntity<>(evt, HttpStatus.OK);
	}

	/**
	 * This API executes a Scenario for a given Application.
	 * 
	 * @param request
	 * @param scenarioAdapter
	 * @return
	 */
	@RequestMapping(value = "/api/executescenariowithapplication/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> executeScenarioWithApplication(HttpServletRequest request,
			@RequestBody final ScenarioExecutionAdapter scenarioAdapter) {

		final Scenario execScenario = scenarioAdapter.getScenario();
		final Application application = scenarioAdapter.getApplication();
		if (execScenario == null) {
			final String error = "Scenario cannot be null ";
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (application == null) {
			final String error = " Application cannot be null  ";
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		logger.debug("Executing scenario with name: " + execScenario.getName() + " for Monkey Strategy Id:"
				+ execScenario.getMonkeyStrategyId() + " for Scenario Id: " + execScenario.getId()
				+ " for application scenario: " + execScenario.getApplicationName() + " for application server: "
				+ execScenario.getServerName() + " for application server component: "
				+ execScenario.getSoftwareComponentName());

		TeamUser teamUser = userDetailsService.getCurrentTeamForUser(request);
		final String userId = teamUser.getUserName();
		final String teamName = teamUser.getTeamName();

		execScenario.setUserId(userId);
		String initialStatus = " executing scenario " + execScenario.getName() + " Monkey Strategy"
				+ execScenario.getMonkeyStrategy() + ",  application " + execScenario.getApplicationName()
				+ ", application server " + execScenario.getServerName() + ",  server component"
				+ execScenario.getSoftwareComponentName() + " submitted by user Id " + userId;

		final EventRecorder evt = scenarioService.createEvent(execScenario, initialStatus, teamName);

		if (AppUtil.getSuperUser().equals(teamName)) {
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, ALPHAERR, ALPHAERR);
			scenarioService.finalizeEvent(evt.getId(), ALPHAERR, EventStatus.REJECTED, teamName);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (evt == null) {
			final String error = "Unable to create event.";
			logger.error(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		final String eventId = evt.getId();
		logger.debug(" Event Id -->" + eventId);

		executeSingleScenarioTask(execScenario, application, eventId, teamName, userId, evt);
		return new ResponseEntity<>(evt, HttpStatus.OK);
	}

	/**
	 * This API executes multiple Scenarios
	 * 
	 * @param request
	 * @param scenarioAdapter
	 * @return
	 */
	@RequestMapping(value = "/api/executebulkscenario/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> executeBulkScenario(HttpServletRequest request,
			@RequestBody ScenarioAdapter scenarioAdapter) {

		if (scenarioAdapter == null) {
			final String error = "NULL request received in bulk executing scenarios";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		logger.debug("Bulk executing scenarios Total #-->" + scenarioAdapter.getScenarios().size());

		TeamUser teamUser = userDetailsService.getCurrentTeamForUser(request);
		final String userId = teamUser.getUserName();
		final String teamName = teamUser.getTeamName();

		if (teamName.equals(AppUtil.getSuperUser())) {
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, ALPHAERR, ALPHAERR);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		for (final Scenario scenario : scenarioAdapter.getScenarios()) {
			if (scenario.getName() == null || "".equals(scenario.getName().trim())) {
				logger.debug("Scenario name cannot be empty");
				scenario.setUserId(userId);
			}

			String status = " Scenario " + scenario.getName() + " submitted for execution ";
			final EventRecorder evt = scenarioService.createEvent(scenario, status, teamName);
			final String eventId = evt.getId();

			taskExecutor.execute(() -> executeSingleScenarioTask(scenario, null, eventId, teamName, userId, evt));
		}

		final String message = "Bulk Scenario execution job submitted";
		final MessageWrapper apiError = new MessageWrapper(HttpStatus.OK, message, message);
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * This method is used to execute a single Scenario.
	 * 
	 * @param execScenario
	 * @param oldApp
	 * @param eventId
	 * @param teamName
	 * @param userId
	 * @param evt
	 */
	private void executeSingleScenarioTask(Scenario execScenario, Application oldApp, String eventId, String teamName,
			String userId, EventRecorder evt) {

		scenarioService.recordEvent(eventId, " Job scheduled by " + userId + ", @ " + new DateTime(),
				EventStatus.SUBMITTED, teamName);
		evt.setEventStatus("");

		Application application;
		if (oldApp == null) {
			application = applicationService.findByApplicationNameAndTeamName(execScenario.getApplicationName(),
					teamName);
		} else {
			application = oldApp;
		}

		if (validateApplication(application, execScenario, eventId, teamName, evt)) {
			return;
		}

		Environment execEnvironment = application.getEnvironment(execScenario.getEnvironmentName());
		Server execServer = execEnvironment.getServerWithName(execScenario.getServerName());

		String monkeyStrategyId = execScenario.getMonkeyStrategyId();
		MonkeyStrategy monkeyStrategy = getMonkeyStrategyforScenarioRun(monkeyStrategyId, eventId, teamName,
				execScenario, evt);
		if (null == monkeyStrategy) {
			return;
		}

		String execStatus = "Default Monkey Strategy found with version " + monkeyStrategy.getMonkeyStrategyVersion()
				+ " , ";
		scenarioService.recordEvent(eventId, execStatus, EventStatus.INPROGRESS, teamName);
		evt.setEventStatus(evt.getEventStatus() + "\n" + execStatus);
		evt.setEventStatusType(EventStatus.INPROGRESS);

		taskExecutor.execute(() -> execScenarioFromAgentController(eventId, execScenario, execServer, execEnvironment,
				monkeyStrategy, teamName));
		return;
	}

	/**
	 * This method validates Scenario object and returns the Monkey Strategy
	 * object.
	 * 
	 * @param monkeyStrategyId
	 * @param eventId
	 * @param teamName
	 * @param execScenario
	 * @param evt
	 * @return
	 */
	private MonkeyStrategy getMonkeyStrategyforScenarioRun(String monkeyStrategyId, String eventId, String teamName,
			Scenario execScenario, EventRecorder evt) {
		MonkeyStrategy monkeyStrategy = null;
		String execStatus;
		if (monkeyStrategyId != null) {
			execStatus = " monkeyStrategyId in Scenario -->" + monkeyStrategyId + " for scenario "
					+ execScenario.getTeamName();
			monkeyStrategy = monkeyStrategyService.findOneForTeam(monkeyStrategyId, teamName);
		} else {
			execStatus = "MonkeyStrategy in scenario is null. \n " + " Selecting default Monkey Strategy for "
					+ " Monkey Type ->" + execScenario.getMonkeyType().toString() + " and Monkey Strategy ->"
					+ execScenario.getMonkeyStrategy() + " , ";
			logger.debug("MonkeyStrategy Id in scenario is null ");
		}

		scenarioService.recordEvent(eventId, execStatus, EventStatus.SUBMITTED, teamName);
		evt.setEventStatus(evt.getEventStatus() + "\n" + execStatus);

		if (execScenario.getMonkeyType() == null || execScenario.getMonkeyStrategy() == null
				|| "".equals(execScenario.getMonkeyStrategy().trim())) {

			String error = " Cannot execute Scenario monkeyStrategyId is null, Monkey Type = "
					+ execScenario.getMonkeyType() + ", MonkeyStrategy=" + execScenario.getMonkeyStrategy();
			logger.error(error);
			scenarioService.finalizeEvent(eventId, error, EventStatus.REJECTED, teamName);
			evt.setEventStatus(evt.getEventStatus() + "\n" + error);
			return null;
		}

		return getDefaultMonkeyStrategy(eventId, teamName, execScenario, evt, monkeyStrategy);
	}

	/**
	 * This method returns the Default Monkey Strategy object.
	 * 
	 * @param monkeyStrategyId
	 * @param eventId
	 * @param teamName
	 * @param execScenario
	 * @param evt
	 * @param monkeyStrategy
	 * @return
	 */
	private MonkeyStrategy getDefaultMonkeyStrategy(String eventId, String teamName, Scenario execScenario,
			EventRecorder evt, MonkeyStrategy monkeyStrategy) {
		MonkeyStrategy newMonkeyStrategy;
		if (monkeyStrategy == null || monkeyStrategy.getMonkeyStrategyScript() == null
				|| "".equals(monkeyStrategy.getMonkeyStrategyScript().trim())) {
			logger.debug("MonkeyStrategy null or monkeyStrategy Script not available ");
			logger.debug("ExecScenario.getMonkeyType().monkeyType() -->" + execScenario.getMonkeyType().monkeyType());

			List<MonkeyStrategy> monkeyStrategyList = Lists
					.newArrayList(monkeyStrategyService.findDefaultMonkeyStrategy(execScenario.getMonkeyType(),
							execScenario.getMonkeyStrategy(), "Y", teamName));

			if (monkeyStrategyList == null || monkeyStrategyList.isEmpty()) {
				final String error = "Either MonkeyStrategy is null or MonkeyStrategy Script is not available ";
				scenarioService.finalizeEvent(eventId, error, EventStatus.REJECTED, teamName);
				evt.setEventStatus(evt.getEventStatus() + "\n" + error);
				return null;
			} else if (monkeyStrategyList.size() > 1) {
				final String error = "More than one monkeyStrategy is found for MonkeyType "
						+ execScenario.getMonkeyType() + " and Team " + teamName;
				scenarioService.finalizeEvent(eventId, error, EventStatus.REJECTED, teamName);
				evt.setEventStatus(evt.getEventStatus() + "\n" + error);
				return null;
			}
			newMonkeyStrategy = monkeyStrategyList.get(0);
		} else {
			newMonkeyStrategy = monkeyStrategy;
		}

		return newMonkeyStrategy;
	}

	/**
	 * This method validates the Application object.
	 * 
	 * @param application
	 * @param execScenario
	 * @param eventId
	 * @param teamName
	 * @param evt
	 * @param execEnvironment
	 * @param execServer
	 * @return
	 */
	private boolean validateApplication(Application application, Scenario execScenario, String eventId, String teamName,
			EventRecorder evt) {
		Environment excEnvironment;
		Server excServer;

		if (application == null) {
			final String error = " Application Not found for name " + execScenario.getApplicationName();
			logger.debug(error);
			scenarioService.finalizeEvent(eventId, error, EventStatus.REJECTED, teamName);
			evt.setEventStatus(error);
			return true;
		}

		excEnvironment = application.getEnvironment(execScenario.getEnvironmentName());

		if (excEnvironment == null) {
			final String error = "Environment with name " + execScenario.getEnvironmentName()
					+ " not found in the application " + execScenario.getApplicationName();
			logger.debug(error);
			scenarioService.finalizeEvent(eventId, error, EventStatus.REJECTED, teamName);
			evt.setEventStatus(error);
			return true;
		}

		excServer = excEnvironment.getServerWithName(execScenario.getServerName());
		if (excServer == null) {
			final String error = "server with host name " + execScenario.getServerName()
					+ " not found in environment with name " + execScenario.getEnvironmentName()
					+ " and application name" + execScenario.getApplicationName();

			logger.debug(error);
			scenarioService.finalizeEvent(eventId, error, EventStatus.REJECTED, teamName);
			evt.setEventStatus(error);
			return true;
		}

		validateSoftComp(excServer, execScenario, evt, eventId, teamName);
		return false;
	}

	/**
	 * This method validates Software Component object.
	 * 
	 * @param execServer
	 * @param execScenario
	 * @param evt
	 * @param eventId
	 * @param teamName
	 */
	private void validateSoftComp(Server execServer, Scenario execScenario, EventRecorder evt, String eventId,
			String teamName) {
		SoftwareComponent swComp = execServer.getSoftwareComponentWithProcessName(execScenario.getProcessName());
		if (swComp == null) {
			final String error = "SoftwareComponent with  name " + execScenario.getProcessName() + " not found in host "
					+ execServer.getHostName() + " and in environment " + execScenario.getEnvironmentName()
					+ " and application name" + execScenario.getApplicationName();
			logger.debug(error);
			evt.setEventStatus(evt.getEventStatus() + "\n" + error);
			scenarioService.recordEvent(eventId, error, EventStatus.SUBMITTED, teamName);
		}

		String processName = execScenario.getProcessName();
		if (processName != null && swComp != null
				&& !processName.toLowerCase().trim().equalsIgnoreCase(swComp.getProcessName().toLowerCase().trim())) {
			final String error = "SoftwareComponent with  name " + execScenario.getSoftwareComponentName()
					+ " has no process with name " + execScenario.getProcessName() + " in host "
					+ execServer.getHostName() + " and in environment " + execScenario.getEnvironmentName()
					+ " and application name" + execScenario.getApplicationName();
			logger.debug(error);
			evt.setEventStatus(evt.getEventStatus() + "\n" + error);
			scenarioService.recordEvent(eventId, error, EventStatus.SUBMITTED, teamName);
		}
	}

	/**
	 * This method invokes Agent Controller to Run the monkey script of Target
	 * Application Server.
	 * 
	 * @param eventId
	 * @param execScenario
	 * @param execServer
	 * @param execEnvironment
	 * @param monkeyStrategy
	 * @param teamName
	 */
	private void execScenarioFromAgentController(String eventId, Scenario execScenario, Server execServer,
			Environment execEnvironment, MonkeyStrategy monkeyStrategy, String teamName) {
		try {
			Gson gson = new Gson();
			RestTemplate restTemplate = new RestTemplate();
			EventJobDTO eventdto = createEventJobDTO(eventId, execScenario, execServer, execEnvironment, monkeyStrategy,
					teamName);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", env.getProperty("resiliencystudio.agentAuthorization"));

			HttpEntity<EventJobDTO> entity = new HttpEntity<>(eventdto, headers);
			String agentURL = env.getProperty("resiliencystudio.agentURL") + AGENTURI;
			ResponseEntity<String> result = restTemplate.exchange(agentURL, HttpMethod.POST, entity, String.class);
			eventdto = gson.fromJson(result.getBody(), EventJobDTO.class);

			logger.debug("Execution Status -->" + eventdto.getExecStatus());
			scenarioService.finalizeEvent(eventId, eventdto.getExecStatus(), EventStatus.COMPLETED, teamName,
					getJobExecStatus(eventdto.getReturnCode()));
		} catch (Exception e) {
			logger.error("Error while calling Execution Agent Controller. ", e);
			final String error = "Error while calling Execution Agent Controller. Error is: " + e.getMessage();
			scenarioService.finalizeEvent(eventId, error, EventStatus.FAILED, teamName);
		}
	}

	/**
	 * This method return the Job Execution status based on Job return code.
	 * 
	 * @param returnCode
	 * @return
	 */
	private String getJobExecStatus(String returnCode) {
		String jobExecStatus;
		switch (returnCode) {
		case "0":
			jobExecStatus = "Passed";
			break;
		case "1":
			jobExecStatus = FAILED;
			break;
		case "-1":
			jobExecStatus = FAILED;
			break;
		case "101":
			jobExecStatus = FAILED;
			break;
		default:
			jobExecStatus = FAILED;
		}
		return jobExecStatus;
	}

	/**
	 * This is the setter method for EventJobDTO
	 * 
	 * @param eventId
	 * @param execScenario
	 * @param execServer
	 * @param execEnvironment
	 * @param monkeyStrategy
	 * @param teamName
	 * @return
	 */
	private EventJobDTO createEventJobDTO(String eventId, Scenario execScenario, Server execServer,
			Environment execEnvironment, MonkeyStrategy monkeyStrategy, String teamName) {
		EventJobDTO dto = new EventJobDTO();

		dto.setEventId(eventId);
		dto.setTeamName(teamName);

		dto.setMonkeyScriptType(monkeyStrategy.getScriptTypeCategory());
		dto.setMonkeyStrategyName(monkeyStrategy.getMonkeyStrategyName());
		dto.setMonkeyStrategyVersion(monkeyStrategy.getMonkeyStrategyVersion());
		dto.setMonkeyStrategyId(monkeyStrategy.getId());
		dto.setMonkeyScriptContent(monkeyStrategy.getMonkeyStrategyScript());

		dto.setServerName(execServer.getInstanceName());
		dto.setHostName(execServer.getHostName());
		dto.setIpAdd(execServer.getIpAddress());

		dto.setUserName(execServer.getUserName());
		dto.setPrivateKey(execServer.getPrivatekey());
		dto.setPassword(execServer.getPassword());

		dto.setProcessName(execScenario.getProcessName());
		dto.setFailureMode(execScenario.getFailureMode());
		dto.setScenarioServerName(execScenario.getServerName());

		dto.setDiscoverHostName(execEnvironment.getDiscoverHostName());
		dto.setDiscoverHostIpAddress(execEnvironment.getDiscoverHostIpAddress());
		dto.setDiscoverHostPort(execEnvironment.getDiscoverHostPort());
		dto.setDiscoverUserId(execEnvironment.getUserId());
		dto.setDiscoverPassword(execEnvironment.getPassword());

		if ("Ansible Script".equals(dto.getMonkeyScriptType())) {
			String role = "managed-nodes";
			if (execServer.getRoles() != null && !execServer.getRoles().isEmpty()) {
				role = execServer.getRoles().get(0);
			}
			dto.setConfigFile(String.format("[" + role + "]%n" + execServer.getInstanceName() + " ansible_ssh_user="
					+ execServer.getUserName() + " ansible_ssh_private_key_file=~/.ssh/" + eventId + "_"
					+ execServer.getInstanceName() + "%n"));
		}
		return dto;
	}
}