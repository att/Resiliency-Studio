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
#

Feature: Resiliency studio Application section
  As a user
  I want to be able to validate the Resiliency studio application
 
 @RSApplication-DataDriven
  Scenario: Testing Resiliency studio application features
  Given I am on the login page for authenticate
    When I enter the username and password on login page
    When I click the logon button on login page
    ###########################Add Application##############################
    Given I am on the dash board page for add application
    When I enter all the application details in tab one
    |applicationName|categoryIndex|environmentIndex|serverName|role|hostName|ipAddress|tierIndex|cpuIndex|memoryIndex|osIndex|storageIndex|osTypeIndex |
    |CUCUMBERTEST|1            |1               |server-update |admin |hostname       |1.0.0.0|1        |1       |12GB          |UNIX     |12GB        |1 |
   When I click the next button on add application page
   When I enter all the application details in tab two
  |userName|password|
  | sensk|hellonwheels|
   When I click the next button on add application page
   When I enter all the application details in tab three
   |softwareComponentName|processName|
   |httpd|java|
   When I click the next button on add application page
   When I enter all the application details in tab four
   |discoveryName|discoveryApi|
   |discovery1|https://discovery/|
   When I click the next button on add application page
   When I enter all the application details in tab five
   |counterType|monitorApi|
   |counterType|https://monitor/|
   When I click the next button on add application page 
   When I enter all the application details in tab six
   |logType|logLocation|
   |sl4j|http://tmp/log/|
   When I click the submit button on add application page
   And I click view application link on add application page
   ############################Add ApplicationRSA###########################
   Then I am going to another feature of resiliency studio application
   Given I am on the dash board page for RSA login
    When I enter all the application details in tab one for RSA login
    |applicationName|categoryIndex|environmentIndex|serverName|role|hostName|ipAddress|tierIndex|cpuIndex|memoryIndex|osIndex|storageIndex|osTypeIndex|
    |RSA Test|1            |1               |server1   |admin |host1             |1.0.0.2|1  |1       |12GB          |1      |12GB        |1|       
   When I click the next button on add application page for RSA login
   When I enter all the application details in tab two for RSA login
   |userName|
  |user    | 
   When I click the next button on add application page for RSA login
   When I enter all the application details in tab three for RSA login
   |softwareComponentName|processName|
   |httpd|web server|
   When I click the next button on add application page for RSA login
   When I enter all the application details in tab four for RSA login
   |discoveryName|discoveryApi|
   |discovery1|https://discovery/|
   When I click the next button on add application page for RSA login
   When I enter all the application details in tab five for RSA login
   |counterType|monitorApi|
   |counterType|https://monitor/|
   When I click the next button on add application page for RSA login 
   When I enter all the application details in tab six for RSA login
   |logType|logLocation|
   |sl4j|http://tmp/log/|
   When I click the submit button on add application page for RSA login
   And I click view application link on add application page for RSA login
   #################################Add Application PrivateKey###########################
    Then I am going to another feature of resiliency studio application
    Given I am on the dash board page for Private Key
    When I enter all the application details in tab one for Private Key
    |applicationName|categoryIndex|environmentIndex|serverName|role|hostName|ipAddress|tierIndex|cpuIndex|memoryIndex|osIndex|storageIndex|osTypeIndex|
    |PrivateKey Test|1            |1               |server1   |admin |host1   |1.0.0.3|1  |1       |12GB          |1      |12GB        |1          |      
   When I click the next button on add application page for Private Key
   When I enter all the application details in tab two for Private Key
 |userName|privateKey| 
  | user    |private   | 
   When I click the next button on add application page for Private Key
   When I enter all the application details in tab three for Private Key
   |softwareComponentName|processName|
   |httpd|web server|
   When I click the next button on add application page for Private Key
   When I enter all the application details in tab four for Private Key
   |discoveryName|discoveryApi|
   |discovery1|https://discovery/|
   When I click the next button on add application page for Private Key
   When I enter all the application details in tab five for Private Key
   |counterType|monitorApi|
   |counterType|https://monitor/|
   When I click the next button on add application page for Private Key 
   When I enter all the application details in tab six for Private Key
   |logType|logLocation|
   |sl4j|http://tmp/log/|
   When I click the submit button on add application page for Private Key
   And I click view application link on add application page for Private Key
   #################################View Application#####################################
   
    ################################Add MonkeyStartegy######################################
    Then I am going to another feature of resiliency studio application
    Given I am on the dashboard page for Add MonkeyStrategy
	  Then I fill up the details in basic tab for add monkey strategy and check the version field is disabled
	  |monkeyStrategyName|monkeyType|
      |ProcessStatus|Latency|
	  Then I will upload the Script and try to modify the code script
	  Then I add the monkeystrategy only by filling the basic tab and validate the success message 
	 Then I click on the add monkeystrategy and filling all the basictab data 
	  Then I add the monkeystrategy by making the generic toggle on and verifying all the fields in the advance tab
	  |osType|flavor|failureCategory|failureSubCategory|
	  |1	|1	|software	|all|
	  Then I click on add button and verify the success message and make sure generic toggle is off
	  Then I click the generic toggle is on and click on the reset and make sure the generic toggle is off
	  Then I add a monkey strategy with same name and verify the error message only by filling the basic tab data
	  ##################################View Monkey Startegy#######################################
	  Then I am going to another feature of resiliency studio application
	  Given I am on the dashboard page for View Monkey Strategy
	 Then I check for the default columns are present on the page load
	 Then I click on the settings and make sure it contains list of expected columns
	 Then I click on the checkbox of one of the listed columns and make sure the table got added with the checked column
	 Then I click on all the checkboxes present in settings button and make sure table got changed
	 Then I check for a particular monkey strategy "TestMonkey2" to validate the details
	 Then I click on the view script link and make sure the script window got loaded 
	 Then I click on the other tabs of monkey types and check for the table change
	 ###############################Delete Application#####################################
     Then I am going to another feature of resiliency studio application
    Given I am on the dash board page for application delete
    When I enter the application delete name as "RSA Test"
    When I click on delete icon button to delete a application
    And I click yes button on delete application page
    When I enter the application delete name as "CUCUMBERTEST"
    When I click on delete icon button to delete a application
    And I click yes button on delete application page
    When I enter the application delete name as "PrivateKey Test"
    When I click on delete icon button to delete a application
    And I click yes button on delete application page
	 ##################################Delete MonkeyStartegy###########################################
	 Then I am going to another feature of resiliency studio application
	 Given I am on the dashboard page for Delete MonkeyStrategy
	  When I enter the monkeytype and monkeystrategy to delete
	  |monkeyStrategyName|monkeyType|
   |ProcessStatus|LATENCY|
    |TestMonkey2|CHAOS|
	  When I click the delete icon on MonkeyStrategy page
	  When I click the yes to confirm delete on MonkeyStrategy
	  