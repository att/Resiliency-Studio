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
app.controller('authCtrl', ['$scope','env','constantService','$timeout','$rootScope','$location','$cookieStore','$cookies','$http','resiliencyStudioService',  function($scope,env,constantService,$timeout,$rootScope,$location,$cookieStore,$cookies,$http,resiliencyStudioService) {
	
	$rootScope.sideBar = ""; //initializing sidebar to hidden
	$("#navigation").css('display','none'); // hiding top navigation bar
	
	var teamArray = [];
	var roleArray = [];
	
	//Initiate Login
	$scope.perfomLogin = function(){
		$scope.isLogining = true;
		$http.defaults.headers.common['Authorization'] = ''; // Initialize  Authorization header 
		 $scope.logindetails ={usrid:"", pwdkey:""}; //Initialize userid and password 
		 $scope.logindetails.usrid =$scope.username; // Assigned username val from scope variable 
		 $scope.logindetails.pwdkey =$scope.password; //Assigned password val from scope variable
		$http.defaults.headers.common.Authorization = "Basic c2VjdXJlTGF5ZXIxMjNyc3w6TE9HSU4="; //Assigned authorization header value
		var data = JSON.stringify($scope.logindetails); //Generating string value of login details in form of Json object 
		
		 //Get method for Authentication 
	       resiliencyStudioService.getAuthentication(data).then(function(success) {
											$scope.gotoAuthorization(); 
											var _base64 = {
													_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
													encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=_base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},
													decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9+/=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=_base64._utf8_decode(t);return t},
													_utf8_encode:function(e){e=e.replace(/rn/g,"n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},
													_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}
												}
											$http.defaults.headers.common.Authorization = "Basic " +_base64.encode($scope.username + '|' + ':' + 'LOGIN');
										}, function(error) {
											console.log(error) // log return error
											$timeout(function() {
												  $scope.msg = "Login Failed!";
													$scope.msgClass = "bg-danger";
												}, 100); 
										});
	     
	}
	
	//Authorization Service call
	$scope.gotoAuthorization = function(){
		resiliencyStudioService.getAuthorization($scope.username).then(
    			function(resultUserRole){ //return resultUserRole in form of json with team and their respective roles
    				roleArray = []; 
    				teamArray = []; 
		    		for(var i=0;i<resultUserRole.length;i++){
		    			teamArray.push(resultUserRole[i].teamname); // retrieving teamname and push to teamArray
		    			roleArray.push(resultUserRole[i].role); //retrieving role for respective team and pushing to roleArray at same index of team name
		    		}
    				$scope.gotoDefaultTeam(); // calling gotoDefaultTeam scope function
    			},function(error){
    				console.log(error) // error log return over console.
    			}		
    			);
	}
	
	//get Default team Service call
	$scope.gotoDefaultTeam = function(){
		
		resiliencyStudioService.getDefaultTeam().then(
    			function(data){
    				$rootScope.selectedTeam = data.teamName; //Assigning selectedteam 
					$rootScope.teamArray = teamArray; //Assigning teamArray to rootScope variable
					$rootScope.roleArray = roleArray; //Assigning roleArray to rootScope variable
					$rootScope.UserPrivilege =  roleArray[$rootScope.teamArray.indexOf(data.teamName)]; //Assigning user privilege respective to selected team
					$cookieStore.put('userprivilege',$rootScope.UserPrivilege); //Storing user privilege in cookie for global access in application
					$timeout(function() {
						$("#navigation").css('display','block'); // Making top navigation visible
						$("#myNavbar").removeClass('in'); //Collapsing top navigation if expanded for login page
						$rootScope.username = $scope.username; // Assigning username to rootscope variable for global access.
						$cookieStore.put('username', $scope.username); //Storing username in cookie (if user refresh the page still available from userdetails with cookies)
						$cookieStore.put('selectedTeam',$rootScope.selectedTeam); //storing selected team in cookie
						$cookieStore.put('teams',teamArray); //storing team array in cookie
						$cookieStore.put('role',roleArray); //storing role array in cookie
						$location.path("/dashboard"); //navigating app to dashboard
					}, 100);
				
    			},function(error){
    				 $timeout(function() {
						  $scope.msg = "Login Failed!";
							$scope.msgClass = "bg-danger";//Failed message display while entering wrong credential
						}, 100);
    			}		
    			);
	}
	
	//Login button enable disable 
	$scope.userDetailsChange = function(){
		$scope.isLogining = false;
		 $scope.msg = "";
		}
	
}]);










