import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckPostTest extends Data {
    private By loginField = By.xpath("//form[@id='login']/div/label/input");
    private By loginInput = By.cssSelector(".mdc-text-field--label-floating > .mdc-text-field__input");
    private By passwordField = By.cssSelector(".field:nth-child(2) > .mdc-text-field");
    private By passwordInput = By.cssSelector(".mdc-text-field--focused > .mdc-text-field__input");
    private By submitButton = By.className("mdc-button__label");
    private By nextPageButton = By.cssSelector(".svelte-d01pfs > a:nth-child(2)");
    private By previousPageButton = By.cssSelector("a.svelte-d01pfs");
    private By post = By.cssSelector(".post:nth-child(2) > .svelte-127jg4t:nth-child(2)");
    private By postImage = By.cssSelector("img");
    private By postDescription = By.cssSelector(".content");
    private By greetingMessage = By.linkText("Hello, Dum");
    private By logOutButton = By.xpath("//div[@id='app']/main/nav/ul/li[3]/div/ul/li[3]/span[2]");


    @BeforeAll
    public static void start() {

        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--incognito");
        options.addArguments("start-maximized");
        options.setPageLoadTimeout(Duration.ofSeconds(30));
        WebDriverManager.chromedriver().setup();
    }

    @Test
    @DisplayName("Check no post in page")
    public void checkNoPostInPageTest() {

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        Data getData = new Data();

        driver.get(getData.getLoginUrl);
        Assertions.assertTrue(driver.getPageSource().contains("login"), "error");
        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.userNameNoPost);
        driver.findElement(passwordField).sendKeys(getData.passwordNoPost);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        Assertions.assertTrue(driver.getPageSource().contains("login"), "error");

        Assertions.assertTrue(driver.findElement(postDescription).getText().contains("No items for your filter"), "there is a post on page");
        driver.findElement(By.linkText("Hello, Bunny")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.findElement(logOutButton).click();
        driver.close();
        driver.quit();
    }

    @Test
    @DisplayName("Check post in page")
    public void checkPostInPageTest() {

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        Data getData = new Data();

        driver.get(getData.getLoginUrl);
        Assertions.assertTrue(driver.getPageSource().contains("login"), "error");
        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.userName);
        driver.findElement(passwordField).sendKeys(getData.password);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        Assertions.assertTrue(driver.getPageSource().contains("login"), "error");


        driver.findElement(nextPageButton).click();
        driver.findElement(post).click();
        driver.findElement(nextPageButton).click();
        driver.findElement(previousPageButton).click();
        driver.findElement(previousPageButton).click();
        driver.findElement(post).click();
        Assertions.assertTrue(driver.findElement(By.tagName("h1")).isDisplayed(), "there is no title");
        Assertions.assertTrue(driver.findElement(postImage).isDisplayed(), "there is no image");
        Assertions.assertTrue(driver.findElement(postDescription).isDisplayed(), "there is no descripion");
        driver.findElement(greetingMessage).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.findElement(logOutButton).click();
        driver.close();
        driver.quit();
    }

    @Test
    @DisplayName("Check surf between pages")
    public void checkSurfBetweenPagesTest() {

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);
        Actions action = new Actions(driver);
        Data getData = new Data();

        driver.get(getData.getLoginUrl);
        Assertions.assertTrue(driver.getPageSource().contains("login"), "error");
        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.userName);
        driver.findElement(passwordField).sendKeys(getData.password);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        Assertions.assertTrue(driver.getPageSource().contains("login"), "error");

        wait.until(ExpectedConditions.elementToBeClickable(nextPageButton));
        driver.findElement(nextPageButton).click();
        wait.until(ExpectedConditions.urlContains(urlPage2));
        driver.findElement(post).click();
        Assertions.assertTrue(driver.getPageSource().contains("4 day"), "error");
        driver.navigate().back();
        driver.findElement(nextPageButton).click();
        wait.until(ExpectedConditions.urlContains(urlPage3));
        driver.findElement(post).click();
        Assertions.assertTrue(driver.getPageSource().contains("start study"), "error");
        driver.navigate().to(urlPage3);
        driver.findElement(previousPageButton).click();
        wait.until(ExpectedConditions.urlContains(urlPage2));
        driver.findElement(post).click();
        Assertions.assertTrue(driver.getPageSource().contains("4 day"), "error");
        driver.navigate().to(urlPage2);
        driver.findElement(previousPageButton).click();
        wait.until(ExpectedConditions.urlContains(urlPage1));
        driver.findElement(post).click();
        Assertions.assertTrue(driver.getPageSource().contains("what happened"), "error");
        driver.findElement(greetingMessage).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.findElement(logOutButton).click();
        driver.close();
        driver.quit();
    }

}
