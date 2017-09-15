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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.ajsc.logging.AjscEelfManager;
import com.att.eelf.configuration.EELFLogger;
import com.att.tta.rs.data.es.repository.TeamUserRepository;
import com.att.tta.rs.model.TeamUser;

/** 
 * Implementation class for {@link TeamUserService}
 * @author ak983d
 *
 */

@Service("teamUserService")
@Transactional
public class TeamUserServiceImpl implements TeamUserService {
	private static EELFLogger logger = AjscEelfManager.getInstance().getLogger(TeamUserServiceImpl.class);

	private TeamUserRepository teamUserRepository;

	@Autowired
	public void setTeamUserRepository(TeamUserRepository teamUserRepository) {
		this.teamUserRepository = teamUserRepository;
	}

	private TeamUser save(TeamUser teamUser) {
		return teamUserRepository.save(teamUser);
	}

	@Override
	public TeamUser findOne(String id) {
		return teamUserRepository.findOne(id);
	}

	@Override
	public Iterable<TeamUser> findAll() {
		return teamUserRepository.findAll();
	}

	@Override
	public Page<TeamUser> findByTeamName(String name, Pageable pageable) {
		return teamUserRepository.findByTeamName(name, pageable);
	}

	@Override
	public TeamUser findByTeamName(String name) {
		return teamUserRepository.findByTeamName(name);
	}

	@Override
	public Page<TeamUser> findByUserName(String name, Pageable pageable) {
		return teamUserRepository.findByUserName(name, pageable);
	}

	@Override
	public TeamUser findByUserName(String name) {
		return teamUserRepository.findByUserNameAndDefaultFlag(name, "Y");
	}

	@Override
	public long count() {
		return teamUserRepository.count();
	}

	@Override
	public void delete(TeamUser teamUser) {
		teamUserRepository.delete(teamUser);
	}

	@Override
	public void deleteAllTeamUsers() {
		teamUserRepository.deleteAll();
	}

	@Override
	public boolean isTeamExist(TeamUser teamUser) {
		return findByTeamName(teamUser.getTeamName()) != null;
	}

	@Override
	public boolean isUserExist(TeamUser teamUser) {
		return findByUserNameAndDefaultFlag(teamUser.getUserName(), "Y") != null;
	}

	@Override
	public TeamUser update(TeamUser teamUser) {

		if (teamUser != null && !"".equals(teamUser.getId().trim())
				&& (teamUserRepository.findOne(teamUser.getId()) != null)) {
			return save(teamUser);
		}
		logger.debug("Not able to update Team User object.");
		return null;
	}

	@Override
	public TeamUser insert(TeamUser teamUser) {
		if (this.isTeamUserExist(teamUser)) {
			logger.debug("TeamUser entry already exists with Team name " + teamUser.getTeamName() + " and User Name: "
					+ teamUser.getUserName());
			return null;
		}
		return save(teamUser);
	}

	@Override
	public boolean isTeamUserExist(TeamUser teamUser) {
		return findByTeamNameAndUserName(teamUser.getTeamName(), teamUser.getUserName()) != null;
	}

	@Override
	public TeamUser findByTeamNameAndUserName(String teamName, String userName) {
		return teamUserRepository.findByTeamNameAndUserName(teamName, userName);
	}

	@Override
	public TeamUser findByUserNameAndDefaultFlag(String userName, String defaultFlag) {
		TeamUser teamUser = teamUserRepository.findByUserNameAndDefaultFlag(userName, defaultFlag);
		if (teamUser == null)
			logger.error(" User info from ES is null !!");
		return teamUser;
	}

	@Override
	public TeamUser getCurrentTeamForUser(HttpServletRequest request) {
		String userName = getUserIdFromAuth(request);		 
		return this.findByUserName(userName);
	}

	/**
	 * This function return the User name from Authentication header.
	 * 
	 * @param request
	 * @return User name
	 */
	private static String getUserIdFromAuth(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		SecurityContextImpl secSession = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
		UserDetails principal = (UserDetails) secSession.getAuthentication().getPrincipal();
		String userString = principal.getUsername();
		return userString.split("\\|")[0];
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		TeamUser user = findByUserName(username);
		List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
		return buildUserForAuthentication(user, authorities);
	}

	/**
	 * Converts TeamUser to org.springframework.security.core.userdetails.User
	 * 
	 * @param user
	 * @param authorities
	 * @return
	 */
	private User buildUserForAuthentication(TeamUser user, List<GrantedAuthority> authorities) {
		return new User(user.getUserName(), user.getTeamName(), true, true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(String role) {
		Set<GrantedAuthority> setAuths = new HashSet<>();
		setAuths.add(new SimpleGrantedAuthority(role));
		return new ArrayList<>(setAuths);
	}
}
