import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class ImportPage {

    private final WebDriver driver;

    public ImportPage(WebDriver driver) {
        this.driver = driver;
    }

    public void importCSV() {
        WebElement fileInput = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[2]/div[3]/div/input"));
        String relativeFilePath = "src/test/resources/data/testdata.csv";
        Path absolutePath = Paths.get(System.getProperty("user.dir"), relativeFilePath);
        String absoluteFilePath = absolutePath.toAbsolutePath().toString();
        fileInput.sendKeys(absoluteFilePath);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        By fileListLocator = By.xpath("/html/body/div[1]/div/div/div[2]/div[3]/div[2]/table");
        wait.until(ExpectedConditions.visibilityOfElementLocated(fileListLocator));
    }
}