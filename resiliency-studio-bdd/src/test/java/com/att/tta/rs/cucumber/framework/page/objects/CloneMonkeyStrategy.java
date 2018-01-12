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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
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
public class CloneMonkeyStrategy extends ParentPage {

	@FindBy(xpath = "//*[@id='chaosMonkey']/div/div[1]/span[2]")
	private WebElement MONKEY_VIEW;

	@FindBy(linkText = "Clone")
	private List<WebElement> CLONE_LINK;

	@FindBy(id = "versionId")
	private WebElement VERSION;

	@FindBy(id = "monkeyType")
	private WebElement MONKEY_TYPE;

	@FindBy(id = "strategyName")
	private WebElement MONKEY_STRATEGY_NAME;

	@FindBy(id = "category")
	private WebElement SCRIPT_CATEGORY;

	@FindBy(xpath = "//label[@for='genericFlagId']/span")
	private WebElement GENERIC_TOGGLE;

	@FindBy(id = "osTypeId")
	private WebElement OS_TYPE;

	@FindBy(id = "flavourAddFPId")
	private WebElement FLAVOR;

	@FindBy(id = "failureCategoryAddFPId")
	private WebElement FAILURE_CATEGORY;

	@FindBy(id = "failureSubCategoryAddFPId")
	private WebElement FAILURE_SUBCATEGORY;

	@FindBy(id = "advance")
	private WebElement ADVANCE_CLICK;

	@FindBy(id = "addMonkeyStrategy")
	private WebElement CLONE_BUTTON;

	@FindBy(partialLinkText = "View Monkey Strategy")
	private WebElement SUCCES_CLONE;

	@FindBy(xpath = "//*[@id='cloneMonkey']/div/div/div[2]/div[3]/div/div[1]/div[3]/div/div[2]/div/button")
	private WebElement VIEW_SCRIPT;

	@FindBy(id = "myBody")
	private WebElement SCRIPT_CONTENT;

	@FindBy(xpath = "//*[@id='myContent']/form/div[1]/button")
	private WebElement SCRIPT_CONTENT_CLOSE;

	@FindBy(name = "script")
	private List<WebElement> SCRIPT;

	@FindBy(xpath = "//*[@id='cloneMonkey']/div/div/div[2]/div[3]/div/div[1]/div[3]/div/div[3]/button")
	private WebElement CODE_SCRIPT_BUTTON;

	@FindBy(id = "upload")
	private WebElement UPLOAD_SCRIPT;

	@FindBy(id = "file") // id will be created
	private WebElement FILE_UPLOAD;

	@FindBy(id = "resetMonkeyStrategy")
	private WebElement RESET_BUTTON;

	@FindBy(linkText = "Basic")
	private WebElement BASIC_TAB;

	private WebElement monkeyname = null;
	private static int searchmonkeyrow;
	protected WebDriver driver;
	JavascriptLibrary jsLib = new JavascriptLibrary();

	public CloneMonkeyStrategy(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void click_on_viewing_monkeystrategies() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		this.MONKEY_VIEW.click();
		driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("unable to redirect to view monkey strategies", this.CLONE_LINK.get(0).isDisplayed());
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);

	}

	public void click_on_clone_link(String s) {

		List<WebElement> TABLE_MONKEYSTRATGIES_COUNT = driver
				.findElements(By.xpath("//*[@id='frame_chaos']/table/tbody/tr"));
		for (int i = 2; i <= TABLE_MONKEYSTRATGIES_COUNT.size(); i++) {

			monkeyname = driver.findElement(By.xpath("//*[@id='frame_chaos']/table/tbody/tr[" + i + "]/td[1]"));

			if (monkeyname.getText().equalsIgnoreCase(s)) {
				searchmonkeyrow = i;
				WebElement clonemonkey = driver.findElement(
						By.xpath("//*[@id='frame_chaos']/table/tbody/tr[" + searchmonkeyrow + "]/td[14]/a"));
				Actions action = new Actions(driver);
				Action clicking = action.moveToElement(clonemonkey).click().build();
				clicking.perform();
			}
		}

	}

	// monkeyStrategy name validation and viewing the script and closing it
	public void validate_the_clone_strategy_name() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("strategy name is not as expected" + this.MONKEY_STRATEGY_NAME.getAttribute("value"),
				this.MONKEY_STRATEGY_NAME.getAttribute("value").contains("Clone"));
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.VIEW_SCRIPT, "click", "0,0");

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("script content is not visible", this.SCRIPT_CONTENT.isDisplayed());
		AssertJUnit.assertTrue("script doesnt contain any values", !(this.SCRIPT_CONTENT.getText().matches("\\s")));
		this.SCRIPT_CONTENT_CLOSE.click();
	}

	// trying to edit the script present i mean coding the script
	public void edit_the_script() throws InterruptedException {

		this.SCRIPT.get(1).click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("code radio doesnt clicked", this.SCRIPT.get(1).isSelected());// driver.findElement(By.xpath("//*[@id='cloneMonkey']/div/div/div[2]/div[2]/div/div[1]/div[3]/div/div[1]/label[2]/input")).isSelected());
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("code button not present", this.CODE_SCRIPT_BUTTON.isDisplayed());
		this.CODE_SCRIPT_BUTTON.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);

		driver.findElement(By.xpath("//*[@id='myBody']/textarea")).getText().concat("updated1");
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		driver.findElement(By.xpath("//*[@id='myContent']/form/div[3]/button[2]")).click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

	// provide values for the monkeytype and monkeystrategyname updation
	public void mandatory_fileds_filled(List<CloneMonkeyStrategyBasicTabUIData> data) {
		Iterator<CloneMonkeyStrategyBasicTabUIData> iterator1 = data.iterator();
		CloneMonkeyStrategyBasicTabUIData basicdata = iterator1.next();

		AssertJUnit.assertTrue("monkey type is not having any value",
				!(new Select(this.MONKEY_TYPE).getFirstSelectedOption().getText().matches("\\s")));
		new Select(this.MONKEY_TYPE).selectByValue(basicdata.monkeyType);
		this.MONKEY_STRATEGY_NAME.sendKeys(basicdata.monkeyStrategyName);
		
	}

	// uploading the script
	public void upload_the_new_script() throws InterruptedException {

		edit_the_script();
		new Select(this.SCRIPT_CATEGORY).selectByValue("UNIX Script");
		this.SCRIPT.get(0).click();

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
		this.FILE_UPLOAD.sendKeys(System.getProperty("user.dir") + "//src//test//resources//ProcessStatus.sh");
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

	public void click_on_advance_tab() throws InterruptedException {
		this.ADVANCE_CLICK.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("unable to redirect to advance tab", this.OS_TYPE.isDisplayed());
	}

	// making generic toggle on and checking the mandatory fields by giving
	// different values
	public void checking_generic_toggle_is_on(List<CloneMonkeyStrategyAdvanceTabUIData> advanceclonedata)
			throws InterruptedException {
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		
		Iterator<CloneMonkeyStrategyAdvanceTabUIData> iterator2 = advanceclonedata.iterator();
		CloneMonkeyStrategyAdvanceTabUIData advancedata = iterator2.next();

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		String generictoggleclickcheck = driver.findElement(By.id("genericFlagId")).getAttribute("class");
		
		if (generictoggleclickcheck.equals("ng-valid ng-dirty ng-valid-parse ng-touched ng-empty")) {
			jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.GENERIC_TOGGLE, "click", "0,0");
			Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
				}
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);

		new Select(this.OS_TYPE).selectByIndex(advancedata.osType);

		new Select(this.FAILURE_CATEGORY).selectByVisibleText(advancedata.failureCategory);
		AssertJUnit.assertTrue("failure sub category is disabled which is not expected",
				this.FAILURE_SUBCATEGORY.isEnabled());
		if (new Select(this.FAILURE_CATEGORY).getFirstSelectedOption().getText().equals("software")) {
			new Select(this.FAILURE_SUBCATEGORY).selectByVisibleText(advancedata.failureSubCategory);
		}

		AssertJUnit.assertTrue("drop down not enabled", !(this.FLAVOR.isEnabled()));

	}

	// clicking on clone button and validating success message
	public void clicking_on_clone_button() throws InterruptedException {
		// sending data in the advance tab and cloning.

		this.CLONE_BUTTON.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("success message is not visible which is not expected",
				driver.findElement(By.xpath("//*[@id='cloneMonkey']/div/div/div[2]/div[1]")).isDisplayed());
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		String message = driver.findElement(By.xpath("//*[@id='cloneMonkey']/div/div/div[2]/div[1]")).getText();
		AssertJUnit.assertTrue(
				"already strategy with that name exists and please try once again iwth other strategy name" + message,
				message.contains("success"));

	}

	// after cloning we need to check the mandatory fields behaviour for the
	// toggle is on
	public void after_submit_check_the_mandatory_fields() throws InterruptedException {
		AssertJUnit.assertTrue("ideally generic toggle should not be on", !(this.GENERIC_TOGGLE.isSelected()));

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);

	}

	// with out filling mandatory fields try to clone which is negative
	public void checking_mandatory_fields_filling() throws InterruptedException {

		this.CLONE_BUTTON.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("success message is visible which is not expected",
				!(driver.findElement(By.xpath("//*[@id='cloneMonkey']/div/div/div[2]/div[1]")).isDisplayed()));

	}

	// after reset the clone for a strategy which is having generic toggle is on
	public void validate_reset_over_generic_toggle() throws InterruptedException {

		this.SUCCES_CLONE.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		this.CLONE_LINK.get(0).click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		this.ADVANCE_CLICK.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);

		this.RESET_BUTTON.click();

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("toggle is in on", !(this.GENERIC_TOGGLE.isSelected()));

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		if (!(this.GENERIC_TOGGLE.isSelected())) {
			this.GENERIC_TOGGLE.click();
			Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
			
		}
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);

		AssertJUnit.assertTrue("toggle is not in on", driver.findElement(By.id("genericFlagId")).isSelected());
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		this.RESET_BUTTON.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("ideally generic toggle should not be on", !(this.GENERIC_TOGGLE.isSelected()));
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		this.BASIC_TAB.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

	// need to provide values
	public void submitting_clone_by_filling_basictab_only() throws InterruptedException {
		this.MONKEY_STRATEGY_NAME.sendKeys("updated");
		new Select(this.MONKEY_TYPE).selectByValue("Latency");
		this.ADVANCE_CLICK.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		if (this.GENERIC_TOGGLE.isSelected()) {
			this.GENERIC_TOGGLE.click();
		}

	}

}
