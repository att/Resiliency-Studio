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

import org.testng.annotations.AfterSuite;

import com.att.tta.rs.cucumber.framework.ParentScenario;
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class StepDefinitionLoginPage extends ParentScenario {

	@Before()
	public void beforeScenario() {
		startBrowser();
	}

	@Given("^I am on the login page for authenticate$")
	public void I_am_on_the_login_page_for_authenticate() {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		
	}

	@When("^I enter the username and password on login page$")
	public void I_enter_the_username_and_password_on_login_page() throws InterruptedException {
		new LoginPage(driver).enter_valid_user();
	}

	@When("^I click the logon button on login page$")
	public void I_click_on_logon_button_on_login_page() throws Throwable {
		new LoginPage(driver).click_logon_button();
	}

	@Given("^I want to Log out from the RS application$")
	public void i_want_to_log_out_from_rs_app() throws Throwable {
		new LoginPage(driver).click_logout_button();
	}

	@And("^we are closing the browser after logging out$")
	public void we_are_closing_the_browser() throws Throwable {
		new LoginPage(driver).closingbrowser();
	}

	@Given("^I am enetering credentials for Log out feature$")
	public void I_am_entering_cred_for_adding_application() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}
	@AfterSuite
	void closingthebrowser()
	{
		System.out.println("i am in after suite");
		driver.quit();
	}

}
