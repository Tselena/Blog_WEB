import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.time.Duration;

public class AuthorizationTest extends Data{
    private By loginField = By.xpath("//form[@id='login']/div/label/input");
    private By loginInput = By.cssSelector(".mdc-text-field--label-floating > .mdc-text-field__input");
    private By passwordField = By.cssSelector(".field:nth-child(2) > .mdc-text-field");
    private By passwordInput = By.cssSelector(".mdc-text-field--focused > .mdc-text-field__input");
    private By submitButton = By.className("mdc-button__label");
    private By post = By.cssSelector(".post:nth-child(2) > .svelte-127jg4t:nth-child(2)");
    private By greetingMessage = By.linkText("Hello, Dum");
    private By logOutButton = By.xpath("//div[@id='app']/main/nav/ul/li[3]/div/ul/li[3]/span[2]");


    @BeforeAll
    public static void start() {

        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--incognito");
        options.addArguments("start-maximized");
        options.setPageLoadTimeout(Duration.ofSeconds(15));
        WebDriverManager.chromedriver().setup();
    }

    @Test
    @DisplayName("Check autorization with valid data")
    public void autorizationTest() {

        WebDriver driver = new ChromeDriver();
        Data getData = new Data();

        driver.get(getData.getLoginUrl);
        Assertions.assertTrue(driver.getPageSource().contains("login"), "error");

        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.userName);
        driver.findElement(passwordField).sendKeys(getData.password);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        driver.findElement(post).click();
        Assertions.assertTrue(driver.getPageSource().contains("what happened"), "Что-то пошло не так");
        Assertions.assertTrue(driver.getPageSource().contains("Hello, Dum"), "Что-то пошло не так");
        driver.findElement(greetingMessage).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.findElement(logOutButton).click();
        driver.close();
        driver.quit();
    }

    @Test
    @DisplayName("Check autorization w/o password")
    public void withoutPasswordAutorizationTest() {

        WebDriver driver = new ChromeDriver();
        Data getData = new Data();

        driver.get(getData.getLoginUrl);

        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.userName);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        Assertions.assertTrue(driver.getPageSource().contains("login"));
        driver.close();
        driver.quit();
    }

    @Test
    @DisplayName("Check autorization spec simbols in login")
    public void wrongUserNameAutorizationTest() {

        WebDriver driver = new ChromeDriver();
        Data getData = new Data();

        driver.get(getData.getLoginUrl);

        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.wrongUserName1);
        driver.findElement(passwordField).sendKeys(getData.password);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        Assertions.assertTrue(driver.getPageSource().contains("login"));
        driver.close();
        driver.quit();
    }

    @Test
    @DisplayName("Check autorization too long password")
    public void tooLongPasswordAutorizationTest() {

        WebDriver driver = new ChromeDriver();
        Data getData = new Data();

        driver.get(getData.getLoginUrl);

        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.userName);
        driver.findElement(passwordField).sendKeys(getData.tooLongPassword);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        Assertions.assertTrue(driver.getPageSource().contains("login"));
        driver.close();
        driver.quit();
    }



}
