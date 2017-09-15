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

//Scenarios Run YTD Controller
app
		.controller(
				'scenariosRunYTDCtrl',
				[
						'$rootScope',
						'$scope',
						'$filter',
						'$http',
						'$location',
						'$parse',
						'$compile',
						'$sce',
						'$modal',
						'$window',
						'constantService',
						'env',
						function($rootScope, $scope, $filter, $http, $location,
								$parse, $compile, $sce, $modal, $window,
								constantService, env) {

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							$rootScope.sideBar = "toggled";
							var _env = {
								URL : env.get('env_url'),

							}
							$(document)
									.ready(
											function() {
												$('#scenarioYtdTable')
														.DataTable(
																{
																	"scrollX" : true,
																	"order" : [ [9,"desc" ] ],
																	ajax : {

																		"dataSrc" : "",
																		type : "GET",
																		xhrFields : {
																			withCredentials : true
																		},
																		contentType : "application/json",
																		crossDomain : true,
																		url : _env.URL
																				+ constantService.constants.scenarioRunYTD,
																		"error" : function(
																				jqXHR,
																				textStatus,
																				errorThrown) {
																			$(
																					'#scenarioYtdTable')
																					.DataTable()
																					.clear()
																					.draw();
																		}
																	},
																	createdRow : function(
																			row,
																			data,
																			dataIndex) {

																		$compile(
																				angular
																						.element(row)
																						.contents())
																				($scope);
																	},
																	"columns" : [
																			// TODO:
																			// Scenario
																			// field
																			// should
																			// be
																			// updated
																			// here
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					$scope.teamName = row.teamName;
																					return '<div><span title="'
																							+ row.name
																							+ '" class="glyphicon glyphicon-info-sign" style="cursor: pointer;"></span></div>';

																				}
																			},
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.applicationName == null
																							|| row.applicationName == undefined) {
																						return '';
																					} else
																						return row.applicationName;
																				}
																			},
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.environmentName == null
																							|| row.environmentName == undefined) {
																						return '';
																					} else
																						return row.environmentName;
																				}

																			},
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.serverName == null
																							|| row.serverName == undefined) {
																						return '';
																					} else
																						return row.serverName;
																				}
																			},
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.softwareComponentName == null
																							|| row.softwareComponentName == undefined) {
																						return '';
																					} else
																						return row.softwareComponentName;
																				}
																			},
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.monkeyType == null
																							|| row.monkeyType == undefined) {
																						return '';
																					} else
																						return row.monkeyType;
																				}
																			},
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.monkeyStrategy == null
																							|| row.monkeyStrategy == undefined) {
																						return '';
																					} else
																						return row.monkeyStrategy;
																				}
																			},
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.eventStatusType == 'Completed') {
																						return '<div><span id="completed" class="badge">Completed</span></div>';
																					} else if (row.eventStatusType == 'Failed') {
																						return '<div><span id="failed" class="badge"> Failed</span></div>';
																					} else if (row.eventStatusType == 'Submitted') {
																						return '<div><span id="submitted" class="badge"> Submitted</span></div>';
																					} else if (row.eventStatusType == 'Scheduled') {
																						return '<div><span id="scheduled" class="badge"> Scheduled</span></div>';
																					} else if (row.eventStatusType == 'In Progress') {
																						return '<div><font color="red"><span class="mif-spinner4 mif-ani-spin"></span> In Progress</font></div>';
																					} else if (row.eventStatusType == 'Success') {
																						return '<div><span id="success" class="badge"> Success</span></div>';
																					} else if (row.eventStatusType == 'Rejected') {
																						return '<div><span id="rejected" class="badge"> Rejected</span></div>';
																					}

																				}
																			},
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.execStatus == null
																							|| row.execStatus == undefined) {
																						return '';
																					} else {
																						return row.execStatus;
																					}
																				}
																			},
																			{
																				"data" : "@timestamp",

																			},
																			{

																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.eventStatus == null
																							|| row.eventStatus == undefined) {
																						return '';
																					} else {
																						var txt = row.eventStatus
																								.replace(
																										/["']/g,
																										"");

																						return '<a ng-click="ViewRemarks(\''
																								+ txt
																								+ '\')">Remarks</a>';
																					}
																				}
																			}

																	// TODO:
																	// Duration
																	// time
																	// should be
																	// updated
																	// here

																	]

																});
												$('#scenarioYtdTable')
														.on('draw.dt',
																function() {
																	$(
																			'[data-toggle="tooltip"]')
																			.tooltip();
																});
											});

							// View Remarks can be viewed using popup
							$scope.ViewRemarks = function(remarks) {
								$scope.viewRemarks = remarks;
								loadPopup();
								return $scope.viewRemarks;
							}

							$(document).on('click', '.closed', function() {
								$("#myModal").removeClass('in');
								$rootScope.$apply();
								$(".modal-backdrop").remove();
							});

							function loadPopup() {
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
						} ]);