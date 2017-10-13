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

package com.att.tta.rs.cucumber.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public abstract class DSL {

	private WebDriver driver;

	public DSL(WebDriver driver) {
		this.driver = driver;
	}

	public void click(String text) {
		click(By.linkText(text));
	}

	public void click(By by) {
		driver.findElement(by).click();
	}

	public boolean hasElement(By by) {
		return !driver.findElements(by).isEmpty();
	}

	public void enterText(WebElement webElement, String text) {
		webElement.sendKeys(text);
	}

	public WebElement getElementByXpath(String xpath) {
		return driver.findElement(By.xpath(xpath));
	}

	public WebElement getElementById(String id) {
		return driver.findElement(By.id(id));
	}

	public WebElement getElementByClassName(String className) {
		return driver.findElement(By.className(className));
	}

	public WebElement getElementByCssSelector(String selector) {
		return driver.findElement(By.cssSelector(selector));
	}

	public void selectElementByXpath(String xpath, String visibleText, int index) {
		Select select = new Select(driver.findElement(By.xpath(xpath)));
		if (index == -1) {
			select.selectByVisibleText(visibleText);
			return;
		} else {
			select.selectByIndex(index);
		}

	}

	public void selectElementById(String id, String visibleText, int index) {
		Select select = new Select(driver.findElement(By.id(id)));
		if (index == -1) {
			select.selectByVisibleText(visibleText);
			return;
		} else {
			select.selectByIndex(index);
		}

	}

	public boolean elementIsPresent(WebElement element) {
		try {
			element.isDisplayed();
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}

	public boolean elementIsNotEnabled(WebElement element) {
		try {
			if (!element.isEnabled()) {
				return true;
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
		return false;
	}

}