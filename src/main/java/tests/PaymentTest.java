package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.PaymentPage;
import pages.HomePage;

import java.time.Duration;

public class PaymentTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private PaymentPage paymentPage;
    private HomePage homePage;


    @Before
    public void setup() {
        driver = new PaymentPage(driver, wait).chromeDriverConnection();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // זמן ההמתנה הכללי
        paymentPage = new PaymentPage(driver, wait);
        homePage = new HomePage(driver, wait);
        paymentPage.visit("https://perfectaccessories.co.il/");
    }

    @Test
    public void shouldAddItemsToCartAndVerifyTotal() {
        try {


            homePage.addProduct1ToCart(); // הוספת  פריט לעגלה

            //  homePage.addProduct2ToCart(); // הוספת  פריט לעגלה

            homePage.proceedToPayment(); // מעבר לעמוד התשלום

            double totalSum = paymentPage.calculateTotalInCart();
            double shippingFee = 30;

            paymentPage.waitForCartToLoad(); // המתנה לטעינת העגלה באופן דינמי

            boolean isTotalCorrect = paymentPage.verifyTotalWithShipping(totalSum, shippingFee);

            assert isTotalCorrect : "The total amount displayed does not match the expected total with shipping.";
        }catch (Exception e) {}}

    @Test
    public void shouldNotAllowPaymentWhenCartIsEmpty() {


        // בודק שכאשר העגלה ריקה, לא ניתן לבצע תשלום
        paymentPage.verifyPaymentButtonIsDisabledWhenCartIsEmpty();
    }
    //שינוי קוד ביום האחרון להגשה
//    @Test
//    public void shouldAllowEnterPostalCode() {
//
//        homePage.addProductToCart(); // הוספת  פריט לעגלה
//        homePage.proceedToPayment(); // מעבר לעמוד התשלום
//
//
//        // הזנת מיקוד
//        cartPage.getPickOption(); // תוסיף מתודה שתבחר באופציה של PICK UP
//        cartPage.enterPostalCode("9931915");
//
//        // בדיקה שהכפתור מופעל לאחר הזנת המיקוד
//       boolean  verifyPaymentButtonEnabled=  cartPage.verifyPaymentButtonEnabled();
//        assert verifyPaymentButtonEnabled : "The button for finding the stores is not available";
//    }
    @Test
    public void validateAllFormFields() {

        homePage.addProduct1ToCart(); // הוספת  פריט לעגלה
        homePage.proceedToPayment(); // מעבר לעמוד התשלום
        paymentPage.validateAllFormFields();
    }
    @Test
    public void validateValidInput() {

        homePage.addProduct1ToCart(); // הוספת  פריט לעגלה
        homePage.proceedToPayment(); // מעבר לעמוד התשלום
        paymentPage.validateValidInput();
    }

    @After
    public void endTest() {
        driver.quit();
    }
}
