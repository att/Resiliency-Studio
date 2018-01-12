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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.att.tta.rs.model.EventMonkeyStrategyDTO;
import com.att.tta.rs.model.EventRecorder;
import com.att.tta.rs.model.EventStatus;
import com.att.tta.rs.model.MonkeyStrategy;
import com.att.tta.rs.model.Scenario;
import com.att.tta.rs.model.ScenarioAdapter;
import com.att.tta.rs.model.ScenarioExecutionAdapter;
import com.att.tta.rs.model.ScenarioMonkeyStrategy;
import com.att.tta.rs.model.Server;
import com.att.tta.rs.model.SoftwareComponent;
import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.ApplicationService;
import com.att.tta.rs.service.MonkeyStrategyService;
import com.att.tta.rs.service.ScenarioService;
import com.att.tta.rs.service.TeamUserService;
import com.att.tta.rs.util.AppUtil;
import com.att.tta.rs.util.EUtil;
import com.att.tta.rs.util.MessageWrapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This Class provides certain REST APIs to perform CRUD operations on Scenario
 * repository.
 * 
 * @author ak983d
 *
 */

@EnableWebMvc
@RestController
@Api(value = "Scenario Rest Controller", description = "This REST controller provides REST APIs for performing CRUD Operation on Scenario Repository")
public class ScenarioRestController {
	private static EELFLogger logger = AjscEelfManager.getInstance().getLogger(ScenarioRestController.class);
	private static final String APPNAME = " and application name";
	private static final String NOTFOUND = " not found";
	private static final String SCENARIO = "A scenario with name ";
	
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
	private static final String ALPHAERR = "\n Team Alpha cannot execute scenarios.";
	private static final String MONKEYSTRATEGYNOTPERSENT = "Monkey Strategies cannot be null.";
	private static final String FAILED = "Failed";

	/**
	 * This API returns list of all Scenarios objects available in Scenario
	 * Repository.
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "This API returns all Scenario Objects present in Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned all Scenario Objects"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Scenario object found") })
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
	@ApiOperation(value = "This API returns list of all Scenario objects present in Elastic Search for given team", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned all Scenario Objects for given team"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Scenario object found for given team") })
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
	@ApiOperation(value = "This API returns count of Scenario objects available in Elastic Search for team name", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned count of Scenario objects for team name"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "The resource trying to reach is not found") })
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
	@ApiOperation(value = "This API returns single Scenario Object present in Elastic Search for given Scenario ID", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned single Scenario Object for given Scenario ID"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Scenario object found for given Scenario ID") })
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
	@ApiOperation(value = "This API returns Scenario Objects present in Elastic Search for given App Name", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned Scenario Objects for given App Name"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Scenario object found for given App Name") })
	@RequestMapping(value = "/api/scenarios/scenariosby-appname/{applicationName:.+}", method = RequestMethod.GET, produces = "application/json")
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
	@ApiOperation(value = "This API returns single Scenario Object present in Elastic Search for given Scenario Name", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returned single Scenario Object for given Scenario Name"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Scenario object found for given Scenario Name") })
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
	@ApiOperation(value = "This API returns Scenario Objects present in Elastic Search for given Scenario Name and team Name", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returned single Scenario Object for given Scenario Name and team Name"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Scenario object found for given Scenario Name and team Name") })
	@RequestMapping(value = "/api/scenarios/listscenariobyname/{name:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getScenarioListByName(HttpServletRequest request, @PathVariable("name") String name) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Page<Scenario> scenarioPage = scenarioService.findScenarioListByScenarioNameByTeamName(name, teamName,
				new PageRequest(0, 9999));
		List<Scenario> scenarioList = scenarioPage.getContent();
		if (scenarioList == null || scenarioList.isEmpty()) {
			String error = "Scenario with name " + name + NOTFOUND;
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
	@ApiOperation(value = "This API inserts a Scenario Object into Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Scenario object successfully inserted into ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Add operation"),
			@ApiResponse(code = 409, message = "Scenario with same name is already exist"),
			@ApiResponse(code = 404, message = "The resource trying to reach is not found"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/scenario/", method = RequestMethod.POST, produces = "application/json", consumes = MediaType.ALL_VALUE)
	public ResponseEntity<Object> addScenario(HttpServletRequest request, @RequestBody Scenario scenario,
			UriComponentsBuilder ucBuilder) {
		logger.debug("Creating scenario with name ->%s, application name ->%s", scenario.getName(),
				scenario.getApplicationName());

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();

		if (scenarioService.isScenarioExistForTeamName(scenario, teamName)) {
			final String error = SCENARIO + scenario.getName() + " already exist";
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
	@ApiOperation(value = "This API updates a Scenario Objects into Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Scenario objects successfully updated into ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Update operation"),
			@ApiResponse(code = 409, message = "Scenario with same name is already exist"),
			@ApiResponse(code = 404, message = "Scenario object not found in ES for given Scenario ID"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/scenarios/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> updateScenario(HttpServletRequest request, @PathVariable("id") String id,
			@RequestBody Scenario toModifyScenario) {
		logger.debug("Updating Scenario with ID: " + id);

		Scenario currentScenario = scenarioService.findOne(id);
		if (currentScenario == null) {
			final String error = "Scenario with id " + id + NOTFOUND;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		toModifyScenario.setTeamName(currentScenario.getTeamName());

		if (scenarioService.isScenarioExistForTeamName(toModifyScenario, teamName)) {
			final String error = SCENARIO + toModifyScenario.getName() + " already exist";
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
	@ApiOperation(value = "This API deletes a Scenario Objects from Elastic Search for given Scenario ID", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Scenario object successfully deleted from ES for given Scenario ID"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Delete operation"),
			@ApiResponse(code = 404, message = "Scenario object not found in ES for given Scenario ID"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/scenarios/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteScenario(@PathVariable("id") String id) {
		logger.debug("Deleting Scenario with id " + id);
		Scenario scenario = scenarioService.findOne(id);
		if (scenario == null) {
			final String error = "Unable to delete. Scenario with id " + id + NOTFOUND;
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
	@ApiOperation(value = "This API deletes a Scenario Objects from Elastic Search for given App Name", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Scenario object successfully deleted from ES for given App Name"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Delete operation"),
			@ApiResponse(code = 404, message = "Scenario object not found in ES for given App Name"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/scenarios/scenariosby-appname/{applicationName}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteScenarioForApplication(HttpServletRequest request,
			@PathVariable("applicationName") String applicationName) {
		logger.debug("Deleting all Scenarios for application " + applicationName);
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		List<Scenario> scenarioList = scenarioService.findByApplicationNameByTeamName(applicationName, teamName);
		if (scenarioList == null || scenarioList.isEmpty()) {
			final String error = "Unable to delete. Scenario for application " + applicationName + NOTFOUND;
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
	@ApiOperation(value = "This API deletes all Scenario Objects from Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All Scenario object successfully deleted from ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Delete operation"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/scenarios/", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteAllScenarios() {
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
	@ApiOperation(value = "This API do bulk insertion of Scenario Objects into Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Scenario objects successfully inserted into ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Add operation"),
			@ApiResponse(code = 409, message = "Scenario with same name is already exist"),
			@ApiResponse(code = 404, message = "The resource trying to reach is not found"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/scenarios/bulk/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> bulkaddScenario(HttpServletRequest request,
			@RequestBody ScenarioAdapter scenarioAdapter) {

		logger.debug("Bulk inserting scenarios Total #-->" + scenarioAdapter.getScenarios().size());
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		List<Scenario> toBeInertScenarios = new ArrayList<>();
		for (Scenario scenario : scenarioAdapter.getScenarios()) {
			scenario.setTeamName(teamName);
			if (scenarioService.isScenarioExistForTeamName(scenario, teamName)) {
				logger.debug(SCENARIO + scenario.getName() + " already exists.");
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
	@ApiOperation(value = "This API deletes Bulk Scenario Objects from Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Scenario object successfully deleted from ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Delete operation"),
			@ApiResponse(code = 404, message = "The resource trying to reach is not found"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
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
	 * Execute Scenario based on given Scenario Name
	 * 
	 * @param request
	 * @param execScenario
	 * @return
	 */
	@ApiOperation(value = "This API executes a Scenario for given Scenario Name", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Scenario successfully executed"),
			@ApiResponse(code = 401, message = "User is not authorized to perform execute a Scenario"),
			@ApiResponse(code = 404, message = "Scenario not found for given name"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/executescenario/{name:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> executeScenariobyScenarioName(HttpServletRequest request,
			@PathVariable("name") String name) {

		TeamUser teamUser = userDetailsService.getCurrentTeamForUser(request);
		final String userId = teamUser.getUserName();
		final String teamName = teamUser.getTeamName();

		Scenario execScenario = scenarioService.findByScenarioNameByTeamName(name, teamName);

		if (execScenario == null) {
			logger.debug("Scenario with name " + name + NOTFOUND);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (null == execScenario.getStrategies() || execScenario.getStrategies().isEmpty()) {
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, MONKEYSTRATEGYNOTPERSENT,
					MONKEYSTRATEGYNOTPERSENT);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		execScenario.setUserId(userId);

		Map<String, EventRecorder> events = new HashMap<>();
		EventRecorder parentEvent = null;

		String initialStatus = "";
		org.joda.time.DateTime dt = new org.joda.time.DateTime();

		for (ScenarioMonkeyStrategy strategy : execScenario.getStrategies()) {
			initialStatus = "Executing scenario with name: " + execScenario.getName() + " Monkey Strategy: "
					+ strategy.getMonkeyStrategy() + ",  application: " + execScenario.getApplicationName()
					+ ", application server: " + execScenario.getServerName() + ",  server component: "
					+ execScenario.getSoftwareComponentName() + " submitted by user Id: " + userId + " \n";
			logger.debug(initialStatus);

			EventRecorder event = scenarioService.createEvent(execScenario, initialStatus, teamName, strategy,
					dt.toString(), strategy.getExecSequence());
			events.put(event.getId(), event);
			strategy.setEventID(event.getId());

			if (null == parentEvent) {
				parentEvent = event;
			} else {
				parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + initialStatus);
			}
		}

		if (AppUtil.getSuperUser().equals(teamName)) {
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, ALPHAERR, ALPHAERR);
			events.forEach(
					(k, v) -> scenarioService.finalizeEvent(v.getId(), ALPHAERR, EventStatus.REJECTED, teamName));
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (events.isEmpty()) {
			final String error = "Unable to create event.";
			logger.error(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		executeSingleScenarioTask(execScenario, null, teamName, userId, parentEvent, events);
		return new ResponseEntity<>(parentEvent, HttpStatus.OK);
	}

	/**
	 * This API executes a Scenario.
	 * 
	 * @param request
	 * @param execScenario
	 * @return
	 */
	@ApiOperation(value = "This API executes a Scenario for given Scenario Object", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Scenario successfully executed"),
			@ApiResponse(code = 401, message = "User is not authorized to perform execute a Scenario"),
			@ApiResponse(code = 404, message = "Scenario not found for given name"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/executescenario/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> executeScenario(HttpServletRequest request,
			@RequestBody final Scenario execScenario1) {
		TeamUser teamUser = userDetailsService.getCurrentTeamForUser(request);
		final String userId = teamUser.getUserName();
		final String teamName = teamUser.getTeamName();

		Scenario execScenario = scenarioService.findByScenarioNameByTeamName(execScenario1.getName(), teamName);
		execScenario.setUserId(userId);
		Map<String, EventRecorder> events = new HashMap<>();
		EventRecorder parentEvent = null;

		String initialStatus = "";
		org.joda.time.DateTime dt = new org.joda.time.DateTime();

		if (null == execScenario.getStrategies() || execScenario.getStrategies().isEmpty()) {
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, MONKEYSTRATEGYNOTPERSENT,
					MONKEYSTRATEGYNOTPERSENT);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		for (ScenarioMonkeyStrategy strategy : execScenario.getStrategies()) {
			initialStatus = "Executing scenario with name: " + execScenario.getName() + " Monkey Strategy: "
					+ strategy.getMonkeyStrategy() + ",  application: " + execScenario.getApplicationName()
					+ ", application server: " + execScenario.getServerName() + ",  server component: "
					+ execScenario.getSoftwareComponentName() + " submitted by user Id: " + userId + " \n";

			logger.debug(initialStatus);

			EventRecorder event = scenarioService.createEvent(execScenario, initialStatus, teamName, strategy,
					dt.toString(), strategy.getExecSequence());
			events.put(event.getId(), event);
			strategy.setEventID(event.getId());

			if (null == parentEvent) {
				parentEvent = event;
			} else {
				parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + initialStatus);
			}
		}

		if (AppUtil.getSuperUser().equals(teamName)) {
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, ALPHAERR, ALPHAERR);
			events.forEach(
					(k, v) -> scenarioService.finalizeEvent(v.getId(), ALPHAERR, EventStatus.REJECTED, teamName));
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (events.isEmpty()) {
			final String error = "Unable to create event.";
			logger.error(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		executeSingleScenarioTask(execScenario, null, teamName, userId, parentEvent, events);
		return new ResponseEntity<>(parentEvent, HttpStatus.OK);
	}

	/**
	 * This API executes a Scenario for a given Application.
	 * 
	 * @param request
	 * @param scenarioAdapter
	 * @return
	 */
	@ApiOperation(value = "This API executes a Scenario for given Name and App Name", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Scenario successfully executed for given Name and App Name"),
			@ApiResponse(code = 401, message = "User is not authorized to perform execute a Scenario"),
			@ApiResponse(code = 404, message = "Scenario not found for given name"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/executescenariowithapplication/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> executeScenarioWithApplication(HttpServletRequest request,
			@RequestBody final ScenarioExecutionAdapter scenarioAdapter) {
		final Scenario execScenario1 = scenarioAdapter.getScenario();
        TeamUser teamUser = userDetailsService.getCurrentTeamForUser(request);
        final String userId = teamUser.getUserName();
        final String teamName = teamUser.getTeamName();

        Scenario execScenario = scenarioService.findByScenarioNameByTeamName(execScenario1.getName(), teamName);
		
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

		if (null == execScenario.getStrategies() || execScenario.getStrategies().isEmpty()) {
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, MONKEYSTRATEGYNOTPERSENT,
					MONKEYSTRATEGYNOTPERSENT);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		execScenario.setUserId(userId);

		Map<String, EventRecorder> events = new HashMap<>();
		EventRecorder parentEvent = null;

		String initialStatus = "";
		org.joda.time.DateTime dt = new org.joda.time.DateTime();
		for (ScenarioMonkeyStrategy strategy : execScenario.getStrategies()) {
			initialStatus = " executing scenario " + execScenario.getName() + " Monkey Strategy"
					+ strategy.getMonkeyStrategy() + ",  application " + execScenario.getApplicationName()
					+ ", application server " + execScenario.getServerName() + ",  server component"
					+ execScenario.getSoftwareComponentName() + " submitted by user Id " + userId;
			logger.debug(initialStatus);

			EventRecorder event = scenarioService.createEvent(execScenario, initialStatus, teamName, strategy,
					dt.toString(), strategy.getExecSequence());
			events.put(event.getId(), event);
			strategy.setEventID(event.getId());

			if (null == parentEvent) {
				parentEvent = event;
			} else {
				parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + initialStatus);
			}
		}

		if (AppUtil.getSuperUser().equals(teamName)) {
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, ALPHAERR, ALPHAERR);
			events.forEach(
					(k, v) -> scenarioService.finalizeEvent(v.getId(), ALPHAERR, EventStatus.REJECTED, teamName));
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (events.isEmpty()) {
			final String error = "Unable to create event.";
			logger.error(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		executeSingleScenarioTask(execScenario, application, teamName, userId, parentEvent, events);
		return new ResponseEntity<>(parentEvent, HttpStatus.OK);
	}

	/**
	 * This API executes multiple Scenarios
	 * 
	 * @param request
	 * @param scenarioAdapter
	 * @return
	 */
	@ApiOperation(value = "This API do bulk execution of Scenarios", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Bulk Scenario execution is completed successfully"),
			@ApiResponse(code = 401, message = "User is not authorized to perform execute a Scenario"),
			@ApiResponse(code = 404, message = "Scenario not found for given name"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
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
			} else if (null == scenario.getMonkeyStrategy() || scenario.getMonkeyStrategy().isEmpty()) {
				logger.debug(MONKEYSTRATEGYNOTPERSENT);
			} else {
				scenario.setUserId(userId);
				Map<String, EventRecorder> events = new HashMap<>();
				EventRecorder parentEvent = null;
				String initialStatus = "";
				org.joda.time.DateTime dt = new org.joda.time.DateTime();
				for (ScenarioMonkeyStrategy strategy : scenario.getStrategies()) {
					initialStatus = " executing scenario " + scenario.getName() + " Monkey Strategy"
							+ strategy.getMonkeyStrategy() + ",  application " + scenario.getApplicationName()
							+ ", application server " + scenario.getServerName() + ",  server component"
							+ scenario.getSoftwareComponentName() + " submitted by user Id " + userId;
					logger.debug(initialStatus);

					EventRecorder event = scenarioService.createEvent(scenario, initialStatus, teamName, strategy,
							dt.toString(), strategy.getExecSequence());
					events.put(event.getId(), event);
					strategy.setEventID(event.getId());

					if (null == parentEvent) {
						parentEvent = event;
					} else {
						parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + initialStatus);
					}
				}
				EventRecorder event = parentEvent;
				taskExecutor.execute(() -> executeSingleScenarioTask(scenario, null, teamName, userId, event, events));
			}
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
	private void executeSingleScenarioTask(Scenario execScenario, Application oldApp, String teamName, String userId,
			EventRecorder parantEvent, Map<String, EventRecorder> events) {

		events.forEach((k, v) -> scenarioService.recordEvent(v.getId(),
				" Job scheduled by " + userId + ", @ " + new DateTime(), EventStatus.SUBMITTED, teamName));

		Application application;
		if (oldApp == null) {
			application = applicationService.findByApplicationNameAndTeamName(execScenario.getApplicationName(),
					teamName);
		} else {
			application = oldApp;
		}

		if (validateApplication(application, execScenario, teamName, parantEvent, events)) {
			return;
		}
		Environment execEnvironment = application.getEnvironment(execScenario.getEnvironmentName());
		Server execServer = execEnvironment.getServerWithName(execScenario.getServerName());
		try {
			if (null != execServer) {
				if (null != execServer.getPrivatekey()) {
					String privateKey = EUtil.decrypt(execServer.getPrivatekey());
					execServer.setPrivatekey(privateKey);
				}
				if ( null != execServer.getPassword() && !execServer.isRsaLogin()) {
					String password = EUtil.decrypt(execServer.getPassword());
					execServer.setPassword(password);
				}
			}
		} catch (Exception e) {
			logger.error("Unsupported decoding exception", e);
		}
		Map<String, MonkeyStrategy> monkeyStrategyMap = new HashMap<>();

		if (!getMonkeyStrategies(teamName, execScenario, parantEvent, events, monkeyStrategyMap)
				|| monkeyStrategyMap.isEmpty()) {
			return;
		}

		for (ScenarioMonkeyStrategy sceMonkeyStrategy : execScenario.getStrategies()) {
			MonkeyStrategy monkeyStrategy = monkeyStrategyMap.get(sceMonkeyStrategy.getMonkeyStrategy());
			if (null != monkeyStrategy) {
				String execStatus = "Default Monkey Strategy found with version "
						+ monkeyStrategy.getMonkeyStrategyVersion() + " for Monkey Strategy Name: "
						+ monkeyStrategy.getMonkeyStrategyName();

				parantEvent.setEventStatus(parantEvent.getEventStatus() + "\n" + execStatus + " \n");
				scenarioService.recordEvent(sceMonkeyStrategy.getEventID(), execStatus, EventStatus.INPROGRESS,
						teamName);
			}
		}

		parantEvent.setEventStatusType(EventStatus.INPROGRESS);
		execScenario.setFilePath(application.getFilePath());
		taskExecutor.execute(() -> execScenarioFromAgentController(parantEvent.getId(), execScenario, execServer,
				execEnvironment, monkeyStrategyMap, teamName, events));
		return;
	}

	private boolean getMonkeyStrategies(String teamName, Scenario execScenario, EventRecorder parentEvent,
			Map<String, EventRecorder> events, Map<String, MonkeyStrategy> monkeyStrategies) {

		for (ScenarioMonkeyStrategy monkeyStrategy : execScenario.getStrategies()) {
			MonkeyStrategy strategy = getMonkeyStrategyforScenarioRun(teamName, execScenario, parentEvent,
					events.get(monkeyStrategy.getEventID()), monkeyStrategy);
			if (null != strategy) {
				monkeyStrategies.put(strategy.getMonkeyStrategyName(), strategy);
			} else {
				return false;
			}
		}
		return true;
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
	private MonkeyStrategy getMonkeyStrategyforScenarioRun(String teamName, Scenario execScenario,
			EventRecorder parentEvent, EventRecorder event, ScenarioMonkeyStrategy sceMonkeyStrategy) {
		MonkeyStrategy monkeyStrategy = null;
		String execStatus;
		if (sceMonkeyStrategy.getMonkeyStrategyId() != null) {
			execStatus = " monkeyStrategyId in Scenario -->" + sceMonkeyStrategy.getMonkeyStrategyId()
					+ " for scenario " + execScenario.getTeamName();
			monkeyStrategy = monkeyStrategyService.findOneForTeam(sceMonkeyStrategy.getMonkeyStrategyId(), teamName);
		} else {
			execStatus = "MonkeyStrategy in scenario is null. \n " + " Selecting default Monkey Strategy for "
					+ " Monkey Type ->" + sceMonkeyStrategy.getMonkeyType().toString() + " and Monkey Strategy ->"
					+ sceMonkeyStrategy.getMonkeyStrategy() + " , ";
			logger.debug("MonkeyStrategy Id in scenario is null ");
		}

		scenarioService.recordEvent(event.getId(), execStatus, EventStatus.SUBMITTED, teamName);
		parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + execStatus);

		if (sceMonkeyStrategy.getMonkeyType() == null || sceMonkeyStrategy.getMonkeyStrategy() == null
				|| "".equals(sceMonkeyStrategy.getMonkeyStrategy().trim())) {

			String error = " Cannot execute Scenario monkeyStrategyId is null where Monkey Type = "
					+ sceMonkeyStrategy.getMonkeyType() + ", MonkeyStrategy=" + sceMonkeyStrategy.getMonkeyStrategy();
			logger.error(error);

			scenarioService.finalizeEvent(event.getId(), error, EventStatus.REJECTED, teamName);
			parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + error);
			return null;
		}

		return getDefaultMonkeyStrategy(teamName, execScenario, parentEvent, monkeyStrategy, event, sceMonkeyStrategy);
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
	private MonkeyStrategy getDefaultMonkeyStrategy(String teamName, Scenario execScenario, EventRecorder parentEvent,
			MonkeyStrategy monkeyStrategy, EventRecorder event, ScenarioMonkeyStrategy sceMonkeyStrategy) {
		MonkeyStrategy newMonkeyStrategy;
		if (monkeyStrategy == null || monkeyStrategy.getMonkeyStrategyScript() == null
				|| "".equals(monkeyStrategy.getMonkeyStrategyScript().trim())) {
			logger.debug("MonkeyStrategy null or monkeyStrategy Script not available ");
			logger.debug(
					"ExecScenario.getMonkeyType().monkeyType() -->" + sceMonkeyStrategy.getMonkeyType().monkeyType());

			List<MonkeyStrategy> monkeyStrategyList = Lists
					.newArrayList(monkeyStrategyService.findDefaultMonkeyStrategy(sceMonkeyStrategy.getMonkeyType(),
							sceMonkeyStrategy.getMonkeyStrategy(), "Y", teamName));

			if (monkeyStrategyList == null || monkeyStrategyList.isEmpty()) {
				final String error = "Either MonkeyStrategy is null or MonkeyStrategy Script is not available ";
				scenarioService.finalizeEvent(event.getId(), error, EventStatus.REJECTED, teamName);
				parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + error);
				return null;
			} else if (monkeyStrategyList.size() > 1) {
				final String error = "More than one monkeyStrategy is found for MonkeyType "
						+ execScenario.getMonkeyType() + " and Team " + teamName;
				scenarioService.finalizeEvent(event.getId(), error, EventStatus.REJECTED, teamName);
				parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + error);
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
	private boolean validateApplication(Application application, Scenario execScenario, String teamName,
			EventRecorder parentEvent, Map<String, EventRecorder> events) {
		Environment excEnvironment;
		Server excServer;

		if (application == null) {
			final String error = " Application Not found for name " + execScenario.getApplicationName();
			logger.debug(error);

			events.forEach((k, v) -> scenarioService.finalizeEvent(v.getId(), error, EventStatus.REJECTED, teamName));
			parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + error + "\n");
			return true;
		}

		excEnvironment = application.getEnvironment(execScenario.getEnvironmentName());

		if (excEnvironment == null) {
			final String error = "Environment with name " + execScenario.getEnvironmentName()
					+ " not found in the application " + execScenario.getApplicationName();
			logger.debug(error);

			events.forEach((k, v) -> scenarioService.finalizeEvent(v.getId(), error, EventStatus.REJECTED, teamName));
			parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + error + "\n");
			return true;
		}

		excServer = excEnvironment.getServerWithName(execScenario.getServerName());
		if (excServer == null) {
			final String error = "server with host name " + execScenario.getServerName()
					+ " not found in environment with name " + execScenario.getEnvironmentName() + APPNAME
					+ execScenario.getApplicationName();
			logger.debug(error);

			events.forEach((k, v) -> scenarioService.finalizeEvent(v.getId(), error, EventStatus.REJECTED, teamName));
			parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + error + "\n");
			return true;
		}

		validateSoftComp(excServer, execScenario, parentEvent, teamName, events);
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
	private void validateSoftComp(Server execServer, Scenario execScenario, EventRecorder parentEvent, String teamName,
			Map<String, EventRecorder> events) {
		SoftwareComponent swComp = execServer.getSoftwareComponentWithProcessName(execScenario.getProcessName());
		if (swComp == null) {
			final String error = "SoftwareComponent with  name " + execScenario.getProcessName() + " not found in host "
					+ execServer.getHostName() + " and in environment " + execScenario.getEnvironmentName() + APPNAME
					+ execScenario.getApplicationName();
			logger.debug(error);
			parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + error + "\n");

			events.forEach(
					(k, v) -> scenarioService.recordEvent(v.getId(), error + "\n", EventStatus.SUBMITTED, teamName));
		}

		String processName = execScenario.getProcessName();
		if (processName != null && swComp != null
				&& !processName.toLowerCase().trim().equalsIgnoreCase(swComp.getProcessName().toLowerCase().trim())) {
			final String error = "SoftwareComponent with  name " + execScenario.getSoftwareComponentName()
					+ " has no process with name " + execScenario.getProcessName() + " in host "
					+ execServer.getHostName() + " and in environment " + execScenario.getEnvironmentName() + APPNAME
					+ execScenario.getApplicationName();
			logger.debug(error);
			parentEvent.setEventStatus(parentEvent.getEventStatus() + "\n" + error + "\n");
			events.forEach(
					(k, v) -> scenarioService.recordEvent(v.getId(), error + "\n", EventStatus.SUBMITTED, teamName));
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
	private void execScenarioFromAgentController(String parentEventId, Scenario execScenario, Server execServer,
			Environment execEnvironment, Map<String, MonkeyStrategy> monkeyStrategyMap, String teamName,
			Map<String, EventRecorder> events) {
		try {
			Gson gson = new Gson();
			RestTemplate restTemplate = new RestTemplate();
			EventJobDTO eventdto = createEventJobDTO(parentEventId, execScenario, execServer, execEnvironment,
					monkeyStrategyMap, teamName);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", env.getProperty("resiliencystudio.agentAuthorization"));

			HttpEntity<EventJobDTO> entity = new HttpEntity<>(eventdto, headers);
			String agentURL = env.getProperty("resiliencystudio.agentURL") + AGENTURI;
			ResponseEntity<String> result = restTemplate.exchange(agentURL, HttpMethod.POST, entity, String.class);
			eventdto = gson.fromJson(result.getBody(), EventJobDTO.class);
			final String execDuration = eventdto.getExecDuration();

			eventdto.getEventMonkeyList().forEach(eventMonkeyStrategyDTO -> {
				if ("-11".equalsIgnoreCase(eventMonkeyStrategyDTO.getReturnCode())) {
					scenarioService.finalizeEvent(eventMonkeyStrategyDTO.getEventId(),
							eventMonkeyStrategyDTO.getExecStatus(), EventStatus.FAILED, teamName);
				} else if ("000".equalsIgnoreCase(eventMonkeyStrategyDTO.getReturnCode())) {
					scenarioService.finalizeEvent(eventMonkeyStrategyDTO.getEventId(),
							eventMonkeyStrategyDTO.getExecStatus(), EventStatus.REJECTED, teamName);
				} else {
					scenarioService.finalizeEvent(eventMonkeyStrategyDTO.getEventId(),
							eventMonkeyStrategyDTO.getExecStatus(), EventStatus.COMPLETED, teamName,
							getJobExecStatus(eventMonkeyStrategyDTO.getReturnCode()), execDuration);
				}
			});

		} catch (Exception e) {
			logger.error("Error while calling Execution Agent Controller. ", e);
			final String error = "Error while calling Execution Agent Controller. Error is: " + e.getMessage();
			events.forEach((k, v) -> scenarioService.finalizeEvent(v.getId(), error, EventStatus.FAILED, teamName));
		}
	}

	/**
	 * This method return the Job Execution status based on Job return code.
	 * 
	 * @param returnCode
	 * @return
	 */
	private String getJobExecStatus(String returnCode) {
		if (null == returnCode) {
			return "";
		} else {
			String jobExecStatus;
			switch (returnCode) {
			case "0":
				jobExecStatus = "Passed";
				break;
			case "1":
			case "-1":
			case "101":
			default:
				jobExecStatus = FAILED;
				break;
			}
			return jobExecStatus;
		}
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
	private EventJobDTO createEventJobDTO(String parentEventId, Scenario execScenario, Server execServer,
			Environment execEnvironment, Map<String, MonkeyStrategy> monkeyStrategyMap, String teamName) {
		EventJobDTO dto = new EventJobDTO();

		dto.setEventId(parentEventId);
		dto.setTeamName(teamName);

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
	
		if (null != execEnvironment.getPassword()){
			dto.setDiscoverPassword(EUtil.decrypt(execEnvironment.getPassword()));
		}
		
		dto.setFilePath(execScenario.getFilePath());

		dto.setEventMonkeyList(createMonkeyStrategyDTO(execScenario, monkeyStrategyMap));

		dto.getEventMonkeyList().forEach(eventMonkey -> {
			if ("Ansible Script".equals(eventMonkey.getMonkeyScriptType())) {
				String role = "managed-nodes";
				if (execServer.getRoles() != null && !execServer.getRoles().isEmpty()
						&& !execServer.getRoles().get(0).isEmpty()) {
					role = execServer.getRoles().get(0);
				}
				dto.setConfigFile(String.format("[" + role + "]%n" + execServer.getInstanceName() + " ansible_ssh_user="
						+ execServer.getUserName() + " ansible_ssh_private_key_file=~/.ssh/" + parentEventId + "_"
						+ execServer.getInstanceName() + "%n"));
			}
		});

		return dto;
	}

	private List<EventMonkeyStrategyDTO> createMonkeyStrategyDTO(Scenario execScenario,
			Map<String, MonkeyStrategy> monkeyStrategyMap) {
		List<EventMonkeyStrategyDTO> eventMonkeyStrategyDTOs = new ArrayList<>();
		EventMonkeyStrategyDTO dto = null;
		MonkeyStrategy monkeyStrategy = null;

		for (ScenarioMonkeyStrategy sceMonkeyStrategy : execScenario.getStrategies()) {
			dto = new EventMonkeyStrategyDTO();
			monkeyStrategy = monkeyStrategyMap.get(sceMonkeyStrategy.getMonkeyStrategy());

			dto.setEventId(sceMonkeyStrategy.getEventID());
			dto.setExecSequence(sceMonkeyStrategy.getExecSequence());

			dto.setMonkeyStrategy(sceMonkeyStrategy.getMonkeyStrategy());
			dto.setMonkeyStrategyId(sceMonkeyStrategy.getMonkeyStrategyId());

			dto.setMonkeyScriptContent(monkeyStrategy.getMonkeyStrategyScript());
			dto.setMonkeyScriptType(monkeyStrategy.getScriptTypeCategory());
			dto.setMonkeyStrategyVersion(monkeyStrategy.getMonkeyStrategyVersion());

			eventMonkeyStrategyDTOs.add(dto);
		}
		return eventMonkeyStrategyDTOs;
	}
}