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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.att.tta.rs.model.EventRecorder;
import com.att.tta.rs.model.EventStatusUpdate;
import com.att.tta.rs.service.EventService;
import com.att.tta.rs.service.TeamUserService;
import com.att.tta.rs.util.MessageWrapper;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This Class provides certain REST APIs to perform CRUD operations on Event
 * repository.
 * 
 * @author mb6872,ak983d
 *
 */
@RestController
@Api(value = "Event Rest Controller", description = "This REST controller provides REST APIs for performing CRUD Operation on Event Repository")
public class EventRestController {

	private static final Logger logger = LoggerFactory.getLogger(EventRestController.class);
	private static final String TIMESTAMP = "@timestamp"; 
	
	/**
	 * instance of EventService
	 */
	@Autowired
	EventService eventService;

	/**
	 * instance of TeamUserService
	 */
	@Autowired
	TeamUserService userDetailsService;

	/**
	 * This API returns list of all Event objects available in Event Repository.
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "This API returns all Event Objects present in Elastic Search", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned all Event Objects"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Event object found") })
	@RequestMapping(value = "/api/events/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> listEvents() {
		List<EventRecorder> events = Lists.newArrayList(eventService.findAll());
		if (events.isEmpty()) {
			final String error = "No events found !!!";
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

	/**
	 * This API returns all Event records for a User's current team.
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "This API returns list of all Event objects present in Elastic Search for given team", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned all Application Event for given team"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Event object found for given team") })
	@RequestMapping(value = "/api/events/team/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> listAllEvents(HttpServletRequest request) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		List<EventRecorder> events = Lists.newArrayList(eventService.findAllByTeamName(teamName));
		if (events.isEmpty()) {
			final String error = "No events found for team " + teamName;
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		Map<String, List<EventRecorder>> eventListMap = new HashMap<>();
		for (EventRecorder eventRecorder : events) {
			org.joda.time.DateTime dt = new org.joda.time.DateTime(eventRecorder.field(TIMESTAMP));
			eventRecorder.fields().put(TIMESTAMP, dt.toString("yyyy-MM-dd HH:mm:ss.SSS"));
			String eventTimestamp = eventRecorder.field(TIMESTAMP);
			if (eventListMap.containsKey(eventTimestamp)) {
				eventListMap.get(eventTimestamp).add(eventRecorder);
			} else {
				List<EventRecorder> eventRecorderList = new ArrayList<>();
				eventRecorderList.add(eventRecorder);
				eventListMap.put(eventTimestamp, eventRecorderList);
			}
		}
		eventListMap.forEach((k, v) -> {
			v.forEach(eventRecorder -> {
				eventRecorder.setExecSequence(
						(null == eventRecorder.getExecSequence() || eventRecorder.getExecSequence().isEmpty()) ? "0"
								: eventRecorder.getExecSequence());
			});
		});
		eventListMap.forEach((k, v) -> v.sort(Comparator.comparing(EventRecorder::getExecSequence)));

		List<EventRecorder> finalEventRecorderList = new ArrayList<>();
		eventListMap.forEach((k, v) -> {
			EventRecorder newEvent = new EventRecorder();
			int i = 0;
			for (EventRecorder eventRecorder : v) {
				String eventStatus = "*** Script Execution O/P for Monkey Strategy: "
						+ eventRecorder.field("monkeyStrategy") + " *** \n";
				if (i == 0) {
					newEvent = eventRecorder;
					newEvent.setEventStatus(eventStatus + eventRecorder.getEventStatus() + "\n\n");
				} else {
					newEvent.setEventStatus(newEvent.getEventStatus() + eventStatus + eventRecorder.getEventStatus()+ "\n\n");
					newEvent.fields().put("monkeyStrategy",
							newEvent.field("monkeyStrategy") + ", " + eventRecorder.field("monkeyStrategy"));
					newEvent.fields().put("monkeyType",
							newEvent.field("monkeyType") + ", " + eventRecorder.field("monkeyType"));
					newEvent.setExecStatus("Failed".equalsIgnoreCase(eventRecorder.getExecStatus()) ? "Failed"
							: newEvent.getExecStatus());
				}
				i++;
			}
			finalEventRecorderList.add(newEvent);
		});
		return new ResponseEntity<>(finalEventRecorderList, HttpStatus.OK);
	}

	/**
	 * This API returns Event object for given Id
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "This API returns single Event Objects present in Elastic Search for given Event ID", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned single Event Object for given Event ID"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Event object found for given Event ID") })
	@RequestMapping(value = "/api/events/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> listEvent(HttpServletRequest request, @PathVariable("id") String id) {
		logger.debug("Listing event for id %s", id);
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		EventRecorder event = eventService.findOneForTeam(id, teamName);
		if (event == null) {
			final String error = "No event found  for team " + teamName + " and id " + id;
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	/**
	 * This API returns the latest Event Status for given Event Id
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "This API returns Event Status Objects present in Elastic Search for given Event ID", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned Event Status Object for given Event ID"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Event Status object found for given Event ID") })
	@RequestMapping(value = "/api/events/status/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> getstatusupdateforevent(HttpServletRequest request, @PathVariable("id") String id) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		EventRecorder event = eventService.findOneForTeam(id, teamName);
		if (event == null) {
			final String error = "No events found !!! for team " + teamName;
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}
		EventStatusUpdate update = event.getEventStatusUpdate();
		if (update == null)
			logger.debug(" No update");
		return new ResponseEntity<>(update, HttpStatus.OK);
	}

	/**
	 * This API returns count of Events under a User's current team.
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "This API returns count of Event objects available in Elastic Search for given team name", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned count of Event objects for given team name"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "The resource trying to reach is not found") })
	@RequestMapping(value = "/api/events/count/", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> countByTeamName(HttpServletRequest request) {
		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		logger.debug("count of Events for team %s", teamName);
		Long count = eventService.countByTeamName(teamName);
		return new ResponseEntity<>(count.toString(), HttpStatus.OK);
	}

	/**
	 * This API provides the latest Event List for a given application name.
	 * 
	 * @param request
	 * @param applicationName
	 * @return
	 */
	@ApiOperation(value = "This API returns Latest Events per Scenario present in Elastic Search for given App Name", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Returned Latest Events per Scenario for given App Name"),
			@ApiResponse(code = 401, message = "User is not authorized to view requested object"),
			@ApiResponse(code = 404, message = "No Event object found for given App Name") })
	@RequestMapping(value = "/api/events/latestevent/{applicationName}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> listLatestEventsByAppName(HttpServletRequest request,
			@PathVariable("applicationName") String applicationName) {
		logger.debug("Listing Latest Events for given application: %s", applicationName);

		final String teamName = userDetailsService.getCurrentTeamForUser(request).getTeamName();
		List<EventRecorder> events = Lists.newArrayList(eventService.findByAppName(teamName, applicationName));
		Map<String, EventRecorder> latestEventMap = new HashMap<>();
		if (!events.isEmpty()) {
			setLatestEventMap(latestEventMap, events);
		}
		if (latestEventMap.isEmpty()) {
			final String error = " No events found for team " + teamName + " and for Application Name: "
					+ applicationName;
			final MessageWrapper apiError = new MessageWrapper(HttpStatus.NOT_FOUND, error, error);
			return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
		}

		return new ResponseEntity<>(latestEventMap, HttpStatus.OK);
	}

	/**
	 * This method creates a Map object containing latest events for each
	 * Scenario name.
	 * 
	 * @param latestEventMap
	 * @param events
	 */
	private void setLatestEventMap(Map<String, EventRecorder> latestEventMap, List<EventRecorder> events) {
		for (EventRecorder event : events) {
			if (latestEventMap.containsKey(event.field("name"))) {
				eventDateComparison(latestEventMap, event);
			} else {
				latestEventMap.put(event.field("name"), event);
			}
		}
	}

	private void eventDateComparison(Map<String, EventRecorder> latestEventMap, EventRecorder event) {
		EventRecorder prevEvent = latestEventMap.get(event.field("name"));
		Date prevEventDt;
		Date curEventDt;
		try {
			prevEventDt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(prevEvent.field(TIMESTAMP));
			curEventDt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.field(TIMESTAMP));
			if (null != prevEventDt && null != curEventDt && (curEventDt.getTime() - prevEventDt.getTime()) > 0) {
				latestEventMap.put(event.field("name"), event);
			}
		} catch (ParseException e) {
			logger.error("Parsing error while parsing Timestamp: ", e);
		}
	}
}
