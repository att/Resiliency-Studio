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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author ak983d
 *
 *         This class authorizes a User information based on http Authentication
 *         header.
 * 
 */
@Component
public class AuthenticationTokenFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

	private static final String ERROR = "Unauthorized user name present in Basic auth Header!! ";

	@Override
	public void init(FilterConfig fc) throws ServletException {
		logger.info("Init AuthenticationTokenFilter");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		try {
			HttpServletResponse resp = (HttpServletResponse) res;
			HttpServletRequest request = (HttpServletRequest) req;
			setResponseHeader(resp, request);

			if (request.getRequestURI() != null && !request.getRequestURI().contains("/api/execJob/")) {
				fc.doFilter(req, res);
				return;
			} else {
				SecurityContext context = SecurityContextHolder.getContext();
				if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
					validateUser(context);
				} else {
					logger.error("Unauthorized user access - session context not present  ");
					throw new ServletException("Unauthorized user access - session context not present !! ");
				}
			}
		} catch (Exception e) {
			logger.error(ERROR + "\n" + e);
			throw new BadCredentialsException(ERROR);
		}

		fc.doFilter(req, res);
	}

	private void setResponseHeader(HttpServletResponse resp, HttpServletRequest request) {
		resp.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		resp.setHeader("Access-Control-Max-Age", "3600");
		resp.setHeader("Access-Control-Allow-Headers",
				"Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
	}

	private void validateUser(SecurityContext context) throws IOException, ServletException {
		if (context.getAuthentication().getPrincipal() instanceof String) {
			logger.error(ERROR);
			throw new BadCredentialsException(ERROR);
		}
		UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
		String userString = principal.getUsername();
		String[] arrayStr = userString.split("\\|");
		String name = arrayStr[0];
		if (!"agent123c".equals(name)) {
			logger.error(ERROR + name);
			context.getAuthentication().setAuthenticated(false);
			throw new ServletException(ERROR);
		}
	}

	@Override
	public void destroy() {
		/*
		 * This method is to complete the operation needed at the undeploy time.
		 */
	}

}
