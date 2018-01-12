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

import com.att.tta.rs.cucumber.framework.ParentScenario;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;
import com.att.tta.rs.cucumber.framework.page.objects.ViewMonkeyStrategy;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class StepDefinitionViewMonkeyStrategiesFeature extends ParentScenario {

	@Before
	public void beforeScenarioCases() throws InterruptedException {
		startBrowser();
	}

	@Given("^I am entering the credentials for View Monkey Strategy$")
	public void entering_credentials_for_view_monkey_Startegies() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();
	}

	@Given("^I am on the dashboard page for View Monkey Strategy$")
	public void view_monkey_strategies_dashboard_click() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new ViewMonkeyStrategy(driver).clicking_on_view_monkey();
	}

	@Then("^I check for the default columns are present on the page load$")
	public void check_for_default_columns_in() throws InterruptedException {
		new ViewMonkeyStrategy(driver).default_column_check_after_page_load();
	}

	@Then("^I click on the settings and make sure it contains list of expected columns$")
	public void click_on_setting_button_to_check_list_of_expected_columns() throws InterruptedException {
		new ViewMonkeyStrategy(driver).clicking_on_setting_button_and_check_for_the_expected_columns();
	}

	@Then("^I click on the checkbox of one of the listed columns and make sure the table got added with the checked column$")
	public void after_clicking_on_checkbox_table_should_reflect() throws InterruptedException {
		new ViewMonkeyStrategy(driver).clicking_on_checkbox_change_on_view_table();
	}

	@Then("^I click on all the checkboxes present in settings button and make sure table got changed$")
	public void clicking_allcheckboxes_of_settings() throws InterruptedException {
		new ViewMonkeyStrategy(driver).clicking_all_checkboxes_and_change_view();
	}

	@Then("^I check for a particular monkey strategy \"([^\"]*)\" to validate the details$")
	public void i_check_for_a_particular_monkey_strategy_to_validate_the_details(String s) throws Throwable {
		new ViewMonkeyStrategy(driver).checking_the_particularmonkey_exist(s);
	}

	@Then("^I click on the view script link and make sure the script window got loaded$")
	public void click_on_view_script_link() throws InterruptedException {
		new ViewMonkeyStrategy(driver).clicking_on_view_script_link();
	}

	@Then("^I click on the other tabs of monkey types and check for the table change$")
	public void click_on_other_tabs_of_monkey_type() throws InterruptedException {
		new ViewMonkeyStrategy(driver).click_on_other_tabs_of_monkey_type();
	}

}
