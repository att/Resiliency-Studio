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
import java.util.List;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * This class is the representation of 'Server' Data Model in the Elastic
 * Search.
 * 
 * @author ak983d
 */

public class Server implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The server host name . */
	private String hostName;

	/** The server instance name . */
	private String instanceName;

	/** The Role (AppServer/WEb Server) per Shrinath. */
	private String role;

	/** The Role List (AppServer/WEb Server) per Shrinath. */
	private List<String> roles;

	/** User Name **/
	private String userName;

	/** password **/
	private String password;

	/** RSA indicator **/
	private boolean rsaLogin;

	/** key **/
	private String key;

	/** private key **/
	private String privatekey;

	/** The server memory. */
	private String memory;

	/** The server memory. */
	private String cpu;

	/** The server storage. */
	private String storage;

	/** The server Operating System. */
	private String operatingSystem;

	/** The server IP address */
	private String ipAddress;

	/** The server Operating System Type. */
	private String operatingSystemType;

	/** The server Tier. */
	private String tier;

	/** software components **/
	@Field(type = FieldType.Nested)
	private List<SoftwareComponent> swComps;

	/** Monitor Details **/
	@Field(type = FieldType.Nested)
	private List<MonitorElement> monitor;

	/** Log Details **/
	@Field(type = FieldType.Nested)
	private List<LogElement> log;

	@Field(type = FieldType.Nested)
	private List<DiscoveryApiElement> discoveryApiList;
	
	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the instanceName
	 */
	public String getInstanceName() {
		return instanceName;
	}

	/**
	 * @param instanceName
	 *            the instanceName to set
	 */
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	/**
	 * @return the memory
	 */
	public String getMemory() {
		return memory;
	}

	/**
	 * @param memory
	 *            the memory to set
	 */
	public void setMemory(String memory) {
		this.memory = memory;
	}

	/**
	 * @return the storage
	 */
	public String getStorage() {
		return storage;
	}

	/**
	 * @param storage
	 *            the storage to set
	 */
	public void setStorage(String storage) {
		this.storage = storage;
	}

	/**
	 * @return the operatingSystem
	 */
	public String getOperatingSystem() {
		return operatingSystem;
	}

	/**
	 * @param operatingSystem
	 *            the operatingSystem to set
	 */
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the operatingSystemType
	 */
	public String getOperatingSystemType() {
		return operatingSystemType;
	}

	/**
	 * @param operatingSystemType
	 *            the operatingSystemType to set
	 */
	public void setOperatingSystemType(String operatingSystemType) {
		this.operatingSystemType = operatingSystemType;
	}

	/**
	 * @return the tier
	 */
	public String getTier() {
		return tier;
	}

	/**
	 * @param tier
	 *            the tier to set
	 */
	public void setTier(String tier) {
		this.tier = tier;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRsaLogin() {
		return rsaLogin;
	}

	public void setRsaLogin(boolean rsaLogin) {
		this.rsaLogin = rsaLogin;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPrivatekey() {
		return privatekey;
	}

	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}

	/**
	 * @return the swComps
	 */
	public List<SoftwareComponent> getSwComps() {
		return swComps;
	}

	/**
	 * @param swComps
	 *            the swComps to set
	 */
	public void setSwComps(List<SoftwareComponent> swComps) {
		this.swComps = swComps;
	}

	/**
	 * @return the monitor
	 */
	public List<MonitorElement> getMonitor() {
		return monitor;
	}

	/**
	 * @param monitor
	 *            the monitor to set
	 */
	public void setMonitor(List<MonitorElement> monitor) {
		this.monitor = monitor;
	}

	/**
	 * @return the log
	 */
	public List<LogElement> getLog() {
		return log;
	}

	/**
	 * @param log
	 *            the log to set
	 */
	public void setLog(List<LogElement> log) {
		this.log = log;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<DiscoveryApiElement> getDiscoveryApiList() {
		return discoveryApiList;
	}

	public void setDiscoveryApiList(List<DiscoveryApiElement> discoveryApiList) {
		this.discoveryApiList = discoveryApiList;
	}
	
	
	/**
	 * @return the discoveryApi
	 */
	public List<DiscoveryApiElement> getDiscoveryApi() {
		return discoveryApiList;
	}

	/**
	 * @param discoveryApi
	 *            the discoveryApi to set
	 */
	public void setDiscoveryApi(List<DiscoveryApiElement> discoveryApiList) {
		this.discoveryApiList = discoveryApiList;
	}
	
	public SoftwareComponent getSoftwareComponentWithProcessName(String processName) {
		if (this.swComps != null && processName != null) {
			for (SoftwareComponent softwareComponent : this.swComps) {
				if (softwareComponent.getProcessName().trim().equalsIgnoreCase(processName.toLowerCase().trim()))
					return softwareComponent;
			}
		}
		return null;
	}
}
