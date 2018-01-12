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
import com.att.tta.rs.cucumber.framework.page.objects.AddApplicationOfRSALoginPage;
import com.att.tta.rs.cucumber.framework.page.objects.AddApplicationUITab1Data;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class StepDefinitionApplicationOfRSALogin extends ParentScenario {

	@Before
	public void beforeScenario() {
		startBrowser();
	}

	@Given("^I am enetering credentials for Add Application RSA login$")
	public void I_am_entering_credentials_for_add_application_RSA() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();
	}

	@Given("^I am on the dash board page for RSA login$")
	public void I_am_on_the_dash_board_page_for_add_application() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new AddApplicationOfRSALoginPage(driver).click_add_application_link();
	}

	@When("^I enter all the application details in tab one for RSA login$")
	public void i_enter_all_the_application_details_in_tab_one(List<AddApplicationUITab1Data> appDataList)
			throws Throwable {
		new AddApplicationOfRSALoginPage(driver).fill_tab1_details(appDataList);
	}

	@When("^I click the next button on add application page for RSA login$")
	public void i_click_the_next_button_on_add_application_page() throws Throwable {
		new AddApplicationOfRSALoginPage(driver).goToNextTab();
	}

	@When("^I enter all the application details in tab two for RSA login$")
	public void i_enter_all_the_application_details_in_tab_two(List<AddApplicationUITab1Data> appDataList)
			throws Throwable {
		new AddApplicationOfRSALoginPage(driver).fill_tab2_details(appDataList);
	}

	@When("^I enter all the application details in tab three for RSA login$")
	public void i_enter_all_the_application_details_in_tab_three(List<AddApplicationUITab1Data> appDataList)
			throws Throwable {
		new AddApplicationOfRSALoginPage(driver).fill_tab3_details(appDataList);
	}

	@When("^I enter all the application details in tab five for RSA login$")
	public void i_enter_all_the_application_details_in_tab_five(List<AddApplicationUITab1Data> appDataList)
			throws Throwable {
		new AddApplicationOfRSALoginPage(driver).fill_tab5_details(appDataList);
	}

	@When("^I enter all the application details in tab six for RSA login$")
	public void i_enter_all_the_application_details_in_tab_six(List<AddApplicationUITab1Data> appDataList)
			throws Throwable {
		new AddApplicationOfRSALoginPage(driver).fill_tab6_details(appDataList);
	}

	@When("^I click the submit button on add application page for RSA login$")
	public void i_click_the_submit_button_on_add_application_page() throws Throwable {
		new AddApplicationOfRSALoginPage(driver).clickSubmit();
	}

	@And("^I click view application link on add application page for RSA login$")
	public void I_click_view_application_link_on_add_application_page() throws InterruptedException {
		new AddApplicationOfRSALoginPage(driver).click_view_application_link();
	}

	@After
	public void afterScenario() {
		//closeBrowser();
	}
}
