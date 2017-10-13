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
Feature: Application section
  As a user
  I want to be able to add a new application for RSA login in the system
 
 @AddApplication-DataDriven
  Scenario: Add a new application
    Given I am enetering credentials for Add Application RSA login
    Given I am on the dash board page for RSA login
    When I enter all the application details in tab one for RSA login
    |applicationName|categoryIndex|environmentIndex|serverName|role|hostName|ipAddress|tierIndex|cpuIndex|memoryIndex|osIndex|storageIndex|osTypeIndex|
    |Rsa test23|1            |1               |server1   |admin |host1             |1.0.0.2|1  |1       |12GB          |1      |12GB        |1|       
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
    
