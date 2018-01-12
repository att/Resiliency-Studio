app
		.controller(
				'editScenarioCtrl',
				[
						'$rootScope',
						'$scope',
						'$filter',
						'$http',
						'$location',
						'$timeout',
						'$routeParams',
						'$parse',
						'$compile',
						'$sce',
						'dialogs',
						'$modal',
						'$window',
						'resiliencyStudioService',
						function($rootScope, $scope, $filter, $http, $location,
								$timeout, $routeParams, $parse, $compile, $sce,
								dialogs, $modal, $window,
								resiliencyStudioService) {
							var scenarioID = $routeParams.id;
							$rootScope.sideBar = "toggled";

							// To hide inline messages when close the message
							$timeout(function() {
								$("[data-hide]")
										.on("click",
												function() {
													$(this).closest("." + $(this).attr("data-hide"))
															.hide();
												});
							});
							
							$scope.newMonkey = false;
							
							// Get Scenario by id to auto-populate in
							// edit_scenario page
							 var monkeysData = {
					                	execSequence: '',
					                    monkeyStrategyId: '',
					                    monkeyStrategy: '',
					                    monkeyType: '',
					                    monkeyTypeList:[],
					                    enableMonkeyType:false,
					                    enableDelay:false,
					                    removed:false
					                }
							 
							$scope.exeDelay=true;
			                $scope.OutOfFocus = function(v, value) {

								for (var i = 0; i < $scope.appfields.strategies.length; i++) {
									if ($scope.appfields.strategies[i].execSequence != undefined
												&& $scope.appfields.strategies[i].execSequence != ''
												&& $scope.appfields.strategies[i].monkeyStrategy != undefined
												&& $scope.appfields.strategies[i].monkeyStrategy != '') {
										
										 $scope.exeDelay=false;
									} else {
										 $scope.exeDelay=true;
										break;
									}
								}
							}
							 
			                $scope.delayChange = function() {
				            	   $scope.exeDelay = false;
				            }
						
							resiliencyStudioService
									.getScenarioById(scenarioID)
									.then(
											function(data) {
												$scope.scenario = data;
												$scope.currStrategy = data.monkeyStrategy;
												$scope.appfields = angular
														.copy(data);
												
												if($scope.scenario.strategies == undefined) {
													//$scope.newMonkey = true;
													$scope.appfields["strategies"] = [];
													
													$scope.addMonkeys($scope.appfields);
												} 
												
												angular.forEach($scope.scenario.strategies,function(v,i) {
													$scope.counter+=1;
								                	$scope.activities.push(''+$scope.counter+'');
												});
												$scope.monkeyTypeList = [];
												
												resiliencyStudioService
												.getMonkeyStrategies()
												.then(function(data1) {
													angular.forEach($scope.scenario.strategies,function(v,ix) {
															for (var i = 0; i < data1.length; i++) {
																if ((data1[i].monkeyType)
																		.toUpperCase() === v.monkeyType
																		.toUpperCase()) {
																	$scope.monkeyTypeList.push({"monkeyStrategyName":data1[i].monkeyStrategyName,"id":data1[i].id});
																	$scope.appfields.strategies.map(function(obj) {
																		return angular.extend(obj, {monkeyTypeList:[{"monkeyStrategyName":'',"id":data1[i].id}]});
																	});
																	
																}
																
															}
													});
													
													angular.forEach($scope.appfields.strategies,function(v,i) {
														$scope.appfields.strategies[i].monkeyTypeList = $scope.monkeyTypeList;
														$scope.appfields.strategies[i].enableDelay=true;
														$scope.appfields.strategies[i].enableMonkeyType=true;
													});
												});
												
												$scope.selectedMonkeyType();
											},
											function(error) {
												console.log("scenariobyid gets failed.");
											});

							// To populate failure tenet drop down values
							resiliencyStudioService
									.getFailureTenet()
									.then(
											function(data) {
												$scope.scenarios = data;
											},
											function(error) {
												console.log("Some error occured while fetching failure tenets")
											});
							
							
							
							
							
							
							$scope.counter=0;
			                $scope.activities=[];
			                $scope.addMonkeys = function(fields, index) {
			                	fields.strategies.splice(index+1, 0, angular.copy(monkeysData));
			                	$scope.counter+=1;
			                	$scope.activities.push(''+$scope.counter+'');
			                	
			                        	$('#btnDeleteMonkey > a').removeClass('disabled');
			                    	
			               }
			                
			                $scope.addMonkeys1 = function(fields, index) {
			                	
			                	$('#btnDeleteMonkey > a').removeClass('disabled');
			                	
			                	fields.strategies.splice(index+1, 0, angular.copy(monkeysData));
			                	$scope.counter+=1;
			                	 $scope.activities.push($scope.counter);
			               }
			                
			                $scope.removeMonkeys = function(fields,
			                        index) {
			                    	fields.strategies[index].removed=true;
			                        fields.strategies[index].execSequence=1000001;
			                       
			                        
			                        $scope.counter--;
			                        $scope.activities.pop();
			                        if($('.test[data-order!="1000001"]').length == 2) {
			                        	$('#btnDeleteMonkey > a').addClass('disabled');
			                    	}
			                    }

							// To populate Monkey Type in drop done box
							resiliencyStudioService.getMonkeyTypes().then(
									function(data) {
										$scope.monkeys = data;
									},
									function(error) {
										dialogs.error("Error",
												"monkey retrieved error.",
												error);
									});
							
							
							 $scope.selectedMonkeyStrategyScn = function($event, $index) {
								   $scope.appfields.strategies[$index].enableDelay=true;
				                   $scope.appfields.strategies[$index].monkeyStrategy = $('#'+event.target.id).find('option:selected').text();
				                   var idIndex = $('#'+event.target.id).prop('selectedIndex')-1;
				                   $scope.appfields.strategies[$index].monkeyStrategyId = $scope.appfields.strategies[$index].monkeyTypeList[idIndex].id;
				             }
							
							 $scope.sortMonkeys = function($event) {
				                    $timeout(function() {
				                    
				                    	$scope.appfields.strategies = $filter('orderBy')( $scope.appfields.strategies, 'execSequence');
				                    
				                    }, 500);
				              }

							// Monkey Type end

							// Monkey Strategy selected based on Monkey type
							// selection.
														
							var selectedMonkeyType = "";
							$scope.selectedMonkeyType = function($event,index) {
								$scope.test=true;
								if(index!==undefined){
									//selectedMonkeyType = $scope.appfields.strategies[index].monkeyType;
									 var selectedMonkeyTypeValue = $('#' + event.target.id).find('option:selected').val();
					                    selectedMonkeyType = selectedMonkeyTypeValue; 
									$scope.appfields.strategies[index].monkeyStrategy = '';
									$scope.appfields.strategies[index].monkeyTypeList = [];
									$scope.monkeyStrat =[];
									
								}
								
								
								var Strategies = {
									monkeyStrageySelected : [],
									monkeyStrategyId : []
								}

										resiliencyStudioService
												.getMonkeyStrategies()
												.then(function(data1) {

															for (var i = 0; i < data1.length; i++) {
																if ((data1[i].monkeyType)
																		.toUpperCase() === selectedMonkeyType
																		.toUpperCase()) {
																	Strategies.monkeyStrageySelected
																			.push(data1[i].monkeyStrategyName);
																	Strategies.monkeyStrategyId
																			.push(data1[i].id);
																	$scope.appfields.strategies[index].monkeyTypeList.push({"monkeyStrategyName":data1[i].monkeyStrategyName,"id":data1[i].id});
																	
																}
															}
															if(index!==undefined){
															$scope.appfields.strategies[index].enableMonkeyType=true;
															}
															// To make both
															// strategy and
															// strategyId as
															// single object
															// array
															$scope.repeatData = Strategies.monkeyStrageySelected
																	.map(function(
																			value,
																			index) {
																		return {
																			data : value,
																			value : Strategies.monkeyStrategyId[index]
																		}
																	});
															if(index!==undefined){
															if ($scope.appfields.strategies[index].monkeyStrategyId === undefined
																	|| $scope.appfields.strategies[index].monkeyStrategyId === null
																	|| $scope.appfields.strategies[index].monkeyStrategyId === '') {
																var tempData = $filter(
																		'filter')
																		(
																				$scope.repeatData,
																				{
																					data : $scope.appfields.strategies[index].monkeyStrategy
																				});
																for (var tempVal = 0; tempVal < tempData.length; tempVal++) {
																	if (tempData[tempVal].data == $scope.currStrategy) {
																		$scope.appfields.monkeyStrategy = tempData[tempVal];
																		break;
																	}
																}
															}
															}else {
																$scope.appfields.monkeyStrategy = $filter(
																		'filter')
																		(
																				$scope.repeatData,
																				{
																					value : $scope.appfields.monkeyStrategyId
																				})[0];
															}

														}),
										(function(error) {
											console.log("some error occure during get monkey strategy");
										});
							}

							// To store edited data into object
							var editData = {
								name : "",
								applicationName : "",
								environmentName : "",
								tierName : "",
								filePath : "",
								serverName : "",
								role : "",
								softwareComponentName : "",
								failureTenet : "",
								failureMode : "",
								userBehavior : "",
								systemBehavior : "",
								causeOfFailure : "",
								currentControls : "",
								detectionMechanism : "",
								recoveryMechanism : "",
								recommendations : "",
								mttd : "",
								mttr : "",
								failureScript : "",
								monkeyStrategy : "",
								version : "",
								processName : "",
								strategies: []
							};

							// To reset the form
							$scope.scenarioReset = function() {
								$scope.appfields = {};
							}

							// To submit updated scenario
							$scope.submitScenario = function() {
								$scope.editedVersion = Math
										.round((Number($scope.appfields.version) + 0.1) * 1e12) / 1e12;
								editData.name = $scope.appfields.applicationName
										+ '.'
										+ $scope.appfields.environmentName
										+ '.'
										+ $scope.appfields.serverName
										+ '.'
										+ $scope.appfields.softwareComponentName
										+ '.'
										+ $scope.appfields.processName
										+ '.'
										+ $scope.appfields.failureTenet
										+ '.'
										+ $scope.appfields.failureMode
										+ '.' + $scope.editedVersion;
								editData.applicationName = $scope.appfields.applicationName;
								editData.environmentName = $scope.appfields.environmentName;
								editData.filePath = $scope.appfields.filePath;
								editData.serverName = $scope.appfields.serverName;
								editData.role = $scope.appfields.role;
								editData.softwareComponentName = $scope.appfields.softwareComponentName;
								editData.processName = $scope.appfields.processName;
								editData.failureTenet = $scope.appfields.failureTenet;
								editData.failureMode = $scope.appfields.failureMode;
								editData.userBehavior = $scope.appfields.userBehavior;
								editData.systemBehavior = $scope.appfields.systemBehavior;
								editData.causeOfFailure = $scope.appfields.causeOfFailure;
								editData.currentControls = $scope.appfields.currentControls;
								editData.detectionMechanism = $scope.appfields.detectionMechanism;
								editData.recoveryMechanism = $scope.appfields.recoveryMechanism;
								editData.recommendations = $scope.appfields.recommendations;
								editData.mttd = $scope.appfields.mttd;
								editData.mttr = $scope.appfields.mttr;
								editData.failureScript = $scope.appfields.failureScript;
								/*editData.monkeyStrategy = $scope.appfields.monkeyStrategy.data;
								editData.monkeyType = $scope.appfields.monkeyType;
								editData.monkeyStrategyId = $scope.appfields.monkeyStrategy.value;*/
								editData.version = $scope.editedVersion;
								editData.strategies = [];
								for(var i=0;i<$scope.appfields.strategies.length;i++){
			                    	if($scope.appfields.strategies[i].execSequence != '1000001'){
			                    		editData.strategies.push($scope.appfields.strategies[i]);	
									}
			                    }

								var id = $scope.appfields.id;

								var data = editData;
								$scope.updatedScenario = data.name;
								resiliencyStudioService
										.updateScenario(id, data)
										.then(
												function(data) {
													$('.alert').show();
													$scope.showerrorAlert = false;
													$scope.successAlert = true
													$('body, html')
															.animate(
																	{
																		scrollTop : $(
																				'body')
																				.offset().top
																	}, 'fast');
												},
												function(error) {

													console.log(error)
													$('.alert').show();
													$scope.successAlert = false;
													$scope.showerrorAlert = true;
													$scope.errorMessage = error.statusMessage;
													$('body, html')
															.animate(
																	{
																		scrollTop : $(
																				'body')
																				.offset().top
																	}, 'fast');
												});
							}

							// Go to View scenario to view updated scenarios
							$scope.getViewScenario = function() {
								$('body').removeClass('modal-open');
								$('.modal-backdrop').remove();
								$location.path("/viewScenario/"
										+ $scope.appfields.applicationName
										+ "/"
										+ $scope.appfields.environmentName);
							}

							$(document).on('click', '.closed', function() {
								$("#myModal").removeClass('in');

								$rootScope.$apply();
								$(".modal-backdrop").remove();
							});
						} ]);