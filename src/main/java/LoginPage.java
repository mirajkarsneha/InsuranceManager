import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    private final WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void login() {
        String hubUrl = "https://insurance-manager.sb-qa-candidatetask-2.sisu.sh/login";
        String userName = "testsellingpartner1@simplesurance.de";
        String password = "TestSellingPartner1Pass";

        driver.get(hubUrl);

        WebElement emailInput = driver.findElement(By.id("login_username"));
        emailInput.sendKeys(userName);

        WebElement passwordInput = driver.findElement(By.id("login_password"));
        passwordInput.sendKeys(password);

        WebElement signInButton = driver.findElement(By.xpath("/html/body/div/div/div/div/form/div[4]/button"));
        signInButton.click();
    }
}
