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
				'executionDashboardCtrl',
				[
						'$rootScope',
						'$scope',
						'resiliencyStudioService',
						'$parse',
						'applicationServices',
						'$location',
						'$http',
						'$compile',
						'$sce',
						'$timeout',
						'env',
						function($rootScope, $scope, resiliencyStudioService,
								$parse, applicationServices, $location, $http,
								$compile, $sce, $timeout, env) {
							$rootScope.sideBar = "toggled";// For sidebar(Left
															// ) Navigation

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							/*
							 * Initialize Multi select Drop down Filter data and
							 * model
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

							// To populate application name in dropdown
							resiliencyStudioService
									.getApplicationDetails()
									.then(
											function(data) {
												$scope.scenario = data;
												$scope.scenariosFilterData
														.push({
															'id' : "all",
															'label' : "ALL"
														});
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

																});
											});

							// Kibana url populate on iframe.
							$scope.getKibana = function() {
								$scope.showTable = false;
								var appName = $scope.scenariosFilterModel.id;
								var kibanaApp = "";
								if (appName === 'all') {
									kibanaApp = env.get('kibana').domain
											+ ':'
											+ env.get('kibana').port
											+ "/app/kibana#/dashboard/Resiliency-Studio?_a=(filters:!(),options:(darkTheme:!t),panels:!((col:1,id:resiliencyStudio-FailureStats,panelIndex:1,row:6,size_x:12,size_y:4,type:visualization),(col:1,id:resiliencyStudio-Execution-Application-New,panelIndex:2,row:3,size_x:4,size_y:3,type:visualization),(col:9,id:resiliencyStudio-Execution-Component-New,panelIndex:3,row:1,size_x:4,size_y:5,type:visualization),(col:5,id:resiliencyStudio-Execution-MonkeySplit-New,panelIndex:4,row:1,size_x:4,size_y:5,type:visualization),(col:1,id:resiliencyStudio_Events_Count,panelIndex:5,row:1,size_x:4,size_y:2,type:visualization)),query:(query_string:(analyze_wildcard:!t,query:'*')),title:resiliencyStudio,uiState:())&_g=(refreshInterval:(display:Off,pause:!f,value:0),time:(from:now-30d,mode:quick,to:now))"
								} else {
									kibanaApp = env.get('kibana').domain
											+ ':'
											+ env.get('kibana').port
											+ "/app/kibana#/dashboard/Resiliency-Studio?_a=(filters:!(),options:(darkTheme:!t),panels:!((col:1,id:resiliencyStudio-FailureStats,panelIndex:1,row:6,size_x:12,size_y:4,type:visualization),(col:1,id:resiliencyStudio-Execution-Application-New,panelIndex:2,row:3,size_x:4,size_y:3,type:visualization),(col:9,id:resiliencyStudio-Execution-Component-New,panelIndex:3,row:1,size_x:4,size_y:5,type:visualization),(col:5,id:resiliencyStudio-Execution-MonkeySplit-New,panelIndex:4,row:1,size_x:4,size_y:5,type:visualization),(col:1,id:resiliencyStudio_Events_Count,panelIndex:5,row:1,size_x:4,size_y:2,type:visualization)),query:(query_string:(analyze_wildcard:!t,query:'"
											+ appName
											+ "')),title:resiliencyStudio,uiState:())&_g=(refreshInterval:(display:Off,pause:!f,value:0),time:(from:now-30d,mode:quick,to:now))"
								}

								// To populate Kibana url dynamically on the
								// table
								$("#iframeId").attr('src', '');
								$timeout(function() {
									$("#iframeId").attr('src', kibanaApp);
								}, 100);
								$scope.showTable = true;
							}

						} ]);