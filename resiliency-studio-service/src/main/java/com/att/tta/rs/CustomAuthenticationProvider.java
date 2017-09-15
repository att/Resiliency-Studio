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
package com.att.tta.rs;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.TeamUserService;

/**
 * This class validates Authentication Header information against Elastic Search
 * User Repository and create Spring 'Authentication' token accordingly.
 * 
 * @author ak983d
 *
 */
@Configuration
@EnableGlobalAuthentication
@Component
public class CustomAuthenticationProvider
		implements AuthenticationProvider, ApplicationListener<AuthenticationSuccessEvent> {
	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
	private static final String PD = "LOGIN";
	private static final String TEAM = "DEFAULT";
	private static final String ERROR = "Unauthorized user name present in Basic auth Header!! ";

	@Autowired
	TeamUserService userDetailsService;

	/*
	 * This overridden method provides mechanism to validate Authentication
	 * Header information against Elastic Search User Repository. (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication auth = null;
		String userString = authentication.getName();
		logger.debug("UserString : %s", userString);
		String pkey = authentication.getCredentials().toString();
		String[] arrayStr = userString.split("\\|");

		final String userName = arrayStr[0];
		final String teamName = getTeamNameFromAuthString(userString);

		if (teamName.equals(TEAM) && PD.equals(pkey.trim())) {
			TeamUser teamUserObj = userDetailsService.findByUserNameAndDefaultFlag(userName, "Y");

			if (teamUserObj != null && teamUserObj.getTeamName() != null
					&& !"".equals(teamUserObj.getTeamName().trim())) {
				logger.debug("User found in ES with Default team -->" + teamUserObj.getTeamName());
				auth = createAuth(userString, teamUserObj.getTeamName());
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				logger.error("No teamUserObj found in ES for user: " + userName);
				auth = createAuth(userString, "Null");
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} else {
			throw new BadCredentialsException(ERROR);
		}
		return auth;
	}

	/**
	 * This method creates Spring 'Authentication' token for given User Name,
	 * Team name & Role.
	 * 
	 * @param userString
	 * @param teamName
	 * @param role
	 * @return
	 */
	private Authentication createAuth(String userString, String teamName) {
		final List<GrantedAuthority> grantedAuths = new ArrayList<>();
		final UserDetails principal = new User(userString, teamName, grantedAuths);
		return new UsernamePasswordAuthenticationToken(principal, teamName, grantedAuths);
	}

	/**
	 * This method provide the Team Name coming into Authentication Header
	 * 
	 * @param userString
	 * @return
	 */
	private static String getTeamNameFromAuthString(String userString) {
		String[] arrayStr = userString.split("\\|");
		if (arrayStr.length > 1) {
			logger.debug("team in auth : %s", arrayStr[1]);
			return arrayStr[1];
		} else {
			logger.debug("team in auth : %s ", TEAM);
			return TEAM;
		}
	}

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		try {
			SecurityContext ctx = SecurityContextHolder.createEmptyContext();
			SecurityContextHolder.setContext(ctx);
			ctx.setAuthentication(event.getAuthentication());
		} finally {
			// SecurityContextHolder.clearContext(); // TBD cleanup
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
