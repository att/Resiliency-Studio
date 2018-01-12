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
import com.att.tta.rs.cucumber.framework.page.objects.AddApplicationUITab1Data;
import com.att.tta.rs.cucumber.framework.page.objects.EditApplicationPage;
import com.att.tta.rs.cucumber.framework.page.objects.EditApplicationPrivateKeyPage;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitionEditApplicationPrivateKey extends ParentScenario {

	@Before
	public void beforeScenario() {
		startBrowser();
	}

	@Given("^I am entering credentials for Edit Application private Key$")
	public void I_am_entering_credentials_for_edit_application_privateKey() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}

	@Given("^I am on the dash board page for edit application for private key$")
	public void display_view_application_page_for_edit() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new EditApplicationPrivateKeyPage(driver).click_view_application_for_edit();
	}

	@Then("^I enter the specific application name to be edited for private key \"([^\"]*)\"$")
	public void searching_for_a_specified_app(String s) throws InterruptedException {
		new EditApplicationPage(driver).selecting_the_particular_application_edit(s);
	}

	@When("^I click first row of the expand button on view application page for edit for private key$")
	public void i_click_first_row_of_the_expand_button_on_view_application_page_for_edit() throws Throwable {
		new EditApplicationPrivateKeyPage(driver).get_row1_app_details();
	}

	@When("^I click the edit button enter all edit values for private key$")
	public void i_click_the_edit_button_enter_all_edit_values(List<AddApplicationUITab1Data> appDataList)
			throws Throwable {
		new EditApplicationPrivateKeyPage(driver).fill_edit_app_details(appDataList);
	}

	@When("^I enter all the application details in tab two edit application page for private key$")
	public void i_enter_all_the_application_details_in_tab_two_edit_application_page(
			List<AddApplicationUITab1Data> appDataList) throws Throwable {
		new EditApplicationPrivateKeyPage(driver).fill_tab2_details(appDataList);

	}

	@When("^I enter all the application details in tab three edit application page for private key$")
	public void i_enter_all_the_application_details_in_tab_three_edit_application_page(
			List<AddApplicationUITab1Data> appDataList) throws Throwable {
		new EditApplicationPrivateKeyPage(driver).fill_tab3_details(appDataList);

	}

	@When("^I enter all the application details in tab five edit application page for private key$")
	public void i_enter_all_the_application_details_in_tab_five_edit_application_page(
			List<AddApplicationUITab1Data> appDataList) throws Throwable {
		new EditApplicationPrivateKeyPage(driver).fill_tab5_details(appDataList);

	}

	@When("^I enter all the application details in tab six edit application page for private key$")
	public void i_enter_all_the_application_details_in_tab_six_edit_application_page(
			List<AddApplicationUITab1Data> appDataList) throws Throwable {
		new EditApplicationPrivateKeyPage(driver).fill_tab6_details(appDataList);

	}

	@When("^I click the next button on edit application page for private key$")
	public void i_click_the_next_button_on_edit_application_page() throws Throwable {
		new EditApplicationPrivateKeyPage(driver).goToNextTab();

	}

	@And("^I click the update button on edit application page for private key$")
	public void i_click_the_update_button_on_edit_application_page() throws Throwable {
		new EditApplicationPrivateKeyPage(driver).clickUpdate();
	}

	@After
	public void afterScenario() {
		// closeBrowser();
	}

}
