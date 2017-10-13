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
Feature: View MonkeyStrategy Page
  As a user I want to be able to View a Monkey Strategy in the system
 
 @ViewMonkeyStrategy-DataDriven
  Scenario: View Monkey Strategy
  
  Given I am entering the credentials for View Monkey Strategy
	  Given I am on the dashboard page for View Monkey Strategy
	 Then I check for the default columns are present on the page load
	 Then I click on the settings and make sure it contains list of expected columns
	 Then I click on the checkbox of one of the listed columns and make sure the table got added with the checked column
	 Then I click on all the checkboxes present in settings button and make sure table got changed
	 Then I check for a particular monkey strategy "TestMonkey2" to validate the details
	 Then I click on the view script link and make sure the script window got loaded 
	 Then I click on the other tabs of monkey types and check for the table change
	 