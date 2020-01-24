package Testing.LIMBICARCPOC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.client.ClientProtocolException;
import org.jasypt.properties.EncryptableProperties;
import org.jasypt.util.text.BasicTextEncryptor;
import org.json.JSONException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.appium.java_client.AppiumDriver;

public class WebdriverAPI {

	private TestController tc = new TestController();
	private WebElement element = null;


	// ====================================================================================================
	// ====================================================================================================
	// Methods
	public WebElement findElementBy(By locator, WebDriver driver, HashMap<String, String> map) {

		return driver.findElement(locator);

	}

	public void getBaseURL(HashMap<String, String> map, WebDriver driver) {
		/*
		 * Returns full URL with protocol, domain and URI.
		 */
		
		String baseURL = map.get("protocol") + "://" + map.get("targetHost");
		driver.navigate().to(baseURL);
		driver.manage().window().maximize();
		waitForPageToLoad(20, driver);

	}

	public boolean isElementExistsAndVisible(By locator, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Returns true or false if element exists and is visible.
		 */
		try {
			waitForElementVisible(locator, driver, map);
			return true;
		} catch (NoSuchElementException | TimeoutException e) { // If element
																// does not
																// exist or
																// element not
																// visible,
																// return false
			return false;
		}
	}

	public String randomAlphabet(int max, int min) {
		/*
		 * Generates random letters string
		 */
		Random randomRange = new Random();
		Random randomChar = new Random();
		String randomString = "";
		int randomRangeInt = randomRange.nextInt((max - min) + 1) + min;
		char c;
		while (randomRangeInt > 0) {
			c = (char) (randomChar.nextInt(26) + 'a');
			randomString = randomString + c;
			randomRangeInt = randomRangeInt - 1;
		}
		return randomString;
	}

	public String randomInt(int min, int max) {
		/*
		 * Generates random int between min and max
		 */
		Random rnd = new Random();
		int randomInt = min + rnd.nextInt(max - min);
		String randomString = "" + randomInt + "";
		return randomString;
	}

	public void sleepInterval(int milliseconds, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Method to sleep. Waits for tagname to prevent Sauce Labs timeout.
		 */
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		if (milliseconds >= 60000) {
			try {
				waitForElementToExist(By.tagName("body"), driver, map);
			} catch (Exception e) {
			}
		}
	}

	public void browserClearCookies(WebDriver driver, HashMap<String, String> map) {
		/*
		 * Deletes all cookies
		 */
		if (!map.get("browser").contains("edge")) {
			tc.writeLogToFile("Deleting all cookies from current domain.", map);
			Set<Cookie> cookies = driver.manage().getCookies();
			driver.manage().deleteAllCookies();
			while (!cookies.isEmpty()) {
				cookies.clear();
			}
		}
	}

	public void click(By locator, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Clicks on locator. Uses Action chains to click for most cases, but uses click
		 * method for iOS and older Androids.
		 */
		waitForElementToExist(locator, driver, map);
		findElementBy(locator, driver, map);
		waitForElementVisible(locator, driver, map);
		if (map.get("browser").equalsIgnoreCase("edge")) { // Issue with Edge
															// (15) causing
															// script to not
															// click properly
															// unless there is a
															// delay
			clickNotVisible(locator, driver, map);
		} else {
			clickNoWait(locator, driver, map);
		}
	}

	public void click(By locator, int waitTime, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Clicks on locator. Waits for page to load for length of seconds defined by
		 * waitTime
		 */
		click(locator, driver, map);
		waitForPageToLoad(waitTime, driver);
	}

	public void click(WebElement element, WebDriver driver, HashMap<String, String> map) {
		tc.writeLogToFile("Clicking on: " + element, map);
		findWebElement(element, driver, map);
		element.click();
	}

	public void clickIfElementPresent(By locator, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Runs clickNoWait keyword. If element does not exist, then it continues
		 * without failing.
		 */
		waitForPageToLoad(60, driver);
		boolean elementExistsAndVisible = isElementExistsAndVisible(locator, driver, map);
		if (elementExistsAndVisible == true) {
			try {
				clickNoWait(locator, driver, map);
			} catch (WebDriverException e) {
				tc.writeLogToFile("Skipping clicking because element '" + locator + "' is not present!", map);
			}
		} else {
			tc.writeLogToFile("Skipping clicking because element '" + locator + "' is not present!", map);
		}
	}

	

	public void clickNotVisible(By locator, WebDriver driver, HashMap<String, String> map) {
		/*
		 * After finding the target element, clicks that element regardless of it being
		 * visible or not.
		 */
		tc.writeLogToFile("Clicking on: " + locator, map);
		element = findElementBy(locator, driver, map);
		if (map.get("browser").equalsIgnoreCase("edge")) { // Issue with Edge
															// (15) causing
															// script to not
															// click properly
															// unless there is a
															// delay
			sleep(500, driver, map);
			try {
				element.click();
			} catch (ElementNotVisibleException e) {
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].click();", element);
			}
		} else {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			jse.executeScript("arguments[0].click();", element);
		}
		sleep(5000, driver, map);
	}

	public void clickNoWait(By locator, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Clicks on element without waiting for it to become visible. Uses Action
		 * chains to click for most cases, but uses click method for iOS and older
		 * Androids.
		 */
		tc.writeLogToFile("Clicking on: " + locator, map);
		element = findElementBy(locator, driver, map);
		if (map.get("browser").equalsIgnoreCase("android") && !map.get("platformVersion").equals("4.0")
				&& !map.get("platformVersion").equals("4.1") && !map.get("platformVersion").equals("4.2")) {
			// Actions (Actions needed for Android 4.3+; Not supported by
			// Selendroid (Android versions 4.0-4.2); Issues when combined with
			// js scrollintoview on iOS)
			Actions actions = new Actions(driver);
			actions.click(element);
			actions.perform();
		}
		// else if (map.get("browser").equalsIgnoreCase("ios") &&
		// (map.get("platformVersion").equals("9.0") ||
		// map.get("platformVersion").equals("9.1") ||
		// map.get("platformVersion").equals("9.2"))) {
		// //iOS 9+ sometimes takes longer to complete loading next page when
		// click or submit are used
		// JavascriptExecutor jse = (JavascriptExecutor)driver;
		// jse.executeScript("arguments[0].click();", element);
		// }
		else {
			// Normal (has issues in Android about clicking other element even
			// though the elements don't overlap)
			if (map.get("browser").equalsIgnoreCase("edge")) { // Issue with
																// Edge (15)
																// causing
																// script to not
																// click
																// properly
																// unless there
																// is a delay
				sleep(500, driver, map);
			}
			element.click();
			// not yet implemented exception (tried for ios)
			// MobileDriver mobdriver = (MobileDriver) driver;
			// TouchAction tAction = new TouchAction(mobdriver);
			// tAction.tap(element);
			// tAction.perform();
		}
	}

	public void elementClickBIfElementANotPresent(By locatorA, By locatorB, WebDriver driver,
			HashMap<String, String> map) {
		/*
		 * Checks if element A is visible. If it is visible, then it does nothing. If it
		 * is not visible, then it clicks on element B.
		 */
		boolean iselementVisible;
		iselementVisible = isElementExistsAndVisible(locatorA, driver, map);
		if (iselementVisible == true) {
			tc.writeLogToFile(locatorA + " is visible! Will not click on Element " + locatorB, map);
		} else {
			tc.writeLogToFile(locatorA + " is not visible! Will click on Element " + locatorB, map);
			click(locatorB, driver, map);
		}
	}

	public void elementClickBIfElementAPresent(By locatorA, By locatorB, WebDriver driver,
			HashMap<String, String> map) {
		/*
		 * Checks if element A is visible. If it is not visible, then it does nothing.
		 * If it is visible, then it clicks on element B.
		 */
		boolean iselementVisible;
		iselementVisible = isElementExistsAndVisible(locatorA, driver, map);
		if (iselementVisible == true) {
			tc.writeLogToFile(locatorA + " is visible! Will  click on Element " + locatorB, map);
			click(locatorB, driver, map);
		} else {
			tc.writeLogToFile(locatorA + " is not visible! Will not click on Element " + locatorB, map);
		}
	}

	public void findWebElement(WebElement element, WebDriver driver, HashMap<String, String> map) {
		if ((map.get("browser").equalsIgnoreCase("ios")) || (map.get("browser").equalsIgnoreCase("iexplore"))
				|| (map.get("browser").equalsIgnoreCase("edge"))
				|| ((map.get("browser").equalsIgnoreCase("android"))
						&& ((map.get("platformVersion").equalsIgnoreCase("4.4"))
								|| (map.get("platformVersion").equalsIgnoreCase("5.1"))))) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);
		} else if ((map.get("browser").equalsIgnoreCase("chrome"))
				|| (map.get("browser").equalsIgnoreCase("android"))) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
		}
	}

	public void sendKeys(By locator, String onDemandValue, WebDriver driver, HashMap<String, String> map) {
		waitForElementVisible(locator, driver, map);
		element = findElementBy(locator, driver, map);
		element.sendKeys(onDemandValue);
	}

	

	public void sleep(int milliseconds, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Sleeps for user-defined amount of milliseconds. Breaks up time into 60 second
		 * intervals if greater than 60 seconds to prevent 90 second timeout from Sauce
		 * Labs
		 */
		if (milliseconds > 60000) {
			int min = milliseconds / 60000;
			int remaining = milliseconds - (min * 60000);
			for (int i = 0; i < min; i++) {
				sleepInterval(60000, driver, map);
			}
			if (remaining != 0) {
				sleepInterval(remaining, driver, map);
			}
		} else {
			sleepInterval(milliseconds, driver, map);
		}
	}

	public void validateElementExistence(By locator, boolean expectedValue, WebDriver driver,
			HashMap<String, String> map) {
		/*
		 * Validates if element exists.
		 */
		boolean elementExists;
		try {
			waitForElementToExist(locator, driver, map);
			elementExists = true;
		} catch (TimeoutException e) {
			elementExists = false;
		}
		if (elementExists == expectedValue) {
			tc.writeLogToFile("TEST PASSED - element existence is '" + elementExists + "' and you were expecting '"
					+ expectedValue + "' for element: " + locator, map);
		} else {
			tc.writeLogToFile("TEST FAILED - element existence is '" + elementExists + "' but you were expecting '"
					+ expectedValue + "' for element: " + locator, map);
			tc.failedStep(map);
		}

	}

	public void validateElementVisibility(By locator, boolean expectedValue, WebDriver driver,
			HashMap<String, String> map) {
		/*
		 * Validates if element is visible.
		 */
		boolean elementExistsAndVisible = isElementExistsAndVisible(locator, driver, map);
		if (elementExistsAndVisible == expectedValue) {
			tc.writeLogToFile("TEST PASSED - visibility is '" + elementExistsAndVisible + "' and you were expecting '"
					+ expectedValue + "' for element: " + locator, map);
		} else {
			tc.writeLogToFile("TEST FAILED - visibility is '" + elementExistsAndVisible + "' but you were expecting '"
					+ expectedValue + "' for element: " + locator, map);
			tc.failedStep(map);
		}
	}

	

	public void validateTitle(String string, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Verifies actual title matches expected title.
		 */
		String actualTitle;
		waitForPageToLoad(30, driver);
		try {
			actualTitle = driver.getTitle();
			if (actualTitle.equals(string)) {
				tc.writeLogToFile("TEST PASSED - page title is: " + string, map);
			} else {
				tc.writeLogToFile("TEST FAILED - page title found is '" + actualTitle + "' but you were expecting '"
						+ string + "'!", map);
				tc.failedStep(map);
			}
		} catch (WebDriverException e) {
			if (map.get("endPoint").equalsIgnoreCase("TESTOBJECT"))
				tc.writeLogToFile(
						"Could not verify the title!!! Issues with verification on IOS devices in Test Object", map);
			else {
				tc.writeLogToFile("TEST FAILED - Exception while verifying title of the new window!!!", map);
				tc.failedStep(map);
			}
		}
	}

	public void waitForElementToExist(By locator, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Runs waitForElementToExist with default wait time of 10 seconds.
		 */
		waitForElementToExist(locator, 60, driver, map);
	}

	public void waitForElementToExist(By locator, int waitTime, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Using the WebDriverWait API, will wait expected 'waitTime' seconds until the
		 * target element is found on the page. After wait time expires, if element does
		 * not exist then WebDriverWait will throw a TimeoutException.
		 */
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public void waitForElementVisible(By locator, WebDriver driver, HashMap<String, String> map) {
		/*
		 * Same as waitForElementVisible with default wait time of 10 seconds.
		 */
		waitForElementToExist(locator, driver, map);
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void waitForPageToLoad(int waitTime, WebDriver driver) {
		/*
		 * Waits until javascript "document.readyState" returns "compelete".
		 */
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, waitTime);
		wait.until(pageLoadCondition);
	}

	public void waitUntilElementIsPresentAndClick(By locator, WebDriver driver, HashMap<String, String> map) {

		clickIfElementPresent(locator, driver, map);

	}

}