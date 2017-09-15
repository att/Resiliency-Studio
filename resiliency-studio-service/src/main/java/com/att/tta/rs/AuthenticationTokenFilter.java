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

import java.io.IOException;

import javax.annotation.Priority;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.att.tta.rs.model.TeamUser;
import com.att.tta.rs.service.TeamUserService;

/**
 * @author ak983d
 *
 *         This class authorizes a User information based on http Authentication
 *         header.
 * 
 */

@Provider
@Priority(Priorities.AUTHENTICATION)
@Component
public class AuthenticationTokenFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);
	
	private static final String ERROR = "Unauthorized user name present in Basic auth Header!! ";
	private static final String SESSCONTEXTERR = "Unauthorized user access session context not present !! ";
	private static final String SESSCONTEXTFALSEERR = "Unauthorized user access. Context.getAuthentication().isAuthenticated() is false !! ";

	@Autowired
	TeamUserService userDetailsService;

	@Override
	public void init(FilterConfig fc) throws ServletException {
		logger.debug("Init AuthenticationTokenFilter");
	}

	/*
	 * This is the Filter chain method to perfrom Authorization logic.
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.getContext();
		HttpServletResponse resp = (HttpServletResponse) res;
		HttpServletRequest request = (HttpServletRequest) req;
		setResponseHeader(request, resp);
		if ("OPTIONS".equals(request.getMethod())) {
			resp.setStatus(HttpStatus.OK.value());
			return;
		}

		if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
			String userName = getUserIdFromAuth(request);
			TeamUser teamUser = userDetailsService.findByUserName(userName);
			if (teamUser == null) {
				logger.error(ERROR);
				context.getAuthentication().setAuthenticated(false);
				resp.setStatus(HttpStatus.UNAUTHORIZED.value());
				return;
			}
		} else if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
			logger.debug(SESSCONTEXTFALSEERR);
		} else {
			logger.debug(SESSCONTEXTERR);
			resp.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}
		fc.doFilter(req, res);
	}

	/**
	 * This function set the response header.
	 * 
	 * @param request
	 * @param resp
	 */
	private void setResponseHeader(HttpServletRequest request, HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		resp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		resp.setHeader("Access-Control-Max-Age", "3600");
		resp.setHeader("Access-Control-Allow-Headers",
				"Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
		if ("OPTIONS".equals(request.getMethod())) {
			resp.setStatus(HttpStatus.OK.value());
		}
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
	public void destroy() {
		/*
		 * This block is for cleanup activity.
		 */
	}
}
