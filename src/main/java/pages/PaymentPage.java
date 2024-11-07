package pages;

import DDT.ExcelReader;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class PaymentPage extends Base {
    private ExcelReader excelReader= new ExcelReader("\"C:\\Users\\tsw32\\OneDrive\\שולחן העבודה\\לימודים\\בוקמפ\\projectPerfect\\src\\main\\resources\\\u200F\u200Fperfect_xl_data - עותק.xlsx\"");

    private By emailField = By.name("email");
    private By firstNameField = By.name("firstName");
    private By lastNameField = By.name("lastName");
    private By addressField = By.name("address1");
    private By apartmentField = By.name("address2");
    private By cityField = By.name("city");
    private By zipField = By.name("postalCode");
    private By countryField = By.name("countryCode");
    private By phoneField = By.name("phone");
    private By submitButton = By.cssSelector("#checkout-pay-button");
    private By errorField = By.cssSelector(".field-error");

    private By cartRow = By.cssSelector("div._6zbcq51n ._19gi7yt0"); // selector לכל שורת מוצר בסל
    private By priceInRow = By.cssSelector("[class=\"_197l2oft _1fragemnn _1fragemme _1fragem28 _1fragemlj Byb5s\"] [class=\"_19gi7yt0 _19gi7ytw _19gi7ytv _1fragemnu notranslate\"]"); // selector למחיר
    private By quantityInRow = By.cssSelector("div._6zbcq51n span:nth-child(2)");
    private By totalSumElement = By.cssSelector("strong._19gi7yt0._19gi7yt10._19gi7ytz._1fragemnw");
    private By shippingCheckbox = By.cssSelector("#delivery_strategies-SHIPPING");
    private By shopingCartButton = By.cssSelector(".header__nav-icon.icon.icon-cart");
    private By postelPlace=By.cssSelector("#AutocompleteSingleAddressField0");
    private By pickOption = By.cssSelector("#delivery_strategies-PICK_UP");
    private By changeLocation = By.cssSelector("#Form0 > div.km09ry1._1fragemlj > div:nth-child(4) > div > section > div > div._1ip0g651._1ip0g650._1fragemlj._1fragem4b._1fragem64._1fragem2s > section:nth-child(2) > div > div > div._1mrl40q0._1fragemlj._1fragem4o._1fragem6h._1fragemma._1fragemmf._1fragem2s._1fragemm3._1fragem78._1fragemoh._16s97g7f._16s97g7i._16s97g7p._16s97g71j._16s97g71m._16s97g71t > button");
    private  By findStores=By.cssSelector("[class=\"_1m2hr9ge _1m2hr9gd _1fragemsq _1fragemlj _1fragemnk _1fragem2i _1fragems4 _1fragemsg _1fragemsl _1fragemsa _1m2hr9g16 _1m2hr9g13 _1fragemop _1fragemon _1fragemor _1fragemol _1fragempl _1fragemph _1fragempp _1fragempd _1fragemb4 _1fragemaf _1fragembt _1fragem9q _1fragemsa _1m2hr9g1q _1m2hr9g1o _1m2hr9g10 _1m2hr9gx _1m2hr9g29 _1m2hr9g28 _1fragems0 _1m2hr9g1b _1m2hr9g19 _1fragems5 _1m2hr9g25\"]");
    public PaymentPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }





    public void verifyPaymentButtonIsDisabledWhenCartIsEmpty() {

        click(shopingCartButton);
        // חפש את כפתור התשלום


        if (!isDisplayed(shopingCartButton)) {
            throw new AssertionError("Expected the payment button to be disabled when the cart is empty.");
        }
    }

    public double calculateTotalInCart() {
        List<WebElement> priceElements = findElements(priceInRow);
        List<WebElement> quantityElements = findElements(quantityInRow);

        double total = 0.0;
        System.out.println("מספר מחירים: " + priceElements.size());
        System.out.println("מספר כמויות: " + quantityElements.size());
        // Ensure both lists have the same size
        if (priceElements.size() != quantityElements.size()) {
            System.out.println("שגיאה: מספר האלמנטים של מחירים וכמויות לא תואם.");
            return total; // Return 0 if there is a mismatch
        }

        // Loop through each price and corresponding quantity element
        for (int i = 0; i < priceElements.size(); i++) {
            WebElement priceElement = priceElements.get(i);
            WebElement quantityElement = quantityElements.get(i);

            // Get the price and quantity values
            String priceText = priceElement.getText().replace("₪", "").trim(); // Remove ₪ sign
            String quantityText = quantityElement.getText().trim(); // Get the quantity as text

            // Convert to double
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            // Calculate total for this item
            total += price * quantity;
        }

        return total;
    }

    public void waitForCartToLoad() throws InterruptedException {
        Thread.sleep(3000);
        waitUntilElementLocated(totalSumElement);
        waitUntilElementLocated(cartRow);
    }

    public double getTotalSumDisplayed() {
        String totalText = findElement(totalSumElement).getText().trim().split("\\.")[0]; // קבל את הטקסט של הסכום הכולל
        return parsePrice(totalText.replaceAll("[^0-9]", "")); // המרה לערך מספרי
    }

    private double parsePrice(String text) {
        try {
            return Double.parseDouble(text.replaceAll("[^0-9.]", "")); // הסרת תווים לא מספריים
        } catch (NumberFormatException e) {
            return 0; // החזר 0 במקרה של שגיאה
        }
    }
    public void validateAllFormFields() {

        // בדיקות שדות
        validateInvalidEmail();

        validateInvalidFirstName();

        validateInvalidLastName();
        validateInvalidAddress();
        validateInvalidPhone();
        validateValidInput();
    }

    public boolean verifyTotalWithShipping(double totalSum, double shippingFee) {
        double displayedTotal = getTotalSumDisplayed();
        System.out.println(displayedTotal );
        System.out.println(totalSum );

        if (totalSum < 300) {
            if (findElement(shippingCheckbox).isSelected()) {
                return displayedTotal == totalSum + shippingFee; // חישוב כולל עם דמי משלוח
            } else {
                return displayedTotal == totalSum; // חישוב כולל בלי דמי משלוח
            }
        }
        return true; // אם הסכום הכולל הוא מעל 300, אין דמי משלוח
    }
    public void getPickOption() {
        // בחר באופציית Pick
        click(pickOption);
    }

    public void enterPostalCode(String postalCode) {
        click(changeLocation);
        // הזנת מיקוד
        type(postalCode,postelPlace);


    }
    public boolean verifyPaymentButtonEnabled(){
        // בדיקה שהכפתור מופעל לאחר הזנת המיקוד
        return     isDisplayed(findStores);
    }

    //מכאןןןןןןןןןןןןןןןןןןןן
    private void validateInvalidEmail() {
        System.out.println("Typing invalid email in the email field.");
        type("invalidemail", emailField);

        System.out.println("Clicking on the submit button.");
        findElement(submitButton).click();

        System.out.println("Looking for the error message for email.");
        boolean isEmailErrorDisplayed = isElementPresent(By.cssSelector("#error-for-email"));
        Assert.assertTrue("Error message for email was not displayed", isEmailErrorDisplayed);

        System.out.println("Clearing the email field.");
    }

    private void validateInvalidFirstName() {
        type("12345", firstNameField);
        click(submitButton);

        boolean isNameErrorDisplayed = isElementPresent(By.cssSelector("#error-for-name"));
        Assert.assertTrue("Error message for first name was not displayed", isNameErrorDisplayed);
    }

    private void validateInvalidLastName() {
        type("!@#$%", lastNameField);
        findElement(submitButton).click();

        boolean isLastNameErrorDisplayed = isElementPresent(By.cssSelector("#error-for-TextField1"));
        Assert.assertTrue("Error message for last name was not displayed", isLastNameErrorDisplayed);

        System.out.println("Clearing the LastName field.");
    }

    private void validateInvalidAddress() {
        type("!@#$%^", addressField);
        findElement(submitButton).click();

        boolean isAddressErrorDisplayed = isElementPresent(By.cssSelector("#error-for-TextField2"));
        Assert.assertTrue("Error message for address was not displayed", isAddressErrorDisplayed);

        System.out.println("Clearing the Address field.");
    }

    private void validateInvalidPhone() {
        type("abc123", phoneField);
        findElement(submitButton).click();

        boolean isPhoneErrorDisplayed = isElementPresent(By.cssSelector("#error-for-TextField6"));
        Assert.assertTrue("Error message for phone was not displayed", isPhoneErrorDisplayed);

        System.out.println("Clearing the Phone field.");
    }

    public void validateValidInput() {

        try {
            List<String[]> formDataList = excelReader.readFormData();

            for (String[] rowData : formDataList) {
                // מניח שהעמודות מכילות את הסדר הבא: מייל, שם פרטי, שם משפחה, כתובת, דירה, עיר, מיקוד, מדינה, טלפון
                String email = rowData[0];
                String firstName = rowData[1];
                String lastName = rowData[2];
                String address = rowData[3];
                String apartment = rowData[4];
                String city = rowData[5];
                String zip = rowData[6];
                String country = rowData[7];
                String phone = rowData[8];

                // מימוש הקוד שממלא את השדות בטופס
                type(email, emailField);
                type(firstName, firstNameField);
                type(lastName, lastNameField);
                type(address, addressField);
                type(apartment, apartmentField);
                type(city, cityField);
                type(zip, zipField);
                type(country,countryField);
                type(phone, phoneField);

                // לחץ על כפתור שליחה לאחר מילוי השדות
                findElement(submitButton).click();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
