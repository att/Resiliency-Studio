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
							/*var _base64 = {
									_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
									encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=_base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},
									decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9+/=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=_base64._utf8_decode(t);return t},
									_utf8_encode:function(e){e=e.replace(/rn/g,"n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},
									_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}
								}
							$http.defaults.headers.common.Authorization = "Basic " +_base64.encode($scope.username + '|' + ':' + 'LOGIN');*/
							
							$(document)
									.ready(
											function() {
												var table = $('#scenarioYtdTable')
														.DataTable(
																{
																	//"scrollX" : true,
																	"order" : [ [11,"desc" ] ],
																	
																	ajax : {

																		"dataSrc" : "",
																		type : "GET",
																		xhrFields : {
																			withCredentials : true
																		},
																		beforeSend: function (request) {
																            request.setRequestHeader("TokenAcess",  $rootScope.globeToken);
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
																	"dom": 'fl<"settingWrapper"><"scenarioYTDContainer"t>ip',
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
																					if (row.failureTenet == null
																							|| row.failureTenet == undefined) {
																						return '';
																					} else
																						return row.failureTenet;
																				}
																			},
																			{
																				"data" : function(
																						row,
																						type,
																						val,
																						meta) {
																					if (row.failureMode == null
																							|| row.failureMode == undefined) {
																						return '';
																					} else
																						return row.failureMode;
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
																					if (row.execDuration == null
																							|| row.execDuration == undefined) {
																						return '';
																					} else {
																						return row.execDuration;
																					}
																				}
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

																	],
																	"columnDefs": [
															            {
															                "targets": [ 0 ],
															                "visible": false,
															                "searchable": false
															            },
															            {
															                "targets": [ 2 ],
															                "visible": false,
															                "searchable": true
															            },
															            {
															                "targets": [ 4,6 ],
															                "width": "70px"
															            },
															            {
															                "targets": [ 5 ],
															                "visible": false,
															                "searchable": true
															            },
															            {
															                "targets": [7 ],
															                "visible": false,
															                "searchable": true
															            },
															            {
															                "targets": [8 ],
															                "visible": false,
															                "searchable": true
															            },
															            {
															                "targets": [ 12 ],
															                "visible": false,
															                "searchable": true
															            },
															            {
															                "targets": [ 13 ],
															                "searchable": false
															            }
															        ]

																});
												
												$('input.toggle-vis').on( 'click', function (e) {
													e.stopPropagation();
											        var column = table.column( $(this).attr('data-column') );
											        column.visible( !column.visible() );
											    });
												
												$('.dropdown-menu input, .dropdown-menu label').click(function(e) {
											        e.stopPropagation();
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

							setTimeout(function(){
								$( ".settingsBtn" ).insertBefore( $( ".settingWrapper" ) );
							},100);
							
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

