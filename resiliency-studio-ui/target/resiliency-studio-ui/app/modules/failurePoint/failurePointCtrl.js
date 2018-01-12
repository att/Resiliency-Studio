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
app.controller('failurePointCtrl', ['$rootScope','$scope', 'applicationServices', '$parse',
		'$location','$http','$compile','$sce','dialogs','resiliencyStudioService','$timeout',
		'$routeParams','$route',
		function($rootScope,$scope, applicationServices, $parse,$location,
					$http,$compile,$sce,dialogs,resiliencyStudioService,$timeout,$routeParams,$route)
{

	$rootScope.sideBar = "toggled";
	 $('.alert').hide();
	$scope.scenariosFilterData = [];
	$scope.scenariosFilterModel = [];
	$scope.scenariosFilterSettings 	= {enableSearch: true,scrollable: true,closeOnBlur:true, showCheckAll:false, showUncheckAll:false,smartButtonMaxItems:1,selectionLimit:1,closeOnSelect:true,closeOnDeselect:true}; 
	$scope.scenariosFilterCustomTexts = {buttonDefaultText: 'Application Name'};
		
	$scope.loader = {
			  loading: false,
			  };
	$timeout(function(){
		$("[data-hide]").on("click", function(){
	        $(this).closest("." + $(this).attr("data-hide")).hide();
	    });
	});
	
	$scope.applicationSelected=true;
	$scope.filterTr=false;
	$scope.respectiveFilter=false;
	
	$scope.showFilter= function() {
		$scope.filterTr=true;
	}
	$scope.FPDelsuccess = false; 
	$scope.closeFilter= function() {
		delete $scope.search;
		$scope.filterTr=false;		 
		 $scope.search = {};
		
	}
	
	//To clear filter null values	
          $scope.search = {};
          $scope.clearMonkeyType = function(){
        	  if($scope.search.monkeyType.length == 0){
                  delete $scope.search.monkeyType;
              }  
          }
          $scope.clearMonkeyStrategy = function(){
        	  if($scope.search.monkeyStrategy.length == 0){
                  delete $scope.search.monkeyStrategy;
              }  
          }
          $scope.clearComponent = function(){
        	  if($scope.search.component.length == 0){
                  delete $scope.search.component;
              }  
          } 
          $scope.clearRole = function(){
        	  if($scope.search.role.length == 0){
                  delete $scope.search.role;
              }  
          } 
          $scope.clearProcessName = function(){
        	  if($scope.search.processName.length == 0){
                  delete $scope.search.processName;
              }  
          } 
            $scope.clearOsType = function(){
        	  if($scope.search.osType.length == 0){
                  delete $scope.search.osType;
              }
          	}
          $scope.clearFlavor = function(){
        	  if($scope.search.flavor.length == 0){
                  delete $scope.search.flavor;
              }
          }
          $scope.clearFailCategory = function(){
        	  if($scope.search.failureCategory.length == 0){
                  delete $scope.search.failureCategory;
              }
          }
          $scope.clearFailSubCatregory = function(){
        	  if($scope.search.failureSubCategory.length == 0){
                  delete $scope.search.failureSubCategory;
              }
          }
          $scope.clearMttd = function(){
        	  if($scope.search.mttd.length == 0){
                  delete $scope.search.mttd;
              }
          }
          $scope.clearMttr = function(){
        	  if($scope.search.mttr.length == 0){
                  delete $scope.search.mttr;
              }
          }
	resiliencyStudioService.getApplicationDetails().then(
		       function(data) {
		    	   console.log(data);
		    	   $scope.scenario = data;
		    	   angular.forEach(data, function(value, key) {
		    		   
		  			$scope.scenariosFilterData.push({'id':value.applicationName,'label':value.applicationName});
		  						
		  		});
		       }
		       );
	 
	 $timeout(function () 
	{
		 if($routeParams.hasOwnProperty('application') && $routeParams.hasOwnProperty('environment'))
		 {
			 $scope.scenariosFilterModel={'id':$routeParams.application,'label':$routeParams.application};		
			 $scope.applicationChange();
			 $scope.appfields.environmentName=$routeParams.environment;
			 $scope.getScenarios();
			 
		 }
		 
	
	});
	
	 
	 
	//To get Category
	 $http({
	       method: 'POST',
	       url: 'app/json/add_application.json'
	        
	    }).success(function(data) {
	        $scope.categories = data.categoryLists; // response data
		       
	    });
	
	$scope.viewScen=false;
	
	/*//Calling getScenarios() to view Updated scenarios
	$rootScope.$on("CallParentMethod",function(appName1){
		for(key in appName1) {
		    if(appName1.hasOwnProperty(key)) {
		        var value = appName1[key];
		        console.log("From view :"+value);
		    }
		}
		$location.path('/view-scenario/');
        $scope.getScenarios();
     });*/
	// $scope.getFailurePoint = function() {				 
			// var applicationName = $scope.scenariosFilterModel.id;
			 //var envName =$scope.appfields.environmentName;
			 $scope.loader.loading = true ;
			 $scope.viewScen=true;		 
			 
			 resiliencyStudioService.getfailurepoint().then(
				       function(data) { 
				    	   $scope.errorMessage=false
				    	   $scope.data = data;
							$scope.loader.loading = false ;
							$('#userList').show();
				       },function(error){
				    	   $('#userList').hide();
							$scope.loader.loading = false ;
							$scope.errorMessage="Scenario is not available for selected Application: { "+applicationName+" } and environment: { "+envName+" }";
				       }
				       );
	 $scope.showDetail = function (u) {
		    if ($scope.active != u) {
		      $scope.active = u;
		      
		    }
		    else {
		      $scope.active = null;
		    }
		  };
	    	
	$scope.appfields = {
			name:"",
			applicationName:"",
			serverName:"",
			role:"",
			environmentName:"",
			tier:"",
			softwareComponentName:"",
			failureTenet: "",
			failureMode: "",
			causeOfFailure:"",
			currentControls:"",
			detectionMechanism:"",
			recoveryMechanism:"",
			recommendations:"",
			mttd:"",
			mttr:"",
			failureScript:"",
			version:"",
			processName:""
	};
	
	
	
	$scope.editData = {};

	$scope.Edit = function() {
	  var editableFailurePointID = $scope.editData.OneScenario.id;
	  //if($scope.editData.OneScenario !="null")
		  	$location.path('/edit-failurepoint/'+editableFailurePointID);
	};
	
	$scope.addFailurePoint = function(){
		$location.path('/addFailurePoint');
	}
	
	// Delete Failure point Starts
	$scope.deleteData = {};
	$scope.Delete = function() {
		//console.log('FailurepointId'+$scope.deleteData.OneScenario.id);
		  var deletefpID = $scope.deleteData.OneScenario.id;
		  if($scope.deleteData.OneScenario != null)
			  {
			  var confirmdialog =  dialogs.confirm("Delete", "Are you sure want to delete the failurepoint?","yes");
			   confirmdialog.result.then(function(btn){				  
				   var dataValue = $scope.getfpDeleteValues(deletefpID);
				   //location.reload();	
					//console.log("DeleteData::"+dataValue);	
			   }); 					  
			  }
		};		
		//To Delete Failure point values
		$scope.getfpDeleteValues = function(deletefpID){	
			
			 resiliencyStudioService.deletefailurepointbyid(deletefpID).then(
				       function(data) { 
				    	   console.log("Data Successfully deleted for Failurepoint Id: "+deletefpID);
				    	   $('body, html').animate({scrollTop:$('body').offset().top}, 'fast');
				    	   $('.alert').show();
				    	   $scope.loader.loading = true ;
						   /*$timeout(function(){
							   $location.path("failurepoint");
								//$("#viewscen").ajax.reload();
							  //$route.reload();
						   },1000)*/
						   resiliencyStudioService.getfailurepoint().then(
							       function(data) {
							    	   $scope.FPDelsuccess = true;
							    	   $scope.errorMessage=false
							    	   $scope.data = data;
										$scope.loader.loading = false ;
										$('#userList').show();
							       },function(error){
							    	   $('#userList').hide();
										$scope.loader.loading = false ;
							       }
							       );
						   
				       },function(error){
				    	   $scope.FPDelSuccess =false;
				    	   $scope.errorMessage="Unable to delete the Failurepoint";
				       }
				       );		
			
			return "";
		}; //Delete Failure point Ends	
		
		
		 function deleteScenarioPopup(){
		     $("#myBulkDeleteScenario").on("show", function() {    // wire up the OK button to dismiss the modal when shown
		          $("#myBulkDeleteScenario a.btn").on("click", function(e) {
		              console.log("button pressed");   // just as an example...
		              $("#myBulkDeleteScenario").modal('hide');     // dismiss the dialog
		          });
		      });
		      $("#myBulkDeleteScenario").on("hide", function() {    // remove the event listeners when the dialog is dismissed
		          $("#myBulkDeleteScenario a.btn").off("click");
		      });
		      
		      $("#myBulkDeleteScenario").on("hidden", function() {  // remove the actual elements from the DOM when fully hidden
		          $("#myBulkDeleteScenario").remove();
		      });
		      
		      $("#myBulkDeleteScenario").modal({                    // wire up the actual modal functionality and show the dialog
		        "backdrop"  : "static",
		        "keyboard"  : true,
		        "show"      : true                     // ensure the modal is shown immediately
		      });
		  
	 }
		
		/*//Select the check All
		var isAllSelected;
		$scope.selectAll = function() {
			 var toggleStatus = !$scope.isAllSelected;
			 var toggleStatus1= $scope.isAllSelected;
			 alert("Hi..."+toggleStatus)
		     angular.forEach($scope.data, function(itm){
		    	 if(toggleStatus){
		    		// alert("SelectAll"+toggleStatus);
		    		 console.log("status :"+toggleStatus)
		    	itm.selected = toggleStatus;
		    	 }else{
		    		 console.log("Fail: "+toggleStatus1)
		    		 itm.selected = toggleStatus1;
		    	 }
		    	 
		    	 });
		  }
		*/
		
		
		
		$scope.$watch('data', function() {
	        var no = 0;
	        if($scope.data != null && $scope.data != undefined){
	        for(var i = 0; i < $scope.data.length; i++) {
	            if($scope.data[i].selected === true)
	                no++;
	        }}
	        $scope.noSelectedItems = no;
	    }, true);
		
		
		
		 $scope.retainDeleteScenario= function(){
			 location.reload();
			 
		  }
		 //Check box model attribute initialization
		 $scope.outcome ={
				 success:""
		 };
		 
		 //Checkbox select/deselect all functionality
		 $scope.selectAll = function(){
			 //alert($scope.outcome.success);
			 if ($scope.outcome.success) {
			        
		            $scope.outcome.success = true;
		        } else {
		            $scope.outcome.success = false;
		        }
		        angular.forEach($scope.data, function (u) {
		        	u.selected = $scope.outcome.success;
		        });
		 }
		
		
}]);

