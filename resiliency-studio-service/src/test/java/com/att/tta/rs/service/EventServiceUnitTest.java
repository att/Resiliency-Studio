
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.att.tta.rs.data.es.repository.EventRepository;
import com.att.tta.rs.model.EventRecorder;
import com.att.tta.rs.model.EventStatus;
import com.att.tta.rs.model.EventStatusUpdate;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
public class EventServiceUnitTest {

	@InjectMocks
	EventServiceImpl eventService;

	@Mock
	private EventRepository eventRepository;

	private static final String TEAMNAME = "TEST";
	private static final String ALPHATEAMNAME = "alpha";
	private static final String EVENTID = "eventID";
	private static final String APPNAME = "applicationName"; 
	private static final String MONKEYTYPE = "monkeyType";
	
	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * This Test method validates the Successful insertion of EventRecorder
	 * Object into Elastic Search.
	 * 
	 */
	@Test
	public void testCreateApplication() {
		EventRecorder event = createEvent(TEAMNAME);
		when(eventRepository.save(event)).thenReturn(event);
		EventRecorder insertedEvent = eventService.save(event);
		assertEquals(EVENTID, insertedEvent.getId());
		assertEquals(EventStatus.COMPLETED, insertedEvent.getEventStatusType());
	}

	/**
	 * This Test method validates the findOne method for given Event ID.
	 * 
	 */
	@Test
	public void testFindOne() {
		EventRecorder event = createEvent(TEAMNAME);
		when(eventRepository.findOne(event.getId())).thenReturn(event);
		EventRecorder getEvent = eventService.findOne(event.getId());
		assertEquals(EVENTID, getEvent.getId());
		assertEquals(EventStatus.COMPLETED, getEvent.getEventStatusType());
	}

	/**
	 * This Test method validates the Error Condition of findOne method for
	 * given Event ID.
	 * 
	 */
	@Test
	public void testFindOneErrCndtn() {
		EventRecorder event = createEvent(TEAMNAME);
		when(eventRepository.findOne(event.getId())).thenReturn(null);
		EventRecorder getEvent = eventService.findOne(event.getId());
		assertEquals(null, getEvent);
	}

	/**
	 * This Test method validates the findAllByTeamName method for given Team
	 * Name.
	 * 
	 */
	@Test
	public void testFindAllByTeamName() {
		EventRecorder event = createEvent(TEAMNAME);
		EventRecorder alphaEvent = createEvent(ALPHATEAMNAME);

		List<EventRecorder> eventList = new ArrayList<>();
		eventList.add(event);
		Page<EventRecorder> eventPage = new PageImpl<>(eventList);

		List<EventRecorder> alphaEventList = new ArrayList<>();
		alphaEventList.add(alphaEvent);
		alphaEventList.add(event);
		Page<EventRecorder> alphaEventPage = new PageImpl<>(alphaEventList);

		when(eventRepository.findAll()).thenReturn(alphaEventPage);
		when(eventRepository.findByTeamName(TEAMNAME, new PageRequest(0, 9999))).thenReturn(eventPage);

		List<EventRecorder> getEventList = Lists.newArrayList(eventService.findAllByTeamName(event.getTeamName()));
		assertEquals(1, getEventList.size());
		assertEquals(TEAMNAME, getEventList.get(0).getTeamName());

		List<EventRecorder> getAlphaEventList = Lists
				.newArrayList(eventService.findAllByTeamName(alphaEvent.getTeamName()));
		assertEquals(2, getAlphaEventList.size());
		assertEquals(ALPHATEAMNAME, getAlphaEventList.get(0).getTeamName());
	}

	/**
	 * This Test method validates the findByAppName method for given App name &
	 * Team Name.
	 * 
	 */
	@Test
	public void testFindByAppName() {
		EventRecorder event = createEvent(TEAMNAME);

		List<EventRecorder> eventList = new ArrayList<>();
		eventList.add(event);
		Page<EventRecorder> eventPage = new PageImpl<>(eventList);

		when(eventRepository.findByAppName(TEAMNAME, event.field(APPNAME), new PageRequest(0, 9999)))
				.thenReturn(eventPage);

		List<EventRecorder> getEventList = Lists
				.newArrayList(eventService.findByAppName(event.getTeamName(), event.field(APPNAME)));
		assertEquals(1, getEventList.size());
		assertEquals(TEAMNAME, getEventList.get(0).getTeamName());
		assertEquals("eventAppName", getEventList.get(0).field(APPNAME));
	}

	/**
	 * This Test method validates the findOneForTeam method for given Event ID
	 * and Team Name.
	 * 
	 */
	@Test
	public void testFindOneForTeam() {
		EventRecorder event = createEvent(TEAMNAME);
		EventRecorder alphaEvent = createEvent(ALPHATEAMNAME);

		when(eventRepository.findOne(alphaEvent.getId())).thenReturn(alphaEvent);
		when(eventRepository.findOneForTeam(event.getId(), TEAMNAME)).thenReturn(event);

		EventRecorder getAlphaEvent = eventService.findOneForTeam(alphaEvent.getId(), ALPHATEAMNAME);
		EventRecorder getEvent = eventService.findOneForTeam(event.getId(), TEAMNAME);

		assertEquals(ALPHATEAMNAME, getAlphaEvent.getTeamName());
		assertEquals(TEAMNAME, getEvent.getTeamName());
	}

	/**
	 * This Test method validates the findAll method.
	 * 
	 */
	@Test
	public void testFindAll() {
		EventRecorder event = createEvent(TEAMNAME);
		EventRecorder alphaEvent = createEvent(ALPHATEAMNAME);

		List<EventRecorder> alphaEventList = new ArrayList<>();
		alphaEventList.add(alphaEvent);
		alphaEventList.add(event);
		Page<EventRecorder> alphaEventPage = new PageImpl<>(alphaEventList);

		when(eventRepository.findAll()).thenReturn(alphaEventPage);

		List<EventRecorder> getAlphaEventList = Lists.newArrayList(eventService.findAll());
		assertEquals(2, getAlphaEventList.size());
		assertEquals(ALPHATEAMNAME, getAlphaEventList.get(0).getTeamName());
	}

	/**
	 * This Test method validates the findByMonkeyTypeUsingCustomQuery method
	 * for given Monkey Type.
	 * 
	 */
	@Test
	public void testFindByMonkeyTypeUsingCustomQuery() {
		EventRecorder event = createEvent(TEAMNAME);
		List<EventRecorder> eventList = new ArrayList<>();
		eventList.add(event);
		Page<EventRecorder> eventPage = new PageImpl<>(eventList);

		when(eventRepository.findByMonkeyTypeUsingCustomQuery(event.field(MONKEYTYPE), new PageRequest(0, 9999)))
				.thenReturn(eventPage);

		List<EventRecorder> getEventList = Lists.newArrayList(
				eventService.findByMonkeyTypeUsingCustomQuery(event.field(MONKEYTYPE), new PageRequest(0, 9999)));
		assertEquals(1, getEventList.size());
		assertEquals(TEAMNAME, getEventList.get(0).getTeamName());
		assertEquals("CHAOS", getEventList.get(0).field(MONKEYTYPE));
	}

	/**
	 * This Test method validates the testCount method.
	 * 
	 */
	@Test
	public void testCount() {
		when(eventRepository.count()).thenReturn((long) 2);
		long countEvent = eventService.count();
		assertEquals(2, countEvent);
	}

	/**
	 * This Test method validates the delete method.
	 * 
	 */
	@Test
	public void testDelete() {
		EventRecorder event = createEvent(TEAMNAME);
		eventService.delete(event);
		verify(eventRepository, times(1)).delete(event);
	}

	/**
	 * This Test method validates the testCountByTeamName method.
	 * 
	 */
	@Test
	public void testCountByTeamName() {
		when(eventRepository.count()).thenReturn((long) 2);
		when(eventRepository.countByTeamName(TEAMNAME)).thenReturn((long) 1);

		long countAlphaEvent = eventService.countByTeamName(ALPHATEAMNAME);
		long countEvent = eventService.countByTeamName(TEAMNAME);
		assertEquals(1, countEvent);
		assertEquals(2, countAlphaEvent);
	}

	/**
	 * This Test method validates the getLatestUpdate method for given Event ID.
	 * 
	 */
	@Test
	public void testGetLatestUpdate() {
		EventRecorder event = createEvent(TEAMNAME);
	
		when(eventRepository.findOne(event.getId())).thenReturn(event);
		Map<String, String>  latestStatus = eventService.getLatestUpdate(event.getId());

		assertEquals(event.getEventStatusUpdate().getStatusString(),
				latestStatus.get(event.getEventStatusUpdate().getTimeStamp()));
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
		eventRecorder.setId(EVENTID);
		eventRecorder.setEventStatusType(EventStatus.COMPLETED);
		eventRecorder.setExecStatus("Passed");
		eventRecorder.setEventStatus("Event Completed Successfully.");

		map.put("@timestamp", "2017-09-24T10:32:23.154Z");
		map.put(APPNAME, "eventAppName");
		map.put("monkeyStrategy", "eventMonkeyStrategyName");
		map.put(MONKEYTYPE, "CHAOS");
		eventRecorder.addFields(map);

		String timeStampString = String.valueOf(new Date());
		EventStatusUpdate eventStatusUpdate = new EventStatusUpdate(timeStampString, "Latest Event for an Event");
		eventRecorder.setEventStatusUpdate(eventStatusUpdate);

		return eventRecorder;
	}
}
