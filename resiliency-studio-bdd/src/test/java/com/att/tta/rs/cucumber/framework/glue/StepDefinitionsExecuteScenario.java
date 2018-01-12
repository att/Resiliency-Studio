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
import com.att.tta.rs.cucumber.framework.page.objects.ExecuteScenarioPage;
import com.att.tta.rs.cucumber.framework.page.objects.ExecuteScenarioUIData;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;
import com.att.tta.rs.cucumber.framework.page.objects.ValidateViewUIData1;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitionsExecuteScenario extends ParentScenario {

	@Before
	public void beforeScenario() {
		startBrowser();
	}

	@Given("^I am entering the credentials for Execute Scenario$")
	public void i_am_entering_the_credentials_for_Execute_Scenario() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}

	@When("^I am on the dash board page for execute single scenario$")
	public void I_am_on_the_dash_board_page_for_execute_single_scenario() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new ExecuteScenarioPage(driver).click_execute_single_scenario_link();
	}

	@When("^I select the applicationName and environment on execute scenario page$")
	public void I_select_the_applicationName_and_environment_on_execute_sceanrio_page(
			List<ExecuteScenarioUIData> executeList) throws InterruptedException {
		new ExecuteScenarioPage(driver).select_application_details(executeList);
	}

	@When("^I click the list button on execute scenario page$")
	public void I_click_the_list_button_on_execute_scenario_page() throws InterruptedException {
		new ExecuteScenarioPage(driver).click_list_button();
	}
	
	@When("^I click on the Runnow button with out selecting the scenario$")
	public void i_click_on_the_Runnow_button_with_out_selecting_the_scenario() throws Throwable {
		new ExecuteScenarioPage(driver).click_run_now();
	}

	@Then("^I validate the error message and close the pop up window$")
	public void i_validate_the_error_message_and_close_the_pop_up_window() throws InterruptedException {
		new ExecuteScenarioPage(driver).error_window_validation();
	}
	
	@And("I validate the mandatory fields are filled with exepected values")
	public void I_validate_mandatory_fields_filled_with_expected_values_in_executeScenario(
			List<ValidateViewUIData1> validateviewdata) throws InterruptedException {
		new ExecuteScenarioPage(driver).validating_mandatory_fields_of_scenario(validateviewdata);
	}
	
	@When("^I mark a scenario on execute scenario page$")
	public void I_mark_a_scenario_on_execute_scenario_page() throws InterruptedException {
		new ExecuteScenarioPage(driver).mark_scenario();
	}

	@When("^I click the run now button on execute scenario page$")
	public void I_click_the_run_now_button_on_execute_scenario_page() throws InterruptedException {
		new ExecuteScenarioPage(driver).click_run_now();
	}

	@Then("^I check for the execution event widget is appeared$")
	public void i_check_for_the_monitoring_widget_is_appeared() throws Throwable {
		new ExecuteScenarioPage(driver).execution_event_widget_appeared();
	}

	@Then("^I check the data inside execution event widget is as expected$")
	public void I_check_the_data_inside_execution_widget_is_as_expected() throws Throwable {
		new ExecuteScenarioPage(driver).checking_data_inside_execution_event_widget();
	}

	@Then("^I check Monitoring widget is appeared on the page after clicking run now$")
	public void i_check_Monitoring_widget_is_appeared_on_the_page_after_clicking_run_now() throws Throwable {
		new ExecuteScenarioPage(driver).checking_monitoring_widget_is_appeared();
	}

	@Then("^I check Log widget is appeared on the page after clicking on run now$")
	public void i_check_Log_widget_is_appeared_on_the_page_after_clicking_on_run_now() throws Throwable {
		new ExecuteScenarioPage(driver).checking_logging_widget_is_appeared();
	}
	
}
