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

package com.att.tta.rs.controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.att.tta.rs.model.EventJobDTO;
import com.att.tta.rs.service.EventJobService;

/**
 * This REST Controller provided suite of REST APIs to execute a Script and
 * stream the latest event status.
 * 
 * @author ak983d
 *
 */
@EnableWebMvc
@RestController
@RequestMapping("/api")
public class EventJobExecutionController {
	public static final Logger logger = LoggerFactory.getLogger(EventJobExecutionController.class);

	private static final ConcurrentMap<String, String> eventData = new ConcurrentHashMap<>();

	private String completeString = "##Completed##";
	private String startString = "Job Execution has been started at Agent Controller.";
	private boolean entryFlag = false;
	private int retry = 0;

	@Autowired
	EventJobService eventJobService;

	/**
	 * This method is used to execute a Script for given event Job
	 * 
	 * @param eventJobDTO
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(value = "/execJob/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> executeJob(@RequestBody EventJobDTO eventJobDTO) {

		if (eventJobDTO.getMonkeyScriptType() == null || "".equalsIgnoreCase(eventJobDTO.getMonkeyScriptType())) {
			logger.error("Monkey Script Type is blank");
			final String error = "Monkey Script Type is blank.";
			eventJobDTO.setExecStatus(error);
			eventJobDTO.setReturnCode("1");
			eventData.put(eventJobDTO.getEventId(), error + completeString);
			return new ResponseEntity<>(eventJobDTO, HttpStatus.OK);
		} else if (eventJobDTO.getMonkeyScriptContent() == null
				|| "".equalsIgnoreCase(eventJobDTO.getMonkeyScriptContent())) {
			logger.error("Monkey Script Content is blank");
			final String error = "Monkey Script Content is blank.";
			eventJobDTO.setExecStatus(error);
			eventJobDTO.setReturnCode("1");
			eventData.put(eventJobDTO.getEventId(), error + completeString);
			return new ResponseEntity<>(eventJobDTO, HttpStatus.OK);
		}

		eventData.put(eventJobDTO.getEventId(), startString);
		eventJobDTO.setExecStatus(startString);
		eventJobService.executeJob(eventJobDTO, eventData);

		if (eventJobDTO.getExecStatus() != null) {
			int maxLength = (eventJobDTO.getExecStatus().length() < 32765) ? eventJobDTO.getExecStatus().length()
					: 32765;
			eventJobDTO.setExecStatus(eventJobDTO.getExecStatus().substring(0, maxLength));
		}
		return new ResponseEntity<>(eventJobDTO, HttpStatus.OK);
	}

	/**
	 * This method is used to stream the real time event data to the calling
	 * application for given Event ID.
	 * 
	 * @param eventId
	 * @return
	 */
	@RequestMapping(value = "/eventStatus/{eventId}", method = RequestMethod.GET)
	public ResponseBodyEmitter getEventStream(@PathVariable("eventId") String eventId) {
		logger.debug("Event ID in the request : " + eventId);
		final SseEmitter emitter = new SseEmitter();
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(() -> checkEvetnStatus(eventId, emitter));
		service.shutdown();
		return emitter;
	}

	
	private void checkEvetnStatus(String eventId, SseEmitter emitter) {
		while (true) {
			try {
				if (sendEventStatus(emitter, eventId)) {
					break;
				}
				Thread.sleep(500);
			} catch (Exception e) {
				logger.error("Error in getEventStream method: " + e.getMessage());
				emitter.completeWithError(e);
				return;
			}
		}
		emitter.complete();
	}

	private boolean sendEventStatus(SseEmitter emitter, String eventId) throws IOException {
		String eventStatus = eventData.get(eventId);
		if (eventStatus != null && !"".equalsIgnoreCase(eventStatus)) {
			entryFlag = true;
			retry = 0;
			eventStatus = eventStatus.replaceAll("\\r\\n|\\r|\\n", " @#");
			if (eventStatus.endsWith(completeString)) {
				eventStatus = eventStatus.replace(completeString, "");
				emitter.send(eventStatus, org.springframework.http.MediaType.TEXT_PLAIN);
				eventData.remove(eventId);
				return true;
			}
			emitter.send(eventStatus, org.springframework.http.MediaType.TEXT_PLAIN);
			return false;
		} else {
			retry++;
			if (checkCondition()) {
				logger.error("No Event data available to stream for event ID: " + eventId);
				eventData.remove(eventId);
				emitter.send("No Event data available to stream for event ID: " + eventId,
						org.springframework.http.MediaType.TEXT_PLAIN);
				return true;
			}
			return false;
		}
	}

	private boolean checkCondition() {
		if (retry > 90 || (retry > 5 && !entryFlag)) {
			return true;
		}
		return false;
	}
}
