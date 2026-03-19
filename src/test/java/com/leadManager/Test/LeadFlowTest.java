package com.leadManager.Test;


import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.leadManager.base.BaseClass;
import com.leadManager.pages.DashboardPage;
import com.leadManager.pages.LeadPage;
import com.leadManager.pages.LoginPage;
import com.leadManager.utils.TestDataUtil;

public class LeadFlowTest extends BaseClass{
	private LoginPage loginPage;
	private DashboardPage dashboardPage;
	private LeadPage leadPage;

	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		dashboardPage = new DashboardPage(getDriver());
		leadPage = new LeadPage(getDriver());
	}
	
	
	@Test
	public void testCreateLeadFlow() {

		// Login
		loginPage.login(BaseClass.getProp().getProperty("username"),BaseClass.getProp().getProperty("password"));

		// Create Lead
		dashboardPage.clickCreateLead();
		
        // Use reusable test data
        String name = TestDataUtil.getName();
        String email = TestDataUtil.getEmail();
		    
		leadPage.createLead(name, email, "9999999999");
		leadPage.searchLead(name);
		// Verify Lead List
		Assert.assertTrue(leadPage.isLeadDisplayed(name,email), "Lead not displayed!");
	}
}
