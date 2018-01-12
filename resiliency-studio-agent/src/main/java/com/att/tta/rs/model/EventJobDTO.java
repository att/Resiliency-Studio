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
	
/**
 * This class is a Event Job DTO. 
 * @author ak983d
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class EventJobDTO {

	private String eventId;
	private String teamName;

	private String monkeyScriptType;
	private String monkeyStrategyName;
	private String monkeyStrategyVersion;
	private String monkeyStrategyId;
	private String monkeyScriptContent;

	private String serverName;
	private String hostName;
	private String ipAdd;

	private String discoverHostName;
	private String discoverHostIpAddress;
	private String discoverHostPort;
	private String discoverUserId;
	private String discoverPassword;

	private String userName;
	private String privateKey;
	private String password;

	private String processName;
	private String failureMode;
	private String scenarioServerName;

	private String connectionType;
	private String configFile;

	private String execStatus;
	private String returnCode;
	
	private DateTime startTS;
	private DateTime endTS;
	
	private String execDuration;
	private String filePath;
	
	public String getExecDuration() {
		return execDuration;
	}

	public void setExecDuration(String execDuration) {
		this.execDuration = execDuration;
	}

	public DateTime getStartTS() {
		return startTS;
	}

	public void setStartTS(DateTime startTS) {
		this.startTS = startTS;
	}

	public DateTime getEndTS() {
		return endTS;
	}

	public void setEndTS(DateTime endTS) {
		this.endTS = endTS;
	}

	/** The List of Monkey Strategies tagged to this Scenario.*/
	private List<EventMonkeyStrategyDTO> eventMonkeyList;

	public String getExecStatus() {
		return execStatus;
	}

	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getScenarioServerName() {
		return scenarioServerName;
	}

	public void setScenarioServerName(String scenarioServerName) {
		this.scenarioServerName = scenarioServerName;
	}

	public String getDiscoverHostPort() {
		return discoverHostPort;
	}

	public void setDiscoverHostPort(String discoverHostPort) {
		this.discoverHostPort = discoverHostPort;
	}

	public String getDiscoverUserId() {
		return discoverUserId;
	}

	public void setDiscoverUserId(String discoverUserId) {
		this.discoverUserId = discoverUserId;
	}

	public String getDiscoverPassword() {
		return discoverPassword;
	}

	public void setDiscoverPassword(String discoverPassword) {
		this.discoverPassword = discoverPassword;
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

	public String getMonkeyScriptType() {
		return monkeyScriptType;
	}

	public void setMonkeyScriptType(String monkeyScriptType) {
		this.monkeyScriptType = monkeyScriptType;
	}

	public String getMonkeyStrategyName() {
		return monkeyStrategyName;
	}

	public void setMonkeyStrategyName(String monkeyStrategyName) {
		this.monkeyStrategyName = monkeyStrategyName;
	}

	public String getMonkeyStrategyVersion() {
		return monkeyStrategyVersion;
	}

	public void setMonkeyStrategyVersion(String monkeyStrategyVersion) {
		this.monkeyStrategyVersion = monkeyStrategyVersion;
	}

	public String getMonkeyStrategyId() {
		return monkeyStrategyId;
	}

	public void setMonkeyStrategyId(String monkeyStrategyId) {
		this.monkeyStrategyId = monkeyStrategyId;
	}

	public String getMonkeyScriptContent() {
		return monkeyScriptContent;
	}

	public void setMonkeyScriptContent(String monkeyScriptContent) {
		this.monkeyScriptContent = monkeyScriptContent;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getFailureMode() {
		return failureMode;
	}

	public void setFailureMode(String failureMode) {
		this.failureMode = failureMode;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIpAdd() {
		return ipAdd;
	}

	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	

	public List<EventMonkeyStrategyDTO> getEventMonkeyList() {
		return eventMonkeyList;
	}

	public void setEventMonkeyList(List<EventMonkeyStrategyDTO> eventMonkeyList) {
		this.eventMonkeyList = eventMonkeyList;
	}

	public void addEventMonkey(EventMonkeyStrategyDTO strategy) {
		if (this.eventMonkeyList == null)
			this.eventMonkeyList = new ArrayList<>();
		this.eventMonkeyList.add(strategy);
	}
	

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public EventMonkeyStrategyDTO getStrategyWithName(String strategyName) {
		if (this.eventMonkeyList != null) {
			for (EventMonkeyStrategyDTO eventMonkeyStrategy : this.eventMonkeyList) {
				if (eventMonkeyStrategy.getMonkeyStrategy().trim().equalsIgnoreCase(strategyName))
					return eventMonkeyStrategy;
			}
		}
		return null;
	}

	public EventMonkeyStrategyDTO getStrategyWithId(String strategyId) {
		if (this.eventMonkeyList != null) {
			for (EventMonkeyStrategyDTO eventMonkeyStrategy : this.eventMonkeyList) {
				if (eventMonkeyStrategy.getMonkeyStrategyId().trim().equalsIgnoreCase(strategyId))
					return eventMonkeyStrategy;
			}
		}
		return null;
	}
}
