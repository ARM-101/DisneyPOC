package com.LimbicArc.testCases;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.LimbicArc.Pageobjects.LoginPage;

import Testing.LIMBICARCPOC.FailedTestException;
import Testing.LIMBICARCPOC.TestController;
import Testing.LIMBICARCPOC.WebdriverAPI;
import Testing.LIMBICARCPOC.WebdriverController;

public class TC_LoginPage_001 {

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
			loginPage.loginSteps(driver, map);
			WebdriverAPI.waitForElementToExist(LoginPage.logo(), driver, map);
			
			WebdriverAPI.validateTitle("Limbic Arc - Customer Portal", driver, map);
			
			

		} catch (Exception | AssertionError e) {
			map.put("exceptionString", e.toString());
			tc.failedStep(map);
		}
	}
}
