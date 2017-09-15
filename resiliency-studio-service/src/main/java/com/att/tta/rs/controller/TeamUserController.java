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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.TeamUserService;
import com.att.tta.rs.util.MessageWrapper;
import com.google.common.collect.Lists;

/**
 * This Class provides certain REST APIs to perform CRUD operations on TeamUser repository. 
 * @author ak983d
 *
 */
@RestController
public class TeamUserController {
	private static final Logger logger = LoggerFactory.getLogger(TeamUserController.class);
	
	@Autowired
	TeamUserService userDetailsService;
	
	/**
	 * This API returns list of all users available in TeamUser repository. 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/users/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllUsers() {
		logger.debug("Fetching All Users ");
		List<TeamUser> userList = Lists.newArrayList(userDetailsService.findAll());
		if (userList == null) {
			final String error = " No User Found in TeamUser repository";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}
	

	/**
	 * This API returns TeamUser object of a user present in Request Session object.
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/user/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCurrentTeamOfUser(HttpServletRequest request) {
		logger.debug("Fetching Current Team for user name.");
		TeamUser teamUser = userDetailsService.getCurrentTeamForUser(request);
		if (teamUser == null) {
			final String error = "TeamUser Not found for a User.";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(teamUser, HttpStatus.OK);
	}
	

	/**
	 * This API updates a TeamName of a User present in Request Session object.
	 * @param request
	 * @param newteamname
	 * @return
	 */
	@RequestMapping(value = "/api/user/{newteamname}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Object> updateTeamOfUser(HttpServletRequest request,
			@PathVariable("newteamname") String newteamname) {		
		if (newteamname == null || "".equals(newteamname.trim())) {
			final String error = "TeamUser cannot be modified as new Team Name is null";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		
		TeamUser teamUser = userDetailsService.getCurrentTeamForUser(request);
		if (teamUser == null) {
			final String error = "TeamUser object not found in ES Repository.";
			logger.debug(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		logger.debug("Updating User %s team : %s " , teamUser.getUserName() ,newteamname);
		teamUser.setTeamName(newteamname);
		userDetailsService.update(teamUser);
		return new ResponseEntity<>(teamUser, HttpStatus.OK);
	}
}
