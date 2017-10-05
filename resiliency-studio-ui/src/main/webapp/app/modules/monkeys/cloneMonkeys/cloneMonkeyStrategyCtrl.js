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

'use strict';

app
		.controller(
				'cloneMonkeyStrategyCtrl',
				[
						'$rootScope',
						'$scope',
						'$routeParams',
						'$parse',
						'$location',
						'$http',
						'$compile',
						'$sce',
						'resiliencyStudioService',
						'$timeout',
						function($rootScope, $scope, $routeParams, $parse,
								$location, $http, $compile, $sce,
								resiliencyStudioService, $timeout) {

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							// Getting Clone monkey object from session storage.
							var cloneMonkey = JSON.parse(sessionStorage
									.getItem("cloneMonkey"));
							$scope.fields = angular.copy(cloneMonkey);

							$rootScope.sideBar = "toggled";
							$scope.scriptName = 'View Script';// Initially
																// button name
																// under upload
																// file text box
							$scope.btnScriptVal = 'Code Script';// Button title
																// when radio
																// value is code
																// script
							$scope.value = "upload";//
							$scope.cloneScript = $scope.fields.monkeyStrategyScript;// To
																					// copy
																					// script
																					// content
																					// into
																					// scope
							$scope.enableRadio = true;// Initially radio
														// button enable towards
														// upload script
							$scope.monkeyWriteScript = null;
							$scope.showSuccessMessage = false;// Initially
																// inline
																// success
																// message flag
							$scope.showErrorMessage = false;// Initially inline
															// error message
															// flag
							$scope.scriptValidation = false;// Initially script
															// validation
															// message is hide
							$scope.radioVal = 'upload';
							$scope.scriptContent = $scope.fields.monkeyStrategyScript;// Initially
																						// cloned
																						// monkey
																						// script
																						// content
																						// copied
																						// into
																						// scope
							$scope.fields.monkeyStrategyName = $scope.fields.monkeyStrategyName
									+ "Clone";// Cloned monkey name should
												// ends with 'Clone'
							$scope.fields.monkeyStrategyVersion = 0.1;// Initially
																		// cloned
																		// monkey
																		// version

							// To prevent space in strategy input, close the
							// inline messages and validate advanced tab based
							// on UserPrivilege(i.e: admin for now)
							$timeout(
									function() {
										if ($rootScope.UserPrivilege != 'admin') {
											$("#osTypeId").removeAttr(
													'required');
											$("#flavourAddFPId").removeAttr(
													'required');
										}
										$('#strategyName').on('keypress',
												function(e) {
													if (e.which == 32)
														return false;
												});
										$("[data-hide]")
												.on(
														"click",
														function() {
															$(this)
																	.closest(
																			"."
																					+ $(
																							this)
																							.attr(
																									"data-hide"))
																	.hide();
														});
									}, 1000);

							// To Populate flavor/failure subcategory dropdown
							// values
							resiliencyStudioService.getFailures().then(
									function(failures) {
										$scope.failures = failures;
										// To auto select Flavor
										$scope.getFlavors(false);
										$scope.getSubCategory();
									}, function(error) {
										console.log(error)
									});

							// Populate monkey types into monkey type dropdown
							resiliencyStudioService.getMonkeyTypes().then(
									function(allMonkeys) {
										$scope.monkeys = allMonkeys;
									},
									function(error) {
										dialogs.error("Error",
												"monkey retrieved error.",
												error);
									});

							if ($scope.fields.defaultFlag === 'Y') {
								$scope.defaultFlagValue = true;
							} else {
								$scope.defaultFlagValue = false;
							}

							if ($scope.fields.generic === 'Y') {
								$scope.GenericFlag = true;
								$scope.mandatoryFields = true;
							} else {
								$scope.GenericFlag = false;
								$scope.mandatoryFields = false;
							}

							// Script File Validation while doing upload script
							$scope.uploadScript=function(){
								$scope.invalidfile = false;
								$('.input-file-wrapper').val('');
								}
								$scope.uploadFile = function(files) {

								var ext = files[0].name.match(/\.(.+)$/)[1];

								if($scope.fields.scriptTypeCategory=="UNIX Script" || $scope.fields.scriptTypeCategory=="Windows/DOS Script"){

								if (angular.lowercase(ext) === 'sh') {
								$scope.invalidfile = false;
								var fd = new FormData();
								fd.append("file", files[0]);
								} else {
									$scope.scriptValidation = false;
								$scope.invalidfile = true;
								}

								}
								else if($scope.fields.scriptTypeCategory=="Ansible Script"){

								if (angular.lowercase(ext) === 'yml') {
								$scope.invalidfile = false;
								var fd = new FormData();
								fd.append("file", files[0]);
								} else {
									$scope.scriptValidation = false;
								$scope.invalidfile = true;
								}

								}

								}

							// Show script content in a popup window
							$scope.displayFileContents = function(contents) {
								// $scope.results = contents;
								$scope.scriptContent = contents;
								$scope.cloneScript = contents;
							};

							// When Generic flag clicks
							$scope.genericFlagSwitch = function(flag) {
								$scope.fields.genericFlag = $scope.GenericFlag;
								if (flag) {
									$scope.GenericFlag = true;
									$("#failureSubCategoryAddFPId").attr(
											'required', true);
									$("#failureCategoryAddFPId").attr(
											'required', true);
									$("#osTypeId").attr('required', true);
									$("#flavourAddFPId").attr('required', true);
									$scope.mandatoryFields = true;
								} else {
									$scope.GenericFlag = false;
									$("#failureSubCategoryAddFPId").removeAttr(
											'required');
									$("#failureCategoryAddFPId").removeAttr(
											'required');
									$("#osTypeId").removeAttr('required');
									$("#flavourAddFPId").removeAttr('required');
									$("#genericFlagId").removeAttr('checked');
									$scope.mandatoryFields = false;
								}
							}

							// When default flag clicks
							$scope.defaultFlagSwitch = function() {
								$scope.defaultFlagValue = !$scope.defaultFlagValue;
							}

							// To Populate flavors based on selected OS Types
							$scope.getFlavors = function() {
								$scope.options1 = $scope.failures.osType;
								$scope.options2 = [];
								var key = $scope.options1
										.indexOf($scope.fields.osType);
								var myNewOptions = $scope.failures.flavors[key];
								$scope.flavors = myNewOptions;
								$scope.fields.flavor = $scope.flavors[0];
							}

							// To Populate failureSubCategory based on selected
							// failureCategory
							$scope.getSubCategory = function() {
								$scope.category = $scope.failures.failureCategory;
								$scope.subcategory = [];
								var key = $scope.category
										.indexOf($scope.fields.failureCategory);
								var mySubCategory = $scope.failures.failureSubCateogy[key];
								$scope.failureSubCategories = mySubCategory;
								if (key == 1) {
									$scope.fields.failureSubCategory = mySubCategory[0]
								}
							}

							// When radio button clicks
							$scope.radioCheck = function(val) {
								$scope.radioVal = val;
							}
							$scope.radioSwitch = function(value) {
								if (value == 'upload') {
									$scope.enableRadio = true
								}
								if (value == 'code') {
									$scope.enableRadio = false
								}
							}

							// Clone monkey Strategy submit function
							$scope.addCloneMonkeyStrategy = function() {
								if ($scope.cloneScript != null
										&& $scope.cloneScript != ""
										&& $scope.cloneMonkey.strategyName.$error.pattern == undefined) {
									$scope.scriptValidation = false;
									$scope.fields.monkeyStrategyScript = $scope.cloneScript;

									if ($scope.defaultFlagValue) {
										$scope.fields.defaultFlag = "Y";
									} else {
										$scope.fields.defaultFlag = "N";
									}

									if ($scope.GenericFlag) {
										$scope.fields.generic = "Y";
									} else {
										$scope.fields.generic = "N";
									}

									// To capture user info cloned by
									var currentdate = new Date();
									var curr_date_time = currentdate.getDate()
											+ "/"
											+ (currentdate.getMonth() + 1)
											+ "/" + currentdate.getFullYear()
											+ " @ " + currentdate.getHours()
											+ ":" + currentdate.getMinutes()
											+ ":" + currentdate.getSeconds();
									$scope.fields.createdBy = $scope.username;
									$scope.fields.createDate = curr_date_time;
									$scope.fields.lastModifyBy = $scope.username;
									$scope.fields.lastModifyDate = curr_date_time;
									var cloneMonkeyData = $scope.fields;
									delete cloneMonkeyData.id;

									// Clone monkey strategy service call
									resiliencyStudioService
											.addMonkeyStrategy(cloneMonkeyData)
											.then(
													function(success) {
														$('.alert').show();
														$scope.updatedStrategy = cloneMonkeyData.monkeyStrategyName;
														$scope
																.resetCloneMonkeyForm();
														$scope.showErrorMessage = false;
														$scope.showSuccessMessage = true;
														$scope.message = 'Monkey Strategy is Cloned successfully.'
														$scope.success = true;
														$scope.showButton = false;
													},
													function(error, status) {
														$('.alert').show();
														$scope.data = cloneMonkeyData;
														if (error.status == "CONFLICT") {
															$scope.message = 'The monkey strategy with same name is already exists.';
															$scope.success = false;
															$scope.showSuccessMessage = false;
															$scope.showErrorMessage = true;
															$scope.showButton = true;
														} else {
															$scope.message = error.message;
															$scope.success = false;
															$scope.showErrorMessage = true;
														}

													});
								} else if ($scope.cloneMonkey.strategyName.$error.pattern)
									return;
								else {
									$scope.scriptValidation = true;
								}
							}

							var clonedMonkeyData = $scope.fields;

							// Resetting Clone monkey form
							$scope.resetCloneMonkeyForm = function() {
								$scope.defaultFlagValue = false;
								$scope.GenericFlag = false;
								$scope.fields = angular.copy(clonedMonkeyData);
								$scope
										.genericFlagSwitch($scope.fields.genericFlag)
								$scope.cloneMonkey.$setPristine();
								$scope.fields.monkeyStrategyScript = null;
								$scope.scriptContent = null;
								$scope.cloneScript = null;
								$scope.fields = {};
								delete $scope.flavors;
								delete $scope.failureSubCategories;
								$('body, html').animate({
									scrollTop : $('body').offset().top
								}, 'fast');
								$timeout(function() {
									$("#versionId").val(' 0.1');
									$scope.value = "upload";
									$scope.radioVal = 'upload';
									$("#upload").prop("checked", true);
									$('#defaultFlagId').prop('checked', true);
									$('#genericFlagId').prop('checked', false)
								}, 2000);
								$scope.enableRadio = true;
							}

							// After adding script make the button name
							// accordingly
							$scope.scriptComplete = function() {
								if ($scope.scriptContent == ""
										|| $scope.scriptContent == undefined) {
									$scope.btnScriptVal = 'Code Script';
								} else {
									$scope.btnScriptVal = 'View Script';
								}
							}

							// Add/Edit script in a popup window
							$scope.addCloneScript = function() {
								$scope.originalSript = angular
										.copy($scope.cloneScript);
								$scope.loadCloneScriptPopup();
							}

							// Cancel the add/edit script in a popup window
							$scope.cancelScript = function() {
								$scope.cloneScript = $scope.originalSript;
							}

							// Redirect to view monkeys page
							$scope.viewMonkey = function() {
								$location.path('/viewMonkeyStrategies');
							}

							// load script content in a popup to read only
							$scope.openScriptPopup = function() {
								$("#modalWriteScript").on(
										"show",
										function() {
											$("#modalWriteScript a.btn").on(
													"click",
													function(e) {
														$("#modalWriteScript")
																.modal('hide');
													});
										});
								$("#modalWriteScript").on("hide", function() {
									$("#modalWriteScript a.btn").off("click");
								});

								$("#modalWriteScript").on("hidden", function() {
									$("#modalWriteScript").remove();
								});

								$("#modalWriteScript").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});
							}

							// load script content in a popup to add/edit
							$scope.loadCloneScriptPopup = function() {
								$("#modalCloneScript").on(
										"show",
										function() {
											$("#modalCloneScript a.btn").on(
													"click",
													function(e) {
														$("#modalCloneScript")
																.modal('hide');
													});
										});
								$("#modalCloneScript").on("hide", function() {
									$("#modalCloneScript a.btn").off("click");
								});

								$("#modalCloneScript").on("hidden", function() {
									$("#modalCloneScript").remove();
								});

								$("#modalCloneScript").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});
							}

							// Find default strategy
							$scope.searchDefaultStrategy = function() {
								$scope.showButton = false;
								var data = $scope.data;
								resiliencyStudioService
										.searchDefaultMonkeyStrategy(
												data.monkeyStrategyName,
												data.monkeyType)
										.then(
												function(data) {
													// call update function
													$scope
															.updateDefaultStrategy(data);
												},
												function(error) {
													$scope.message = "Unable to find default strategy ["
															+ data.monkeyStrategyName
															+ "]";

												});
							}

							// Update defaultflag 'N' when duplicate strategy
							// found
							$scope.updateDefaultStrategy = function(data) {
								$scope.showButton = false;
								data.defaultFlag = 'N';
								resiliencyStudioService
										.updateMonkeyStrategy(data.id, data)
										.then(
												function(data) {
													// call insertion function
													$scope
															.insertDefaultStrategy();
												},
												function(error) {
													$scope.message = "Unable to modify existing default strategy ["
															+ data.monkeyStrategyName
															+ "]";
												});

							};

							// Insert defaultflag 'Y' with current strategy when
							// duplicate strategy found
							$scope.insertDefaultStrategy = function() {
								var data = $scope.fields;
								resiliencyStudioService
										.addMonkeyStrategy(data)
										.then(
												function(data) {
													$scope.addedStrategy = data.monkeyStrategyName;
													$scope
															.resetCloneMonkeyForm();
													$scope.showSuccessMessage = true;
													$scope.message = 'Monkey Strategy is added successfully.'
													$scope.success = true;

												},
												function(error) {
													$scope.message = "Unable to make current strategy to default ["
															+ data.monkeyStrategyName
															+ "]";
												});

							};
						} ]);
