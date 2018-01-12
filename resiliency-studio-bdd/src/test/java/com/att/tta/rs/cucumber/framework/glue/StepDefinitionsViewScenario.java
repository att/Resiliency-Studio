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
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;
import com.att.tta.rs.cucumber.framework.page.objects.ValidateViewUIData1;
import com.att.tta.rs.cucumber.framework.page.objects.ViewScenarioPage;
import com.att.tta.rs.cucumber.framework.page.objects.ViewScenarioUIData;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class StepDefinitionsViewScenario extends ParentScenario {

	@Before
	public void beforeScenario() {
		startBrowser();
	}

	@Given("^I am entering credentials for view Scenario$")
	public void I_am_entering_credentials_for_view_scenario() throws InterruptedException {

		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}

	@Given("^I am on the dash board page$")
	public void I_am_on_the_dash_board_page() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new ViewScenarioPage(driver).click_link_on_dashboard();
	}

	@When("^I select application name environment name in the view scenario page$")
	public void i_select_application_name_environment_name_in_the_view_scenario_page(List<ViewScenarioUIData> viewList)
			throws Throwable {
		new ViewScenarioPage(driver).select_application(viewList);
	}

	@And("^I click the view button on view scenario page$")
	public void i_click_the_view_button_on_view_scenario_page() {
		new ViewScenarioPage(driver).view_scenario();

	}

	@And("^I should check the table is there or not for view scenario$")
	public void checking_table_is_present() throws InterruptedException {
		new ViewScenarioPage(driver).checking_view_scenario_table_is_present();
	}

	@When("^I need to check the Mandatory fields are filled for view scenario$")
	public void validating_the_mandatory_fields_with_the_given_values(List<ValidateViewUIData1> validateviewdata)
			throws InterruptedException {
		new ViewScenarioPage(driver).checking_Scenario_table_values_over_given_values(validateviewdata);
	}

	@After
	public void afterScenario() {
		// closeBrowser();
	}

}
