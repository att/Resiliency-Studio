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
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;
import com.att.tta.rs.cucumber.framework.page.objects.ScenarioDeletePage;
import com.att.tta.rs.cucumber.framework.page.objects.ScenarioDeleteUIData;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitionScenarioDelete extends ParentScenario {

	@Before
	public void beforeScenario() {
		startBrowser();
	}

	@Given("^I am entering credentials for deleting scenario$")
	public void I_am_entering_credentials_for_deleting_scenario() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}

	@Given("^I am on the dash board page for scenario delete$")
	public void I_am_on_the_dash_board_page_for_scenario_delete() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new ScenarioDeletePage(driver).click_scenario_delete();
	}

	@When("^I select the applicationName and environment on View Scenario page$")
	public void I_select_the_applicationName_and_environment_on_View_Scenario_page(
			List<ScenarioDeleteUIData> deleteList) throws InterruptedException {
		new ScenarioDeletePage(driver).select_application_details(deleteList);
	}

	@When("^I click on view button for delete a scenario on view scenario page$")
	public void I_click_on_view_button_for_delete_a_scenario_on_view_scenario_page() throws InterruptedException {
		new ScenarioDeletePage(driver).click_on_view();
	}

	@When("^I mark a check box to delete a scenario$")
	public void I_mark_a_ckeck_box_to_delete_a_scenario() throws InterruptedException {
		new ScenarioDeletePage(driver).click_checkbox();
	}

	@When("^I click on delete button on view scenario page$")
	public void I_click_on_delete_button_on_view_scenario_page() throws InterruptedException {
		new ScenarioDeletePage(driver).click_on_delete();
	}

	@When("^I click on yes button to conform delete$")
	public void I_click_on_yes_button_to_confirm_delete() throws InterruptedException {
		new ScenarioDeletePage(driver).click_to_confirm_yes();
	}

	@When("^I select application and environment name to delete in view scenario page$")
	public void I_select_application_and_environment_to_delete(List<ScenarioDeleteUIData> deleteList) throws InterruptedException{
		new ScenarioDeletePage(driver).select_application_details(deleteList);
		new ScenarioDeletePage(driver).click_on_view();
	}
	
	@Then("^I delete scenarios created through autodiscovery$")
	public void dleting_scenarios_created_through_autodiscovery() throws InterruptedException{
		new ScenarioDeletePage(driver).deleting_all_scenarios_at_a_time();
	}
	
	@Then("^I click on delete for deleting all scenarios$")
	public void clicking_on_delete_for_all_scenarios()throws InterruptedException{
		new ScenarioDeletePage(driver).click_on_delete();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		new ScenarioDeletePage(driver).click_to_confirm_yes();
	}
	
}
