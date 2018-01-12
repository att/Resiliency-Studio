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
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.att.tta.rs.cucumber.framework.ParentPage;
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

/**
 * This class contains the page objects and functions to test the feature
 * 
 * @author sk494t
 *
 */

public class DeleteMonkeyStrategyPage extends ParentPage {

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

	@FindBy(id = "deleteMonkeyStrategy")
	private WebElement DELETE_MONKEYSTRATEGY;

	@FindBy(xpath = "//button[@ng-click='gotoDeleteMonkeyStrategy()']")
	private WebElement CLICK_YES_CONFIRM;

	@FindBy(xpath = "//*[@id='frame_chaos']/table/tbody/tr")
	private List<WebElement> TABLE_MONKEYSTRATEGIES;

	@FindBy(id = "close")
	private WebElement OTHER_WINDOW;

	@FindBy(xpath = "//*[@id='frame_chaos']/table")
	private WebElement WHOLE_TABLE;

	protected WebDriver driver;
	public static WebElement monkeyname;
	public static List<WebElement> resulted_delete_monkey;

	public DeleteMonkeyStrategyPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void click_monkey_strategy_link() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
		Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.CLICK_CHAOSMONKEY));
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_CHAOSMONKEY, "click", "0,0");
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
	}

	public void choosing_the_monkeytype_and_monkeystrategyname(
			List<CloneMonkeyStrategyBasicTabUIData> monkeybasicuidata) throws InterruptedException {
		String monkeytypetemp = null;
		for (CloneMonkeyStrategyBasicTabUIData monkeydata : monkeybasicuidata) {
			if (!monkeydata.monkeyType.equalsIgnoreCase("chaos")) {

				Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
				driver.findElement(By.partialLinkText(monkeydata.monkeyType)).click();
			}
			Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
			monkeytypetemp = monkeydata.monkeyType.toLowerCase();
			List<WebElement> TABLE_MONKEYSTRATGIES_COUNT = driver
					.findElements(By.xpath("//*[@id='frame_" + monkeytypetemp + "']/table/tbody/tr"));
			for (int i = 2; i <= TABLE_MONKEYSTRATGIES_COUNT.size(); i++) {
				monkeyname = driver.findElement(
						By.xpath("//*[@id='frame_" + monkeytypetemp + "']/table/tbody/tr[" + i + "]/td[1]"));
				if (monkeyname.getText().equalsIgnoreCase(monkeydata.monkeyStrategyName)) {
					WebElement deletemonkey = driver.findElement(By
							.xpath("//*[@id='frame_" + monkeytypetemp + "']/table/tbody/tr[" + i + "]/td[13]/button"));
					
					Actions action = new Actions(driver);
					Action clicking = action.moveToElement(deletemonkey).click().build();
					clicking.perform();
					Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);
					click_yes_to_confirm();
					break;
				}
			}
		}
	}

	public void click_delete_icon() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		for (int i = 0; i < resulted_delete_monkey.size(); i++) {
			jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", resulted_delete_monkey.get(i), "click", "0,0");
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOf(this.OTHER_WINDOW));
			click_yes_to_confirm();
		}
	}

	public void click_yes_to_confirm() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_YES_CONFIRM, "click", "0,0");
		Thread.sleep(PropertyUtil.MEDIUM_MILLISEC);

	}
}