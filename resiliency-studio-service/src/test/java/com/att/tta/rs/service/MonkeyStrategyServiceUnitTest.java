package com.att.tta.rs.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
@WebAppConfiguration
public class MonkeyStrategyServiceUnitTest {
	/**
	 * This Test method validates the Successful insertion and deletion of
	 * Monkey Strategy Object into/from Elastic Search.
	 * 
	 */
	@Test
	public void testCreateAndDeleteMonkeyStrategy() {
		/*
		 * This Test Method validate the Create and Delete Monkey Strategy
		 * Object Functionality.
		 */
	}
}
