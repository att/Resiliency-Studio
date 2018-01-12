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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EUtil {

	private static final Logger logger = LoggerFactory.getLogger(EUtil.class);

	/**
	 * This method is used to encrypt a string.
	 * 
	 * @param value
	 * @return
	 */
	public static String encrypt(String value) {
		return encrypt(new StringBuilder(value), 0xFACA);
	}

	/**
	 * This method is used to decrypt a string.
	 * 
	 * @param value
	 * @return
	 */
	public static String decrypt(String encrypted) {
		return decrypt(new StringBuilder(encrypted), 0xFACA);
	}

	private static String encrypt(StringBuilder str, int key) {
		for (int i = 0; i <= (str.length() - 1); i++) {
			char c = (char) (str.charAt(i) - key);
			str.setCharAt(i, c);
		}
		logger.debug("Encryption Key is -->" + key);
		logger.debug("Encrypted String is -->" + str);
		return str.toString();
	}

	private static String decrypt(StringBuilder str, int key) {
		for (int i = 0; i <= (str.length() - 1); i++) {
			char c = (char) (str.charAt(i) + key);
			str.setCharAt(i, c);
		}
		logger.debug("Decryption Key is -->" + key);
		logger.debug("Decrypted String is -->" + str);
		return str.toString();
	}

}
