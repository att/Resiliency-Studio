package com.att.tta.rs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.att.tta.rs.Application;
import com.att.tta.rs.model.User;
import com.att.tta.rs.model.UserPrivilege;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SecurityServiceUnitTest {

	@Autowired
	SecurityService service;

	private String validUserID = "esadmin";
	private String validPdKey = "esadmin";
	private String invalidUserID = "invalidID";
	private String invalidPdKey = "invalidKey";

	@Before
	public void setUp() {
		/*
		 * This method is for Setup up.
		 */
	}

	/**
	 * This Test method is to validate the functionality of 'authorizeUser'
	 * method by passing valid 'User ID' as an input parameter.
	 * 
	 */
	@Test
	public void testAuthorizeWithValidUser() {
		List<UserPrivilege> privilege = service.authorizeUser(validUserID);
		UserPrivilege userPrivilege = privilege.get(0);
		assertEquals("EnterpriseService", userPrivilege.getTeamname());
		assertEquals("admin", userPrivilege.getRole());
	}

	/**
	 * This Test method is to validate the functionality of 'authorizeUser'
	 * method by passing invalid 'User ID' as an input parameter.
	 * 
	 */
	@Test
	public void testAuthorizeWithInvalidUser() {
		List<UserPrivilege> privilege = service.authorizeUser(invalidUserID);
		assertNull("The privilege object must null", privilege);
	}

	/**
	 * This Test method is to validate the functionality of 'authenticateUser'
	 * method by passing User Object with valid 'User ID' and 'pwdKey' as an
	 * input parameter.
	 * 
	 */
	@Test
	public void testAuthenticateUserWithValidCredentials()  {
		User user = new User();
		user.setUsrid(validUserID);
		user.setPwdkey(validPdKey);
		assertEquals(true, service.authenticateUser(user));
	}

	/**
	 * This Test method is to validate the functionality of 'authenticateUser'
	 * method by passing User Object with invalid 'User ID' and 'pwdKey' as an
	 * input parameter.
	 * 
	 */
	@Test
	public void testAuthenticateUserWithInvalidCredentials() {
		User user = new User();
		user.setUsrid(invalidUserID);
		user.setPwdkey(invalidPdKey);
		assertEquals(false, service.authenticateUser(user));
	}

	@After
	public void tearDown() throws Exception {
		/*
		 * This method is for clean up part.
		 */
	}
}