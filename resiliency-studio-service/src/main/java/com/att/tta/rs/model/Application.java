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

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * This class is the representation of 'Application' type in the Elastic Search.
 * 
 * @author ak983d
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@Document(indexName = "resiliencystudio", type = "application")
public class Application implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The application Id . */
	@Id
	private String id;

	/** The application name . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String applicationName;

	/** The application category. */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String category;

	/** The Team name . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String teamName;

	/** Environment Network Architecture URL **/
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String networkArchitectureURL;

	/** The fields. */
	private Map<String, String> fields = new HashMap<>();

	/** The Role (AppServer/WEb Server) per Shrinath. */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String role;

	/** Map of Environment names, Environments **/
	private LinkedHashMap<String, Environment> environmentMap;

	/** Environment Network Architecture Diagram **/
	private File networkArchitectureDiagram;

	public Application() {
		super();
	}
	
	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 * @return the name
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
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

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the environmentMap
	 */
	public Map<String, Environment> getEnvironmentMap() {
		return environmentMap;
	}

	/**
	 * @param environmentMap
	 *            to set
	 */
	public void setEnvironmentMap(LinkedHashMap<String, Environment> environmentMap) {
		this.environmentMap = environmentMap;
	}

	public void addEnvironment(Environment environment) {
		if (environmentMap == null)
			environmentMap = new LinkedHashMap<>();
		if (environment != null)
			environmentMap.put(environment.getName(), environment);
	}

	public Environment getEnvironment(String environmentName) {
		if (environmentMap == null)
			return null;
		return environmentMap.get(environmentName);
	}

	/**
	 * @return the networkArchitectureDiagram
	 */
	public File getNetworkArchitectureDiagram() {
		return networkArchitectureDiagram;
	}

	/**
	 * @param networkArchitectureDiagram
	 *            the networkArchitectureDiagram to set
	 */
	public void setNetworkArchitectureDiagram(File networkArchitectureDiagram) {
		this.networkArchitectureDiagram = networkArchitectureDiagram;
	}

	@JsonAnySetter
	public Application addField(String name, String value) {
		fields.put(name, value);
		return this;
	}

	/**
	 * Adds the Application fields.
	 *
	 * @param toAdd
	 *            the fields to set
	 * @return <b>this</b> so you can chain many addFields calls together
	 */
	public Application addFields(Map<String, String> toAdd) {
		fields.putAll(toAdd);
		return this;
	}

	@JsonAnyGetter
	public Map<String, String> fields() {
		return Collections.unmodifiableMap(fields);
	}

	/**
	 * @return the networkArchitectureURL
	 */
	public String getNetworkArchitectureURL() {
		return networkArchitectureURL;
	}

	/**
	 * @param networkArchitectureURL
	 *            the networkArchitectureURL to set
	 */
	public void setNetworkArchitectureURL(String networkArchitectureURL) {
		this.networkArchitectureURL = networkArchitectureURL;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
