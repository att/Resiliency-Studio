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
				'viewApplicationCtrl',
				[
						'$rootScope',
						'$scope',
						'$filter',
						'resiliencyStudioService',
						'dialogs',
						'$http',
						'$location',
						'$parse',
						'$compile',
						'$sce',
						'$modal',
						'$window',
						'constantService',
						'$timeout',
						'env',
						'$cookies',
						function($rootScope, $scope, $filter,
								resiliencyStudioService, dialogs, $http,
								$location, $parse, $compile, $sce, $modal,
								$window, constantService, $timeout, env, $cookies) {

							$rootScope.sideBar = "toggled";

							var _env = {
								URL : env.get('env_url')
							}
							var table = "";

							if ($rootScope.UserPrivilege) {
								loadTable();
							} else {
								$location.path("/");
							}

							$timeout(function() {
								$("[data-hide]").on("click",function() {
													$(this).closest("."+ $(this).attr("data-hide"))
															.hide();
												});
							});

							function format(data) {
								// serverTable implementation

								if ($rootScope.UserPrivilege == 'user') {

									var html = "<div><table id='serverTable' class='table table-striped table-bordered' style='width:100%' > "
											+ "<thead>"
											+ "<tr>"
											+ "<th class='theadSim' width='5%'></th>"
											+ "<th class=theadSim>Server Name</th>"
											+ "<th class=theadSim>Role</th>"
											+ "<th class=theadSim>Host Name</th>"
											+ "<th class=theadSim>Operating system</th>"
											+ "<th class=theadSim colspan='2'>Operating system Type</th>"
											+ "<th class=theadSim>CPU</th>"
											+ "<th class=theadSim>Memory</th>"
											+ "<th class=theadSim>Storage</th>"
											+ "<th class=theadSim>IP Address</th>"
											+ "</tr>"
											+ "</thead>";

									// Assign server index ,application id,tier
									// name,environment name in table row of
									// each server
									for ( var environmentServer in data.environmentMap) {

										angular
												.forEach(
														data.environmentMap[environmentServer].serverList,
														function(value, key) {

															var cpuValue = "";
															if (value.cpu != null) {
																cpuValue = value.cpu;
															}
															var roles = "";
															if (value.roles != null) {
																roles = value.roles;
															}
															var availMemory = "";
															if (value.memory != null) {
																availMemory = value.memory
																		.toUpperCase();
																if (availMemory
																		.substr(-2) === "KB") {
																	availMemory = Math
																			.ceil(parseInt(availMemory)
																					/ (1024 * 1024))
																			+ 'GB';
																}
															}

															var availStorage = "";
															if (value.storage != null) {
																availStorage = value.storage
																		.toUpperCase();
															}

															var hostNameValue = "";
															if (value.hostName != null) {
																hostNameValue = value.hostName;
															}

															var osValue = "";
															if (value.operatingSystem != null) {
																osValue = value.operatingSystem;
															}

															var osType = "";
															if (value.operatingSystemType != null) {
																osType = value.operatingSystemType;
															}

															var ipAddressValue = "";
															if (value.ipAddress != null) {
																ipAddressValue = value.ipAddress;
															}

															var instanceValue = "";
															if (value.instanceName != null) {
																instanceValue = value.instanceName;
															}

															html = html
																	+ "<tr id="
																	+ key
																	+ " parent="
																	+ data.id
																	+ " map="
																	+ environmentServer
																	+ "> <td class='details-control'> <span title='Click to expand' class='fa fa-plus-circle'></span> </td>"
																	+ "<td>"
																	+ instanceValue
																	+ "</td>"
																	+ "<td>"
																	+ roles
																	+ "</td>"
																	+ "<td>"
																	+ hostNameValue
																	+ "</td>"
																	+ "<td>"
																	+ osValue
																	+ "</td>"
																	+ "<td colspan='2'>"
																	+ osType
																	+ "</td>"
																	+ "<td>"
																	+ cpuValue
																	+ "</td>"
																	+ "<td>"
																	+ availMemory
																	+ "</td>"
																	+ "<td>"
																	+ availStorage
																	+ "</td>"
																	+ "<td>"
																	+ ipAddressValue
																	+ "</td>"
																	+ "</tr>";

														});
									}
									html = html + "</table></div>";
									return html;
								} // function format(data) end.

								else if ($rootScope.UserPrivilege == 'power'
										|| $rootScope.UserPrivilege == 'admin') {

									var htmlPowerAdmin = "<div><table id='serverTable' class='table table-striped table-bordered' style='width:100%' > "
											+ "<thead>"
											+ "<tr>"
											+ "<th class='theadSim' width='5%'></th>"
											+ "<th class=theadSim>Server Name</th>"
											+ "<th class=theadSim>Role</th>"
											+ "<th class=theadSim>Host Name</th>"
											+ "<th class=theadSim>Operating system</th>"
											+ "<th class=theadSim>Operating system Type</th>"
											+ "<th class=theadSim>CPU</th>"
											+ "<th class=theadSim>Memory</th>"
											+ "<th class=theadSim>Storage</th>"
											+ "<th class=theadSim>IP Address</th>"
											+ "<th class=theadSim width='5%' id='test'>Edit</th>"
											+ "</tr>" + "</thead>";

									// Assign server index ,application id,tier
									// name,environment name in table row of
									// each server
									if(data != undefined) {
									for ( var environment in data.environmentMap) {
										angular
												.forEach(
														data.environmentMap[environment].serverList,
														function(value, key) {
															var cpuValue = "";
															if (value.cpu != null) {
																cpuValue = value.cpu;
															}
															var roles = "";
															if (value.roles != null) {
																roles = value.roles;
															}
															var availMemory = "";
															if (value.memory != null) {
																availMemory = value.memory
																		.toUpperCase();
																if (availMemory
																		.substr(-2) === "KB") {
																	availMemory = Math
																			.ceil(parseInt(availMemory)
																					/ (1024 * 1024))
																			+ 'GB';
																}
															}

															var availStorage = "";
															if (value.storage != null) {
																availStorage = value.storage
																		.toUpperCase();
															}

															var hostNameValue = "";
															if (value.hostName != null) {
																hostNameValue = value.hostName;
															}

															var osValue = "";
															if (value.operatingSystem != null) {
																osValue = value.operatingSystem;
															}

															var osType = "";
															if (value.operatingSystemType != null) {
																osType = value.operatingSystemType;
															}

															var ipAddressValue = "";
															if (value.ipAddress != null) {
																ipAddressValue = value.ipAddress;
															}

															var instanceValue = "";
															if (value.instanceName != null) {
																instanceValue = value.instanceName;
															}

															htmlPowerAdmin = htmlPowerAdmin
																	+ "<tr id="
																	+ key
																	+ " parent="
																	+ data.id
																	+ " map="
																	+ environment
																	+ "> <td class='details-control'> <span title='Click to expand' class='fa fa-plus-circle'></span> </td>"
																	+ "<td>"
																	+ instanceValue
																	+ "</td>"
																	+ "<td>"
																	+ roles
																	+ "</td>"
																	+ "<td>"
																	+ hostNameValue
																	+ "</td>"
																	+ "<td>"
																	+ osValue
																	+ "</td>"
																	+ "<td>"
																	+ osType
																	+ "</td>"
																	+ "<td>"
																	+ cpuValue
																	+ "</td>"
																	+ "<td>"
																	+ availMemory
																	+ "</td>"
																	+ "<td>"
																	+ availStorage
																	+ "</td>"
																	+ "<td>"
																	+ ipAddressValue
																	+ "</td>"
																	+ "<td><p data-placement='top' data-toggle='tooltip' title='Edit'><button class='btn btn-primary btn-xs edbutton' type='button' id='editButton'><span class='glyphicon glyphicon-pencil'></span></button></p></td>"
																	+ "</tr>";

														});
									}
									}
									htmlPowerAdmin = htmlPowerAdmin + "</table></div>";
									return htmlPowerAdmin;
								} // function format(data) end.

							}

							$.fn.dataTableExt.sErrMode = "console";

							$.fn.dataTableExt.oApi._fnLog = function(oSettings,
									iLevel, sMesg, tn) {
								var sAlert = (oSettings === null) ? "DataTables warning: "
										+ sMesg
										: "DataTables warning (table id = '"
												+ oSettings.sTableId + "'): "
												+ sMesg;

								if (tn) {
									sAlert += ". For more information about this error, please see "
											+ "http://datatables.net/tn/" + tn;
								}

								if (iLevel === 0) {
									if ($.fn.dataTableExt.sErrMode == "alert") {
										alert(sAlert);
									} else if ($.fn.dataTableExt.sErrMode == "thow") {
										throw sAlert;
									} else if ($.fn.dataTableExt.sErrMode == "console") {
										console.log(sAlert);
									} else if ($.fn.dataTableExt.sErrMode == "mute") {
										console.log(sAlert);
									}

									return;
								} else if (console !== undefined && console.log) {
									console.log(sAlert);
								}
							}

							// Application Table Implementation
							function loadTable() {

								table = $('#fdApplicationTable')
										.DataTable(
												{
													"ajax" : {
														"url" : _env.URL
																+ constantService.constants.viewApplicationTeamInfo,
														"dataSrc" : "",
														type : "GET",
														xhrFields : {
															withCredentials : true
														},
														contentType : "application/json",
														crossDomain : true,

														"error" : function(jqXHR,textStatus,errorThrown) {
															$('#fdApplicationTable').DataTable().clear().draw();
														}

													},

													"columns" : [
															{
																"className" : 'details-control',
																"data" : null,
																"defaultContent" : '<span title="Click to expand" id="expandId" class="fa fa-plus-circle"></span>',
																"orderable" : false,
																"targets" : 0,
																"bSortable" : false
															},
															{
																"data" : "applicationName"
															},
															{
																"data" : "category"
															},
															{
																"data" : function(row,type,val,meta) {
																	var keys = "";
																	if (row != null && row.environmentMap != null) {

																		keys = Object.keys(row.environmentMap)[0];
																		return row.environmentMap[keys].name;
																	} else {
																		return "";
																	}
																}
															},
															{
																"data" : function(row,type,val,meta) {
																	if (row.networkArchitectureURL != null
																			&& row.networkArchitectureURL.length > 0) { // networkArchitectureDiagram
																		$scope.appName = row.applicationName;
																		return "<div><a class='thumbnail' href='javascript:void(0)'><span class='hide'>"
																				+ row.applicationName
																				+ "</span><img src="
																				+ row.networkArchitectureURL
																				+ " alt='Architecture Diagram'></div></a>";
																	} else {
																		$scope.appName = "";
																		return "Architecture Diagram is not available.";
																	}
																}
															},
															{
																data : function(row,type,val,meta) {

																	if (row != null
																			&& row != undefined
																			&& $rootScope.UserPrivilege != 'power'
																			&& $rootScope.UserPrivilege != 'user') {

																		return "<p data-placement='top' data-toggle='tooltip' title='Delete'>"
																				+ "<button class='btn btn-primary btn-xs appDelete' type='button' name="
																				+ row.applicationName
																				+ " id="
																				+ row.id
																				+ ">"
																				+ "<span id='applicationDelete' class='glyphicon glyphicon-trash'></span></button></p>";
																	} else {
																		return '';
																	}

																},
																orderable : false
															} ],
													"columnDefs" : [ {
														"targets" : [ 5 ],
														"visible" : $rootScope.UserPrivilege == 'admin'
													} ]
												});

							}

							$(document).ready(function() {
								if ($rootScope.UserPrivilege) {
									loadTable();
								}

							});

							$('#fdApplicationTable tbody').on('click', 'td.details-control', function(){
												var tr = $(this).closest('tr');
												var row = table.row(tr);
												var currentElem = $(this);
												/*table.rows()
														.every(function() {
																	// Checking
																	// child row
																	// already
																	// exsists
																	if (this.child.isShown() && !$(this.node()).is($(currentElem).parent())) {
																		var tr = $(this.node());
																		this.child.hide();
																		$(tr).find('.fa')
																				.removeClass('fa-minus-circle');
																	};
																});*/
												
												// Checking row display and make
												// css changes
												if (row.child.isShown()) {
													row.child.hide();
													tr.removeClass('shown');
												} else {
													// Adding new child row for
													// application
													row.child(format(row.data())).show();
													tr.addClass('shown');
												}
											});

							// For Architecture diagram Model pop up display
							$(document)
									.on(
											'click',
											'.thumbnail',
											function() {
												var titleName = $(this).find(
														'.hide').text();
												var imageSrc = $(this).find(
														'img').attr('src');
												$("#image-gallery").find(
														".img-responsive")
														.attr('src', imageSrc);
												$('.modal-title')
														.text(
																titleName
																		+ " Architecture Diagram");
												$("#image-gallery").modal({
													"backdrop" : "static",
													"keyboard" : true,
													"show" : true
												});
											});

							// event listener for closing parent app level table
							$('#fdApplicationTable tbody').on(
									'click',
									'td.details-control',
									function() {
										$(this).find('span:first').toggleClass(
												'fa-minus-circle', 1000);
									});

							$scope.softwareComponent = [];
							$scope.credentials = {
								username : "",
								privatekey : ""
							}
							$scope.monitor = {
								counterType : "",
								monitorAPI : ""
							}
							$scope.logs = {
								logType : "",
								location : ""
							}

							// Loading tab data by filtering using application
							// id,tier name, environment name and row index
							$scope.loadTabData = function(rowId, parentRowId,
									length, map) {
								var table1 = $('#fdApplicationTable')
										.dataTable().api().rows();
								$scope.serverData = table1.rows().data();
								var obj = $filter('filter')($scope.serverData,
										{
											'id' : parentRowId
										}, true)[0];
								if (obj.environmentMap != null) {
									Object.keys(obj.environmentMap)[0];

									if (obj.environmentMap[map] != null
											&& obj.environmentMap[map] != undefined) {
										var envObj = obj.environmentMap[map];

										if (envObj.serverList != null
												&& envObj.serverList[rowId] != undefined) {
											var currentServerObj = envObj.serverList[rowId];

											// DataTable creation configuration
											// for inner Tab

											// Credential tab level
											// implementation
											var credentials = [];
											credentials.push(currentServerObj);
											$("#tab1default" + length)
													.find('table')
													.DataTable(
															{
																"paging" : false,
																"searching" : false,
																data : credentials,
																"columns" : [
																		{
																			data : function(
																					row,
																					type,
																					val,
																					meta) {
																				return row.userName;
																			},
																			orderable : false
																		},
																		{
																			data : function(
																					row,
																					type,
																					val,
																					meta) {
																				var txtPwd = "";
																				if (row.password != null
																						&& row.password.length > 0) {
																					for (var i = 0; i < row.password.length; i++) {
																						txtPwd += "*";
																					}
																				}
																				return txtPwd;
																			},
																			orderable : false
																		},
																		{
																			data : function(
																					row,
																					type,
																					val,
																					meta) {
																				var pkey = "";
																				if (row.privatekey != null
																						&& row.privatekey.length > 0) {
																					if(row.privatekey.length < 10) {
																						for (var i = 0; i < row.privatekey.length; i++) {
																							pkey += "*";
																						}
																					} else {
																						pkey = "**********";
																					}
																					return pkey;
																				}
																				return pkey;
																			},
																			orderable : false
																		},
																		{
																			data : function(
																					row,
																					type,
																					val,
																					meta) {
																				if (row.userName != null
																						&& $rootScope.UserPrivilege != 'user') {
																					return "<p data-placement='top' data-toggle='tooltip' title='Edit'>"
																							+ "<button class='btn btn-primary btn-xs tabEdit_new' type='button' >"
																							+ "<span class='glyphicon glyphicon-pencil'></span></button></p>";
																				}
																			},
																			orderable : false
																		}, ],

																"columnDefs" : [ {
																	"targets" : [ 3 ],
																	"visible" : $rootScope.UserPrivilege != 'user'
																} ]
															});

											// Software component tab level
											// implementation
											var softwareComponents = $scope
													.checkIsValid(currentServerObj.swComps);

											$("#tab2default" + length)
													.find('table')
													.DataTable(
															{
																data : softwareComponents,
																columns : [
																		{
																			data : "serverComponentName",
																			orderable : false
																		},
																		{
																			data : "processName",
																			orderable : false
																		},

																		{
																			data : function(
																					row,
																					type,
																					val,
																					meta) {
																				if ($rootScope.UserPrivilege != 'user'
																						&& meta != null
																						&& meta.row != undefined) {
																					return "<p data-placement='top' data-toggle='tooltip' title='Edit'>"
																							+ "<button class='btn btn-primary btn-xs tabEdit' type='button' id="
																							+ meta.row
																							+ " >"
																							+ "<span class='glyphicon glyphicon-pencil'></span></button></p>";
																				} else if ($rootScope.UserPrivilege) {
																					return '';
																				}
																			},
																			orderable : false
																		}, ],

																"columnDefs" : [ {
																	"targets" : [ 2 ],
																	"visible" : $rootScope.UserPrivilege != 'user'
																} ],
																"scrollCollapse" : true,
																"searching" : false,
																"paging" : false,
																"order": []
															});

											// Discovery tab level
											// implementation
											/*var discoveryComponents = $scope
													.checkIsValid(currentServerObj.discoveryApi);
											$("#tab3default" + length)
													.find('table')
													.DataTable(
															{
																data : discoveryComponents,

																columns : [
																		{
																			data : "discoveryApiDescription",
																			orderable : false
																		},
																		{
																			data : "discoveryApi",
																			orderable : false
																		},
																		{
																			data : function(
																					row,
																					type,
																					val,
																					meta) {
																				if ($rootScope.UserPrivilege != 'user') {
																					return "<p data-placement='top' data-toggle='tooltip' title='Edit'>"
																							+ "<button class='btn btn-primary btn-xs tabEdit' type='button' >"
																							+ "<span class='glyphicon glyphicon-pencil'></span></button></p>";
																				} else if ($rootScope.UserPrivilege) {
																					return '';
																				}

																			},
																			orderable : false
																		},

																],
																"columnDefs" : [ {
																	"targets" : [ 2 ],
																	"visible" : $rootScope.UserPrivilege != 'user'
																} ],
																"scrollCollapse" : true,
																"searching" : false,
																"paging" : false
															});*/

											// Monitor tab level implementation
											var monitorComponents = $scope
													.checkIsValid(currentServerObj.monitor);
											$("#tab3default" + length)
													.find('table')
													.DataTable(
															{
																data : monitorComponents,
																columns : [
																		{
																			data : "counterType",
																			orderable : false
																		},
																		{
																			data : "monitorApi",
																			orderable : false
																		},
																		{
																			data : function(
																					row,
																					type,
																					val,
																					meta) {
																				if ($rootScope.UserPrivilege != 'user') {
																					return "<p data-placement='top' data-toggle='tooltip' title='Edit'>"
																							+ "<button class='btn btn-primary btn-xs tabEdit' type='button' >"
																							+ "<span class='glyphicon glyphicon-pencil'></span></button></p>";
																				} else if ($rootScope.UserPrivilege) {
																					return '';
																				}
																			},
																			orderable : false
																		}, ],

																"columnDefs" : [ {
																	"targets" : [ 2 ],
																	"visible" : $rootScope.UserPrivilege != 'user'
																} ],
																"scrollCollapse" : true,
																"searching" : false,
																"paging" : false,
																"order":[]
															});

											// Log tab level implementation
											var logComponents = $scope
													.checkIsValid(currentServerObj.log);
											$("#tab4default" + length)
													.find('table')
													.DataTable(
															{
																data : logComponents,
																columns : [
																		{
																			data : "logType",
																			orderable : false
																		},
																		{
																			data : "logLocation",
																			orderable : false
																		},
																		{
																			data : function(
																					row,
																					type,
																					val,
																					meta) {
																				if ($rootScope.UserPrivilege != 'user') {
																					return "<p data-placement='top' data-toggle='tooltip' title='Edit'>"
																							+ "<button class='btn btn-primary btn-xs tabEdit' type='button' >"
																							+ "<span class='glyphicon glyphicon-pencil'></span></button></p>";
																				} else if ($rootScope.UserPrivilege) {
																					return '';
																				}
																			},
																			orderable : false
																		}, ],
																"columnDefs" : [ {
																	"targets" : [ 2 ],
																	"visible" : $rootScope.UserPrivilege != 'user'
																} ],
																"scrollCollapse" : true,
																"searching" : false,
																"paging" : false,
																"order":[]
															});
										}
									}
								}
							};
							// For dynamic Tab Creation getting old tab and
							// splicing its old id.
							$scope.returnId = function(name) {
								if (typeof Number(name.substr(name.length - 1)) == "number"
										&& !isNaN(Number(name
												.substr(name.length - 1)))) {
									name = name.slice(0, -1);
								}
								return name;
							}

							// Appending new Id for dynamic tab Creation and
							// nested Childs
							// To avoid Same Child references
							$scope.appendTabParentId = function() {
								var visibleTabLength = $("#fdApplicationTable #serverChild").length;
								$('#tab_content a[href*="tab"]').each(function() {
													var hrefStr = $scope.returnId($(this).attr("href"));
													$(this).attr("href",hrefStr + visibleTabLength);
												});

								$('#tab_content .panel-body div[id*="tab"]')
										.each(function() {
													$(this).find("table")
															.DataTable()
															.destroy();
													var tableIdStr = $scope
															.returnId($(this)
																	.find("table")
																	.attr("id"));
													var idStr = $scope.returnId($(this).attr("id"));
													$(this).attr("id",idStr	+ visibleTabLength);
													$(this).find("table")
															.attr("id",tableIdStr + visibleTabLength);
												});
								return visibleTabLength;
							};

							// For Single child Server Display at a time.
							$scope.hideChildServers = function(name,currentElem) {
								$('tr[parent ="' + name + '"]')
										.each(function() {
													var nextElem = $(this)
															.next(serverChild);
													if ($(nextElem).hasClass('show') && !$(this).is($(currentElem).parent())) {
														$(this).find('.fa')
																.removeClass('fa-minus-circle');
														$(nextElem).addClass('hide').removeClass('show');
													}
												});
							}

							// To open Inner Child Server Click Event is added
							// By retrieving the assigned server
							// index,application id,tier name,environment map
							// name
							$(document)
									.on('click',
											'#serverTable tbody td.details-control',
											function(event) {
												var innerData = $('#tab_content').html();
												event.stopImmediatePropagation();

												var rowId = $(this).closest("tr").attr("id");
												var parentRowId = $(this).closest("tr").attr("parent");
												var map = $(this).closest("tr").attr("map");
												$(this).closest('table');
												var nextElement = $(this).closest("tr").next();

												$scope.hideChildServers(parentRowId, this);
												if ($(nextElement).attr('id') == 'serverRowChild') {
													if ($(nextElement).hasClass('show')) {
														$(nextElement).removeClass('show')
																.addClass('hide');
													} else {
														$(nextElement)
																.removeClass('hide')
																.addClass('show');
													}
												} else {
													var length = $scope.appendTabParentId();
													$scope.loadTabData(rowId,
															parentRowId,
															length, map);
													$("#tab_content #serverChild")
															.attr('childId',rowId);
													$("#tab_content #serverChild")
															.attr('parent',parentRowId);
													$("#tab_content #serverChild")
															.attr('map', map);
													$("#tab_content #serverChild")
															.innerWidth($(this)
																			.closest("tr")
																			.innerWidth() - 30);
													var tabHtml = $("#tab_content table tbody ").html();
													$(tabHtml).insertAfter(
															$(this).closest("tr"));
													$(this).closest("tr")
															.next().addClass("show");

												}
												$('#tab_content').html(innerData);

											});

							// To get Edit Server values
							$scope.getServerEditValues = function(currentRow,
									parentElem, attr) {
								$scope.rowData = $('#fdApplicationTable')
										.dataTable().api().rows().data();
								var rowId = $(currentRow).closest(parentElem)
										.attr(attr);
								var parentId = $(currentRow)
										.closest(parentElem).attr("parent");
								var map = $(currentRow).closest("tr").attr(
										"map");
								var obj = $filter('filter')($scope.rowData, {
									'id' : parentId
								}, true)[0];
								var currentServerObj = obj.environmentMap[map].serverList[rowId];
								return {
									"parentId" : parentId,
									"serverIndex" : rowId,
									"envName" : map
								};
							}

							// To get Component Edit Values
							$scope.getComponentEditValues_new = function(
									currentRow, parentElem, attr) {
								$scope.rowData = $('#fdApplicationTable')
										.dataTable().api().rows().data();
								var rowId = $(currentRow).closest(parentElem)
										.attr(attr);
								var parentId = $(currentRow)
										.closest(parentElem).attr("parent");
								var map = $(currentRow).closest(parentElem)
										.attr("map");
								var obj = $filter('filter')($scope.rowData, {
									'id' : parentId
								}, true)[0];
								obj.environmentMap[map].serverList[rowId];
								var tabId = $(currentRow).closest('.tab-pane')
										.attr('id').match(/[^\d]+|\d+/g);
								var tabRowId = $(currentRow).closest('tr')
										.index();
								return {
									"parentId" : parentId,
									"serverIndex" : rowId,
									"envName" : map,
									"tabId" : tabId[0],
									"tabRowId" : tabRowId
								};
							}
							$scope.getComponentEditValues = function(
									currentRow, parentElem, attr) {
								$scope.rowData = $('#fdApplicationTable')
										.dataTable().api().rows().data();
								var rowId = $(currentRow).closest(parentElem)
										.attr(attr);
								var parentId = $(currentRow)
										.closest(parentElem).attr("parent");
								var map = $(currentRow).closest(parentElem)
										.attr("map");
								var obj = $filter('filter')($scope.rowData, {
									'id' : parentId
								}, true)[0];
								obj.environmentMap[map].serverList[rowId];
								var tabId = $(currentRow).closest('.tab-pane')
										.attr('id').match(/[^\d]+|\d+/g);
								var tabRowId = $(currentRow).closest('tr')
										.index();
								return {
									"parentId" : parentId,
									"serverIndex" : rowId,
									"envName" : map,
									"tabId" : tabId[1],
									"tabRowId" : tabRowId
								};
							}

							// To get Delete Server values
							$scope.getServerDeleteValues = function(currentRow,
									parentElem, attr) {
								$scope.rowData = $('#fdApplicationTable')
										.dataTable().api().rows().data();
								var rowId = $(currentRow).closest(parentElem)
										.attr(attr);
								var parentId = $(currentRow)
										.closest(parentElem).attr("parent");
								var map = $(currentRow).closest("tr").attr(
										"map");
								var obj = $filter('filter')($scope.rowData, {
									'id' : parentId
								}, true)[0];
								obj.environmentMap[map].serverList[rowId];
								return {
									"parentId" : parentId,
									"serverIndex" : rowId,
									"envName" : map
								};
							}

							// If clicks edit function, this method will invoke.
							$(document)
									.on('click',
											'.edbutton',
											function() {
												var dataValue = $scope
														.getServerEditValues(
																this, "tr","id");
												if (dataValue.envName != "null"
														&& dataValue.envName.length > 0
														&& dataValue.envName != undefined
														&& dataValue.envName != null)
													$location
															.path('/edit-application/'
																	+ dataValue.parentId
																	+ '/'
																	+ dataValue.envName
																	+ '/'
																	+ dataValue.serverIndex);
												else
													$location
															.path('/edit-application/'
																	+ dataValue.parentId);
												$rootScope.$apply();
											});

							// Component Edit Event is addded.
							$(document)
									.on('click',
											'.tabEdit',
											function() {
												var dataValue = $scope
														.getComponentEditValues(
																this,"#serverChild","childId");
												if (dataValue.envName != "null"
														&& dataValue.envName.length > 0
														&& dataValue.envName != undefined
														&& dataValue.envName != null)
													$location
															.path('/edit-application/'
																	+ dataValue.parentId
																	+ '/'
																	+ dataValue.envName
																	+ '/'
																	+ dataValue.serverIndex
																	+ '/'
																	+ dataValue.tabId
																	+ '/'
																	+ dataValue.tabRowId);
												else
													$location
															.path('/edit-application/'
																	+ dataValue.parentId);
												$rootScope.$apply();
											});
							
							$(document)
							.on('click',
									'.tabEdit_new',
									function() {
										var dataValue = $scope
												.getComponentEditValues_new(
														this,"#serverChild","childId");
										if (dataValue.envName != "null"
												&& dataValue.envName.length > 0
												&& dataValue.envName != undefined
												&& dataValue.envName != null)
											$location
													.path('/edit-application/'
															+ dataValue.parentId
															+ '/'
															+ dataValue.envName
															+ '/'
															+ dataValue.serverIndex
															+ '/'
															+ dataValue.tabId
															+ '/'
															+ dataValue.tabRowId);
										else
											$location
													.path('/edit-application/'
															+ dataValue.parentId);
										$rootScope.$apply();
									});

							// To get Delete App values
							$scope.getappDeleteValues = function(appId, appNAME) {

								resiliencyStudioService.deleteapplicationbyid(
										appId).then(function(data) {

									$('.alert').show();
									$scope.successDeleteApp = true;
									$scope.errorDeleteApp = false;
									$scope.deletedApp = appNAME;
									$('body, html').animate({
										scrollTop : $('body').offset().top
									}, 'fast');

								}, function(error) {

									$('.alert').show();
									$scope.errorDeleteApp = true;
									$('body, html').animate({
										scrollTop : $('body').offset().top
									}, 'fast');
								});
								return "";
							}

							// Application level delete
							$('#wrapper')
									.find('.panel')
									.find('#fdApplicationTable_wrapper')
									.on('click',
											'.appDelete',
											function() {
												var appId = $(this).attr('id');
												var appNAME = "";
												resiliencyStudioService
														.getapplicationbyid(
																appId)
														.then(
																function(data) {
																	appNAME = data.applicationName;
																},
																function(error) {
																	console.log("error occured while application name");
																});
												var confirmdialog = dialogs.confirm("Delete","Are you sure want to delete" + appNAME,"yes");
												confirmdialog.result
														.then(function(btn) {
															$scope.getappDeleteValues(appId,appNAME);
															
															($(this)
																	.closest('table')
																	.find('caption')
																	.text())
																	.toLowerCase();
															$(this)
																	.closest('#serverChild')
																	.attr("childid");
															var tableId = $(this)
																	.closest('table')
																	.attr('id');
															var tr = $(this)
																	.closest('tr')
																	.index();

															$("#" + tableId + "_filter").remove();
															$("#" + tableId + "_info").remove();
															$("#" + tableId).dataTable().api().row(tr).remove().draw();

															$('.modal').css('display','none');
															$('body').removeClass('modal-open');
															$('.modal-backdrop')
																	.remove();

															$timeout(function() {

																		table.ajax
																				.reload();
																	}, 1000);
														});

											});

							$(document)
									.on('click',
											'.tabDelete',
											function() {
												var tabDelete = true
												// redirect to edit page
												var dataValue = $scope
														.getComponentEditValues(
																this,
																"#serverChild",
																"childId");
												if (dataValue.envName != "null"
														&& dataValue.envName.length > 0
														&& dataValue.envName != undefined
														&& dataValue.envName != null)
													$location
															.path('/delete-application/'
																	+ dataValue.parentId
																	+ '/'
																	+ dataValue.envName
																	+ '/'
																	+ dataValue.serverIndex
																	+ '/'
																	+ dataValue.tabId
																	+ '/'
																	+ dataValue.tabRowId
																	+ '/'
																	+ tabDelete);
												else
													$location
															.path('/delete-application/'
																	+ dataValue.parentId
																	+ '/'
																	+ tabDelete);
												$rootScope.$apply();
											});

							// Server delete
							$(document)
									.on(
											'click',
											'.serverDelete',
											function() {
												var servDelete = true;
												var dataValue = $scope
														.getServerDeleteValues(
																this, "tr",
																"id");
												if (dataValue.envName != "null"
														&& dataValue.envName.length > 0
														&& dataValue.envName != undefined
														&& dataValue.envName != null)
													$location
															.path('/delete-application/'
																	+ dataValue.parentId
																	+ '/'
																	+ dataValue.envName
																	+ '/'
																	+ dataValue.serverIndex
																	+ '/'
																	+ servDelete);
												else
													$location
															.path('/delete-application/'
																	+ dataValue.parentId
																	+ '/'
																	+ servDelete);
												$rootScope.$apply();

												
												$(this).closest('tr').next()
														.remove();
												$(this).closest('tr').remove();

											});

							$scope.checkIsValid = function(arrObj) {

								angular.forEach(arrObj,
										function(Objval, ObjKey) {

											var checkValidObj = function() {
												var isValid = false;
												var keys = Object.keys(Objval);
												keys.forEach(function(key) {
													if (Objval[key]) {
														isValid = true;
													}
												});
												return isValid;
											}

											var val = checkValidObj();

											if (!val) {
												arrObj.splice(ObjKey, 1);
											}

										});
								return arrObj;

							};

						} ]);
