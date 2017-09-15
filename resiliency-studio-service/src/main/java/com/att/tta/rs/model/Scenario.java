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
/**
 *
 */
package com.att.tta.rs.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This class is the representation of 'Scenario' type in the Elastic Search.
 * 
 * @author ak983d
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@Document(indexName = "resiliencystudio", type = "scenario")
public class Scenario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String name;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String applicationName;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String monkeyStrategy;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String monkeyStrategyId;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String environmentName;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String tierName;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String serverName;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String softwareComponentName;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String failureTenet;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String failureMode;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String causeOfFailure;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String currentControls;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String detectionMechanism;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String recoveryMechanism;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String recommendations;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String mttd;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String mttr;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String failureScript;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String version;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String processName;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String role;

	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String userBehavior;

	/** The systemBehavior . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String systemBehavior;

	/** The Team name . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String teamName;

	/** The user id . */
	@Field(type = FieldType.String, analyzer = "keyword", store = false)
	private String userId; /* only a place holder to store in event object */

	/**
	 * The monkeyType // Failure Tenet in Scenario can be represented as Monkey
	 * type // @Field(type = FieldType.Nested, analyzer = "not_analyzed", store
	 * = true)
	 */
	private MonkeyType monkeyType;

	public Scenario() {
		super();
	}

	public MonkeyType getMonkeyType() {
		return monkeyType;
	}

	public void setMonkeyType(MonkeyType monkeyType) {
		this.monkeyType = monkeyType;
	}

	public String getMonkeyStrategy() {
		return monkeyStrategy;
	}

	public void setMonkeyStrategy(String monkeyStrategy) {
		this.monkeyStrategy = monkeyStrategy;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param aplicationName
	 *            the aplicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the environmentName
	 */
	public String getEnvironmentName() {
		return environmentName;
	}

	/**
	 * @param environmentName
	 *            the environmentName to set
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	/**
	 * @return the tierName
	 */
	public String getTierName() {
		return tierName;
	}

	/**
	 * @param tierName
	 *            the tierName to set
	 */
	public void setTierName(String tierName) {
		this.tierName = tierName;
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName
	 *            the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * @return the softwareComponentName
	 */
	public String getSoftwareComponentName() {
		return softwareComponentName;
	}

	/**
	 * @param softwareComponentName
	 *            the softwareComponentName to set
	 */
	public void setSoftwareComponentName(String softwareComponentName) {
		this.softwareComponentName = softwareComponentName;
	}

	/**
	 * @return the failureMode
	 */
	public String getFailureMode() {
		return failureMode;
	}

	/**
	 * @param failureMode
	 *            the failureMode to set
	 */
	public void setFailureMode(String failureMode) {
		this.failureMode = failureMode;
	}

	/**
	 * @return the causeOfFailure
	 */
	public String getCauseOfFailure() {
		return causeOfFailure;
	}

	/**
	 * @param causeOfFailure
	 *            the causeOfFailure to set
	 */
	public void setCauseOfFailure(String causeOfFailure) {
		this.causeOfFailure = causeOfFailure;
	}

	/**
	 * @return the currentControls
	 */
	public String getCurrentControls() {
		return currentControls;
	}

	/**
	 * @param currentControls
	 *            the currentControls to set
	 */
	public void setCurrentControls(String currentControls) {
		this.currentControls = currentControls;
	}

	public String getMonkeyStrategyId() {
		return monkeyStrategyId;
	}

	public void setMonkeyStrategyId(String monkeyStrategyId) {
		this.monkeyStrategyId = monkeyStrategyId;
	}

	/**
	 * @return the detectionMechanism
	 */
	public String getDetectionMechanism() {
		return detectionMechanism;
	}

	/**
	 * @param detectionMechanism
	 *            the detectionMechanism to set
	 */
	public void setDetectionMechanism(String detectionMechanism) {
		this.detectionMechanism = detectionMechanism;
	}

	/**
	 * @return the recoveryMechanism
	 */
	public String getRecoveryMechanism() {
		return recoveryMechanism;
	}

	/**
	 * @param recoveryMechanism
	 *            the recoveryMechanism to set
	 */
	public void setRecoveryMechanism(String recoveryMechanism) {
		this.recoveryMechanism = recoveryMechanism;
	}

	/**
	 * @return the recommendations
	 */
	public String getRecommendations() {
		return recommendations;
	}

	/**
	 * @param recommendations
	 *            the recommendations to set
	 */
	public void setRecommendations(String recommendations) {
		this.recommendations = recommendations;
	}

	/**
	 * @return the mttd
	 */
	public String getMttd() {
		return mttd;
	}

	/**
	 * @param mttd
	 *            the mttd to set
	 */
	public void setMttd(String mttd) {
		this.mttd = mttd;
	}

	/**
	 * @return the mttr
	 */
	public String getMttr() {
		return mttr;
	}

	/**
	 * @param mttr
	 *            the mttr to set
	 */
	public void setMttr(String mttr) {
		this.mttr = mttr;
	}

	/**
	 * @return the failureScript
	 */
	public String getFailureScript() {
		return failureScript;
	}

	/**
	 * @param failureScript
	 *            the failureScript to set
	 */
	public void setFailureScript(String failureScript) {
		this.failureScript = failureScript;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * @param processName
	 *            the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserBehavior() {
		return userBehavior;
	}

	public void setUserBehavior(String userBehavior) {
		this.userBehavior = userBehavior;
	}

	public String getSystemBehavior() {
		return systemBehavior;
	}

	public void setSystemBehavior(String systemBehavior) {
		this.systemBehavior = systemBehavior;
	}

	public String getFailureTenet() {
		return failureTenet;
	}

	public void setFailureTenet(String failureTenet) {
		this.failureTenet = failureTenet;
	}
}
