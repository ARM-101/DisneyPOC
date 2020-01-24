package com.LimbicArc.Pageobjects;

import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import Testing.LIMBICARCPOC.WebdriverAPI;


public class LoginPage {

	private final WebdriverAPI WebdriverAPI = new WebdriverAPI();
	public static String email = "limbicarc@hotmail.com";
	public static String password = "Password1!";

	public static By emailTextBox() {
		return By.name("email");
	}

	public static By passwordTextBox() {
		return By.name("password");
	}

	public static By submitBtn() {
		return By.name("submit");
	}
	
	public static By logo() {
		return By.className("goc-wide-link");
	}
	//disney.com-->shop_link
	public static By shoplink() {
		return By.xpath("//li[@class='goc-desktop bar-dropdown'][2]/a");
	}
	//disney.com-->shop_link-->Signup_link
	public static By signInLink() {
		//return By.className("signIn");
		return By.xpath("/html/body/div[1]/header/nav/div/div[1]/div/div/div[2]/div[2]/div[1]/div/ul/li[3]/div/button/span/span");
	}
	public static By createAccountLink() {
		//return By.className("createAccount");
		return By.xpath("//*[@id=\"did-ui-view\"]/div/section/section/form/section/div[5]/a");
	}
	
	public static By firstNameInput() {
		return By.name("firstName");
	}
	
	
	public static By lastNameInput() {
		return By.name("lastName");
	}
	
	//disney.com-->shop_link-->Signup_link-->createaccount_link-->email
		public static By emailInput() {
			return By.name("email");
		}
		
		public static By passwordInput() {
			return By.name("newPassword");
		}
		public static By confirmPasswordnput() {
			return By.name("verifyPassword");
		}
		
		public static By dateofBirth() {
			return By.name("dateOfBirth");
		}
		
		public static By CreateAccount() {
			return By.xpath("//*[@id=\"did-ui-view\"]/div/section/section/form/section[7]/div/button");
		}
		public static By ContinueButton() {
			return By.xpath("//*[@id=\"did-ui-view\"]/div/section/section/div/button");
		}
		
		public static By vacationv() {
			return By.xpath("/html/body/div[1]/main/section[6]/div/header/h2");
		}
		
		public static By signoutlink() {
			return By.xpath("/html/body/div[1]/header/nav/div/div[1]/div/div/div[2]/div[2]/div[1]/div/ul/li[3]/div/span/span");
		}
		

		
		public static By Vacationlbl1() {
			return By.xpath("//*[@id=\"vacation\"]/span");
		}
		
		
		
		
		////////
		
	
	public static By signOutLink() {
		//return By.className("signOut");
		//return By.xpath("//a[contains(@href, '/ocapi/cc/logout')]");
		return By.cssSelector("css=.show > .signout");
	}
	
	
	public static By confirmEmailInput() {
		return By.name("confirmEmail");
	}

	
	
	
	
	public static By signInUserNameInput() {
		//return By.name("email");
		return By.xpath("//*[@id=\"did-ui-view\"]/div/section/section/form/section/div[1]/div/label/span[2]/input");
	}
	public static By signInPasswordInput() {
		//return By.name("password");
		return By.xpath("//*[@id=\"did-ui-view\"]/div/section/section/form/section/div[2]/div/label/span[2]/input");
	}
	
	public static By submitBtn1() {
		return By.xpath("//*[@id=\"did-ui-view\"]/div/section/section/form/section/div[3]/button");
	}
	public static By addressInput() {
		return By.name("address[street1]");
	}
	public static By addressCityInput() {
		return By.name("address[city]");
	}
	public static By addressZipCodeInput() {
		return By.name("address[zip]");
	}
	public static By birthMonth() {
		return By.id("birthDate[month]-wrapper");
	}
	public static By birthMonthList() {
		return By.id("dobMonth-7");
	}
	public static By createAccountComplete() {
		return By.id("done");
	}
	public static By acceptButton() {
		return By.xpath("//div[@id='acceptTermsContainerStepTwo']/span[@class='richCheckBox standardFormElement']");
	}
	public static By birthDay() {
		return By.id("birthDate[day]-wrapper");
	}
	public static By birthDayList() {
		return By.id("dobDay-7");
	}
	public static By addressState() {
		return By.id("address[state]-wrapper");
	}
	public static By addressStateList() {
		return By.id("address-state-5");
	}
	
	public static By birthYear() {
		return By.id("dobYear");
	}
	public static By parksDropDown() {
		//return By.xpath("//*[@id='goc-bar-left']/li[3]/a");
		//return By.xpath("//*[@id=\"goc-bar-left\"]/li[3]/a/u");
		return By.cssSelector("#goc-bar-left > li:nth-child(3) > a");
	}
	
	public static By shopNowLinks() {
		return By.xpath("/html/body/div[1]/header/nav/div/div[1]/div/div/div[2]/div[2]/div[1]/div/ul/li[3]/div/button/span/span");
	}
	public static By shopNowclickLink(int count) {
		return By.xpath("(//div[@class='details-container']/a[@class='skip-link-style ada-el-focus'])["+count+"]");
	}
	public void loginSteps(WebDriver driver, HashMap<String, String> map)
	{
		WebdriverAPI.getBaseURL(map,driver);
		WebdriverAPI.sendKeys(LoginPage.emailTextBox(),  LoginPage.email,driver, map);
		WebdriverAPI.sendKeys(LoginPage.passwordTextBox(),  LoginPage.password,driver, map);
		WebdriverAPI.click(LoginPage.submitBtn(),  30,driver, map);
	}

}
