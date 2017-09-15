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
package com.att.tta.rs.data.es.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.att.tta.rs.model.EventRecorder;

/**
 * This class provides query implementation for EventRepository
 * @author mb6872,ak983d
 *
 */
@Repository
public interface EventRepository extends ElasticsearchRepository<EventRecorder, String> {
	
	Long countByTeamName(String teamName);

	Iterable<EventRecorder> findByTeamName(String teamName);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"resiliencystudio.monkeyType\": \"?0\"}}]}}")
	Page<EventRecorder> findByMonkeyTypeUsingCustomQuery(String name, Pageable pageable);

	@Query("{\"bool\" : {\"must\" : [ {\"term\" : {\"_id\" : \"?0\"}}  , {\"term\" : {\"teamName\" : \"?1\"}}    ] }}")
	EventRecorder findOneForTeam(String id, String teamName);

	@Query("{\"bool\" : {\"must\" : {\"term\" : {\"teamName\" : \"?0\"}}}},\"sort\":{\"eventStartTime\":{\"order\":\"desc\"}}")
	Page<EventRecorder> findByTeamName(String name, Pageable pageable);

	@Query("{\"bool\" :{\"must\" : [ {\"term\" : {\"teamName\" : \"?0\"}},  {\"term\" : {\"applicationName\" : \"?1\"}} ] }},\"sort\":{\"eventStartTime\":{\"order\":\"desc\"}}")
	Page<EventRecorder> findByAppName(String teamName, String appName, Pageable pageable);
}
