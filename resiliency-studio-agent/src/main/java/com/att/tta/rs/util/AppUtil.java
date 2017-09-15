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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.att.tta.rs.model.EventJobDTO;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * This class provides Utility methods to connect to the UNIX server and run the
 * script.
 * 
 * @author ak983d
 *
 */
public class AppUtil {
	private static final Logger logger = LoggerFactory.getLogger(AppUtil.class);

	private JSch jschSSH;
	private Session session;

	private String strUserName;
	private String strConnectionIP;
	private String intConnectionPort;
	private String strConKey;
	private String errorStr = "Error connecting to the instance with private key, ip -->";
	private String successStr = "Successfully connected to -->";
	private static final String RSAGENT = "RS-Agent-";

	public AppUtil(String userName, String strConKey, String connectionIP, String port) {
		jschSSH = new JSch();
		this.strUserName = userName;
		this.strConKey = strConKey;
		this.strConnectionIP = connectionIP;
		this.intConnectionPort = port;
	}

	/**
	 * This method creates a JSCH connection based on private key.
	 * 
	 * @param statusString
	 */
	public void sessionConPrvaiteKey(StringBuilder statusString) {
		try {
			jschSSH.addIdentity(this.strUserName, this.strConKey.getBytes(), null, new byte[0]);
			session = jschSSH.getSession(this.strUserName, this.strConnectionIP,
					Integer.valueOf(this.intConnectionPort));
			setUpHostKey(session);
			session.connect();
		} catch (Exception ex) {
			logger.error(errorStr + this.strConnectionIP + ex);
			if (statusString != null) {
				statusString.append(errorStr + this.strConnectionIP);
				statusString.append(ex.getMessage());
			}

		}
		logger.debug(successStr + this.strConnectionIP);
	}

	/**
	 * This method creates a JSCH connection based on private key.
	 * 
	 * @param statusString
	 */
	public void sessionConPrivateFile(StringBuilder statusString) {
		try {
			jschSSH.addIdentity(strConKey);
			session = jschSSH.getSession(this.strUserName, this.strConnectionIP,
					Integer.valueOf(this.intConnectionPort));
			setUpHostKey(session);
			session.connect();
		} catch (Exception ex) {
			logger.error(errorStr + this.strConnectionIP + ex);
			if (statusString != null) {
				statusString.append(errorStr + this.strConnectionIP);
				statusString.append(ex.getMessage());
			}

		}
		logger.debug(successStr + this.strConnectionIP);
	}

	/**
	 * This method creates a JSCH connection based on password.
	 * 
	 * @param statusString
	 */
	public void sessionConPassword(StringBuilder statusString) {
		try {
			session = jschSSH.getSession(this.strUserName, this.strConnectionIP,
					Integer.valueOf(this.intConnectionPort));
			session.setPassword(this.strConKey);
			setUpHostKey(session);
			session.connect();
		} catch (Exception ex) {
			logger.error(errorStr + this.strConnectionIP + ex);
			if (statusString != null) {
				statusString.append(errorStr + this.strConnectionIP);
				statusString.append(ex.getMessage());
			}

		}
		logger.debug(successStr + this.strConnectionIP);
	}

	/**
	 * This method sets the JSCH configuration.
	 * 
	 * @param session
	 * 
	 */
	private void setUpHostKey(Session session) {
		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
	}

	/**
	 * This method puts Script file, configuration file on the destination
	 * server.
	 * 
	 * @param eventJobDTO
	 * @param statusString
	 * @param configFileName
	 * @param privateKeyName
	 * @return
	 */
	private String putScript(EventJobDTO eventJobDTO, StringBuilder statusString, String configFileName,
			String privateKeyName) {
		String filename = null;
		Channel channel = null;
		try {
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;

			if ("UNIX Script".equalsIgnoreCase(eventJobDTO.getMonkeyScriptType())) {
				filename = eventJobDTO.getMonkeyStrategyName() + "_" + eventJobDTO.getMonkeyStrategyVersion() + "_"
						+ eventJobDTO.getMonkeyStrategyId() + "_" + eventJobDTO.getEventId() + ".sh";
				channelSftp.put(new ByteArrayInputStream(eventJobDTO.getMonkeyScriptContent().getBytes()),
						"/tmp/" + filename);
			} else {
				String parentDirName = RSAGENT + eventJobDTO.getEventId();
				filename = eventJobDTO.getMonkeyStrategyName() + "_" + eventJobDTO.getMonkeyStrategyVersion() + "_"
						+ eventJobDTO.getMonkeyStrategyId() + "_" + eventJobDTO.getEventId() + ".yml";

				channelSftp.mkdir(parentDirName);
				channelSftp.put(new ByteArrayInputStream(eventJobDTO.getMonkeyScriptContent().getBytes()),
						parentDirName + "/" + filename);
				channelSftp.put(new ByteArrayInputStream(eventJobDTO.getConfigFile().getBytes()),
						parentDirName + "/" + configFileName);

				if (null != eventJobDTO.getPrivateKey() && null != eventJobDTO.getPrivateKey().getBytes()) {
					channelSftp.put(new ByteArrayInputStream(eventJobDTO.getPrivateKey().getBytes()),
							".ssh/" + privateKeyName);
				}

			}

		} catch (Exception ex) {
			logger.error(" Error while putting a Script file to the instance -->" + filename + "\n" + ex);
			if (statusString != null) {
				statusString.append(" Error while putting a Script file to the instance -->" + filename);
				statusString.append(ex.getMessage());
			}
			if (null != session) {
				session.disconnect();
			}
		} finally {
			if (null != channel) {
				channel.disconnect();
			}
		}
		return filename;
	}

	/**
	 * This method executes a Shell script.
	 * 
	 * @param eventJobDTO
	 * @param args
	 * @param statusString
	 * @param filename
	 * @param configFileName
	 * @param eventData
	 * @return
	 */
	private String executeScript(EventJobDTO eventJobDTO, List<String> args, StringBuilder statusString,
			String filename, String configFileName, ConcurrentMap<String, String> eventData) {
		String exitStatusCode = "-1";
		ChannelExec channel = null;
		try {
			channel = (ChannelExec) session.openChannel("exec");

			if ("UNIX Script".equalsIgnoreCase(eventJobDTO.getMonkeyScriptType())) {
				((ChannelExec) channel).setCommand("bash /tmp/" + filename + setCommandArguments(args));
			} else {
				String parentDirName = RSAGENT + eventJobDTO.getEventId();
				String command = "ansible-playbook " + parentDirName + "/" + filename + " -v";
				command = "ansible-playbook -i" + parentDirName + "/" + configFileName + " " + parentDirName + "/"
						+ filename + " -v";

				logger.debug(" running ansible script with command ->:  " + command);

				((ChannelExec) channel).setCommand(command);
				channel.setPty(true);
			}

			channel.setInputStream(null);
			channel.setOutputStream(System.out);
			exitStatusCode = getScriptResponse(channel, statusString, eventData, eventJobDTO);
		} catch (Exception ex) {
			logger.error(" Error while running a Script file -->" + filename + "\n" + ex);
			if (statusString != null) {
				statusString.append(" Error while running a Script file -->" + filename);
				statusString.append(ex.getMessage());
			}
			if (null != session) {
				session.disconnect();
			}
		} finally {
			if (null != channel) {
				channel.disconnect();
			}
		}
		return exitStatusCode;
	}

	/**
	 * This method is to set the command arguments.
	 * 
	 * @param args
	 * @return
	 */
	private String setCommandArguments(List<String> args) {
		StringBuilder sb = new StringBuilder();
		if (args != null) {
			for (String arg : args) {
				sb.append(" ");
				sb.append(arg);
			}
		}
		return sb.toString();
	}

	/**
	 * This method is to get the script execution response.
	 * 
	 * @param channel
	 * @param statusString
	 * @param eventData
	 * @param eventJobDTO
	 * @return
	 * @throws IOException
	 * @throws JSchException
	 */
	private String getScriptResponse(ChannelExec channel, StringBuilder statusString,
			ConcurrentMap<String, String> eventData, EventJobDTO eventJobDTO) throws IOException, JSchException {
		String exitStatusCode;
		InputStream out = channel.getInputStream();
		InputStream stderr = channel.getErrStream();
		channel.connect();

		StringBuilder outputBuffer = new StringBuilder();
		byte[] tmp = new byte[1024];
		byte[] tmp2 = new byte[1024];
		while (true) {
			boolean getData = false;
			while (out.available() > 0) {
				int i = out.read(tmp, 0, 1024);
				if (i < 0) {
					break;
				}
				outputBuffer.append(new String(tmp, 0, i));
				getData = true;
			}
			while (stderr.available() > 0) {
				int i = stderr.read(tmp2, 0, 1024);
				if (i < 0) {
					break;
				}
				outputBuffer.append(new String(tmp2, 0, i));
				getData = true;
			}
			if (getData) {
				eventData.put(eventJobDTO.getEventId(), eventJobDTO.getExecStatus() + "\n" + outputBuffer.toString());
			}

			if (channel.isEOF()) {
				break;
			}
		}

		exitStatusCode = channel.getExitStatus() + "";
		statusString.append(outputBuffer.toString());
		return exitStatusCode;
	}

	/**
	 * This method is to execute a UNIX script.
	 * 
	 * @param eventJobDTO
	 * @param args
	 * @param statusString
	 * @param eventData
	 * @return
	 */
	public String executeUNIXScript(EventJobDTO eventJobDTO, List<String> args, StringBuilder statusString,
			ConcurrentMap<String, String> eventData) {
		String exitStatusCode = "-1";
		try {
			String filename = putScript(eventJobDTO, statusString, null, null);
			if (null == statusString || "".equalsIgnoreCase(statusString.toString())) {
				exitStatusCode = executeScript(eventJobDTO, args, statusString, filename, null, eventData);
				removeScript(filename, "/tmp/", statusString);
			}
		} catch (Exception e) {
			logger.error(" Error Running a Shell Script on the instance with private key, ip -->" + this.strConnectionIP
					+ "\n" + e);
		} finally {
			if (null != session) {
				session.disconnect();
			}
		}
		return exitStatusCode;
	}

	/**
	 * This method is to execute a Ansible script.
	 * 
	 * @param eventJobDTO
	 * @param args
	 * @param statusString
	 * @param eventData
	 * @return
	 */
	public String executeAnsibleScript(EventJobDTO eventJobDTO, List<String> args, StringBuilder statusString,
			ConcurrentMap<String, String> eventData) {
		String exitStatusCode = "-1";
		try {
			String parentDirName = RSAGENT + eventJobDTO.getEventId();
			String configFileName = eventJobDTO.getMonkeyStrategyName() + "_" + eventJobDTO.getMonkeyStrategyVersion()
					+ "_" + eventJobDTO.getMonkeyStrategyId() + "_" + eventJobDTO.getEventId() + "_config";
			String privateKeyName = eventJobDTO.getEventId() + "_" + eventJobDTO.getServerName();

			String filename = putScript(eventJobDTO, statusString, configFileName, privateKeyName);

			if (null == statusString || "".equalsIgnoreCase(statusString.toString())) {
				exitStatusCode = executeScript(eventJobDTO, args, statusString, filename, configFileName, eventData);
				if (null != eventJobDTO.getPrivateKey() && null != eventJobDTO.getPrivateKey().getBytes()) {
					removeScript(privateKeyName, ".ssh/", statusString);
				}
				removeScript(configFileName, parentDirName + "/", statusString);
				removeScript(filename, parentDirName + "/", statusString);
				removeParentDir(parentDirName, statusString);
			}

		} catch (Exception e) {
			logger.error(" Error Running a Shell Script on the instance with private key, ip -->" + this.strConnectionIP
					+ "\n" + e);
		} finally {
			if (null != session) {
				session.disconnect();
			}
		}
		return exitStatusCode;
	}

	/**
	 * This method is to delete a script file from destination server.
	 * 
	 * @param fileName
	 * @param path
	 * @param statusString
	 */
	private void removeScript(String fileName, String path, StringBuilder statusString) {
		Channel channel = null;
		try {
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;
			channelSftp.rm(path + fileName);
		} catch (Exception ex) {
			logger.error(" Error deleting a file -->" + fileName + "\n" + ex);
			if (statusString != null) {
				statusString.append(" Error deleting a file-->" + fileName);
				statusString.append(ex.getMessage());
			}
		} finally {
			if (null != channel) {
				channel.disconnect();
			}
		}
	}

	/**
	 * This method is to delete a directory from destination server.
	 * 
	 * @param fileName
	 * @param path
	 * @param statusString
	 */
	private void removeParentDir(String dirName, StringBuilder statusString) {
		Channel channel = null;
		try {
			channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;
			channelSftp.rmdir(dirName);
		} catch (Exception ex) {
			logger.error(" Error deleting a Directory -->" + dirName + "\n" + ex);
			if (statusString != null) {
				statusString.append(" Error deleting a Directory -->" + dirName);
				statusString.append(ex.getMessage());
			}
		} finally {
			if (null != channel) {
				channel.disconnect();
			}
		}
	}

}
