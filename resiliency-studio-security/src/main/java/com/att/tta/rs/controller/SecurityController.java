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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.att.tta.rs.model.User;
import com.att.tta.rs.model.UserPrivilege;
import com.att.tta.rs.service.SecurityService;
import com.att.tta.rs.util.MessageWrapper;


/**
 * This controller class provides REST APIs for Authentication and Authorization features.
 * @author ak983d
 *
 */
@EnableWebMvc
@RestController
@RequestMapping("/api")
public class SecurityController {
	public static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

	@Autowired
	SecurityService securityService;

	/**
	 * This method is used to Authenticate a user based on provided User ID and pwdKey.
	 * @param userDTO
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/session/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> authenticateUser(@RequestBody User userDTO) {

		if (!securityService.authenticateUser (userDTO)){
			final String error = "Login failed with entered credential information. Pls try again !!.";
			logger.error(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.UNAUTHORIZED, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}else{
			final String error = "Login successful !!.";
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.OK, error, "");
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
	}
	
	
	/**
	 * This method is used to Authorize the user by providing a list of roles information for given User ID.
	 * @param userID
	 * @return
	 */
	@RequestMapping(value = "/user/{userID}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> authorizeUser(@PathVariable("userID") String userID) {
		
		List<UserPrivilege> privilege = securityService.authorizeUser(userID);
		
		if (null == privilege){
			final String error = "No Roles have been setup for provided user ID: " + userID;
			logger.error(error);
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.UNAUTHORIZED, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		return new ResponseEntity<>(privilege, HttpStatus.OK);
	}
}
