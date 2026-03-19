package com.leadManager.actionDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.leadManager.base.BaseClass;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;

	

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
	}

	// Wait for element to be clickable
	private void waitforElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			System.out.println("Element is not clickable : " + e.getMessage());
		}
	}

	// Wait for element to be visible
	private void waitforElementToBeVisible(By by) {

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			System.out.println("Element not visible: " + e.getMessage());
		}
	}

	// Method to click an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		applyBorder(by, "green");
		try {
			waitforElementToBeClickable(by);
			driver.findElement(by).click();
		} catch (Exception e) {
			applyBorder(by, "red");
		}
	}

	// method to enter text in input field
	public void enterText(By by, String value) {
		try {
			waitforElementToBeVisible(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			applyBorder(by, "red");
		}
	}

	public String getText(By by) {
		try {
			waitforElementToBeVisible(by);
			applyBorder(by, "green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to get the Text : " + e.getMessage());
		}
		return "";
	}

	// Method to compare text
	public boolean compareText(By by, String expectedText) {
		try {
			waitforElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				System.out.println("Text are matching :" + actualText + " equals " + expectedText);
				
				return true;
			} else {
				applyBorder(by, "red");
				System.out.println("Text are not matching :" + actualText + " , " + expectedText);
				
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to compare text : " + e.getMessage());
			return false;
		}
	}

	// Method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitforElementToBeVisible(by);
			applyBorder(by, "green");
			System.out.println("Element is displayed" + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Element is not displayed : " + e.getMessage());
			
			return false;
		}
	}

	// Scroll to an element -- Added a semicolon ; at the end of the script string
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to locate element:" + e.getMessage());
		}
	}

	// Wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			System.out.println("Page loaded successfully.");
		} catch (Exception e) {
			System.out.println("Page not loaded  : " + e.getMessage());
		}
	}

	// Method to get the description of an element using By locator
	public String getElementDescription(By locator) {

		// Check for null driver or locator to avoid NullPointerException
		if (driver == null) {
			return "Driver is not initialized.";
		}
		if (locator == null) {
			return "Locator is null.";
		}

		try {
			// Find the element using the locator
			WebElement element = driver.findElement(locator);

			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String placeholder = element.getDomAttribute("placeholder");
			String type = element.getDomAttribute("type");
			String className = element.getDomAttribute("class");
			String text = element.getDomAttribute("text");

			// Return a description based on available attributes
			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with ID: " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class: " + className;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder: " + placeholder;
			} else {
				return "Element located using: " + locator.toString();
			}
		} catch (Exception e) {
			System.out.println("Unable to describe element due to error: " + e.getMessage());
		}
		return "Unable to describe element due to error: ";
	}

	// Utility method to truncate long strings
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

	// Utility method to check if a string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to apply border to Element
	public void applyBorder(By by, String color) {
		try {
			WebElement element = driver.findElement(by);
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			System.out.println("Applied the border with color " + color + " to element :" + getElementDescription(by));
		} catch (Exception e) {
			System.out.println("Failed to apply the border to an element" + getElementDescription(by)+"," +e.getMessage());
			e.printStackTrace();
		}
	}

	// ***** SELECT METHODS ********

	public void SelectByVisibleText(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByVisibleText(value);
			applyBorder(by, "green");
			System.out.println("Select dropdown Value:" + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to select dropdown value :" + value+"," +e.getMessage());
		}

	}

	public void SelectByValue(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByValue(value);
			applyBorder(by, "green");
			System.out.println("Selected dropdown value by actual value" + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to select dropdown by value :" + value+"," +e.getMessage());
		}
	}

	public void SelectByIndex(By by, int index) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByIndex(index);
			applyBorder(by, "green");
			System.out.println("Selected dropdown value by index :" + index);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to select dropdown by index :" + index+"," +e.getMessage());
		}
	}

	public void getDropdownOptions(By by) {
		List<String> optionsList = new ArrayList<>();
		try {
			WebElement dropdownELement = driver.findElement(by);
			Select select = new Select(dropdownELement);

			for (WebElement option : select.getOptions()) {
				optionsList.add(option.getText());
			}
			applyBorder(by, "green");
			System.out.println("Retrive dropdown option for :" + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to get dropdown options :" + e.getMessage());
		}
	}

	// Click using Javascript

	public void clickUsingJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			applyBorder(by, "green");
			System.out.println("Clicked element using JavaScript:" + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("Unable to click using JavaScript:" + e);
		}

	}

	public void scrollToBottom() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		} catch (Exception e) {
			System.out.println("Unable to scroll to bottom: " + e.getMessage());
		}
	}

	public void highlightElement(By by) {

		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", element);
			System.out.println(" Hightlight element using JavaScript:" + getElementDescription(by));

		} catch (Exception e) {
			System.out.println("Unable to hightlight element using JavaScript" + e);

		}
	}

	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().contains(windowTitle)) {
					System.out.println("Switched to window: " + windowTitle);
					return;
				}
			}
			System.out.println("Window not found: " + windowTitle);
		} catch (Exception e) {
			System.out.println("Unable to switch window: " + e.getMessage());
		}
	}
	
	public void switchToFrame(By by) {
	    try {
	        WebElement frame = driver.findElement(by);
	        driver.switchTo().frame(frame);
	        System.out.println("Switched to frame: " + getElementDescription(by));
	    } catch (Exception e) {
	        System.out.println("Unable to switch to frame: " + e.getMessage());
	    }
	}
	
	public void switchToDefaultContent() {
	    try {
	        driver.switchTo().defaultContent();
	        System.out.println("Switched back to main page");
	    } catch (Exception e) {
	        System.out.println("Unable to switch to default content: " + e.getMessage());
	    }
	}
	
	/// ****** Handling Alert ********
	public void acceptAlert() {
	    try {
	        driver.switchTo().alert().accept();
	        System.out.println("Alert accepted successfully");
	    } catch (Exception e) {
	        System.out.println("Unable to accept alert: " + e.getMessage());
	    }
	}
	
	public void dismissAlert() {
	    try {
	        driver.switchTo().alert().dismiss();
	        System.out.println("Alert dismissed successfully");
	    } catch (Exception e) {
	    	System.out.println("Unable to dismissed Alert: " + e.getMessage());
	    }
	}
	
	public String getAlertText() {
		String alertText = "";
		try {
			alertText = driver.switchTo().alert().getText();
			System.out.println("Alert text: " + alertText);
		} catch (Exception e) {
			System.out.println("Unable to get Alert text: " + e.getMessage());
		}
		return alertText;
	}
	
	// ********** Browser Actions **********
	
	public void refreshPage() {
	    try {
	        driver.navigate().refresh();
	        System.out.println("Page refreshed successfully");
	    } catch (Exception e) {
	    	System.out.println("Unable to page refreshed: " + e.getMessage());
	    }
	}
	
	public String getCurrentURL() {
	    try {
	        String url = driver.getCurrentUrl();
	        System.out.println("Current url fetched :" +url);
	        return url;
	    } catch (Exception e) {
	    	System.out.println("Unable to fetch current url: " + e.getMessage());
	        return null;
	    }
	}
	
	
	// ******** Advance Actions *******
	
	public void moveToElement(By by) {
	    try {
	        WebElement element = driver.findElement(by);
	        Actions actions = new Actions(driver);
	        actions.moveToElement(element).perform();
	    } catch (Exception e) {
	    	System.out.println("Unable to move to Element: " + e.getMessage());
	    }
	}
	
	public void dragAndDrop(By sourceBy, By targetBy) {
	    try {
	        WebElement source = driver.findElement(sourceBy);
	        WebElement target = driver.findElement(targetBy);
	        Actions actions = new Actions(driver);
	        actions.dragAndDrop(source, target).perform();
	    } catch (Exception e) {
	    	System.out.println("Unable to drag and drop: " + e.getMessage());
	    }
	}
	
	public void doubleClick(By by) {
	    try {
	        WebElement element = driver.findElement(by);
	        Actions actions = new Actions(driver);
	        actions.doubleClick(element).perform();
	    } catch (Exception e) {
	    	System.out.println("Unable to double click : " + e.getMessage());
	    }
	}
	
	public void rightClick(By by) {
	    try {
	        WebElement element = driver.findElement(by);
	        Actions actions = new Actions(driver);
	        actions.contextClick(element).perform();
	    } catch (Exception e) {
	    	 System.out.println("Unable to right click: " + e.getMessage());
	    }
	}
	
	public void sendKeyWithAction(By by, String value) {
	    try {
	        WebElement element = driver.findElement(by);
	        Actions actions = new Actions(driver);
	        actions.moveToElement(element)
	               .click()
	               .sendKeys(value)
	               .build()
	               .perform();
	     
	        System.out.println("Value" +value +"entered using Actions on element: " + getElementDescription(by));
	    } catch (Exception e) {
	    	System.out.println("Unable to send text: " + e.getMessage());
	    }
	}
	
	public void clearText(By by) {
	    try {
	        WebElement element = driver.findElement(by);
	        element.clear();
	    } catch (Exception e) {
	        System.out.println("Unable to clear text: " + e.getMessage());
	    }
	}
	
}
