package ru.Vlad.NauJava.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthUiTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeAll
    static void setUpDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe");
//        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    private void login(String username, String password) {
        driver.get(baseUrl + "/login");
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Test
    void loginSuccess_showsHomePage() {
        login("admin", "admin");

        wait.until(ExpectedConditions.urlToBe(baseUrl + "/"));
        Assertions.assertEquals("FileShareService", driver.findElement(By.id("title")).getText());
        Assertions.assertEquals("admin", driver.findElement(By.id("username")).getText());
    }

    @Test
    void logout_redirectsToLoginPage() {
        login("admin", "admin");
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/"));

        driver.findElement(By.id("logout")).click();

        wait.until(ExpectedConditions.urlContains("/login"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("/login"));
    }

    @Test
    void loginWithWrongPassword_staysOnLoginWithError() {
        login("admin", "wrong");

        wait.until(ExpectedConditions.urlContains("/login?error"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("error"));
    }
}