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
		.factory(
				'applicationServices',
				[
						'$rootScope',
						'$http',
						'dialogs',
						'env',
						function($rootScope, $http, dialogs, env) {
							var loadingdialog = angular.element(document
									.querySelector('#loadingdialog'));
							loadingdialog = $(loadingdialog).data('dialog');
							$rootScope.loading_content = "Waiting on operation to complete.";

							var _env = {
								URL : env.get('env_url'),

							}

							var factoryDefinitions = {
								getMonkeyDetails : function(isClient, isAction,
										isOpenStackDetail) {
									var params = "isClient=" + isClient
											+ "&isAction=" + isAction
											+ "&isOpenStackDetail="
											+ isOpenStackDetail;

									loadingdialog.open();
									return $http
											.get(
													_env.URL
															+ '/resiliency-studio-service/api/v1/monkey/details?'
															+ params).success(
													function(data) {
														loadingdialog.close();
														return data;
													}).error(function(data) {
												loadingdialog.close();
												return data;
											});
								},
								getServerStatus : function(applicationReq) {
									return $http
											.post(
													_env.URL
															+ '/resiliency-studio-service/api/v1/monkey/details',
													applicationReq).success(
													function(data) {
														return data;
													}).error(function(data) {
												return data;
											});
								},
								executeEvent : function(url, applicationReq) {
									return $http.post(url, applicationReq)
											.success(function(data) {
												return data;
											}).error(function(data) {
												return data;
											});
								},
								getChaosEvents : function() {

									loadingdialog.open();
									return $http
											.get(
													_env.URL
															+ '/resiliency-studio-service/api/v1/chaos')
											.success(function(data) {
												loadingdialog.close();
												return data;
											}).error(function(data) {
												loadingdialog.close();
												return data;
											});
								},
								getChaosMonkeyEvents : function(url) {
									return $http.get(url).success(
											function(data) {
												return data;
											}).error(function(data) {
										return data;
									});
								}
							}

							return factoryDefinitions;
						} ]);
