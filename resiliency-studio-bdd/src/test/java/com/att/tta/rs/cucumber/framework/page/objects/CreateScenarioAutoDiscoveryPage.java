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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
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



/**
 * @author sk494t
 *
 */


public class CreateScenarioAutoDiscoveryPage extends ParentPage{
	
	protected static PropertyUtil configProp = new PropertyUtil();

	@FindBy(name = "userid")
	private WebElement USERID_TEXTBOX;
	
	@FindBy(name = "password")
	private WebElement PASSWORD_TEXTBOX;
	
	@FindBy(name = "btnSubmit")
	private WebElement LOGIN_BUTTON;
	
	@FindBy(name = "successOK")
	private WebElement SUCCESS_OK;
	
	@FindBy(xpath="//a[@href='#/createScenario']/div/div[1]/span")
	private WebElement CLICK_CREATESCEANRIO;
	
	@FindBy(id = "applicationName")
	private WebElement APPLICATION_NAME_SELECTOR;
	
	@FindBy(id = "environmentSelect")
	private WebElement ENVIRONMENT_SELECTOR;
	
	@FindBy(id="clickView1")
	private WebElement VIEWsCENARIO_BUTTON;
	
	@FindBy(xpath="//*[@id='rolePanel']/span[1]")
	List<WebElement> metricdet;
	
	@FindBy(xpath="//a[@ng-click='viewAutoDiscovery()']")
	WebElement AUTODISCOVERY_BUTTON;
	
	@FindBy(xpath="//div[@ng-show='showerrorAlert']")
	WebElement ERROR_TEXT;
	protected WebDriver driver;
	
	JavascriptLibrary jsLib = new JavascriptLibrary();
	
	
	static Map <String,WebElement> m= new HashMap<String,WebElement>();
	
	static final String[] MONKEY_STRATEGIES={"TestMonkey2","TestMonkey2Clonestrategy"};
	static String xpaths[]=new String[MONKEY_STRATEGIES.length];
	static String failuretenetxpath[]=new String[MONKEY_STRATEGIES.length]; 
	static String failuremodexpath=null;
	
	public CreateScenarioAutoDiscoveryPage(WebDriver driver) {
		super(driver);
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	public void click_scenario_auto_discovery() throws InterruptedException{
		
		 	driver.manage().timeouts().implicitlyWait(PropertyUtil.MEDIUM_TIMEOUT, TimeUnit.SECONDS);
			Thread.sleep(5000);
			 
			
			driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);Thread.sleep(2000);
			Assert.assertTrue("Unable to Visible the Element", elementIsPresent(this.CLICK_CREATESCEANRIO));
			jsLib.callEmbeddedSelenium(driver,"triggerMouseEventAt", this.CLICK_CREATESCEANRIO,"click", "0,0");
			Thread.sleep(5000);
			//Assert.assertTrue("Unable to Visible the Element", this.APPLICATION_NAME_SELECTOR.isSelected());
	}
	
	
	public void select_application_details(List<AddScenarioAutoDiscoveryUIData> autoDiscoveryList) throws InterruptedException{
		
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		for(AddScenarioAutoDiscoveryUIData autoDiscoveryData : autoDiscoveryList){
			
            driver.findElement(By.xpath("//div[contains(@class,'dropdown-multiselect')]")).click();
            driver.findElement(By.xpath("//input[@ng-model='searchFilter']")).sendKeys(autoDiscoveryData.applicationName);
			Thread.sleep(1000); 
			WebElement test= driver.findElement(By.xpath("//*[@id='applicationListDiv']/div/ul/div/li[1]/span"));
			 test.click();  
			 Thread.sleep(2000);
			new Select(this.ENVIRONMENT_SELECTOR).selectByIndex(Integer.parseInt(autoDiscoveryData.environmentIndex));
		}
		
	}
	
	public void click_auto_discovery_button() throws InterruptedException{
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		this.AUTODISCOVERY_BUTTON.click();
		Thread.sleep(10000);
		//AssertJUnit.assertTrue("no scenarios autodiscovered for this application", !(driver.findElement(By.id("clickView2")).isDisplayed()));
	}
	
	public void open_metric_details() throws InterruptedException{
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		

	jsLib.callEmbeddedSelenium(driver,"triggerMouseEventAt", this.metricdet.get(1),"click", "0,0");
	
	
	}
	
	public void open_scenario_details() throws InterruptedException{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(3000);
		jsLib.callEmbeddedSelenium(driver,"triggerMouseEventAt",driver.findElement(By.xpath("//*[@id='autoDiscovery']/span[1]")) ,"click", "0,0");
		
	}
	
	public void click_submit() throws InterruptedException{
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
	
		jsLib.callEmbeddedSelenium(driver,"triggerMouseEventAt",driver.findElement(By.id("submitScenario")) ,"click", "0,0");
	}
	
	public void click_ok_button() throws InterruptedException{
		
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		
	
		Thread.sleep(3000);boolean value;
	if(value=this.ERROR_TEXT.isDisplayed()){
		AssertJUnit.assertTrue("u r creating duplicating scenarios", !(value));
	}
		
		AssertJUnit.assertTrue("unable to see view scenarios button",this.VIEWsCENARIO_BUTTON.isDisplayed() );
		this.VIEWsCENARIO_BUTTON.click();
	}
	
	
	public void validating_the_resultantmonkeys_as_expected(){
		List<WebElement> tablerows=driver.findElements(By.xpath("//*[@id='autoDiscoveryTable']/tbody/tr"));
		
		int tablesize=tablerows.size();System.out.println(tablesize);
		WebElement element=null; 
		

		 for(int i=1;i<=tablesize;i++){
if(i%2!=0){
			
		 element=driver.findElement(By.xpath("//*[@id='autoDiscoveryTable']/tbody/tr["+i+"]/td[9]"));

		 if((element.getText().equals(MONKEY_STRATEGIES[0]))||(element.getText().equals(MONKEY_STRATEGIES[1]))){
		 m.put(element.getText(),element);
		 }
		
		 }

		 }
		
		 AssertJUnit.assertTrue("scenarios are not generated for the expected monkeys", !(m.isEmpty()));
		 
		 for(int i=0;i<MONKEY_STRATEGIES.length;i++){
		  xpaths[i]=m.get(MONKEY_STRATEGIES[i]).toString().split("xpath:")[1];
		 }
		
			
	}
		 
public void validating_failure_tenet_and_failure_mode_are_as_expected() throws InterruptedException{
	
	
	for (int i = 0; i < xpaths.length; i++) {
		failuretenetxpath[i]=failureTenetgenereate(xpaths[i])[0];
		failuremodexpath=failureTenetgenereate(xpaths[i])[1];
AssertJUnit.assertTrue("failuremode is not as expected", driver.findElement(By.xpath(failuremodexpath)).getAttribute("value").equalsIgnoreCase(MONKEY_STRATEGIES[i]));
	}
	AssertJUnit.assertTrue("failuretenet is not as expected", driver.findElement(By.xpath(failuretenetxpath[0])).getAttribute("value").equalsIgnoreCase("fault"));
	AssertJUnit.assertTrue("failuretenet is not as expected", driver.findElement(By.xpath(failuretenetxpath[1])).getAttribute("value").equalsIgnoreCase("latency"));

	
	
	
	
}

public String[] failureTenetgenereate(String temp){
	 String Failuretenet1[]=new String[2];
	 String xpath[]=new String[2];
	 int i=0;
	 for(String s:temp.split("td")){
		
		 Failuretenet1[i]=s;
	 i++;
	 }
	 String Failuremode1[]=new String[2];
	 Failuremode1[0]=Failuretenet1[0];
	 
	 Failuretenet1[1]=Failuretenet1[1].replace(Failuretenet1[1].charAt(1),'8');
	 Failuremode1[1]=Failuretenet1[1].replace(Failuretenet1[1].charAt(1),'7');
	 Failuretenet1[1]=Failuretenet1[1].replace(Failuretenet1[1].charAt(2),' ');
	 Failuremode1[1]=Failuremode1[1].replace(Failuremode1[1].charAt(2),' ');
		
	  xpath[0]=Failuretenet1[0]+"td"+Failuretenet1[1]+"]/input";
	  xpath[1]=Failuremode1[0]+"td"+Failuremode1[1]+"]/textarea";
	 return xpath;
}

}
