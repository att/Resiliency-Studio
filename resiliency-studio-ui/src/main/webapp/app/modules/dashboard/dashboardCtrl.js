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
		.controller(
				'dashboardCtrl',
				[
						'$scope',
						'$cookieStore',
						'$http',
						'$rootScope',
						'$location',
						'env',
						'resiliencyStudioService',
						function($scope, $cookieStore, $http, $rootScope,
								$location, env, resiliencyStudioService) {

							$rootScope.sideBar = "";
							// To get Monkey Strategies count individually
							var Strategies = {
								Chaos : [],
								Doctor : [],
								Janitor : [],
								Security : [],
								Latency : [],
								Confirmity : [],
								ChaosGorilla : []
							}

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							// To get total application count
							resiliencyStudioService
									.getApplicationCount()
									.then(
											function(applicationCount) {
												$scope.applicationCount = applicationCount;
											}, function(error) {
												$scope.applicationCount = 0;
											});

							// To get total scenarios count
							resiliencyStudioService.getScenarioCount().then(
									function(scenarioCount) {
										$scope.scenarioCount = scenarioCount;
									}, function(error) {
										$scope.scenarioCount = 0;
									});

							// To get total execution events count
							resiliencyStudioService
									.getEventsCount()
									.then(
											function(eventsCount) {
												$scope.scenarioRunYTDCount = eventsCount;
											}, function(error) {
												$scope.scenarioRunYTDCount = 0;
											});

							// To get each monkey strategy count individually
							resiliencyStudioService
									.getMonkeyStrategiesCount()
									.then(
											function(data) {
												$scope.strategiesCount = data.length;
												for (var i = 0; i < data.length; i++) {
													if (data[i].monkeyType
															.includes('Chaos Monkey'))
														Strategies.Chaos
																.push(data[i]);
													else if (data[i].monkeyType
															.includes('Doctor'))
														Strategies.Doctor
																.push(data[i]);
													else if (data[i].monkeyType
															.includes('Janitor'))
														Strategies.Janitor
																.push(data[i]);
													else if (data[i].monkeyType
															.includes('Security'))
														Strategies.Security
																.push(data[i]);
													else if (data[i].monkeyType
															.includes('Latency'))
														Strategies.Latency
																.push(data[i]);
													else if (data[i].monkeyType
															.includes('Confirmity'))
														Strategies.Confirmity
																.push(data[i]);
													else if (data[i].monkeyType
															.includes('Chaos Gorilla'))
														Strategies.ChaosGorilla
																.push(data[i]);
												}
												$scope.ChaosLength = Strategies.Chaos.length;
												$scope.DoctorLength = Strategies.Doctor.length;
												$scope.JanitorLength = Strategies.Janitor.length;
												$scope.SecurityLength = Strategies.Security.length;
												$scope.LatencyLength = Strategies.Latency.length;
												$scope.ConfirmityLength = Strategies.Confirmity.length;
												$scope.ChaosGorillaLength = Strategies.ChaosGorilla.length;
											}, function(error) {
												$scope.ChaosLength = 0;
												$scope.DoctorLength = 0;
												$scope.LatencyLength = 0;
											});

						} ]);