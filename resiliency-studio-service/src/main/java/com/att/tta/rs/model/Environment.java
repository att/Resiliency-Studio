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
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * This class is the representation of 'Environment' type in the Elastic Search.
 * 
 * @author ak983d
 */

@Document(indexName = "resiliencystudio", type = "environment")
public class Environment implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The application Id . */
	@Id
	private String id;
	
	/** The Environment type/name . */
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String name;

	/** The List of Servers in this Tier */
	private List<Server> serverList;

	/** discover host User Id for this environment */
	private String userId;

	/** discover host password for this environment */
	private String password;

	/** discover host name for this environment */
	private String discoverHostName;

	/** discover host Ip Address for this environment */
	private String discoverHostIpAddress;

	/** discover host port ( ssh 22) for this environment */
	private String discoverHostPort;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Server> getServerList() {
		return serverList;
	}
	
	public void setServerList(List<Server> serverList) {
		this.serverList = serverList;
	}

	public void addServer(Server server) {
		if (this.serverList == null)
			this.serverList = new ArrayList<>();
		this.serverList.add(server);
	}

	public Server getServerWithName(String serverName) {
		if (this.serverList != null) {
			for (Server server : this.serverList) {
					if (server.getInstanceName().trim().equalsIgnoreCase(serverName))
					return server;
			}
		}
		return null;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDiscoverHostName() {
		return discoverHostName;
	}

	public void setDiscoverHostName(String discoverHostName) {
		this.discoverHostName = discoverHostName;
	}

	public String getDiscoverHostIpAddress() {
		return discoverHostIpAddress;
	}

	public void setDiscoverHostIpAddress(String discoverHostIpAddress) {
		this.discoverHostIpAddress = discoverHostIpAddress;
	}

	public String getDiscoverHostPort() {
		return discoverHostPort;
	}

	public void setDiscoverHostPort(String discoverHostPort) {
		this.discoverHostPort = discoverHostPort;
	}
}
