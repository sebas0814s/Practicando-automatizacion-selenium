import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.*;

public class AppTest {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private String[] buttons;
    private float retiro = 10;
    private float deposite = 10;

    @BeforeClass
    public static void setUpClass(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // Ejecutar en headless para CI y entornos sin GUI
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 15);
        driver.get("https://www.globalsqa.com/angularJs-protractor/BankingProject/#/login");
    }

    @AfterClass
    public static void tearDownClass(){
        if (driver != null) driver.quit();
    }

    @Test
    public void Ejecutar(){
        buttons = new String[4];
        buttons[0] = "//button[contains(@class, 'btn btn-primary btn-lg')]";
        buttons[1] = "//button[contains(@class, 'btn btn-default')]";
        buttons[2] = "//button[contains(@class, 'btn btn-lg tab')]";
        buttons[3] = "//button[contains(@class, 'btn btn-default')]";

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttons[0]))).click();
        List<WebElement> optionList = driver.findElements(By.xpath("//option[contains(@class, 'ng-binding ng-scope')]"));
        if (optionList.size() > 1) optionList.get(1).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttons[1]))).click();
        WebElement selectTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttons[2])));
        selectTab.click();

        WebElement inputAmount = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[ng-model='amount']")));
        inputAmount.clear();
        inputAmount.sendKeys(String.valueOf(retiro));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(buttons[3]))).click();
        WebElement spanMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[ng-show='message']")));
        System.out.println("Deposit message visible: " + spanMessage.isDisplayed());
    }

    @Test
    public void ExisteDeposito(){
        Ejecutar();
        WebElement spansuccess = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span[ng-show='message']")));
        Assert.assertTrue(spansuccess.isDisplayed());
        Retirar();
    }

    public void Retirar(){
        WebElement withdraw = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[ng-class='btnClass3']")));
        withdraw.click();
        WebElement inputAmout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@class, 'form-control') and contains(@class, 'ng-pristine')]")));
        inputAmout.clear();
        inputAmout.sendKeys(String.valueOf(retiro));

        WebElement buttonRetirar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn btn-default')]")));
        buttonRetirar.click();
    }

    @Test
    public void HayRetiro(){
        ExisteDeposito();
        WebElement retiroMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class, 'error ng-binding')]")));
        Assert.assertTrue(retiroMsg.isDisplayed());
    }

    @Test
    public void RestoBalance(){
        ExisteDeposito();
        List<WebElement> balance = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//strong[contains(@class, 'ng-binding')]")));
        float saldoEsperado = deposite - retiro;
        Assert.assertEquals(String.valueOf(Math.round(saldoEsperado)), balance.get(1).getText());
    }
}
