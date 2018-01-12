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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.AssertJUnit;

import com.att.tta.rs.cucumber.framework.ParentPage;
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

public class EditMonkeyStrategyPage extends ParentPage {

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

	@FindBy(id = "chaosMonkey")
	private WebElement CLICK_CHAOSMONKEY;

	@FindBy(id = "editMonkeyStrategy")
	private List<WebElement> EDIT_MONKEYSTRATEGY_CLICK;

	@FindBy(id = "monkeyType")
	private WebElement MONKEYTYPE_SELECTOR;

	@FindBy(id = "strategyName")
	private WebElement STRATEGYNAME_TEXTBOX;

	@FindBy(id = "descriptionName")
	private WebElement DESCRIPTIONNAME_TEXTBOX;

	@FindBy(id = "file")
	private WebElement FILE_UPLOADER;

	@FindBy(id = "versionId")
	private WebElement VERSION_TEXTBOX;

	@FindBy(id = "addMonkeyStrategy")
	private WebElement UPDATE_MONKEY_STRATEGY;

	@FindBy(id = "category")
	private WebElement SCRIPT_CATEGORY;

	@FindBy(linkText = "Advanced")
	private WebElement ADVANCETAB_CLICK;

	@FindBy(id = "osTypeId")
	private WebElement OS_TYPE;

	@FindBy(xpath = "//label[@for='genericFlagId']/span")
	private WebElement GENERIC_TOGGLE;

	@FindBy(id = "flavourAddFPId")
	private WebElement FLAVOR;
	@FindBy(id = "failureCategoryAddFPId")
	private WebElement FAILURE_CATEGORY;
	@FindBy(id = "failureSubCategoryAddFPId")
	private WebElement FAILURE_SUB_CATEGORY;
	@FindBy(id = "resetMonkeyStrategy")
	private WebElement RESET_CLICK;

	@FindBy(linkText = "Basic")
	private WebElement BASIC_TAB;
	@FindBy(name = "script")
	private List<WebElement> SCRIPT;

	@FindBy(xpath = "//div[@ng-show='showSuccessMessage']")
	private WebElement RESULT_TEXT;

	@FindBy(id = "myBody")
	private WebElement SCRIPT_BODY;

	@FindBy(xpath = "//*[@id='myBody']/textarea")
	private WebElement TEXT_SCRIPT_BODY;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr/td[1]")
	private List<WebElement> MONKEY_STRATEGIES_LIST;

	@FindBy(xpath = "//*[@id='chaosMonkey']/div/div[1]/span[2]")
	private WebElement MONKEY_VIEW;

	@FindBy(xpath = "//th[text()='Edit']")
	private WebElement EDIT_HEADER;

	private static String Monkeytype = "";
	private String MonkeyStrategy = "";
	private String version = "";
	private int column_count;

	protected WebDriver driver;

	public EditMonkeyStrategyPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void clicking_on_view_monkeys_to_edit() throws InterruptedException {

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		this.MONKEY_VIEW.click();

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("unable to redirect to view monkey strategies", this.EDIT_HEADER.isDisplayed());
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

	public void click_edit_icon_for_Editing_monkeystrategy(String s) throws InterruptedException {

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		for (int i = 0; i < this.MONKEY_STRATEGIES_LIST.size(); i++) {
			if (this.MONKEY_STRATEGIES_LIST.get(i).getText().equalsIgnoreCase(s)) {
				column_count = i;

			}
		}
		this.EDIT_MONKEYSTRATEGY_CLICK.get(column_count).click();

		Thread.sleep(PropertyUtil.LOW_MILLISEC);
		AssertJUnit.assertTrue("unable to redirect timed out", this.VERSION_TEXTBOX.isDisplayed());
	}

	public void check_fields_got_disabled() throws InterruptedException {
		AssertJUnit.assertTrue("not disabled on pageload", !(this.VERSION_TEXTBOX.isEnabled()));
		AssertJUnit.assertTrue("not disabled on pageload", !(this.MONKEYTYPE_SELECTOR.isEnabled()));
		AssertJUnit.assertTrue("not disabled on pageload", !(this.STRATEGYNAME_TEXTBOX.isEnabled()));

		MonkeyStrategy = this.STRATEGYNAME_TEXTBOX.getText();
		Monkeytype = new Select(this.MONKEYTYPE_SELECTOR).getFirstSelectedOption().getText();
		System.out.println("mine" + Monkeytype);
		version = this.VERSION_TEXTBOX.getText();
	}

	public void check_fields_in_basic_tab_are_filled() throws InterruptedException {
		System.out.println("mine" + Monkeytype);
		AssertJUnit.assertTrue("not selected the script category", !(new Select(this.SCRIPT_CATEGORY)
				.getFirstSelectedOption().getAttribute("value").equalsIgnoreCase("Select Script")));
		// clicking on view script
		// get the text area data and should not match with space ok
		if (this.SCRIPT.get(0).isSelected()) {
			AssertJUnit.assertTrue("no file uploaded", !(this.FILE_UPLOADER.getText().equals("\\s")));
		}

	}

	public void check_fields_in_advance_tab_accordingly() throws InterruptedException {
		this.ADVANCETAB_CLICK.click();

		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		System.out.println(this.GENERIC_TOGGLE.isSelected());
		if (this.GENERIC_TOGGLE.isSelected()) {
			AssertJUnit.assertTrue("os type not selected though generic toggle is on",
					!(new Select(this.OS_TYPE).getFirstSelectedOption().getText().equalsIgnoreCase("Select OSType")));
			AssertJUnit.assertTrue("flavor not selected though generic toggle is on", this.FLAVOR.isEnabled());
			AssertJUnit.assertTrue("failuer category not selected though generic toggle is on",
					!(new Select(this.FAILURE_CATEGORY).getFirstSelectedOption().getText()
							.equalsIgnoreCase("Select Category")));
		}

	}

	public void clicking_update_to_edit_monkey() throws InterruptedException {

		this.UPDATE_MONKEY_STRATEGY.click();
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		AssertJUnit.assertTrue("success message not found which is not expected",
				this.RESULT_TEXT.getText().contains("Successfully"));
	}

}
