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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.att.ajsc.logging.AjscEelfManager;
import com.att.eelf.configuration.EELFLogger;
import com.att.tta.rs.model.Application;
import com.att.tta.rs.model.ApplicationAdapter;
import com.att.tta.rs.service.ApplicationService;
import com.att.tta.rs.service.TeamUserService;
import com.att.tta.rs.util.AppUtil;
import com.att.tta.rs.util.MessageWrapper;
import com.google.common.collect.Lists;

/**
 * This Class provides certain REST APIs to perform CRUD operations on
 * Application repository.
 * 
 * @author ak983d
 *
 */
@RestController
public class ApplicationRestController {
	private static EELFLogger logger = AjscEelfManager.getInstance().getLogger(ApplicationRestController.class);

	@Autowired
	ApplicationService applicationService;

	@Autowired
	TeamUserService userDetailsService;

	/**
	 * This API returns list of all Application objects available in Application
	 * Repository.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/applications/", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllApplication() {
		List<Application> applications = Lists.newArrayList(applicationService.findAllApplications());
		if (applications == null || applications.isEmpty()) {
			final String error = " No Applications found !!!  ";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(applications, HttpStatus.OK);
	}

	/**
	 * This API returns count of Application objects available in Application
	 * Repository.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/applications/count/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> countByTeamName(HttpServletRequest request) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Long count = applicationService.countByTeamName(teamName);
		logger.debug("count of applications for team -->" + teamName);
		return new ResponseEntity<>(count.toString(), HttpStatus.OK);
	}

	/**
	 * This API returns list of all Application objects available in Application
	 * Repository under given Team Name.
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/applications/team/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getAllApplicationsForTeam(HttpServletRequest request) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();

		List<Application> applications = Lists.newArrayList(applicationService.findAllForTeam(teamName));

		if (applications == null || applications.isEmpty()) {
			final String error = " No Applications found !!! for Team " + teamName;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(applications, HttpStatus.OK);
	}

	/**
	 * This API returns single Application Object for given Application Id
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/applications/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getApplication(HttpServletRequest request, @PathVariable("id") String id) {
		logger.debug("Fetching Application Object with id " + id);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Application application = applicationService.findOneForTeam(id, teamName);

		if (application == null) {
			final String error = "Application Not found for id " + id;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(application, HttpStatus.OK);
	}

	/**
	 * This API returns single Application Object for given Application Name
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/api/applications/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getApplicationsByName(HttpServletRequest request, @PathVariable("name") String name) {
		logger.debug("Fetching Application object with name " + name);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Application application = applicationService.findByApplicationNameAndTeamName(name, teamName);

		if (application == null) {
			final String error = "Application Not found with name: " + name + " for Team: " + teamName;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(application, HttpStatus.OK);
	}

	/**
	 * This API returns single Application Object for given Application Id and
	 * Category
	 * 
	 * @param request
	 * @param name
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/api/applications/{name}/{category}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getApplicationByNameAndCategory(HttpServletRequest request,
			@PathVariable("name") String name, @PathVariable("category") String category) {

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Application application = applicationService.findByApplicationNameAndCategoryAndTeamName(name, category,
				teamName);

		if (application == null) {
			final String error = "application with name " + name + " and category " + category + " not found "
					+ " for Team " + teamName;
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		return new ResponseEntity<>(application, HttpStatus.OK);
	}

	/**
	 * This API creates an Application object in the repository.
	 * 
	 * @param request
	 * @param application
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/api/application/", method = RequestMethod.POST, produces = "application/json", consumes = MediaType.ALL_VALUE)
	public ResponseEntity<Object> addApplication(HttpServletRequest request, @RequestBody Application application,
			UriComponentsBuilder ucBuilder) {

		if (application == null) {
			final String error = "NULL request received to add application";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (application.getApplicationName() == null || "".equals(application.getApplicationName().trim())) {
			final String error = "Application name is empty";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		application.setTeamName(teamName);

		if (applicationService.insertForTeam(application, teamName) == null) {
			final String error = "Application with name " + application.getApplicationName() + " already exist";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/applications/{id}").buildAndExpand(application.getId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * This API do a bulk insertion of Application objects.
	 * 
	 * @param request
	 * @param applicationadapter
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/api/applications/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> bulkaddApplication(HttpServletRequest request,
			@RequestBody ApplicationAdapter applicationadapter) {

		if (applicationadapter == null) {
			final String error = "NULL request received to add application";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();

		for (Application application : applicationadapter.getApplications()) {
			if (application.getApplicationName() == null || "".equals(application.getApplicationName().trim())) {
				final String error = "Application name cannot be empty";
				logger.debug(error);
				final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
				return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
			}

			application.setTeamName(teamName);

			if (applicationService.insertForTeam(application, teamName) == null) {
				final String error = "Application with name " + application.getApplicationName() + " already exist";
				logger.debug(error);
				final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
				return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
			}

			logger.debug("Application saved in ES with id -->" + application.getId());
		}
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * This API does an update to a Application object for given Application ID
	 * 
	 * @param request
	 * @param id
	 * @param toModifyApplication
	 * @return
	 */
	@RequestMapping(value = "/api/applications/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> updateApplication(HttpServletRequest request, @PathVariable("id") String id,
			@RequestBody Application toModifyApplication) {
		logger.debug("Updating Application with ID: " + id);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();

		if (!AppUtil.getSuperUser().equalsIgnoreCase(toModifyApplication.getTeamName())
				&& !toModifyApplication.getTeamName().equalsIgnoreCase(teamName)) {

			final String error = "Application can be changed only by the owning Team -->"
					+ toModifyApplication.getTeamName() + "<--";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		Application currentApplication = applicationService.findOneForTeam(id, teamName);
		if (currentApplication == null) {
			final String error = "Application with id " + id + " not found";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		toModifyApplication.setId(id);
		toModifyApplication.setTeamName(teamName);
		applicationService.update(toModifyApplication);
		return new ResponseEntity<>(toModifyApplication, HttpStatus.OK);
	}

	/**
	 * This API deletes an Application based on given Application ID
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/applications/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteApplication(HttpServletRequest request, @PathVariable("id") String id) {
		logger.debug("Deleting Application with id: " + id);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		Application application = applicationService.findOneForTeam(id, teamName);

		if (application == null) {
			final String error = "Unable to delete. Application with id " + id + " not found";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (!AppUtil.getSuperUser().equalsIgnoreCase(application.getTeamName())
				&& !application.getTeamName().equalsIgnoreCase(teamName)) {
			final String error = "Application can be deleted only by the owning Team " + application.getTeamName();
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		applicationService.delete(application);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * This API deletes All Applications
	 * 
	 * @return
	 */
	@RequestMapping(value = "/api/applications/", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteAllApplications() {
		logger.debug("Deleting All Applications");
		applicationService.deleteAllApplications();
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
