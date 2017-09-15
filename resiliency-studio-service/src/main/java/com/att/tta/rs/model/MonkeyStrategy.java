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
 * This class is the representation of 'Monkey Strategy' type in the Elastic Search
 * 
 */
@Document(indexName = "resiliencystudio", type = "monkeystrategy")
public class MonkeyStrategy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** The monkeyStrategyId */
	@Id
	private String id;

	/**
	 * MonkeyStrategy Name. We can reference the monkey index and replicate the
	 * information for the monkey strategy as a child document inside scenario.
	 * Or have only monkeyStrategyId as reference. Since its NoSQL and if
	 * joins are going to be a challenge, we can duplicate data inside scenario document itself.
	 */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String monkeyStrategyName; 
	
	/**
	 * Failure Tenet in Scenario can be represented as Monkey type
	 */
	// @Field(type = FieldType.Nested, analyzer = "not_analyzed", store = true)*/
	private MonkeyType monkeyType;

	/** The Team name . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String teamName;

	/** monkeyStrategyScript **/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String monkeyStrategyScript;

	/** The defaultFlag  // used for auto discovery of scenarios*/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String defaultFlag;

	/** The strategyDescription  // used for auto discovery of scenarios*/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String strategyDescription;

	/** The monkeyStrategyVersion */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String monkeyStrategyVersion;
	
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
		
	/** Generic*/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String generic;
	
	/** Script Type Category*/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String scriptTypeCategory;

	/** createdBy **/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String createdBy;
	
	/** createDate **/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String createDate;
	
	/** lastModifyBy **/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String lastModifyBy;
	
	/** lastModifyDate **/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String lastModifyDate;
	
	public String getMonkeyStrategyName() {
		return monkeyStrategyName;
	}

	public void setMonkeyStrategyName(String monkeyStrategyName) {
		this.monkeyStrategyName = monkeyStrategyName;
	}

	public MonkeyType getMonkeyType() {
		return monkeyType;
	}

	public void setMonkeyType(MonkeyType monkeyType) {
		this.monkeyType = monkeyType;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getMonkeyStrategyScript() {
		return monkeyStrategyScript;
	}

	public void setMonkeyStrategyScript(String monkeyStrategyScript) {
		this.monkeyStrategyScript = monkeyStrategyScript;
	}

	public String getMonkeyStrategyVersion() {
		return monkeyStrategyVersion;
	}

	public void setMonkeyStrategyVersion(String monkeyStrategyVersion) {
		this.monkeyStrategyVersion = monkeyStrategyVersion;
	}

	public String getId() {
		return id;
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

	public String getGeneric() {
		return generic;
	}

	public void setGeneric(String generic) {
		this.generic = generic;
	}

	public String getScriptTypeCategory() {
		return scriptTypeCategory;
	}

	public void setScriptTypeCategory(String scriptTypeCategory) {
		this.scriptTypeCategory = scriptTypeCategory;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	public String getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	
	public String getStrategyDescription() {
		return this.strategyDescription;
	}

	public void setStrategyDescription(String strategyDescription) {
		this.strategyDescription = strategyDescription;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
	public String getDefaultFlag() {
		return defaultFlag;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
