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

import java.util.Iterator;
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
 * 
 * @author sk494t
 *
 */
public class ViewScenarioPage extends ParentPage {

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
	private WebElement CLICK_VIEWSCEANRIO;

	@FindBy(id = "viewscen")
	private WebElement TABLE_VIEW;

	@FindBy(id = "cell-content")
	private WebElement ELEMENTS_VIEW;

	@FindBy(id = "applicationName")
	private WebElement APPLICATION_NAME_SELECTOR;

	@FindBy(id = "categorySelect")
	private WebElement ENVIRONMENT_SELECTOR;

	protected WebDriver driver;

	public ViewScenarioPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void click_link_on_dashboard() throws InterruptedException {

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.CLICK_VIEWSCEANRIO));
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_VIEWSCEANRIO, "click", "0,0");

	}

	public void select_application(List<ViewScenarioUIData> viewList) throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(PropertyUtil.MEDIUM_TIMEOUT, TimeUnit.SECONDS);
		for (ViewScenarioUIData viewData : viewList) {
			Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
			driver.findElement(By.xpath("//div[@id='applicationListDiv']")).click();
			driver.findElement(By.xpath("//input[@ng-model='searchFilter']")).sendKeys(viewData.applicationName);
			Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
			WebElement test = driver.findElement(By.xpath("//*[@id='applicationListDiv']/div/ul/div/li/span"));
			test.click();
			new Select(this.ENVIRONMENT_SELECTOR).selectByIndex(Integer.parseInt(viewData.environmentSelector));

		}
	}

	public void view_scenario() {
		driver.findElement(By.xpath("//button[@id='submitButton']")).click();
	}

	public void checking_view_scenario_table_is_present() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		Thread.sleep(PropertyUtil.LONG_MILLISEC);

		AssertJUnit.assertTrue("unable to find table", elementIsPresent(this.TABLE_VIEW));

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);

	}

	public void checking_Scenario_table_values_over_given_values(List<ValidateViewUIData1> validateviewdata) {
		List<WebElement> table = driver.findElements(By.xpath("//*[@id='viewscen']/tbody/tr"));
		Iterator<ValidateViewUIData1> iterator = validateviewdata.iterator();

		for (int i = 1, j = 1; i <= table.size() / 2; i++) {

			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.MILLISECONDS);
			WebElement ftenet = driver.findElement(By.id("failuretenet" + i));
			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.MILLISECONDS);
			WebElement fmode = driver.findElement(By.id("failuremode" + i));
			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.MILLISECONDS);
			driver.findElement(By.id("expandbutton" + i)).click();
			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.MILLISECONDS);
			WebElement monkey = driver.findElement(By.id("monkeystrategy" + i));
			String[] monkeysplitarray = monkey.getText().split("\\s+");
			String s = monkeysplitarray[2] + "," + monkeysplitarray[3];

			while (iterator.hasNext() && j == i) {
				ValidateViewUIData1 viewdata1 = iterator.next();
				AssertJUnit.assertEquals(viewdata1.failureTenet, ftenet.getText());
				AssertJUnit.assertEquals(viewdata1.failureMode, fmode.getText());
				AssertJUnit.assertEquals(viewdata1.monkeyStrategy, s);
				j++;
			}

		}

	}
}
