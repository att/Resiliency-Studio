
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