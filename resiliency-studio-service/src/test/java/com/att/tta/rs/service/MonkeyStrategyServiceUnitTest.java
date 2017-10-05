
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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.att.tta.rs.data.es.repository.MonkeyStrategyRepository;
import com.att.tta.rs.model.MonkeyStrategy;
import com.att.tta.rs.model.MonkeyType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
public class MonkeyStrategyServiceUnitTest {

	@InjectMocks
	MonkeyStrategyServiceImpl monkeyStrategyService;

	@Mock
	private MonkeyStrategyRepository monkeyStrategyRepository;

	private static final String TEAMNAME = "TEST";
	private static final String MONKEYSTRATEGYNAME = "TestMonkeyStrategy";
	private static final String UNIXSCRIPT = "UNIX Script";
	
	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * This Test method validates the Successful insertion of 
	 * Monkey Strategy Object into Elastic Search.
	 * 
	 */
	@Test
	public void testCreateMonkeyStrategy() {

		List<MonkeyStrategy> monkeyStrategyList = new ArrayList<>();
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategyList);

		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);

		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), monkeyStrategy.getMonkeyStrategyName(), TEAMNAME))
						.thenReturn(monkeyStrategyPage);
		when(monkeyStrategyRepository.save(monkeyStrategy)).thenReturn(monkeyStrategy);

		MonkeyStrategy insertedMonkeyStrategy = monkeyStrategyService.insertForTeam(monkeyStrategy, "TEST");
		assertEquals(MONKEYSTRATEGYNAME, insertedMonkeyStrategy.getMonkeyStrategyName());
		assertEquals(MonkeyType.CHAOS, insertedMonkeyStrategy.getMonkeyType());
	}

	/**
	 * This Test method validates the Successful Deletion of MonkeyStrategy
	 * Object from Elastic Search.
	 * 
	 */
	@Test
	public void testDeleteMonkeyStrategy() {
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		monkeyStrategyService.delete(monkeyStrategy);
		verify(monkeyStrategyRepository, times(1)).delete(monkeyStrategy);
	}

	/**
	 * This Test method validates the Successful Update of Monkey Strategy
	 * Object into Elastic Search.
	 * 
	 */
	@Test
	public void testUpdateMonkeyStrategy() {
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);

		when(monkeyStrategyRepository.findOne(monkeyStrategy.getId())).thenReturn(monkeyStrategy);
		when(monkeyStrategyRepository.save(monkeyStrategy)).thenReturn(monkeyStrategy);

		MonkeyStrategy updatedMonkeyStrategy = monkeyStrategyService.update(monkeyStrategy);
		assertEquals("N", updatedMonkeyStrategy.getDefaultFlag());
		assertEquals(UNIXSCRIPT, updatedMonkeyStrategy.getScriptTypeCategory());
		assertEquals(MonkeyType.CHAOS, updatedMonkeyStrategy.getMonkeyType());
	}

	/**
	 * This Test method validates the Error Condition while updating of
	 * non-exist MonkeyStrategy Object into Elastic Search.
	 * 
	 */
	@Test
	public void testUpdateNonExistMonkeyStrategy() {
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		when(monkeyStrategyRepository.findOne(monkeyStrategy.getId())).thenReturn(null);

		MonkeyStrategy updatedMonkeyStrategy = monkeyStrategyService.update(monkeyStrategy);
		assertNull("MonkeyStrategy doesn't exist so not able to update an MonkeyStrategy ", updatedMonkeyStrategy);
	}

	/**
	 * This Test method validates the error condition on insertion of Monkey
	 * Strategy Object with same existing Monkey Strategy name.
	 * 
	 */
	@Test
	public void testDuplicateMonkeyStrategy() {

		List<MonkeyStrategy> monkeyStrategyList = new ArrayList<>();
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		monkeyStrategyList.add(monkeyStrategy);
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategyList);

		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), monkeyStrategy.getMonkeyStrategyName(), TEAMNAME))
						.thenReturn(monkeyStrategyPage);

		MonkeyStrategy insertedMonkeyStrategy = monkeyStrategyService.insertForTeam(monkeyStrategy, "TEST");
		assertNull("Monkey Strategy with duplicate Name  is not allowed.", insertedMonkeyStrategy);
	}

	/**
	 * This Test method validates the functionality to get the Monkey Strategy
	 * by given Monkey Strategy Name, Version and Team Name .
	 * 
	 */
	@Test
	public void testFindByMonkeyStrategyNameAndTeamNameAndVersion() {
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		when(monkeyStrategyRepository.findByMonkeyStrategyNameAndTeamNameAndMonkeyStrategyVersion(MONKEYSTRATEGYNAME,
				TEAMNAME, monkeyStrategy.getMonkeyStrategyVersion())).thenReturn(monkeyStrategy);

		MonkeyStrategy getMonkeyStrategy = monkeyStrategyService.findByMonkeyStrategyNameAndTeamNameAndVersion(
				monkeyStrategy.getMonkeyStrategyName(), TEAMNAME, monkeyStrategy.getMonkeyStrategyVersion());

		assertEquals(MONKEYSTRATEGYNAME, getMonkeyStrategy.getMonkeyStrategyName());
		assertEquals(TEAMNAME, getMonkeyStrategy.getTeamName());
		assertEquals("N", getMonkeyStrategy.getDefaultFlag());
		assertEquals(UNIXSCRIPT, getMonkeyStrategy.getScriptTypeCategory());
		assertEquals(MonkeyType.CHAOS, getMonkeyStrategy.getMonkeyType());
	}

	/**
	 * This Test method validates the functionality to get the Default Monkey
	 * Strategy by given Monkey Strategy Name, Monkey Type and Team Name .
	 * 
	 */
	@Test
	public void testfindDefaultMonkeyStrategy() {
		List<MonkeyStrategy> monkeyStrategyList = new ArrayList<>();
		MonkeyStrategy monkeyStrategy = createMonkeyStrategy(MONKEYSTRATEGYNAME, TEAMNAME);
		monkeyStrategy.setDefaultFlag("Y");
		monkeyStrategyList.add(monkeyStrategy);
		Page<MonkeyStrategy> monkeyStrategyPage = new PageImpl<>(monkeyStrategyList);

		when(monkeyStrategyRepository.findByMonkeyTypeAndMonkeyStrategyNameAndDefaultFlagAndTeamName(
				monkeyStrategy.getMonkeyType().monkeyType(), MONKEYSTRATEGYNAME, "Y", TEAMNAME))
						.thenReturn(monkeyStrategyPage);

		MonkeyStrategy getMonkeyStrategy = monkeyStrategyService
				.findDefaultMonkeyStrategy(monkeyStrategy.getMonkeyStrategyName(), MonkeyType.CHAOS, TEAMNAME);

		assertEquals(MONKEYSTRATEGYNAME, getMonkeyStrategy.getMonkeyStrategyName());
		assertEquals(TEAMNAME, getMonkeyStrategy.getTeamName());
		assertEquals("Y", getMonkeyStrategy.getDefaultFlag());
	}

	/**
	 * This method returns a MonkeyStrategy Object.
	 * 
	 * @return
	 */
	private static MonkeyStrategy createMonkeyStrategy(String msName, String teamName) {
		MonkeyStrategy obj = new MonkeyStrategy();
		obj.setId("testMSID");
		obj.setMonkeyStrategyName(msName);
		obj.setTeamName(teamName);
		obj.setMonkeyStrategyScript("testmonkeyStrategyScript");
		obj.setMonkeyType(MonkeyType.CHAOS);
		obj.setDefaultFlag("N");
		obj.setMonkeyStrategyVersion("1");
		obj.setScriptTypeCategory(UNIXSCRIPT);
		return obj;
	}
}
