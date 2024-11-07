package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends  Base {
    private By Product = By.cssSelector("#featured-collections-product-list-carousel-collection-1 product-card:first-child .product-card__image--secondary\n");
    private By addToCartButton = By.cssSelector("[class=\"button w-full\"]");
    private By proceedToPaymentButton = By.cssSelector("[class=\"button-group\"] [class=\"button w-full\"]");

    public HomePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);

    }

    public void addProduct1ToCart() {

        findElement(Product).click();
        waitUntilElementLocated(addToCartButton);
        getLast(addToCartButton).click();
    }
    public void addProduct2ToCart() {

        findElement(Product).click();
        waitUntilElementLocated(addToCartButton);
        getLast(addToCartButton).click();
    }
    public void proceedToPayment() {
        click(proceedToPaymentButton);
    }
}
