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
				'executeScenarioCtrl',
				[
						'$interval',
						'$rootScope',
						'$scope',
						'$filter',
						'$http',
						'dialogs',
						'resiliencyStudioService',
						'env',
						'$timeout',
						function($interval, $rootScope, $scope, $filter, $http,
								dialogs, resiliencyStudioService, env, $timeout) {
							$rootScope.sideBar = "toggled"; // For sidebar(Left
															// ) Navigation

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							/*
							 * Initialize Multi select Drop down Filter data and
							 * bind with model
							 */
							$scope.scenariosFilterData = [];
							$scope.scenariosFilterModel = [];
							$scope.scenariosFilterSettings = {
								enableSearch : true,
								scrollable : true,
								closeOnBlur : true,
								showCheckAll : false,
								showUncheckAll : false,
								smartButtonMaxItems : 1,
								selectionLimit : 1,
								closeOnSelect : true,
								closeOnDeselect : true
							};
							$scope.scenariosFilterCustomTexts = {
								buttonDefaultText : 'Application Name'
							};
							$scope.applicationSelected = true;

							/* Initialize spinner loader */
							$scope.loader = {
								loading : false,
							};
							/* Initialize env service variable */
							var _env = {
								URL : env.get('env_url'),
								URL_agent : env.get('agent_url')
							}

							// To get Application name
							resiliencyStudioService
									.getApplicationDetails()
									.then(function(data) {
												$scope.scenario = data;
												angular.forEach(
																data,
																function(value,key) {
																	$scope.scenariosFilterData
																			.push({
																				'id' : value.applicationName,
																				'label' : value.applicationName
																			});

																});
											});

							/*
							 * get Called after selection change on multiple
							 * Drop down filter box
							 */
							$scope.applicationChange = function() {
								angular
										.forEach(
												$scope.scenario,
												function(value, key) {
													if (value.applicationName == $scope.scenariosFilterModel.id) {
														$scope.applicationSelected = false;
														$scope.environmentMap = [];
														for ( var obj in value.environmentMap) {
															if (value.environmentMap
																	.hasOwnProperty(obj)) {
																$scope.environmentMap
																		.push(value.environmentMap[obj]);
															}

														}
													}

												});
								$scope.appfields.environmentName = $scope.environmentMap[0];
							};

							$scope.exeScen = false;

							// To get Scenario for the selected application
							var envName = "";
							$scope.getScenariosList = function() {

								$scope.modelNames = [];
								var applicationName = $scope.scenariosFilterModel.id;
								$scope.appName = applicationName;
								envName = $scope.appfields.environmentName.name;
								$scope.exeScen = true;
								$scope.loader.loading = true;

								resiliencyStudioService
										.getScenariosByApplicationName(
												applicationName)
										.then(function(data) {

													// Scenario execution status
													// filter
													$http(
															{
																method : 'GET',
																url : _env.URL
																		+ '/resiliency-studio-service/api/events/latestevent/'
																		+ applicationName
																		+ '/'
															})
															.success(function(
																			latestevent) {
																		for (var i = 0; i < data.length; i++) {
																			for ( var key in latestevent) {
																				if (data[i].name == key) {
																					data[i]['status'] = latestevent[key].eventStatusType;
																					break;
																				}
																			}
																		}
																	})
															.error(function(error) {
																		console.log(error);
																	})

													$scope.data = data;

													$('#loader').hide();
													$('#userList').show();
													$scope.loader.loading = false;
													$scope.errorMessage = false;

												},
												function(error) {
													$('#loader').hide();
													$('#userList').hide();
													$scope.loader.loading = false;
													$scope.errorMessage = "Scenario is not available for selected Application: { "
															+ applicationName
															+ " } and environment: { "
															+ envName + " }";
												});

							}

							// Table row expansion
							$scope.showDetail = function(u) {
								if ($scope.active != u) {
									$scope.active = u;
								} else {
									$scope.active = null;
								}
							};

							// Filter Fucntionality
							$scope.filterTr = false;

							$scope.showFilter = function() {
								$scope.filterTr = true;
							}

							$scope.closeFilter = function() {
								delete $scope.search;
								$scope.filterTr = false;
								$scope.search = {};
							}
							// To clear filter values if it having null
							$scope.search = {};
							$scope.clearSCName = function() {
								if ($scope.search.softwareComponentName.length == 0) {
									delete $scope.search.softwareComponentName;
								}
							}
							$scope.clearProcessName = function() {
								if ($scope.search.processName.length == 0) {
									delete $scope.search.processName;
								}
							}
							$scope.clearFailureTenet = function() {
								if ($scope.search.failureTenet.length == 0) {
									delete $scope.search.failureTenet;
								}
							}
							$scope.clearmonkeyStrategy = function() {
								if ($scope.search.monkeyStrategy.length == 0) {
									delete $scope.search.monkeyStrategy;
								}
							}
							$scope.clearStatus = function() {
								if ($scope.search.status.length == 0) {
									delete $scope.search.status;
								}
							}

							$scope.appfields = {
								name : "",
								applicationName : "",
								serverName : "",
								environmentName : "",
								tier : "",
								softwareComponentName : "",
								failureTenet : "",
								failureMode : "",
								causeOfFailure : "",
								currentControls : "",
								detectionMechanism : "",
								recoveryMechanism : "",
								recommendations : "",
								mttd : "",
								mttr : "",
								monkeyType : "",
								monkeyStrategy : "",
								monkeyStrategyId : "",
								failureScript : "",
								version : "",
								processName : ""
							};

							$('.collapse').on('show.bs.collapse', function() {
								$('.collapse.in').collapse('hide');
							});

							$scope.$watch('data',
											function() {
												var no = 0;
												if ($scope.data != null
														&& $scope.data != undefined) {
													for (var i = 0; i < $scope.data.length; i++) {
														if ($scope.data[i].selected === true)
															no++;
													}
												}
												$scope.noSelectedItems = no;
											}, true);

							function executeScenarioPopup() {
								$("#myexecuteScenarioPopup")
										.on("show",
												function() { // wire up the
																// OK button to
																// dismiss the
																// modal when
																// shown
													$("#myexecuteScenarioPopup a.btn")
															.on("click",
																	function(e) {

																		$("#myexecuteScenarioPopup")
																				.modal('hide'); // dismiss
																									// the
																									// dialog
																	});
												});
								$("#myexecuteScenarioPopup").on(
										"hide",
										function() { // remove the event
														// listeners when the
														// dialog is dismissed
											$("#myexecuteScenarioPopup a.btn")
													.off("click");
										});

								$("#myexecuteScenarioPopup").on(
										"hidden",
										function() { // remove the actual
														// elements from the DOM
														// when fully hidden
											$("#myexecuteScenarioPopup")
													.remove();
										});

								$("#myexecuteScenarioPopup").modal({ // wire
																		// up
																		// the
																		// actual
																		// modal
																		// functionality
																		// and
																		// show
																		// the
																		// dialog
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								// ensure the modal is shown immediately
								});

							}

							// select checkboxes
							$scope.checkSelectedSecenarios = function(position,
									data) {

								$scope.showTable = false;
								var singleScenario = {}
								singleScenario.name = $scope.data.name;
								singleScenario.applicationName = $scope.data.applicationName;

								$scope.modelNames = [];

								$scope.selectedScenarios = {};

								// To provide single scenario execution to the
								// user role. It will not allow multiple select
								// scenarios

								angular
										.forEach(
												$scope.data,
												function(u, index) {

													// To select single scenario
													// if the role is USER
													if (position != index
															&& $scope.UserPrivilege == 'user')
														u.selected = false;

													if (u.selected) {

														u.eventSelected = true;
														angular
																.forEach(
																		$scope.appfields.environmentName.serverList,
																		function(
																				value,
																				key) {
																			$scope.server = value;
																			if (value.instanceName == u.serverName) {
																				$scope.monitor = value.monitor;
																				$scope.log = value.log;
																				if (value.monitor != null) {
																					angular
																							.forEach(
																									value.monitor,
																									function(
																											value,
																											key) {
																										u.hystrix = value.monitorApi;
																										$scope.hystrix = u.hystrix;

																									})

																				}

																				if (value.log != null) {
																					angular
																							.forEach(
																									value.log,
																									function(
																											value,
																											key) {
																										u.kibanaApp = value.logLocation;
																										$scope.kibanaApp = u.kibanaApp;
																									})
																				}

																			}
																		});
														$scope.showTable = true;

													}
													$scope.modelNames.push(u);

												});

							}

							$scope.runScenario = function() {
								$("html, body").animate({
									scrollTop : $(document).height()
								}, "slow");
								$scope.modelNames = [];
								$scope.showTable = false;
								var bulkExecuteList = [];
								var bulkExecute = $scope.data;
								if ($scope.noSelectedItems == 1) {

									$scope.appModelNames = [];

									$scope.selectedScenarios = {};
									var ScenariowithAppData = {
										'scenario' : '',
										'application' : ''
									}
									angular
											.forEach(
													$scope.data,
													function(u, index) {

														if (u.selected == 1) {
															$scope.appModelNames
																	.push(u);

															$scope.selectedScenarios[u.name];
															var singleScenario = {}
															singleScenario.name = u.name;
															singleScenario.environmentName = $scope.appfields.environmentName.name;
															singleScenario.applicationName = u.applicationName;
															singleScenario.tierName = u.tierName;
															singleScenario.serverName = u.serverName;
															singleScenario.softwareComponentName = u.softwareComponentName;
															singleScenario.failureTenet = u.failureTenet;
															singleScenario.failureTenet = u.failureTenet;
															singleScenario.failureMode = u.failureMode;
															singleScenario.causeOfFailure = u.causeOfFailure;
															singleScenario.currentControls = u.currentControls;
															singleScenario.detectionMechanism = u.detectionMechanism;
															singleScenario.recoveryMechanism = u.recoveryMechanism;
															singleScenario.recommendations = u.recommendations;
															singleScenario.mttd = u.mttd;
															singleScenario.mttr = u.mttr;
															singleScenario.failureScript = u.failureScript;
															singleScenario.monkeyType = u.monkeyType;
															singleScenario.monkeyStrategy = u.monkeyStrategy;
															singleScenario.monkeyStrategyId = u.monkeyStrategyId;
															singleScenario.version = u.version;
															singleScenario.processName = u.processName;
															singleScenario.role = u.role;
															singleScenario.userBehavior = u.userBehavior;
															singleScenario.systemBehavior = u.systemBehavior;

															$scope.addSingleScenario = singleScenario;

															ScenariowithAppData.scenario = singleScenario;

														}

													});
									// RSA's Scenario Execution with application
									// object
									$scope.executeWithRSA = function() {
										for (var i = 0; i < ScenariowithAppData.application.environmentMap[envName].serverList.length; i++) {
											if (ScenariowithAppData.application.environmentMap[envName].serverList[i].instanceName == ScenariowithAppData.scenario.serverName) {
												ScenariowithAppData.application.environmentMap[envName].serverList[i].password = $scope.rsa;
											}
										}
										$http(
												{
													method : 'POST',
													data : ScenariowithAppData,
													url : _env.URL
															+ '/resiliency-studio-service/api/executescenariowithapplication/'
												})
												.success(
														function(data) {

															$scope.rsa = null;

															var eventStatus = data.eventStatus
																	.toString()
																	.replace(
																			/\n/g,
																			"<br/>");
															document
																	.getElementById("eventSpan").innerHTML = eventStatus;
															var myDiv = document
																	.getElementById("eventDiv");
															myDiv.scrollTop = myDiv.scrollHeight;

															var agentURL = _env.URL_agent
																	+ "/resiliency-studio-agent/api/eventStatus/"
																	+ data.id;
															if (typeof (EventSource) !== "undefined") {
																var source = new EventSource(
																		agentURL);
																source.onmessage = function(
																		event) {

																	var eventData = event.data
																			.toString()
																			.replace(
																					/@#/g,
																					"<br/>");
																	document
																			.getElementById("eventSpan").innerHTML = eventStatus
																			+ "<br/>"
																			+ eventData;
																	var myDiv = document
																			.getElementById("eventDiv");
																	myDiv.scrollTop = myDiv.scrollHeight;
																};
																source.addEventListener('open',
																				function(e) {
																					console.log("connecton opened");
																				},
																				false);
																source.addEventListener('error',
																				function(e) {
																					source.close();
																					if (e.readyState == EventSource.CLOSED) {
																						console.log("connecton closed");
																					}
																				},
																				false);
															} else {
																console.log(" Your browser does not support server-sent events.- ");
																document.getElementById("sseDiv").innerHTML = "Your browser does not support server-sent events.";
															}
														})
												.error(
														function(error) {
															console.log("Scenario with application execution is failed with rsa: "
																			+ error)
															$scope.rsa = null;
														})
									}
									// getting application object data for RSA's
									// scenario executions

											resiliencyStudioService
													.getApplication()
													.then(
															function(data) {
																$scope.resultData = data;
																angular
																		.forEach(
																				$scope.resultData,
																				function(
																						app,
																						index) {
																					if (app.applicationName == $scope.appName) {
																						ScenariowithAppData.application = app;
																						for (var i = 0; i < app.environmentMap[envName].serverList.length; i++) {
																							if (app.environmentMap[envName].serverList[i].instanceName == ScenariowithAppData.scenario.serverName
																									&& app.environmentMap[envName].serverList[i].rsaLogin) {
																								loadRSAPopup();
																							} else if (app.environmentMap[envName].serverList[i].instanceName == ScenariowithAppData.scenario.serverName
																									&& !(app.environmentMap[envName].serverList[i].rsaLogin)) {
																								$scope.executeSingleScenario();
																							}
																						}
																					}
																				});
															}),
											(function(error) {
												console.log(error);
											});

									var singleData = $scope.addSingleScenario;

									// Single scenario execution without RSA
									$scope.executeSingleScenario = function() {
										resiliencyStudioService
												.executeScenario(singleData)
												.then(
														function(data) {

															$scope.showTable = true;

															var eventStatus = data.eventStatus
																	.toString()
																	.replace(
																			/\n/g,
																			"<br/>");
															$timeout(
																	function() {
																		document.getElementById("eventSpan").innerHTML = eventStatus;
																		var myDiv = document
																				.getElementById("eventDiv");
																		myDiv.scrollTop = myDiv.scrollHeight;
																	}, 400);

															var agentURL = _env.URL_agent
																	+ "/resiliency-studio-agent/api/eventStatus/"
																	+ data.id;

															if (typeof (EventSource) !== "undefined") {
																var source = new EventSource(agentURL);
																source.onmessage = function(event) {

																	var eventData = event.data
																			.toString()
																			.replace(
																					/@#/g,
																					"<br/>");
																	document
																			.getElementById("eventSpan").innerHTML = eventStatus
																			+ "<br/>"
																			+ eventData;
																	var myDiv = document
																			.getElementById("eventDiv");
																	myDiv.scrollTop = myDiv.scrollHeight;

																	$(function() {
																		var wtf = $('#eventSpan');
																		var height = wtf[0].scrollHeight;
																		wtf.scrollTop(height);
																	});
																};

																source.addEventListener(
																				'open',
																				function(e) {
																					// Connection
																					// was
																					// opened.

																				},
																				false);

																source.addEventListener(
																				'error',
																				function(e) {
																					source.close();
																					if (e.readyState == EventSource.CLOSED) {
																						console.log("connecton closed");
																					}
																				},
																				false);

															} else {
																console.log(" Your browser does not support server-sent events.- ");
																document.getElementById("sseDiv").innerHTML = "Your browser does not support server-sent events.";
															}

														},
														function(error) {
															$scope.loader.loading = false;
															dialogs
																	.error(
																			"Error",
																			"Single scenario execution failed",
																			"");
														});
									}

								} else {
									var count = 0;

									for (var bulkExeIndex = 0; bulkExeIndex < $scope.data.length; bulkExeIndex++) {
										if (bulkExecute[bulkExeIndex].selected == true) {

											var bulkExecuteObj = {};

											bulkExecuteObj.name = bulkExecute[bulkExeIndex].name;
											bulkExecuteObj.environmentName = $scope.appfields.environmentName.name;
											bulkExecuteObj.applicationName = bulkExecute[bulkExeIndex].applicationName;
											bulkExecuteObj.tierName = bulkExecute[bulkExeIndex].tierName;
											bulkExecuteObj.serverName = bulkExecute[bulkExeIndex].serverName;
											bulkExecuteObj.softwareComponentName = bulkExecute[bulkExeIndex].softwareComponentName;
											bulkExecuteObj.failureTenet = bulkExecute[bulkExeIndex].failureTenet;
											bulkExecuteObj.failureTenet = bulkExecute[bulkExeIndex].failureTenet;
											bulkExecuteObj.failureMode = bulkExecute[bulkExeIndex].failureMode;
											bulkExecuteObj.causeOfFailure = bulkExecute[bulkExeIndex].causeOfFailure;
											bulkExecuteObj.currentControls = bulkExecute[bulkExeIndex].currentControls;
											bulkExecuteObj.detectionMechanism = bulkExecute[bulkExeIndex].detectionMechanism;
											bulkExecuteObj.recoveryMechanism = bulkExecute[bulkExeIndex].recoveryMechanism;
											bulkExecuteObj.recommendations = bulkExecute[bulkExeIndex].recommendations;
											bulkExecuteObj.mttd = bulkExecute[bulkExeIndex].mttd;
											bulkExecuteObj.mttr = bulkExecute[bulkExeIndex].mttr;
											bulkExecuteObj.failureScript = bulkExecute[bulkExeIndex].failureScript;
											bulkExecuteObj.version = bulkExecute[bulkExeIndex].version;
											bulkExecuteObj.processName = bulkExecute[bulkExeIndex].processName;
											bulkExecuteObj.role = bulkExecute[bulkExeIndex].role;
											bulkExecuteObj.userBehavior = bulkExecute[bulkExeIndex].userBehavior;
											bulkExecuteObj.systemBehavior = bulkExecute[bulkExeIndex].systemBehavior;
											bulkExecuteObj.monkeyType = bulkExecute[bulkExeIndex].monkeyType;
											bulkExecuteObj.monkeyStrategy = bulkExecute[bulkExeIndex].monkeyStrategy;
											bulkExecuteObj.monkeyStrategyId = bulkExecute[bulkExeIndex].monkeyStrategyId;

											bulkExecuteList
													.push(bulkExecuteObj);
											count++;
										}
									}
									$scope.bulkExecuteScenarioList = bulkExecuteList;
									var bulkData = {};
									bulkData["scenarios"] = $scope.bulkExecuteScenarioList

									if ($scope.bulkExecuteScenarioList != null
											&& $scope.bulkExecuteScenarioList.length > 0) {
										// Call multiple scenario execution
										resiliencyStudioService
												.executeBulkScenario(bulkData)
												.then(
														function(data) {

															$scope.showTable = true;
															executeScenarioPopup();
														},
														function(error) {
															dialogs
																	.error(
																			"Error",
																			"Bulk execute scenario failed",
																			"");
															$scope.loader.loading = false;
														});
									} else {
										dialogs
												.error("Warning",
														"Please select atleast one scenario to proceed with execution.");
										$scope.loader.loading = false;
									}

								}
								$scope.showTable = true;

							}

							// load RSA modal popup
							function loadRSAPopup() {
								$("#RSAmodal").on(
										"show",
										function() { // wire up the OK button
														// to dismiss the modal
														// when shown
											$("#RSAmodal a.btn").on(
													"click",
													function(e) {

														$("#RSAmodal").modal(
																'hide'); // dismiss
																			// the
																			// dialog
													});
										});
								$("#RSAmodal").on("hide", function() { // remove
																		// the
																		// event
																		// listeners
																		// when
																		// the
																		// dialog
																		// is
																		// dismissed
									$("#RSAmodal a.btn").off("click");
								});

								$("#RSAmodal").on("hidden", function() { // remove
																			// the
																			// actual
																			// elements
																			// from
																			// the
																			// DOM
																			// when
																			// fully
																			// hidden
									$("#RSAmodal").remove();
								});

								$("#RSAmodal").modal({ // wire up the actual
														// modal functionality
														// and show the dialog
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								// ensure the modal is shown immediately
								});
							}

						} ]);