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
				'viewScenarioCtrl',
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
						'$route',
						'$routeParams',
						function($rootScope, $scope, $filter, $http, $location,
								$parse, $compile, $sce, $timeout,
								resiliencyStudioService, $route, $routeParams) {
							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							$rootScope.sideBar = "toggled";
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
							$scope.viewScen = false;// Initially scenario table
													// is hidden
							$scope.loader = {
								loading : false,
							};
							$timeout(function() {
								$("[data-hide]")
										.on("click",function() {
											$(this).closest("." + $(this).attr("data-hide"))
															.hide();
												});
							});

							$scope.applicationSelected = true;
							$scope.filterTr = false;
							$scope.respectiveFilter = false;

							// Filter Functionality starts here
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
							$scope.clearFailureTenet = function() {
								if ($scope.search.failureTenet.length == 0) {
									delete $scope.search.failureTenet;
								}
							}
							$scope.clearFailureMode = function() {
								if ($scope.search.failureMode.length == 0) {
									delete $scope.search.failureMode;
								}
							}

							// To populate Application names in drop down box
							resiliencyStudioService
									.getApplicationDetails()
									.then(
											function(data) {
												$scope.scenario = data;
												angular
														.forEach(
																data,
																function(value,
																		key) {
																	$scope.scenariosFilterData
																			.push({
																				'id' : value.applicationName,
																				'label' : value.applicationName
																			});
																	$scope.applicationChange();

																});
											});

							// To fetch scenarios automatically when come back
							// from create scenario
							$timeout(function() {
								if ($routeParams.hasOwnProperty('application')
										&& $routeParams
												.hasOwnProperty('environment')) {
									$scope.scenariosFilterModel = {
										'id' : $routeParams.application,
										'label' : $routeParams.application
									};
									$scope.applicationChange();
									$scope.appfields.environmentName = $routeParams.environment;
									$scope.getScenarios();
								}
							});

							// To populate environment name based on application
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
																		.push(value.environmentMap[obj].name);
															}
														}
													}
												});
								if ($scope.environmentMap)
									$scope.appfields.environmentName = $scope.environmentMap[0];
							};

							// get Scenarios by application service call
							$scope.getScenarios = function() {
								var applicationName = $scope.scenariosFilterModel.id;
								var envName = $scope.appfields.environmentName;
								$scope.scenarioDelSuccess = false;
								$scope.loader.loading = true;
								$scope.viewScen = true;

								resiliencyStudioService
										.getScenariosByApplicationName(
												applicationName)
										.then(
												function(data) {
													$scope.errorMessage = false
													$scope.data = data;
													$scope.outcome.success = false;
													$scope.loader.loading = false;
													$('#userList').show();
												},
												function(error) {
													$('#userList').hide();
													$scope.scenarioDelSuccess = false;
													$scope.loader.loading = false;
													$scope.errorMessage = "Scenario is not available for selected Application: { "
															+ applicationName
															+ " } and environment: { "
															+ envName + " }";
												});
							}

							// To view single scenario exapansion ata time
							$scope.showDetail = function(u) {
								if ($scope.active != u) {
									$scope.active = u;
								} else {
									$scope.active = null;
								}
							};

							$scope.appfields = {
								name : "",
								applicationName : "",
								serverName : "",
								role : "",
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
								failureScript : "",
								version : "",
								processName : ""
							};

							// Redirecting to Edit scenario page
							$scope.editData = {};
							$scope.Edit = function() {
								var editableScenarioID = $scope.editData.OneScenario.id;
								if ($scope.editData.OneScenario != "null")
									$location.path('/editScenario/'
											+ editableScenarioID);
							};

							$scope
									.$watch('data',
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

							// To Delete scenario(s) based on single and
							// multiple selection
							$scope.selectedScenarios = {};
							$scope.deleteScenario = function() {
								var bulkDelete = $scope.data;
								var deleteBulkSecnarioList = [];

								for (var i = 0; i < $scope.data.length; i++) {
									if (bulkDelete[i].selected == true) {
										var deleteBulkScenario = {};
										deleteBulkScenario.id = bulkDelete[i].id;
										deleteBulkScenario.name = bulkDelete[i].name;
										deleteBulkScenario.environmentName = $scope.appfields.environmentName;
										deleteBulkScenario.applicationName = bulkDelete[i].applicationName;
										deleteBulkScenario.tierName = bulkDelete[i].tierName;
										deleteBulkScenario.serverName = bulkDelete[i].serverName;
										deleteBulkScenario.softwareComponentName = bulkDelete[i].softwareComponentName;
										deleteBulkScenario.failureTenet = bulkDelete[i].failureTenet;
										deleteBulkScenario.failureMode = bulkDelete[i].failureMode;
										deleteBulkScenario.causeOfFailure = bulkDelete[i].causeOfFailure;
										deleteBulkScenario.currentControls = bulkDelete[i].currentControls;
										deleteBulkScenario.detectionMechanism = bulkDelete[i].detectionMechanism;
										deleteBulkScenario.recoveryMechanism = bulkDelete[i].recoveryMechanism;
										deleteBulkScenario.recommendations = bulkDelete[i].recommendations;
										deleteBulkScenario.mttd = bulkDelete[i].mttd;
										deleteBulkScenario.mttr = bulkDelete[i].mttr;
										deleteBulkScenario.failureScript = bulkDelete[i].failureScript;
										deleteBulkScenario.version = bulkDelete[i].version;
										deleteBulkScenario.processName = bulkDelete[i].processName;
										deleteBulkScenario.role = bulkDelete[i].role;
										deleteBulkScenario.userBehavior = bulkDelete[i].userBehavior;
										deleteBulkScenario.systemBehavior = bulkDelete[i].systemBehavior;

										deleteBulkSecnarioList
												.push(deleteBulkScenario);
									}
								}

								$scope.bulkDeleteScenario = deleteBulkSecnarioList;

								$scope.selectedScenarios["scenarios"] = $scope.bulkDeleteScenario;
								if ($scope.bulkDeleteScenario != null
										&& $scope.bulkDeleteScenario.length > 0) {
									$scope.deleteConfirmationMsg = "Are you sure want to delete for selected scenarios?"
									deleteScenarioConfirmationPopup();
								} else {
									console.log("error occured while deleting scenarios")
									deleteScenarioErrorPopup();
								}
							}

							var selectedScenarios = {
								scenarios : ""
							}
							selectedScenarios = $scope.selectedScenarios;
							$scope.gotoBulkScenarioDelete = function() {
								$scope.loader.loading = true;
								resiliencyStudioService
										.deleteSelectedScenarios(
												selectedScenarios)
										.then(function(data) {
													$('body, html')
															.animate(
																	{
																		scrollTop : $('body')
																				.offset().top
																	}, 'fast');
													$('.alert').show();
													$scope.outcome.success = false;
													$scope.loader.loading = true;
													resiliencyStudioService
															.getScenariosByApplicationName(
																	$scope.scenariosFilterModel.id)
															.then(function(data) {
																		$scope.scenarioDelSuccess = true;
																		$scope.errorMessage = false
																		$scope.data = data;
																		$scope.loader.loading = false;
																		$('#userList')
																				.show();
																	},
																	function(error) {
																		$('#userList')
																				.hide();
																		$scope.loader.loading = false;
																		$scope.errorMessage = "Scenario(s) is not available for selected Application: { "
																				+ $scope.scenariosFilterModel.id
																				+ " }" // and
																						// environment:
																						// {
																						// "+envName+"
																						// }";
																	});
												},
												function(error) {
													$scope.scenarioDelSuccess = false;
													$scope.loader.loading = false;
													$scope.errorMessage = "Unable to delete the scenrio";
												});

								return "";
							};

							$scope.retainDeleteScenario = function() {
								$('#bulkDeleteScenario').modal('hide');
								$('body').removeClass('modal-open');
								$('.modal-backdrop').remove();
								$route.reload();
								$location.path("/view-scenario/"
										+ $scope.scenariosFilterModel.id + "/"
										+ $scope.appfields.environmentName);

							}
							// Check box model attribute initialization
							$scope.outcome = {
								success : ""
							};

							// Checkbox select/deselect all functionality
							$scope.selectAll = function() {
								if ($scope.outcome.success) {

									$scope.outcome.success = true;
								} else {
									$scope.outcome.success = false;
								}
								angular.forEach($scope.data, function(u) {
									u.selected = $scope.outcome.success;
								});
							}

							// Delete Scenario confirmation popup
							function deleteScenarioConfirmationPopup() {
								$("#bulkDeleteScenario")
										.on("show",
												function() {
													$("#bulkDeleteScenario a.btn")
															.on("click",
																	function(e) {
																		$("#bulkDeleteScenario")
																				.modal('hide');
																	});
												});
								$("#bulkDeleteScenario").on(
										"hide",
										function() {
											$("#bulkDeleteScenario a.btn").off("click");
										});

								$("#bulkDeleteScenario").on("hidden",
										function() {
											$("#bulkDeleteScenario").remove();
										});

								$("#bulkDeleteScenario").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});

							}

							// Delete scenario error popup
							function deleteScenarioErrorPopup() {
								$("#errorDeleteScenario")
										.on("show",
												function() {
													$("#errorDeleteScenario a.btn")
															.on("click",
																	function(e) {
																		$("#errorDeleteScenario")
																				.modal('hide');
																	});
												});
								$("#errorDeleteScenario").on(
										"hide",
										function() {
											$("#errorDeleteScenario a.btn")
													.off("click");
										});

								$("#errorDeleteScenario").on("hidden",
										function() {
											$("#errorDeleteScenario").remove();
										});

								$("#errorDeleteScenario").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});
							}

						} ]);