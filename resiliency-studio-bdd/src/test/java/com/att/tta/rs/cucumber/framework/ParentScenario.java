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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class ParentScenario {

	protected static WebDriver driver = null;
	protected static String broserURL = null;

	protected static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();

	protected static PropertyUtil configProp = new PropertyUtil();

	protected void startBrowser() {
		
		broserURL = configProp.getValue("chromeDriverURL");
		
		driver = getBrowser(configProp.getValue("browser"));
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}

	protected void navigateTo(String url) {
		
		driver.navigate().to(configProp.getValue("baseurl")+url);
	}

	protected void closeBrowser() {
		driver.close();
		driver.quit();
	}

	/*
	 * Factory method for getting browsers
	 */
	public static WebDriver getBrowser(String browserName) {
		WebDriver driver = null;
		switch (browserName) {
		case "Firefox":
			driver = drivers.get("Firefox");
			if (driver == null) {
				driver = new FirefoxDriver();
				drivers.put("Firefox", driver);
			}
			break;
		case "IE":
			driver = drivers.get("IE");
			if (driver == null) {
				System.setProperty("webdriver.ie.driver", broserURL);
				driver = new InternetExplorerDriver();
				drivers.put("IE", driver);
			}
			break;
		case "chrome":
			
			driver = drivers.get("Chrome");
			if (driver == null) {
				System.setProperty("webdriver.chrome.driver", broserURL);
				driver = new ChromeDriver();
				
				drivers.put("Chrome", driver);
			}
			break;
		}
		
		return driver;
	}

}
