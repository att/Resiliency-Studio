
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
package com.att.tta.rs.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import com.att.tta.rs.data.es.repository.FailurePointRepository;
import com.att.tta.rs.model.FailurePoint;
import com.att.tta.rs.model.FailurePointAdapter;
import com.att.tta.rs.model.MonkeyType;
import com.att.tta.rs.service.FailurePointServiceImpl;
import com.att.tta.rs.util.MessageWrapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { com.att.tta.rs.Application.class })
public class FailurePointRestControllerTest {

	@Mock
	private FailurePointRepository failurePointRepository;

	@InjectMocks
	FailurePointServiceImpl failurePointService;

	@InjectMocks
	FailurePointRestController failurePointController;

	@Autowired
	HttpServletRequest req;

	private static final String TEAMNAME = "TEST";
	private static final String FPNAME = "TestFP";

	@Before
	public void setupMock() {
		MockitoAnnotations.initMocks(this);
		failurePointController.failurePointService = failurePointService;
	}

	/**
	 * This Test method validates listAllFailurePoints method.
	 * 
	 */
	@Test
	public void testListAllFailurePoints() {

		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp);

		when(failurePointRepository.findAll()).thenReturn(fpList);

		@SuppressWarnings("unchecked")
		List<FailurePoint> failurePointList = (List<FailurePoint>) failurePointController.listAllFailurePoints()
				.getBody();

		assertEquals(1, failurePointList.size());
		assertEquals(MonkeyType.CHAOS, failurePointList.get(0).getMonkeyType());
		assertEquals(FPNAME, failurePointList.get(0).getName());
	}

	/**
	 * This Test method validates listAllMonkeyStrategies method when no
	 * FailurePoint present..
	 * 
	 */
	@Test
	public void testListAllFailurePointsErrCond() {
		List<FailurePoint> fpList = new ArrayList<>();
		when(failurePointRepository.findAll()).thenReturn(fpList);

		MessageWrapper apiError = (MessageWrapper) failurePointController.listAllFailurePoints().getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals(" No FailurePoint(s) found !!!", apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getFailurePoint method for given Failure Point
	 * ID.
	 * 
	 */
	@Test
	public void testGetFailurePoint() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		when(failurePointRepository.findOne(fp.getId())).thenReturn(fp);

		FailurePoint failurePoint = (FailurePoint) failurePointController.getFailurePoint(fp.getId()).getBody();

		assertEquals(MonkeyType.CHAOS, failurePoint.getMonkeyType());
		assertEquals(FPNAME, failurePoint.getName());
	}

	/**
	 * This Test method validates getFailurePoint method when no FailurePoint
	 * present for given FailurePoint ID..
	 * 
	 */
	@Test
	public void testGetFailurePointErrCond() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		when(failurePointRepository.findOne(fp.getId())).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) failurePointController.getFailurePoint(fp.getId()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("FailurePoint Not found for id " + fp.getId(), apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getFailurePointsByName method for given
	 * FailurePoint name.
	 * 
	 */
	@Test
	public void testGetFailurePointsByName() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		when(failurePointRepository.findByName(fp.getName())).thenReturn(fp);

		FailurePoint failurePoint = (FailurePoint) failurePointController.getFailurePointsByName(fp.getName())
				.getBody();

		assertEquals(MonkeyType.CHAOS, failurePoint.getMonkeyType());
		assertEquals(FPNAME, failurePoint.getName());
	}

	/**
	 * This Test method validates getFailurePointsByName method when no
	 * FailurePoint present for given FailurePoint name.
	 * 
	 */
	@Test
	public void testGetFailurePointsByNameErrCond() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		when(failurePointRepository.findByName(fp.getName())).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) failurePointController.getFailurePointsByName(fp.getName())
				.getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals(" FailurePoint Not found for name " + fp.getName(), apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getFailurePointByCategory method for given
	 * FailurePoint Category.
	 * 
	 */
	@Test
	public void testGetFailurePointByCategory() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp);

		when(failurePointRepository.findByCategory(fp.getCategory())).thenReturn(fpList);

		@SuppressWarnings("unchecked")
		List<FailurePoint> failurePointList = (List<FailurePoint>) failurePointController
				.getFailurePointByCategory(fp.getCategory()).getBody();

		assertEquals(1, failurePointList.size());
		assertEquals(MonkeyType.CHAOS, failurePointList.get(0).getMonkeyType());
		assertEquals(FPNAME, failurePointList.get(0).getName());
		assertEquals("fpCategory", failurePointList.get(0).getCategory());
	}

	/**
	 * This Test method validates getFailurePointByCategory method when no
	 * FailurePoint present for given FailurePoint Category..
	 * 
	 */
	@Test
	public void testGetFailurePointByCategoryErrCond() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		when(failurePointRepository.findByCategory(fp.getCategory())).thenReturn(fpList);

		MessageWrapper apiError = (MessageWrapper) failurePointController.getFailurePointByCategory(fp.getCategory())
				.getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("failurePoints not found with category " + fp.getCategory(), apiError.getStatusMessage());
	}

	/**
	 * This Test method validates getFailurePointByRole method for given
	 * FailurePoint Role.
	 * 
	 */
	@Test
	public void testGetFailurePointByRole() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp);

		when(failurePointRepository.findByRole(fp.getRole())).thenReturn(fpList);

		@SuppressWarnings("unchecked")
		List<FailurePoint> failurePointList = (List<FailurePoint>) failurePointController
				.getFailurePointByRole(fp.getRole()).getBody();

		assertEquals(1, failurePointList.size());
		assertEquals(MonkeyType.CHAOS, failurePointList.get(0).getMonkeyType());
		assertEquals(FPNAME, failurePointList.get(0).getName());
		assertEquals("role", failurePointList.get(0).getRole());
	}

	/**
	 * This Test method validates getFailurePointByRole method when no
	 * FailurePoint present for given FailurePoint Role..
	 * 
	 */
	@Test
	public void testGetFailurePointByRoleErrCond() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		when(failurePointRepository.findByRole(fp.getRole())).thenReturn(fpList);

		MessageWrapper apiError = (MessageWrapper) failurePointController.getFailurePointByRole(fp.getRole()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("failurePoints not found for  role " + fp.getRole(), apiError.getStatusMessage());
	}

	/**
	 * This test method validates addFailurePoint functionality when valid
	 * FailurePoint Object is passed.
	 * 
	 */
	@Test
	public void testAddFailurePoint() {

		UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);

		when(failurePointRepository.findByName(fp.getName())).thenReturn(null);
		when(failurePointRepository.save(fp)).thenReturn(fp);

		ResponseEntity<Object> response = failurePointController.addFailurePoint(fp, ucBuilder);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	/**
	 * This test method validates Error Condition of addFailurePoint
	 * functionality when invalid FailurePoint Object is passed.
	 * 
	 */
	@Test
	public void testAddFailurePointErrCndtn() {

		UriComponentsBuilder ucBuilder = UriComponentsBuilder.newInstance();
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		fp.setName("");

		MessageWrapper apiError = (MessageWrapper) failurePointController.addFailurePoint(fp, ucBuilder).getBody();
		assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
		assertEquals("FailurePoint name cannot be empty", apiError.getStatusMessage());

		fp.setName("fpName");
		when(failurePointRepository.findByName(fp.getName())).thenReturn(fp);
		apiError = (MessageWrapper) failurePointController.addFailurePoint(fp, ucBuilder).getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("FailurePoint already exist with name " + fp.getName(), apiError.getStatusMessage());
	}

	/**
	 * This test method validates BulkaddFailurePoint functionality when valid
	 * FailurePoint Object is passed.
	 * 
	 */
	@Test
	public void testBulkAddFailurePoint() {
		FailurePoint fp1 = createFailurePoint(FPNAME, TEAMNAME);
		FailurePoint fp2 = createFailurePoint("fpName2", TEAMNAME);
		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp1);
		fpList.add(fp2);

		FailurePointAdapter fpAdapter = new FailurePointAdapter();
		fpAdapter.setFailurePoints(fpList);

		when(failurePointRepository.findByName(fp1.getName())).thenReturn(null);
		when(failurePointRepository.save(fp1)).thenReturn(fp1);
		when(failurePointRepository.findByName(fp2.getName())).thenReturn(null);
		when(failurePointRepository.save(fp2)).thenReturn(fp2);

		ResponseEntity<Object> response = failurePointController.bulkaddFailurePoint(fpAdapter);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	/**
	 * This test method validates Error Condition of bulkaddFailurePoint
	 * functionality when invalid FailurePoint Object is passed.
	 * 
	 */
	@Test
	public void testBulkAddFailurePointErrCndtn() {

		FailurePointAdapter fpAdapter = null;
		MessageWrapper apiError = (MessageWrapper) failurePointController.bulkaddFailurePoint(fpAdapter).getBody();
		assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
		assertEquals("NULL request received to add failurePoint", apiError.getStatusMessage());

		FailurePoint fp1 = createFailurePoint(FPNAME, TEAMNAME);
		FailurePoint fp2 = createFailurePoint("fpName2", TEAMNAME);
		fp1.setName("");

		List<FailurePoint> fpList = new ArrayList<>();
		fpList.add(fp1);
		fpList.add(fp2);

		fpAdapter = new FailurePointAdapter();
		fpAdapter.setFailurePoints(fpList);

		apiError = (MessageWrapper) failurePointController.bulkaddFailurePoint(fpAdapter).getBody();
		assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
		assertEquals("FailurePoint name cannot be empty", apiError.getStatusMessage());

		fp1.setName("fpName");
		when(failurePointRepository.findByName(fp1.getName())).thenReturn(fp1);
		apiError = (MessageWrapper) failurePointController.bulkaddFailurePoint(fpAdapter).getBody();
		assertEquals(HttpStatus.CONFLICT, apiError.getStatus());
		assertEquals("FailurePoint with name " + fp1.getName() + " already exist", apiError.getStatusMessage());
	}

	/**
	 * This test method validates updateFailurePoint functionality when valid
	 * FailurePoint Object is passed.
	 * 
	 */
	@Test
	public void testUpdateFailurePoint() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);

		when(failurePointRepository.findOne(fp.getId())).thenReturn(fp);
		when(failurePointRepository.save(fp)).thenReturn(fp);

		ResponseEntity<Object> response = failurePointController.updateFailurePoint(fp.getId(), fp);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	/**
	 * This test method validates Error Condition of updateFailurePoint
	 * functionality when invalid FailurePoint Object is passed.
	 * 
	 */
	@Test
	public void testUpdateFailurePointErrCndtn() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		when(failurePointRepository.findOne(fp.getId())).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) failurePointController.updateFailurePoint(fp.getId(), fp).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("FailurePoint with id " + fp.getId() + " not found", apiError.getStatusMessage());
	}

	/**
	 * This test method validates deleteFailurePoint functionality when valid
	 * FailurePoint Object is passed.
	 * 
	 */
	@Test
	public void testDeleteFailurePoint() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		when(failurePointRepository.findOne(fp.getId())).thenReturn(fp);
		ResponseEntity<Object> response = failurePointController.deleteFailurePoint(fp.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(failurePointRepository, times(1)).delete(fp);
	}

	/**
	 * This test method validates Error Condition of deleteFailurePoint
	 * functionality when invalid FailurePoint Object is passed.
	 * 
	 */
	@Test
	public void testDeleteFailurePointErrCndtn() {
		FailurePoint fp = createFailurePoint(FPNAME, TEAMNAME);
		when(failurePointRepository.findOne(fp.getId())).thenReturn(null);

		MessageWrapper apiError = (MessageWrapper) failurePointController.deleteFailurePoint(fp.getId()).getBody();
		assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
		assertEquals("Unable to delete. FailurePoint not found with id " + fp.getId(), apiError.getStatusMessage());
	}

	/**
	 * This test method validates deleteAllFailurePoints functionality.
	 * 
	 */
	@Test
	public void testDeleteAllFailurePoint() {

		ResponseEntity<Object> response = failurePointController.deleteAllFailurePoints();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(failurePointRepository, times(1)).deleteAll();
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
