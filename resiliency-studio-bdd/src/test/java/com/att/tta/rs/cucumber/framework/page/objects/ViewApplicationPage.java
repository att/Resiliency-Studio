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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.AssertJUnit;

import com.att.tta.rs.cucumber.framework.ParentPage;
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

/**
 * This class contains the page objects and functions to test the feature
 * @author sk494t
 *
 */

public class ViewApplicationPage extends ParentPage {

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

	@FindBy(id = "cell-content")
	private WebElement TABLE_VIEWAPPLICATION;

	@FindBy(id = "userlogin")
	private WebElement CLICK_USER;

	@FindBy(id = "userlogout")
	private WebElement CLICK_LOGOUT;

	@FindBy(id = "expandId")
	private WebElement EXPAND_BUTTON;

	@FindBy(xpath = "//tr[@id='0']/td/span[@class='fa fa-plus-circle']")
	private WebElement EXPAND_BUTTON_SERVERSECTION;

	@FindBy(xpath = "//*[@id='fdApplicationTable']/tbody/tr[1]/td[2]")
	private WebElement APPLICATION_NAME;

	@FindBy(xpath = "//*[@id='fdApplicationTable']/tbody/tr[1]/td[3]")
	private WebElement CATEGORY;

	@FindBy(xpath = "//*[@id='fdApplicationTable']/tbody/tr[1]/td[4]")
	private WebElement ENVIRONMENT;

	@FindBy(xpath = "//*[@id='viewAppCredentialTable0']/tbody/tr/td[1]")
	private WebElement USERNAME;

	@FindBy(xpath = "//*[@id='0']/td[2]")
	private WebElement SERVERNAME;

	@FindBy(xpath = "//*[@id='0']/td[4]")
	private WebElement HOSTNAME;

	@FindBy(xpath = "//*[@id='0']/td[10]")
	private WebElement IPADDRESS;

	@FindBy(xpath = "//*[@id='fdApplicationTable_filter']/label/input")
	private WebElement SEARCH_TEXT;

	@FindBy(xpath = "//*[@id='fdApplicationTable']/tbody/tr/td")
	private WebElement ERROR_TEXT_NO_RECORDS;

	protected WebDriver driver;

	public ViewApplicationPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void click_on_view_application_link() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.CLICK_VIEWAPPLICATION));
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_VIEWAPPLICATION, "click", "0,0");
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		Assert.assertTrue("Unable to display the table", elementIsPresent(this.TABLE_VIEWAPPLICATION));
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

	public void search_for_an_application(String s) throws InterruptedException {
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.SEARCH_TEXT, "click", "0,0");
		this.SEARCH_TEXT.sendKeys(s);
		Thread.sleep(4000);
	}

	public void application_details_validation(List<AddApplicationUITab1Data> appDataList) throws InterruptedException {
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.EXPAND_BUTTON, "click", "0,0");
		Thread.sleep(2000);
		for (AddApplicationUITab1Data appdata : appDataList) {
			AssertJUnit.assertTrue("application name not matched",
					this.APPLICATION_NAME.getText().equals(appdata.applicationName));
			AssertJUnit.assertTrue("application name not matched",
					this.CATEGORY.getText().equals(appdata.categoryIndex));
			AssertJUnit.assertTrue("application name not matched",
					this.ENVIRONMENT.getText().equals(appdata.environmentIndex));
		}
	}

	public void applications_server_section_details(List<AddApplicationUITab1Data> appDataList)
			throws InterruptedException {

		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.EXPAND_BUTTON_SERVERSECTION, "click", "0,0");
		Thread.sleep(3000);
		for (AddApplicationUITab1Data appdata : appDataList) {
			AssertJUnit.assertTrue("host name not matched", this.HOSTNAME.getText().equals(appdata.hostName));
			AssertJUnit.assertTrue("application name not matched", this.IPADDRESS.getText().equals(appdata.ipAddress));
		}
	}

	public void entering_unavailable_application_name() throws InterruptedException {
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.SEARCH_TEXT, "click", "0,0");
		this.SEARCH_TEXT.sendKeys("zzzzz");
		Thread.sleep(4000);
		AssertJUnit.assertTrue("error message is not as expected",
				this.ERROR_TEXT_NO_RECORDS.getText().contains("records"));
	}
}