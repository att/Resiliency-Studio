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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;

import com.att.tta.rs.cucumber.framework.ParentPage;
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

/**
 * This class contains the page objects and functions to test the feature
 * 
 * @author sk494t
 *
 */
public class ExecuteScenarioPage extends ParentPage {

	protected static PropertyUtil configProp = new PropertyUtil();

	JavascriptLibrary jsLib = new JavascriptLibrary();

	@FindBy(name = "userid")
	private WebElement USERID_TEXTBOX;

	@FindBy(name = "password")
	private WebElement PASSWORD_TEXTBOX;

	@FindBy(name = "btnSubmit")
	private WebElement LOGIN_BUTTON;

	@FindBy(name = "successOK")
	private WebElement SUCCESS_OK;

	@FindBy(xpath = "//a[@href='#/executeScenario']/div/div/span")
	private WebElement CLICK_EXECUTESCENARIO;

	@FindBy(id = "applicationName")
	private WebElement APPLICATION_NAME_SELECTOR;

	@FindBy(id = "categorySelect")
	private WebElement ENVIRONMENT_SELECTOR;

	@FindBy(id = "submitButton")
	private WebElement BUTTON_SUBMIT;

	@FindBy(id = "runScenario")
	private WebElement BUTTON_RUN_SCENARIO;

	@FindBy(id = "markScenario")
	private WebElement MARK_SCEANRIO;

	@FindBy(id = "clickOk")
	private WebElement CLICK_OK;

	@FindBy(id = "scenarioList")
	private WebElement TABLE_VIEW;

	@FindBy(id = "eventId")
	private WebElement WIDGET_EVENT;

	@FindBy(id = "cell-content")
	private WebElement ELEMENTS_VIEW;

	@FindBy(xpath = "//div[@ng-bind-html='msg']")
	private WebElement ERROR_WINDOW_MESSAGE;

	@FindBy(id = "closeButton")
	private WebElement CLOSE_ERROR_WINDOW;

	@FindBy(xpath = "//*[@id='userList']/table/tbody/tr[1]/td[4]")
	private WebElement SERVER_NAME;

	@FindBy(xpath = "//*[@id='userList']/table/tbody/tr[1]/td[7]")
	private WebElement FAILURE_TENET;

	@FindBy(xpath = "//*[@id='userList']/table/tbody/tr[2]/td[4]")
	private WebElement FAILURE_MODE;

	@FindBy(xpath = "//*[@id='userList']/table/tbody/tr[2]/td[4]/ul/li")
	private List<WebElement> MONKEYSTARTEGY_NAME;

	@FindBy(xpath = "//*[@id='userList']/table/tbody/tr[1]/td[1]/button")
	private WebElement EXPAND_BUTTON;

	@FindBy(xpath = "	//*[@id='eventSpan']")
	private WebElement EXECUTION_WIDGET_DATA;

	@FindBy(xpath = "//*[@id='eventId']/div[2]/div[2]")
	private WebElement MONITORING_WIDGET;

	@FindBy(xpath = "//*[@id='eventId']/div[2]/div[3]")
	private WebElement LOGGING_WIDGET;

	@FindBy(xpath = "//*[@id='userList']/table/thead/tr[2]/th[1]/a/span")
	private WebElement FILTER_CLICK;

	@FindBy(xpath = "//*[@id='userList']/table/thead/tr[1]/td[8]/input")
	private WebElement MONKEYSTRATEGY_SEARCH;

	protected WebDriver driver;

	private static String monkeyArr[];

	public ExecuteScenarioPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);

	}

	public void click_execute_single_scenario_link() throws InterruptedException {

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.CLICK_EXECUTESCENARIO));
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_EXECUTESCENARIO, "click", "0,0");

	}

	public void select_application_details(List<ExecuteScenarioUIData> executeList) throws InterruptedException {
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		Thread.sleep(5000);
		for (ExecuteScenarioUIData executeData : executeList) {
			Thread.sleep(3000);
			jsLib.callEmbeddedSelenium(driver,"triggerMouseEventAt", driver.findElement(By.xpath("//div[@ng-click='toggleDropdown()']")),"click", "0,0");
    		
			Thread.sleep(5000);
			driver.findElement(By.xpath("//input[@ng-model='searchFilter']")).clear();

			Thread.sleep(1000);
			driver.findElement(By.xpath("//input[@ng-model='searchFilter']")).sendKeys(executeData.applicationName);
			Thread.sleep(1000);
			WebElement test = driver.findElement(By.xpath("//*[@id='applicationListDiv']/div/ul/div/li/span"));
			test.click();
			new Select(this.ENVIRONMENT_SELECTOR).selectByIndex(Integer.parseInt(executeData.environmentIndex));

		}

	}

	public void click_list_button() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.BUTTON_SUBMIT, "click", "0,0");
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Scenario is not available for Selected Application", elementIsPresent(this.TABLE_VIEW));

	}

	public void mark_scenario() throws InterruptedException {

		
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.MARK_SCEANRIO, "click", "0,0");

	}

	public void click_run_now() throws InterruptedException {

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.BUTTON_RUN_SCENARIO, "click", "0,0");
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

	public void error_window_validation() throws InterruptedException {
		Thread.sleep(2000);
		AssertJUnit.assertTrue("error message is not as expected",
				this.ERROR_WINDOW_MESSAGE.getText().contains("atleast one"));
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLOSE_ERROR_WINDOW, "click", "0,0");
		Thread.sleep(2000);
	}

	public void validating_mandatory_fields_of_scenario(List<ValidateViewUIData1> validateviewdata)
			throws InterruptedException {

		this.EXPAND_BUTTON.click();
		Thread.sleep(2000);
		
		for(ValidateViewUIData1 viewdata:validateviewdata ){
    		AssertJUnit.assertTrue("not matched with the expected value",this.SERVER_NAME.getText().equals(viewdata.serverName) );
    		AssertJUnit.assertTrue("not matched with the expected value",this.FAILURE_TENET.getText().equals(viewdata.failureTenet));
    		monkeyArr=viewdata.monkeyStrategy.split(",");
    		for(int i=0;i<this.MONKEYSTARTEGY_NAME.size()&&i<monkeyArr.length;i++){
    		System.out.println("mine"+this.MONKEYSTARTEGY_NAME.get(i).getText());
    		AssertJUnit.assertTrue("not matched with the expected value",this.MONKEYSTARTEGY_NAME.get(i).getText().equals(monkeyArr[i]));
    		
    		}
		}
	}

	public void execution_event_widget_appeared() throws InterruptedException {
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Monitering Widget is not available for Selected Application",
				elementIsPresent(this.WIDGET_EVENT));

	}

	public void checking_data_inside_execution_event_widget() throws InterruptedException {
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		Thread.sleep(PropertyUtil.LOW_MILLISEC);
//this code will check the existence of monkey name in the widget
		for (int i = 0; i < monkeyArr.length; i++) {
			AssertJUnit.assertTrue(
					"data is not as expected inside widget refer error text for private key"
							+ this.EXECUTION_WIDGET_DATA.getText(),
					this.EXECUTION_WIDGET_DATA.getText().contains(monkeyArr[i]));
		}

	}

	public void checking_monitoring_widget_is_appeared() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOf(this.MONITORING_WIDGET));
		AssertJUnit.assertTrue("monitornig widget is not displayed", this.MONITORING_WIDGET.isDisplayed());
	}

	public void checking_logging_widget_is_appeared() {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOf(this.LOGGING_WIDGET));
		AssertJUnit.assertTrue("logging Widget is not displayed as expected", this.LOGGING_WIDGET.isDisplayed());
	}
}
