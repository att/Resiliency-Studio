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
app.controller('addFailurePointCtrl', ['$rootScope','$scope','$filter','resiliencyStudioService','$http','$location','$timeout',
                                         '$parse','$compile','$sce','$modal', '$window',
                                         function($rootScope,$scope,$filter,resiliencyStudioService,$http,$location,$timeout,$parse,$compile,$sce,$modal,$window)
{
	$rootScope.sideBar = "toggled";
	//Get select box values from Json file
	$http({
		method : 'GET',
		url : 'app/json/add-scenario.json'
	}).success(function(data) {
		$scope.scenarios = data;
	});
	//To get Category
	 $http({
       method: 'POST',
       url: 'app/json/add_application.json'
        
    }).success(function(data) {
        $scope.categories = data.categoryLists; // response data
	       
    });
	 $timeout(function(){
			$("[data-hide]").on("click", function(){
		        $(this).closest("." + $(this).attr("data-hide")).hide();
		    });
		});
	// $("#myElem").show();
	
	
	// setTimeout(function() { $("#myElem").hide(); }, 3000);
	/* // To get
	 $http({
	       method: 'GET',
	       url: 'app/json/failurePoint.json'
	        
	    }).success(function(data) {
	        $scope.fauilrePoints = data; // response data
		       
	    });*/
	 
	 //To get failure Points
	 
	 $http({
			method : 'GET',
			url : 'app/json/failures.json'
		}).success(function(data) {
			$scope.failures = data;
		});
	 
	 
	 // To get flavors based on selected OS Types
	 $scope.getFlavors = function(){
			
			//$scope.failures = data;
			$scope.options1 = $scope.failures.osType;
			$scope.options2 = [];
			var key = $scope.options1.indexOf($scope.appfields.osType);
			var myNewOptions = $scope.failures.flavors[key];
			$scope.flavors = myNewOptions;
			
		}

	 //To get failureSubCategory based on selected failureCategory
	 $scope.getSubCategory = function(){
		 
		 $scope.category = $scope.failures.failureCategory;
		 $scope.subcategory = [];
		 var key =  $scope.category.indexOf($scope.appfields.failureCategory);
		 var mySubCategory = $scope.failures.failureSubCateogy[key];
		 $scope.failureSubCategories = mySubCategory;
	 }
	 
	 
	//To get Monkey Type
	 resiliencyStudioService.getMonkeyTypes().then(
				function(data) {
					 $scope.monkeys = data;	
				},
				function(error) {
					dialogs.error("Error","monkey retrieved error.",error);
				});
	 
	 // To get Monkey Strategy
	 var selectedMonkeyType = "";			
		$scope.selectedMonkeyType = function(){				
			selectedMonkeyType = $scope.appfields.monkeyType;				
			
			var Strategies = {						
					monkeyStragey:[],
					monkeyStrategyId:[]
			}
			
					resiliencyStudioService.getMonkeyStrategies().then(
				       function(data) {

				    	   for (var i = 0; i < data.length; i++) {								
							if (data[i].monkeyType.includes(selectedMonkeyType))
								{
								//console.log("data - "+data[i]);
								Strategies.monkeyStragey.push(data[i].monkeyStrategyName);
								console.log("monkey strategy - "+data[i].monkeyStrategyName);
								console.log("monkey strategy id - "+data[i].id);
								Strategies.monkeyStrategyId.push(data[i].id);
								}
						}
				    	   //To make both strategy and strategyId as single object array
				    	   $scope.repeatData = Strategies.monkeyStragey.map(function(value, index) {
				    		    return {
				    		        data: value,
				    		        value: Strategies.monkeyStrategyId[index]
				    		    }
				    		});
				    	 				    	  
				    	}),
				    	(function(error){
				    	console.log("some error occure during get Monkey Stratgies....");
				       });
		} //Monkey Strategy end.
	 
	 
	 $scope.fields ={
			 name :"",
			 category :"",
			 processName :"",
			 role : "", 
			 osType : "",
			 flavor : "",
			 failureCategory : "",
			 failureSubCategory : "",
			 component:"",
			 failureTenet :"",
			 failureMode :"",
			 userBehavior :"",
			 systemBehavior : "", 
			 causeOfFailure:"",
			 currentControls :"",
			 detection :"",
			 recoveryAction :"",
			 recommendation : "", 
			 mttd :"",
			 mttr :"",
			 monkeyStrategy :"",
			 monkeyStrategyId :"",
			 monkeyType :""
		}
	 
							
	var addData = {
			
	};
	
	//To reset the form
	$scope.scenarioReset = function(){
		$scope.appfields = {};
	}
	
	
	//To submit updated scenario
	$scope.submitFailurePoint = function(){
		addData.name=$scope.appfields.name;
		addData.category=$scope.appfields.category;
		addData.processName=$scope.appfields.processName;
		addData.role=$scope.appfields.role;
		addData.osType=$scope.appfields.osType;
		addData.flavor=$scope.appfields.flavor;
		addData.failureCategory=$scope.appfields.failureCategory;
		addData.failureSubCategory=$scope.appfields.failureSubCategory;
		addData.component=$scope.appfields.component;
		addData.failureTenet=$scope.appfields.failureTenet;
		addData.failureMode=$scope.appfields.failureMode;
		addData.userBehavior=$scope.appfields.userBehavior;
		addData.systemBehavior=$scope.appfields.systemBehavior;
		addData.causeOfFailure=$scope.appfields.causeOfFailure;
		addData.currentControls=$scope.appfields.currentControls;
		addData.detection=$scope.appfields.detection;
		addData.recoveryAction=$scope.appfields.recoveryAction;
		addData.recommendation=$scope.appfields.recommendation;
		addData.mttd=$scope.appfields.mttd;
		addData.mttr=$scope.appfields.mttr;
		addData.monkeyType=$scope.appfields.monkeyType;
		addData.monkeyStrategy=$scope.appfields.monkeyStrategy.data;		
		addData.monkeyStrategyId=$scope.appfields.monkeyStrategy.value; 
		
		var data=addData;		
		resiliencyStudioService.addFailurePoint(data).then(
			       function(success) { 
			    	   //loadPopup();
			    	   $scope.addedFP = data.name;
			    	   $('.alert').show();
			    	   $scope.showerrorAlert= false;
			    	   $scope.successAlert = true
			    	   $('body, html').animate({scrollTop:$('body').offset().top}, 'fast');
			    	   /*$timeout(function(){
			    		   $scope.successAlert = false;
			    		 },5000);*/
				       console.log("FailurePoint added successfully.");
			       },function(error){
			    	   console.log("add failure point failed.");
			    	   $('.alert').show();
			    	   $scope.successAlert = false;
			    	   $scope.showerrorAlert= true;
			    	   $scope.errorMessage = error.statusMessage;
			    	   $('body, html').animate({scrollTop:$('body').offset().top}, 'fast');
			    	   /*$timeout(function(){
			    			 $scope.showerrorAlert = false;
			    		 },5000);*/
			       }
			       );
	      
	}
	
	//To redirect view failure point
	$scope.viewFailurePoint = function(){
		 $('body').removeClass('modal-open');
		 $('.modal-backdrop').remove();
		$location.path('/failurePoint');
	}
	
	//Load Message PopUp after Updation
	function loadPopup(){
	     $("#myModal").on("show", function() {    // wire up the OK button to dismiss the modal when shown
	          $("#myModal a.btn").on("click", function(e) {
	              $("#myModal").modal('hide');     // dismiss the dialog
	          });
	      });
	      $("#myModal").on("hide", function() {    // remove the event listeners when the dialog is dismissed
	          $("#myModal a.btn").off("click");
	      });
	      
	      $("#myModal").on("hidden", function() {  // remove the actual elements from the DOM when fully hidden
	          $("#myModal").remove();
	      });
	      
	      $("#myModal").modal({                    // wire up the actual modal functionality and show the dialog
	        "backdrop"  : "static",
	        "keyboard"  : true,
	        "show"      : true                     // ensure the modal is shown immediately
	      });
	}
	
	$(document).on('click','.closed',function(){
		$("#myModal").removeClass('in');
		
		$rootScope.$apply();
		$(".modal-backdrop").remove();
		});
	
}]);