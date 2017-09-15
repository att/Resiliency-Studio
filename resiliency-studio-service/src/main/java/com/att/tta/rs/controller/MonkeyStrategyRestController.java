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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.att.tta.rs.model.Application;
import com.att.tta.rs.model.FailurePoint;
import com.att.tta.rs.model.MonkeyStrategy;
import com.att.tta.rs.model.MonkeyType;
import com.att.tta.rs.service.ApplicationService;
import com.att.tta.rs.service.MonkeyStrategyService;
import com.att.tta.rs.service.TeamUserService;
import com.att.tta.rs.util.AppUtil;
import com.att.tta.rs.util.MessageWrapper;
import com.google.common.collect.Lists;

/**
 * This Class provides certain REST APIs to perform CRUD operations on Monkey
 * Strategy repository.
 * 
 * @author mb6872
 *
 */
@RestController
public class MonkeyStrategyRestController {

	private static final Logger logger = LoggerFactory.getLogger(MonkeyStrategyRestController.class);

	/**
	 * instance of {@link MonkeyStrategyService}
	 */
	@Autowired
	MonkeyStrategyService monkeyStrategyService;

	@Autowired
	ApplicationService applicationService;

	/**
	 * instance of {@link TeamUserService}
	 */
	@Autowired
	TeamUserService userDetailsService;

	/**
	 * This API returns list of all Monkey Strategies objects available in
	 * Monkey Strategy Repository.
	 * 
	 * @return ResponseEntity<Object>
	 */
	@RequestMapping(value = "/api/monkeystrategies/", method = RequestMethod.GET)
	public ResponseEntity<Object> listAllMonkeyStrategies() {
		List<MonkeyStrategy> monkeyStrategies = Lists.newArrayList(monkeyStrategyService.findAllMonkeyStrategies());
		if (monkeyStrategies == null || monkeyStrategies.isEmpty()) {
			final String error = " No monkeyStrategies found ";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(monkeyStrategies, HttpStatus.OK);
	}

	/**
	 * This API returns all monkeyStrategies for current user Team
	 * 
	 * @param request
	 * @return list of AllMonkeyStrategy
	 */
	@RequestMapping(value = "/api/monkeystrategies/team-strategies/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> listAllMonkeyStrategy(HttpServletRequest request) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		List<MonkeyStrategy> monkeyStrategies = Lists.newArrayList(monkeyStrategyService.findAllForTeam(teamName));

		if (monkeyStrategies == null || monkeyStrategies.isEmpty()) {
			final String error = "No monkeyStrategies found for Team " + teamName;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(monkeyStrategies, HttpStatus.OK);
	}

	/**
	 * This API returns All monkeyTypes
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/monkeytypes/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> listAllMonkeyTypes() {
		return new ResponseEntity<>(Arrays.asList(MonkeyType.values()), HttpStatus.OK);
	}

	/**
	 * This API returns Single Monkey Strategy object for given Monkey Strategy
	 * id
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/monkeystrategies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMonkeyStrategy(HttpServletRequest request, @PathVariable("id") String id) {
		logger.debug("Fetching monkeyStrategy with id : %s ", id);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		MonkeyStrategy monkeyStrategy = monkeyStrategyService.findOneForTeam(id, teamName);

		if (monkeyStrategy == null) {
			final String error = "MonkeyStrategy not found for id " + id;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(monkeyStrategy, HttpStatus.OK);
	}

	/**
	 * This API returns Single Monkey Strategy object for given Monkey Strategy
	 * Name
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/api/monkeystrategies/monkeystrategybyname/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMonkeyStrategyByName(HttpServletRequest request,
			@PathVariable("name") String name) {
		logger.debug("Fetching monkeyStrategy with name : %s", name);
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		MonkeyStrategy monkeyStrategy = monkeyStrategyService.findByMonkeyStrategyNameAndTeamName(name, teamName);
		if (monkeyStrategy == null) {
			final String error = "Monkey Strategy not found for Team : " + name + ", " + teamName;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(monkeyStrategy, HttpStatus.OK);
	}

	/**
	 * This API returns default Monkey Strategy for given Monkey Strategy Name
	 * and MonkeyType
	 * 
	 * @param request
	 * @param name
	 * @param monkeyTypeStr
	 * @return
	 */
	@RequestMapping(value = "/api/monkeystrategies/defaultmonkeystrategy/{name}/{monkeytype}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDefaultMonkeyStrategy(HttpServletRequest request,
			@PathVariable("name") String name, @PathVariable("monkeytype") String monkeyTypeStr) {

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		MonkeyType monkeyType = MonkeyType.fromString(monkeyTypeStr.trim());

		MonkeyStrategy monkeyStrategy = monkeyStrategyService.findDefaultMonkeyStrategy(name, monkeyType, teamName);
		if (monkeyStrategy == null) {
			final String error = "Default Monkey Strategy not found with name & monkeyType : " + name + " ,"
					+ monkeyType + " for Team " + teamName;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(monkeyStrategy, HttpStatus.OK);
	}

	/**
	 * This API returns Single monkeyStrategy by Name and Version
	 * 
	 * @param request
	 * @param name
	 * @param version
	 * @return
	 */
	@RequestMapping(value = "/api/monkeystrategies/{name}/{version}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getApplicationByNameAndCategory(HttpServletRequest request,
			@PathVariable("name") String name, @PathVariable("version") String version) {
		logger.debug("Fetching monkeyStrategy : %s and version: %s", name, version);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		MonkeyStrategy monkeyStrategy = monkeyStrategyService.findByMonkeyStrategyNameAndTeamNameAndVersion(name,
				teamName, version);
		if (monkeyStrategy == null) {
			final String error = "monkeyStrategy " + name + " and verion " + version + " not found " + " for Team "
					+ teamName;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(monkeyStrategy, HttpStatus.OK);
	}

	/**
	 * This API returns count of Monkey Strategies
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/monkeystrategies/count/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> countByTeamName(HttpServletRequest request) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Long count = monkeyStrategyService.countByTeamName(teamName);
		return new ResponseEntity<>(count.toString(), HttpStatus.OK);
	}

	/**
	 * This API returns all Monkey Strategies by given Application and
	 * Application Environment
	 * 
	 * @param request
	 * @param applicationname
	 * @param environmentname
	 * @return
	 */
	@RequestMapping(value = "/api/monkeystrategies/autodiscover/{applicationname}/{environmentname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getFailurePointByApplicationEnvironmentAndTeamName(HttpServletRequest request,
			@PathVariable("applicationname") String applicationname,
			@PathVariable("environmentname") String environmentname) {
		logger.debug("Fetching Monkey Strategies for application: %s , and environment : %s", applicationname,
				environmentname);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Application application = applicationService.findByApplicationNameAndTeamName(applicationname, teamName);

		if (application == null) {
			final String error = " Application Not found for name " + applicationname;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (application.getEnvironment(environmentname) == null) {
			final String error = "Environment " + environmentname + " not found for application with name "
					+ applicationname;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		HashMap<String, List<FailurePoint>> serverFailurePointMap = (HashMap<String, List<FailurePoint>>) monkeyStrategyService
				.findMonkeyStrategyByOSTypeAndProcessName(application.getCategory(),
						application.getEnvironment(environmentname).getServerList());

		if (serverFailurePointMap == null || serverFailurePointMap.isEmpty()) {
			final String error = "No Monkey Strategy found for applicationname " + applicationname + "and environment "
					+ environmentname;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(serverFailurePointMap, HttpStatus.OK);
	}

	/**
	 * This API creates a Monkey Strategy
	 * 
	 * @param request
	 * @param monkeyStrategy
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/api/monkeystrategy/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> addMonkeyStrategy(HttpServletRequest request,
			@RequestBody MonkeyStrategy monkeyStrategy, UriComponentsBuilder ucBuilder) {

		if (monkeyStrategy == null) {
			final String error = "NULL request received to add monkeyStrategy";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (monkeyStrategy.getMonkeyStrategyName() == null
				|| "".equals(monkeyStrategy.getMonkeyStrategyName().trim())) {
			final String error = "Monkey Strategy name is empty";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		monkeyStrategy.setTeamName(teamName);
		logger.debug("Creating monkeyStrategy with name -->" + monkeyStrategy.getMonkeyStrategyName());

		if (insertMonkeyStrategy(monkeyStrategy, teamName)) {
			final String error = "MonkeyStrategy with name " + monkeyStrategy.getMonkeyStrategyName()
					+ " already exist";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		logger.debug("MonkeyStrategy saved in ES with id -->" + monkeyStrategy.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/monkeyStrategy/{id}").buildAndExpand(monkeyStrategy.getId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * This method inserts a monkey strategy.
	 * 
	 * @param monkeyStrategy
	 * @param teamName
	 * @return
	 */
	private boolean insertMonkeyStrategy(MonkeyStrategy monkeyStrategy, String teamName) {
		if (monkeyStrategyService.findDefaultMonkeyStrategy(monkeyStrategy.getMonkeyType(),
				monkeyStrategy.getMonkeyStrategyName(), "Y", teamName).iterator().hasNext()
				&& "Y".equals(monkeyStrategy.getDefaultFlag().trim())) {
			return true;
		}
		if (monkeyStrategyService.insertForTeam(monkeyStrategy, teamName) == null) {
			return true;
		}

		return false;
	}

	/**
	 * This API updates a monkeyStrategy for given Monkey Strategy ID
	 * 
	 * @param request
	 * @param id
	 * @param toModifyMonkeyStrategy
	 * @return
	 */
	@RequestMapping(value = "/api/monkeystrategies/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> updateMonkeyStrategy(HttpServletRequest request, @PathVariable("id") String id,
			@RequestBody MonkeyStrategy toModifyMonkeyStrategy) {
		logger.debug("Updating monkeyStrategy : %s", id);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		MonkeyStrategy currentMonkeyStrategy = monkeyStrategyService.findOneForTeam(id, teamName);

		if (!AppUtil.getSuperUser().equalsIgnoreCase(toModifyMonkeyStrategy.getTeamName())
				&& !toModifyMonkeyStrategy.getTeamName().equalsIgnoreCase(teamName)) {

			final String error = "MonkeyStrategy can be changed only by the owning Team:"
					+ toModifyMonkeyStrategy.getTeamName();
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (currentMonkeyStrategy == null) {
			final String error = "MonkeyStrategy with id " + id + " not found";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (isDefaultStrategyExist(toModifyMonkeyStrategy, teamName)) {
			final String error = "Default monkeyStrategy with name " + toModifyMonkeyStrategy.getMonkeyStrategyName()
					+ " already exist.";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		toModifyMonkeyStrategy.setId(id);
		toModifyMonkeyStrategy.setTeamName(teamName);
		monkeyStrategyService.update(toModifyMonkeyStrategy);
		return new ResponseEntity<>(toModifyMonkeyStrategy, HttpStatus.OK);
	}

	/**
	 * This method checks for Default monkey strategy.
	 * 
	 * @param toModifyMonkeyStrategy
	 * @param teamName
	 * @return
	 */
	private boolean isDefaultStrategyExist(MonkeyStrategy toModifyMonkeyStrategy, String teamName) {
		if ("Y".equalsIgnoreCase(toModifyMonkeyStrategy.getDefaultFlag())) {
			Iterable<MonkeyStrategy> iterable = monkeyStrategyService.findDefaultMonkeyStrategy(
					toModifyMonkeyStrategy.getMonkeyType(), toModifyMonkeyStrategy.getMonkeyStrategyName(), "Y",
					teamName);
			MonkeyStrategy defaultMonkeyStrategy = null;
			if (null != iterable && null != iterable.iterator()) {
				for (Iterator<MonkeyStrategy> it = iterable.iterator(); it.hasNext();) {
					defaultMonkeyStrategy = it.next();
					break;
				}
			}
			if (null != defaultMonkeyStrategy
					&& !defaultMonkeyStrategy.getId().equals(toModifyMonkeyStrategy.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This API deletes a MonkeyStrategy
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/monkeystrategies/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deletemonkeystrategy(HttpServletRequest request, @PathVariable("id") String id) {
		logger.debug("Deleting monkeystrategy with id %s", id);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		MonkeyStrategy monkeyStrategy = monkeyStrategyService.findOneForTeam(id, teamName);
		if (monkeyStrategy == null) {
			final String error = "Unable to delete as Monkeystrategy with id: " + id + " not found";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (!AppUtil.getSuperUser().equalsIgnoreCase(monkeyStrategy.getTeamName())
				&& !monkeyStrategy.getTeamName().equalsIgnoreCase(teamName)) {
			final String error = "MonkeyStrategy can be deleted only by the owning Team "
					+ monkeyStrategy.getTeamName();
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		monkeyStrategyService.delete(monkeyStrategy);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
