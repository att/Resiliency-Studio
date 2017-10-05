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
				'editMonkeyStrategyCtrl',
				[
						'$http',
						'$rootScope',
						'$scope',
						'$routeParams',
						'$parse',
						'$location',
						'$compile',
						'$sce',
						'resiliencyStudioService',
						'$timeout',
						function($http, $rootScope, $scope, $routeParams,
								$parse, $location, $compile, $sce,
								resiliencyStudioService, $timeout) {

							// get monkey object data from session storage
							var monkeyInfo = JSON.parse(sessionStorage
									.getItem("info"));
							$scope.fields = angular.copy(monkeyInfo);

							$scope.monkeyScript = $scope.fields.monkeyStrategyScript;// Script
																						// content
																						// keep
																						// in
																						// scope
																						// to
																						// read
																						// only
							$scope.editMonkeyScript = $scope.fields.monkeyStrategyScript;// Script
																							// content
																							// keep
																							// in
																							// scope
																							// to
																							// add/edit
																							// the
																							// content
							$scope.fields.monkeyStrategyVersion = Number($scope.fields.monkeyStrategyVersion) + 0.1;// updated
																													// version
																													// by
																													// 0.1
							$scope.fields.monkeyStrategyVersion = $scope.fields.monkeyStrategyVersion
									.toFixed(1);
							$scope.fields.lastModifyBy = $rootScope.attuid;// To
																			// capture
																			// last
																			// modified
																			// user
																			// id
							$scope.enableRadio = true;// Initially radio
														// button enable for
														// upload script
							$scope.scriptValidation = false;// Initially script
															// is valid
							$scope.radioVal = 'upload';// Initially radio
														// button value
							$scope.showSuccessMessage = false;// Initially
																// inline
																// success
																// messages hide
							$scope.showErrorMessage = false;// Initially inline
															// error messages
															// hide
							$scope.value = "upload";

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							$rootScope.sideBar = "toggled";
							// remove required validation on advanced tab fields
							// if userprivilege not an admin
							$timeout(
									function() {
										if ($rootScope.UserPrivilege != 'admin') {
											$("#osTypeId").removeAttr(
													'required');
											$("#flavourAddFPId").removeAttr(
													'required');
										}
										$("[data-hide]")
												.on("click",
														function() {
															$(this).closest("." + $(this).attr("data-hide"))
																	.hide();
														});

									}, 1000);

							// To get flavor/failuresubcategory dropdown values
							resiliencyStudioService.getFailures().then(
									function(failures) {
										$scope.failures = failures;
										// To auto select Flavor &
										// FailureSubCategory
										$scope.getFlavors();
										$scope.getSubCategory();
									}, function(error) {
										console.log(error)
									});

							// To populate flavors based on selected OS Types
							// criteria
							$scope.getFlavors = function() {
								$scope.options1 = $scope.failures.osType;
								$scope.options2 = [];
								var key = $scope.options1
										.indexOf($scope.fields.osType);
								var myNewOptions = $scope.failures.flavors[key];
								$scope.flavors = myNewOptions;
								$scope.fields.flavor = $scope.flavors[0];

							}

							// To populate failureSubCategory based on selected
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

							// Enable/Disable default switch from existing value
							if ($scope.fields.defaultFlag === 'Y') {
								$scope.defaultFlagValue = true;
							} else {
								$scope.defaultFlagValue = false;
							}

							// Enable/Disable Generic switch from existing value
							if ($scope.fields.generic === 'Y') {
								$scope.GenericFlag = true;
							} else {
								$scope.GenericFlag = false;
							}
							$scope.mandateField = $scope.GenericFlag;// Required
																		// field
																		// flag
																		// for
																		// advanced
																		// tab.

							// Populate monkeys for monkey type dropdown.
							resiliencyStudioService.getMonkeyTypes().then(
									function(data) {
										$scope.monkeys = data;
									}, function(error) {
										console.log(error)
									});

							// Script File Validation which allowing specific
							// format only.
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

							// Display script content when view script button
							// clicks
							$scope.displayFileContents = function(contents) {
								$scope.results = contents;
								$scope.monkeyScript = contents;
								$scope.editMonkeyScript = contents;
							};

							// Genric Flag switch
							$scope.genericFlagSwitch = function(flag) {
								// If Generic flag true enable required
								// validation on fields
								if (flag) {
									$scope.GenericFlag = true;
									$("#failureSubCategoryAddFPId").attr(
											'required', true);
									$("#failureCategoryAddFPId").attr(
											'required', true);
									$("#osTypeId").attr('required', true);
									$("#flavourAddFPId").attr('required', true);
									$scope.mandateField = true;
								} else {
									$scope.GenericFlag = false;
									$("#failureSubCategoryAddFPId").removeAttr(
											'required');
									$("#failureCategoryAddFPId").removeAttr(
											'required');
									$("#osTypeId").removeAttr('required');
									$("#flavourAddFPId").removeAttr('required');
									$("#genericFlagId").removeAttr('checked');
									$scope.mandateField = false;
								}
							}

							// Default flag switch
							$scope.defaultFlagSwitch = function() {
								$scope.defaultFlagValue = !$scope.defaultFlagValue;
							}

							// Update Monkey strategy
							$scope.updateMonkeyForm = function() {
								// To Capture last update time
								var currentdate = new Date();
								var curr_date_time = currentdate.getDate()
										+ "/" + (currentdate.getMonth() + 1)
										+ "/" + currentdate.getFullYear()
										+ " @ " + currentdate.getHours() + ":"
										+ currentdate.getMinutes() + ":"
										+ currentdate.getSeconds();
								$scope.fields.lastModifyDate = curr_date_time;
								$scope.fields.lastModifyBy = $scope.username;

								if ($scope.monkeyScript != null
										&& $scope.monkeyScript != "") {
									$scope.scriptValidation = false;
									$scope.fields.monkeyStrategyScript = $scope.monkeyScript;

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

									var editData = $scope.fields;
									// Post call to update a monkey strategy
									// through resiliencyStudioService.js
									resiliencyStudioService
											.updateMonkeyStrategy(editData.id,
													editData)
											.then(
													function(success) {
														$('.alert').show();
														$scope.updatedStrategy = success.monkeyStrategyName;
														$scope.modalMessage = 'Monkey Strategy is Updated Successfully.'
														$scope
																.resetUpdateMonkeyForm();
														$scope.showErrorMessage = false;
														$scope.showSuccessMessage = true;
														$scope.enableRadio = true
													},
													function(error) {
														$('.alert').show();
														$scope.modalMessage = 'Update monkey strategy failed.'
														$scope.showSuccessMessage = false;
														$scope.showErrorMessage = true;
													});
								} else {
									$scope.scriptValidation = true;
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

							// keep values into disabled form fields after
							// submit form
							/*
							 * $scope.version =
							 * $scope.fields.monkeyStrategyVersion;
							 * $scope.strategyName =
							 * $scope.fields.monkeyStrategyName;
							 * $scope.monkeyType = $scope.fields.monkeyType;
							 */

							// To Reset Edit Monkey Strategy form
							var original = $scope.fields;

							$scope.resetUpdateMonkeyForm = function() {
								$scope.fields = angular.copy(original);
								$scope.genericFlagSwitch(false);
								$scope.editMonkeyStrategy.$setPristine();
								$scope.fields.monkeyStrategyScript = '';
								$scope.monkeyScript = null;
								$scope.editMonkeyScript = null;
								$scope.mandateField = false;
								$scope.fields.scriptTypeCategory = '';
								$scope.fields.strategyDescription = '';
								$scope.fields.osType = '';
								$scope.fields.flavor = '';
								$scope.fields.failureCategory = '';
								$scope.fields.failureSubCategory = '';
								$scope.fields.monkeyStrategyVersion = '';
								delete $scope.monkeys;
								$scope.fields.monkeyStrategyName = '';
								delete $scope.flavors;
								delete $scope.failureSubCategories;
								$('body, html').animate({
									scrollTop : $('body').offset().top
								}, 'fast');
								$timeout(
										function() {
											$("#versionId").val($scope.version);
											$("#strategyName").val(
													$scope.strategyName);
											$("#monkeyType").val(
													$scope.monkeyType);
											$("#upload").prop("checked", true);
											$scope.value = 'upload';
											$('#defaultFlagId').prop('checked',
													true);
											$('#genericFlagId').prop('checked',
													false)
										}, 1000);
								$scope.enableRadio = true;
							}

							// To Update Script file content
							$scope.updateMonkeyScript = function() {
								$scope.fields.monkeyStrategyScript = $scope.monkeyScript;
							}

							// Display Script on Popup after click on view
							// Script
							$scope.ViewScript = function(monkeyStrategyScript) {
								loadPopupScript();
							}

							// Redirecting to view monkey strategies
							$scope.viewMonkey = function() {
								$location.path('/viewMonkeyStrategies');
							}

							// To edit/view script in popup window
							$scope.editWrittenScript = function() {
								$scope.originalSript = angular
										.copy($scope.monkeyScript);
								loadEditMonkeyScript();
							}

							// Cancel edit/view script in popuip window
							$scope.cancelScript = function() {
								$scope.monkeyScript = $scope.originalSript;
							}

							// To display script content in popup
							function loadPopupScript() {
								$("#myModal1").on(
										"show",
										function() {
											$("#myModal1 a.btn").on(
													"click",
													function(e) {
														$("#myModal1").modal(
																'hide');
													});
										});
								$("#myModal1").on("hide", function() {
									$("#myModal1 a.btn").off("click");
								});

								$("#myModal1").on("hidden", function() {
									$("#myModal1").remove();
								});

								$("#myModal1").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});

							}

							// load script content into popup to edit
							function loadEditMonkeyScript() {
								$("#editWrittenModal").on(
										"show",
										function() { // wire up the OK button
														// to dismiss the modal
														// when shown
											$("#editWrittenModal a.btn").on(
													"click",
													function(e) {
														$("#editWrittenModal")
																.modal('hide'); // dismiss
																				// the
																				// dialog
													});
										});
								$("#editWrittenModal").on("hide", function() {
									$("#editWrittenModal a.btn").off("click");
								});

								$("#editWrittenModal").on("hidden", function() {
									$("#editWrittenModal").remove();
								});

								$("#editWrittenModal").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});
							}

						} ]);
