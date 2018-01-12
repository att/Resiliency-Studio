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
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
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

public class ViewMonkeyStrategy extends ParentPage {

	JavascriptLibrary jsLib = new JavascriptLibrary();

	@FindBy(id = "chaosMonkey")
	private WebElement VIEW_MONKEY;
	@FindBy(xpath = "//button/span[@class='glyphicon glyphicon-cog']")
	private WebElement SETTINGS_BUTTON;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[1]")
	private WebElement STRATEGY_NAME_HEADER;
	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[2]")
	private WebElement STRATEGY_DESCRIPTION_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[7]")
	private WebElement SCRIPT_HEADER;
	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[14]")
	private WebElement CLONE_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[13]")
	private WebElement DELETE_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[12]")
	private WebElement EDIT_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[10]")
	private WebElement FAILURE_CATEGORY_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[8]")
	private WebElement OS_TYPE_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[9]")
	private WebElement FLAVOR_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[11]")
	private WebElement FAILURE_SUBCATEGORY_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[17]")
	private WebElement LAST_MODIFIED_BY_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[18]")
	private WebElement LAST_MODIFIED_DATE_HEADER;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[15]")
	private WebElement CREATED_BY_HEADER;
	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[16]")
	private WebElement CREATED_DATE_HEADER;
	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[3]")
	private WebElement VERSION_HEADER;
	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[4]")
	private WebElement DEFAULT_FLAG_HEADER;
	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[5]")
	private WebElement GENERIC_FLAG_HEADER;
	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr[1]/th[6]")
	private WebElement SCRIPT_TYPE;

	@FindBy(xpath = "//a/label/input[@ng-click='osTypeToggle = !osTypeToggle']")
	private WebElement OSTYPE_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='flavorToggle = !flavorToggle']")
	private WebElement FLAVOR_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='failureCategoryToggle = !failureCategoryToggle']")
	private WebElement FAILURE_CATEGORY_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='failureSubToggle = !failureSubToggle']")
	private WebElement FAILURESUB_CATEGORY_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='versionToggle = !versionToggle']")
	private WebElement VERSION_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='defaultToggle = !defaultToggle']")
	private WebElement DEFAULTTAG_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='genericToggle = !genericToggle']")
	private WebElement GENERIC_FLAG_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='createdBy = !createdBy']")
	private WebElement CREATED_BY_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='createDate = !createDate']")
	private WebElement CREATED_DATE_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='lastModifyBy = !lastModifyBy']")
	private WebElement LASTMODIFIED_BY_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='lastModifyDate = !lastModifyDate']")
	private WebElement LAST_MODIFIED_DATE_CHECK;
	@FindBy(xpath = "//a/label/input[@ng-click='scriptTypeToggle = !scriptTypeToggle']")
	private WebElement SCRIPT_TYPE_CHECK;

	@FindBy(xpath = "//button[@class='close']")
	private WebElement CLOSE_WINDOW;

	@FindBy(partialLinkText = "DOCTOR ")
	private WebElement DOCTOR_TAB;
	@FindBy(partialLinkText = "LATENCY ")
	private WebElement LATENCY_TAB;

	protected WebDriver driver;
	private WebElement monkeyname = null;
	private WebElement viewscript = null;
	private static int searchmonkeyrow;

	public ViewMonkeyStrategy(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void clicking_on_view_monkey() throws InterruptedException {
		Thread.sleep(2000);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.VIEW_MONKEY, "click", "0,0");
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
		Thread.sleep(2000);
		AssertJUnit.assertTrue("unable to redirect to view monkey page", this.SETTINGS_BUTTON.isDisplayed());
	}

	public void default_column_check_after_page_load() throws InterruptedException {
		AssertJUnit.assertTrue("table columns are not present as expected ", this.STRATEGY_NAME_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected",
				this.STRATEGY_DESCRIPTION_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected", this.SCRIPT_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected", this.CLONE_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected", this.DELETE_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected", this.EDIT_HEADER.isDisplayed());
	}

	public void clicking_on_setting_button_and_check_for_the_expected_columns() throws InterruptedException {
		this.SETTINGS_BUTTON.click();
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LOW_TIMEOUT, TimeUnit.SECONDS);

		AssertJUnit.assertTrue("columns are not as expected in the settings button", this.OSTYPE_CHECK.isDisplayed());

		AssertJUnit.assertTrue("columns are not as expected in the settings button", this.FLAVOR_CHECK.isDisplayed());
		AssertJUnit.assertTrue("columns are not as expected in the settings button",
				this.FAILURE_CATEGORY_CHECK.isDisplayed());
		AssertJUnit.assertTrue("columns are not as expected in the settings button",
				this.FAILURESUB_CATEGORY_CHECK.isDisplayed());
		AssertJUnit.assertTrue("columns are not as expected in the settings button", this.VERSION_CHECK.isDisplayed());
		AssertJUnit.assertTrue("columns are not as expected in the settings button",
				this.CREATED_BY_CHECK.isDisplayed());
		AssertJUnit.assertTrue("columns are not as expected in the settings button",
				this.CREATED_DATE_CHECK.isDisplayed());
		AssertJUnit.assertTrue("columns are not as expected in the settings button",
				this.LASTMODIFIED_BY_CHECK.isDisplayed());
		AssertJUnit.assertTrue("columns are not as expected in the settings button",
				this.LAST_MODIFIED_DATE_CHECK.isDisplayed());
	}

	public void clicking_on_checkbox_change_on_view_table() throws InterruptedException {
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.OSTYPE_CHECK, "click", "0,0");

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LOW_TIMEOUT, TimeUnit.SECONDS);
		Thread.sleep(3000);
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.OS_TYPE_HEADER.isDisplayed());
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.OSTYPE_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.FLAVOR_CHECK, "click", "0,0");

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LOW_TIMEOUT, TimeUnit.SECONDS);
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.FLAVOR_HEADER.isDisplayed());
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.FLAVOR_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.GENERIC_FLAG_CHECK, "click", "0,0");

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LOW_TIMEOUT, TimeUnit.SECONDS);
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.GENERIC_FLAG_HEADER.isDisplayed());

		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.GENERIC_FLAG_CHECK, "click", "0,0");
	}

	public void clicking_all_checkboxes_and_change_view() throws InterruptedException {

		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.OSTYPE_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.FLAVOR_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.FAILURE_CATEGORY_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.FAILURESUB_CATEGORY_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.SCRIPT_TYPE_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.VERSION_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.DEFAULTTAG_CHECK, "click", "0,0");

		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.GENERIC_FLAG_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CREATED_BY_CHECK, "click", "0,0");

		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CREATED_DATE_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.LASTMODIFIED_BY_CHECK, "click", "0,0");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.LAST_MODIFIED_DATE_CHECK, "click", "0,0");

		Thread.sleep(PropertyUtil.LOW_TIMEOUT);

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LOW_TIMEOUT, TimeUnit.SECONDS);
		Thread.sleep(5000);
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.GENERIC_FLAG_HEADER.isDisplayed());

		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.FLAVOR_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.OS_TYPE_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.FAILURE_CATEGORY_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.FAILURE_SUBCATEGORY_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.CREATED_BY_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.CREATED_DATE_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.VERSION_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.DEFAULT_FLAG_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.LAST_MODIFIED_BY_HEADER.isDisplayed());
		AssertJUnit.assertTrue("table columns are not present as expected after checking",
				this.LAST_MODIFIED_DATE_HEADER.isDisplayed());
	}

	public void clicking_on_view_script_link() throws InterruptedException {
		this.SETTINGS_BUTTON.click();
		Thread.sleep(PropertyUtil.MEDIUM_TIMEOUT);
		viewscript = driver
				.findElement(By.xpath("//*[@id='frame_chaos']/table/tbody/tr[" + searchmonkeyrow + "]/td[7]/a"));

		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", viewscript, "click", "0,0");

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
		Thread.sleep(PropertyUtil.MEDIUM_TIMEOUT);
		Set<String> windows = driver.getWindowHandles();
		Iterator<String> itr1 = windows.iterator();
		String windowid = itr1.next();
		driver.switchTo().window(windowid);

		Thread.sleep(PropertyUtil.LONG_TIMEOUT);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLOSE_WINDOW, "click", "0,0");

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_TIMEOUT, TimeUnit.SECONDS);
		Thread.sleep(2000);
	}

	public void click_on_other_tabs_of_monkey_type() throws InterruptedException {
		this.DOCTOR_TAB.click();
		this.LATENCY_TAB.click();
	}

	public void checking_the_particularmonkey_exist(String s) {
		List<WebElement> TABLE_MONKEYSTRATGIES_COUNT = driver
				.findElements(By.xpath("//*[@id='frame_chaos']/table/tbody/tr"));
		for (int i = 2; i <= TABLE_MONKEYSTRATGIES_COUNT.size(); i++) {

			monkeyname = driver.findElement(By.xpath("//*[@id='frame_chaos']/table/tbody/tr[" + i + "]/td[1]"));

			if (monkeyname.getText().equalsIgnoreCase(s)) {
				searchmonkeyrow = i;
			}
		}
	}
}