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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.tta.rs.data.es.repository.EventRepository;
import com.att.tta.rs.model.EventRecorder;
import com.att.tta.rs.model.EventStatusUpdate;
import com.att.tta.rs.util.AppUtil;

/** 
 * Implementation class for {@link EventService}
 * @author mb6872
 *
 */
@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService {
	
	/**
	 *  Instance of EventRepository
	 */
	private EventRepository eventRepository;	

	@Autowired
	public void setEventRepository(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	@Override
	public EventRecorder save(EventRecorder event) {
		return eventRepository.save(event);
	}

	@Override
	public EventRecorder findOne(String id) {
		return eventRepository.findOne(id);
	}

	@Override
	public Iterable<EventRecorder> findAllByTeamName(String teamName) {
		if (teamName.trim().equals(AppUtil.getSuperUser())) {
			return eventRepository.findAll();
		}
		else {
			return eventRepository.findByTeamName(teamName,
					new PageRequest(0, 9999));	
		}
	}
	
	@Override
	public Iterable<EventRecorder> findAll() {
			return eventRepository.findAll();
	}

	@Override
	public Page<EventRecorder> findByMonkeyTypeUsingCustomQuery(String name, Pageable pageable) {
		return eventRepository.findByMonkeyTypeUsingCustomQuery(name, pageable);
	}

	@Override
	public long count() {
		return eventRepository.count();
	}

	@Override
	public void delete(EventRecorder event) {
		eventRepository.delete(event);
	}

	@Override
	public Long countByTeamName(String teamName) {
		if (teamName.trim().equals(AppUtil.getSuperUser()))
			return eventRepository.count();
		else
			return eventRepository.countByTeamName(teamName);
	}

	@Override
	public Map<String, String> getLatestUpdate(String eventId) {
		EventRecorder evt = eventRepository.findOne(eventId);
		EventStatusUpdate statusUpdate = evt.getEventStatusUpdate();
		HashMap<String, String> latestStatus = new HashMap<>();
		latestStatus.put(statusUpdate.getTimeStamp(), statusUpdate.getStatusString());
		return latestStatus;
	}

	@Override
	public EventRecorder findOneForTeam(String id, String teamName) {
		if (teamName.trim().equals(AppUtil.getSuperUser()))
			return eventRepository.findOne(id);
		else
			return eventRepository.findOneForTeam(id, teamName);
	}

	/**
	 * This Service implementation provides the Event List for a given application and team.
	 * @param teamName
	 * @param applicationName
	 * @return
	 */
	@Override
	public Iterable<EventRecorder> findByAppName(String teamName, String appName) {
		return eventRepository.findByAppName(teamName, appName,
				new PageRequest(0, 9999));
	}
}
