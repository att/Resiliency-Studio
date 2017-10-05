
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
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;

import com.att.tta.rs.data.es.repository.TeamUserRepository;
import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.TeamUserServiceImpl;
import com.att.tta.rs.util.MessageWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { com.att.tta.rs.Application.class })
public class TeamUserControllerTest {

	@Mock
	private TeamUserRepository teamUserRepository;

	@InjectMocks
	TeamUserServiceImpl teamUserService;

	@InjectMocks
	TeamUserController teamUserController;

	@Autowired
	HttpServletRequest req;

	private static final String TEAMNAME = "TEST";
	private static final String USERNAME = "TestUser";
	private static final String DEFAULTFLAG = "Y";
	private static final String ROLE = "UserRole";
	private static final String USERID = "UserId";

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		teamUserController.userDetailsService = teamUserService;
	}

	/**
	 * This method set the Security Context with User Object.
	 */
	private void setSecuirtyContext(String teamName, String userName) {
		final List<GrantedAuthority> grantedAuths = new ArrayList<>();
		UserDetails user = new User(userName, teamName, grantedAuths);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
				user.getPassword(), user.getAuthorities());
		SecurityContext ctx = SecurityContextHolder.createEmptyContext();
		ctx.setAuthentication(authentication);
		req.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, ctx);
	}

	/**
	 * This Test method validates getAllUsers method.
	 * 
	 */
	@Test
	public void testGetAllUsers() {

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		List<TeamUser> teamUserList = new ArrayList<>();
		teamUserList.add(teamUser);

		when(teamUserRepository.findAll()).thenReturn(teamUserList);

		@SuppressWarnings("unchecked")
		List<TeamUser> getAllUsers = (List<TeamUser>) teamUserController.getAllUsers().getBody();

		assertEquals(1, getAllUsers.size());
		assertEquals(USERNAME, getAllUsers.get(0).getUserName());
		assertEquals(TEAMNAME, getAllUsers.get(0).getTeamName());
	}

	/**
	 * This Test method validates Error Condition of getAllUsers method when
	 * there is no user entry present.
	 * 
	 */
	@Test
	public void testGetAllUsersErrCndtn() {
		List<TeamUser> teamUserList = new ArrayList<>();

		when(teamUserRepository.findAll()).thenReturn(teamUserList);

		MessageWrapper apiError = (MessageWrapper) teamUserController.getAllUsers().getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals(" No User Found in TeamUser repository", apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getCurrentTeamOfUser method.
	 * 
	 */
	@Test
	public void testGetCurrentTeamOfUser() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);

		TeamUser user = (TeamUser) teamUserController.getCurrentTeamOfUser(req).getBody();
		assertEquals(USERNAME, user.getUserName());
		assertEquals(TEAMNAME, user.getTeamName());
		assertEquals(ROLE, user.getRoles());
	}

	/**
	 * This Test method validates Error Condition of getCurrentTeamOfUser
	 * method.
	 * 
	 */
	@Test
	public void testGetCurrentTeamOfUserErrCndtn() {
		setSecuirtyContext(TEAMNAME, USERNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(null);
		MessageWrapper apiError = (MessageWrapper) teamUserController.getCurrentTeamOfUser(req).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("TeamUser Not found for a User.", apiError.getStatusMessage());
	}

	/**
	 * This Test method validates Error Condition of updateTeamOfUser method.
	 * 
	 */
	@Test
	public void testUpdateTeamOfUserErrCndtn() {
		MessageWrapper apiError = (MessageWrapper) teamUserController.updateTeamOfUser(req, "").getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("TeamUser cannot be modified as new Team Name is null", apiError.getStatusMessage());

		String newTeamName = "newTeamName";
		setSecuirtyContext(TEAMNAME, USERNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(null);

		apiError = (MessageWrapper) teamUserController.updateTeamOfUser(req, newTeamName).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("TeamUser object not found in ES Repository.", apiError.getStatusMessage());
	}

	/**
	 * This Test method validates updateTeamOfUser method.
	 * 
	 */
	@Test
	public void testUpdateTeamOfUser() {
		String newTeamName = "newTeamName";
		setSecuirtyContext(TEAMNAME, USERNAME);
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);
		when(teamUserRepository.findOne(teamUser.getId())).thenReturn(teamUser);
		when(teamUserRepository.save(teamUser)).thenReturn(teamUser);

		TeamUser user = (TeamUser) teamUserController.updateTeamOfUser(req, newTeamName).getBody();
		assertEquals(USERNAME, user.getUserName());
		assertEquals(newTeamName, user.getTeamName());
		assertEquals(ROLE, user.getRoles());
	}

	/**
	 * This method returns an TeamUser Object.
	 * 
	 * @return
	 */
	private static TeamUser createTeamUserObject(String userName, String teamName) {
		TeamUser teamUser = new TeamUser();
		teamUser.setId(USERID);
		teamUser.setUserName(userName);
		teamUser.setTeamName(teamName);
		teamUser.setDefaultFlag(DEFAULTFLAG);
		teamUser.setRoles(ROLE);
		return teamUser;
	}

}
