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

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.att.tta.rs.model.TeamUser;

/**
 * This interface provides the declaration of CRUD operations for TeamUser repository. 
 * Also it is extension of Spring UserDetailsService.
 * @author ak983d
 *
 */
public interface TeamUserService extends UserDetailsService {

	/**
	 * This method updates TeamUser object.
	 * @param teamUser
	 * @return
	 */
	TeamUser update(TeamUser teamUser);

	/**
	 * This method inserts TeamUser object.
	 * @param teamUser
	 * @return
	 */
	TeamUser insert(TeamUser teamUser);
	
	/**
	 * This method deletes Team User object.
	 * @param teamUser
	 */
	void delete(TeamUser teamUser);
	

	/**
	 * This method deletes all Team User object from ES.
	 */
	void deleteAllTeamUsers();
	
	/**
	 * This method returns count of TeamUser objects available in ES.
	 * @return
	 */
	long count();
	
	/**
	 * This method returns the TeamUser object for given ID.
	 * @param id
	 * @return
	 */
	TeamUser findOne(String id);
	
	/**
	 * This method returns all TeamUser objects available in ES.
	 * @return
	 */
	Iterable<TeamUser> findAll();
	
	/**
	 * This method returns TeamUser objects list which are matching with given Team name
	 * @param name
	 * @param pageable
	 * @return
	 */
	Page<TeamUser> findByTeamName(String name, Pageable pageable);
	
	/**
	 * This method returns TeamUser object for given Team name
	 * @param name
	 * @return
	 */
	TeamUser findByTeamName(String name);
	
	/**
	 * This method checks for Team User object presence in ES.
	 * @param teamUser
	 * @return
	 */
	boolean isTeamExist(TeamUser teamUser);
	
	/**
	 * This method checks if User is exist in ES.
	 * @param teamUser
	 * @return
	 */
	boolean isUserExist(TeamUser teamUser);
	
	/**
	 * This method checks if Team User object is exist in ES.
	 * @param teamUser
	 * @return
	 */
	boolean isTeamUserExist(TeamUser teamUser);	

	/**
	 * This method returns TeamUser objects list which are matching with given User name
	 * @param name
	 * @param pageable
	 * @return
	 */
	Page<TeamUser> findByUserName(String name, Pageable pageable);
	
	/**
	 * This method returns TeamUser object for given User name
	 * @param name
	 * @return
	 */
	TeamUser findByUserName(String name);
	
	/**
	 * This method return TeamUser object for given Team Name and User Name.
	 * @param teamName
	 * @param userName
	 * @return
	 */
	TeamUser findByTeamNameAndUserName(String teamName, String userName);

	/**
	 * This method return TeamUser object for given Default flag and User Name.
	 * @param name
	 * @param defaultFlag
	 * @return
	 */
	TeamUser findByUserNameAndDefaultFlag(String name, String defaultFlag);

	/**
	 * This method returns TeamUser object of a user.
	 * @param request
	 * @return
	 */
	TeamUser getCurrentTeamForUser(HttpServletRequest request);
}
