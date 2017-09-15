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
				'resiliencyStudioProvider',
				[
						'constantService',
						'$http',
						'$q',
						'$filter',
						'$location',
						'env',
						function(constantService, $http, $q, $filter,
								$location, env) {

							var _env = {
								URL : env.get('env_url')

							}
							return {
								getAuthentication : function(data) {
									var deferred = $q.defer();
									var url = env.get('sec_url')
											+ constantService.constants.getapisession;
									;
									$http.post(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;

								},
								getAuthorization : function(user) {
									var deferred = $q.defer();
									var url = env.get('sec_url')
											+ constantService.constants.getAuthorization
											+ user;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;

								},
								getDefaultTeam : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getDefaultTeam;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								getFailures : function() {
									var deferred = $q.defer();
									var url = constantService.constants.getFailures;

									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);
									});
									return deferred.promise;
								},
								getMonkeyType : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getMonkeyType;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								addMonkeyStrategy : function(data) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.addMonkeyStrategy;
									$http.post(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								searchDefaultMonkeyStrategy : function(
										strategyName, monkeyType) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.searchDefaultMonkeyStrategy
											+ strategyName + '/' + monkeyType
											+ '/';
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								updateMonkeyStrategy : function(id, data) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.updateMonkeyStrategy
											+ id;
									$http.put(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg, code) {
										deferred.reject(msg);
									});
									return deferred.promise;
								},
								monkeyStrategies : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getMonkeyStrategies;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								deleteStrategybyId : function(id) {
									var deferred = $q.defer();
									$http(
											{
												method : 'DELETE',
												url : _env.URL
														+ constantService.constants.deleteStrategybyId
														+ id,
												headers : {
													'Content-type' : 'application/json;charset=utf-8'
												}
											}).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								getApplicationCount : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getApplicationCount;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								getScenarioCount : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getScenarioCount;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								getEventsCount : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getEventsCount;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								getMonkeyStrategiesCount : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getMonkeyStrategiesCount;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								changeTeam : function(newTeam) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.changeTeam
											+ newTeam;
									$http.put(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;

								},
								addapplication : function(data) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.addapplication;
									$http.post(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg, code) {
										deferred.reject(msg);
									});
									return deferred.promise;

								},
								getApplicationDetails : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getApplicationList;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								addScenario : function(scenarioObjectData) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.addScenario;
									$http.post(url, scenarioObjectData)
											.success(function(success) {
												deferred.resolve(success);
											}).error(function(msg, code) {
												deferred.reject(msg);
											});
									return deferred.promise;
								},
								getScenariosByApplicationName : function(
										applicationName) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getScenariosByApplicationName
											+ applicationName;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								getScenarioById : function(scenarioId) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getScenarioById
											+ scenarioId;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								updateScenario : function(scenarioId, data) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.updateScenario
											+ scenarioId;
									$http.put(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								searchFailurepointsByAppEnv : function(
										applicationName, environment) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.searchFailurepointsByAppEnv
											+ applicationName + '/'
											+ environment + '/';
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								getFailureTenet : function() {
									var deferred = $q.defer();
									var url = constantService.constants.getFailureTenet;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								deleteSelectedScenarios : function(
										selectedScenarios) {

									var deferred = $q.defer();

									$http(
											{
												method : 'DELETE',
												url : _env.URL
														+ constantService.constants.deleteSelectedScenarios,
												data : selectedScenarios,
												headers : {
													'Content-type' : 'application/json;charset=utf-8'
												}
											}).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								updateapplicationbyid : function(id, data) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.updateapplicationbyid
											+ id;
									$http.put(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg, code) {
										deferred.reject(msg);
									});
									return deferred.promise;
								},
								getapplicationbyid : function(id) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.apllicationEnvInfo
											+ id;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								deleteapplicationbyid : function(id) {
									var deferred = $q.defer();

									$http(
											{
												method : 'DELETE',
												url : _env.URL
														+ constantService.constants.deleteapplicationbyid
														+ id,
												headers : {
													'Content-type' : 'application/json;charset=utf-8'
												}
											}).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								getScenarioRunYTD : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.scenarioRunYTD;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},

								addFailurePoint : function(data) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.addFailurePoint;
									$http.post(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								getfailurepoint : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getFailurePoint;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								getfailurepointbyid : function(FailurePointId) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getFailurePoint
											+ FailurePointId;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								updateFailurePoint : function(id, data) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.updateFailurePoint
											+ id;
									$http.post(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								deletefailurepointbyid : function(
										failurePointId) {
									var deferred = $q.defer();
									$http(
											{
												method : 'DELETE',
												url : _env.URL
														+ constantService.constants.deleteFailurePointbyid
														+ failurePointId,
												headers : {
													'Content-type' : 'application/json;charset=utf-8'
												}
											}).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},

								getApplication : function() {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getApplicationList;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg) {
										deferred.reject(msg)
									});
									return deferred.promise;
								},
								executeScenario : function(data) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.executeScenario;
									$http.post(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								executeBulkScenario : function(data) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.executeBulkScenario;
									$http.post(url, data).success(
											function(data) {
												deferred.resolve(data);
											}).error(function(msg, code) {
										deferred.reject(msg);

									});
									return deferred.promise;
								},
								getFailurePointsByAppEnv : function(
										applicationName, environment) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.getFailurePointsByAppEnv
											+ applicationName + '/'
											+ environment;
									$http.get(url).success(function(data) {
										deferred.resolve(data);
									}).error(function(msg, code) {
										deferred.reject(msg);
									});
									return deferred.promise;
								},
								bulkAddScenario : function(bulkScenarioData) {
									var deferred = $q.defer();
									var url = _env.URL
											+ constantService.constants.bulkAddScenario;
									$http.post(url, bulkScenarioData).success(
											function(success) {
												deferred.resolve(success);
											}).error(function(msg, code) {
										deferred.reject(msg);
									});
									return deferred.promise;
								}

							}

						} ]);