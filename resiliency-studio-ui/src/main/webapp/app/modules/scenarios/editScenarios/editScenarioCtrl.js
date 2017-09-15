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
				'editScenarioCtrl',
				[
						'$rootScope',
						'$scope',
						'$filter',
						'$http',
						'$location',
						'$timeout',
						'$routeParams',
						'$parse',
						'$compile',
						'$sce',
						'dialogs',
						'$modal',
						'$window',
						'resiliencyStudioService',
						function($rootScope, $scope, $filter, $http, $location,
								$timeout, $routeParams, $parse, $compile, $sce,
								dialogs, $modal, $window,
								resiliencyStudioService) {
							var scenarioID = $routeParams.id;
							$rootScope.sideBar = "toggled";

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							// To hide inline messages when close the message
							$timeout(function() {
								$("[data-hide]")
										.on("click",
												function() {
													$(this).closest("." + $(this).attr("data-hide"))
															.hide();
												});
							});
							// Get Scenario by id to auto-populate in
							// edit_scenario page
							resiliencyStudioService
									.getScenarioById(scenarioID)
									.then(
											function(data) {
												$scope.scenario = data;
												$scope.currStrategy = data.monkeyStrategy;
												$scope.appfields = angular
														.copy(data);
												$scope.selectedMonkeyType();
											},
											function(error) {
												console.log("scenariobyid gets failed.");
											});

							// To populate failure tenet drop down values
							resiliencyStudioService
									.getFailureTenet()
									.then(
											function(data) {
												$scope.scenarios = data;
											},
											function(error) {
												console.log("Some error occured while fetching failure tenets")
											})

							// To populate Monkey Type in drop done box
							resiliencyStudioService.getMonkeyTypes().then(
									function(data) {
										$scope.monkeys = data;
									},
									function(error) {
										dialogs.error("Error",
												"monkey retrieved error.",
												error);
									});

							// Monkey Type end

							// Monkey Strategy selected based on Monkey type
							// selection.
							var selectedMonkeyType = "";
							$scope.selectedMonkeyType = function() {
								selectedMonkeyType = $scope.appfields.monkeyType;

								var Strategies = {
									monkeyStrageySelected : [],
									monkeyStrategyId : []
								}

										resiliencyStudioService
												.getMonkeyStrategies()
												.then(function(data1) {

															for (var i = 0; i < data1.length; i++) {
																if ((data1[i].monkeyType)
																		.toUpperCase() === selectedMonkeyType
																		.toUpperCase()) {
																	Strategies.monkeyStrageySelected
																			.push(data1[i].monkeyStrategyName);
																	Strategies.monkeyStrategyId
																			.push(data1[i].id);
																}
															}

															// To make both
															// strategy and
															// strategyId as
															// single object
															// array
															$scope.repeatData = Strategies.monkeyStrageySelected
																	.map(function(
																			value,
																			index) {
																		return {
																			data : value,
																			value : Strategies.monkeyStrategyId[index]
																		}
																	});

															if ($scope.appfields.monkeyStrategyId === undefined
																	|| $scope.appfields.monkeyStrategyId === null) {
																var tempData = $filter(
																		'filter')
																		(
																				$scope.repeatData,
																				{
																					data : $scope.appfields.monkeyStrategy
																				});
																for (var tempVal = 0; tempVal < tempData.length; tempVal++) {
																	if (tempData[tempVal].data == $scope.currStrategy) {
																		$scope.appfields.monkeyStrategy = tempData[tempVal];
																		break;
																	}
																}
															} else {
																$scope.appfields.monkeyStrategy = $filter(
																		'filter')
																		(
																				$scope.repeatData,
																				{
																					value : $scope.appfields.monkeyStrategyId
																				})[0];
															}

														}),
										(function(error) {
											console.log("some error occure during get monkey strategy");
										});
							}

							// To store edited data into object
							var editData = {
								name : "",
								applicationName : "",
								environmentName : "",
								tierName : "",
								serverName : "",
								role : "",
								softwareComponentName : "",
								failureTenet : "",
								failureMode : "",
								userBehavior : "",
								systemBehavior : "",
								causeOfFailure : "",
								currentControls : "",
								detectionMechanism : "",
								recoveryMechanism : "",
								recommendations : "",
								mttd : "",
								mttr : "",
								failureScript : "",
								monkeyStrategy : "",
								version : "",
								processName : "",
								monkeyType : ""
							};

							// To reset the form
							$scope.scenarioReset = function() {
								$scope.appfields = {};
							}

							// To submit updated scenario
							$scope.submitScenario = function() {
								$scope.editedVersion = Math
										.round((Number($scope.appfields.version) + 0.1) * 1e12) / 1e12;
								editData.name = $scope.appfields.applicationName
										+ '.'
										+ $scope.appfields.environmentName
										+ '.'
										+ $scope.appfields.serverName
										+ '.'
										+ $scope.appfields.softwareComponentName
										+ '.'
										+ $scope.appfields.processName
										+ '.'
										+ $scope.appfields.failureTenet
										+ '.'
										+ $scope.appfields.failureMode
										+ '.' + $scope.editedVersion;
								editData.applicationName = $scope.appfields.applicationName;
								editData.environmentName = $scope.appfields.environmentName;
								editData.serverName = $scope.appfields.serverName;
								editData.role = $scope.appfields.role;
								editData.softwareComponentName = $scope.appfields.softwareComponentName;
								editData.processName = $scope.appfields.processName;
								editData.failureTenet = $scope.appfields.failureTenet;
								editData.failureMode = $scope.appfields.failureMode;
								editData.userBehavior = $scope.appfields.userBehavior;
								editData.systemBehavior = $scope.appfields.systemBehavior;
								editData.causeOfFailure = $scope.appfields.causeOfFailure;
								editData.currentControls = $scope.appfields.currentControls;
								editData.detectionMechanism = $scope.appfields.detectionMechanism;
								editData.recoveryMechanism = $scope.appfields.recoveryMechanism;
								editData.recommendations = $scope.appfields.recommendations;
								editData.mttd = $scope.appfields.mttd;
								editData.mttr = $scope.appfields.mttr;
								editData.failureScript = $scope.appfields.failureScript;
								editData.monkeyStrategy = $scope.appfields.monkeyStrategy.data;
								editData.monkeyType = $scope.appfields.monkeyType;
								editData.monkeyStrategyId = $scope.appfields.monkeyStrategy.value;
								editData.version = $scope.editedVersion;

								var id = $scope.appfields.id;

								var data = editData;
								$scope.updatedScenario = data.name;
								resiliencyStudioService
										.updateScenario(id, data)
										.then(
												function(data) {
													$('.alert').show();
													$scope.showerrorAlert = false;
													$scope.successAlert = true
													$('body, html')
															.animate(
																	{
																		scrollTop : $(
																				'body')
																				.offset().top
																	}, 'fast');
												},
												function(error) {

													console.log(error)
													$('.alert').show();
													$scope.successAlert = false;
													$scope.showerrorAlert = true;
													$scope.errorMessage = error.statusMessage;
													$('body, html')
															.animate(
																	{
																		scrollTop : $(
																				'body')
																				.offset().top
																	}, 'fast');
												});
							}

							// Go to View scenario to view updated scenarios
							$scope.getViewScenario = function() {
								$('body').removeClass('modal-open');
								$('.modal-backdrop').remove();
								$location.path("/viewScenario/"
										+ $scope.appfields.applicationName
										+ "/"
										+ $scope.appfields.environmentName);
							}

							$(document).on('click', '.closed', function() {
								$("#myModal").removeClass('in');

								$rootScope.$apply();
								$(".modal-backdrop").remove();
							});
						} ]);