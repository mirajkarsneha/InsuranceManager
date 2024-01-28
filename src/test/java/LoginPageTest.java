import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class LoginPageTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private HomePage homePage;
    private PolicyPage policyPage;
    private ImportPage importPage;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        policyPage = new PolicyPage(driver);
        importPage = new ImportPage(driver);
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void shouldCreateNewInsurancePolicy() throws InterruptedException {
        loginPage.login();
        homePage.createNewInsurance();
        policyPage.fillProductDetails();
        policyPage.fillCustomerDetails();
        policyPage.fillConfirmationDetails();
        policyPage.checkout();
    }

    @Test
    public void shouldImportCSVFile() {
        loginPage.login();
        homePage.clickImportButton();
        importPage.importCSV();
    }

    @Test
    public void shouldVerifyFilteringByPolicyNumber() {
        loginPage.login();
        homePage.clickSearchDropdown();
        wait.withTimeout(Duration.ofSeconds(2));
        homePage.selectSearchFilter(By.xpath("/html/body/div[3]/div[3]/ul/li[1]/span[1]"));
        String searchFilterValue = "200000395261";
        homePage.enterSearchFilterValue(By.xpath("/html/body/div[1]/div/div/div[3]/div[1]/div/div/div/input"), searchFilterValue);
        List<WebElement> policies = homePage.getPolicies();
        String policyNumber = homePage.getPolicyNumber(policies.get(1));
        Assert.assertEquals(searchFilterValue, policyNumber);
    }

    @Test
    public void shouldVerifyFilteringByEmail() {
        loginPage.login();
        homePage.clickSearchDropdown();
        wait.withTimeout(Duration.ofSeconds(2));
        homePage.selectSearchFilter(By.xpath("/html/body/div[3]/div[3]/ul/li[2]/span[1]"));
        String searchFilterValue = "Tester@gmail.com";
        homePage.enterSearchFilterValue(By.xpath("/html/body/div[1]/div/div/div[3]/div[1]/div/div/div/input"), searchFilterValue);
        List<WebElement> policies = homePage.getPolicies();
        for (int i = 1; i < policies.size(); i++) {
            String policyEmail = homePage.getPolicyEmail(policies.get(i));
            Assert.assertEquals(searchFilterValue, policyEmail);
        }
    }

    @Test
    public void shouldVerifyFilteringByNewerThanDate() {
        loginPage.login();
        homePage.clickSearchDropdown();
        wait.withTimeout(Duration.ofSeconds(2));
        homePage.selectSearchFilter(By.xpath("/html/body/div[3]/div[3]/ul/li[3]/span[1]"));
        String searchFilterValue = "2024-01-27";
        homePage.enterSearchFilterValue(By.xpath("/html/body/div[1]/div/div/div[3]/div[1]/div/div/div/input"), searchFilterValue);
        List<WebElement> policies = homePage.getPolicies();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMAN);
        LocalDate filterDate = LocalDate.parse("27. Januar 2024", formatter);
        for (int i = 1; i < policies.size(); i++) {
            List<WebElement> cells = policies.get(i).findElements(By.tagName("th"));
            WebElement activationDate = cells.get(3).findElement(By.tagName("span"));
            LocalDate localDate = LocalDate.parse(activationDate.getText(), formatter);
            Assert.assertTrue(localDate.isAfter(filterDate));
        }
    }

    @Test
    public void shouldVerifyFilteringByOlderThanDate() {
        loginPage.login();
        homePage.clickSearchDropdown();
        wait.withTimeout(Duration.ofSeconds(2));
        homePage.selectSearchFilter(By.xpath("/html/body/div[3]/div[3]/ul/li[4]/span[1]"));
        String searchFilterValue = "2024-01-28";
        homePage.enterSearchFilterValue(By.xpath("/html/body/div[1]/div/div/div[3]/div[1]/div/div/div/input"), searchFilterValue);
        List<WebElement> policies = homePage.getPolicies();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMAN);
        LocalDate filterDate = LocalDate.parse("28. Januar 2025", formatter);
        for (int i = 1; i < policies.size(); i++) {
            List<WebElement> cells = policies.get(i).findElements(By.tagName("th"));
            WebElement activationDate = cells.get(4).findElement(By.xpath("span"));
            LocalDate localDate = LocalDate.parse(activationDate.getText(), formatter);
            Assert.assertTrue(localDate.isBefore(filterDate));
        }
    }

    @Test
    public void shouldVerifySortingByPolicyNumber() {
        loginPage.login();
        WebElement policyNumberSort = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[3]/div[2]/div/table"));
        policyNumberSort.click();

        List<WebElement> rows = policyNumberSort.findElements(By.tagName("tr"));
        WebElement headerRow = rows.get(0);

        List<WebElement> firstRowCellsBefore = rows.get(1).findElements(By.tagName("th"));
        String policyNumberBeforeSorting = firstRowCellsBefore.get(0).getText();

        List<WebElement> headerCells = headerRow.findElements(By.tagName("th"));
        WebElement policyNumberHeader = headerCells.get(0);
        WebElement sortIcon = policyNumberHeader.findElement(By.tagName("svg"));
        Actions actions = new Actions(driver);
        actions.moveToElement(sortIcon).click().perform();

        WebElement table = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[3]/div[2]/div/table"));

        List<WebElement> rowsAfter = table.findElements(By.tagName("tr"));
        List<WebElement> firstRowCellsAfter = rowsAfter.get(1).findElements(By.tagName("th"));
        String policeNumberAfterSorting = firstRowCellsAfter.get(0).getText();

        Assert.assertTrue(Long.parseLong(policeNumberAfterSorting) < Long.parseLong(policyNumberBeforeSorting));
    }

    @Test
    public void shouldVerifyActivationDateSorting() {
        loginPage.login();
        WebElement policyTable = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[3]/div[2]/div/table"));
        policyTable.click();

        List<WebElement> rows = policyTable.findElements(By.tagName("tr"));
        WebElement headerRow = rows.get(0);

        List<WebElement> firstRowCellsBefore = rows.get(1).findElements(By.tagName("th"));
        String activationDateBeforeSorting = firstRowCellsBefore.get(3).getText();
        System.out.println(activationDateBeforeSorting);

        List<WebElement> headerCells = headerRow.findElements(By.tagName("th"));
        WebElement activationDateHeader = headerCells.get(3);
        WebElement sortIcon = activationDateHeader.findElement(By.tagName("svg")).findElement(By.tagName("path"));
        Actions actions = new Actions(driver);
        actions.moveToElement(sortIcon).click().perform();

        WebElement table = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[3]/div[2]/div/table"));

        List<WebElement> rowsAfter = table.findElements(By.tagName("tr"));
        List<WebElement> firstRowCellsAfter = rowsAfter.get(1).findElements(By.tagName("th"));
        String activationDateAfterSorting = firstRowCellsAfter.get(3).getText();
        System.out.println(activationDateAfterSorting);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMAN);
        LocalDate before = LocalDate.parse(activationDateBeforeSorting, formatter);
        LocalDate after = LocalDate.parse(activationDateAfterSorting, formatter);

        Assert.assertTrue(after.isEqual(before)  || after.isAfter(before));
    }
}