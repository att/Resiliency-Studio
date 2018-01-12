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
import com.att.tta.rs.cucumber.framework.page.objects.EditMonkeyStrategyPage;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitionEditMonkeyStrategy extends ParentScenario {

	@Before
	public void beforeScenario() {
		startBrowser();
	}

	@Given("^I am entering the credentials for Edit Monkey Strategy$")
	public void log_dashboard_for_Edit_monkey() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}

	@Given("^I am on the dashboard page for Edit MonkeyStrategy$")
	public void clicking_the_monkeystrategies_to_edit() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new EditMonkeyStrategyPage(driver).clicking_on_view_monkeys_to_edit();
	}

	@When("^I click the edit icon on MonkeyStrategy page of monkey \"([^\"]*)\" by verifying the redirection to the edit monkey strategy page$")
	public void I_click_the_edit_icon_on_MonkeyStrategy_page(String s) throws InterruptedException {
		new EditMonkeyStrategyPage(driver).click_edit_icon_for_Editing_monkeystrategy(s);
	}

	@Then("^I should check weather the monkey strategy and monkey type and version got disabled$")
	public void checking_the_fields_got_disabled_after_pageload() throws InterruptedException {
		new EditMonkeyStrategyPage(driver).check_fields_got_disabled();
	}

	@Then("^I should check weather the script type and the file is uploaded$")
	public void checking_the_fields_are_filled_basic_tab_on_page_load_for_edit_monkey() throws InterruptedException {
		new EditMonkeyStrategyPage(driver).check_fields_in_basic_tab_are_filled();
	}

	@Then("^I should click on the Advance tab and verify the generic toggle is on if it is on make sure mandatory fields got filled$")
	public void checking_the_fields_in_advance_tab_on_page_load_for_edit_monkey() throws InterruptedException {
		new EditMonkeyStrategyPage(driver).check_fields_in_advance_tab_accordingly();
	}

	@When("^I click the update on edit MonkeyStrategy page$")
	public void clicking_on_update_for_edit_monkeyStrategy() throws InterruptedException {
		new EditMonkeyStrategyPage(driver).clicking_update_to_edit_monkey();
	}

}
