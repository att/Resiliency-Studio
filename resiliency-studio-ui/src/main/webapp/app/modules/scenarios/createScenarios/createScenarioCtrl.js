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
				'createScenarioCtrl',
				[
						'$rootScope',
						'$scope',
						'$filter',
						'$http',
						'$location',
						'$parse',
						'$compile',
						'$sce',
						'$timeout',
						'resiliencyStudioService',
						function($rootScope, $scope, $filter, $http, $location,
								$parse, $compile, $sce, $timeout,
								resiliencyStudioService) {

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							$rootScope.sideBar = "toggled";// Side Bar visible
							$scope.show = true;
							$scope.hide = false;
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
							// Hide the inline success/error messages
							$timeout(
									function() {
										$("[data-hide]")
												.on("click",
														function() {
															$(this).closest("."	+ $(this).attr("data-hide"))
																	.hide();
														});
										$scope.successAlert = false;
										$scope.showerrorAlert = false;
									}, 1000);
							// Initially loader is disable
							$scope.loader = {
								loading : false,
							};
							// Initializing App fields
							$scope.appfields = {
								name : "",
								applicationName : "",
								serverName : "",
								environmentName : "",
								tier : "",
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
								monkeyStrategy : "",
								monkeyStrategyId : "",
								version : "",
								processName : "",
								monkeyType : ""
							};

							// creating duplicate object for storing app fields
							var addData = {
								name : "",
								applicationName : "",
								environmentName : "",
								tierName : "",
								serverName : "",
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
								version : "",
								processName : "",
								monkeyType : "",
								monkeyStrategy : "",
								monkeyStrategyId : ""
							};

							// Get Failure Tenet values from Json.
							resiliencyStudioService
									.getFailureTenet()
									.then(
											function(data) {
												$scope.scenarios = data;
											},
											function(error) {
												console.log("Some error occured while fetching failure tenets")
											})

							// To populate Application name into dropdown
							resiliencyStudioService
									.getApplicationDetails()
									.then(
											function(data) {
												$scope.scenariosapp = data;
												angular.forEach(
																data,
																function(value,
																		key) {
																	$scope.scenariosFilterData
																			.push({
																				'id' : value.applicationName,
																				'label' : value.applicationName
																			});

																});
											});

							// Populate Environment name based on Application
							// name
							$scope.applicationChange = function() {
								$scope.successAlert = false;
								$scope.showerrorAlert = false;
								$scope.AutoDiscoveryON = false;
								angular
										.forEach(
												$scope.scenariosapp,
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

							// Add 'ALL' to software component dropdown
							$scope.defaultValue = function() {

								if ($scope.appfields.serverName.swComps == null) {
									$scope.appfields.serverName.swComps = [];
								}
								$scope.appfields.serverName.swComps
										.push({
											id : 0,
											serverComponentName : 'ALL'
										});
							}

							// To populate monkey types in dropdown box
							resiliencyStudioService.getMonkeyTypes().then(
									function(data) {
										$scope.monkeys = data;
									});

							// To populate Monkey Strategies in dropdown box
							var selectedMonkeyType = "";
							$scope.selectedMonkeyType = function() {
								selectedMonkeyType = $scope.appfields.monkeyType;
								var Strategies = {
									monkeyStragey : [],
									monkeyStrategyId : []
								}

								// Monkey Strategies Service Call
										resiliencyStudioService
												.getMonkeyStrategies()
												.then(function(data) {

															for (var i = 0; i < data.length; i++) {
																if (data[i].monkeyType
																		.includes(selectedMonkeyType)) {
																	Strategies.monkeyStragey
																			.push(data[i].monkeyStrategyName);
																	Strategies.monkeyStrategyId
																			.push(data[i].id);
																}
															}
															// To make both
															// strategy and
															// strategyId as
															// single object
															// array
															$scope.repeatData = Strategies.monkeyStragey
																	.map(function(
																			value,
																			index) {
																		return {
																			data : value,
																			value : Strategies.monkeyStrategyId[index]
																		}
																	});
															$scope.monkeyStrategy = Strategies.monkeyStragey;
															$scope.monkeyStrategyId = Strategies.monkeyStrategyId;
														}),
										(function(error) {
											console.log("some error occure during get monkey strategy....");
										});
							}

							// Submit Create Scenario Form
							$scope.submitMyForm = function() {
								addData.name = $scope.scenariosFilterModel.id
										+ '.'
										+ $scope.appfields.environmentName.name
										+ '.'
										+ $scope.appfields.serverName.instanceName
										+ '.'
										+ $scope.appfields.softwareComponentName.serverComponentName
										+ '.'
										+ $scope.appfields.softwareComponentName.processName
										+ '.' + $scope.appfields.failureTenet
										+ '.' + $scope.appfields.failureMode
										+ '.' + '1.0';
								addData.applicationName = $scope.scenariosFilterModel.id;
								addData.environmentName = $scope.appfields.environmentName.name;
								addData.serverName = $scope.appfields.serverName.instanceName;
								addData.role = $scope.appfields.role;
								addData.softwareComponentName = $scope.appfields.softwareComponentName.serverComponentName;
								addData.processName = $scope.appfields.softwareComponentName.processName;
								addData.failureTenet = $scope.appfields.failureTenet;
								addData.failureMode = $scope.appfields.failureMode;
								addData.monkeyType = $scope.appfields.monkeyType;
								addData.userBehavior = $scope.appfields.userBehavior;
								addData.systemBehavior = $scope.appfields.systemBehavior;
								addData.causeOfFailure = $scope.appfields.causeOfFailure;
								addData.currentControls = $scope.appfields.currentControls;
								addData.detectionMechanism = $scope.appfields.detectionMechanism;
								addData.recoveryMechanism = $scope.appfields.recoveryMechanism;
								addData.recommendations = $scope.appfields.recommendations;
								addData.mttd = $scope.appfields.mttd;
								addData.mttr = $scope.appfields.mttr;
								addData.monkeyStrategy = $scope.appfields.monkeyStrategy.data;
								addData.monkeyStrategyId = $scope.appfields.monkeyStrategy.value;
								addData.version = "1.0";

								// Create Scenario Service Call
								resiliencyStudioService
										.addScenario(addData)
										.then(
												function(data) {
													$('.alert').show();
													$scope.showerrorAlert = false;
													$scope.successAlert = true;
													$scope.resetScenario();
													$('body, html')
															.animate(
																	{
																		scrollTop : $(
																				'body')
																				.offset().top
																	}, 'fast');
												},
												function(error) {
													$('.alert').show();
													$scope.successAlert = false;
													$scope.showerrorAlert = true;
													$scope.errorMessage = error.message;
													$('body, html')
															.animate(
																	{
																		scrollTop : $(
																				'body')
																				.offset().top
																	}, 'fast');
												});
							};

							// Resetting form partially after submitting form
							// and retain some fields
							$scope.resetScenario = function() {
								$scope.appfields.userBehavior = null;
								$scope.appfields.systemBehavior = null;
								$scope.appfields.failureMode = null;
								$scope.appfields.causeOfFailure = null;
								$scope.appfields.currentControls = null;
								$scope.appfields.detectionMechanism = null;
								$scope.appfields.recoveryMechanism = null;
								$scope.appfields.recommendations = null;
								$scope.appfields.mttd = null;
								$scope.appfields.mttr = null;
								$scope.appfields.monkeyStrategy = null;
								$scope.appfields.monkeyType = null;
								delete $scope.repeatData;
							};

							// Resetting Create Scenario page values
							$scope.resetForm = function() {
								$scope.resetMsg = "Are you sure you want to reset the Scenario?";
								resetPopup();// Reset confirmation popup
							}

							// Resetting complete form
							$scope.gotoResetForm = function() {
								$scope.scenariosFilterModel.id = null;
								$scope.addScenarioForm.$setPristine(true);
								$scope.appfields.name = null;
								$scope.appfields.applicationName = null;
								$scope.appfields.serverName = null;
								$scope.appfields.environmentName = null;
								$scope.appfields.softwareComponentName = null;
								$scope.appfields.failureTenet = null;
								$scope.appfields.failureMode = null;
								$scope.appfields.causeOfFailure = null;
								$scope.appfields.userBehavior = null;
								$scope.appfields.systemBehavior = null;
								$scope.appfields.currentControls = null;
								$scope.appfields.detectionMechanism = null;
								$scope.appfields.recoveryMechanism = null;
								$scope.appfields.recommendations = null;
								$scope.appfields.mttd = null;
								$scope.appfields.mttr = null;
								$scope.appfields.monkeyStrategy = null;
								$scope.appfields.monkeyType = null;
								$scope.appfields.version = null;
								$scope.appfields.processName = null;
							}

							// Scenario Auto Discovery starts here
							$scope.viewAutoDiscovery = function(env) {
								$scope.AutoDiscoveryON = true;
								$scope.loader.loading = true;
								$('#submitScenario').show();
								$scope.show = false;

								var applicationName = $scope.scenariosFilterModel.id;
								var environment = $scope.appfields.environmentName.name;

								// Auto discovery service call
								resiliencyStudioService
										.getFailurePointsByAppEnv(
												applicationName, environment)
										.then(
												function(data) {
													$scope.hide = true;
													var dataRes = data;
													$scope.loader.loading = false;
													$('#myBulk').show();
													$('#scenarioList').show();

													var addScenarioObjList = new Array();

													for ( var serv in dataRes) {

														if (dataRes
																.hasOwnProperty(serv)) {

															for (var j = 0; j < dataRes[serv].length; j++) {
																var addScenarioObj = {};
																addScenarioObj.serv = serv;
																addScenarioObj.category = dataRes[serv][j].category;
																addScenarioObj.softwareComponentName = dataRes[serv][j].component;
																addScenarioObj.role = dataRes[serv][j].role;
																addScenarioObj.processName = dataRes[serv][j].processName;
																addScenarioObj.failureTenet = dataRes[serv][j].failureTenet;
																addScenarioObj.failureMode = dataRes[serv][j].failureMode;
																addScenarioObj.causeOfFailure = dataRes[serv][j].causeOfFailure;
																addScenarioObj.currentControls = dataRes[serv][j].currentControls;
																addScenarioObj.detection = dataRes[serv][j].detection;
																addScenarioObj.recoveryAction = dataRes[serv][j].recoveryAction;
																addScenarioObj.recommendation = dataRes[serv][j].recommendation;
																addScenarioObj.mttd = dataRes[serv][j].mttd;
																addScenarioObj.mttr = dataRes[serv][j].mttr;
																addScenarioObj.userBehavior = dataRes[serv][j].userBehavior;
																addScenarioObj.systemBehavior = dataRes[serv][j].systemBehavior;
																addScenarioObj.monkeyStrategy = dataRes[serv][j].monkeyStrategy;
																addScenarioObj.monkeyType = dataRes[serv][j].monkeyType;
																addScenarioObj.version = dataRes[serv][j].version;
																addScenarioObj.id = dataRes[serv][j].id;
																addScenarioObjList
																		.push(addScenarioObj);
															}
														}
													}
													$scope.scenarioObjList = addScenarioObjList;
													var roleResult = groupBy(
															'role',
															addScenarioObjList);

													var summaryObj = [];

													for ( var role in roleResult) {
														var summaryInfo = {};
														var scenarioCountByrole = countDuplicates(roleResult[role]);
														if (null != role) {
															summaryInfo.role = role;
														} else {
															summaryInfo.role = "";
														}

														summaryInfo.scenarioCount = scenarioCountByrole.scenarioCount
														summaryInfo.serverCount = scenarioCountByrole.serverCount
														summaryInfo.totalScenarioCount = scenarioCountByrole.scenarioCount
																* scenarioCountByrole.serverCount
														summaryObj
																.push(summaryInfo);

													}

													$scope.summaryObjList = summaryObj;
													$scope.roleCount = 0;

													for (var i = 0; i < summaryObj.length; i++) {
														if (summaryObj[i].role != undefined
																&& summaryObj[i].role != null
																&& summaryObj[i].role != "") {
															$scope.roleCount = $scope.roleCount + 1;
														}
													}

													$scope.scenarioCount = addScenarioObjList.length;
													$scope.serverCount = uniqueElement().length;

													if ($scope.roleCount == 0
															&& $scope.scenarioCount == 0
															&& $scope.serverCount == 0) {
														$scope.submitScnDisable = true;
													}

												},
												function(error) {
													$scope.loader.loading = false;
													$('#category').hide();
													$('#scenarioList').hide();
													$('#myBulk').hide();
													$scope.hide = false;
													$scope.showerrorAlert = true;
													$scope.errorMessage = "Some error occured while fetching auto discovered failure points";
													$scope.submitScnDisable = true;
												});

							}

							function uniqueElement() {
								var lookup = {};
								var items = $scope.scenarioObjList;
								var result = [];

								for (var item, i = 0; item = items[i++];) {
									var serv = item.serv;

									if (!(serv in lookup)) {
										lookup[serv] = 1;
										result.push(item.serv);

									}
								}

								return result;
							}

							function countDuplicates(arr) {
								var a = [], b = [], prev;
								var obj = {};
								for (var i = 0; i < arr.length; i++) {
									if (arr[i] !== prev) {
										a.push(arr[i]);
										b.push(1);
									} else {
										b[b.length - 1]++;
									}
									prev = arr[i];
								}

								obj.serverCount = a.length;
								var sum = 0;
								for (var j = 0; j < b.length; j++) {
									sum = sum + b[j];

								}
								obj.scenarioCount = sum;
								return obj;
							}

							function groupBy(propertyName, array) {
								var groupedElements = {};

								for (var i = 0; i < array.length; i++) {
									var element = array[i];
									var value = element[propertyName];

									var group = groupedElements[value];
									if (group == undefined) {
										group = [ element.serv ];
										groupedElements[value] = group;
									} else {
										group.push(element.serv);
									}
								}

								return groupedElements;
							}

							// To expand/collapse auto discovered scenarios from
							// table
							$scope.showDetail = function(coll) {
								if ($scope.active != coll) {
									$scope.active = coll;
								} else {
									$scope.active = null;
								}
							};

							// Bulk Add Scenario Object initialization

							// To check Application Name & Environment is not
							// null
							$scope.autoDiscoveryEnable = function() {
								if ($scope.scenariosFilterModel.id
										&& $scope.appfields.environmentName) {
									return false;
								} else {
									return true;
								}
							}

							// Submit bulk Scenario
							$scope.submitBulkScenario = function() {
								$scope.loader.loading = true;
								window.scrollTo(0, 0);
								var bulkScenario = $scope.scenarioObjList;
								var bulkAddList = new Array();
								for (var j = 0; j < bulkScenario.length; j++) {
									var bulkScenarioObj = {};
									bulkScenarioObj.applicationName = $scope.scenariosFilterModel.id;
									bulkScenarioObj.environmentName = $scope.appfields.environmentName.name;
									bulkScenarioObj.serverName = bulkScenario[j].serv;
									bulkScenarioObj.role = bulkScenario[j].role;
									bulkScenarioObj.softwareComponentName = bulkScenario[j].softwareComponentName;
									bulkScenarioObj.processName = bulkScenario[j].processName;
									bulkScenarioObj.causeOfFailure = bulkScenario[j].causeOfFailure;
									bulkScenarioObj.failureTenet = bulkScenario[j].failureTenet;
									bulkScenarioObj.failureMode = bulkScenario[j].failureMode;
									bulkScenarioObj.currentControls = bulkScenario[j].currentControls;
									bulkScenarioObj.detectionMechanism = bulkScenario[j].detection;
									bulkScenarioObj.recoveryMechanism = bulkScenario[j].recoveryAction;
									bulkScenarioObj.recommendations = bulkScenario[j].recommendation;
									bulkScenarioObj.mttd = bulkScenario[j].mttd;
									bulkScenarioObj.mttr = bulkScenario[j].mttr;
									bulkScenarioObj.userBehavior = bulkScenario[j].userBehavior;
									bulkScenarioObj.systemBehavior = bulkScenario[j].systemBehavior;
									bulkScenarioObj.monkeyStrategy = bulkScenario[j].monkeyStrategy;
									bulkScenarioObj.monkeyType = bulkScenario[j].monkeyType;

									// TODO:Currently Rest Service is not
									// retrieving version. So, hard coded the
									// version.
									bulkScenarioObj.version = "1.0";

									bulkScenarioObj.name = $scope.scenariosFilterModel.id
											+ '.'
											+ $scope.appfields.environmentName.name
											+ '.'
											+ bulkScenario[j].serv
											+ '.'
											+ bulkScenario[j].softwareComponentName
											+ '.'
											+ bulkScenario[j].processName
											+ '.'
											+ bulkScenario[j].failureTenet
											+ '.' + bulkScenario[j].failureMode; // +i;
									bulkScenarioObj.tierName = "";

									bulkAddList.push(bulkScenarioObj);
								}
								$scope.bulkAddList = bulkAddList;
								var bulkScenarioData = {};
								bulkScenarioData["scenarios"] = $scope.bulkAddList;

								// Bulk add scenario service call
								resiliencyStudioService
										.bulkAddScenario(bulkScenarioData)
										.then(
												function(success) {

													$scope.loader.loading = false;
													$('.alert').show();
													$scope.showerrorAlert = false;
													$scope.successAlert = true;
													$scope.dialogIsHidden = false;
												},
												function(error) {
													$scope.loader.loading = false;
													$('.alert').show();
													$scope.successAlert = false;
													$scope.showerrorAlert = true;
													$scope.errorMessage = error.statusMessage;
												});
							}

							// Metrics panel, Server panel open/close(+/-)
							// symbols changing
							$scope.SDpanel = true;
							$scope.serverpanel = function() {
								if ($scope.SDpanel)
									$scope.SDpanel = false;
								else
									$scope.SDpanel = true;
							}
							$scope.MDpanel = true;
							$scope.metricspanel = function() {
								if ($scope.MDpanel)
									$scope.MDpanel = false;
								else
									$scope.MDpanel = true;
							}

							// Filter Functionality
							$scope.filterTr = false;

							$scope.showFilter = function() {
								$scope.filterTr = true;
							}

							$scope.closeFilter = function() {
								$scope.filterTr = false;
								$scope.search = {};
							}
							$scope.search = {};
							$scope.clearServer = function() {
								if ($scope.search.serv.length == 0) {
									delete $scope.search.serv;
								}
							}
							$scope.clearSfComp = function() {
								if ($scope.search.softwareComponentName.length == 0) {
									delete $scope.search.softwareComponentName;
								}
							}
							$scope.clearRole = function() {
								if ($scope.search.role.length == 0) {
									delete $scope.search.role;
								}
							}
							$scope.clearProcessName = function() {
								if ($scope.search.processName.length == 0) {
									delete $scope.search.processName;
								}
							}
							$scope.clearFailureMode = function() {
								if ($scope.search.failureMode.length == 0) {
									delete $scope.search.failureMode;
								}
							}
							$scope.clearFailureTenet = function() {
								if ($scope.search.failureTenet.length == 0) {
									delete $scope.search.failureTenet;
								}
							}

							// Back button functioinality
							$scope.goBack = function() {
								location.reload();
							};

							// Go to View Scenario
							$scope.gotoViewScenario = function() {
								$timeout(function() {
									$location
											.path("/viewScenario/"
													+ $scope.scenariosFilterModel.id
													+ "/"
													+ $scope.appfields.environmentName.name);
								});
							}

							// Reset scenario confirmation popup
							function resetPopup() {
								$("#resetModal").on(
										"show",
										function() {
											$("#resetModal a.btn").on(
													"click",
													function(e) {
														$("#resetModal").modal(
																'hide');
													});
										});
								$("#resetModal").on("hide", function() {
									$("#resetModal a.btn").off("click");
								});

								$("#resetModal").on("hidden", function() {
									$("#resetModal").remove();
								});

								$("#resetModal").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});
							}
						} ]);