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


/**
 * @author mb6872
 *
 */
public class HardwareDetails {
	
	private String memory;
	private String cpu;
	private String storage;
	private String operatingSystem;
	private String operatingSystemType;
	/**
	 * @return the memory
	 */
	public String getMemory() {
		return memory;
	}
	/**
	 * @param memory the memory to set
	 */
	public void setMemory(String memory) {
		this.memory = memory;
	}
	/**
	 * @return the cpu
	 */
	public String getCpu() {
		return cpu;
	}
	/**
	 * @param cpu the cpu to set
	 */
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	/**
	 * @return the storage
	 */
	public String getStorage() {
		return storage;
	}
	/**
	 * @param storage the storage to set
	 */
	public void setStorage(String storage) {
		this.storage = storage;
	}
	/**
	 * @return the operatingSystem
	 */
	public String getOperatingSystem() {
		return operatingSystem;
	}
	/**
	 * @param operatingSystem the operatingSystem to set
	 */
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	/**
	 * @return the operatingSystemType
	 */
	public String getOperatingSystemType() {
		return operatingSystemType;
	}
	/**
	 * @param operatingSystemType the operatingSystemType to set
	 */
	public void setOperatingSystemType(String operatingSystemType) {
		this.operatingSystemType = operatingSystemType;
	}
	
	
	

}
