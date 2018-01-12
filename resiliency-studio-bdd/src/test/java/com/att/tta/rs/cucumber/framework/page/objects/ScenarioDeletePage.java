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

import com.att.tta.rs.cucumber.framework.ParentPage;
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

/**
 * This class contains the page objects and functions to test the feature
 * @author sk494t
 *
 */

public class ScenarioDeletePage extends ParentPage {

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

	@FindBy(xpath = "//a[@href='#/viewScenario']//span[2]")
	private WebElement CLICK_VIEWSCENARIO;

	@FindBy(id = "applicationName")
	private WebElement APPLICATION_NAME_SELECTOR;

	@FindBy(id = "categorySelect")
	private WebElement ENVIRONMENT_SELECTOR;

	@FindBy(id = "submitButton")
	private WebElement BUTTON_SUBMIT;

	@FindBy(id = "viewText")
	private WebElement VIEW_TEXT;

	@FindBy(id = "deleteScenario")
	private WebElement BUTTON_SUBMIT_SCENARIO;

	@FindBy(id = "clickCheckbox")
	private WebElement CLICK_CHECKBOX;

	@FindBy(xpath = "//button[@ng-click='gotoBulkScenarioDelete()']")
	private WebElement CLICK_YES;

	@FindBy(id = "clickOk")
	private WebElement CLICK_OK;

	@FindBy(id = "myBulkDeleteScenario")
	private WebElement SUCCESS_DELETE;

	@FindBy(id = "viewscen")
	private WebElement TABLE_VIEW;

	@FindBy(xpath = "//input[@ng-click='selectAll()']")
	private WebElement ALL_SCENARIOS_SELECTION;

	protected WebDriver driver;

	public ScenarioDeletePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void click_scenario_delete() throws InterruptedException {

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.CLICK_VIEWSCENARIO));
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_VIEWSCENARIO, "click", "0,0");
		Assert.assertTrue("Unable to reach View Scenarios", elementIsPresent(this.VIEW_TEXT));

	}

	public void select_application_details(List<ScenarioDeleteUIData> deleteList) throws InterruptedException {

		for (ScenarioDeleteUIData deleteData : deleteList) {

			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//div[@id='applicationListDiv']")).click();
			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//input[@ng-model='searchFilter']")).clear();

			driver.findElement(By.xpath("//input[@ng-model='searchFilter']")).sendKeys(deleteData.applicationName);
			Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
			WebElement test = driver.findElement(By.xpath("//*[@id='applicationListDiv']/div/ul/div/li/span"));
			test.click();
			new Select(this.ENVIRONMENT_SELECTOR).selectByIndex(Integer.parseInt(deleteData.environmentSelector));

		}

	}

	public void click_on_view() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(PropertyUtil.MEDIUM_TIMEOUT, TimeUnit.SECONDS);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.BUTTON_SUBMIT, "click", "0,0");
		Assert.assertTrue("Scenario is not available for Selected Application", elementIsPresent(this.TABLE_VIEW));

	}

	public void click_checkbox() throws InterruptedException {

		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_CHECKBOX, "click", "0,0");
		String markedTest = this.CLICK_CHECKBOX.getAttribute("class");
		Assert.assertTrue("Unable to CheckBox", markedTest.contains("ng-not-empty"));
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);

	}

	public void click_on_delete() throws InterruptedException {
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.BUTTON_SUBMIT_SCENARIO, "click", "0,0");

	}

	public void click_to_confirm_yes() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_YES, "click", "0,0");
		driver.manage().timeouts().implicitlyWait(PropertyUtil.MEDIUM_TIMEOUT, TimeUnit.SECONDS);

		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
	}

	public void click_ok_button() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

	public void deleting_all_scenarios_at_a_time() throws InterruptedException {
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		if (this.ALL_SCENARIOS_SELECTION.isSelected()) {
			jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.ALL_SCENARIOS_SELECTION, "click", "0,0");
			System.out.println("in side delete all scenarios");
		}
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.ALL_SCENARIOS_SELECTION, "click", "0,0");
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

}
