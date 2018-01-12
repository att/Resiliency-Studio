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

app
		.factory(
				'resiliencyStudioService',
				[
						'resiliencyStudioProvider',
						'$http',
						'$q',
						function(resiliencyStudioProvider, $http, $q) {

							return {
								getAuthentication : function(data) {
									return resiliencyStudioProvider
											.getAuthentication(data);
								},
								getAuthorization : function(user) {
									return resiliencyStudioProvider
											.getAuthorization(user);
								},
								getDefaultTeam : function() {
									return resiliencyStudioProvider
											.getDefaultTeam();
								},
								changeTeam : function(newTeam) {
									return resiliencyStudioProvider
											.changeTeam(newTeam);
								},
								getApplicationCount : function() {
									return resiliencyStudioProvider
											.getApplicationCount();
								},
								getScenarioCount : function() {
									return resiliencyStudioProvider
											.getScenarioCount();
								},
								getEventsCount : function() {
									return resiliencyStudioProvider
											.getEventsCount();
								},
								getMonkeyStrategiesCount : function() {
									return resiliencyStudioProvider
											.getMonkeyStrategiesCount();
								},
								getFailures : function() {
									return resiliencyStudioProvider
											.getFailures();
								},
								getMonkeyTypes : function() {
									return resiliencyStudioProvider
											.getMonkeyType();
								},
								addMonkeyStrategy : function(data) {
									return resiliencyStudioProvider
											.addMonkeyStrategy(data);
								},
								searchDefaultMonkeyStrategy : function(
										strategyName, monkeyType) {
									return resiliencyStudioProvider
											.searchDefaultMonkeyStrategy(
													strategyName, monkeyType);
								},
								updateMonkeyStrategy : function(id, data) {
									return resiliencyStudioProvider
											.updateMonkeyStrategy(id, data);
								},
								getMonkeyStrategies : function() {
									return resiliencyStudioProvider
											.monkeyStrategies();
								},
								deleteStrategybyId : function(id) {
									return resiliencyStudioProvider
											.deleteStrategybyId(id);
								},
								addapplication : function(data) {
									return resiliencyStudioProvider
											.addapplication(data);
								},
								discoverfuelapplication : function(data) {
									return resiliencyStudioProvider
											.discoverfuelapplication(data);
								},
								updateapplicationbyid : function(id, data) {
									return resiliencyStudioProvider
											.updateapplicationbyid(id, data);
								},

								getapplicationbyid : function(id) {
									return resiliencyStudioProvider
											.getapplicationbyid(id);
								},
								deleteapplicationbyid : function(id) {
									return resiliencyStudioProvider
											.deleteapplicationbyid(id);
								},
								getApplicationDetails : function() {
									return resiliencyStudioProvider
											.getApplicationDetails();
								},
								addScenario : function(scenarioObjectData) {
									return resiliencyStudioProvider
											.addScenario(scenarioObjectData);
								},
								getScenariosByApplicationName : function(
										applicationName) {
									return resiliencyStudioProvider
											.getScenariosByApplicationName(applicationName);
								},
								getScenarioById : function(scenarioId) {
									return resiliencyStudioProvider
											.getScenarioById(scenarioId);
								},
								updateScenario : function(id, data) {
									return resiliencyStudioProvider
											.updateScenario(id, data);
								},
								searchFailurepointsByAppEnv : function(
										applicationName, environment) {
									return resiliencyStudioProvider
											.searchFailurepointsByAppEnv(
													applicationName,
													environment);
								},
								getFailureTenet : function() {
									return resiliencyStudioProvider
											.getFailureTenet();
								},
								deleteSelectedScenarios : function(
										selectedScenarios) {
									return resiliencyStudioProvider
											.deleteSelectedScenarios(selectedScenarios);
								},
								getScenarioRunYTD : function() {
									return resiliencyStudioProvider
											.getScenarioRunYTD();
								},

								addFailurePoint : function(data) {
									return resiliencyStudioProvider
											.addFailurePoint(data);
								},
								getfailurepoint : function() {
									return resiliencyStudioProvider
											.getfailurepoint();
								},
								failurepointbyid : function(FailurePointId) {
									return resiliencyStudioProvider
											.getfailurepointbyid(FailurePointId);
								},
								updateFailurePoint : function(id, data) {
									return resiliencyStudioProvider
											.updateFailurePoint(id, data);
								},
								deletefailurepointbyid : function(
										failurePointId) {
									return resiliencyStudioProvider
											.deletefailurepointbyid(failurePointId);
								},

								executeScenario : function(data) {
									return resiliencyStudioProvider
											.executeScenario(data);
								},
								getApplication : function(data) {
									return resiliencyStudioProvider
											.getApplication(data);
								},
								executeBulkScenario : function(data) {
									return resiliencyStudioProvider
											.executeBulkScenario(data);
								},
								getFailurePointsByAppEnv : function(
										applicationName, environment) {
									return resiliencyStudioProvider
											.getFailurePointsByAppEnv(
													applicationName,
													environment);
								},
								bulkAddScenario : function(bulkScenarioData) {
									return resiliencyStudioProvider
											.bulkAddScenario(bulkScenarioData);
								},
								discoverServerDetails : function(data) {
									return resiliencyStudioProvider
											.discoverServerDetails(data);
								},
								
							}
						} ]);