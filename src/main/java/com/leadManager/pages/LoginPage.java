package com.leadManager.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.leadManager.actionDriver.ActionDriver;
import com.leadManager.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;
	
	private By email = By.xpath("//input[@type='email']");
	private By password = By.xpath("//input[@type='password']");
	private By loginBtn = By.xpath("//button");

    public LoginPage(WebDriver driver) {
    	this.actionDriver = BaseClass.getActionDriver();
    }

    public void login(String userEmail, String userPassword) {
    	actionDriver.enterText(email,userEmail);
    	actionDriver.enterText(password,userPassword);
    	actionDriver.click(loginBtn);
    	actionDriver.waitForPageLoad(10);
    }
}
