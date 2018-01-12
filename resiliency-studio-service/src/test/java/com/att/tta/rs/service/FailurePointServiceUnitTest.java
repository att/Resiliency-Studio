
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

import com.att.tta.rs.data.es.repository.FailurePointRepository;
import com.att.tta.rs.model.FailurePoint;
import com.att.tta.rs.model.MonkeyType;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.att.tta.rs.Application.class)
public class FailurePointServiceUnitTest {

	@InjectMocks
	FailurePointServiceImpl failurePointService;

	@Mock
	private FailurePointRepository failurePointRepository;

	private static final String TEAMNAME = "TEST";
	private static final String FPNAME = "TestFP";

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * This Test method validates the Successful insertion Failure Point Object
	 * into/from Elastic Search.
	 * 
	 */
	@Test
	public void testCreateFailurePoint() {

		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);

		when(failurePointRepository.findByName(fp.getName())).thenReturn(null);
		when(failurePointRepository.save(fp)).thenReturn(fp);

		FailurePoint insertedFailurePoint = failurePointService.insert(fp);
		assertEquals(FPNAME, insertedFailurePoint.getName());
		assertEquals(MonkeyType.CHAOS, insertedFailurePoint.getMonkeyType());
	}

	/**
	 * This Test method validates the Successful Deletion of FailurePoint Object
	 * from Elastic Search.
	 * 
	 */
	@Test
	public void testDeleteFailurePoint() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		failurePointService.delete(fp);
		verify(failurePointRepository, times(1)).delete(fp);
	}

	/**
	 * This Test method validates the Successful Update of FailurePoint Object
	 * into Elastic Search.
	 * 
	 */
	@Test
	public void testUpdateFailurePoint() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);

		when(failurePointRepository.findOne(fp.getId())).thenReturn(fp);
		when(failurePointRepository.save(fp)).thenReturn(fp);

		FailurePoint updatedFailurePoint = failurePointService.update(fp);
		assertEquals(FPNAME, updatedFailurePoint.getName());
		assertEquals(MonkeyType.CHAOS, updatedFailurePoint.getMonkeyType());
	}

	/**
	 * This Test method validates the Error Condition while updating of
	 * non-exist FailurePoint Object into Elastic Search.
	 * 
	 */
	@Test
	public void testUpdateNonExistFailurePoint() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		when(failurePointRepository.findOne(fp.getId())).thenReturn(null);

		FailurePoint updatedFailurePoint = failurePointService.update(fp);
		assertNull("FailurePoint doesn't exist so not able to update an FailurePoint ", updatedFailurePoint);
	}

	/**
	 * This Test method validates the error condition on insertion of
	 * FailurePoint Object with same existing FailurePoint name.
	 * 
	 */
	@Test
	public void testDuplicateFailurePoint() {

		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);

		when(failurePointRepository.findByName(fp.getName())).thenReturn(fp);
		when(failurePointRepository.save(fp)).thenReturn(fp);

		FailurePoint insertedfp = failurePointService.insert(fp);
		assertNull("FailurePoint with duplicate Name  is not allowed.", insertedfp);
	}

	/**
	 * This Test method validates the FindOne method for given FailurePoint ID.
	 * 
	 */
	@Test
	public void testFindOne() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		when(failurePointRepository.findOne(fp.getId())).thenReturn(fp);
		FailurePoint getFP = failurePointService.findOne(fp.getId());
		assertEquals(FPNAME, getFP.getName());
		assertEquals(MonkeyType.CHAOS, getFP.getMonkeyType());
	}

	/**
	 * This Test method validates the FindAll method for given FailurePoint ID.
	 * 
	 */
	@Test
	public void testFindAll() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp);
		Page<FailurePoint> fpPage = new PageImpl<>(fpList);

		when(failurePointRepository.findAll()).thenReturn(fpPage);
		List<FailurePoint> getFPList = Lists.newArrayList(failurePointService.findAll());
		assertEquals(1, getFPList.size());
		assertEquals(FPNAME, getFPList.get(0).getName());
		assertEquals(MonkeyType.CHAOS, getFPList.get(0).getMonkeyType());
	}

	/**
	 * This Test method validates the findByCategory method for given
	 * FailurePoint Category.
	 * 
	 */
	@Test
	public void testFindByCategory() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp);

		when(failurePointRepository.findByCategory(fp.getCategory())).thenReturn(fpList);
		List<FailurePoint> getFPList = Lists.newArrayList(failurePointService.findByCategory(fp.getCategory()));

		assertEquals(1, getFPList.size());
		assertEquals(FPNAME, getFPList.get(0).getName());
		assertEquals(MonkeyType.CHAOS, getFPList.get(0).getMonkeyType());
	}

	/**
	 * This Test method validates the findByCategory method for given
	 * FailurePoint Role.
	 * 
	 */
	@Test
	public void testFindByRole() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp);

		when(failurePointRepository.findByRole(fp.getRole())).thenReturn(fpList);
		List<FailurePoint> getFPList = failurePointService.findByRole(fp.getRole());

		assertEquals(1, getFPList.size());
		assertEquals(FPNAME, getFPList.get(0).getName());
		assertEquals(MonkeyType.CHAOS, getFPList.get(0).getMonkeyType());
	}

	/**
	 * This Test method validates the findByComponent method for given
	 * FailurePoint Component Name.
	 * 
	 */
	@Test
	public void testFindByComponent() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp);

		when(failurePointRepository.findByComponent(fp.getComponent())).thenReturn(fpList);
		List<FailurePoint> getFPList = failurePointService.findByComponent(fp.getComponent());

		assertEquals(1, getFPList.size());
		assertEquals(FPNAME, getFPList.get(0).getName());
		assertEquals(MonkeyType.CHAOS, getFPList.get(0).getMonkeyType());
	}

	/**
	 * This Test method validates the findByProcessName method for given
	 * FailurePoint Process Name.
	 * 
	 */
	@Test
	public void testFindByProcessName() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp);

		when(failurePointRepository.findByProcessName(fp.getProcessName())).thenReturn(fpList);
		List<FailurePoint> getFPList = failurePointService.findByProcessName(fp.getProcessName());

		assertEquals(1, getFPList.size());
		assertEquals(FPNAME, getFPList.get(0).getName());
		assertEquals(MonkeyType.CHAOS, getFPList.get(0).getMonkeyType());
	}

	/**
	 * This Test method validates the count method.
	 * 
	 */
	@Test
	public void testCount() {
		when(failurePointRepository.count()).thenReturn((long) 2);
		long fpCount = failurePointService.count();
		assertEquals(2, fpCount);
	}

	/**
	 * This method returns a FailurePoint Object.
	 * 
	 * @return
	 */
	private static FailurePoint createFailurePoint(String fpName, String teamName) {
		FailurePoint obj = new FailurePoint();
		obj.setId("testFPID");
		obj.setName(fpName);
		obj.setCauseOfFailure(teamName);
		obj.setMonkeyStrategy("msName");
		obj.setMonkeyType(MonkeyType.CHAOS);
		obj.setCategory("fpCategory");
		obj.setRole("role");
		obj.setComponent("component");
		obj.setFailureMode("fpMode");
		obj.setFailureTenet("fptenet");
		obj.setProcessName("process");

		return obj;
	}
}
