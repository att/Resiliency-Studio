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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.att.ajsc.logging.AjscEelfManager;
import com.att.eelf.configuration.EELFLogger;
import com.att.tta.rs.data.es.repository.ApplicationRepository;
import com.att.tta.rs.model.Application;
import com.att.tta.rs.util.AppUtil;

/** 
 * Implementation class for {@link ApplicationService}
 * @author ak983d
 *
 */

@Service("applicationService")
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

	private static EELFLogger logger = AjscEelfManager.getInstance().getLogger(ApplicationServiceImpl.class);
	private ApplicationRepository applicationRepository;

	@Autowired
	public void setApplicationRepository(ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}

	private Application save(Application application) {
		return applicationRepository.save(application);
	}

	@Override
	public Application insertForTeam(Application application, String teamName) {
		/*
		 * Validation: If Application with same name is already exist then
		 * return without inserting application object
		 */
		if (this.isApplicationExistAndTeamName(application, teamName)) {
			logger.debug("Application already exists with name " + application.getApplicationName());
			return null;
		}
		return save(application);
	}

	@Override
	public Application update(Application application) {
		if (application != null && !"".equals(application.getId().trim())
				&& (applicationRepository.findOne(application.getId()) != null)) {
			return save(application);
		}
		return null;
	}

	@Override
	public void delete(Application application) {
		applicationRepository.delete(application);
	}

	@Override
	public void deleteAllApplications() {
		applicationRepository.deleteAll();
	}

	@Override
	public long count() {
		return applicationRepository.count();
	}

	@Override
	public Long countByTeamName(String teamName) {
		if (teamName.trim().equals(AppUtil.getSuperUser()))
			return applicationRepository.count();
		else
			return applicationRepository.countByTeamName(teamName);
	}

	@Override
	public boolean isApplicationExistAndTeamName(Application application, String teamName) {
		return findByApplicationNameAndTeamName(application.getApplicationName(), teamName) != null;
	}

	@Override
	public Application findOneForTeam(String id, String teamName) {
		if (AppUtil.getSuperUser().equals(teamName.trim()))
			return applicationRepository.findOne(id);
		else
			return applicationRepository.findOneForTeam(id, teamName);
	}

	@Override
	public Page<Application> findByApplicationNameAndTeamName(String name, String teamName, Pageable pageable) {
		if (AppUtil.getSuperUser().equals(teamName.trim()))
			return applicationRepository.findByApplicationName(name, pageable);
		else
			return applicationRepository.findByApplicationNameAndTeamName(name, teamName, pageable);
	}

	@Override
	public Application findByApplicationNameAndTeamName(String name, String teamName) {
		if (AppUtil.getSuperUser().equals(teamName.trim()))
			return applicationRepository.findByApplicationName(name, new PageRequest(0, 999)).iterator().next();

		Page<Application> appPage = null;
		Application app = applicationRepository.findByApplicationNameAndTeamName(name, teamName);

		if (app == null) {
			logger.debug("findByApplicationName returned null , going for page request");
			appPage = applicationRepository.findByApplicationNameAndTeamName(name, teamName, new PageRequest(0, 999));
		}

		if (appPage != null && appPage.hasContent())
			app = appPage.getContent().get(0);
		else
			logger.debug("There is no Applicaiton object found in ES for given Application name and Team name.");

		return app;
	}

	@Override
	public Page<Application> findByApplicationNameUsingCustomQueryAndTeamName(String name, String teamName,
			Pageable pageable) {
		return applicationRepository.findByApplicationUsingCustomQueryAndTeamName(name, teamName, pageable);
	}

	@Override
	public Application findByApplicationNameAndCategoryAndTeamName(String applicationName, String category,
			String teamName) {
		return applicationRepository.findByApplicationNameAndCategoryAndTeamName(applicationName, category, teamName);
	}

	@Override
	public Iterable<Application> findAllForTeam(String teamName) {
		if (AppUtil.getSuperUser().equals(teamName.trim()))
			return applicationRepository.findAll();
		else
			return applicationRepository.findByTeamName(teamName, new PageRequest(0, 9999));
	}

	@Override
	public Iterable<Application> findAllApplications() {
		return applicationRepository.findAll();
	}
}
