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

'use strict';

app
		.controller(
				'viewMonkeyStrategyCtrl',
				[
						'$rootScope',
						'$scope',
						'$parse',
						'$location',
						'$http',
						'$compile',
						'$sce',
						'resiliencyStudioService',
						'$timeout',
						'$routeParams',
						'$route',
						function($rootScope, $scope, $parse, $location, $http,
								$compile, $sce, resiliencyStudioService,
								$timeout, $routeParams, $route) {
							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							// Initializing Strategies object to store each
							// strategy length
							var Strategies = {
								Chaos : [],
								Doctor : [],
								Janitor : [],
								Security : [],
								Latency : [],
								Confirmity : [],
								ChaosGorilla : [],
								Monkey : []
							}
							$rootScope.sideBar = "toggled";
							$scope.UserPrivilege = $rootScope.UserPrivilege;
							$scope.janitorMonkey = true;
							$scope.securityMonkey = true;
							$scope.confirmityMonkey = true;
							$scope.chaosGorillaMonkey = true;
							$scope.monkey = true;

							// To show/hide columns in table by checking.
							$scope.osTypeToggle = true;
							$scope.flavorToggle = true;
							$scope.failureSubToggle = true;
							$scope.failureCategoryToggle = true;
							$scope.versionToggle = true;
							$scope.defaultToggle = true;
							$scope.genericToggle = true;
							$scope.scriptTypeToggle = true;
							$scope.createdBy = true;
							$scope.createDate = true;
							$scope.lastModifyBy = true;
							$scope.lastModifyDate = true;

							// to identify exact tab from dashboard
							$scope.currentChaosPage = 1;
							$scope.currentDoctorPage = 1;
							$scope.currentJanitorPage = 1;
							$scope.currentLatencyPage = 1;
							$scope.currentSecurityPage = 1;
							$scope.currentConfirmityPage = 1;
							$scope.currentChaosGorillaPage = 1;
							$scope.currentMonkeyPage = 1;

							// To initiate each monkey length
							$scope.ChaosLength = 0
							$scope.DoctorLength = 0
							$scope.JanitorLength = 0
							$scope.SecurityLength = 0
							$scope.LatencyLength = 0
							$scope.ConfirmityLength = 0
							$scope.ChaosGorillaLength = 0
							$scope.MonkeyLength = 0;

							// To display Monkey Strategies count on View Monkey
							// Strategies
							$scope.loadMonkeyStrategies = function() {
										resiliencyStudioService
												.getMonkeyStrategies()
												.then(
														function(data_Monkeys) {
															for (var i = 0; i < data_Monkeys.length; i++) {
																if (data_Monkeys[i].monkeyType
																		.includes('Chaos Monkey'))
																	Strategies.Chaos
																			.push(data_Monkeys[i]);
																else if (data_Monkeys[i].monkeyType
																		.includes('Doctor'))
																	Strategies.Doctor
																			.push(data_Monkeys[i]);
																else if (data_Monkeys[i].monkeyType
																		.includes('Janitor'))
																	Strategies.Janitor
																			.push(data_Monkeys[i]);
																else if (data_Monkeys[i].monkeyType
																		.includes('Security'))
																	Strategies.Security
																			.push(data_Monkeys[i]);
																else if (data_Monkeys[i].monkeyType
																		.includes('Latency'))
																	Strategies.Latency
																			.push(data_Monkeys[i]);
																else if (data_Monkeys[i].monkeyType
																		.includes('Confirmity'))
																	Strategies.Confirmity
																			.push(data_Monkeys[i]);
																else if (data_Monkeys[i].monkeyType
																		.includes('Chaos Gorilla'))
																	Strategies.ChaosGorilla
																			.push(data_Monkeys[i]);
																else if (data_Monkeys[i].monkeyType
																		.includes('10 - 18 Monkey'))
																	Strategies.Monkey
																			.push(data_Monkeys[i]);
															}
															$scope.Strategies = Strategies;
															$scope.ChaosLength = Strategies.Chaos.length;
															$scope.DoctorLength = Strategies.Doctor.length;
															$scope.JanitorLength = Strategies.Janitor.length;
															$scope.SecurityLength = Strategies.Security.length;
															$scope.LatencyLength = Strategies.Latency.length;
															$scope.ConfirmityLength = Strategies.Confirmity.length;
															$scope.ChaosGorillaLength = Strategies.ChaosGorilla.length;
															$scope.MonkeyLength = Strategies.Monkey.length;
														}), (function(error) {

											console.log(error);
										});
							}
							$scope.loadMonkeyStrategies();// Initially loads
															// each monkey
															// strategy count

							// To display exact tab when clicked from dashboard
							var tabId = $routeParams.tabId;
							if (tabId != undefined) {
								if (tabId == 1)
									$('.nav-tabs a[href="/#frame_doctor"]')
											.tab('show');
								else if (tabId ==2)
									$('.nav-tabs a[href="/#frame_latency"]')
											.tab('show');
							}

							// Delete a Monkey Strategy Starts
							$scope.deleteData = {};
							$scope.deleteMonkeyStrategy = function() {
								if ($scope.deleteData.OneStrategy != null) {
									$scope.deleteStrategyMsg = "Are you sure want to delete the ["
											+ $scope.deleteData.OneStrategy.monkeyStrategyName
											+ "] strategy ?";
									deletePopup();
								}
							};

							// service call for delete a Monkey Strategy using
							// ID
							$scope.gotoDeleteMonkeyStrategy = function() {
								var deleteID = $scope.deleteData.OneStrategy.id;
								resiliencyStudioService
										.deleteStrategybyId(deleteID)
										.then(
												function(success) {
													$('.modal-backdrop')
															.fadeOut();
													$location
															.path('/viewMonkeyStrategies');
													$route.reload();

												},
												function(error) {
													$scope.errorMessage = "Unable to delete the Strategy";
												});

								return "";
							}; // Delete Strategy Ends

							// View Script can be viewed using popup
							$scope.ViewScript = function(monkeyStrategyScript) {
								$scope.monkeyScript = monkeyStrategyScript;
								viewScriptPopup();
								return $scope.monkeyScript;
							}

							// Redirecting to Edit Monkey Strategy
							$scope.Edit = function(monkey) {
								if (monkey.id != null) {
									sessionStorage.setItem("info", JSON
											.stringify(monkey));
									$location.path('/editMonkeyStrategy/?info');
								}
							}

							// Redirecting to Clone Monkey Strategy
							$scope.cloneMonkey = function(monkey) {
								if (monkey.id != null) {
									sessionStorage.setItem("cloneMonkey", JSON
											.stringify(monkey));
									$location
											.path('/cloneMonkeyStrategy/?cloneMonkey');
								}
							}

							// Displays the script content in a pop when clicks
							// viewscript link
							function viewScriptPopup() {
								$("#myModal").on(
										"show",
										function() {
											$("#myModal a.btn").on(
													"click",
													function(e) {
														$("#myModal").modal(
																'hide');
													});
										});
								$("#myModal").on("hide", function() {
									$("#myModal a.btn").off("click");
								});

								$("#myModal").on("hidden", function() {
									$("#myModal").remove();
								});

								$("#myModal").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});

							}

							function deletePopup() {
								$("#deleteModal").on(
										"show",
										function() {
											$("#deleteModal a.btn").on(
													"click",
													function(e) {
														$("#deleteModal")
																.modal('hide');

													});
										});
								$("#deleteModal").on("hide", function() {
									$("#deleteModal a.btn").off("click");
								});

								$("#deleteModal").on("hidden", function() {
									$("#deleteModal").remove();
								});

								$("#deleteModal").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});

							}

						} ]);