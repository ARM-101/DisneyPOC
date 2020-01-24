package Testing.LIMBICARCPOC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.itextpdf.text.Document;

/*
 * LogCategorizer.java does following:
 * - categorizes the logs printed on console into separate classes of different devices
 * - prints the categorized logs
 * - creates a pdf of categorized logs with test suite details, list of failed devices and results
 * - sends an email with this pdf attached
 */

public class LogCategorizer {
	private static String currentTestFile = "";
	private static String currentSuite = "";
	private static final String currentUser = System.getProperty("user.name");	
	private static HashMap<String, HashMap<String, TreeMap<Integer, String>>> logMap = new HashMap<String, HashMap<String, TreeMap<Integer, String>>>();
	private static List<String> logSections = new ArrayList<String>();
	public static String suiteIdentifier;
	public static PrintWriter pw;
	public static HashMap<String, String> TESTOBJECT_TEST_URL = new HashMap<String, String>();
	public static int logCounter = 1;
	public static int totalTests = 0;
	public static int totalPassedTests = 0;
	private static int percentagePassed;
	private static String suiteStatus = "FAILED";
	public static Document document = new Document();

	@BeforeSuite
	public void beforeSuite(ITestContext t) {
		LogCategorizer.document = new Document();
		currentTestFile = "" + t.getCurrentXmlTest().getSuite().getFileName();
		currentSuite = "" + t.getCurrentXmlTest().getSuite().getName();
		Calendar calendar = Calendar.getInstance();
		suiteIdentifier = (calendar.getTime().toString().replace(" ", "") + calendar.getTimeInMillis()).replace(":", "");
		try {
			FileWriter fw = new FileWriter("" + LogCategorizer.suiteIdentifier + ".txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
		} catch (IOException e) {
			// do nothing with the exception
		}
	}

	@AfterSuite
	public void afterSuite(ITestContext t) throws IOException {
		pw.close();
		readLogFile();
		printCategorizedLogs();
		
	}

	
	private void deleteFile(String string) {
		File file = new File(string);
		if (file.delete())
			System.out.println(string + " is deleted");	
	}

	private static String normalNewLogLine(String newLog, String contentOfLog) {
		newLog = newLog + "\t\t" + contentOfLog + "\n";
		return newLog;
	}

	private static String removeTimeStampFromContentOfLog(String contentOfLog) {
		String[] contentOfLogSplit = contentOfLog.split("\\) - ");
		contentOfLog = contentOfLogSplit[1];
		return contentOfLog;
	}
	
	private static void printCategorizedLogs() {
		int failedTestsCount = 0;
		int passedTestsCount = 0;
		long startTime = 0;
		long endTime = 0;
		String newLog = "";
		String failedTests = "", passedTests="";	
		String emailContent = "";
		String headerString = "\nTestNG file = " + currentTestFile + "\nTargetHost = " + WebdriverController.targethostOfTest + "\nEnvironment = " + WebdriverController.environmentOfTest + "\nSuite = " + currentSuite + "\nUser = " + currentUser + "\n \n";
		System.out.println("\n\n_______________________________New Logging___________________________________");
		try {
			//for all devices
			String[] classOfTestdeviceArray = {};
			String classOfTestdeviceName = "";
			String jobId = "";
			String key = "";
			int testLine;
			boolean summaryPortionEnable = false;
			boolean testEnded = true;
			String messageMapString = "";
			String failContent = "", failedTestDetails = "", presentation = "", val = "";
			boolean failureDetailsEnable = false;
			String contentOfLog = "";
			String trimmed = "";
			String[] timeSplit = {};
			String testFailedString = "";
			String unexpectedQuitString = "";
			for (String testDevice : logMap.keySet()) {
				HashMap<String, TreeMap<Integer, String>> hm = logMap.get(testDevice);
				//for all classes
				for (String classOfTestdevice : hm.keySet()) {
					if (testDevice.contains("API Testing"))
						newLog = newLog+ "Script: API Testing \n";
					else
						newLog = newLog + "Device Name: " + testDevice + "\n";
					classOfTestdeviceArray = classOfTestdevice.split("::");
					classOfTestdeviceName = classOfTestdeviceArray[0];
					newLog = newLog + "Class: " + classOfTestdeviceName + "\n";
					jobId = classOfTestdeviceArray[1];
					if (!testDevice.contains("API Testing")) {
						if (testDevice.contains("real")) {
							newLog = newLog +"Test Objects job:     " + TESTOBJECT_TEST_URL.get(testDevice) ;	
						} 
					}	
					newLog = newLog + "\n";
					TreeMap<Integer, String> messageMap = hm.get(classOfTestdevice);
					testLine = 1;
					summaryPortionEnable = false;
					testEnded = true;
					messageMapString = messageMap.toString();
					failContent = failedTestDetails = presentation = val = "";
					if (!messageMapString.contains("endTimeOfTest")) { //checks if endTimeOfTest is not found to see if script ended prematurely
						failedTestsCount++;
						failedTests = failedTests + failedTestsCount + ". " + testDevice + " - " + classOfTestdeviceName + "\n";
						endTime = Long.parseLong("" + System.currentTimeMillis()); //if ended prematurely then device/class is recorded and endTime added
						testEnded = false;
						if (testDevice.contains("real") || testDevice.contains("API Testing"))
						    failedTestDetails = "" + "Test Fail " + failedTestsCount + ":\n" + "Device Name: " + testDevice + "\n" + "Class: " + classOfTestdeviceName + "\n" ;
						else
						    failedTestDetails = "" + "Test Fail " + failedTestsCount + ":\n" + "Device Name: " + testDevice + "\n" + "Class: " + classOfTestdeviceName + "\n" + "Sauce Labs job: " + "https://saucelabs.com/jobs/" + jobId + "?auth=" + keyMessageToHMACMD5(jobId, key) + "\n"; //failedTestDetails are also retrieved
					}
					failureDetailsEnable = false;
					//for all logs
					for (Integer timeOfMessage : messageMap.keySet()) {
						contentOfLog = messageMap.get(timeOfMessage);
						
						if (contentOfLog.contains("TEST FAILED - Details below:")) {
							failedTestsCount++;
							failedTests = failedTests + failedTestsCount + ". " + testDevice + " - " + classOfTestdeviceName + "\n";
							failedTestDetails = "" + "Test Fail " + failedTestsCount + ":\n" + "Device Name: " + testDevice + "\n" + "Class: " + classOfTestdeviceName + "\n" ;
							
							
						}
						if (contentOfLog.contains("TEST COMPLETED!")) {
							passedTestsCount++;
							passedTests = passedTests + passedTestsCount +". " + testDevice + " - " + classOfTestdeviceName + "\n";
						}
						if (!contentOfLog.equals("") && !contentOfLog.isEmpty()) {
							trimmed = "";
							if (contentOfLog.contains("TEST FAILED - Details below:")) {
								failureDetailsEnable = true;
							    failContent = failContent + "TEST FAILED - Details below:\n";
							} else if (contentOfLog.contains("TEST FAILED -") && !contentOfLog.contains("TEST FAILED - Expected column values:")) { //Removed compare column fails from email
									failContent = failContent + testLine + " - " + contentOfLog + "\n";
							} else if (failureDetailsEnable) {
								int messagePartIndex = contentOfLog.indexOf(")") + 4;
								if (messagePartIndex <= contentOfLog.length())
									 trimmed = contentOfLog.substring(messagePartIndex);
								failContent = failContent + trimmed + "\n";
							}
							if (contentOfLog.contains("startTimeOfTest:")) {
								timeSplit = contentOfLog.split("::");
								startTime = Long.parseLong("" + timeSplit[1]);
								newLog = newLog+ "\t\t" + "Script Start time :  " + getCompleteDate(startTime) + "\n";
							} else if (contentOfLog.contains("FeatureName: ")) {
								contentOfLog = removeTimeStampFromContentOfLog(contentOfLog);
								newLog = newLog + "\n\t\t" + "===============================================\n";
								newLog = normalNewLogLine(newLog, contentOfLog);
								testLine = 1;
							} else if (contentOfLog.contains("SauceOnDemandSessionID=") || contentOfLog.contains("Sauce Connect tunnel-identifier set as:") || contentOfLog.contains("Total Features Ran: ")) {
								contentOfLog = removeTimeStampFromContentOfLog(contentOfLog);
								newLog = normalNewLogLine(newLog, contentOfLog);
							} else if (contentOfLog.contains("===============================================")) {
								newLog = newLog + "\t\t" + "===============================================\n";
							} else if (contentOfLog.contains("endTimeOfTest:")) {
								timeSplit = contentOfLog.split("::");
								endTime = Long.parseLong("" + timeSplit[1]);
								newLog = newLog + "\t\t" + "Script End time :  " + getCompleteDate(endTime) + "\n";
								summaryPortionEnable = true;
							} else if (contentOfLog.contains("TEST COMPLETED!")) {
								newLog = newLog + "\t\t" + "TEST COMPLETED AND PASSED!" + "\n";
							} else if (contentOfLog.contains("Exception thrown:")) {
								newLog = newLog + "\t\t" + "EXCEPTION THROWN: ";
							} else {
								if (summaryPortionEnable == true) {
									int messagePartIndex=contentOfLog.indexOf(")") + 4;
									if(messagePartIndex<=contentOfLog.length())
										contentOfLog = contentOfLog.substring(messagePartIndex);
									if (contentOfLog.contains("Exception: "))
										newLog = newLog + contentOfLog + "\n";
									else
										newLog = normalNewLogLine(newLog, contentOfLog);
								} else {
									newLog = newLog + "\t\t" + testLine + " - " + contentOfLog + "\n";
									testLine++;
								}
							}
						}
					}
					if (testEnded == false) { //if script ended prematurely 'TEST FAILED details' is added manually to the strings used in logs and email since it is not caught by TestController
						testFailedString = "TEST FAILED - Details below:";
						unexpectedQuitString = "Exception thrown: Test quit unexpectedly and did not complete. Possible cause may be due to test not seeing a new command for some time and timing out in Sauce Labs.";
						newLog = newLog + "\t\t" + testFailedString + "\n";
						newLog = newLog + "\t\t" + unexpectedQuitString + "\n";
						failContent = failContent + testFailedString + "\n" + unexpectedQuitString + "\n";
					}
					//for all logs of a class
					if (!failedTestDetails.equals("") && !failContent.equals("")) {
						emailContent = emailContent +
								   "<font color=\"blue\">" + failedTestDetails + "</font>" +
								   "<font color=\"purple\">" + presentation + "</font>" +
								   "<font color=\"red\">" + failContent + "</font>" +
								   "<hr>";
					}
					newLog = newLog + "\t\t" + "Execution time of test :  " + executionTime(startTime, endTime) + "\n";
					newLog = newLog + "_________________________________End of Class_______________________________\n";
				}
			}
	    } catch(Exception e) {
			System.out.println("There is exception in framing new log\n" + e);
	    	newLog = "";
		}
		String resultString = "\nResult: \nTotal Tests run: " + totalTests + "\n" + "Total Tests passed: " + totalPassedTests + "\n" + "Total Tests failed: " + (totalTests - totalPassedTests) + "\n";
		double ratioPassed = ((double) totalPassedTests / (double) totalTests);
		percentagePassed = (int) (ratioPassed * 100);
		System.out.println(headerString);
		System.out.println(newLog);
		System.out.println(resultString);
		if (!failedTests.equals(""))
			failedTests = "The following scripts failed:\n" + failedTests;
		else {
			failedTests = "No scripts failed!\n";
			suiteStatus = "PASSED";
		}
		if (!passedTests.equals(""))
			passedTests = "The following scripts passed:\n" + passedTests;
		else
			passedTests = "No scripts passed!\n";
		System.out.println(failedTests);
		System.out.println("EmailContent: ");
		//System.out.println(emailContent);
		logSections.add(0, headerString);
		logSections.add(1, failedTests);
		logSections.add(2, passedTests);
		logSections.add(3, newLog);
		logSections.add(4, resultString);
		logSections.add(5, emailContent);
	}

	private static double executionTime(long startTime, long endTime) {
		long duration = endTime - startTime;
		double n = (double) duration / 1000.0;
		return n;
	}

	private static String getCompleteDate(long milliseconds) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("EEEE yyyy-MM-dd HH:mm:ss:SSS");
		Date dte = new Date(milliseconds);
		return "" + sdfDate.format(dte);  
	}

	private static void storeLogsInLogMap(String lineOfLogFile) {
		if (lineOfLogFile.equals("") || lineOfLogFile.isEmpty())
			return;
		if (lineOfLogFile.contains("%%%")) {
			String[] contentArray = lineOfLogFile.split("%%%");
			if (contentArray.length == 4) {
				String deviceName = contentArray[0].trim();
				String className = contentArray[1].trim();
				String timestamp = contentArray[2].trim();
				String message = contentArray[3].trim();
				Integer timestampCounter = Integer.parseInt(timestamp + "");
				if (logMap.containsKey(deviceName)) {
					HashMap<String, TreeMap<Integer, String>> hm = logMap.get(deviceName);
					if (hm.containsKey(className)) {
						TreeMap<Integer, String> tm = hm.get(className);
						tm.put(timestampCounter, message);
					} else {
						TreeMap<Integer, String> tm = new TreeMap<Integer, String>();
						tm.put(timestampCounter, message);
						hm.put(className, tm);
					}
				} else {
					HashMap<String, TreeMap<Integer, String>> hm = new HashMap<String, TreeMap<Integer, String>>();
					TreeMap<Integer, String> tm = new TreeMap<Integer, String>();
					tm.put(timestampCounter, message);
					hm.put(className, tm);
					logMap.put(deviceName, hm);
				}
		    } else
			System.out.println("log separator %%% present less than 4 times");
		} else
		  System.out.println("log separator %%% not present");
	}

	private static void readLogFile() throws IOException {
		BufferedReader br;
		FileInputStream fis = null;
		File logfileToSort = new File("" + suiteIdentifier + ".txt");
		try {
			fis = new FileInputStream(logfileToSort);
			br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while ((line = br.readLine()) != null) {
				storeLogsInLogMap(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (fis != null) {
            	fis.close();
            }
        }
	}

	private static String keyMessageToHMACMD5(String message, String key) {
		/*
		 * Returns MD5 encoded string
		 */
        String encodedString = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(keySpec);
            byte[] bytes = mac.doFinal(message.getBytes());
            StringBuffer hash = new StringBuffer();
            String hex = "";
            for (int i=0; i<bytes.length; i++) {
                hex = Integer.toHexString(0xFF &  bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            encodedString = hash.toString();
        } catch (InvalidKeyException e) {
			// do nothing with the exception
        } catch (NoSuchAlgorithmException e) {
			// do nothing with the exception
        }
        return encodedString;
    }
}
