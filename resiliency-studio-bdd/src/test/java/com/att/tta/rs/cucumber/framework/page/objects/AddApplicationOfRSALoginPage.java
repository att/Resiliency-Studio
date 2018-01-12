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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.att.tta.rs.cucumber.framework.ParentPage;
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

/**
 * This class contains the page objects and functions to test the feature
 * 
 * @author sk494t
 *
 */
public class AddApplicationOfRSALoginPage extends ParentPage {

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

	@FindBy(xpath = "//a[@href='#/onboardApplication']/div/div[1]/span")
	private WebElement CLICK_ONBOARDAPPLICATION;

	@FindBy(id = "applicationNameTextbox")
	private WebElement APPLICATION_NAME_TEXTBOX;

	@FindBy(id = "applicationNameTextbox")
	private WebElement APPLICATION_NAME_SELECTOR;

	@FindBy(id = "categoryMOTSOFF")
	private WebElement CATEGORY_SELECTOR;

	@FindBy(id = "environmentMOTSOFF")
	private WebElement ENVIRONMENT_SELECTOR;

	@FindBy(id = "serverNameText0")
	private WebElement SERVER_NAME_TEXT;

	@FindBy(id = "roleSelect0")
	private WebElement ROLE_TEXT;

	@FindBy(id = "cpuSelect0")
	private WebElement CPU_SELECTOR;

	@FindBy(id = "memorySelect0")
	private WebElement MEMORY_SELECTOR;

	@FindBy(id = "hostNameText0")
	private WebElement HOST_NAME_TEXTBOX;

	@FindBy(id = "osSelect0")
	private WebElement OS_SELECTOR;

	@FindBy(id = "storageSelect0")
	private WebElement STORAGE_SELECTOR;

	@FindBy(id = "osTypeSelect0")
	private WebElement OS_TYPE_SELECTOR;

	@FindBy(id = "tierSelect0")
	private WebElement TIER_SELECTOR;

	@FindBy(id = "addServerButton")
	private WebElement ADD_SERVER_BUTTON;

	@FindBy(xpath="//button[text()='Next']")
	private List<WebElement> NEXT__BUTTON;

	@FindBy(id = "submitButton")
	private WebElement SUBMIT__BUTTON;

	@FindBy(id = "serverTabstep10")
	private WebElement SERVER_TAB;

	@FindBy(id = "ipAddressText0")
	private WebElement IP_ADDRESS_TEXTBOX;

	@FindBy(id = "userNameText0")
	private WebElement USER_NAME_TEXTBOX;

	@FindBy(id = "password0")
	private WebElement PASSWORD_BOX;

	@FindBy(id = "softwareComponentText0")
	private WebElement SOFTWARE_COMPONENT_TEXT;

	@FindBy(id = "processNameText0")
	private WebElement PROCESS_NAME_TEXT;

	@FindBy(id = "discoveryNameText0")
	private WebElement DISCOVERY_NAME_TEXT;

	@FindBy(id = "discoveryApi0")
	private WebElement DISCOVERY_API_TEXT;

	@FindBy(id = "counterType0")
	private WebElement COUNTER_TYPE_TEXT;

	@FindBy(id = "monitorApi0")
	private WebElement MONITOR_API_TEXT;

	@FindBy(id = "logType0")
	private WebElement LOG_TYPE_TEXT;

	@FindBy(id = "logLocation0")
	private WebElement LOG_LOCATION;

	@FindBy(id = "fdApplicationTable")
	private WebElement TABLE_VIEWAPPLICATION;

	@FindBy(xpath = "//a[@ng-show='showSuccessAlert']")
	private WebElement SUCCESS_ONBOARD;

	@FindBy(xpath = "//input[@id='logLocation0']")
	private WebElement SUBMIT_BUTTON;

	@FindBy(id = "autoDiscovery")
	private WebElement MOTS_TOGGLE_SWITCH;

	@FindBy(id = "rsaLogin0")
	private WebElement CLICK_RSA_LOGIN;

	protected WebDriver driver;
	private static String APPLICATION_NAME;

	public AddApplicationOfRSALoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void click_add_application_link() throws InterruptedException {
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.CLICK_ONBOARDAPPLICATION));
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_ONBOARDAPPLICATION, "click", "0,0");
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.APPLICATION_NAME_TEXTBOX));
	}

	public void fill_tab1_details(List<AddApplicationUITab1Data> appDataList) throws InterruptedException {
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);

		for (AddApplicationUITab1Data appData : appDataList) {
			driver.manage().timeouts().implicitlyWait(PropertyUtil.MEDIUM_TIMEOUT, TimeUnit.SECONDS);
			APPLICATION_NAME = appData.applicationName;
			this.APPLICATION_NAME_TEXTBOX.sendKeys(appData.applicationName);
			Thread.sleep(PropertyUtil.LOW_MILLISEC);
			new Select(this.ENVIRONMENT_SELECTOR).selectByIndex(Integer.parseInt(appData.environmentIndex));
			new Select(this.CATEGORY_SELECTOR).selectByIndex(Integer.parseInt(appData.categoryIndex));
			driver.manage().timeouts().implicitlyWait(PropertyUtil.MEDIUM_TIMEOUT, TimeUnit.SECONDS);

			this.SERVER_NAME_TEXT.sendKeys(appData.serverName);
			this.ROLE_TEXT.sendKeys(appData.role);
			this.IP_ADDRESS_TEXTBOX.sendKeys(appData.ipAddress);

			this.HOST_NAME_TEXTBOX.sendKeys(appData.hostName);
			this.USER_NAME_TEXTBOX.sendKeys(appData.userName);
			Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
			jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_RSA_LOGIN, "click", "0,0");
		
		}
	}

	public void goToNextTab() {
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.NEXT__BUTTON.get(1), "click", "0,0");
	}

	public void clickSubmit() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//input[@id='logLocation0']")).submit();
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
		Assert.assertTrue("Unable to success Onboard Application", elementIsPresent(this.SUCCESS_ONBOARD));
	}

	public void fill_tab2_details(List<AddApplicationUITab1Data> appDataList) throws InterruptedException {
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		for (AddApplicationUITab1Data appData : appDataList) {
			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);

			new Select(this.CPU_SELECTOR).selectByIndex(Integer.parseInt(appData.cpuIndex));
			new Select(this.OS_SELECTOR).selectByIndex(Integer.parseInt(appData.osIndex));
			this.STORAGE_SELECTOR.sendKeys(appData.storageIndex);
			new Select(this.OS_TYPE_SELECTOR).selectByIndex(Integer.parseInt(appData.osTypeIndex));
			this.MEMORY_SELECTOR.sendKeys(appData.memoryIndex);
			
			}
	}

	public void fill_tab3_details(List<AddApplicationUITab1Data> appDataList) {

		for (AddApplicationUITab1Data appData : appDataList) {
			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
			this.SOFTWARE_COMPONENT_TEXT.sendKeys(appData.softwareComponentName);
			this.PROCESS_NAME_TEXT.sendKeys(appData.processName);
		}
	}

	public void fill_tab5_details(List<AddApplicationUITab1Data> appDataList) {
		for (AddApplicationUITab1Data appData : appDataList) {
			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
			this.COUNTER_TYPE_TEXT.sendKeys(appData.counterType);
			this.MONITOR_API_TEXT.sendKeys(appData.monitorApi);
		}
	}

	public void fill_tab6_details(List<AddApplicationUITab1Data> appDataList) {
		for (AddApplicationUITab1Data appData : appDataList) {
			driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
			this.LOG_TYPE_TEXT.sendKeys(appData.logType);
			this.LOG_LOCATION.sendKeys(appData.logLocation);
		}
	}

	public void click_view_application_link() throws InterruptedException {
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.SUCCESS_ONBOARD, "click", "0,0");
		Assert.assertTrue("Unable to display the table", elementIsPresent(this.TABLE_VIEWAPPLICATION));
		driver.manage().timeouts().implicitlyWait(PropertyUtil.MEDIUM_TIMEOUT, TimeUnit.SECONDS);
		WebElement search = driver.findElement(By.id("fdApplicationTable_filter"));
		Actions builder = new Actions(driver);
		Actions seriesOfActions = builder.moveToElement(search).click().sendKeys(search, APPLICATION_NAME);
		seriesOfActions.perform();
	}
}