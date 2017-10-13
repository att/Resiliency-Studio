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
import com.att.tta.rs.cucumber.framework.page.objects.CloneMonkeyStrategyBasicTabUIData;
import com.att.tta.rs.cucumber.framework.page.objects.DeleteMonkeyStrategyPage;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

public class StepDefinitionDeleteMonkeyStrategy extends ParentScenario {

	@Before
	public void beforeScenario() {
		startBrowser();
	}

	@Given("^I am entering credentials for deleting monkey strategies$")
	public void I_am_enetering_credentials_for_deleting_monkeystartegy() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}

	@Given("^I am on the dashboard page for Delete MonkeyStrategy$")
	public void I_am_on_the_dashboard_page_for_Delete_MonkeyStrategy() throws InterruptedException {

		new DeleteMonkeyStrategyPage(driver).click_monkey_strategy_link();
	}

	@When("^I click the delete icon on MonkeyStrategy page$")
	public void I_click_the_delete_icon_on_MonkeyStrategy_page() throws InterruptedException {
		// new DeleteMonkeyStrategyPage(driver).click_delete_icon();
	}

	@When("^I enter the monkeytype and monkeystrategy to delete$")
	public void entering_monkey_type_monkeystrategy_for_deleting(List<CloneMonkeyStrategyBasicTabUIData> basictabuidata)
			throws InterruptedException {
		new DeleteMonkeyStrategyPage(driver).choosing_the_monkeytype_and_monkeystrategyname(basictabuidata);
	}

	@When("^I click the yes to confirm delete on MonkeyStrategy$")
	public void I_click_the_yes_to_confirm_delete_on_MonkeyStrategy() {
		// implemented this code in the previous step itself

		// new DeleteMonkeyStrategyPage(driver).click_yes_to_confirm();
	}

}
