import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class HomePage {

    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void createNewInsurance() {
        WebElement newButton = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[1]/div/a[2]"));
        newButton.click();
    }

    public void clickImportButton() {
        WebElement importButton = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[1]/div/a[3]/button"));
        importButton.click();
    }

    public void clickSearchDropdown() {
        WebElement searchFilter;
        try {
            searchFilter = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[3]/div[1]/div/div/div/input"));
            Actions actions = new Actions(driver);
            actions.moveToElement(searchFilter).click().perform();
        } catch (StaleElementReferenceException e) {
            System.out.println("Element is stale, refreshing...");
            searchFilter = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[3]/div[1]/div/div/div/input"));
            Actions actions = new Actions(driver);
            actions.moveToElement(searchFilter).click().perform();
        }
    }

    public void selectSearchFilter(By searchFilter) {
        WebElement searchDropdown = driver.findElement(searchFilter);
        searchDropdown.click();
    }

    public void enterSearchFilterValue(By searchFilterInputSelector, String searchFilterValue) {
        WebElement searchFilterInput = driver.findElement(searchFilterInputSelector);
        Actions actions = new Actions(driver);
        actions.sendKeys(searchFilterInput, searchFilterValue).sendKeys(Keys.ENTER).perform();
    }

    public List<WebElement> getPolicies() {
        WebElement table = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[3]/div[2]/div/table"));
        return table.findElements(By.tagName("tr"));
    }

    public String getPolicyNumber(WebElement policyRow) {
        return policyRow.findElements(By.tagName("th")).get(0).getText();
    }

    public String getPolicyEmail(WebElement policyElement) {
        return policyElement.findElements(By.tagName("th")).get(2).getText();
    }
}
