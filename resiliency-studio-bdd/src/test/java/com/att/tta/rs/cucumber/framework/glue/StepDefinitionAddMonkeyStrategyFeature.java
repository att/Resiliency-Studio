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
package com.att.tta.rs.cucumber.framework.glue;

import java.util.List;

import com.att.tta.rs.cucumber.framework.ParentScenario;
import com.att.tta.rs.cucumber.framework.page.objects.AddMonkeyStrategyFeature;
import com.att.tta.rs.cucumber.framework.page.objects.CloneMonkeyStrategyAdvanceTabUIData;
import com.att.tta.rs.cucumber.framework.page.objects.CloneMonkeyStrategyBasicTabUIData;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class StepDefinitionAddMonkeyStrategyFeature extends ParentScenario {
	@Before
	public void beforeScenarioCases() throws InterruptedException {
		startBrowser();
	}

	@Given("^I am entering the credentials for Add Monkey Strategy$")
	public void log_dashboard_for_add_monkey() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}

	@Given("^I am on the dashboard page for Add MonkeyStrategy$")
	public void clicking_on_add_monkeystrategy_button() throws InterruptedException {
		new AddMonkeyStrategyFeature(driver).click_on_addmonkey();
	}

	@Then("^I fill up the details in basic tab for add monkey strategy and check the version field is disabled$")
	public void filling_only_basic_tab_fields(List<CloneMonkeyStrategyBasicTabUIData> basicdata)
			throws InterruptedException {
		// need t send the data for the basic tab
		new AddMonkeyStrategyFeature(driver).fill_the_basic_tab_data(basicdata);
	}

	@Then("^I will upload the Script and try to modify the code script$")
	public void fill_up_the_uploadscript_and_codescript() throws InterruptedException {
		// upload the script and code the script and do a permutation like that
		new AddMonkeyStrategyFeature(driver).modify_the_script();
	}

	@Then("I add the monkeystrategy only by filling the basic tab and validate the success message")
	public void adding_monkeystrategy_validating_successmessage() throws InterruptedException {
		// click on add button and verify the success message and see the all
		// the filled fields got reset
		new AddMonkeyStrategyFeature(driver).add_monkey_and_verify_success_message();
	}

	@Then("^I click on the add monkeystrategy and filling all the basictab data$")
	public void clicking_on_addmonkesystrategy_button_and_again_fill_basicdata() throws InterruptedException {
		// clicking on the add monkey strategy button which is on side of html
		// and fill the basic tab data
		new AddMonkeyStrategyFeature(driver).click_on_addmonkey_of_html_fillup_basictab_data();
	}

	@Then("^I add the monkeystrategy by making the generic toggle on and verifying all the fields in the advance tab$")
	public void filling_the_advance_tab_data_making_the_generic_toogle_on(
			List<CloneMonkeyStrategyAdvanceTabUIData> advancetabdata) throws InterruptedException {
		// clicking on the advance tab and clicking on the generic toggle on and
		// verifying all the fields in the advance tab
		// send the values for the advance tab fields
		new AddMonkeyStrategyFeature(driver).filling_advance_tab_data_verifying_mandtoryfields(advancetabdata);

	}

	@Then("^I click on add button and verify the success message and make sure generic toggle is off$")
	public void clicking_on_the_addbutton_verify_the_successmessage() throws InterruptedException {
		// click on the add button and verify the success message and make sure
		// generic toggle is off
		new AddMonkeyStrategyFeature(driver).add_the_monkeystrategycheck_on_toggle();
	}

	@Then("^I add a monkey strategy with same name and verify the error message only by filling the basic tab data$")
	public void filling_up_the_basictab_data_with_same_monkeystrategyname_and_verify_the_errormessage()
			throws InterruptedException {
		// fill up the basic tab data with same monkey strategy and verify the
		// error message
		new AddMonkeyStrategyFeature(driver).adding_monkey_with_samename_and_validate_error_message();
	}

	@Then("^I click the generic toggle is on and click on the reset and make sure the generic toggle is off$")
	public void checking_the_reset_by_clicking_on_toggle_on_and_reset() throws InterruptedException {
		new AddMonkeyStrategyFeature(driver).click_on_toggle_and_reset();
	}

}
