package com.att.tta.rs.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.Test;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
@WebAppConfiguration
public class ScenarioServiceImplUnitTest {

	/**
	 * This Test method validates the Successful insertion and deletion of
	 * Scenario Object into/from Elastic Search.
	 * 
	 */
	@Test
	public void testCreateAndDeleteScenario() {
		/*
		 * This Test Method validate the Create and Delete Scenario Object
		 * Functionality.
		 */
	}
}
