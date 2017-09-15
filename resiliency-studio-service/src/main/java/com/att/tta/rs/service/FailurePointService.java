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

import java.util.List;
import java.util.Map;

import com.att.tta.rs.model.FailurePoint;
import com.att.tta.rs.model.Server;

/** service interface class for Failure point 
 * @author mb6872,ak983d
 *
 */
public interface FailurePointService {

	/** find Failurepoint by Id
	 * @param id
	 * @return
	 */
	FailurePoint findOne(String id);

	/** find all Failure points
	 * @return
	 */
	Iterable<FailurePoint> findAll();

	/** find failure point by name
	 * @param name
	 * @return
	 */
	FailurePoint findByName(String name);

	/** find all failure points with matching category
	 * @param category
	 * @return
	 */
	List<FailurePoint> findByCategory(String category);

	/** Find failure points by Role
	 * @param role
	 * @return
	 */
	List<FailurePoint> findByRole(String role);

	/** Find failure points by component
	 * @param component
	 * @return
	 */
	List<FailurePoint> findByComponent(String component);

	/** find failure points with matching processName
	 * @param processName
	 * @return
	 */
	List<FailurePoint> findByProcessName(String processName);

	/** total number of failure points
	 * @return
	 */
	long count();

	/** delete failurepoint
	 * @param failurePoint
	 */
	void delete(FailurePoint failurePoint);

	/**
	 *  delete All failure points
	 */
	void deleteAllFailurePoint();

	/** check if failurepoint exist
	 * @param failurePoint
	 * @return
	 */
	boolean isFailurePointExist(FailurePoint failurePoint);

	/** update failurepoint
	 * @param failurePoint
	 * @return
	 */
	FailurePoint update(FailurePoint failurePoint);

	/** add Failure point
	 * @param failurePoint
	 * @return
	 */
	FailurePoint insert(FailurePoint failurePoint);

	/** save Failurepoint 
	 * @param failurePoint
	 * @return
	 */
	FailurePoint save(FailurePoint failurePoint);

	/** Find failure point by category and role 
	 * @param category
	 * @param serverList
	 * @return
	 */
	Map<String, List<FailurePoint>> findByCategoryAndRole(String category, List<Server> serverList);

}
