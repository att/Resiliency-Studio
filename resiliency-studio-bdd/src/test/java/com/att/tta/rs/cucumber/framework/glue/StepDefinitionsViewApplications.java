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
import com.att.tta.rs.cucumber.framework.page.objects.LoginPage;
import com.att.tta.rs.cucumber.framework.page.objects.ViewApplicationPage;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class StepDefinitionsViewApplications extends ParentScenario{

	
	@Before
    public void beforeScenario() {
        startBrowser();
    }
 @Given("^I am Entering credentials for View applications$")
	public void I_am_enetering_credentials_for_view_application() throws InterruptedException{
		navigateTo("/resiliency-studio-ui/#/dashboard");
		 new LoginPage(driver).enter_valid_user();
		 new LoginPage(driver).click_logon_button();
		 
	}
	
	@Given("^I am on the dash board page for view application page$")
    public void display_view_application_page() throws InterruptedException {
		navigateTo("/resiliency-studio-ui/#/dashboard");Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		navigateTo("/resiliency-studio-ui/#/dashboard");
		new ViewApplicationPage(driver).click_on_view_application_link();
    }
	@Then("^I search for an application \"([^\"]*)\" that is present in the list of aplications$")
	public void i_search_for_an_application_that_is_present_in_the_list_of_aplications(String s) throws InterruptedException {
		new ViewApplicationPage(driver).search_for_an_application(s);
	     
	}

	@Then("^I click on the expand button to evaluate the mandatory fileds of application are filled with expected values$")
	public void i_click_on_the_expand_button_to_evaluate_the_mandatory_fileds_of_application_are_filled_with_expected_values(List <AddApplicationUITab1Data> appDataList) throws InterruptedException {
		new ViewApplicationPage(driver).application_details_validation(appDataList);
	     
	}

	@Then("^I click on the server section expand button to evaluate the mandatory fields are filled with expected values$")
	public void i_click_on_the_server_section_expand_button_to_evaluate_the_mandatory_fields_are_filled_with_expected_values(List <AddApplicationUITab1Data> appDataList) throws InterruptedException {
		new ViewApplicationPage(driver).applications_server_section_details(appDataList);
	     
	}

	@Then("^I search for an application which is not there in the applications list$")
	public void i_search_for_an_application_which_is_not_there_in_the_applications_list() throws InterruptedException {
		new ViewApplicationPage(driver).entering_unavailable_application_name();
	     
	}
	
    @After
    public void afterScenario() {
        //closeBrowser();
    }
	
}
