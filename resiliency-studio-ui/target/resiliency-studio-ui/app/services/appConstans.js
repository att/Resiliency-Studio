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
				'constantService',
				function() {
					var constant = {
						getFailures : '/resiliency-studio-ui/app/json/failures.json',
						getapisession : '/resiliency-studio-security/api/session/',
						getAuthorization : '/resiliency-studio-security/api/user/',
						getDefaultTeam : '/resiliency-studio-service/api/user/',
						getMonkeyStrategies : '/resiliency-studio-service/api/monkeystrategies/team-strategies/',
						getMonkeyType : '/resiliency-studio-service/api/monkeytypes/',
						getApplicationCount : '/resiliency-studio-service/api/applications/count/',
						getScenarioCount : '/resiliency-studio-service/api/scenarios/count/',
						getEventsCount : '/resiliency-studio-service/api/events/count/',
						getMonkeyStrategiesCount : '/resiliency-studio-service/api/monkeystrategies/team-strategies/',
						addMonkeyStrategy : '/resiliency-studio-service/api/monkeystrategy/',
						changeTeam : '/resiliency-studio-service/api/user/',
						addapplication : '/resiliency-studio-service/api/application/',
						apllicationEnvInfo : '/resiliency-studio-service/api/applications/',
						viewApplicationTeamInfo : '/resiliency-studio-service/api/applications/team/',
						deleteapplicationbyid : '/resiliency-studio-service/api/applications/',
						deleteStrategybyId : '/resiliency-studio-service/api/monkeystrategies/',
						updateMonkeyStrategy : '/resiliency-studio-service/api/monkeystrategies/',
						updateapplicationbyid : '/resiliency-studio-service/api/applications/',
						getApplicationList : '/resiliency-studio-service/api/applications/team/',
						addScenario : '/resiliency-studio-service/api/scenario/',
						getScenariosByApplicationName : '/resiliency-studio-service/api/scenarios/scenariosby-appname/',
						getScenarioById : '/resiliency-studio-service/api/scenarios/',
						updateScenario : '/resiliency-studio-service/api/scenarios/',
						getFailureTenet : 'app/json/add-scenario.json',
						searchFailurepointsByAppEnv : '/resiliency-studio-service/api/failurepoints/',
						deleteSelectedScenarios : '/resiliency-studio-service/api/scenarios/bulk/',
						scenarioRunYTD : '/resiliency-studio-service/api/events/team/',
						executeScenario : '/resiliency-studio-service/api/executescenario/',
						getApplication : '/resiliency-studio-service/api/application/',
						executeBulkScenario : '/resiliency-studio-service/api/executebulkscenario/',
						addFailurePoint : '/resiliency-studio-service/api/failurepoint/',
						getFailurePoint : '/resiliency-studio-service/api/failurepoints/',
						getFailurePointByCategory : '/resiliency-studio-service/api/failurepoints/category/',
						updateFailurePoint : '/resiliency-studio-service/api/failurepoints/',
						deleteFailurePointbyid : '/resiliency-studio-service/api/failurepoints/',
						getFailurePointsByAppEnv : '/resiliency-studio-service/api/monkeystrategies/autodiscover/',
						discoverServerDetails: '/resiliency-studio-service/api/applications/discoverServerDetails/',
						bulkAddScenario : '/resiliency-studio-service/api/scenarios/bulk/',
					};
					var getConstants = function() {
						return constant;
					};
					return {
						constants : getConstants()
					}
				});