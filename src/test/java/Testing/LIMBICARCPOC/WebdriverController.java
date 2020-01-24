package Testing.LIMBICARCPOC;

import java.util.HashMap;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;

public class WebdriverController {
	WebDriver driver;
	static String receiverOfEmail = "";
	static String targethostOfTest = "";
	static String environmentOfTest = "";
	static String sendEmailOnSuccess = "false";
	static String baseEnvironment = "";
	public HashMap<String, String> map = new HashMap<String, String>();
	private String seleniumVersion = "3.8.1";
	private final String executablesDir = System.getProperty("user.dir") + "\\ExcutableDriverFiles";
	private final String seleniumVersionFirefox = "3.4.0"; /*
															 * NOTE: Firefox has issue with switchTo window handle when
										Z					 * version is greater than 3.4.0
															 */
	private final String seleniumVersionIE = "2.53.1";

	public WebDriver getDriver(String browser, String targetHost, String environment, String deviceName,
			String platformVersion, String protocol, String recipients, String sendEmailOnSuccessValue, String className)
			throws Exception {
		TestController tc = new TestController();
		DesiredCapabilities caps;
		final long startTime = System.currentTimeMillis();
		// Counts tests
		synchronized (LogCategorizer.class) {
			LogCategorizer.totalTests++;
		}
		// Stores parameters into map
		map.put("browser", browser);
		map.put("targetHost", targetHost);
		map.put("environment", environment);
		map.put("deviceName", deviceName);
		map.put("platformVersion", platformVersion);
		map.put("protocol", protocol);
		map.put("recipients", recipients);
		map.put("sendEmailOnSuccess",sendEmailOnSuccessValue);
		tc.PreTest(map);
		// Re-stores values from map so that otherParams can overwrite TestNG
		// parameters
		browser = map.get("browser");
		targethostOfTest = map.get("targetHost");
		environmentOfTest = map.get("environment");
		deviceName = map.get("deviceName");
		platformVersion = map.get("platformVersion");
		protocol = map.get("protocol");
		receiverOfEmail = map.get("recipients");

		if (map.get("sendEmailOnSuccess") != null && map.get("sendEmailOnSuccess").equals("true"))
			sendEmailOnSuccess = "true";

		String[] splitClass = className.split("\\.");
		String currentScriptName = splitClass[splitClass.length - 1];
		map.put("currentScriptName", currentScriptName);
		String jobName = className + " : " + deviceName + "_" + browser + "_v" + platformVersion;
		map.put("jobName", jobName);
		// Sets capabilities by browser
		if (browser.equalsIgnoreCase("firefox")) {
			caps = DesiredCapabilities.firefox();
			caps.setCapability("platform", deviceName);
			caps.setCapability("version", platformVersion);
			caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
			caps.setBrowserName("firefox");
			seleniumVersion = seleniumVersionFirefox;
			driver=new FirefoxDriver(caps);

		} else if (browser.equalsIgnoreCase("chrome")) {
			//System.setProperty("webdriver.chrome.driver", executablesDir + "\\chromedriver.exe");
	
			ChromeOptions options = new ChromeOptions();
			// options.addArguments("--disable-infobars");
			caps = DesiredCapabilities.chrome();
			caps.setCapability(ChromeOptions.CAPABILITY, options);
			// caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			//caps.setCapability("platform", deviceName);
			//caps.setCapability("version", platformVersion);
			caps.setBrowserName("chrome");
			driver = new ChromeDriver(caps);
		} else if (browser.equalsIgnoreCase("iexplore")) {
			System.setProperty("webdriver.chrome.driver", executablesDir + "\\IEDriverServer.exe");
			caps = DesiredCapabilities.internetExplorer();
			caps.setCapability("platform", deviceName);
			caps.setCapability("version", platformVersion);
			caps.setBrowserName("internet explorer");
			caps.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "eager");
			caps.setCapability("iedriverVersion", "3.4.0");
			caps.setCapability("avoidProxy", true);
			seleniumVersion = seleniumVersionIE;
			driver=new InternetExplorerDriver(caps);
		} else if (browser.equalsIgnoreCase("safari")) {
			caps = DesiredCapabilities.safari();
			caps.setCapability("platform", deviceName);
			caps.setCapability("version", platformVersion);
			caps.setBrowserName("safari");
			caps.setCapability("safariAllowPopups", true);
			driver = new SafariDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			caps = DesiredCapabilities.edge();
			caps.setCapability("platform", deviceName);
			caps.setCapability("version", platformVersion);
			caps.setBrowserName("microsoftedge");
			driver= new EdgeDriver();
		} else {
			tc.writeLogToFile("Invalid Browser. Please provider following browsers: chrome,firefox,iexplore,safari,edge" + startTime, map);
			throw new Exception("Invalid Browser!");
		}

		// Common capabilities
		caps.setCapability("name", jobName);
		caps.setCapability("maxDuration", 3600);
		caps.setCapability("idleTimeout", 300);
		caps.setCapability("seleniumVersion", seleniumVersion);

		String sessionId = String.valueOf(((ChromeDriver) driver).getSessionId());

		String jobNameLogs = deviceName + "_" + browser + "_v" + platformVersion + " %%% " + className + "::"
				+ sessionId + " %%% ";

		map.put("jobNameLogs", jobNameLogs);
		tc.writeLogToFile("startTimeOfTest::" + startTime, map);

		return driver;
	}
}
