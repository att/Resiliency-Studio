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

public class AddMonkeyStrategyFeature extends ParentPage {

	protected static PropertyUtil configProp = new PropertyUtil();

	JavascriptLibrary jsLib = new JavascriptLibrary();

	@FindBy(xpath = "//a[@href='#/addMonkeyStrategy'] /div/div[1]/span")
	private WebElement MONKEY_STRATEGY_LINK_CLICK;
	@FindBy(id = "strategyName")
	private WebElement MONKEY_STRATEGY_NAME;
	@FindBy(id = "monkeyType")
	private WebElement MONKEY_TYPE;
	@FindBy(id = "versionId")
	private WebElement VERSION;
	@FindBy(id = "category")
	private WebElement SCRIPT_CATEGORY;
	@FindBy(name = "script")
	private List<WebElement> SCRIPT_DATA;
	@FindBy(xpath = "//button[@data-ng-click='writeMonkeyScript()']")
	private WebElement CODE_SCRIPT;
	@FindBy(id = "myBody")
	private WebElement BODY_CODE;
	@FindBy(id = "file")
	private WebElement FILE_UPLOAD;
	@FindBy(xpath = "//button[@type='submit']")
	private WebElement ADD_MONKEY;
	@FindBy(xpath = "//a[@ng-show='success']")
	private WebElement MESSAGE_BODY;

	@FindBy(linkText = "Advanced")
	private WebElement ADVANCED_TAB_CLICK;

	@FindBy(id = "osTypeId")
	private WebElement OSTYPE;
	@FindBy(id = "flavourAddFPId")
	private WebElement FLAVOR;
	@FindBy(id = "failureCategoryAddFPId")
	private WebElement FAILURE_CATEGORY;
	@FindBy(id = "failureSubCategoryAddFPId")
	private WebElement FAILURE_SUBCATEGORY;
	@FindBy(xpath = "//label[@for='genericFlagId']/span")
	private WebElement GENERIC_TOGGLE;

	@FindBy(id = "resetMonkeyStrategy")
	private WebElement RESET_BUTTON;

	@FindBy(linkText = "Basic")
	private WebElement BASIC_TAB_CLICK;
	@FindBy(xpath = "//div[@ng-show='showErrorMessage']")
	private WebElement ERROR_MESSAGE;

	protected WebDriver driver;
	String monkeystrategyname = "";
	String monkeytype = "";

	public AddMonkeyStrategyFeature(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void click_on_addmonkey() throws InterruptedException {
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("not available", this.MONKEY_STRATEGY_LINK_CLICK.isDisplayed());
		this.MONKEY_STRATEGY_LINK_CLICK.click();
		Thread.sleep(8000);
	}

	public void fill_the_basic_tab_data(List<CloneMonkeyStrategyBasicTabUIData> basicdata) throws InterruptedException {
		// need to send the data for the basic tab data
		Iterator<CloneMonkeyStrategyBasicTabUIData> iterator = basicdata.iterator();
		CloneMonkeyStrategyBasicTabUIData basictabdata = iterator.next();
		monkeystrategyname = basictabdata.monkeyStrategyName;
		monkeytype = basictabdata.monkeyType;
		Thread.sleep(6000);
		this.MONKEY_STRATEGY_NAME.sendKeys(basictabdata.monkeyStrategyName);
		new Select(this.MONKEY_TYPE).selectByVisibleText(basictabdata.monkeyType);
		AssertJUnit.assertTrue("version is enabled which is not expected", !(this.VERSION.isEnabled()));
	}

	public void modify_the_script() throws InterruptedException {
		// upload the script and code the script and do a permutation like that
		new Select(this.SCRIPT_CATEGORY).selectByVisibleText("UNIX Script");
		this.SCRIPT_DATA.get(1).click();
		Thread.sleep(5000);
		this.CODE_SCRIPT.click();
		Thread.sleep(5000);
		Set<String> windows = driver.getWindowHandles();
		Iterator<String> itr1 = windows.iterator();
		String windowid = itr1.next();
		driver.switchTo().window(windowid);
		WebElement textcode = driver.findElement(By.xpath("//*[@id='myBody']/textarea"));
		textcode.click();
		textcode.sendKeys("text here");
		driver.findElement(By.xpath("//*[@id='writeScriptModal']/div/div/div[3]/button[2]")).click();

		Thread.sleep(2000);
		this.SCRIPT_DATA.get(0).click();
		Thread.sleep(3000);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.ADD_MONKEY, "click", "0,0");
	}

	public void add_monkey_and_verify_success_message() throws InterruptedException {
		// click on add button and verify the success message and see the all
		// the filled fields got reset
		Thread.sleep(10000);
		AssertJUnit.assertTrue("unable to display the message", this.MESSAGE_BODY.isDisplayed());

		AssertJUnit.assertTrue("monkey strategy name is not as expected",
				this.MESSAGE_BODY.getText().contains(monkeystrategyname));
		AssertJUnit.assertTrue("doesnt selected upload script properly", this.SCRIPT_DATA.get(0).isSelected());
		AssertJUnit.assertTrue("unable to reset the monkey type",
				new Select(this.MONKEY_TYPE).getFirstSelectedOption().getText().contains("Select Monkey"));
		Thread.sleep(1000);
	}

	public void click_on_addmonkey_of_html_fillup_basictab_data() throws InterruptedException {
		// clicking on the add monkey strategy button which is on side of html
		// and fill the basic tab data

		this.MONKEY_STRATEGY_NAME.sendKeys("TestMonkey2");
		new Select(this.MONKEY_TYPE).selectByIndex(1);

		new Select(this.SCRIPT_CATEGORY).selectByVisibleText("UNIX Script");
		this.SCRIPT_DATA.get(1).click();
		Thread.sleep(5000);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CODE_SCRIPT, "click", "0,0");
		Thread.sleep(5000);

		Set<String> windows = driver.getWindowHandles();
		Iterator<String> itr1 = windows.iterator();
		String windowid = itr1.next();
		driver.switchTo().window(windowid);
		WebElement textcode = driver.findElement(By.xpath("//*[@id='myBody']/textarea"));
		textcode.click();
		textcode.sendKeys("text here");
		driver.findElement(By.xpath("//*[@id='writeScriptModal']/div/div/div[3]/button[2]")).click();

		Thread.sleep(2000);
		this.SCRIPT_DATA.get(0).click();
		Thread.sleep(3000);
		this.ADVANCED_TAB_CLICK.click();
		Thread.sleep(4000);

		AssertJUnit.assertTrue("unable to go to advance tab ", this.OSTYPE.isDisplayed());
	}

	public void filling_advance_tab_data_verifying_mandtoryfields(
			List<CloneMonkeyStrategyAdvanceTabUIData> advancetabdata) throws InterruptedException {
		// clicking on the advance tab and clicking on the generic toggle on and
		// verifying all the fields in the advance tab
		// send the values for the advance tab fields
		Iterator<CloneMonkeyStrategyAdvanceTabUIData> itr = advancetabdata.iterator();
		CloneMonkeyStrategyAdvanceTabUIData advancedata = itr.next();
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.GENERIC_TOGGLE, "click", "0,0");
		this.GENERIC_TOGGLE.click();
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		if (!(this.GENERIC_TOGGLE.isSelected())) {
			this.GENERIC_TOGGLE.click();
			Thread.sleep(8000);
		}

		AssertJUnit.assertTrue("not set to required", this.OSTYPE.getAttribute("required").equals("true"));
		new Select(this.OSTYPE).selectByIndex(advancedata.osType);
		this.ADD_MONKEY.click();
		Thread.sleep(2000);
		AssertJUnit.assertTrue("should not be displayed", this.MESSAGE_BODY.isDisplayed());
		new Select(this.FLAVOR).selectByIndex(advancedata.flavor);
		this.ADD_MONKEY.click();
		Thread.sleep(2000);
		AssertJUnit.assertTrue("should not be displayed", this.MESSAGE_BODY.isDisplayed());

		Thread.sleep(2000);
		new Select(this.FAILURE_CATEGORY).selectByVisibleText(advancedata.failureCategory);
		AssertJUnit.assertTrue("not disabled ideally should be disabled", !(this.FLAVOR.isEnabled()));

		if (new Select(this.FAILURE_CATEGORY).getFirstSelectedOption().getText().equals("software")) {
			new Select(this.FAILURE_SUBCATEGORY).selectByVisibleText(advancedata.failureSubCategory);
		}
		this.ADD_MONKEY.click();
		Thread.sleep(5000);
		AssertJUnit.assertTrue("ideally should be displayed", this.MESSAGE_BODY.isDisplayed());
	}

	public void add_the_monkeystrategycheck_on_toggle() throws InterruptedException {
		// click on the add button and verify the success message and make sure

		AssertJUnit.assertTrue("unexpected message may be dupliacte", this.MESSAGE_BODY.isDisplayed());
		Thread.sleep(6000);
	}

	public void adding_monkey_with_samename_and_validate_error_message() throws InterruptedException {
		// fill up the basic tab data with same monkey strategy and verify the
		// error message
		this.BASIC_TAB_CLICK.click();
		Thread.sleep(3000);
		this.MONKEY_STRATEGY_NAME.sendKeys("TestMonkey2");
		new Select(this.MONKEY_TYPE).selectByIndex(1);
		new Select(this.SCRIPT_CATEGORY).selectByVisibleText("UNIX Script");
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.SCRIPT_DATA.get(1), "click", "0,0");

		Thread.sleep(5000);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CODE_SCRIPT, "click", "0,0");

		Thread.sleep(5000);
		Set<String> windows = driver.getWindowHandles();
		Iterator<String> itr1 = windows.iterator();
		String windowid = itr1.next();
		driver.switchTo().window(windowid);
		WebElement textcode = driver.findElement(By.xpath("//*[@id='myBody']/textarea"));
		textcode.click();
		textcode.sendKeys("text here");
		driver.findElement(By.xpath("//*[@id='writeScriptModal']/div/div/div[3]/button[2]")).click();
		Thread.sleep(2000);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.ADD_MONKEY, "click", "0,0");

		Thread.sleep(4000);
		AssertJUnit.assertTrue("message which is displayed is not as expected", this.ERROR_MESSAGE.isDisplayed());
	}

	public void click_on_toggle_and_reset() throws InterruptedException {
		// by clicking on the generic toggle on and click the reset button
		this.GENERIC_TOGGLE.click();
		Thread.sleep(5000);
		if (!(driver.findElement(By.id("genericFlagId")).isSelected())) {
			this.GENERIC_TOGGLE.click();
			Thread.sleep(8000);
		}
		Thread.sleep(5000);
		AssertJUnit.assertTrue("generic toggle is not selected",
				driver.findElement(By.id("genericFlagId")).isSelected());
		this.RESET_BUTTON.click();
		Thread.sleep(6000);
	}
}