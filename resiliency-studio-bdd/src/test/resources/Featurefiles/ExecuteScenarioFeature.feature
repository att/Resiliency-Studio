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
#
#	Ansible based strategy for simulating network latency
#@author sk494t
Feature: Scenario section
  As a user
  I want to be able to execute a scenario in the System

  @ExecuteScenario
  Scenario: Execute the single scenario details
    ##Given I am entering the credentials for Execute Scenario
    Given I am on the dash board page for execute single scenario
    When I select the applicationName and environment on execute scenario page
      | applicationName | environmentIndex |
      | PrivateKey Test |                1 |
    When I click the list button on execute scenario page
    When I click on the Runnow button with out selecting the scenario
    Then I validate the error message and close the pop up window
    And I validate the mandatory fields are filled with exepected values
      | serverName | failureTenet | failureMode | monkeyStrategy                       |
      | server1    | Fault        | mode        | TestMonkey2Clonestrategy,TestMonkey2 |
    When I mark a scenario on execute scenario page
    When I click the run now button on execute scenario page
    Then I check for the execution event widget is appeared
    Then I check the data inside execution event widget is as expected
    Then I check Monitoring widget is appeared on the page after clicking run now
    Then I check Log widget is appeared on the page after clicking on run now
