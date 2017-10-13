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
Feature: Edit MonkeyStrategy Page
  As a user I want to be able to edit a Monkey Strategy in the system
 
 @AddMonkeyStrategy-DataDriven
  Scenario: Add Monkey Strategy
  
  Given I am entering the credentials for Add Monkey Strategy
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
	    
	  
	   