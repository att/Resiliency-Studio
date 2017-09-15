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

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.att.tta.rs.model.EventRecorder;

/**
 * This interface provides the declaration of CRUD operations for Event
 * repository.
 * 
 * @author mb6872,ak983d
 *
 */
public interface EventService {
	
	/** method to save instance of EventRecorder
	 * @param event
	 * @return
	 */
	EventRecorder save(EventRecorder event);

	/** find elastic search recorder event by Id
	 * @param id
	 * @return
	 */
	EventRecorder findOne(String id);

	/** find all EventRecorder by Teamname
	 * @param teamName
	 * @return
	 */
	Iterable<EventRecorder> findAllByTeamName(String teamName);
	
	/**
	 * Executive Dash board changes
	 *  returns all Events 
	 */
	Iterable<EventRecorder> findAll();

	
	/** find EventRecorder by Monkey Type (using custom query)
	 * 
	 * @param name
	 * @param pageable
	 * @return
	 */
	Page<EventRecorder> findByMonkeyTypeUsingCustomQuery(String name, Pageable pageable);

	/** count total number of EventRecorder
	 * @return
	 */
	long count();

	/** count total number of EventRecorder for given Team Name
	 * @param teamName
	 * @return
	 */
	Long countByTeamName(String teamName);

	/** delete a EventRecorder
	 * @param event
	 */
	void delete(EventRecorder event);

	/** get latest updated status for given event id
	 * @param eventId
	 * @return
	 */
	Map<String, String> getLatestUpdate(String eventId);

	/** find event for given Team and id 
	 * @param id
	 * @param teamName
	 * @return
	 */
	EventRecorder findOneForTeam(String id, String teamName);
	
	/** find events for given team and appname
	 * @param teamName
	 * @param appName
	 * @return
	 */
	Iterable<EventRecorder> findByAppName(String teamName, String appName);
}
