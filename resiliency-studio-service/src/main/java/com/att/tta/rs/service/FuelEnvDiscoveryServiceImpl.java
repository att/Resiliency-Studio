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

import java.io.IOException;

import org.jclouds.ssh.SshClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.tta.rs.model.FuelDiscover;
import com.att.tta.rs.model.HardwareDetails;
import com.att.tta.rs.util.AppUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class does Environment discovery for Fuel Server.
 * 
 * @author ak983d
 *
 */
public class FuelEnvDiscoveryServiceImpl implements EnvironmentDiscoveryService {
	private static final Logger logger = LoggerFactory.getLogger(FuelEnvDiscoveryServiceImpl.class);

	public HardwareDetails discoverServerHardwareDetails(FuelDiscover discoverRequest) {
		ObjectMapper mapper = new ObjectMapper();
		SshClient sshClient = null;

		if (null != discoverRequest.getPrivateKey() && !"".equalsIgnoreCase(discoverRequest.getPrivateKey())) {
			sshClient = AppUtil.getSSHClientFromKey(discoverRequest.getHostName(), discoverRequest.getIpAddress(),
					discoverRequest.getPort(), discoverRequest.getUser(), discoverRequest.getPrivateKey());
		} else if (null != discoverRequest.getPassword() && !"".equalsIgnoreCase(discoverRequest.getPassword())) {
			sshClient = AppUtil.getSSHClient(discoverRequest.getHostName(), discoverRequest.getIpAddress(),
					discoverRequest.getPort(), discoverRequest.getUser(), discoverRequest.getPassword());
		}
		if (null == sshClient) {
			logger.error("Error while connecting to Discovery Server");
			return null;
		}

		String scriptName = "discover_hardware";
		String jsonString = AppUtil.executeHardwareDiscoveryScript(sshClient, scriptName, null);
		logger.debug("Response from Server Hardware Discovery --> " + jsonString);

		HardwareDetails serverHwList = null;
		try {
			if (null != jsonString) {
				serverHwList = mapper.readValue(jsonString, HardwareDetails.class);
			}
		} catch (JsonParseException e) {
			logger.error("JSOn Parsing exception ", e);
		} catch (JsonMappingException e) {
			logger.error(" JsonMappingException ", e);
		} catch (IOException e) {
			logger.error(" IO Exception ", e);
		}
		return serverHwList;
	}
}
