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

package com.att.tta.rs.cucumber.framework.page.objects;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.AssertJUnit;

import com.att.tta.rs.cucumber.framework.ParentPage;
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

/**
 * This class contains the page objects and functions to test the feature
 * @author sk494t
 *
 */


public class AddScenarioManualPage extends ParentPage {
	
	protected static PropertyUtil configProp = new PropertyUtil();
	
	JavascriptLibrary jsLib = new JavascriptLibrary(); 

	@FindBy(id = "currentlogin")
	private WebElement userTeam;
	
	@FindBy(name = "userid")
	private WebElement USERID_TEXTBOX;
	
	@FindBy(name = "password")
	private WebElement PASSWORD_TEXTBOX;
	
	@FindBy(name = "btnSubmit")
	private WebElement LOGIN_BUTTON;
	
	@FindBy(name = "successOK")
	private WebElement SUCCESS_OK;
	
	@FindBy(xpath="//a[@href='#/createScenario']/div/div[1]/span")
	private WebElement CLICK_CREATESCEANRIO;
	
	@FindBy(id = "applicationName")
	private WebElement APPLICATION_NAME_SELECTOR;
	
	@FindBy(id = "environmentSelect")
	private WebElement ENVIRONMENT_SELECTOR;
	
	@FindBy(id = "serverName")
	private WebElement SERVER_SELECTOR;
	
	@FindBy(id = "role")
	private WebElement ROLE_SELECTOR;
	
	@FindBy(id = "component")
	private WebElement COMPONENT_SELECTOR;
	
	@FindBy(id = "failureTelnetSelect")
	private WebElement FAILURETENET_SELECTOR;
	
	@FindBy(id = "failureMode")
	private WebElement FAILURE_MODE_TEXTBOX;
	
	@FindBy(id = "monkeyTypeScn")
	private WebElement MONKEY_TYPE_SELECTOR;
	
	@FindBy(id = "monkeyStrategyScn")
	private WebElement MONKEY_STRATEGY_SELECTOR;
	
	@FindBy(id = "userBehavior")
	private WebElement USER_BEHAVIOR_TEXTBOX;
	
	@FindBy(id = "systemBehavior")
	private WebElement SYSTEM_BEHAVIOR_TEXTBOX;
	
	@FindBy(id = "causeOfFailure")
	private WebElement CAUSEOFFAILURE_TEXTBOX;
	
	@FindBy(id = "currentControls")
	private WebElement CURRENT_CONTROLS_TEXTBOX;
	
	@FindBy(id = "detectionMechanism")
	private WebElement DETECTION_MECHANISM_TEXTBOX;
	
	@FindBy(id = "recoveryMechanism")
	private WebElement RECOVERY_MECHANISM_TEXTBOX;
	
	@FindBy(id = "recommendations")
	private WebElement RECOMMENDATIONS_TEXTBOX;
	
	@FindBy(id = "mttd")
	private WebElement MTTD_TEXTBOX;
	
	@FindBy(id = "mttr")
	private WebElement MTTR_TEXTBOX;
	
	@FindBy(id = "failureScript")
	private WebElement FAILURE_SCRIPT_SELECTOR;
	
	@FindBy(id = "submitButton")
	private WebElement BUTTON_SUBMIT;
	
	@FindBy(xpath="//div[@ng-show='successAlert']")
	private WebElement SUCCESS_SCENARIO;
	
	@FindBy(id = "cell-content")
	private WebElement ELEMENTS_VIEW;
	
	@FindBy(id = "clickOk")
	private WebElement CLICK_OK;
	
	@FindBy(name="applicationName")
	private WebElement APP_NAME;
	
	@FindBy(xpath="//div[@ng-show='showerrorAlert']")
	private WebElement ERROR_VIEW_SCENARIO_BUTTON;
	
	@FindBy(xpath="//div[@ng-show='showerrorAlert']/strong")
	private WebElement ERROR_TEXT;
	
	@FindBy(id="monkeyStrategyScn0")
	private WebElement MONKEYSTRATEGY_NAME_DROPDOWN;
	
	@FindBy(id="monkeyDelay0")
	private WebElement MONKEY_STRTAGEY_SEQUENCE;
	
	@FindBy(id="monkeyTypeScn0")
	private WebElement MONKEY_TYPE;
	
	@FindBy(id="addMonk0")
	private WebElement ADD_FIRST_MONKEY_BUTTON;
	
	protected WebDriver driver;

	public AddScenarioManualPage(WebDriver driver) {
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
		
	}
	
     public void click_add_scenario_link() throws InterruptedException{
		
        
		
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("unable to display create scenario link", this.CLICK_CREATESCEANRIO.isDisplayed());
		jsLib.callEmbeddedSelenium(driver,"triggerMouseEventAt", this.CLICK_CREATESCEANRIO,"click", "0,0");
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		
		
	}
	
	public void fill_scenario_details(List<AddScenarioManualUIData> addScenarioList) throws InterruptedException{
		
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.SECONDS);
		   for(AddScenarioManualUIData addScenarioData : addScenarioList){
		    //Thread.sleep(PropertyUtil.LONG_MILLISEC);
			Thread.sleep(1000);
			   driver.findElement(By.xpath("//div[@id='applicationListDiv']")).click();			
			driver.findElement(By.xpath("//input[@ng-model='searchFilter']")).sendKeys(addScenarioData.applicationName);
			Thread.sleep(1000); 
			WebElement test= driver.findElement(By.xpath("//*[@id='applicationListDiv']/div/ul/div/li[1]/span"));
			 test.click();  
			 
			new Select(this.ENVIRONMENT_SELECTOR).selectByIndex(Integer.parseInt(addScenarioData.environmentIndex));
			new Select(this.SERVER_SELECTOR).selectByIndex(Integer.parseInt(addScenarioData.serverIndex));
			//new Select(this.ROLE_SELECTOR).selectByIndex(Integer.parseInt(addScenarioData.roleIndex));
			new Select(this.COMPONENT_SELECTOR).selectByIndex(Integer.parseInt(addScenarioData.softwareComponentIndex));
			new Select(this.FAILURETENET_SELECTOR).selectByIndex(Integer.parseInt(addScenarioData.failureTenetIndex));
			this.FAILURE_MODE_TEXTBOX.sendKeys(addScenarioData.failureMode);
			
			
			this.USER_BEHAVIOR_TEXTBOX.sendKeys(addScenarioData.userBehavior);
			this.SYSTEM_BEHAVIOR_TEXTBOX.sendKeys(addScenarioData.systemBehavior);
			this.CAUSEOFFAILURE_TEXTBOX.sendKeys(addScenarioData.potentialCauseFailure);
			this.CURRENT_CONTROLS_TEXTBOX.sendKeys(addScenarioData.currentControls);
			this.DETECTION_MECHANISM_TEXTBOX.sendKeys(addScenarioData.detectionMehanism);
			this.RECOVERY_MECHANISM_TEXTBOX.sendKeys(addScenarioData.recoveryMehanism);
			this.RECOMMENDATIONS_TEXTBOX.sendKeys(addScenarioData.recommendations);
			this.MTTD_TEXTBOX.sendKeys(addScenarioData.mttd);
			this.MTTR_TEXTBOX.sendKeys(addScenarioData.mttr);
			
		}
		
	}
	
	
	
	public void submit_scenario() throws InterruptedException{
		System.out.println("entered submission");
		 driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
		jsLib.callEmbeddedSelenium(driver,"triggerMouseEventAt", this.BUTTON_SUBMIT,"click", "0,0");
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Unable to create Scenario", elementIsPresent(this.SUCCESS_SCENARIO));
		
	}
	
	public void checking_the_values_retained_properly_after_submission(List<AddScenarioManualUIData> addScenarioList) throws InterruptedException{
		for(AddScenarioManualUIData data:addScenarioList){
		AssertJUnit.assertTrue("application name not matched", this.APP_NAME.getAttribute("title").equalsIgnoreCase(data.applicationName));
		AssertJUnit.assertTrue("environment not matched", new Select(this.ENVIRONMENT_SELECTOR).getFirstSelectedOption().getText().equalsIgnoreCase(data.environmentIndex));
		
		AssertJUnit.assertTrue("servername not matched", new Select(this.SERVER_SELECTOR).getFirstSelectedOption().getText().equalsIgnoreCase(data.serverIndex));
		AssertJUnit.assertTrue("software component not matched", new Select(this.COMPONENT_SELECTOR).getFirstSelectedOption().getText().equalsIgnoreCase(data.softwareComponentIndex));
		
		}
		
	
	}
	
	public void validate_the_error_text()throws InterruptedException{
		
	if(this.ERROR_VIEW_SCENARIO_BUTTON.isDisplayed()){
		System.out.println("trying to enter duplicate Scenarios like expected");
	AssertJUnit.assertTrue("expected error in here", this.ERROR_VIEW_SCENARIO_BUTTON.isDisplayed());
	}
	
	}
	
	
	public void click_ok_button() throws InterruptedException{
		
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		jsLib.callEmbeddedSelenium(driver,"triggerMouseEventAt", this.CLICK_OK,"click", "0,0");
		
	}

	public void fill_scenario_details_second_time(List<AddScenarioManualUIData> addScenarioList)throws InterruptedException{
		
		 for(AddScenarioManualUIData addScenarioData : addScenarioList){
			 Thread.sleep(PropertyUtil.LOW_MILLISEC);
			
				this.FAILURE_MODE_TEXTBOX.sendKeys(addScenarioData.failureMode);
				new Select(this.MONKEY_TYPE).selectByVisibleText(addScenarioData.monkeyType);
				new Select(this.MONKEYSTRATEGY_NAME_DROPDOWN).selectByVisibleText(addScenarioData.monkeyStrategy);
				new Select(this.MONKEY_STRTAGEY_SEQUENCE).selectByVisibleText(addScenarioData.sequence);
				
		 }
		
		
	}
	
	
	
	public void validating_monkey_dropdowns_are_visible() throws InterruptedException{
		
		System.out.println("entered 1");
		AssertJUnit.assertTrue("monkey type drop down is not displayed", this.MONKEY_TYPE.isDisplayed());
		AssertJUnit.assertTrue("monkey strategy name drop down is not displayed", this.MONKEYSTRATEGY_NAME_DROPDOWN.isDisplayed());
		AssertJUnit.assertTrue("monkey sequence drop down is not displayed", this.MONKEY_STRTAGEY_SEQUENCE.isDisplayed());
		AssertJUnit.assertTrue("monkey sequence drop down is not displayed", this.ADD_FIRST_MONKEY_BUTTON.isDisplayed());
		
	}
	
	public void adding_multiple_monkeys_successfully(List<CloneMonkeyStrategyBasicTabUIData> monkeydata)throws InterruptedException{
	
		System.out.println("entered 2");int i=0;
		for(CloneMonkeyStrategyBasicTabUIData monkeydetails:monkeydata){
			System.out.println(monkeydata.size()+"i value"+i);
			
		new Select(driver.findElement(By.id("monkeyTypeScn"+i))).selectByVisibleText(monkeydetails.monkeyType);
		new Select(driver.findElement(By.id("monkeyStrategyScn"+i))).selectByVisibleText(monkeydetails.monkeyStrategyName);
		new Select(driver.findElement(By.id("monkeyDelay"+i))).selectByVisibleText(monkeydetails.sequence);
		Thread.sleep(3000);
		if(i<monkeydata.size()-1){
			driver.findElement(By.id("addMonk"+i)).click();
			i++;
			
		}
		}
		
	}
	
	
}
