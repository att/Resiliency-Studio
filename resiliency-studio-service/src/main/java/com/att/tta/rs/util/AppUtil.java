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
package com.att.tta.rs.util;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.jclouds.Constants;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.ssh.SshClient;
import org.jclouds.ssh.jsch.JschSshClient;
import org.jclouds.ssh.jsch.config.JschSshClientModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.common.net.HostAndPort;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

public class AppUtil {
	private static final Logger logger = LoggerFactory.getLogger(AppUtil.class);
	private static final String SU = "alpha";

	private AppUtil() {
		/*
		 * Empty constructor
		 */
	}

	/**
	 * This utility method returns Super Administrator team name
	 * 
	 * @return
	 */
	public static String getSuperUser() {
		return SU;
	}

	/**
	 * This method returns SSH Connection Object for given Host Server details.
	 * 
	 * @param instanceName
	 * @param ip
	 * @param port
	 * @param user
	 * @param password
	 * @return
	 */
	public static SshClient getSSHClient(String instanceName, String ip, String port, String user, String password) {
		JschSshClient ssh = null;
		try {
			final Injector i = Guice.createInjector(new JschSshClientModule(), new AbstractModule() {
				@Override
				protected void configure() {
					Names.bindProperties(binder(), new Properties());
					bindConstant().annotatedWith(Names.named(Constants.PROPERTY_CONNECTION_TIMEOUT)).to(9000);
				}
			});

			String conString = (null != instanceName && !"".equalsIgnoreCase(instanceName.trim())) ? instanceName : ip;

			final SshClient.Factory factory = i.getInstance(SshClient.Factory.class);
			ssh = JschSshClient.class.cast(factory.create(HostAndPort.fromParts(conString, Integer.valueOf(port)),
					sshConfigure(user, password)));
			ssh.connect();
		} catch (Exception e) {
			logger.debug("ERROR connecting to host " + ip, e);
			return null;
		}
		return ssh;
	}

	public static SshClient getSSHClientFromKey(String instanceName, String ip, String port, String user, String key) {
		JschSshClient ssh = null;
		try {
			final Injector i = Guice.createInjector(new JschSshClientModule(), new AbstractModule() {
				@Override
				protected void configure() {
					Names.bindProperties(binder(), new Properties());
					bindConstant().annotatedWith(Names.named(Constants.PROPERTY_CONNECTION_TIMEOUT)).to(5000);
				}
			});

			final SshClient.Factory factory = i.getInstance(SshClient.Factory.class);
			String conString = (null != instanceName && !"".equalsIgnoreCase(instanceName.trim())) ? instanceName : ip;

			ssh = JschSshClient.class.cast(factory.create(HostAndPort.fromParts(conString, Integer.valueOf(port)),
					LoginCredentials.builder().user(user).privateKey(key).build()));

			ssh.connect();
		} catch (Exception e) {
			logger.error(" ERROR connecting to the instance with private key, instanceName -->" + instanceName
					+ " , ip -->" + ip + ", port -->" + port + ", user -->" + user + ", key -->" + key, e);
			return null;
		}

		logger.debug("Successfully connected to -->" + ip);

		return ssh;
	}

	private static LoginCredentials sshConfigure(String user, String password) {
		return LoginCredentials.builder().user(user).password(password).build();
	}

	private static URL getResourceURL(String scriptName) {
		URL url = null;
		try {
			url = Resources.getResource(AppUtil.class, "/real_time_scripts/" + scriptName);
		} catch (Exception e) {
			logger.error("Exception occured : ", e);
			url = Resources.getResource(AppUtil.class, "/on_demand_scripts/" + scriptName);
		}
		return url;
	}

	/**
	 * This method executes a Shell Script.
	 * 
	 * @param sshinstance
	 * @param scriptName
	 * @param args
	 * @return
	 */
	public static String executeHardwareDiscoveryScript(SshClient sshinstance, String scriptName, List<String> args) {
		ExecResponse response = null;
		logger.debug("Running a Shell script on the SSH Instance");
		try {
			String filename = scriptName + ".sh";
			logger.debug("Running script name: " + filename);

			URL url = getResourceURL(filename);
			logger.debug("URL for Script is -->" + url);

			try {
				String script = Resources.toString(url, Charsets.UTF_8);
				sshinstance.put("/tmp/" + filename, script);

			} catch (IOException e) {
				logger.error(" Exception while placing a script -->", e);
				return null;
			}

			StringBuilder sb = new StringBuilder();
			if (args != null) {
				for (String arg : args) {
					sb.append(" ");
					sb.append(arg);
				}
			}

			logger.debug("Command executed -->" + "/bin/bash /tmp/" + filename + sb.toString());
			response = sshinstance.exec("/bin/bash /tmp/" + filename + sb.toString());

			if (response.getExitStatus() != 0) {
				logger.debug("Non-zero output after running Script:" + response);
			}
		} catch (Exception e) {
			logger.error(" Exception while running a script-->", e);
			return null;
		}
		return response.getOutput();
	}
}
