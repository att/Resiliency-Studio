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
import javax.validation.Valid;

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
import com.att.tta.rs.model.FuelDiscover;
import com.att.tta.rs.model.HardwareDetails;
import com.att.tta.rs.model.Server;
import com.att.tta.rs.service.ApplicationService;
import com.att.tta.rs.service.EnvironmentDiscoveryService;
import com.att.tta.rs.service.FuelEnvDiscoveryServiceImpl;
import com.att.tta.rs.service.TeamUserService;
import com.att.tta.rs.util.AppUtil;
import com.att.tta.rs.util.EUtil;
import com.att.tta.rs.util.MessageWrapper;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This Class provides certain REST APIs to perform CRUD operations on
 * Application repository.
 * 
 * @author ak983d
 *
 */
@RestController
@Api(value = "Application Rest Controller", description = "This REST controller provides REST APIs for performing CRUD Operation on Application Repository")
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
	@ApiOperation(value = "This API returns all Application Objects present in Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned all Application Objects"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Application object found") })
	@RequestMapping(value = "/api/applications/", method = RequestMethod.GET)
	public ResponseEntity<Object> getAllApplication() {
		List<Application> applications = Lists.newArrayList(applicationService.findAllApplications());
		if (applications == null || applications.isEmpty()) {
			final String error = " No Applications found !!!  ";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(decryptAllApplicationPassword(applications), HttpStatus.OK);
	}

	/**
	 * This API returns count of Application objects available in Application
	 * Repository.
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "This API returns count of Application objects available in Elastic Search for team name", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned count of Application objects for team name"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "The resource trying to reach is not found") })
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
	@ApiOperation(value = "This API returns list of all Application objects present in Elastic Search for given team", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned all Application Objects for given team"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Application object found for given team") })
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

		return new ResponseEntity<>(decryptAllApplicationPassword(applications), HttpStatus.OK);
	}

	/**
	 * This method is used to decrypt a Private Key and Password.
	 * 
	 * @param application
	 * @return
	 */
	private List<Application> decryptAllApplicationPassword(List<Application> applications) {
		for (Application application : applications) {
			String envName = application.fields().get("environmentName");
			if (null != application.getEnvironmentMap() && null!= application.getEnvironmentMap().get(envName)) {
				List<Server> serverList = application.getEnvironmentMap().get(envName).getServerList();
				for (Server server : serverList) {
					if (null != server.getPassword()) {
						server.setPassword(EUtil.decrypt(server.getPassword()));
					}

					if (null != server.getPrivatekey()) {
						server.setPrivatekey(EUtil.decrypt(server.getPrivatekey()));
					}
				}
			}
		}
		return applications;
	}

	/**
	 * This API returns single Application Object for given Application Id
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "This API returns single Application Object present in Elastic Search for given App ID", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned single Application Object for given App ID"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Application object found for given App ID") })
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
		return new ResponseEntity<>(decryptPassword(application), HttpStatus.OK);
	}

	/**
	 * This API returns single Application Object for given Application Name
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	@ApiOperation(value = "This API returns single Application Object present in Elastic Search for given App Name", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned Application Object for given App Name"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Application object found for given App Name") })
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
		return new ResponseEntity<>(decryptPassword(application), HttpStatus.OK);
	}

	/**
	 * This method is used to decrypt a Private Key and Password.
	 * 
	 * @param application
	 * @return
	 */
	private Application decryptPassword(Application application) {
		String envName = application.fields().get("environmentName");
		if (null != application.getEnvironmentMap() && null != application.getEnvironmentMap().get(envName)) {
			List<Server> serverList = application.getEnvironmentMap().get(envName).getServerList();
			for (Server server : serverList) {
				if (null != server.getPassword()) {
					server.setPassword(EUtil.decrypt(server.getPassword()));
				}

				if (null != server.getPrivatekey()) {
					server.setPrivatekey(EUtil.decrypt(server.getPrivatekey()));
				}
			}
		}
		return application;
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
	@ApiOperation(value = "This API returns Application Objects present in Elastic Search for given App ID and App Category", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Returned Application Object for given App ID and App Category"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Application object found for given App ID and App Category") })
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

		return new ResponseEntity<>(decryptPassword(application), HttpStatus.OK);
	}

	/**
	 * This API creates an Application object in the repository.
	 * 
	 * @param request
	 * @param application
	 * @param ucBuilder
	 * @return
	 */
	@ApiOperation(value = "This API inserts an Application Object into Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Application object successfully inserted into ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Add operation"),
			@ApiResponse(code = 409, message = "Application with same name is already exist"),
			@ApiResponse(code = 404, message = "The resource trying to reach is not found"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
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
	@ApiOperation(value = "This API do bulk insertion of an Application Objects into Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Application objects successfully inserted into ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Add operation"),
			@ApiResponse(code = 409, message = "Application with same name is already exist"),
			@ApiResponse(code = 404, message = "The resource trying to reach is not found"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
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
	@ApiOperation(value = "This API updates an Application Objects into Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Application objects successfully updated into ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Update operation"),
			@ApiResponse(code = 409, message = "Application can be changed only by the owning team"),
			@ApiResponse(code = 404, message = "Applicaiton object not found in ES for given App ID"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
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
	@ApiOperation(value = "This API deletes an Application Objects from Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Application object successfully deleted from ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Delete operation"),
			@ApiResponse(code = 409, message = "Application can be delted only by the owning team"),
			@ApiResponse(code = 404, message = "Applicaiton object not found in ES for given App ID"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
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
	@ApiOperation(value = "This API deletes all Application Objects from Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All Application object successfully deleted from ES"),
			@ApiResponse(code = 401, message = "User is not authorized to perform Delete operation"),
			@ApiResponse(code = 400, message = "Input Request object is not valid") })
	@RequestMapping(value = "/api/applications/", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteAllApplications() {
		logger.debug("Deleting All Applications");
		applicationService.deleteAllApplications();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * This API discovers Server details by executing UNIX script using SSH
	 * connection
	 * 
	 * @param request
	 * @param discoverRequest
	 * @return
	 */
	@ApiOperation(value = "This API returns Server Hardware details after discovery", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned Server Hardware details after discovery"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 400, message = "Input Request object is not valid"),
			@ApiResponse(code = 404, message = "No Server Hardware details found") })
	@RequestMapping(value = "/api/applications/discoverServerDetails/", method = RequestMethod.POST, produces = "application/json", headers = "Accept=application/json")
	public ResponseEntity<Object> discoverServerDetailsForApplication(HttpServletRequest request,
			@RequestBody @Valid FuelDiscover discoverRequest) {
		logger.debug("Discovering Server details for  Host " + discoverRequest.getHostName());

		EnvironmentDiscoveryService environmentDiscoveryService = new FuelEnvDiscoveryServiceImpl();
		HardwareDetails hardwareInfo = environmentDiscoveryService.discoverServerHardwareDetails(discoverRequest);

		if (null == hardwareInfo) {
			final String error = "No Hardware details found for server";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(hardwareInfo, HttpStatus.OK);
	}

}
