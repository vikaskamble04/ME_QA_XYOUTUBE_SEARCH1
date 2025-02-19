package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class Wrappers {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    public Wrappers(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }
    public void clickElement(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    public void jsClickElement(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }
    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }
    public List<WebElement> findElements(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    public String getText(By locator) {
        return findElement(locator).getText();
    }
    public void enterText(By locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }
    public void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public void search(String locator, String searchText) {
        WebElement searchBox = driver.findElement(By.xpath(locator)); 
        searchBox.click();
        searchBox.clear();
        searchBox.sendKeys(searchText);
        searchBox.sendKeys(Keys.ENTER);
    }
    public long convertViewsToNumber(String views) {
        views = views.replaceAll("[^0-9KM.]", "").trim(); // Remove non-numeric characters except 'K' and 'M'
        if (views.endsWith("K")) {
            return (long) (Double.parseDouble(views.replace("K", "")) * 1_000);
        } else if (views.endsWith("M")) {
            return (long) (Double.parseDouble(views.replace("M", "")) * 1_000_000);
        } else {
            return Long.parseLong(views); 
        }
    }

}
