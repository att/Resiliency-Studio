# * BSD License
# *     
# * Copyright (c) 2017, AT&T Intellectual Property.  All other rights reserved.       
# * 
# *  Redistribution and use in source and binary forms, with or without modification, are permitted
# *   provided that the following conditions are met:
# *
# *   1. Redistributions of source code must retain the above copyright notice, this list of conditions
# *      and the following disclaimer.
# *   2. Redistributions in binary form must reproduce the above copyright notice, this list of
# *      conditions and the following disclaimer in the documentation and/or other materials provided
# *      with the distribution.
# *   3. All advertising materials mentioning features or use of this software must display the
# *      following acknowledgement:  This product includes software developed by the AT&T.
# *   4. Neither the name of AT&T nor the names of its contributors may be used to endorse or
# *      promote products derived from this software without specific prior written permission.
# *
# *   THIS SOFTWARE IS PROVIDED BY AT&T INTELLECTUAL PROPERTY ''AS IS'' AND ANY EXPRESS OR
# *   IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# *   MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
# *   SHALL AT&T INTELLECTUAL PROPERTY BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
# *   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
# *   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  LOSS OF USE, DATA, OR PROFITS;
# *   OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# *   CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
# *   ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
# *   DAMAGE.
# *******************************************************************************
Feature: ADD Scenario section
  As a user
  I want to be able to add a scenario in the System

  @AddScenario
  Scenario: Add a new Scenario
    ##Given I am entering credentials for creating a scenario manual
    Given I am on the dash board page for add scenario
    When I check the monkey strategy dropdowns visible after the page load
    When I enter all the scenario details on add scenario page
      | applicationName | environmentIndex | serverIndex | roleIndex | softwareComponentIndex | failureTenetIndex | failureMode | userBehavior | systemBehavior | potentialCauseFailure | currentControls | detectionMehanism | recoveryMehanism | recommendations | mttd | mttr | failureScript |
      | PrivateKey Test |                1 |           1 |         1 |                      1 |                 1 | mode        | user1.0      | system1.0      | failure cause         | currentcontrols | detect            | recovery         | recommendations |   12 |   12 |             1 |
    When I enter the monkey strategy details seaparately
      | monkeyType   | monkeyStrategyName       | sequence |
      | Latency      | TestMonkey2Clonestrategy |        1 |
      | Chaos Monkey | TestMonkey2              |        2 |
    When I click the submit button on add scenario page
    Then I should check for the values retained properly after submitting the scenario
      | applicationName | environmentIndex | serverIndex | softwareComponentIndex | failureTenetIndex |
      | PrivateKey Test | QA               | server1     | httpd            | Fault             |
    When I enter all the scenario details on add scenario page second time for duplicate scenario creation
      | roleIndex | failureMode | monkeyType | monkeyStrategy           | sequence |
      |         1 | mode        | Latency    | TestMonkey2Clonestrategy |        1 |
    When I click the submit button on add scenario page for duplicate scenario creation
    Then I validate the error text for the duplicate Scenario
    And I click the button on add scenario page