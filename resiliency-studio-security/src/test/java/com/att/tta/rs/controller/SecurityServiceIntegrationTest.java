
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.att.tta.rs.Application;
import com.att.tta.rs.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class })
@WebAppConfiguration
public class SecurityServiceIntegrationTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	private String validUserID = "esadmin";
	private String validPdKey = "esadmin";
	private String invalidUserID = "invalidID";
	private String invalidPdKey = "invalidKey";
	private String statusKey = "$.statusMessage";

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setUp()  {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	/**
	 * This Test method is to validate the REST API '/api/user/' by passing
	 * valid 'User ID' as an input parameter.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUserAPIWithValidUser() throws Exception  {
		mockMvc.perform(get("/api/user/" + validUserID)).andExpect(status().isOk())
			.andExpect(content().contentType(contentType)).andExpect(jsonPath("$[0].teamname", is("EnterpriseService")))
			.andExpect(jsonPath("$[0].role", is("admin")));

	}

	/**
	 * This Test method is to validate the REST API '/api/user/' by passing
	 * invalid 'User ID' as an input parameter.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUserAPIWithInvalidUser() throws Exception {
		mockMvc.perform(get("/api/user/" + invalidUserID)).andExpect(status().isUnauthorized())
				.andExpect(content().contentType(contentType)).andExpect(
						jsonPath(statusKey, is("No Roles have been setup for provided user ID: " + invalidUserID)));
	}

	/**
	 * This Test method is validate the REST API '/api/session/' by passing User
	 * JSON Object with valid 'User ID' and 'pwdKey' as an input parameter.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSessionAPIWithValidCredentials() throws Exception {
		User user = new User();
		user.setUsrid(validUserID);
		user.setPwdkey(validPdKey);

		mockMvc.perform(post("/api/session/").content(this.json(user)).contentType(contentType))
				.andExpect(status().isOk()).andExpect(jsonPath(statusKey, is("Login successful !!.")));
	}

	/**
	 * This Test method is validate the REST API '/api/session/' by passing User
	 * JSON Object with invalid 'User ID' and 'pwdKey' as an input parameter.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSessionAPIWithInvalidCredentials() throws Exception {
		User user = new User();
		user.setUsrid(invalidUserID);
		user.setPwdkey(invalidPdKey);

		mockMvc.perform(post("/api/session/").content(this.json(user)).contentType(contentType))
				.andExpect(status().isUnauthorized()).andExpect(jsonPath(statusKey,
						is("Login failed with entered credential information. Pls try again !!.")));
	}

	@SuppressWarnings("unchecked")
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

	@After
	public void tearDown() throws Exception {
		/*
		 * This method is for clean up part.
		 */
	}
}