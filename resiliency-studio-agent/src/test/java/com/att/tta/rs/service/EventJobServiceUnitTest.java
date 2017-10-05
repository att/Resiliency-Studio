
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

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.att.tta.rs.model.EventJobDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
@WebAppConfiguration
public class EventJobServiceUnitTest {

	@Autowired
	EventJobService eventJobService;

	private String privateKey  = "\nPrivate key";
	private String password  = "\npassword ";
	private String unixErr2 = " Error connecting to instance Name: host IP: 1.1.1.1 and port: 22 and user: user";
	private String unixErr1 = " present executing on the instance host IP Address 1.1.1.1\n"; 
	private String ansibleErr = "Error connecting to Ansible instance server.";
	private String err = "\nNo password and No key present executing on the discovery server discoverHostName IP Address null\n";
	private String fuelErr = "\nNo UserId or password found for Discovery Server";
			
	private String pvtKeyErr = privateKey+unixErr1+unixErr2;
	private String ansiblePvtKeyErr = privateKey+unixErr1+ansibleErr;
	private String passwordKeyErr = password+unixErr1+unixErr2;	
	private String ansiblePasswordKeyErr = password+unixErr1+ansibleErr;
	private String fuelDiscoverErr = err+unixErr2;
	private String ansibleFuelDiscoverErr = err+ansibleErr;

	private String returnCode = "-1";
	private String ansibleScript = "Ansible Script";

	/**
	 * This Test method validates the functionality of UNIX Script Execution
	 * 
	 */
	@Test
	public void testUNIXScriptExecutionErrCndtn() {
		ConcurrentMap<String, String> eventData = new ConcurrentHashMap<>();
		EventJobDTO dto = createEventJobDTO();
		eventJobService.executeJob(dto, eventData);
		assertEquals(pvtKeyErr, dto.getExecStatus());
		assertEquals(returnCode, dto.getReturnCode());

		dto = createEventJobDTO();
		dto.setPrivateKey("");
		eventJobService.executeJob(dto, eventData);
		assertEquals(passwordKeyErr, dto.getExecStatus());
		assertEquals(returnCode, dto.getReturnCode());

		dto = createEventJobDTO();
		dto.setPrivateKey("");
		dto.setPassword("");
		eventJobService.executeJob(dto, eventData);
		assertEquals(fuelErr, dto.getExecStatus());
		assertEquals(returnCode, dto.getReturnCode());

		dto = createEventJobDTO();
		dto.setPrivateKey("");
		dto.setPassword("");
		setDiscoverDetails(dto);
		eventJobService.executeJob(dto, eventData);
		assertEquals(fuelDiscoverErr, dto.getExecStatus());
		assertEquals(returnCode, dto.getReturnCode());
	}

	/**
	 * This Test method validates the functionality of Ansible Script Execution
	 * 
	 */
	@Test
	public void testAnsibleScriptExecutionErrCndtn() {
		ConcurrentMap<String, String> eventData = new ConcurrentHashMap<>();
		EventJobDTO dto = createEventJobDTO();
		dto.setMonkeyScriptType(ansibleScript);
		eventJobService.executeJob(dto, eventData);
		assertEquals(ansiblePvtKeyErr, dto.getExecStatus());
		assertEquals(returnCode, dto.getReturnCode());

		dto = createEventJobDTO();
		dto.setMonkeyScriptType(ansibleScript);
		dto.setPrivateKey("");
		eventJobService.executeJob(dto, eventData);
		assertEquals(ansiblePasswordKeyErr, dto.getExecStatus());
		assertEquals(returnCode, dto.getReturnCode());

		dto = createEventJobDTO();
		dto.setMonkeyScriptType(ansibleScript);
		dto.setPrivateKey("");
		dto.setPassword("");
		eventJobService.executeJob(dto, eventData);
		assertEquals(fuelErr, dto.getExecStatus());
		assertEquals(returnCode, dto.getReturnCode());

		dto = createEventJobDTO();
		dto.setPrivateKey("");
		dto.setPassword("");
		dto.setMonkeyScriptType(ansibleScript);
		setDiscoverDetails(dto);
		eventJobService.executeJob(dto, eventData);

		assertEquals(ansibleFuelDiscoverErr, dto.getExecStatus());
		assertEquals(returnCode, dto.getReturnCode());
	}

	/**
	 * This method returns an EventJobDTO Object.
	 * 
	 * @return
	 */
	private static EventJobDTO createEventJobDTO() {
		EventJobDTO eventDTO = new EventJobDTO();

		eventDTO.setEventId("eventID");

		eventDTO.setMonkeyScriptType("UNIX Script");
		eventDTO.setMonkeyStrategyName("PingScript");
		eventDTO.setMonkeyStrategyVersion("1");
		eventDTO.setMonkeyStrategyId("msID");
		eventDTO.setMonkeyScriptContent("echo 'ping successfully'");

		eventDTO.setServerName("server");
		eventDTO.setHostName("host");
		eventDTO.setIpAdd("1.1.1.1");

		eventDTO.setUserName("user");
		eventDTO.setPrivateKey("privateKey");
		eventDTO.setPassword("password");

		eventDTO.setExecStatus("");
		eventDTO.setReturnCode("0");

		return eventDTO;
	}

	private static void setDiscoverDetails(EventJobDTO dto) {
		dto.setDiscoverUserId("discoveryUser");
		dto.setDiscoverPassword("doscoveryPwd");
		dto.setDiscoverHostName("discoverHostName");
		dto.setDiscoverHostPort("discoverHostPort");
	}
}
