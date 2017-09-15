package com.att.tta.rs.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
@WebAppConfiguration
public class ApplicationServiceUnitTest {

	@Test
	public void testCreateAndDeleteApplication() {
		/*
		 * This Test Method validate the Create and Delete Application
		 * Functionality.
		 */
	}

}
