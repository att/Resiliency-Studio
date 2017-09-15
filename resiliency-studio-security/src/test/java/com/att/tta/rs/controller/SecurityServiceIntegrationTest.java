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