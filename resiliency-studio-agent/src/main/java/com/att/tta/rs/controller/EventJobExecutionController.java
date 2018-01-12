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
import com.att.tta.rs.model.EventMonkeyStrategyDTO;
import com.att.tta.rs.service.EventJobService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
@Api(value = "Event Execution Controller", description = "This REST controller provides suit of REST APIs to execute a Scenario and stream the real time event status")
public class EventJobExecutionController {
	public static final Logger logger = LoggerFactory.getLogger(EventJobExecutionController.class);

	private static final ConcurrentMap<String, String> eventData = new ConcurrentHashMap<>();

	private String completeString = "##Completed##";
	private String startString = "Job Execution has been started at Agent Controller for Monkey Strategy: ";
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
	@ApiOperation(value = "This API executes a Monkey Scripts for given Scenario Object", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Scenario is executed Successfully"),
			@ApiResponse(code = 404, message = "The resource trying to reach is not found") })
	@RequestMapping(value = "/execJob/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> executeJob(@RequestBody EventJobDTO eventJobDTO) {
		StringBuilder errorMsg = new StringBuilder();
		if (validateEventJobDTO(eventJobDTO, errorMsg)) {
			logger.error(errorMsg.toString());
			eventJobDTO.getEventMonkeyList().forEach(eventMonkeyDTO -> {
				eventMonkeyDTO.setReturnCode("000");
				eventMonkeyDTO.setExecStatus(errorMsg.toString());
			});
			eventData.put(eventJobDTO.getEventId(), errorMsg + completeString);
			return new ResponseEntity<>(eventJobDTO, HttpStatus.OK);
		}

		eventJobDTO.setExecStatus(startString);
		eventData.put(eventJobDTO.getEventId(), startString);
		eventJobDTO.getEventMonkeyList().forEach(
				eventMonkeyDTO -> eventMonkeyDTO.setExecStatus(startString + eventMonkeyDTO.getMonkeyStrategy()));

		eventJobService.executeJob(eventJobDTO, eventData);

		eventJobDTO.getEventMonkeyList().forEach(eventMonkeyDTO -> {
			if (eventMonkeyDTO.getExecStatus() != null) {
				int maxLength = (eventMonkeyDTO.getExecStatus().length() < 32765)
						? eventMonkeyDTO.getExecStatus().length() : 32765;
				eventMonkeyDTO.setExecStatus(eventMonkeyDTO.getExecStatus().substring(0, maxLength));
			}
		});

		return new ResponseEntity<>(eventJobDTO, HttpStatus.OK);
	}

	/**
	 * This method validates the Event Job DTO for Monkey Script Type and Monkey
	 * Strategy Content.
	 * 
	 * @param eventJobDTO
	 * @param errorMsg
	 * @return
	 */
	private boolean validateEventJobDTO(EventJobDTO eventJobDTO, StringBuilder errorMsg) {
		boolean validationError = false;
		for (EventMonkeyStrategyDTO dto : eventJobDTO.getEventMonkeyList()) {
			if (dto.getMonkeyScriptType() == null || "".equalsIgnoreCase(dto.getMonkeyScriptType())) {
				errorMsg.append("Monkey Script Type is blank for Monkey Strategy : " + dto.getMonkeyStrategy()
						+ " . So Scenario Execution is terminated.");
				validationError = true;
			} else if (dto.getMonkeyScriptContent() == null || "".equalsIgnoreCase(dto.getMonkeyScriptContent())) {
				errorMsg.append("Monkey Script Content is empty for Monkey Strategy : " + dto.getMonkeyStrategy()
						+ " . So Scenario Execution is terminated.");
				validationError = true;
			}

			if (validationError)
				break;
		}
		return validationError;
	}

	/**
	 * This method is used to stream the real time event data to the calling
	 * application for given Event ID.
	 * 
	 * @param eventId
	 * @return
	 */
	@ApiOperation(value = "This API streams real time Event Job status for given Event ID", response = ResponseBodyEmitter.class)
	@RequestMapping(value = "/eventStatus/{eventId}", method = RequestMethod.GET)
	public ResponseBodyEmitter getEventStream(@PathVariable("eventId") String eventId) {
		final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		logger.debug("Event ID in the request : %S ", eventId);
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(() -> checkEvetnStatus(eventId, emitter));
		service.shutdown();
		return emitter;
	}

	private void checkEvetnStatus(String eventId, SseEmitter emitter) {
		entryFlag = true;
		retry = 0;
		while (true) {
			try {
				if (sendEventStatus(emitter, eventId)) {
					break;
				}
				Thread.sleep(500);
			} catch (Exception e) {
				logger.error("Error in checkEvetnStatus method: " + e.getMessage());
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
				// eventStatus = eventStatus.replace(completeString, "");
				emitter.send(eventStatus, org.springframework.http.MediaType.TEXT_PLAIN);
				eventData.remove(eventId);
				return true;
			}
			emitter.send(eventStatus, org.springframework.http.MediaType.TEXT_PLAIN);
			return false;
		} else {
			retry++;
			if (checkCondition()) {
				logger.error("No Event data available to stream for event ID: %S", eventId);
				eventData.remove(eventId);
				emitter.send("No Event data available to stream for event ID: " + eventId,
						org.springframework.http.MediaType.TEXT_PLAIN);
				return true;
			}
			return false;
		}
	}

	private boolean checkCondition() {
		return retry > 90 || (retry > 5 && !entryFlag);
	}
}
