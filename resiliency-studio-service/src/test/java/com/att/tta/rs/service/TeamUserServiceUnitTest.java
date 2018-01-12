
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.att.tta.rs.data.es.repository.TeamUserRepository;
import com.att.tta.rs.model.TeamUser;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
public class TeamUserServiceUnitTest {

	@InjectMocks
	TeamUserServiceImpl teamUserService;

	@Mock
	private TeamUserRepository teamUserRepository;

	private static final String TEAMNAME = "TEST";
	private static final String USERNAME = "TestUser";
	private static final String DEFAULTFLAG = "Y";
	private static final String ROLE = "UserRole";
	private static final String USERID = "UserId";

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * This Test method validates the Successful insertion of TeamUser Object
	 * from Elastic Search.
	 * 
	 */
	@Test
	public void testCreateTeamUser() {

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByTeamNameAndUserName(TEAMNAME, USERNAME)).thenReturn(null);
		when(teamUserRepository.save(teamUser)).thenReturn(teamUser);

		TeamUser teamUser1 = teamUserService.insert(teamUser);
		assertEquals(USERNAME, teamUser1.getUserName());
		assertEquals(TEAMNAME, teamUser1.getTeamName());
		assertEquals(ROLE, teamUser1.getRoles());
	}

	/**
	 * This Test method validates the error condition on insertion of TeamUser
	 * Object with same existing TeamUser name.
	 * 
	 */
	@Test
	public void testDuplicateCreateTeamUser() {

		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findByTeamNameAndUserName(TEAMNAME, USERNAME)).thenReturn(teamUser);

		TeamUser teamUser1 = teamUserService.insert(teamUser);
		assertNull("TeamUser entry already exists with Team name " + teamUser.getTeamName() + " and User Name: "
				+ teamUser.getUserName(), teamUser1);
	}

	/**
	 * This Test method validates the Successful Deletion of TeamUser Object
	 * from Elastic Search.
	 * 
	 */
	@Test
	public void testDeleteTeamUser() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		teamUserService.delete(teamUser);
		verify(teamUserRepository, times(1)).delete(teamUser);
	}

	/**
	 * This Test method validates the Successful Update of TeamUser Object into
	 * Elastic Search.
	 * 
	 */
	@Test
	public void testUpdateTeamUser() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);

		when(teamUserRepository.findOne(teamUser.getId())).thenReturn(teamUser);
		when(teamUserRepository.save(teamUser)).thenReturn(teamUser);

		TeamUser updatedTeamUser = teamUserService.update(teamUser);
		assertEquals(USERNAME, updatedTeamUser.getUserName());
		assertEquals(TEAMNAME, updatedTeamUser.getTeamName());
		assertEquals(ROLE, updatedTeamUser.getRoles());
	}

	/**
	 * This Test method validates the Error Condition while updating of
	 * non-exist TeamUser Object into Elastic Search.
	 * 
	 */
	@Test
	public void testUpdateNonExistTeamUser() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		when(teamUserRepository.findOne(teamUser.getId())).thenReturn(null);

		TeamUser updatedTeamUser = teamUserService.update(teamUser);
		assertNull("TeamUser doesn't exist so not able to update a TeamUser Object.", updatedTeamUser);
	}

	/**
	 * This Test method validates the findByUserNameAndDefaultFlag method
	 * 
	 */
	@Test
	public void testFindByUserNameAndDefaultFlag() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, DEFAULTFLAG)).thenReturn(teamUser);

		TeamUser updatedTeamUser = teamUserService.findByUserNameAndDefaultFlag(USERNAME, DEFAULTFLAG);
		assertEquals(USERNAME, updatedTeamUser.getUserName());
		assertEquals(TEAMNAME, updatedTeamUser.getTeamName());
		assertEquals(ROLE, updatedTeamUser.getRoles());
	}

	/**
	 * This Test method validates the finaAll method
	 * 
	 */
	@Test
	public void testfinaAll() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		List<TeamUser> teamUserList = new ArrayList<>();
		teamUserList.add(teamUser);
		Page<TeamUser> teamUserPage = new PageImpl<>(teamUserList);

		when(teamUserRepository.findAll()).thenReturn(teamUserPage);

		List<TeamUser> teamUsers = Lists.newArrayList(teamUserService.findAll());
		assertEquals(1, teamUsers.size());
		assertEquals(TEAMNAME, teamUsers.get(0).getTeamName());
		assertEquals(ROLE, teamUsers.get(0).getRoles());
	}

	/**
	 * This Test method validates the findByTeamName method
	 * 
	 */
	@Test
	public void testFindByTeamName() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		List<TeamUser> teamUserList = new ArrayList<>();
		teamUserList.add(teamUser);
		Page<TeamUser> teamUserPage = new PageImpl<>(teamUserList);

		when(teamUserRepository.findByTeamName(TEAMNAME, new PageRequest(0, 9999))).thenReturn(teamUserPage);

		List<TeamUser> teamUsers = Lists
				.newArrayList(teamUserService.findByTeamName(TEAMNAME, new PageRequest(0, 9999)));
		assertEquals(1, teamUsers.size());
		assertEquals(TEAMNAME, teamUsers.get(0).getTeamName());
		assertEquals(ROLE, teamUsers.get(0).getRoles());
	}

	/**
	 * This Test method validates the findByTeamName method
	 * 
	 */
	@Test
	public void testFindTeamByTeamName() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		when(teamUserRepository.findByTeamName(TEAMNAME)).thenReturn(teamUser);

		TeamUser teamUser1 = teamUserService.findByTeamName(TEAMNAME);
		assertEquals(TEAMNAME, teamUser1.getTeamName());
		assertEquals(ROLE, teamUser1.getRoles());
	}

	/**
	 * This Test method validates the findByUserName method
	 * 
	 */
	@Test
	public void testFindByUserName() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		List<TeamUser> teamUserList = new ArrayList<>();
		teamUserList.add(teamUser);
		Page<TeamUser> teamUserPage = new PageImpl<>(teamUserList);

		when(teamUserRepository.findByUserName(USERNAME, new PageRequest(0, 9999))).thenReturn(teamUserPage);

		List<TeamUser> teamUsers = Lists
				.newArrayList(teamUserService.findByUserName(USERNAME, new PageRequest(0, 9999)));
		assertEquals(1, teamUsers.size());
		assertEquals(TEAMNAME, teamUsers.get(0).getTeamName());
		assertEquals(ROLE, teamUsers.get(0).getRoles());
	}

	/**
	 * This Test method validates the findByUserName method
	 * 
	 */
	@Test
	public void testFindTeamByUserName() {
		TeamUser teamUser = createTeamUserObject(USERNAME, TEAMNAME);
		when(teamUserRepository.findByUserNameAndDefaultFlag(USERNAME, "Y")).thenReturn(teamUser);

		TeamUser teamUser1 = teamUserService.findByUserName(USERNAME);
		assertEquals(TEAMNAME, teamUser1.getTeamName());
		assertEquals(ROLE, teamUser1.getRoles());
	}

	/**
	 * This Test method validates the count method
	 * 
	 */
	@Test
	public void testCount() {
		when(teamUserRepository.count()).thenReturn((long) 2);
		long userCount = teamUserService.count();
		assertEquals((long) 2, userCount);
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
