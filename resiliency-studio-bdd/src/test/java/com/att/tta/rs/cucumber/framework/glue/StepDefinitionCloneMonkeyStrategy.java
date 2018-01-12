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
import com.att.tta.rs.cucumber.framework.page.objects.CloneMonkeyStrategy;
import com.att.tta.rs.cucumber.framework.page.objects.CloneMonkeyStrategyAdvanceTabUIData;
import com.att.tta.rs.cucumber.framework.page.objects.CloneMonkeyStrategyBasicTabUIData;
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class StepDefinitionCloneMonkeyStrategy extends ParentScenario {

	@Before
	public void beforeScenarioCases() throws InterruptedException {
		startBrowser();
	}

	@Given("^I am entering the credentials for clone monkey strategy$")
	public void log_dashboard() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new LoginPage(driver).enter_valid_user();
		new LoginPage(driver).click_logon_button();

	}

	@Given("^I am on the dash board page for clone monkey strategy$")
	public void monkey_strategy_on_dashboard() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new CloneMonkeyStrategy(driver).click_on_viewing_monkeystrategies();
	}

	@Given("^I am on view monkey strategies and clicking on the clone link for particular monkey \"([^\"]*)\"$")
	public void clicking_on_view_clone_monkeyStartegy(String monkeyname) throws InterruptedException {
		new CloneMonkeyStrategy(driver).click_on_clone_link(monkeyname);
	}
	
	@Then("^validate the monkey strategy name is ending with clone string and view the script in the basic tab$")
	public void checking_on_strategy_name_for_the_clone_monkeyStrategy() throws InterruptedException {
		new CloneMonkeyStrategy(driver).validate_the_clone_strategy_name();
	}

	@Then("^validate the mandatory fields are filled with data  while loading the page$")
	public void validating_the_mandatory_fields_filled(List<CloneMonkeyStrategyBasicTabUIData> basictabdata)
			throws InterruptedException {
		new CloneMonkeyStrategy(driver).mandatory_fileds_filled(basictabdata);
	}

	@Then("^code the script and update it and upload the new script$")
	public void code_and_upload_script() throws InterruptedException {
		new CloneMonkeyStrategy(driver).upload_the_new_script();
	}

	@Then("^click on the advance tab$")
	public void click_on_the_advance_tab() throws InterruptedException {
		new CloneMonkeyStrategy(driver).click_on_advance_tab();
	}

	@Then("^check weather generic toggle is on then check all the mandatory fields are filled$")
	public void check_generic_toggle_is_on_for_mandatory_fields(
			List<CloneMonkeyStrategyAdvanceTabUIData> advanceclonedata) throws InterruptedException {
		new CloneMonkeyStrategy(driver).checking_generic_toggle_is_on(advanceclonedata);
	}

	@Then("^click on the clone button and validate the monkey strategy is cloned$")
	public void click_on_clone_button() throws InterruptedException {
		new CloneMonkeyStrategy(driver).clicking_on_clone_button();
	}

	@Then("^check for the generic toggle is off ideally it should off after submitting the clone$")
	public void after_cloning_check() throws InterruptedException {
		new CloneMonkeyStrategy(driver).after_submit_check_the_mandatory_fields();
	}

	@Then("^reset all the fields for a clone monkey strategy where generic toggle is on$")
	public void resetting_all_the_fields_validating_generic_toggle() throws InterruptedException {
		new CloneMonkeyStrategy(driver).validate_reset_over_generic_toggle();
	}

	@Then("^submit the monkey strategy only by filling the basic tab$")
	public void cloning_the_monkeystrategy_by_filling_basictab() throws InterruptedException {
		new CloneMonkeyStrategy(driver).submitting_clone_by_filling_basictab_only();
	}
}
