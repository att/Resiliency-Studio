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

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class EditApplicationPrivateKeyPage extends ParentPage {

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

	@FindBy(xpath = "//a[@href='#/viewApplication']/div/div[1]/span[2]")
	private WebElement CLICK_VIEWAPPLICATION;

	@FindBy(id = "applicationName")
	private WebElement APPLICATION_NAME_TEXTBOX;

	@FindBy(id = "categorySelect")
	private WebElement CATEGORY_SELECTOR;

	@FindBy(id = "environmentSelect")
	private WebElement ENVIRONMENT_SELECTOR;

	@FindBy(id = "serverNameText0")
	private WebElement SERVER_NAME_TEXT;

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

	@FindBy(id = "serverTabstep1")
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

	@FindBy(id = "expandId")
	private WebElement CLICK_TO_EXPAND;

	@FindBy(id = "editButton")
	private WebElement EDIT_BUTTON;

	@FindBy(id = "editApplication")
	private WebElement EDIT_APPLICATION;

	@FindBy(id = "updateButton")
	private WebElement UPDATE_BUTTON;

	@FindBy(xpath = "//a[@ng-show='succesEditAlert']")
	private WebElement SUCCESS_UPDATE;

	@FindBy(id = "privateKey0")
	private WebElement PRIVATE_KEY_TEXTAREA;

	protected WebDriver driver;

	public EditApplicationPrivateKeyPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void click_view_application_for_edit() throws InterruptedException {

		JavascriptLibrary jsLib = new JavascriptLibrary();

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.CLICK_VIEWAPPLICATION));
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_VIEWAPPLICATION, "click", "0,0");
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		Assert.assertTrue("Unable to display the table", elementIsPresent(this.TABLE_VIEWAPPLICATION));
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

	public void get_row1_app_details() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		// Select first application to edit
		String expandValue = driver
				.findElement(By.xpath("//table[@id='fdApplicationTable']//span[@title='Click to expand']"))
				.getAttribute("class");

		if (expandValue.equalsIgnoreCase("fa fa-plus-circle")) {

			JavascriptLibrary jsLib = new JavascriptLibrary();
			jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_TO_EXPAND, "click", "0,0");
		}

		String clickValue = driver
				.findElement(By.xpath("//table[@id='fdApplicationTable']//span[@title='Click to expand']"))
				.getAttribute("class");
		Assert.assertTrue("Unable to expand the row", clickValue.contains("fa fa-plus-circle"));
		driver.manage().timeouts().implicitlyWait(PropertyUtil.MEDIUM_MILLISEC, TimeUnit.MILLISECONDS);
		// Select first application, first server to edit
		JavascriptLibrary jsLib = new JavascriptLibrary();
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.EDIT_BUTTON, "click", "0,0");
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);

	}

	public void fill_edit_app_details(List<AddApplicationUITab1Data> appDataList) throws Throwable {
		if (appDataList == null)
			System.out.println("\n\n\n No data in the table ");
		InputStream in = getClass().getClassLoader().getResourceAsStream("privatekeydata.txt");
		String s = "";
		int size = in.available();
		for (int i = 0; i < size; i++) {
			s = s + (char) in.read();
		}
		in.close();
		for (AddApplicationUITab1Data appData : appDataList) {
			this.SERVER_NAME_TEXT.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.serverName);
			this.HOST_NAME_TEXTBOX.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.hostName);
			this.IP_ADDRESS_TEXTBOX.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.ipAddress);

			this.USER_NAME_TEXTBOX.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.userName);
			this.PRIVATE_KEY_TEXTAREA.clear();
			this.PRIVATE_KEY_TEXTAREA.click();
			this.PRIVATE_KEY_TEXTAREA.sendKeys(s);
			
		}

	}

	public void fill_tab2_details(List<AddApplicationUITab1Data> appDataList) throws Throwable {

		

		for (AddApplicationUITab1Data appData : appDataList) {
			Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
			new Select(this.CPU_SELECTOR).selectByIndex(Integer.parseInt(appData.cpuIndex));
			new Select(this.OS_SELECTOR).selectByIndex(Integer.parseInt(appData.osIndex));

			this.STORAGE_SELECTOR.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.storageIndex);
			new Select(this.OS_TYPE_SELECTOR).selectByIndex(Integer.parseInt(appData.osTypeIndex));

			this.MEMORY_SELECTOR.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.memoryIndex);

		}

	}

	public void fill_tab3_details(List<AddApplicationUITab1Data> appDataList) {
		for (AddApplicationUITab1Data appData : appDataList) {
			this.SOFTWARE_COMPONENT_TEXT.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END),
					appData.softwareComponentName);
			this.PROCESS_NAME_TEXT.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.processName);
		}
	}

	public void fill_tab5_details(List<AddApplicationUITab1Data> appDataList) {
		WebElement addMonitorTab = driver.findElement(By.xpath("//a[text()='Monitor']"));
		for (AddApplicationUITab1Data appData : appDataList) {
			addMonitorTab.click();
			this.COUNTER_TYPE_TEXT.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.counterType);
			this.MONITOR_API_TEXT.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.monitorApi);
		}
	}

	public void fill_tab6_details(List<AddApplicationUITab1Data> appDataList) {
		WebElement addLogTab = driver.findElement(By.xpath("//a[text()='Logs']"));

		for (AddApplicationUITab1Data appData : appDataList) {
			addLogTab.click();
			this.LOG_TYPE_TEXT.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.logType);
			this.LOG_LOCATION.sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), appData.logLocation);
		}
	}

	public void goToNextTab() {
		JavascriptLibrary jsLib = new JavascriptLibrary();
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.NEXT__BUTTON.get(1), "click", "0,0");
	}

	public void clickUpdate() {

		JavascriptLibrary jsLib = new JavascriptLibrary();
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.UPDATE_BUTTON, "click", "0,0");
		Assert.assertTrue("Unable to Success", elementIsPresent(this.SUCCESS_UPDATE));
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);

	}

}
