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

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.att.tta.rs.cucumber.framework.ParentPage;
import com.att.tta.rs.cucumber.framework.PropertyUtil;
import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

public class ScenariosRunYTDPage extends ParentPage {

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

	@FindBy(xpath = "//a[@href='#/scenariosRunYTD']//span[2]")
	private WebElement CLICK_SCENARIOSRUN_YTD;

	@FindBy(id = "cell-content")
	private WebElement ELEMENT_VIEW;

	protected WebDriver driver;

	public ScenariosRunYTDPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void click_scenario_run_ytd_link() throws InterruptedException {

		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.CLICK_SCENARIOSRUN_YTD));
		Thread.sleep(PropertyUtil.LONG_MILLISEC);
		jsLib.callEmbeddedSelenium(driver, "triggerMouseEventAt", this.CLICK_SCENARIOSRUN_YTD, "click", "0,0");
		driver.manage().timeouts().implicitlyWait(PropertyUtil.LONG_MILLISEC, TimeUnit.MILLISECONDS);

	}

}
