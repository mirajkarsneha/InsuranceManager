import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PolicyPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public PolicyPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillProductDetails() {
        selectDropdownOption(By.id("country-select"), By.xpath("/html/body/div[3]/div[3]/ul/li[5]"));
        selectDropdownOption(By.id("product-name-select"), By.xpath("/html/body/div[3]/div[3]/ul/li"));
        selectDropdownOption(By.id("tariff-name-select"), By.xpath("/html/body/div[3]/div[3]/ul/li"));
        selectDropdownOption(By.id("category-name-select"), By.xpath("/html/body/div[3]/div[3]/ul/li"));
        selectDropdownOption(By.id("duration-select"), By.xpath("/html/body/div[3]/div[3]/ul/li[1]"));
        selectDropdownOption(By.id("frequency-select"), By.xpath("/html/body/div[3]/div[3]/ul/li"));
        selectDropdownOption(By.id("class-name-select"), By.xpath("//html/body/div[3]/div[3]/ul/li[1]"));
        inputText(By.id("input-createCertificate_serialNumber"), "1");
        inputText(By.id("input-createCertificate_deviceName"), "1");
        inputText(By.id("input-createCertificate_invoiceNumber"), "1");
        inputText(By.id("input-createCertificate_orderNumber"), "1");
        click(By.xpath("/html/body/div[1]/div/div/div[2]/div/div[2]/form/div[5]/div/div[3]/button"));
    }

    public void fillCustomerDetails() {
        click(By.xpath("/html/body/div[1]/div/div/div[2]/div/div[2]/form/div[1]/div/label[1]"));
        inputText(By.id("input-createCertificate_firstName"), "Tester");
        inputText(By.id("input-createCertificate_lastName"), "Tester");
        inputText(By.id("input-createCertificate_email"), "Tester@gmail.com");
        inputText(By.id("input-createCertificate_streetName"), "somestraße");
        inputText(By.id("input-createCertificate_streetNumber"), "1");
        inputText(By.id("input-createCertificate_zip"), "11111");
        inputText(By.id("input-createCertificate_city"),"Berlin");
        inputText(By.id("input-createCertificate_country"),"DE");
        inputText(By.id("input-createCertificate_taxCode"), "0");
        click(By.xpath("/html/body/div[1]/div/div/div[2]/div/div[2]/form/div[11]/div/div[3]/button"));
    }

    public void fillConfirmationDetails() {
        click(By.xpath("/html/body/div[1]/div/div/div[2]/div/div[2]/form/div[1]/div[35]/div/div/label[2]"));
        click(By.xpath("/html/body/div[1]/div/div/div[2]/div/div[2]/form/div[1]/div[35]/div/div/label[3]"));
        click(By.xpath("/html/body/div[1]/div/div/div[2]/div/div[2]/form/div[1]/div[35]/div/div/label[4]/span[1]"));
        click(By.xpath("/html/body/div[1]/div/div/div[2]/div/div[2]/form/div[1]/div[35]/div/div/label[5]"));
        click(By.xpath("/html/body/div[1]/div/div/div[2]/div/div[2]/form/div[2]/div/div[3]/button"));
        click(By.xpath("/html/body/div/div/div[1]/div[2]/div[1]/div/div/div/fieldset/div/label[1]/span[1]"));
        click(By.xpath("/html/body/div/div/div[1]/div[2]/div[2]/div/div[2]/button"));
    }

    public void checkout() throws InterruptedException {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                inputText(By.xpath("/html/body/div/div/div[1]/div[2]/div[1]/div/div/div/div/div/div/div/div/div/input"),"4242424242424242");
                break;
            } catch (Exception e) {
                retryCount++;
                Thread.sleep(1000);
            }
        }
        driver.switchTo().frame(driver.findElement(By.xpath("/html/body/div/div/div[1]/div[2]/div[1]/div/div/div/div/div/div/div/div/div/iframe")));
        inputText(By.xpath("/html/body/div/form/div/div[2]/span[2]/span[1]/span/span/input"),"01 / 26");
        inputText(By.xpath("/html/body/div/form/div/div[2]/span[2]/span[2]/span/span/input"),"540");
        driver.switchTo().defaultContent();
        click(By.xpath("/html/body/div/div/div[1]/div[2]/div[2]/div/div[2]/button[1]"));
    }

    private void selectDropdownOption(By dropdownSelector, By dropdownOptionSelector) {
        WebElement dropDown = driver.findElement(dropdownSelector);
        dropDown.click();
        WebElement dropdownOption = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptionSelector));
        dropdownOption.click();
    }

    private void inputText(By inputSelector, String inputValue) {
        WebElement input = driver.findElement(inputSelector);
        input.sendKeys(inputValue);
    }

    private void click(By elementSelector) {
        WebElement element = driver.findElement(elementSelector);
        element.click();
    }
}