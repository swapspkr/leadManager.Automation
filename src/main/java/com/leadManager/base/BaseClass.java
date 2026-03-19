package com.leadManager.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.beust.jcommander.Parameters;
import com.leadManager.actionDriver.ActionDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {
	protected static Properties prop;
	protected FileInputStream fis;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

	@BeforeSuite
	public void loadConfig() throws IOException {
		// load the configuration file
		prop = new Properties();
		fis = new FileInputStream(
				System.getProperty("user.dir") + File.separator + "src/main/resources/config.properties");
		prop.load(fis);
	}

	@BeforeMethod
	public void setup() {
		String browser = prop.getProperty("browser");
		launchBrowser(browser);
		configueBrowser();
		// Initialize ActionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
	}

	private void configueBrowser() {
		// implicit wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) { //
			System.out.println("Failed to navigate to url" + e.getMessage());
		}
	}

	private synchronized void launchBrowser(String browser) {

		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			// Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			// options.addArguments("--headless"); // Run Chrome in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU for headless mode
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments
			driver.set(new ChromeDriver(options));

		} else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			// Create FirefoxOptions
			FirefoxOptions options = new FirefoxOptions();
			// options.addArguments("--headless"); // Run Firefox in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
			options.addArguments("--width=1920"); // Set browser width
			options.addArguments("--height=1080"); // Set browser height
			options.addArguments("--disable-notifications"); // Disable browser notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD environments
			options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments

			driver.set(new FirefoxDriver(options)); // New Changes as per Thread

		} else if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			EdgeOptions options = new EdgeOptions();
			// options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes

			driver.set(new EdgeDriver(options)); // New Changes as per Thread

		} else {
			throw new IllegalArgumentException("Browser Not Supported:" + browser);
		}

	}

	public static Properties getProp() {
		return prop;
	}

	// Getter method for driver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("Webdriver instance not initialise");
			throw new IllegalStateException("Webdriver instance not initialise");
		}
		return driver.get();
	}

	// Getter method for Actiondriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("ActionDriver instance not initialise");
			throw new IllegalStateException("ActionDriver instance not initialise");
		}

		return actionDriver.get();
	}

	/// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit driver" + e.getMessage());
			}
		}
		driver.remove();
	}

}
