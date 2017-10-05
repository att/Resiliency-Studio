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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationTokenFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);
	private static final String ERRMSG = "Unauthorized user name present in Basic auth Header!!";
	private static final String SESSCONTEXTERR = "Unauthorized user access - session context is not present";

	@Override
	public void init(FilterConfig fc) throws ServletException {
		logger.info("Init AuthenticationTokenFilter");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		try {
			HttpServletResponse resp = (HttpServletResponse) res;
			HttpServletRequest request = (HttpServletRequest) req;

			if ("OPTIONS".equals(request.getMethod())) {
				resp.setStatus(HttpStatus.OK.value());
				return;
			}

			if (request.getRequestURI() != null && !request.getRequestURI().contains("/api/user/")) {
				fc.doFilter(req, res);
				return;
			}
			validateUser();

		} catch (Exception e) {
			logger.error(ERRMSG + e);
			throw new BadCredentialsException(ERRMSG);
		}

		fc.doFilter(req, res);
	}

	private void validateUser() throws ServletException {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
			if (context.getAuthentication().getPrincipal() instanceof String) {
				logger.error(ERRMSG);
				throw new BadCredentialsException(ERRMSG);
			}
			UserDetails principal = (UserDetails) context.getAuthentication().getPrincipal();
			String userString = principal.getUsername();
			String[] arrayStr = userString.split("\\|");
			String name = arrayStr[0];
			/*
			 * Use below Authentication Header while calling this service: Basic
			 * c2VjdXJlTGF5ZXIxMjNyc3w6TE9HSU4=
			 */
			if (!"secureLayer123rs".equals(name)) {
				logger.error(ERRMSG);
				logger.error("Basic auth Header user Name -->" + name);
				context.getAuthentication().setAuthenticated(false);
				throw new ServletException(ERRMSG);
			}
		} else {
			logger.error(SESSCONTEXTERR);
			throw new ServletException(SESSCONTEXTERR);
		}

	}

	@Override
	public void destroy() {
		/*
		 * This block is for cleanup activity.
		 */
	}

}
