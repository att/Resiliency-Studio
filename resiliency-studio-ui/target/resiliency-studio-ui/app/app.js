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

var app = angular.module('loginApp',['ngRoute','ui.bootstrap','ngCookies',
	'angularjs-dropdown-multiselect','datatables','dialogs.main','ngSanitize','ngIdle']);

app.config(function($httpProvider) {
	 $httpProvider.defaults.withCredentials = true;
});
//webService.UseDefaultCredentials = true
app.value('teamname', []);

app.filter('trustAsResourceUrl', [ '$sce', function($sce) {
	return function(val) {
		return $sce.trustAsResourceUrl(val);
	};
} ]);

app.filter('to_trusted', [ '$sce', function($sce) {
	return function(text) {
		return $sce.trustAsHtml(text);
	};
} ]);

app.config(function($routeProvider){
	$routeProvider
	.when("/",{
		templateUrl:"app/modules/login/login.html",
		controller : "authCtrl"
	})
	.when("/logout",{
		template:"<span>Logging out....</span>",
		controller : "lgoutCtrl"
	})
	.when("/dashboard",{
		templateUrl:"app/modules/dashboard/dashboard.html",
		controller : "dashboardCtrl"
	})
	.when("/onboardApplication",{
		templateUrl:"app/modules/applications/onboardApplications/onboardApplication.html",
		controller : "onboardApplicationCtrl"
	})
	.when("/viewApplication",{
		templateUrl:"app/modules/applications/viewApplications/viewApplication.html",
		controller : "viewApplicationCtrl"
	})
	.when("/addMonkeyStrategy",{
		templateUrl:"app/modules/monkeys/addMonkeys/addMonkeyStrategy.html",
		controller : "addMonkeyStrategyCtrl"
	})
	.when("/viewMonkeyStrategies",{
		templateUrl:"app/modules/monkeys/viewMonkeys/viewMonkeyStrategies.html",
		controller : "viewMonkeyStrategyCtrl"
	})
	.when("/viewMonkeyStrategies/:tabId/",{
		templateUrl:"app/modules/monkeys/viewMonkeys/viewMonkeyStrategies.html",
		controller : "viewMonkeyStrategyCtrl"
	})
	.when('/editMonkeyStrategy/:monkey', {
		templateUrl : 'app/modules/monkeys/editMonkeys/editMonkeyStrategy.html',
		controller : 'editMonkeyStrategyCtrl'
	})
	.when('/cloneMonkeyStrategy/:monkey', {
		templateUrl : 'app/modules/monkeys/cloneMonkeys/cloneMonkeyStrategy.html',
		controller : 'cloneMonkeyStrategyCtrl'
	})
	.when("/createScenario",{
		templateUrl:"app/modules/scenarios/createScenarios/createScenario.html",
		controller : "createScenarioCtrl"
	})
	.when("/viewScenario/:application?/:environment?",{
		templateUrl:"app/modules/scenarios/viewScenarios/viewScenario.html",
		controller : "viewScenarioCtrl"
	})
	.when("/viewScenario",{
		templateUrl:"app/modules/scenarios/viewScenarios/viewScenario.html",
		controller : "viewScenarioCtrl"
	})
	.when("/editScenario/:id",{
		templateUrl:"app/modules/scenarios/editScenarios/editScenario.html",
		controller : "editScenarioCtrl"
	})
	.when("/executionDashboard",{
		templateUrl:"app/modules/scenariosExecution/executionDashboard/executionDashboard.html",
		controller : "executionDashboardCtrl"
    
	})
	.when(
			'/edit-application/:appId/',
			{
				templateUrl : 'app/modules/applications/editApplications/editApplication.html',
				controller : 'editApplicationCtrl',
			})
	.when(
			'/edit-application/:appId/:envName/:serverId/',
			{
				templateUrl : 'app/modules/applications/editApplications/editApplication.html',
				controller : 'editApplicationCtrl',
			})
	.when(
			'/edit-application/:appId/:envName/:serverId/:tabId/:tabRowId/',
			{
				templateUrl : 'app/modules/applications/editApplications/editApplication.html',
				controller : 'editApplicationCtrl',
			})
	.when("/addFailurePoint",{
		templateUrl:"app/modules/failurePoint/addFailurePoint.html",
		controller : "addFailurePointCtrl"
    
	})		
	.when("/scenariosRunYTD",{
		templateUrl:"app/modules/scenariosExecution/scenariosRunYtd/scenariosRunYTD.html",
		controller : "scenariosRunYTDCtrl"
    
	})
	.when("/failurePoint",{
		templateUrl:"app/modules/failurePoint/failurePoint.html",
		controller : "failurePointCtrl"
    
	})
	.when("/executeScenario",{
		templateUrl:"app/modules/scenariosExecution/executeScenario/executeScenario.html",
		controller : "executeScenarioCtrl"
	})
	.otherwise({
		templateUrl:"app/modules/dashboard/dashboard.html",
		controller : "dashboardCtrl"
	});
});

//Session Expire
app.config(function(IdleProvider, KeepaliveProvider) {
	IdleProvider.idle(1800);// in seconds
	IdleProvider.timeout(15);
	KeepaliveProvider.interval(20);
});

app.run(function($rootScope, Idle, $timeout,$location,$cookieStore) {
	Idle.watch();
	$rootScope.$on('IdleStart', function() {
		$rootScope.ShowSessionPopup = true;
		$rootScope.counter = 10;
		$rootScope.onTimeout = function() {
			$rootScope.counter--;
			mytimeout = $timeout($rootScope.onTimeout, 1000);
		}
		var mytimeout = $timeout($rootScope.onTimeout, 1000);
		$rootScope.stopCounter = function() {
			$rootScope.ShowSessionPopup = false;
			$timeout.cancel(mytimeout);
		}
		loadSessionOutPopup();
	});
	$rootScope.$on('IdleTimeout', function() {
		$('.modal-backdrop').fadeOut();
		$rootScope.ShowSessionPopup = false;
		$location.path( "/logout" );
	});
});
// For session out dialog box
function loadSessionOutPopup() {
	$("#SessionModal").on("show", function() {
		$("#SessionModal a.btn").on("click", function(e) {
			$("#SessionModal").modal('hide');
		});
	});
	$("#SessionModal").on("hide", function() {
		$("#SessionModal a.btn").off("click");
	});

	$("#SessionModal").on("hidden", function() {
		$("#SessionModal").remove();
	});

	$("#SessionModal").modal({
		"backdrop" : "static",
		"keyboard" : true,
		"show" : true
	});
}


