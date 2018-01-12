
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.att.tta.rs.model.EventJobDTO;
import com.att.tta.rs.model.EventMonkeyStrategyDTO;
import com.att.tta.rs.service.EventJobServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { com.att.tta.rs.Application.class })
public class EventJobExecutionControllerTest {

	@Mock
	EventJobServiceImpl eventService;

	@InjectMocks
	EventJobExecutionController eventJobController;

	private String startString = "Job Execution has been started at Agent Controller for Monkey Strategy: ";

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		eventJobController.eventJobService = eventService;
	}

	/**
	 * This Test method validates Error Condition of executeJob method.
	 * 
	 */
	@Test
	public void testExecuteJobErrCndtn() {
		EventJobDTO eventDTO = createEventJobDTO();
		eventDTO.getEventMonkeyList().get(0).setMonkeyScriptType("");

		EventJobDTO returnDTO = (EventJobDTO) eventJobController.executeJob(eventDTO).getBody();
		System.out.println("return ");
		
		assertEquals("Monkey Script Type is blank for Monkey Strategy : "+ eventDTO.getEventMonkeyList().get(0).getMonkeyStrategy() +" . So Scenario Execution is terminated.", returnDTO.getEventMonkeyList().get(0).getExecStatus());
		assertEquals("000", returnDTO.getEventMonkeyList().get(0).getReturnCode());

		eventDTO = createEventJobDTO();
		eventDTO.getEventMonkeyList().get(0).setMonkeyScriptContent("");
		returnDTO = (EventJobDTO) eventJobController.executeJob(eventDTO).getBody();
		assertEquals("Monkey Script Content is empty for Monkey Strategy : "+ eventDTO.getEventMonkeyList().get(0).getMonkeyStrategy() +" . So Scenario Execution is terminated.", returnDTO.getEventMonkeyList().get(0).getExecStatus());
		assertEquals("000", returnDTO.getEventMonkeyList().get(0).getReturnCode());
	}

	/**
	 * This Test method validates Error Condition of executeJob method.
	 * 
	 */
	@Test
	public void testExecuteJob() {
		EventJobDTO eventDTO = createEventJobDTO();
		ConcurrentMap<String, String> eventData = new ConcurrentHashMap<>();
		eventData.put(eventDTO.getEventId(), startString);
		eventDTO.getEventMonkeyList().forEach(eventMonkeyDTO -> eventMonkeyDTO.setExecStatus(startString + eventMonkeyDTO.getMonkeyStrategy()));
		EventJobDTO returnDTO = (EventJobDTO) eventJobController.executeJob(eventDTO).getBody();
		verify(eventService, times(1)).executeJob(eventDTO, eventData);

		assertEquals(startString, returnDTO.getExecStatus());
	}

	/**
	 * This method returns an EventJobDTO Object.
	 * 
	 * @return
	 */
	private static EventJobDTO createEventJobDTO() {
		EventJobDTO eventDTO = new EventJobDTO();

		eventDTO.setEventId("eventID");

		eventDTO.setMonkeyScriptType("UNIX Script");
		eventDTO.setMonkeyStrategyName("PingScript");
		eventDTO.setMonkeyStrategyVersion("1");
		eventDTO.setMonkeyStrategyId("msID");
		eventDTO.setMonkeyScriptContent("echo 'ping successfully'");

		eventDTO.setServerName("server");
		eventDTO.setHostName("host");
		eventDTO.setIpAdd("1.1.1.1");

		eventDTO.setUserName("user");
		eventDTO.setPrivateKey("privateKey");
		eventDTO.setPassword("password");

		eventDTO.setExecStatus("status");
		eventDTO.setReturnCode("0");

		List<EventMonkeyStrategyDTO> eventMonkeyStrategyDTOs = new ArrayList<>();
		EventMonkeyStrategyDTO dto = new EventMonkeyStrategyDTO();
		
		dto.setEventId("eventID");
		dto.setExecSequence("1");
		dto.setMonkeyStrategy("testMonkeyStrategy");
		dto.setMonkeyStrategyId("testMonkeyStrategyId");
		dto.setMonkeyScriptContent("echo test");
		dto.setMonkeyScriptType("UNIX Script");
		dto.setMonkeyStrategyVersion("1");
		
		eventMonkeyStrategyDTOs.add(dto);
		eventDTO.setEventMonkeyList(eventMonkeyStrategyDTOs);
		
		return eventDTO;
	}
}
