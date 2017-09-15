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
				'addMonkeyStrategyCtrl',
				[
						'$rootScope',
						'$scope',
						'$parse',
						'$location',
						'$http',
						'$compile',
						'$sce',
						'resiliencyStudioService',
						'$timeout',
						function($rootScope, $scope, $parse, $location, $http,
								$compile, $sce, resiliencyStudioService,
								$timeout) {
							$rootScope.sideBar = "toggled";

							if (!$rootScope.UserPrivilege) {
								$location.path("/");
							}

							// Initialize AddMonkeyForm model Object
							$scope.fields = {
								monkeyType : "",
								monkeyStrategyName : "",
								strategyDescription : "",
								defaultFlag : "",
								monkeyStrategyVersion : "",
								genericFlag : "",
								osType : "",
								flavor : "",
								failureCategory : "",
								failureSubCategory : "",
								category : "",
								file : "",
								createdBy : "",
								createDate : "",
								lastModifyBy : "",
								lastModifyDate : ""
							}
							$scope.defaultFlagValue = true;// Initially default
															// flag as enable
							$scope.enableRadio = true;// default radio button
														// enables on file
														// upload
							$scope.results = null;// Upload script content
							$scope.monkeyWriteScript = null;// Write script
															// content
							$scope.radioVal = 'upload';// Initially Radio
														// button value
							$scope.fields.genericFlag = false;// Initially
																// genricFlag
																// value
							$scope.GenericFlag = false;// Intially Generic flag
														// toggle value 'NO'
							$scope.mandatoryFields = false;// Initially fields
															// are not mandatory
															// in advanced tab.
							$scope.scriptValidation = false;// Initially script
															// validation
															// message is hide
							$scope.showUpload = true;
							$scope.value = "upload";
							$scope.showViewScript = false;// Initially View
															// script button is
															// disabled
							$scope.btnScriptTxt = 'Code Script';// btnScriptTxt
																// value becomes
																// when select
																// code script
																// radio
							$scope.fields.monkeyStrategyVersion = 0.1;// Initially
																		// monkey
																		// strategy
																		// version

							// To prevent space in monkey strategy input box
							$timeout(function() {
										$('#strategyName').on('keypress',
												function(e) {
													if (e.which == 32)
														return false;
												});
										// Inline messages closes while click on
										// close symbol
										$("[data-hide]")
												.on("click",
														function() {
															$(this).closest("." + $(this).attr("data-hide")).hide();
														});
							}, 1000);

							// To get flavors/failuresubcategory dropdown
							// values.
							resiliencyStudioService.getFailures().then(
									function(failures) {
										$scope.failures = failures;
									}, function(error) {
										console.log(error)
									});

							// To get flavors based on selected OS Types
							$scope.getFlavors = function() {
								$scope.options1 = $scope.failures.osType;
								$scope.options2 = [];
								var key = $scope.options1
										.indexOf($scope.fields.osType);
								var myNewOptions = $scope.failures.flavors[key];
								$scope.flavors = myNewOptions;
								$scope.fields.flavor = $scope.flavors[0];

							}

							// To get failureSubCategory based on selected
							// failureCategory
							$scope.getSubCategory = function() {
								delete $scope.failureSubCategories;
								$scope.category = $scope.failures.failureCategory;
								$scope.subcategory = [];
								var key = $scope.category
										.indexOf($scope.fields.failureCategory);
								var mySubCategory = $scope.failures.failureSubCateogy[key];
								$scope.failureSubCategories = mySubCategory;
								if (key == 1) {
									$scope.fields.failureSubCategory = $scope.failureSubCategories[0];
								} else {
									$scope.fields.failureSubCategory = "";
								}
							}

							// Populate monkeys for Monkey type dropdown
							resiliencyStudioService.getMonkeyTypes().then(
									function(data) {
										$scope.monkeys = data;
									}, function(error) {
										console.log(error)
									});

							// Script File Validation for specific file format
							$scope.uploadFile = function(files) {
								var ext = files[0].name.match(/\.(.+)$/)[1];
								if (angular.lowercase(ext) === 'sh'
										|| angular.lowercase(ext) === 'yml') {
									$scope.invalidfile = false;
									var fd = new FormData();
									fd.append("file", files[0]);
								} else {
									$scope.invalidfile = true;
								}
							}

							// To read file content and store into scope
							$scope.displayFileContents = function(contents) {
								$scope.results = contents;
								$scope.monkeyWriteScript = contents;
							};

							// To make fields as mandatory/non-mandatory in
							// advanced tab.
							$scope.genericFlagSwitch = function(flag) {
								$scope.fields.genericFlag = $scope.GenericFlag;

								if (flag) {
									$scope.GenericFlag = true;
									$("#failureCategoryAddFPId").attr(
											'required', true);
									$("#failureSubCategoryAddFPId").attr(
											'required', true);
									$("#osTypeId").attr('required', true);
									$("#flavourAddFPId").attr('required', true);
									$scope.mandatoryFields = true;
								} else {
									$scope.GenericFlag = false;
									$("#failureCategoryAddFPId").removeAttr(
											'required');
									$("#failureSubCategoryAddFPId").removeAttr(
											'required');
									$("#osTypeId").removeAttr('required');
									$("#flavourAddFPId").removeAttr('required');
									$("#genericFlagId").removeAttr('checked');
									$scope.mandatoryFields = false;
								}

							}

							// To capture Toggleswitch value while changing the
							// default flag
							$scope.defaultFlagSwitch = function() {
								$scope.defaultFlagValue = !$scope.defaultFlagValue;
							}

							// Submitting add monkey form
							$scope.submitAddMonkeyForm = function() {
								// To Capture user log info
								var currentdate = new Date();
								var curr_date_time = currentdate.getDate()
										+ "/" + (currentdate.getMonth() + 1)
										+ "/" + currentdate.getFullYear()
										+ " @ " + currentdate.getHours() + ":"
										+ currentdate.getMinutes() + ":"
										+ currentdate.getSeconds();
								$scope.fields.createdBy = $scope.username;
								$scope.fields.createDate = curr_date_time;
								$scope.fields.lastModifyBy = $scope.username;
								$scope.fields.lastModifyDate = curr_date_time;
								$scope.fields.monkeyStrategyVersion = 0.1;

								if ($scope.monkeyWriteScript != null
										&& $scope.monkeyWriteScript != ""
										&& $scope.addMonkeyStrategy.name.$error.pattern == undefined) {
									$scope.scriptValidation = false;
									$scope.fields.monkeyStrategyScript = $scope.monkeyWriteScript;

									// Set default flag
									if ($scope.defaultFlagValue) {
										$scope.fields.defaultFlag = "Y";
									} else {
										$scope.fields.defaultFlag = "N";
									}

									// Set generic flag
									if ($scope.GenericFlag) {
										$scope.fields.generic = "Y";
									} else {
										$scope.fields.generic = "N";
									}

									var addMonkeyData = $scope.fields;
									$scope.showSuccessMessage = false;// Initially
																		// inline
																		// success
																		// message
																		// is
																		// hidden
									$scope.showErrorMessage = false;// Initially
																	// inline
																	// error
																	// message
																	// is hidden

									// Add Monkey Strategy Service call
									resiliencyStudioService
											.addMonkeyStrategy(addMonkeyData)
											.then(
													function(success) {
														$('.alert').show();
														$scope.addedStrategy = addMonkeyData.monkeyStrategyName;
														$scope
																.resetAddMonkeyForm();
														$scope.showSuccessMessage = true;
														$scope.showErrorMessage = false;
														$scope.message = 'Monkey Strategy is added successfully.'
														$scope.success = true;
														$scope.showButton = false;
														$('input:radio[name=script]')
																.val([ 'upload' ]);

													},
													function(error, status) {
														$('.alert').show();
														$scope.data = addMonkeyData;
														if (error.status == "CONFLICT") {
															$scope.showSuccessMessage = false;
															$scope.showErrorMessage = true;
															$scope.showButton = true;
															$scope.success = false;
															$scope.message = 'The monkey strategy with same name is already exists.';
														}

													})
									// make advanced tab as non-mandatory
									$scope.genericFlagSwitch(false);
								} else if ($scope.addMonkeyStrategy.name.$error.pattern)
									return;
								else {
									$scope.scriptValidation = true;
								}
							}

							// Find default strategy
							$scope.searchDefaultStrategy = function() {
								var data = $scope.data;
								resiliencyStudioService
										.searchDefaultMonkeyStrategy(
												data.monkeyStrategyName,
												data.monkeyType)
										.then(
												function(data) {
													// call update function
													$scope.updateDefaultStrategy(data);
													$scope.showButton = false;
												},
												function(error) {
													$scope.message = "Unable to find default strategy ["
															+ data.monkeyStrategyName
															+ "]";
													$scope.showButton = false;
													console.log(error)
												});
							}

							// Update defaultflag 'N' when duplicate strategy
							// found
							$scope.updateDefaultStrategy = function(data) {
								$scope.showButton = false;
								data.defaultFlag = 'N';
								resiliencyStudioService
										.updateMonkeyStrategy(data.id, data)
										.then(function(data) {
													// call insertion function
													$scope.insertDefaultStrategy();
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
								$scope.showButton = false;
								var data = $scope.fields;
								resiliencyStudioService
										.addMonkeyStrategy(data)
										.then(
												function(data) {
													$scope.addedStrategy = data.monkeyStrategyName;
													$scope.resetAddMonkeyForm();
													$scope.showErrorMessage = false;
													$scope.showSuccessMessage = true;
													$scope.message = 'Monkey Strategy is added successfully.'
													$scope.success = true;

												},
												function(error) {
													$scope.message = "Unable to make current strategy to default ["
															+ data.monkeyStrategyName
															+ "]";
													console.log("error occured while adding default strategy")
												});

							};

							var addMonkeyData = $scope.fields;

							// Resetting Add Monkey Strategy form
							$scope.resetAddMonkeyForm = function() {
								$scope.fields = angular.copy(addMonkeyData);
								$scope.genericFlagSwitch(false);
								$scope.addMonkeyStrategy.$setPristine();
								$scope.results = null;
								$scope.monkeyWriteScript = null;
								$scope.fields = {};
								angular.element("input[type='file']").val(null);
								delete $scope.flavors;
								delete $scope.failureSubCategories;
								$scope.enableRadio = true
								$('body, html').animate({
									scrollTop : $('body').offset().top
								}, 'fast');
								// To keep default values
								$timeout(function() {
									$("#versionId").val(' 0.1');
									$scope.radioVal = "upload";
									$scope.value = "upload";
									$("#upload").prop("checked", true);
									$('#genericFlagId').prop('checked', false)
									$('#defaultFlagId').prop('checked', true);
									$('input:radio[name=script]').val([ 'upload' ]);
								}, 2000);

							}

							// To capture value When Radio Button Clicks
							$scope.radioCheck = function(value) {
								$scope.radioVal = value;
							}
							$scope.radioSwitch = function(value) {
								if (value == 'upload') {
									$scope.enableRadio = true
								}
								if (value == 'code') {
									$scope.enableRadio = false
								}
							}

							// Add/Edit script to existing content
							$scope.addScript = function() {
								$scope.showViewScript = true;
							}

							// load script content in a popup to add/edit
							$scope.writeMonkeyScript = function() {
								$scope.originalScript = angular
										.copy($scope.monkeyWriteScript);
								loadPopupScript();
							}

							// After add/edit script in popup click ok
							$scope.writeScriptOkClick = function() {
								if ($scope.monkeyWriteScript)
									$scope.btnScriptTxt = 'View Script';
								else
									$scope.btnScriptTxt = 'Code Script';
							}

							// load script content in a popup to read only
							$scope.viewUploadedScript = function() {
								loadViewScriptPopup();
							}

							// To redirect to monkeys page
							$scope.viewMonkey = function() {
								$location.path('/viewMonkeyStrategies');
							}

							// To cancel the script add/edit in popup
							$scope.cancelScript = function() {
								$scope.monkeyWriteScript = $scope.originalScript;
							}

							// load script content in a popup to add/ edit
							function loadPopupScript() {
								$("#writeScriptModal").on("show",function() {
											$("#writeScriptModal a.btn").on("click",function(e) {
												$("#writeScriptModal").modal('hide');
											});
										});
								$("#writeScriptModal").on("hide", function() {
									$("#writeScriptModal a.btn").off("click");
								});

								$("#writeScriptModal").on("hidden", function() {
									$("#writeScriptModal").remove();
								});

								$("#writeScriptModal").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});
							}

							// load script content in a popup to read only
							function loadViewScriptPopup() {
								$("#viewScriptModal").on("show",function() {
											$("#viewScriptModal a.btn").on("click",function(e) {
														$("#viewScriptModal").modal('hide');
													});
										});
								$("#viewScriptModal").on("hide", function() {
									$("#viewScriptModal a.btn").off("click");
								});

								$("#viewScriptModal").on("hidden", function() {
									$("#viewScriptModal").remove();
								});

								$("#viewScriptModal").modal({
									"backdrop" : "static",
									"keyboard" : true,
									"show" : true
								});
							}

						} ])
