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

package com.att.tta.rs.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.att.tta.rs.model.EventJobDTO;
import com.att.tta.rs.util.AppUtil;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * Implementation class for {@link EventJobService}
 * 
 * @author ak983d
 *
 */
@Service("eventJobService")
public class EventJobServiceImpl implements EventJobService {
	private static final Logger logger = LoggerFactory.getLogger(EventJobServiceImpl.class);
	private static final String UNIXSCRIPT = "UNIX Script";
	private static final String ANSIBLESCRIPT = "Ansible Script";
	private static final String COMPLETESTR = "##Completed##";
	private static final String PRVKEYSTR = "Private key present executing on the instance ";
	private static final String PDKEYSTR = "password  present executing on the instance ";
	private static final String ANSIBLERRSTR = "Error connecting to Ansible instance server.";
	private static final String CONNERR = " Error connecting to instance Name: ";
	private static final String PORTSTR = " and port: 22 and user: ";
	private static final String DISCOVERSTR = "No password and No key present executing on the discovery server ";
	private static final String DISCERR = "No UserId or password found for Discovery Server";
	private static final String IPADD = " IP Address ";
	
	@Autowired
	Environment env;

	@Override
	public void executeJob(EventJobDTO eventJobDTO, ConcurrentMap<String, String> eventData) {
		if (UNIXSCRIPT.equalsIgnoreCase(eventJobDTO.getMonkeyScriptType())) {
			executeUNIXScript(eventJobDTO, eventData);
		} else if (ANSIBLESCRIPT.equalsIgnoreCase(eventJobDTO.getMonkeyScriptType())) {
			executeAnsibleScript(eventJobDTO, eventData);
		}
	}

	/**
	 * This method is used to execute a UNIX Shell Script
	 * 
	 * @param eventJobDTO
	 * @param eventData
	 * @return
	 */
	private void executeUNIXScript(EventJobDTO eventJobDTO, ConcurrentMap<String, String> eventData) {
		String execStatus;
		StringBuilder statusStringBuilder = new StringBuilder();
		boolean pdExecution;
		boolean discoverExecution = false;
		AppUtil appUtil;

		String conParam = (eventJobDTO.getHostName() != null && eventJobDTO.getHostName().trim() != "")
				? eventJobDTO.getHostName() : eventJobDTO.getIpAdd();

		if (eventJobDTO.getPrivateKey() != null && !eventJobDTO.getPrivateKey().trim().isEmpty()) {
			logger.debug(PRVKEYSTR);
			execStatus = PRVKEYSTR + eventJobDTO.getHostName() + IPADD + eventJobDTO.getIpAdd();
			pdExecution = false;
		} else if (checkPwdCondition(eventJobDTO)) {
			logger.debug(PDKEYSTR);
			execStatus = PDKEYSTR + eventJobDTO.getHostName() + IPADD + eventJobDTO.getIpAdd();
			pdExecution = true;
		} else {
			if (eventJobDTO.getDiscoverUserId() == null || eventJobDTO.getDiscoverPassword() == null) {
				execStatus = DISCERR;
				eventJobDTO.setExecStatus(eventJobDTO.getExecStatus() + "\n" + execStatus);
				eventData.put(eventJobDTO.getEventId(), eventJobDTO.getExecStatus() + COMPLETESTR);
				eventJobDTO.setReturnCode("-1");
				return;
			} else {
				conParam = (eventJobDTO.getDiscoverHostName() != null && eventJobDTO.getDiscoverHostName().trim() != "")
						? eventJobDTO.getDiscoverHostName() : eventJobDTO.getDiscoverHostIpAddress();
				logger.debug(DISCOVERSTR);
				execStatus = DISCOVERSTR + eventJobDTO.getDiscoverHostName() + IPADD
						+ eventJobDTO.getDiscoverHostIpAddress();
				pdExecution = true;
				discoverExecution = true;
			}
		}
		List<String> params = setParam(pdExecution, eventJobDTO);
		appUtil = setAppUtilObj(eventJobDTO, pdExecution, discoverExecution, execStatus, eventData, conParam,
				statusStringBuilder);
		executeScript(params, appUtil, eventJobDTO, statusStringBuilder, eventData, true);
	}

	/**
	 * This method is used to execute a Ansible Shell Script
	 * 
	 * @param eventJobDTO
	 * @param eventData
	 * @return
	 */
	private void executeAnsibleScript(EventJobDTO eventJobDTO, ConcurrentMap<String, String> eventData) {
		String execStatus;
		StringBuilder statusStringBuilder = new StringBuilder();
		List<String> params = new ArrayList<>();
		AppUtil appUtil;
		if (eventJobDTO.getPrivateKey() != null && !eventJobDTO.getPrivateKey().trim().isEmpty()) {
			execStatus = PRVKEYSTR + eventJobDTO.getHostName() + IPADD + eventJobDTO.getIpAdd();
			logger.debug(execStatus);
			eventJobDTO.setExecStatus(eventJobDTO.getExecStatus() + "\n" + execStatus);
			eventData.put(eventJobDTO.getEventId(), eventJobDTO.getExecStatus());

			appUtil = setAppUtilForAnsible(eventJobDTO, eventData, statusStringBuilder);

		} else if (checkPwdCondition(eventJobDTO)) {

			execStatus = PDKEYSTR + eventJobDTO.getHostName() + IPADD + eventJobDTO.getIpAdd();
			logger.debug(execStatus);

			String conParam = (eventJobDTO.getHostName() != null && eventJobDTO.getHostName().trim() != "")
					? eventJobDTO.getHostName() : eventJobDTO.getIpAdd();
			appUtil = setAppUtilObj(eventJobDTO, true, false, execStatus, eventData, conParam, statusStringBuilder);

		} else {
			if (eventJobDTO.getDiscoverUserId() == null || eventJobDTO.getDiscoverPassword() == null) {
				execStatus = DISCERR;
				eventJobDTO.setExecStatus(eventJobDTO.getExecStatus() + "\n" + execStatus);
				eventData.put(eventJobDTO.getEventId(), eventJobDTO.getExecStatus() + COMPLETESTR);
				eventJobDTO.setReturnCode("-1");
				return;
			} else {
				String conParam = (eventJobDTO.getDiscoverHostName() != null
						&& eventJobDTO.getDiscoverHostName().trim() != "") ? eventJobDTO.getDiscoverHostName()
								: eventJobDTO.getDiscoverHostIpAddress();
				execStatus = DISCOVERSTR + eventJobDTO.getDiscoverHostName() + IPADD
						+ eventJobDTO.getDiscoverHostIpAddress();
				logger.debug(execStatus);

				appUtil = setAppUtilObj(eventJobDTO, true, true, execStatus, eventData, conParam, statusStringBuilder);
			}
		}
		executeScript(params, appUtil, eventJobDTO, statusStringBuilder, eventData, false);
	}

	/**
	 * This method is used to execute a Script
	 * 
	 * @param params
	 * @param appUtil
	 * @param eventJobDTO
	 * @param statusStringBuilder
	 * @param eventData
	 * @param unixScript
	 */
	private void executeScript(List<String> params, AppUtil appUtil, EventJobDTO eventJobDTO,
			StringBuilder statusStringBuilder, ConcurrentMap<String, String> eventData, boolean unixScript) {
		if ("".equals(statusStringBuilder.toString())) {
			String returnCode;
			if (unixScript) {
				returnCode = appUtil.executeUNIXScript(eventJobDTO, params, statusStringBuilder, eventData);
			} else {
				returnCode = appUtil.executeAnsibleScript(eventJobDTO, params, statusStringBuilder, eventData);
			}
			eventJobDTO.setExecStatus(eventJobDTO.getExecStatus() + "\n" + statusStringBuilder.toString());
			eventJobDTO.setReturnCode(returnCode);
			eventData.put(eventJobDTO.getEventId(), eventJobDTO.getExecStatus() + COMPLETESTR);

		} else {
			String execStatus;
			if (unixScript) {
				execStatus = CONNERR + eventJobDTO.getHostName() + " IP: " + eventJobDTO.getIpAdd() + PORTSTR
						+ eventJobDTO.getUserName();
			} else {
				execStatus = ANSIBLERRSTR;
			}
			eventJobDTO.setExecStatus(eventJobDTO.getExecStatus() + "\n" + execStatus);
			eventJobDTO.setReturnCode("-1");
			eventData.put(eventJobDTO.getEventId(), eventJobDTO.getExecStatus() + COMPLETESTR);
		}
	}

	/**
	 * This method sets the script execution parameter.
	 * 
	 * @param pdExecution
	 * @param eventJobDTO
	 * @return
	 */
	private List<String> setParam(boolean pdExecution, EventJobDTO eventJobDTO) {
		List<String> params = new ArrayList<>();
		if (!pdExecution) {
			if (eventJobDTO.getProcessName() != null)
				params.add(eventJobDTO.getProcessName());
		} else {
			params.add(eventJobDTO.getServerName());
			if (eventJobDTO.getProcessName() != null)
				params.add(eventJobDTO.getProcessName());
		}

		return params;
	}

	/**
	 * This method sets the AppUtil object for UNIX and Ansible script.
	 * 
	 * @param eventJobDTO
	 * @param pdExecution
	 * @param discoverExecution
	 * @param execStatus
	 * @param eventData
	 * @param conParam
	 * @param statusStringBuilder
	 * @return
	 */
	private AppUtil setAppUtilObj(EventJobDTO eventJobDTO, boolean pdExecution, boolean discoverExecution,
			String execStatus, ConcurrentMap<String, String> eventData, String conParam,
			StringBuilder statusStringBuilder) {
		AppUtil appUtil;
		eventJobDTO.setExecStatus(eventJobDTO.getExecStatus() + "\n" + execStatus);
		eventData.put(eventJobDTO.getEventId(), eventJobDTO.getExecStatus());
		if (pdExecution) {
			if (discoverExecution) {
				appUtil = new AppUtil(eventJobDTO.getDiscoverUserId(), eventJobDTO.getDiscoverPassword(), conParam,
						eventJobDTO.getDiscoverHostPort());
			} else {
				appUtil = new AppUtil(eventJobDTO.getUserName(), eventJobDTO.getPassword(), conParam, "22");
			}
			appUtil.sessionConPassword(statusStringBuilder);
		} else {
			appUtil = new AppUtil(eventJobDTO.getUserName(), eventJobDTO.getPrivateKey(), conParam, "22");
			appUtil.sessionConPrvaiteKey(statusStringBuilder);
		}
		return appUtil;
	}

	/**
	 * This method sets the AppUtil object for Ansible script based on private
	 * key.
	 * 
	 * @param eventJobDTO
	 * @param eventData
	 * @param statusStringBuilder
	 * @return
	 */
	private AppUtil setAppUtilForAnsible(EventJobDTO eventJobDTO, ConcurrentMap<String, String> eventData,
			StringBuilder statusStringBuilder) {
		AppUtil appUtil = null;
		try {
			URL privateKeyURL = Resources.getResource(EventJobServiceImpl.class, "/ansibleServer/ansiblePrivateKey");
			appUtil = new AppUtil(env.getProperty("ansibleUser"), Resources.toString(privateKeyURL, Charsets.UTF_8),
					env.getProperty("ansibleIP"), env.getProperty("ansiblePort"));

			appUtil.sessionConPrvaiteKey(statusStringBuilder);

		} catch (Exception e) {
			logger.error(ANSIBLERRSTR + e);
			eventJobDTO.setExecStatus(eventJobDTO.getExecStatus() + "\n" + ANSIBLERRSTR);
			eventJobDTO.setReturnCode("-1");
			eventData.put(eventJobDTO.getEventId(), eventJobDTO.getExecStatus() + COMPLETESTR);
		}
		return appUtil;
	}

	private boolean checkPwdCondition(EventJobDTO eventJobDTO) {
		return eventJobDTO.getPassword() != null && !eventJobDTO.getPassword().trim().isEmpty()
				&& !eventJobDTO.getPassword().trim().equals(eventJobDTO.getUserName().trim());
	}
}
