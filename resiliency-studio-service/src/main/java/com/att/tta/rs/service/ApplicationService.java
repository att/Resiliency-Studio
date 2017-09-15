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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.att.tta.rs.model.Application;

/**
 * This interface provides the declaration of CRUD operations for Application
 * repository.
 * 
 * @author ak983d
 *
 */
public interface ApplicationService {

	/**
	 * This method returns count of Application objects available in ES.
	 * 
	 * @return
	 */
	long count();

	/**
	 * This method returns count of Application objects available in ES under
	 * given Team Name.
	 * 
	 * @param teamName
	 * @return
	 */
	Long countByTeamName(String teamName);

	/**
	 * This method checks for Application object presence in ES under given
	 * teamName.
	 * 
	 * @param application
	 * @param teamName
	 * @return
	 */
	boolean isApplicationExistAndTeamName(Application application, String teamName);

	/**
	 * This method returns Application objects having given ID and Team name
	 * 
	 * @param id
	 * @param teamName
	 * @return
	 */
	Application findOneForTeam(String id, String teamName);

	/**
	 * This method returns Application objects having given Application Name and
	 * Team name
	 * 
	 * @param name
	 * @param teamName
	 * @return
	 */
	Application findByApplicationNameAndTeamName(String name, String teamName);

	/**
	 * This method returns Application objects having given Application Name,
	 * Category and Team name
	 * 
	 * @param name
	 * @param teamName
	 * @param category
	 * @return
	 */
	Application findByApplicationNameAndCategoryAndTeamName(String name, String teamName, String category);

	/**
	 * This method returns Application objects list which are matching with
	 * given Application Name and Team name
	 * 
	 * @param name
	 * @param teamName
	 * @param pageable
	 * @return
	 */
	Page<Application> findByApplicationNameAndTeamName(String name, String teamName, Pageable pageable);

	/**
	 * This method returns Application objects list which are matching with
	 * given Application Name and Team name
	 * 
	 * @param name
	 * @param teamName
	 * @param pageable
	 * @return
	 */
	Page<Application> findByApplicationNameUsingCustomQueryAndTeamName(String name, String teamName, Pageable pageable);

	/**
	 * This method returns all Application objects available in ES.
	 * 
	 * @return
	 */
	Iterable<Application> findAllApplications();

	/**
	 * This method returns all Application objects available in ES under given
	 * team name.
	 * 
	 * @param teamName
	 * @return
	 */
	Iterable<Application> findAllForTeam(String teamName);

	/**
	 * This method inserts Application object.
	 * 
	 * @param application
	 * @param teamName
	 * @return
	 */
	Application insertForTeam(Application application, String teamName);

	/**
	 * This method updates Application object.
	 * 
	 * @param application
	 * @return
	 */
	Application update(Application application);

	/**
	 * This method deletes Application object.
	 * 
	 * @param application
	 */
	void delete(Application application);

	/**
	 * This method deletes all Application objects available in ES.
	 */
	void deleteAllApplications();
}
