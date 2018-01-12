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
Feature: Delete Scenario Page
  As a user I want to be able to delete a Scenario in the system

  @DeleteScenario
  Scenario: Delete scenario
    ##Given I am entering credentials for deleting scenario
    Given I am on the dash board page for scenario delete
    When I select the applicationName and environment on View Scenario page
      | applicationName | environmentSelector |
      | PrivateKey Test |                   1 |
    When I click on view button for delete a scenario on view scenario page
    When I mark a check box to delete a scenario
    When I click on delete button on view scenario page
    When I click on yes button to conform delete
    When I select application and environment name to delete in view scenario page
       |applicationName|environmentSelector|
       |CUCUMBERTEST    |1                  |
      Then I delete scenarios created through autodiscovery
      Then I click on delete for deleting all scenarios