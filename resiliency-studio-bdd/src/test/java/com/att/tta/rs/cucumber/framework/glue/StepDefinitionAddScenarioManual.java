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
import com.att.tta.rs.cucumber.framework.page.objects.AddScenarioManualPage;
import com.att.tta.rs.cucumber.framework.page.objects.AddScenarioManualUIData;
import com.att.tta.rs.cucumber.framework.page.objects.CloneMonkeyStrategyBasicTabUIData;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitionAddScenarioManual extends ParentScenario {

	@Before
	public void beforeScenario() {
		startBrowser();
	}

	@Given("^I am entering credentials for creating a scenario manual$")
	public void I_am_entering_credentials_for_creating_a_scenario_manual() throws InterruptedException {

		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}

	@Given("^I am on the dash board page for add scenario$")
	public void I_am_on_the_dash_board_pagr_for_add_scenario() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new AddScenarioManualPage(driver).click_add_scenario_link();
	}

	@When("^I enter all the scenario details on add scenario page$")
	public void I_enter_all_the_scenario_details_on_add_scenario_page(List<AddScenarioManualUIData> addScenarioList)
			throws InterruptedException {
		new AddScenarioManualPage(driver).fill_scenario_details(addScenarioList);

	}

	@When("^I click the submit button on add scenario page$")
	public void I_click_the_submit_button_on_add_scenario_page() throws InterruptedException {
		new AddScenarioManualPage(driver).submit_scenario();

	}

	@And("^I click the button on add scenario page$")
	public void I_click_the_button_on_add_scenario_page() throws InterruptedException {
		new AddScenarioManualPage(driver).click_ok_button();
	}

	@Then("^I should check for the values retained properly after submitting the scenario$")
	public void checking_wheather_the_values_retained_properly_after_submitting_the_scenario(
			List<AddScenarioManualUIData> addScenarioList) throws InterruptedException {
		new AddScenarioManualPage(driver).checking_the_values_retained_properly_after_submission(addScenarioList);
	}

	@When("^I enter all the scenario details on add scenario page second time for duplicate scenario creation$")
	public void entering_all_the_scenario_details_second_time(List<AddScenarioManualUIData> addScenarioList)
			throws InterruptedException {
		new AddScenarioManualPage(driver).fill_scenario_details_second_time(addScenarioList);
	}

	@When("^I click the submit button on add scenario page for duplicate scenario creation$")
	public void i_click_the_submit_button_on_add_scenario_page_for_duplicate_scenario_creation() throws Throwable {

		new AddScenarioManualPage(driver).submit_scenario();
	}

	@Then("^I validate the error text for the duplicate Scenario$")
	public void i_validate_the_error_text_for_the_duplicate_Scenario() throws Throwable {
		new AddScenarioManualPage(driver).validate_the_error_text();
	}

	// new implementations for multiple monkeys
	@When("^I check the monkey strategy dropdowns visible after the page load$")
	public void i_check_the_monkey_startegy_dropdowns_visible_after_the_page_load() throws Throwable {
		new AddScenarioManualPage(driver).validating_monkey_dropdowns_are_visible();
	}

	@When("^I enter the monkey strategy details seaparately$")
	public void i_enter_the_monkey_strategy_details_seaparately(List<CloneMonkeyStrategyBasicTabUIData> monkeydata)
			throws Throwable {
		// pending due to page not having proper id's
		// for this may be i need to create whole new automation scripts// need
		// to try
		new AddScenarioManualPage(driver).adding_multiple_monkeys_successfully(monkeydata);
	}

}
