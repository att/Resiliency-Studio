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
import com.att.tta.rs.model.MonkeyStrategy;
import com.att.tta.rs.model.MonkeyType;
import com.att.tta.rs.model.Server;

/**
 * This interface provides the declaration of CRUD operations for MonkeyStrategy
 * repository.
 * 
 * @author mb6872
 *
 */
public interface MonkeyStrategyService {

	/** 
	 * Find monkey strategy for id,teamname
	 * @param id
	 * @param teamName
	 * @return
	 */
	MonkeyStrategy findOneForTeam(String id, String teamName);
	
	/** 
	 * Find default monkey strategies for team
	 * @param monkeyType
	 * @param monkeyStrategyName
	 * @param defaultFlag
	 * @param teamName
	 * @return
	 */
	Iterable<MonkeyStrategy> findDefaultMonkeyStrategy(MonkeyType monkeyType, String monkeyStrategyName,
			String defaultFlag, String teamName);

	/** 
	 * Returns number of monkey strategies
	 * @return
	 */
	long count();

	/** 
	 * Returns number of monkey strategies for Team
	 * @param teamName
	 * @return
	 */
	Long countByTeamName(String teamName);

	/** 
	 * Deletes monkey strategy 
	 * @param monkeyStrategy
	 */
	void delete(MonkeyStrategy monkeyStrategy);

	/** 
	 * Check if monkey strategy exists for Team name 
	 * @param monkeyStrategy
	 * @param teamName
	 * @return
	 */
	boolean isMonkeyStrategyExistAndTeamName(MonkeyStrategy monkeyStrategy, String teamName);

	/**
	 *  Deletea All monkey strategies 
	 */
	void deleteAllMonkeyStrategy();

	/** 
	 * Updates Monkey strategies 
	 * 
	 * @param monkeyStrategy
	 * @return
	 */
	MonkeyStrategy update(MonkeyStrategy monkeyStrategy);

	/** 
	 * Adds monkey strategy for Teamname
	 * @param monkeyStrategy
	 * @param teamName
	 * @return
	 */
	MonkeyStrategy insertForTeam(MonkeyStrategy monkeyStrategy, String teamName);

	/** 
	 * Find all monkey strategies for team
	 * @param teamName
	 * @return
	 */
	Iterable<MonkeyStrategy> findAllForTeam(String teamName);

	
	/** 
	 * Find default monkey strategy for monkeyStrategyName,monkeyType,teamName
	 * @param monkeyStrategyName
	 * @param monkeyType
	 * @param teamName
	 * @return
	 */
	MonkeyStrategy findDefaultMonkeyStrategy(String monkeyStrategyName, MonkeyType monkeyType, String teamName);

	
	/** 
	 * Get monkey strategy by strategy name and Team
	 * @param monkeyStrategyName
	 * @param teamName
	 * @return
	 */
	MonkeyStrategy findByMonkeyStrategyNameAndTeamName(String monkeyStrategyName, String teamName);

	/** 
	 * Get all monkey strategies for monkeytype,strategyname,teamname
	 * 
	 * @param monkeyStrategy
	 * @param teamName
	 * @return
	 */
	Iterable<MonkeyStrategy> findByMonkeyStrategyAndTeamName(MonkeyStrategy monkeyStrategy, String teamName);

	/** 
	 * Get all monkey strategies for strategyname, teamname, version 
	 * @param monkeyStrategyName
	 * @param teamName
	 * @param monkeyStrategyVersion
	 * @return
	 */
	MonkeyStrategy findByMonkeyStrategyNameAndTeamNameAndVersion(String monkeyStrategyName, String teamName,
			String monkeyStrategyVersion);

	/** 
	 * Get all Monkey Strategy by OSType and process name
	 * 
	 * @param category
	 * @param serverList
	 * @return
	 */
	Map<String, List<FailurePoint>> findMonkeyStrategyByOSTypeAndProcessName(String category, List<Server> serverList);
		
	
	/** 
	 * All monkeyStrategies 
	 * @return
	 */
	Iterable<MonkeyStrategy> findAllMonkeyStrategies();

}
