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


//Edit Application,Server Controller
app
		.controller(
				'editApplicationCtrl',
				[
						'$rootScope',
						'$scope',
						'$routeParams',
						'$filter',
						'$http',
						'$location',
						'$timeout',
						'$parse',
						'$compile',
						'$sce',
						'dialogs',
						'$modal',
						'$window',
						'resiliencyStudioService',
						function($rootScope, $scope, $routeParams, $filter,
								$http, $location, $timeout, $parse, $compile,
								$sce, dialogs, $modal, $window,
								resiliencyStudioService) {

							$rootScope.sideBar = "toggled";// For sidebar(Left) Navigation
							
							/* initialize required object and varibale on top */
							$scope.appfields = {};
							var parentRowId = $routeParams.appId;
							var envName = $routeParams.envName;
							var serverIndex = $routeParams.serverId;
							var tabId = $routeParams.tabId;

							var accordionId = "";
							var servDelete = $routeParams.servDelete;
							var tabDelete = $routeParams.tabDelete;
							$scope.loadTab = [];
							var isAddServer = false;

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							/* To display Discovery mode */
							$scope.optionsManuual = [{
								name : 'Manual',
								value : ''
							}];

							$scope.ToggleSwitch = $scope.optionsManuual[0];

							/* To close inline message through 'x' icon */
							$timeout(function() {
								$("[data-hide]").on("click",function() {
													$scope.succesEditAlert = false;
													$(this).closest("."+ $(this).attr("data-hide"))
															.hide();
												});
							});

							/* To load dropdown Values from json */
							$scope.rolesFilterData = [];
							$http({
								method : 'POST',
								url : 'app/json/add_application.json'
							})
									.success(
											function(data) {
												$scope.applications = data; // response
																			// data

												angular.forEach(data.roles,function(value,key) {
																	$scope.rolesFilterData
																			.push({
																				'id' : value,
																				'label' : value
																			});

																});
												isAddServer = false;
												if ((servDelete != null && servDelete != undefined)
														|| (tabDelete != null && tabDelete != undefined)) {
													$scope.isDelete = true;
												} else {
													$scope.isEdit = true;
												}

											});

							// Load Edit values from rest service
							$scope.loadEditValues = function(data) {
								var autoDisCPU = [];
								var editMemory = [];
								$scope.isEditDiscover = true;
								$scope.appfields = angular.copy(data);
								var key = Object
										.keys($scope.appfields.environmentMap)[0];
								$scope.environmentName = $scope.appfields.environmentMap[key].name;
								var serverList = $scope.appfields.environmentMap[key].serverList;
								var enviName = $scope.appfields.environmentMap[key].name;

								// Set values for Grid Summary
								var totsoftCompCount = 0;
								var roleCount = 0;
								var servList = $scope.appfields.environmentMap[key].serverList;
								var duplicateRoles = [];

								for (var i = 0; i < servList.length; i++) {
									if ($scope.appfields.environmentMap[key].serverList[i].swComps != null) {
										for (var j = 0; j < $scope.appfields.environmentMap[key].serverList[i].swComps.length; j++) {
											if ($scope.appfields.environmentMap[key].serverList[i].swComps[j] != null) {
												totsoftCompCount = totsoftCompCount + 1;
											}
										}
									}

									if ($scope.appfields.environmentMap[key].serverList[i].roles != null) {
										if ($scope.appfields.environmentMap[key].serverList[i].roles.length != 0) {

											for (var envMap = 0; envMap < $scope.appfields.environmentMap[key].serverList[i].roles.length; envMap++) {
												if ($scope.appfields.environmentMap[key].serverList[i].roles[envMap] != null) {

													duplicateRoles
															.push($scope.appfields.environmentMap[key].serverList[i].roles[envMap]);
												}
											}

										} else if ($scope.appfields.environmentMap[key].serverList[i].roles.length == 0) {
											duplicateRoles.push("");
											$scope.appfields.environmentMap[key].serverList[i].roles
													.push("");
										}
									} else {
										duplicateRoles.push("");
										$scope.appfields.environmentMap[key].serverList[i].roles = [];
										$scope.appfields.environmentMap[key].serverList[i].roles
												.push("");
									}

								}

								/*
								 * Display role count,server count and software
								 * component count with unique values
								 */
								var uniquesRoles = [];

								$.each(duplicateRoles, function(i, e) {
									if ($.inArray(e, uniquesRoles) == -1)
										uniquesRoles.push(e);
								});
								var countRoles = [];

								$.each(uniquesRoles, function(i, e) {
									if (e != "") {

										if ($.inArray(e, countRoles) == -1)
											countRoles.push(e);
									}

								});
								roleCount = countRoles.length;

								var genericList = [];

								for (var m = 0; m < uniquesRoles.length; m++) {
									var comObj = {};
									var totserverCount = 0;
									var softCompCount = 0;

									for (var l = 0; l < $scope.appfields.environmentMap[key].serverList.length; l++) {
										if ($scope.appfields.environmentMap[key].serverList[l].roles != null) {
											for (var n = 0; n < $scope.appfields.environmentMap[key].serverList[l].roles.length; n++) {

												if (uniquesRoles[m] == $scope.appfields.environmentMap[key].serverList[l].roles[n]) {

													totserverCount = totserverCount + 1;

													if ($scope.appfields.environmentMap[key].serverList[l].swComps != null) {
														softCompCount = softCompCount
																+ $scope.appfields.environmentMap[key].serverList[l].swComps.length;
													}

												}

											}
										}
									}

									comObj.role = uniquesRoles[m];
									comObj.softCompCount = softCompCount;
									comObj.serverCount = totserverCount;

									genericList.push(comObj);
								}

								$scope.uniquesRoles = genericList;

								// To get unique values
								Array.prototype.unique = function() {
									var arr = [];
									for (var i = 0; i < this.length; i++) {
										if (!arr.contains(this[i])) {
											arr.push(this[i]);
										}
									}
									return arr;
								};
								Array.prototype.contains = function(v) {

									for (var i = 0; i < this.length; i++) {
										if (this[i] === v) {
											return true;
										}

									}
									return false;

								};
								// End to get unique values

								$scope.serverCount = servList.length;
								$scope.totsoftCompCount = totsoftCompCount;
								$scope.roleCount = roleCount;

								// End Set values for Grid Summary

								// Memory value is converted from KB to GB &
								// AutoPopulation for EDIT
								for (var memoryVal = 0; memoryVal < serverList.length; memoryVal++) {
									if (serverList[memoryVal].memory != null) {
										if (serverList[memoryVal].memory
												.substr(-2).toUpperCase() === "KB") {
											serverList[memoryVal].memory = Math
													.ceil(parseInt(serverList[memoryVal].memory)
															/ (1024 * 1024))
													+ 'GB';
											editMemory
													.push(serverList[memoryVal].memory);
										} else if (serverList[memoryVal].memory
												.substr(-2).toUpperCase() === "MB") {
											serverList[memoryVal].memory = Math
													.ceil(parseInt(serverList[memoryVal].memory) / (1024))
													+ 'GB';
											editMemory
													.push(serverList[memoryVal].memory);
										}
									}
								}
								var uniqueEditMemory = [];
								$.each(editMemory, function(i, el) {
									if ($.inArray(el, uniqueEditMemory) === -1)
										uniqueEditMemory.push(el);
								});
								$scope.editMemory = uniqueEditMemory;

								// storage values auto population
								var editStorage = [];
								for (var storageVal = 0; storageVal < serverList.length; storageVal++) {
									editStorage[storageVal] = serverList[storageVal].storage;
								}
								var uniqueEditStorage = [];
								$.each(editStorage,function(i, el) {
									if ($.inArray(el,uniqueEditStorage) === -1)
										uniqueEditStorage.push(el);
								});
								$scope.editStorage = uniqueEditStorage;

								// CPU values fetching dynamically
								for (var cpuVal = 0; cpuVal < serverList.length; cpuVal++) {
									autoDisCPU.push(serverList[cpuVal].cpu);
								}
								var uniqueAutoDisCPU = [];
								$.each(autoDisCPU, function(i, el) {
									if ($.inArray(el, uniqueAutoDisCPU) === -1)
										uniqueAutoDisCPU.push(el);
								});
								$scope.autoDisCPU = uniqueAutoDisCPU;

								angular.forEach(serverList,function(environmentvalue,environmentkey) {
													$scope.loadTab.push(environmentvalue);
													if (envName === enviName && Number(serverIndex) === environmentkey) {
														accordionId = $scope.loadTab.indexOf(environmentvalue);
													}

												});
							}

							// Load active server Accordion and Active Component Tab
							$scope.loadEditServerTab = function() {
								if (!isAddServer) {
									$("div[id^='collapse']").not(
											"div[id^='collapseInner']")
											.closest('.collapse').removeClass(
													'in');
									$('#collapse' + accordionId).addClass('in');
									if (tabId != undefined) {
										$('#collapse' + accordionId)
												.find('.nav-tabs li:eq('+ Number(tabId)	+') a')
												.trigger('click');
									}
								}
							}

							// Get method to display application details
							resiliencyStudioService.getapplicationbyid(parentRowId).then(function(data) {
								$scope.loadEditValues(data);
							}, function(error) {
								$location.url('/404');
							});

							/*
							 * Initialize all object with their properties
							 * details like application fields,server
							 * fields,sever components
							 */
							$scope.appfields = {
								applicationName : "",
								category : "",
								environmentMap : {},
								networkArchitectureDiagram : "",
							};

							var serverfields = {
								hostName : "",
								instanceName : "",
								userName : "",
								password : "",
								rsaLogin : "",
								swComps : [],
								memory : "",
								storage : "",
								operatingSystem : "",
								ipAddress : "",
								operatingSystemType : "",
								monitor : [],
								log : [],
								discoveryApi : []
							}

							var sfComps = {
								serverComponentName : "",
								processName : ""
							}
							var discovery = {
								discoveryApi : "",
								discoveryApiDescription : ""
							}
							var monitor = {
								counterType : "",
								monitorApi : ""
							}
							var logs = {
								logType : "",
								logLocation : ""
							}

							$scope.selectEnvironmentName = function() {
								var key = $scope.environmentName;
								$scope.appfields.environmentMap[key] = {
									"name" : key,
									"environmentName" : {}
								};
							}

							// To add a new Component
							$scope.addServer = function() {
								var newServer = angular.copy(serverfields);
								newServer.swComps.push(angular.copy(sfComps));
								newServer.discoveryApi.push(angular.copy(discovery));
								newServer.monitor.push(angular.copy(monitor));
								newServer.log.push(angular.copy(logs));
								$scope.loadTab.push(newServer);
								isAddServer = true;
							}

							$scope.addSoftwareComponents = function(fields) {

								if (fields.swComps == null) {
									fields.swComps = [];
								}
								fields.swComps.push(angular.copy(sfComps));
							}

							$scope.addDiscovery = function(fields) {

								if (fields.discoveryApi == null) {
									fields.discoveryApi = [];
								}
								fields.discoveryApi.push(angular.copy(discovery));
							}

							$scope.addMonitor = function(fields) {
								if (fields.monitor == null) {
									fields.monitor = [];
								}
								fields.monitor.push(angular.copy(monitor));
							}

							$scope.addLogs = function(fields) {
								if (fields.log == null) {
									fields.log = [];
								}
								fields.log.push(angular.copy(logs));
							}
							// To remove a Component
							$scope.removeServer = function(index) {
								$scope.loadTab.splice(index, 1);
							}

							$scope.removeSoftwareComponents = function(fields,index) {
								fields.swComps.splice(index, 1);
								$('#addSoftwareCompBtn').attr("disabled", false);
							}

							$scope.removeDiscovery = function(fields, index) {
								fields.discoveryApi.splice(index, 1);
							}

							$scope.removeMonitor = function(fields, index) {
								fields.monitor.splice(index, 1);
							}

							$scope.removeLogs = function(fields, index) {
								fields.log.splice(index, 1);
							}

							// Get selected Tiers for a application
							$scope.getEnvNames = function(data) {

								var envName = [];
								angular.forEach(data,function(serverdet, serverkey) {
													angular.forEach(serverdet,function(value,key) {
															if (key === "environmentMap" && envName.indexOf(value) == -1) {
																envName.push(value);
															}
													});
												});
								return envName;
							}

							// Group application servers by Environment name
							$scope.groupServerList = function(envName, data) {
								var envMap = {};
								angular.forEach(envName, function(value, key) {
									var obj = $filter('filter')(data, {
										'environmentName' : value
									}, true);
									var serverList = obj;
									envMap[value] = {
										"name" : value,
										"serverList" : serverList
									};
								});
								return envMap;
							}

							$scope.updateData = function() {
								for (var i = 0; i < $scope.loadTab.length; i++) {
									if (Array.isArray($scope.loadTab[i].roles)) {
										$scope.loadTab[i].roles;
									} else {
										if ($scope.loadTab[i].roles != null)
											$scope.loadTab[i].roles = $scope.loadTab[i].roles.split(',');
									}
								}
								var savedData = angular.copy($scope.loadTab);
								var key = $scope.environmentName;
								$scope.appfields.environmentMap[key].serverList = savedData;
							}

							// Post updated data for Update Onboard Application
							$scope.submitMyForm = function() {
								$scope.updateData();
								var data = $scope.appfields;

								(function filter(obj) {
									$.each(obj,function(key, value) {
														if (value === "" || value === null) {
															delete obj[key];
														} else if (Object.prototype.toString.call(value) === '[object Object]') {
															filter(value);
														} else if ($.isArray(value)) {
															$.each(value,function(k,v) {
																				if (Object.prototype.toString.call(v) === '[object Object]')
																					filter(v);
																			});
														}
													});
								})(data);
								// Eliminate null data to be Posted as updated
								// data for Update Onboard Application
								var stringData = JSON.stringify(data);

								stringData = stringData.replace(new RegExp('{},', 'g'), '');
								stringData = stringData.replace(new RegExp(',{}', 'g'), '');
								while (stringData.indexOf(',"swComps":[{}]') > -1
										|| stringData.indexOf(',"monitor":[{}]') > -1
										|| stringData.indexOf(',"log":[{}]') > -1
										|| stringData.indexOf(',"discoveryApi":[{}]') > -1
										|| stringData.indexOf(',"discoveryApiList":[{}]') > -1) {

									stringData = stringData.replace(
											',"swComps":[{}]', '');
									stringData = stringData.replace(
											',"monitor":[{}]', '');
									stringData = stringData.replace(
											',"log":[{}]', '');
									stringData = stringData.replace(
											',"discoveryApi":[{}]', '');
									stringData = stringData.replace(
											',"discoveryApiList":[{}]', '');

								}

								data = JSON.parse(stringData);

								// Post call to Update application based on
								// application ID
								resiliencyStudioService
										.updateapplicationbyid(parentRowId,data)
										.then(function(data) {
													$('.alert').show();
													$scope.succesEditAlert = true;
													$scope.updatedApp = data.applicationName;

													$('body, html').animate({
																		scrollTop : $('body').offset().top
																	}, 'fast');
												},
												function(error) {

													dialogs.error("Error",
															error.message,
															error.status);
												});

							}

							// Next,Previous Server Clicks.
							$scope.serverClick = function(index, flag) {
								var $active = $('.nav-tabs li.tab_' + index);
								$active.next().removeClass('disabled');
								if (flag) {
									$('.nav-tabs .tab_' + index).parent().find(
											'.active').next().find('a').click();
								} else {
									$('.nav-tabs .tab_' + index).parent().find(
											'.active').prev().find('a').click();
								}
								// nextTab($active);
							}

							/*
							 * Redirect link along with Inline error or sucess
							 * mesage
							 */
							$(document).on('click', '.closed', function() {
								$rootScope.$apply();
								$(".modal-backdrop").remove();
							});
							/* Tab reset of server component */
							var mssageArr = [];
							mssageArr.push("Server");
							mssageArr.push("Software Component");
							mssageArr.push("Discovery");
							mssageArr.push("Monitor");
							mssageArr.push("Logs");

							$scope.tabReset = function(obj, index, tabPosition) {
								var confirmdialog = dialogs.confirm("Reset",
										"Are you sure you want to reset "
												+ mssageArr[tabPosition] + " #"
												+ index, "yes");
								confirmdialog.result.then(function(btn) {
									var keys = Object.keys(obj);
									keys.forEach(function(key) {
										if (typeof obj[key] === 'string') {
											obj[key] = null;
										}
									});
								});
								$scope.addAppForm.$setPristine();
								$scope.addAppForm.$setUntouched();

							}

							// Metrics panel, Server panel open/close(+/-)
							// symbols changing

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

							// i info icon tooltip starts here
							$(document).ready(function() {
								$('[data-toggle="tooltip"]').tooltip({
									placement : 'right'
								});
							});

							// IP Address or Server name validation
							$scope.ServerOrIP = false;
							$scope.OutOfFocus = function(v, value) {

								var key = Object
										.keys($scope.appfields.environmentMap)[0];
								$scope.appfields.environmentMap[key].serverList = angular
										.copy($scope.loadTab)
								var serverList = $scope.appfields.environmentMap[key].serverList;
								for (var i = 0; i < serverList.length; i++) {
									if (serverList[i].ipAddress != undefined
											&& serverList[i].ipAddress != ''
											&& serverList[i].userName != undefined
											&& serverList[i].userName != ''
											&& serverList[i].instanceName != undefined
											&& serverList[i].instanceName != '') {
										$scope.ServerOrIP = false;
									} else if (serverList[i].hostName != undefined
											&& serverList[i].hostName != ''
											&& serverList[i].userName != undefined
											&& serverList[i].userName != ''
											&& serverList[i].instanceName != undefined
											&& serverList[i].instanceName != '') {
										$scope.ServerOrIP = false;
									} else {
										$scope.ServerOrIP = true;
										break;
									}
								}
							}
							$scope.InFocus = function(v) {

							}

						} ]);
