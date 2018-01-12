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
package com.att.tta.rs.model;

import java.io.Serializable;

/**
 * This class is the model class of Environment Discover Object
 *
 */

public abstract class EnvironmentDiscover implements Serializable {

	protected static final long serialVersionUID = 1L;

	/** The application host name . */
	protected String hostName;

	/** The application host IP . */
	protected String ipAddress;

	/** The application host user . */
	protected String user;

	/** The application host port . */
	protected String port;

	/** The application category */
	protected String category;

	/** The application environment */
	protected String environment;

	/** The application host password . */
	protected String password;
	
	protected String privateKey;

	/** Keystone URL or Fuel web URL to get access token */
	protected String tokenURL;

	/** Fuel web discover URL to get node information */
	protected String fuelWebDiscoverURL;

	/** JSOn payload tokenRequestJson */
	protected String tokenRequestJson;

	/** The application . */
	protected Application application;

	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the Environment type/name to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getFuelWebDiscoverURL() {
		return fuelWebDiscoverURL;
	}

	public void setFuelWebDiscoverURL(String fuelWebDiscoverURL) {
		this.fuelWebDiscoverURL = fuelWebDiscoverURL;
	}

	public String getTokenURL() {
		return tokenURL;
	}

	public void setTokenURL(String tokenURL) {
		this.tokenURL = tokenURL;
	}

	public String getTokenRequestJson() {
		return tokenRequestJson;
	}

	public void setTokenRequestJson(String tokenRequestJson) {
		this.tokenRequestJson = tokenRequestJson;
	}

	/**
	 * @return the privateKey
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	

}
