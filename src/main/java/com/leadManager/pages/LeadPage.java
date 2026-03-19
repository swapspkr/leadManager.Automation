package com.leadManager.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.leadManager.actionDriver.ActionDriver;
import com.leadManager.base.BaseClass;

public class LeadPage {

	private ActionDriver actionDriver;

    private By name = By.xpath("//input[@id='create-name']");
    private By email = By.xpath("//input[@id='create-email']");
    private By phone = By.xpath("//input[@id='create-phone']");
    private By saveBtn = By.xpath("//button[@type='submit']");
    private By searchLead = By.xpath("//input[contains(@placeholder,'Search by name')]");
    

    public LeadPage(WebDriver driver) {
    	this.actionDriver = BaseClass.getActionDriver();
    }

    private By leadRow(String name, String email) {
        return By.xpath("//table//tr[td[contains(text(),'" + name + "')] and td[contains(text(),'" + email + "')]]");
    }
    
    public void createLead(String n, String e, String p) {
    	actionDriver.enterText(name, n);
    	actionDriver.enterText(email,e);
    	actionDriver.enterText(phone,p);
    	actionDriver.scrollToElement(saveBtn);
    	actionDriver.click(saveBtn);
    	actionDriver.waitForPageLoad(10);
    }

    public boolean isLeadDisplayed(String name,String email) {
        return actionDriver.isDisplayed(leadRow(name,email));
    }
    
    public void searchLead(String value) {
    	actionDriver.enterText(searchLead, value);
    }
}
