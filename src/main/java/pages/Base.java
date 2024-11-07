package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Base {
    private WebDriver driver;
    private WebDriverWait wait;

    public Base(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriver chromeDriverConnection() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        return driver;
    }


    public void waitUntilElementLocated(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }
    public boolean isElementPresent(By by) {
        try {
            waitUntilElementLocated(by);
            return findElement(by).isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }
    public List findElements(By locator) {
        return driver.findElements(locator);
    }

    public String getText(WebElement element) {
        return element.getText();
    }

    public String getText(By locator) {
        return driver.findElement(locator).getText();
    }

    public WebElement type(String inputText, By locator) {
        findElement(locator).sendKeys(inputText);
        return findElement(locator);
    }

    public void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        if (element.isDisplayed()) {
            element.click();
        } else {
            System.out.println("האלמנט לא מוצג: " + locator);
        }
    }
    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    public WebElement getLast(By locator) {
        List<WebElement> buttons = findElements(locator);
        if (!buttons.isEmpty()) {
            return buttons.get(buttons.size() - 1); // מחזיר את האלמנט האחרון ברשימה
        }
        throw new NoSuchElementException("No elements found for selector: " + locator);
    }


    public boolean isEnabled(By locator) {
        WebElement element = findElement(locator);
        return element.isEnabled();
    }
    public void visit(String url) {
        driver.get(url);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }
}