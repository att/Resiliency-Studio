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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

/**
 * This class is the representation of 'Event' type in the Elastic Search.
 * 
 * @author ak983d
 */
@Document(indexName = "resiliencystudio", type = "event")
public class EventRecorder implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** The event id. */
	@Id
	private String id;

	/** The Team name . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String eventStatus;

	/** The Team name . */
	@Field(type = FieldType.Nested, analyzer = "keyword", store = true)
	private EventStatusUpdate eventStatusUpdate;

	/** The Team name . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String teamName;

	/** The Scenario id . */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String scenarioId;

	@Field(type = FieldType.Date, store = true)
	private Date eventStartTime;

	/** Job Execution Result. */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String execStatus;

	/** The fields. */
	private Map<String, String> fields = new HashMap<>();

	/** The event time. */
	private Date date;

	/** The event status */
	private EventStatus eventStatusType;

	/** The Event Execution Duration. */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String execDuration;

	/** The Event Execution Duration. */
	@Field(type = FieldType.String, analyzer = "keyword", store = true)
	private String execSequence;

	
	public String getExecSequence() {
		return execSequence;
	}

	public void setExecSequence(String execSequence) {
		this.execSequence = execSequence;
	}

	public String getExecDuration() {
		return execDuration;
	}

	public void setExecDuration(String execDuration) {
		this.execDuration = execDuration;
	}

	/**
	 * Constructor.
	 *
	 */
	public EventRecorder() {
		/*
		 * This is the empty constructor.
		 */
	}
	
	public String getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
	}

	public String getExecStatus() {
		return execStatus;
	}

	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date eventTime() {
		return new Date(date.getTime());
	} 

	@JsonAnyGetter
	public Map<String, String> fields() {
		return fields;
	}

	public String field(String name) {
		return fields.get(name);
	}

	@JsonAnySetter
	public EventRecorder addField(String name, String value) {
		fields.put(name, value);
		return this;
	}

	public void removeField(String name, String value) {
		fields.remove(name, value);

	}

	/**
	 * Adds the fields.
	 *
	 * @param toAdd
	 *            the fields to set
	 * @return <b>this</b> so you can chain many addFields calls together
	 */
	public EventRecorder addFields(Map<String, String> toAdd) {
		fields.putAll(toAdd);
		return this;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Date getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public EventStatusUpdate getEventStatusUpdate() {
		return eventStatusUpdate;
	}

	public void setEventStatusUpdate(EventStatusUpdate eventStatusUpdate) {
		this.eventStatusUpdate = eventStatusUpdate;
	}

	public EventStatus getEventStatusType() {
		return eventStatusType;
	}

	public void setEventStatusType(EventStatus eventStatusType) {
		this.eventStatusType = eventStatusType;
	}
}
