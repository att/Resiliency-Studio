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

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.att.tta.rs.model.User;
import com.att.tta.rs.model.UserAdapter;
import com.att.tta.rs.model.UserPrivilege;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;

/**
 * Implementation class for {@link SecurityService}
 * @author ak983d
 *
 */
@Service("securityService")
public class SecurityServiceImpl implements SecurityService {
	public static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);
	private static UserAdapter users;

	static {
		users = populateDummyUsers();
	}

	private static UserAdapter populateDummyUsers() {
		UserAdapter userAdapter = new UserAdapter();
		try {
			String authUserString = Resources.toString(Resources.getResource("AuthUser/AuthUser.json"), Charsets.UTF_8);
			Gson gson = new Gson();
			userAdapter = gson.fromJson(authUserString, UserAdapter.class);
		} catch (IOException e) {
			logger.error("Error while Authenticating a Uer against Json data: " + e);
		}
		return userAdapter;
	}

	@Override
	public boolean authenticateUser(User user) {
		boolean userEntryFound = false;
		for (User user2: users.getUsers()){
			if (user2.getUsrid().equals(user.getUsrid()) && user2.getPwdkey().equals(user.getPwdkey())){
				userEntryFound = true;
				break;
			}
		}
		return userEntryFound;
	}

	@Override
	public List<UserPrivilege> authorizeUser(String userID) {
		List<UserPrivilege> privilege = null;
		for (User user2: users.getUsers()){
			if (user2.getUsrid().equals(userID)){
				privilege = user2.getPrivilege();
				break;
			}
		}
		return privilege;
	}
}
