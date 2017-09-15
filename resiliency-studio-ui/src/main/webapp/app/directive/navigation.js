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

app.directive('navigation', function() {
	return {
		templateUrl : 'app/modules/dashboard/navbar.html',
		controller : function($scope, $cookieStore, $http, $rootScope,$route, $location,resiliencyStudioService) {
			
			$rootScope.team = [];
			$rootScope.role = [];
			$rootScope.isLogin=true;
			if ($cookieStore.get('username') != null) {
				$("#navigation").css('display','block');
				$scope.selectedTeam = $cookieStore.get('selectedTeam');
				$rootScope.isLogin=true;
				$scope.username = $cookieStore.get('username');
				$scope.teamArray = $cookieStore.get('teams');
				$scope.teamArray = $scope.teamArray.toString().split(",");
				$rootScope.roleArray = $cookieStore.get('role');
				$scope.roleArray = $scope.roleArray.toString().split(",");
				$rootScope.UserPrivilege = $cookieStore.get('userprivilege');
				$rootScope.selectedTeam = $cookieStore.get('selectedTeam');
				$scope.teams = $rootScope.team;
				if($rootScope.teamArray != undefined || $rootScope.teamArray != null)
					$scope.selectedRole = $rootScope.role[$rootScope.teamArray.indexOf($scope.selectedTeam)];
				$scope.teams = $rootScope.teamArray;
			
			} else {
				$location.path("/");
			}
			
			//Team changing service call
			$scope.teamClicked = function(newTeam) {
				resiliencyStudioService.changeTeam(newTeam).then(
		    			function(data){
		    				$cookieStore.put("selectedTeam",newTeam);
		    				$rootScope.selectedTeam = $cookieStore.get('selectedTeam');
		    				$route.reload();
		    			},function(error){
		    				console.log("error occured while switch the team ")
		    			});
				$scope.selectedTeam = newTeam;
				localStorage.setItem('selectedTeam', newTeam);
				$scope.selectedRole = $rootScope.roleArray[$rootScope.teamArray.indexOf(newTeam)];
				$rootScope.UserPrivilege =  $scope.selectedRole;
				$cookieStore.put('userprivilege',$rootScope.UserPrivilege);
				
				if($location.path()!="/dashboard"){
					$location.path("#/dashboard");
				}
			}
		}
	}
});
