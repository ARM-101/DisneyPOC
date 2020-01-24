package Testing.LIMBICARCPOC;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

//import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

public class TestController {
	WebDriver driver;
	String checkExceptions = null;

	public void PreTest(HashMap<String, String> map) {
		map.put("totalFailCounter", "0");
		// Stores PrimaryURL in map
		String primaryURL = map.get("targetHost");
		if (primaryURL.contains("|")) {
			String[] primaryURLSplit = primaryURL.split("\\|");
			primaryURL = primaryURLSplit[0];
		}
		map.put("primaryURL", primaryURL);

	}

	public void failedStep(HashMap<String, String> map) {
		/*
		 * Runs at the end of a verification fail to increment fail counter and store
		 * VerificationError in the map. This is used to generate total fails for the
		 * report.
		 */
		map.put("VerificationError", "FAIL");
		String currentFailCount = map.get("totalFailCounter");
		int currentFailCountInt = Integer.parseInt(currentFailCount); // Convert fail count to int
		currentFailCountInt = currentFailCountInt + 1; // Increment fail count
		currentFailCount = Integer.toString(currentFailCountInt); // Convert fail count back to string
		map.put("totalFailCounter", currentFailCount);
	}

	public void VerificationCheck(HashMap<String, String> map) throws FailedTestException {
		WebDriver driver = null;
		VerificationCheck(map, driver);
	}

	public void VerificationCheck(HashMap<String, String> map, WebDriver driver) throws FailedTestException {

		writeLogToFile("===============================================", map);
		final long endTime = System.currentTimeMillis();
		writeLogToFile("endTimeOfTest::" + endTime, map);
		if (map.get("totalFailCounter").equals("0")) {
			synchronized (LogCategorizer.class) {
				LogCategorizer.totalPassedTests++;
			}

			writeLogToFile("TEST COMPLETED!", map);
			writeLogToFile("-----------------------------------------------", map);
			try {
				driver.quit();
			} catch (Exception e) {
			}
		} else {
			writeLogToFile("TEST FAILED - Details below:", map);
			writeLogToFile("Total fails: " + map.get("totalFailCounter"), map);
			checkExceptions = map.get("exceptionString");

			writeLogToFile("-----------------------------------------------", map);
			if (checkExceptions != null) { // Checks if exception in map
				writeLogToFile("Exception thrown:", map);
				String exceptionText = map.get("exceptionString");
				writeLogToFile(exceptionText, map);
				writeLogToFile("-----------------------------------------------", map);
				try {
					driver.quit();
				} catch (Exception e) {
				}
				throw new FailedTestException(exceptionText);
			} else {
				String verificationErrorText = "Verification test(s) failed in the script, causing this script to FAIL!";
				writeLogToFile(verificationErrorText, map);
				writeLogToFile("-----------------------------------------------", map);
				try {
					driver.quit();
				} catch (Exception e) {
				}
				throw new FailedTestException(verificationErrorText);
			}
		}
	}

	public void writeLogToFile(String message, HashMap<String, String> map) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		message = message.trim();
		if (!message.equals(null) && !message.equals("")) {
			System.out.println(message);
			Calendar calendar = Calendar.getInstance();
			Long milliSeconds = calendar.getTimeInMillis();
			Date dte = new Date(milliSeconds);
			String[] splitArray = message.split("\n");
			for (int i = 0; i < splitArray.length; i++) {
				if (splitArray[i].equals(null) || splitArray[i].equals(""))
					continue;
				synchronized (LogCategorizer.class) {
					LogCategorizer.pw.println(map.get("jobNameLogs") + " " + LogCategorizer.logCounter + " %%%  ("
							+ sdfDate.format(dte) + ") - " + splitArray[i] + "");
					LogCategorizer.logCounter++;
				}
			}
		}
	}
}
