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

import com.att.tta.rs.model.FailurePoint;
import com.att.tta.rs.model.FailurePointAdapter;
import com.att.tta.rs.service.FailurePointService;
import com.att.tta.rs.service.MonkeyStrategyService;
import com.att.tta.rs.service.TeamUserService;
import com.att.tta.rs.util.MessageWrapper;
import com.google.common.collect.Lists;

/**
 * This Class provides certain REST APIs to perform CRUD operations on
 * FailurePoint repository.
 * 
 * @author mb6872,ak983d
 *
 */
@RestController
public class FailurePointRestController {
	private static final Logger logger = LoggerFactory.getLogger(FailurePointRestController.class);

	@Autowired
	FailurePointService failurePointService;

	@Autowired
	TeamUserService userDetailsService;

	@Autowired
	MonkeyStrategyService monkeyStrategyService;

	/**
	 * This API returns list of all FailurePoint objects available in
	 * FailurePoint Repository.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoints/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> listAllFailurePoints() {
		List<FailurePoint> failurePoints = Lists.newArrayList(failurePointService.findAll());
		if (failurePoints.isEmpty()) {
			final String error = " No FailurePoint(s) found !!!";
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(failurePoints, HttpStatus.OK);
	}

	/**
	 * This API returns FailurePoint object by given Id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoints/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getFailurePoint(@PathVariable("id") String id) {
		FailurePoint failurePoint = failurePointService.findOne(id);
		if (failurePoint == null) {
			final String error = "FailurePoint Not found for id " + id;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(failurePoint, HttpStatus.OK);
	}

	/**
	 * This API returns FailurePoint object by given Name
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoints/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getFailurePointsByName(@PathVariable("name") String name) {
		FailurePoint failurePoint = failurePointService.findByName(name);
		if (failurePoint == null) {
			final String error = " FailurePoint Not found for name " + name;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(failurePoint, HttpStatus.OK);
	}

	/**
	 * This API returns all FailurePoints by given Category.
	 * 
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoints/category/{category}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getFailurePointByCategory(@PathVariable("category") String category) {
		List<FailurePoint> failurePoints = Lists.newArrayList(failurePointService.findByCategory(category));
		if (failurePoints == null || failurePoints.isEmpty()) {
			final String error = "failurePoints not found with category " + category;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(failurePoints, HttpStatus.OK);
	}

	/**
	 * This API returns all FailurePoints by Role
	 * 
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoints/role/{role}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getFailurePointByRole(@PathVariable("role") String role) {
		List<FailurePoint> failurePoints = Lists.newArrayList(failurePointService.findByRole(role));
		if (failurePoints == null || failurePoints.isEmpty()) {
			final String error = "failurePoints not found for  role " + role;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(failurePoints, HttpStatus.OK);
	}

	/**
	 * This API creates a FailurePoint.
	 * 
	 * @param failurePoint
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoint/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> addFailurePoint(@RequestBody FailurePoint failurePoint,
			UriComponentsBuilder ucBuilder) {
		logger.debug("Creating a failurePoint with name:" + failurePoint.getName());

		if (failurePoint.getName() == null || "".equals(failurePoint.getName().trim())) {
			final String error = "FailurePoint name cannot be empty";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		if (failurePointService.insert(failurePoint) == null) {
			final String error = "FailurePoint already exist with name " + failurePoint.getName();
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		logger.debug("failurePoint saved in ES with id -->" + failurePoint.getId());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/failurePoint/{id}").buildAndExpand(failurePoint.getId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * This API does bulk FailurePoint insertion.
	 * 
	 * @param failurePointadapter
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoints/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> bulkaddFailurePoint(@RequestBody FailurePointAdapter failurePointadapter) {

		if (failurePointadapter == null) {
			final String error = "NULL request received to add failurePoint";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		logger.debug("bulk inserting failurePoints: %s", failurePointadapter.getFailurePoints().size());

		for (FailurePoint failurePoint : failurePointadapter.getFailurePoints()) {
			if (failurePoint.getName() == null || "".equals(failurePoint.getName().trim())) {
				final String error = "FailurePoint name cannot be empty";
				logger.debug(error);
				final MessageWrapper apiError = new MessageWrapper(HttpStatus.BAD_REQUEST, error, error);
				return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
			}

			if (failurePointService.insert(failurePoint) == null) {
				final String error = "FailurePoint with name " + failurePoint.getName() + " already exist";
				logger.debug(error);
				final MessageWrapper apiError = new MessageWrapper(HttpStatus.CONFLICT, error, error);
				return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
			}

			logger.debug("failurePoint saved in ES with id: " + failurePoint.getId());
		}
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	/**
	 * This API updates a FailurePoint
	 * 
	 * @param id
	 * @param toModifyFailurePoint
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoints/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> updateFailurePoint(@PathVariable("id") String id,
			@RequestBody FailurePoint toModifyFailurePoint) {
		logger.debug("Updating FailurePoint : %s ", id);
		FailurePoint currentFailurePoint = failurePointService.findOne(id);
		if (currentFailurePoint == null) {
			final String error = "FailurePoint with id " + id + " not found";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		toModifyFailurePoint.setId(id);
		failurePointService.update(toModifyFailurePoint);
		return new ResponseEntity<>(toModifyFailurePoint, HttpStatus.OK);
	}

	/**
	 * This API deletes a FailurePoint
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoints/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteFailurePoint(@PathVariable("id") String id) {
		logger.debug("Deleting FailurePoint with id : %s ", id);

		FailurePoint failurePoint = failurePointService.findOne(id);
		if (failurePoint == null) {
			final String error = "Unable to delete. FailurePoint not found with id " + id;
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		failurePointService.delete(failurePoint);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * This API deletes All FailurePoint
	 * 
	 * @return
	 */
	@RequestMapping(value = "/api/failurepoints/", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<Object> deleteAllFailurePoints() {
		logger.debug("Deleting All FailurePoints");
		failurePointService.deleteAllFailurePoint();
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
