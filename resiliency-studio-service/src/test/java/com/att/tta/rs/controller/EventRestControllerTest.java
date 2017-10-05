
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;

import com.att.tta.rs.data.es.repository.EventRepository;
import com.att.tta.rs.data.es.repository.TeamUserRepository;
import com.att.tta.rs.model.EventRecorder;
import com.att.tta.rs.model.EventStatus;
import com.att.tta.rs.model.EventStatusUpdate;
import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.EventServiceImpl;
import com.att.tta.rs.service.TeamUserServiceImpl;
import com.att.tta.rs.util.MessageWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { com.att.tta.rs.Application.class })
public class EventRestControllerTest {

	@Mock
	private EventRepository eventRepository;

	@Mock
	private TeamUserRepository teamUserRepository;

	@InjectMocks
	EventServiceImpl eventService;

	@InjectMocks
	TeamUserServiceImpl userDetailsService;

	@InjectMocks
	EventRestController eventController;

	@Autowired
	HttpServletRequest req;

	private static final String TEAMNAME = "TEST";
	private static final String ALPHATEAMNAME = "alpha";
	private static final String USERNAME = "TestUser";
	private static final String TIMESTAMP = "@timestamp";
	private static final String APPNAME = "applicationName";

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		eventController.eventService = eventService;
		eventController.userDetailsService = userDetailsService;
	}

	/**
	 * This method set the Security Context with User Object.
	 */
	private void setSecuirtyContext(String teamName, String userName) {
		final List<GrantedAuthority> grantedAuths = new ArrayList<>();
		UserDetails user = new User(userName, teamName, grantedAuths);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
				user.getPassword(), user.getAuthorities());
		SecurityContext ctx = SecurityContextHolder.createEmptyContext();
		ctx.setAuthentication(authentication);
		req.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, ctx);
	}

	/**
	 * This Test method validates listEvents method.
	 * 
	 */
	@Test
	public void testListEvents() {

		EventRecorder eventRecorder = createEvent(TEAMNAME);
		List<EventRecorder> events = new ArrayList<>();
		events.add(eventRecorder);

		when(eventRepository.findAll()).thenReturn(events);

		@SuppressWarnings("unchecked")
		List<EventRecorder> eventLists = (List<EventRecorder>) eventController.listEvents().getBody();

		assertEquals(1, eventLists.size());
		assertEquals(EventStatus.COMPLETED, eventLists.get(0).getEventStatusType());
		assertEquals(TEAMNAME, eventLists.get(0).getTeamName());
	}

	/**
	 * This Test method validates listEvents method when no Events present..
	 * 
	 */
	@Test
	public void testListEventsErrCond() {
		List<EventRecorder> events = new ArrayList<>();
		when(eventRepository.findAll()).thenReturn(events);

		MessageWrapper apiError = (MessageWrapper) eventController.listEvents().getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("No events found !!!", apiError.getStatusMessage());
	}

	/**
	 * This Test method validates listAllEventRecorder method to validate the
	 * Application list for given team.
	 * 
	 */
	@Test
	public void testListAllEvents() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		EventRecorder eventRecorder = createEvent(TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		List<EventRecorder> events = new ArrayList<>();
		events.add(eventRecorder);
		Page<EventRecorder> eventRecorderPage = new PageImpl<>(events);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(eventRepository.findByTeamName(TEAMNAME, new PageRequest(0, 9999))).thenReturn(eventRecorderPage);

		@SuppressWarnings("unchecked")
		List<EventRecorder> eventList = (List<EventRecorder>) eventController.listAllEvents(req).getBody();
		assertEquals(1, eventList.size());
		assertEquals(EventStatus.COMPLETED, eventList.get(0).getEventStatusType());
		assertEquals(TEAMNAME, eventList.get(0).getTeamName());

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);

		events = new ArrayList<>();
		events.add(createEvent(ALPHATEAMNAME));
		events.add(createEvent(TEAMNAME));

		when(eventRepository.findAll()).thenReturn(events);

		@SuppressWarnings("unchecked")
		List<EventRecorder> alphaEventList = (List<EventRecorder>) eventController.listAllEvents(req).getBody();
		assertEquals(2, alphaEventList.size());
		assertEquals(ALPHATEAMNAME, alphaEventList.get(0).getTeamName());
		assertEquals(TEAMNAME, alphaEventList.get(1).getTeamName());
	}

	/**
	 * This Test method validates ListAllEvents method to validate the Event
	 * list for given team when no Events present.
	 * 
	 */
	@Test
	public void testListAllEventsErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		List<EventRecorder> events = new ArrayList<>();
		Page<EventRecorder> eventRecorderPage = new PageImpl<>(events);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(eventRepository.findByTeamName(TEAMNAME, new PageRequest(0, 9999))).thenReturn(eventRecorderPage);

		MessageWrapper apiError = (MessageWrapper) eventController.listAllEvents(req).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("No events found for team " + TEAMNAME, apiError.getStatusMessage());
	}

	/**
	 * This Test method validates listLatestEventsByAppName method to validate
	 * the Application list for given App Name.
	 * 
	 */
	@Test
	public void testListLatestEventsByAppName() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		EventRecorder eventRecorder = createEvent(TEAMNAME);
		EventRecorder eventSecondRecorder = createEvent(TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		List<EventRecorder> events = new ArrayList<>();
		events.add(eventRecorder);
		events.add(eventSecondRecorder);
		Page<EventRecorder> eventRecorderPage = new PageImpl<>(events);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(eventRepository.findByAppName(TEAMNAME, eventRecorder.field(APPNAME), new PageRequest(0, 9999)))
				.thenReturn(eventRecorderPage);

		@SuppressWarnings("unchecked")
		Map<String, EventRecorder> latestEventMap = (Map<String, EventRecorder>) eventController
				.listLatestEventsByAppName(req, eventRecorder.field(APPNAME)).getBody();
		assertEquals(1, latestEventMap.size());
		assertEquals(EventStatus.COMPLETED, latestEventMap.get(eventRecorder.field("name")).getEventStatusType());
	}

	/**
	 * This Test method validates listLatestEventsByAppName method to validate
	 * the Event list for given App Name when no Events present.
	 * 
	 */
	@Test
	public void testListLatestEventsByAppNameErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		EventRecorder eventRecorder = createEvent(TEAMNAME);
		List<EventRecorder> events = new ArrayList<>();
		Page<EventRecorder> eventRecorderPage = new PageImpl<>(events);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(eventRepository.findByAppName(TEAMNAME, eventRecorder.field(APPNAME), new PageRequest(0, 9999)))
				.thenReturn(eventRecorderPage);

		MessageWrapper apiError = (MessageWrapper) eventController
				.listLatestEventsByAppName(req, eventRecorder.field(APPNAME)).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals(" No events found for team " + TEAMNAME + " and for Application Name: "
				+ eventRecorder.field(APPNAME), apiError.getStatusMessage());
	}

	/**
	 * This Test method validates listEvent method to get the EventRecorder
	 * Object for given Event ID.
	 * 
	 */
	@Test
	public void testListEvent() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		EventRecorder eventRecorder = createEvent(TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(eventRepository.findOneForTeam(eventRecorder.getId(), TEAMNAME)).thenReturn(eventRecorder);

		EventRecorder event = (EventRecorder) eventController.listEvent(req, eventRecorder.getId()).getBody();
		assertEquals("eventID", event.getId());
		assertEquals(TEAMNAME, event.getTeamName());
	}

	/**
	 * This Test method validates Error Condition of listEvent method to get the
	 * EventRecorder Object for given Event ID.
	 * 
	 */
	@Test
	public void testListEventErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		EventRecorder eventRecorder = createEvent(TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(eventRepository.findOneForTeam(eventRecorder.getId(), TEAMNAME)).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) eventController.listEvent(req, eventRecorder.getId()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("No event found  for team " + TEAMNAME + " and id " + eventRecorder.getId(),
				apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getstatusupdateforevent method to get the
	 * Latest Event Status Object for given Event ID.
	 * 
	 */
	@Test
	public void testGetstatusupdateforevent() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		EventRecorder eventRecorder = createEvent(TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(eventRepository.findOneForTeam(eventRecorder.getId(), TEAMNAME)).thenReturn(eventRecorder);

		EventStatusUpdate update = (EventStatusUpdate) eventController
				.getstatusupdateforevent(req, eventRecorder.getId()).getBody();

		assertEquals("Latest Event Status for an Event", update.getStatusString());
	}

	/**
	 * This Test method validates Error Condition of getstatusupdateforevent
	 * method to get the EventRecorder Object for given Event ID.
	 * 
	 */
	@Test
	public void testGetstatusupdateforeventErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		EventRecorder eventRecorder = createEvent(TEAMNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(eventRepository.findOneForTeam(eventRecorder.getId(), TEAMNAME)).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) eventController.getstatusupdateforevent(req, eventRecorder.getId())
				.getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("No events found !!! for team " + TEAMNAME, apiError.getStatusMessage());
	}

	/**
	 * This Test method validates CountByTeamName method to validate the Count
	 * of Events for current team.
	 * 
	 */
	@Test
	public void testCountByTeamName() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(eventRepository.countByTeamName(TEAMNAME)).thenReturn((long) 1);
		when(eventRepository.count()).thenReturn((long) 2);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);

		String eventCount = (String) eventController.countByTeamName(req).getBody();
		assertEquals("1", eventCount);

		teamUser.setTeamName(ALPHATEAMNAME);
		setSecuirtyContext(ALPHATEAMNAME, USERNAME);

		eventCount = (String) eventController.countByTeamName(req).getBody();
		assertEquals("2", eventCount);
	}

	/**
	 * This method returns an Application Object.
	 * 
	 * @return
	 */
	private static EventRecorder createEvent(String teamName) {
		EventRecorder eventRecorder = new EventRecorder();
		Map<String, String> map = new HashMap<>();

		eventRecorder.setTeamName(teamName);
		eventRecorder.setId("eventID");
		eventRecorder.setEventStatusType(EventStatus.COMPLETED);
		eventRecorder.setExecStatus("Passed");
		eventRecorder.setEventStatus("Event Completed Successfully.");

		org.joda.time.DateTime dt = new org.joda.time.DateTime();

		map.put("@timestamp", dt.toString());
		map.put(APPNAME, "eventAppName");
		map.put("EventRecorder", "eventEventRecorderName");
		map.put("monkeyType", "CHAOS");
		map.put("name", "Scenario_Event");

		eventRecorder.addFields(map);

		String timeStampString = String.valueOf(new Date());
		EventStatusUpdate eventStatusUpdate = new EventStatusUpdate(timeStampString,
				"Latest Event Status for an Event");
		eventRecorder.setEventStatusUpdate(eventStatusUpdate);

		return eventRecorder;
	}

	/**
	 * This method returns an TeamUser Object.
	 * 
	 * @return
	 */
	private static TeamUser createTeamUserObject(String userName, String teamName) {
		TeamUser teamUser = new TeamUser();
		teamUser.setUserName(userName);
		teamUser.setTeamName(teamName);
		return teamUser;
	}
}
