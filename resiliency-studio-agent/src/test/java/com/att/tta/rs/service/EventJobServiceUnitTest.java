package com.att.tta.rs.service;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.att.tta.rs.model.EventJobDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
@WebAppConfiguration
public class EventJobServiceUnitTest {

	@Autowired
	EventJobService eventJobService;

	/**
	 * This Test method validates the functionality of UNIX Script Execution
	 * 
	 * @throws IOException
	 * 
	 */
	@Test
	public void testUNIXScriptExecution() {
		ConcurrentMap<String, String> eventData = new ConcurrentHashMap<>();
		EventJobDTO dto = createEventJobDTO();
		eventJobService.executeJob(dto, eventData);
	}

	/**
	 * This Test method validates the functionality of Ansible Script Execution
	 * 
	 */
	@Test
	public void testAnsibleScriptExecution() {
		/*
		 * This Test method is for Ansible Script Execution Validation.
		 */
	}

	/**
	 * This method returns an Application Object.
	 * 
	 * @return
	 * @throws IOException
	 */
	private static EventJobDTO createEventJobDTO() {
		EventJobDTO dto = new EventJobDTO();
		dto.setEventId("EventID");
		dto.setTeamName("TEST");

		dto.setMonkeyScriptType("UNIX Script");
		dto.setMonkeyStrategyName("GenericProcessFailure");
		dto.setMonkeyStrategyVersion("1.0");

		dto.setMonkeyScriptContent("echo 'testing'");

		dto.setServerName("LoadBalancer");
		dto.setIpAdd("1111");

		dto.setUserName("user");
		dto.setPrivateKey("privateKey");

		return dto;
	}
}
