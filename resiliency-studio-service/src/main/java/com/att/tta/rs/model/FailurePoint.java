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

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * This class is the representation of 'FailurePoint' type in the Elastic Search.
 * 
 * @author ak983d
 */

@Document(indexName = "resiliencystudio", type = "failurepoint")
public class FailurePoint implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The FailurePoint Id . */
	@Id
	private String id;

	/** The FailurePoint name . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String name;	

	/** The application category. */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String category;

	/** role/type */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String role;

	/** The component */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String component;

	/** The Process Name */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String processName;

	private String failureTenet;

	/** Failure Mode */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String failureMode;

	/** The cause of failure */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String causeOfFailure;

	/** The cause of Current Controls */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String currentControls;

	/** Detection */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String detection;

	/** Recovery Action */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String recoveryAction;

	/** recommendation */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String recommendation;

	/** mttd */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String mttd;

	/** mttr */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String mttr;

	/** User Behavior */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String userBehavior;

	/** The systemBehavior . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String systemBehavior;

	/** Monkey Strategy */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String monkeyStrategy;

	/** osType */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String osType;

	/** Flavor */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String flavor;

	/** Failure Category */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String failureCategory;

	/** Failure Sub Category*/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String failureSubCategory;
	
	private MonkeyType monkeyType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getFailureTenet() {
		return failureTenet;
	}

	public void setFailureTenet(String failureTenet) {
		this.failureTenet = failureTenet;
	}

	public String getFailureMode() {
		return failureMode;
	}

	public void setFailureMode(String failureMode) {
		this.failureMode = failureMode;
	}

	public String getCauseOfFailure() {
		return causeOfFailure;
	}

	public void setCauseOfFailure(String causeOfFailure) {
		this.causeOfFailure = causeOfFailure;
	}

	public String getCurrentControls() {
		return currentControls;
	}

	public void setCurrentControls(String currentControls) {
		this.currentControls = currentControls;
	}

	public String getDetection() {
		return detection;
	}

	public void setDetection(String detection) {
		this.detection = detection;
	}

	public String getRecoveryAction() {
		return recoveryAction;
	}

	public void setRecoveryAction(String recoveryAction) {
		this.recoveryAction = recoveryAction;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public String getMttd() {
		return mttd;
	}

	public void setMttd(String mttd) {
		this.mttd = mttd;
	}

	public String getMttr() {
		return mttr;
	}

	public MonkeyType getMonkeyType() {
		return monkeyType;
	}

	public void setMonkeyType(MonkeyType monkeyType) {
		this.monkeyType = monkeyType;
	}

	public void setMttr(String mttr) {
		this.mttr = mttr;
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

	public String getMonkeyStrategy() {
		return monkeyStrategy;
	}

	public void setMonkeyStrategy(String monkeyStrategy) {
		this.monkeyStrategy = monkeyStrategy;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getFlavor() {
		return flavor;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	public String getFailureCategory() {
		return failureCategory;
	}

	public void setFailureCategory(String failureCategory) {
		this.failureCategory = failureCategory;
	}

	public String getFailureSubCategory() {
		return failureSubCategory;
	}

	public void setFailureSubCategory(String failureSubCategory) {
		this.failureSubCategory = failureSubCategory;
	}
}
