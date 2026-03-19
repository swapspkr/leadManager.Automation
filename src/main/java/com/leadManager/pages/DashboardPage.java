package com.leadManager.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.leadManager.actionDriver.ActionDriver;
import com.leadManager.base.BaseClass;

public class DashboardPage {

	private ActionDriver actionDriver;

	private By createLeadBtn = By.xpath("//button[contains(text(),'Create Lead')]");

	public DashboardPage(WebDriver driver) {
    	this.actionDriver = BaseClass.getActionDriver();
    }

	public void clickCreateLead() {
		actionDriver.click(createLeadBtn);
	}
}
