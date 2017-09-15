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

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.att.tta.rs.model.EventRecorder;
import com.att.tta.rs.model.EventStatus;
import com.att.tta.rs.model.Scenario;

/**
 * This interface provides the declaration of CRUD operations for Scenario repository.
 * 
 * @author ak983d
 *
 */
public interface ScenarioService {
	
	/**
	 * This method is to save the Scenario object under given team name
	 * @param scenario
	 * @param teamName
	 * @return
	 */
	Scenario save(Scenario scenario, String teamName);
	
	/**
	 * This method is to save the list of Scenario objects
	 * @param scenarios
	 * @param teamName
	 * @return
	 */
	boolean save(List<Scenario> scenarios, String teamName);
	
	/**
	 * This method is to insert the Scenario object under given team name
	 * @param scenario
	 * @param teamName
	 * @return
	 */
	Scenario insert(Scenario scenario, String teamName);
	
	/**
	 * This method is to update the Scenario object under given team name
	 * @param scenario
	 * @param teamName
	 * @return
	 */
	Scenario update(Scenario scenario, String teamName);
	
	/**
	 * This method is to delete the Scenario object.
	 * @param scenario
	 */
	void delete(Scenario scenario);
	
	/**
	 * This method is to delete all Scenario objects.
	 */
	void deleteAllScenarios();

	/**
	 * This method returns count of Scenarios objects available in ES.
	 * 
	 * @return
	 */
	long count();

	/**
	 * This method returns count of Scenarios objects available in ES for given Team name.
	 * 
	 * @return
	 */
	Long countByTeamName(String teamName);
	
	/**
	 * This method checks for Scenario object presence in ES under given
	 * teamName.
	 * @param scenario
	 * @param teamName
	 * @return
	 */
	boolean isScenarioExistForTeamName(Scenario scenario, String teamName);
	
	/**
	 * This method returns Scenario objects having given Scenario ID 
	 * @param id
	 * @return
	 */
	Scenario findOne(String id);

	/**
	 * This method returns Scenario objects having given Scenario name and Team name
	 * @param name
	 * @param teamName
	 * @return
	 */
	Scenario findByScenarioNameByTeamName(String name, String teamName);
	
	/**
	 * This method returns List of Scenario objects having given Team name
	 * @param teamName
	 * @return
	 */
	Iterable<Scenario> findAllByTeamName(String teamName);
	
	/**
	 * This method returns List of all Scenario objects
	 * @return
	 */
	Iterable<Scenario> findAll();

	/**
	 * This method returns List of Scenario objects having given Team name and application name
	 * @param applicationName
	 * @param teamName
	 * @return
	 */
	List<Scenario> findByApplicationNameByTeamName(String applicationName, String teamName);
	
	
	/**
	 * This method returns List of Scenario objects having given Team name and Scenario name
	 * @param name
	 * @param teamName
	 * @param pageable
	 * @return
	 */
	Page<Scenario> findByScenarioNameByTeamName(String name, String teamName, Pageable pageable);
	
	/**
	 * This method returns List of Scenario objects having given Team name and Scenario name
	 * @param name
	 * @param teamName
	 * @param page
	 * @return
	 */
	Page<Scenario> findScenarioListByScenarioNameByTeamName(String name, String teamName, Pageable page);	

	/**
	 * This method creates an Event in the event repository. 
	 * @param execScenario
	 * @param statusString
	 * @param teamName
	 * @return
	 */
	EventRecorder createEvent(Scenario execScenario, String statusString, String teamName);
	
	
	/**
	 * This method updates an Event with in-progress status. 
	 * @param eventId
	 * @param statusString
	 * @param eventStatus
	 * @param teamName
	 * @return
	 */
	EventRecorder recordEvent(String eventId, String statusString,
			EventStatus eventStatus, String teamName);
	
	/**
	 * This method updates an Event with final status.
	 * @param eventId
	 * @param statusString
	 * @param sventStatus
	 * @param teamName
	 * @return
	 */
	EventRecorder finalizeEvent(String eventId, String statusString,
			EventStatus sventStatus, String teamName);
	
	/**
	 * This method updates an Event with final status and execution status.
	 * @param eventId
	 * @param statusString
	 * @param sventStatus
	 * @param teamName
	 * @param execStatus
	 * @return
	 */
	EventRecorder finalizeEvent(String eventId, String statusString,
			EventStatus sventStatus, String teamName, String execStatus);
}
