import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
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
    private By greetingMessage = By.linkText("Привет, Ivanovych");
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
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        Data getData = new Data();

        driver.get(getData.getLoginUrl);
        Assertions.assertTrue(driver.getPageSource().contains("login"), "Ошибка авторизации");
        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.userNameNoPost);
        driver.findElement(passwordField).sendKeys(getData.passwordNoPost);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        Assertions.assertTrue(driver.findElement(postDescription).getText().contains("No items for your filter"), "На странице показаны чьи-то посты");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        Assertions.assertTrue(driver.findElement(By.cssSelector("span.svelte-1rc85o5")).isDisplayed(), "Нет элемента перехода на главную страницу");
        Assertions.assertTrue(driver.findElement(By.cssSelector("span.svelte-1rc85o5")).getText().contains("Главная"), "Неверное название элемента перехода на главную страницу");
        driver.findElement(By.linkText("Привет, Zhenechka")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.findElement(logOutButton).click();
        driver.close();
        driver.quit();
    }

    @Test
    @DisplayName("Check post in page")
    public void checkPostInPageTest() {

        WebDriver driver = new ChromeDriver();
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        Data getData = new Data();

        driver.get(getData.getLoginUrl);
        Assertions.assertTrue(driver.getPageSource().contains("login"), "Ошибка авторизации");
        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.userName);
        driver.findElement(passwordField).sendKeys(getData.password);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.findElement(nextPageButton).click();
        driver.findElement(post).click();
        driver.findElement(nextPageButton).click();
        driver.findElement(previousPageButton).click();
        driver.findElement(previousPageButton).click();
        driver.findElement(post).click();
        Assertions.assertTrue(driver.findElement(By.tagName("h1")).isDisplayed(), "Отсутствует заголовок");
        Assertions.assertTrue(driver.findElement(postImage).isDisplayed(), "Отсутствует картинка");
        Assertions.assertTrue(driver.findElement(postDescription).isDisplayed(), "Отсутствует описание");
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
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class);
        Actions action = new Actions(driver);
        Data getData = new Data();

        driver.get(getData.getLoginUrl);
        Assertions.assertTrue(driver.getPageSource().contains("login"), "Ошибка авторизации");
        driver.findElement(loginField).click();
        driver.findElement(loginInput).sendKeys(getData.userName);
        driver.findElement(passwordField).sendKeys(getData.password);
        driver.findElement(submitButton).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(nextPageButton));
        driver.findElement(nextPageButton).click();
        wait.until(ExpectedConditions.urlContains(urlPage2));
        driver.findElement(post).click();
        Assertions.assertTrue(driver.getPageSource().contains("английского языка"), "Пост отсутствует на странице 2");
        driver.navigate().back();
        driver.findElement(nextPageButton).click();
        wait.until(ExpectedConditions.urlContains(urlPage3));
        driver.findElement(post).click();
        Assertions.assertTrue(driver.getPageSource().contains("Эйфелева башня"), "Пост отсутствует на странице 3");
        driver.navigate().to(urlPage3);
        driver.findElement(previousPageButton).click();
        wait.until(ExpectedConditions.urlContains(urlPage2));
        driver.findElement(post).click();
        Assertions.assertTrue(driver.getPageSource().contains("слишком жарко"), "Пост отсутствует на странице 2");
        driver.navigate().to(urlPage2);
        driver.findElement(previousPageButton).click();
        wait.until(ExpectedConditions.urlContains(urlPage1));
        driver.findElement(post).click();
        Assertions.assertTrue(driver.getPageSource().contains("опять сегодня жарко"), "Пост отсутствует на странице 1");
        driver.findElement(greetingMessage).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.findElement(logOutButton).click();
        driver.close();
        driver.quit();
    }

}
