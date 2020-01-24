package com.LimbicArc.testCases;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.LimbicArc.Pageobjects.LoginPage;

import Testing.LIMBICARCPOC.ExcelUtils;
import Testing.LIMBICARCPOC.FailedTestException;
import Testing.LIMBICARCPOC.TestController;
import Testing.LIMBICARCPOC.WebdriverAPI;
import Testing.LIMBICARCPOC.WebdriverController;

public class SeleniumPOCTest {
	private final WebdriverController wc = new WebdriverController();
	private final WebdriverAPI WebdriverAPI = new WebdriverAPI();
	private final LoginPage loginPage = new LoginPage();
	private final TestController tc = new TestController();
	private final String jobName = getClass().getName();

	private final HashMap<String, String> map = wc.map;
	private WebDriver driver;

	@BeforeClass
	@Parameters({ "browser", "deviceName", "platformVersion", "targetHost", "environment", "protocol", "recipients",
			"sendEmailOnSuccess" })
	public void TestDriver(String browser, String deviceName, String platformVersion, String targetHost,
			String environment, String protocol, String recipients, @Optional("") String sendEmailOnSuccess)
			throws Exception {
		driver = wc.getDriver(browser, targetHost, environment, deviceName, platformVersion, protocol, recipients,
				sendEmailOnSuccess, jobName);

	}

	@AfterClass
	public void tearDown() throws FailedTestException {
		tc.VerificationCheck(map, driver);
	}

	@Test
	public void logintest() throws InterruptedException {

		try {

			WebdriverAPI.getBaseURL(map, driver);
			
			
			WebdriverAPI.browserClearCookies(driver, map);
			//WebdriverAPI.waitForElementToExist(LoginPage.logo(), driver, map);
			WebdriverAPI.validateTitle("Disney.com | The official home for all things Disney", driver, map);
			//WebdriverAPI.waitUntilElementIsPresentAndClick(LoginPage.parksDropDown(), driver, map);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
			
			for(int i=0;i<=2;i++)
			{
				if(!WebdriverAPI.isElementExistsAndVisible(By.id("google_ads_iframe_/21783347309/dcom/homepage_1__"), driver, map))
				{
					break;
				}
				driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				i++;
			}
			WebdriverAPI.waitUntilElementIsPresentAndClick(LoginPage.shoplink(), driver, map);
			

		
			
			
			WebdriverAPI.clickIfElementPresent(LoginPage.signInLink(), driver, map);
			
			
			driver.switchTo().frame(1);
			
			
			WebdriverAPI.clickIfElementPresent(LoginPage.createAccountLink(), driver, map);
			String email = "disneyTest838" + WebdriverAPI.randomAlphabet(3, 3) + "@yopmail.com";
			//WebdriverAPI.sendKeys(LoginPage.emailInput(), email, driver, map);
			//WebdriverAPI.sendKeys(LoginPage.confirmEmailInput(), email, driver, map);
			String password = "Password@123";
			String dateOfBirth ="04/29/1976";
			
			WebdriverAPI.sendKeys(LoginPage.firstNameInput(), "firstName", driver, map);
			
			WebdriverAPI.sendKeys(LoginPage.lastNameInput(), "lastName", driver, map);
			
			WebdriverAPI.sendKeys(LoginPage.emailInput(), email, driver, map);
			WebdriverAPI.sendKeys(LoginPage.passwordInput(), password, driver, map);
			WebdriverAPI.sendKeys(LoginPage.confirmPasswordnput(), password, driver, map);
			WebdriverAPI.sendKeys(LoginPage.dateofBirth(),dateOfBirth, driver, map);
			WebdriverAPI.click(LoginPage.CreateAccount(), driver, map);
			
			
			WebdriverAPI.clickIfElementPresent(LoginPage.ContinueButton(), driver, map);
			for(int i=0;i<=2;i++)
			{
				if(!WebdriverAPI.isElementExistsAndVisible(By.xpath("/html/body/div[9]/svg"), driver, map))
				{
					break;
				}
				driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				i++;
			}
			driver.switchTo().defaultContent();
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("vacation"),driver,map);
			WebdriverAPI.validateTitle("Vacation Shop | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("gifts"),driver,map);
			WebdriverAPI.validateTitle("Gifts & Holiday Gifts | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("toys"),driver,map);
			WebdriverAPI.validateTitle("Disney Toys | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("clothing"),driver,map);
			WebdriverAPI.validateTitle("Clothing | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("accessories"),driver,map);
			WebdriverAPI.validateTitle("Accessories | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("home"),driver,map);
			WebdriverAPI.validateTitle("Home | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("parks"),driver,map);
			WebdriverAPI.validateTitle("Parks | shopDisney", driver, map);
			
			
			//driver.switchTo().defaultContent();
			//System.out.println("By.className(\"user-message\") " +By.className("user-message"));
			WebdriverAPI.clickIfElementPresent(By.className("user-message"), driver, map);
			WebdriverAPI.clickIfElementPresent(By.className("signout"), driver, map);
			
		
			String[] dataToWrite = { email, password };
			ExcelUtils excelUtils = new ExcelUtils();
			excelUtils.writeExcel("TestData.xlsx", "sheet1", dataToWrite);
			((JavascriptExecutor) driver).executeScript("window.open()");
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.close();
			driver.switchTo().window(tabs.get(1));
			driver.get("chrome://settings/clearBrowserData");
			driver.findElement(By.xpath("//settings-ui")).sendKeys(Keys.ENTER);


			ArrayList<Map<String, String>> signInDetails = excelUtils.expectedResultsArrayList("TestData.xlsx", "sheet1");
			WebdriverAPI.getBaseURL(map, driver);
			
			
			WebdriverAPI.browserClearCookies(driver, map);
			//WebdriverAPI.waitForElementToExist(LoginPage.logo(), driver, map);
			WebdriverAPI.validateTitle("Disney.com | The official home for all things Disney", driver, map);
			//WebdriverAPI.waitUntilElementIsPresentAndClick(LoginPage.parksDropDown(), driver, map);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
			
			System.out.println("clicking on shoplink" + LoginPage.shoplink());
			for(int i=0;i<=2;i++)
			{
				if(!WebdriverAPI.isElementExistsAndVisible(By.id("google_ads_iframe_/21783347309/dcom/homepage_1__"), driver, map))
				{
					break;
				}
				driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				i++;
			}
			WebdriverAPI.waitUntilElementIsPresentAndClick(LoginPage.shoplink(), driver, map);

		
			
			
			WebdriverAPI.clickIfElementPresent(LoginPage.signInLink(), driver, map);
			
			
			driver.switchTo().frame(1);
			WebdriverAPI.sendKeys(By.xpath("//*[@id='did-ui-view']/div/section/section/form/section/div[1]/div/label/span[2]/input"), signInDetails.get(0).get("UserName"), driver, map);
			WebdriverAPI.sendKeys(By.xpath("//*[@id='did-ui-view']/div/section/section/form/section/div[2]/div/label/span[2]/input"), signInDetails.get(0).get("Password"), driver, map);
			
			WebdriverAPI.click(By.xpath("//*[@id='did-ui-view']/div/section/section/form/section/div[3]/button"), driver, map);
			for(int i=0;i<=2;i++)
			{
				if(!WebdriverAPI.isElementExistsAndVisible(By.xpath("/html/body/div[9]/svg"), driver, map))
				{
					break;
				}
				driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
				i++;
			}
			driver.switchTo().defaultContent();
			
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("vacation"),driver,map);
			WebdriverAPI.validateTitle("Vacation Shop | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("gifts"),driver,map);
			WebdriverAPI.validateTitle("Gifts & Holiday Gifts | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("toys"),driver,map);
			WebdriverAPI.validateTitle("Disney Toys | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("clothing"),driver,map);
			WebdriverAPI.validateTitle("Clothing | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("accessories"),driver,map);
			WebdriverAPI.validateTitle("Accessories | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("home"),driver,map);
			WebdriverAPI.validateTitle("Home | shopDisney", driver, map);
			WebdriverAPI.waitUntilElementIsPresentAndClick(By.id("parks"),driver,map);
			WebdriverAPI.validateTitle("Parks | shopDisney", driver, map);
		} catch (Exception | AssertionError e) {
			map.put("exceptionString", e.toString());
			tc.failedStep(map);
		}
	}
}
