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
import com.att.tta.rs.cucumber.framework.page.objects.CreateScenarioAutoDiscoveryPage;
import com.att.tta.rs.cucumber.framework.page.objects.AddScenarioAutoDiscoveryUIData;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitionCreateScenarioAutoDiscovery extends ParentScenario{
	
	@Before
    public void beforeScenario() {
        startBrowser();
    }
	
	@Given("^I am entering credentials for scenario autodiacovery$")
	public void entering_credentials_for_scenario_autodiscovery() throws InterruptedException{
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}
	@Given("^I am on the dash board page for scenario auto discovery$")
	public void I_am_on_the_dash_board_page_for_scenario_auto_discovery() throws InterruptedException{
		navigateTo("/resiliency-studio-ui/#/dashboard");
		 new CreateScenarioAutoDiscoveryPage(driver).click_scenario_auto_discovery();
	}
	
	@When("^I select the applicationName and environmentName on add scenario page$")
	public void I_select_the_applicationName_and_environmentName_on_add_scenario_page(List<AddScenarioAutoDiscoveryUIData> autoDiscoveryList) throws InterruptedException{
		new CreateScenarioAutoDiscoveryPage(driver).select_application_details(autoDiscoveryList);
	}
	
	@When("^I click the auto discovery button on add scenario page$")
	public void I_click_the_auto_discovery_button_on_add_scenario_page() throws InterruptedException{
		new CreateScenarioAutoDiscoveryPage(driver).click_auto_discovery_button();
	}
	
	@When("^I open the metric details on add scenario page$")
	public void I_open_the_metric_details_on_add_scenario_page() throws InterruptedException{
		new CreateScenarioAutoDiscoveryPage(driver).open_metric_details();
	}
	
    @When("^I open the scenario details on add scenario page$")
    public void I_open_the_scenario_details_on_add_scenario_page() throws InterruptedException{
    	new CreateScenarioAutoDiscoveryPage(driver).open_scenario_details();
    }
    
    @Then("^I check the Failuretenet and failure mode are as expected$")
    public void checking_the_failure_tenet_failure_mode_as_expected() throws InterruptedException{
    
    	new CreateScenarioAutoDiscoveryPage(driver).validating_failure_tenet_and_failure_mode_are_as_expected();
    }
    @When("^I check the monkey strategies resulted are as expected$")
    public void checking_the_monkey_srategies_are_as_expected()throws InterruptedException{
    	new CreateScenarioAutoDiscoveryPage(driver).validating_the_resultantmonkeys_as_expected();
    	}
    
    @When("^I click the submit button on create scenario page$")
    public void I_click_the_submit_button_on_create_scenario_page() throws InterruptedException{
    	new CreateScenarioAutoDiscoveryPage(driver).click_submit();
    }
    
    @And("^I click the button on create scenario page$")
    public void I_click_the_button_on_create_scenario_page() throws InterruptedException{
    	new CreateScenarioAutoDiscoveryPage(driver).click_ok_button();
    }
}
