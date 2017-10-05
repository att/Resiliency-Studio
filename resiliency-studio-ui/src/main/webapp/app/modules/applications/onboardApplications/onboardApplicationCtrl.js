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
				'onboardApplicationCtrl',
				[
						'$rootScope',
						'$scope',
						'$filter',
						'resiliencyStudioService',
						'$http',
						'dialogs',
						'$location',
						'$parse',
						'$compile',
						'$sce',
						'$timeout',
						function($rootScope, $scope, $filter,
								resiliencyStudioService, $http, dialogs,
								$location, $parse, $compile, $sce, $timeout) {
							$rootScope.sideBar = "toggled"; // For sidebar(Left
															// ) Navigation

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							/* To close inline message through 'x' icon */
							$timeout(function() {
								$("[data-hide]").on("click",function() {
													$(this).closest("."+ $(this).attr("data-hide")).hide();
												});
							});

							/*
							 * Initialize serverCount, totsoftCompCount,
							 * roleCount
							 */
							$scope.serverCount = 0;
							$scope.totsoftCompCount = 0;
							$scope.roleCount = 0;

							/* Initially add the server in expand mode */
							$scope.isaddForServer = true;

							$("[data-toggle=tooltip]").tooltip({
								placement : 'right'
							});

							/* To display Discovery mode */
							$scope.optionsManuual = [

							{
								name : 'Manual',
								value : ''
							} ];

							/* Initially Discovery mode should be manual */
							$scope.ToggleSwitch = $scope.optionsManuual[0];

							/*
							 * To reset onBoard Application form on click of
							 * RESET button
							 */
							
							$scope.serverReset = function() {
								$scope.loadTab = [];
								$scope.addServer();
							}
							$scope.resetForm = function() {
								$scope.addAppForm.$setPristine();
								$scope.addAppForm.$setUntouched();
								$scope.MetricsGrid = false;
								$scope.isSearch = true;
								$scope.showduplicateerrorAlert = false;
								$scope.loadTab = [];
								$scope.addServer();
								$timeout(function() {

									$scope.appfields.category = "";

								}, 500);

							}

							$scope.show = true;

							/*
							 * To display Environment value dropdown on initial
							 * page load
							 */

							$scope.MetricsGrid = false;
							$scope.rolesFilterData = [];
							$http({
								method : 'POST',
								url : 'app/json/add_application.json'

							}).success(
									function(data) {
										$scope.applications = data; // response
										// data
										angular.forEach(data.roles, function(
												value, key) {
											$scope.rolesFilterData.push({
												'id' : value,
												'label' : value
											});

										});
									});

							/* To add new server on click of SERVER button */
							$scope.isAdd = true;

							/* For blank server */
							$scope.loadTab = [];

							/* Initialize appFields values manual */
							$scope.appfields = {
								applicationName : "",
								environmentName : "",
								category : "",
								environmentMap : {},
								networkArchitectureURL : "" // ,

							};

							/* Initialize serverFields values manual */
							var serverfields = {
								hostName : "",
								instanceName : "",
								userName : "",
								password : "",
								rsaLogin : "",
								roles : [],
								swComps : [],
								memory : "",
								storage : "",
								operatingSystem : "",
								ipAddress : "",
								operatingSystemType : "",
								tier : "",
								monitor : [],
								log : [],
								discoveryApi : []
							}

							/*
							 * Initialize Software Components, Discovery,
							 * Monitor and Logs object for manual
							 */
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

							/* Deep clone copy old server to new server fields */
							var newServer = angular.copy(serverfields);
							newServer.swComps.push(angular.copy(sfComps));
							newServer.discoveryApi
									.push(angular.copy(discovery));
							newServer.monitor.push(angular.copy(monitor));
							newServer.log.push(angular.copy(logs));
							$scope.loadTab.push(newServer);

							$scope.selectEnvironmentName_LoadTab = function() {

								var key = $scope.appfields.environmentName;

								$scope.appfields.environmentMap = {};
								$scope.appfields.environmentMap[key] = {
									"name" : key,
									"serverList" : []
								};

							}

							/* To add new Server along with Index */
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

							/* To add Server */
							$scope.addServer = function() {

								var newServer = angular.copy(serverfields);
								newServer.swComps.push(angular.copy(sfComps));
								newServer.discoveryApi.push(angular
										.copy(discovery));
								newServer.monitor.push(angular.copy(monitor));
								newServer.log.push(angular.copy(logs));
								$scope.loadTab.push(newServer);
							}

							/* To remove Server */
							$scope.removeServer = function(index) {
								$scope.loadTab.splice(index, 1);
							}

							/* To add SoftwareComponents fields */
							$scope.addSoftwareComponents = function(fields) {
								fields.swComps.push(angular.copy(sfComps));
							}

							/* To remove SoftwareComponents fields */
							$scope.removeSoftwareComponents = function(fields,
									index) {
								fields.swComps.splice(index, 1);
								$('#addSoftwareCompBtn')
										.attr("disabled", false);
							}

							/* To add Discovery fields */
							$scope.addDiscovery = function(fields) {
								fields.discoveryApi.push(angular
										.copy(discovery));
							}

							/* To remove Discovery fields */
							$scope.removeDiscovery = function(fields, index) {
								fields.discoveryApi.splice(index, 1);
							}

							/* To add Monitor fields */
							$scope.addMonitor = function(fields) {
								fields.monitor.push(angular.copy(monitor));
							}

							/* To remove Monitor fields */
							$scope.removeMonitor = function(fields, index) {
								fields.monitor.splice(index, 1);
							}

							/* To add Logs fields */
							$scope.addLogs = function(fields) {
								fields.log.push(angular.copy(logs));
							}

							/* To remove Logs fields */
							$scope.removeLogs = function(fields, index) {
								fields.log.splice(index, 1);
							}

							/* To save Server details */
							$scope.saveData = function() {

								for (var i = 0; i < $scope.loadTab.length; i++) {
									if (Array.isArray($scope.loadTab[i].roles)) {
										$scope.loadTab[i].roles;
									} else {
										if ($scope.loadTab[i].roles != null)
											$scope.loadTab[i].roles = $scope.loadTab[i].roles
													.split(',');
									}
								}
								var savedData = angular.copy($scope.loadTab);
								$scope.selectEnvironmentName_LoadTab();

								var key = $scope.appfields.environmentName;

								$scope.appfields.environmentMap[key].serverList = angular
										.copy(savedData);
							}

							// Image File Validation
							/*
							 * $scope.uploadImage = function (files) { var ext =
							 * files[0].name.match(/\.(.+)$/)[1];
							 * if(angular.lowercase(ext) ==='jpg' ||
							 * angular.lowercase(ext) ==='gif' ||
							 * angular.lowercase(ext) ==='png' ||
							 * angular.lowercase(ext) ==='bmap'){ var fd = new
							 * FormData(); fd.append("file",files[0]); } else{
							 * alert("Invalid File Format"); } }
							 */

							/* Submit onBoard Application form */
							$scope.submitMyForm = function() {

								$scope.saveData();

								$scope.appfields.applicationName = $scope.appfields.applicationName;

								var data = $scope.appfields;

								(function filter(obj) {
									$.each(obj,function(key, value) {
														if (value === ""
																|| value === null) {
															delete obj[key];
														} else if (Object.prototype.toString
																.call(value) === '[object Object]') {
															filter(value);
														} else if ($.isArray(value)) {
															$.each(value,function(k,v) {
																				if (Object.prototype.toString
																						.call(v) === '[object Object]')
																					filter(v);
																			});
														}
													});
								})(data);
								/*
								 * eleminate or avoid null entry submission of
								 * swComps,discovery,log component
								 */
								var stringData = JSON.stringify(data);
								stringData = stringData.replace(new RegExp('{},', 'g'), '');
								stringData = stringData.replace(new RegExp(',{}', 'g'), '');
								while (stringData.indexOf(',"swComps":[{}]') > -1
										|| stringData.indexOf(',"monitor":[{}]') > -1
										|| stringData.indexOf(',"log":[{}]') > -1
										|| stringData.indexOf(',"discoveryApi":[{}]') > -1
										|| stringData.indexOf(',"discoveryApiList":[{}]') > -1) {

									stringData = stringData.replace(',"swComps":[{}]', '');
									stringData = stringData.replace(',"monitor":[{}]', '');
									stringData = stringData.replace(',"log":[{}]', '');
									stringData = stringData.replace(',"discoveryApi":[{}]', '');
									stringData = stringData.replace(',"discoveryApiList":[{}]', '');

								}

								data = JSON.parse(stringData);

								/* Onboard application using Post method */
								resiliencyStudioService
										.addapplication(data)
										.then(
												function(data) {

													$scope.addAppForm
															.$setPristine();
													$scope.addAppForm
															.$setUntouched();

													$('.alert').show();
													$scope.showSuccessAlert = true;
													$scope.MetricsGrid = false;
													$scope.showduplicateerrorAlert = false;
													/*
													 * After sucessful Onboard
													 * application Auto Redirect
													 * on page top body
													 */
													$('body, html')
															.animate(
																	{
																		scrollTop : $(
																				'body')
																				.offset().top
																	}, 'fast');
													$scope.resetForm();
													$scope.onboardApp = $scope.appfields.applicationName;

													$("#my-form")[0].reset();
													$scope.appfields.environmentName = "";
													$scope.appfields.category = "";

												},
												function(error) {

													$('.alert').show();
													$scope.MetricsGrid = false;
													$scope.showduplicateerrorAlert = true;
													$scope.showSuccessAlert = false;
													/*
													 * After failure message
													 * Onboard application Auto
													 * Redirect on page top body
													 */
													$('body, html')
															.animate({
																		scrollTop : $('body').offset().top
																	}, 'fast');
													$scope.errormessage = "Application with name"
															+ " "
															+ $scope.appfields.applicationName
															+ " "
															+ "already exist";

												});

							}

							/*
							 * Reset server details by clicking of Reset Server
							 * button
							 */
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
											$scope.ServerOrIP = true;
											obj[key] = null;
										}
									});
								});
								$scope.addAppForm.$setPristine();
								$scope.addAppForm.$setUntouched();

							}

							/*
							 * Redirect to view application page after
							 * successful form submit
							 */
							$(document).on('click', '.closed', function() {
								$rootScope.$apply();
								$(".modal-backdrop").remove();
							});

							// Load First server Accordion by default
							$scope.loadFirestServerTab = function() {

								$("div[id^='collapse']").not(
										"div[id^='collapseInner']").closest(
										'.collapse').removeClass('in');
								$('#accordion > .panel:last').find(
										'div[id^="collapse"]').addClass('in');
							}

							$(document).ready(function() {
								$('[data-toggle="tooltip"]').tooltip({
									placement : 'right'
								});
							});

							// Metrics panel, Server panel open/close(+/-)
							// symbols changing
							//
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

							// remove Duplicates in select box
							$(".select option").val(
									function(i, v) {
										$(this).siblings("[value='" + v + "']")
												.remove();
									});

							// RSA Login Check box
							$scope.rsaLoginSwitch = function() {
								// $scope.loadTab.userName = $rootScope.attuid;
							}

							// IP Address or Server name validation
							$scope.ServerOrIP = true;
							$scope.OutOfFocus = function(v, value) {

								var serverList = angular.copy($scope.loadTab);
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

						} ]);