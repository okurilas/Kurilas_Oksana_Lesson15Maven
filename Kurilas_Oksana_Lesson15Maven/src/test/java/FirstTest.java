import config.ConfigServer;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;



public class FirstTest {

    private Logger logger = LogManager.getLogger(FirstTest.class);
    private WebDriver driver;
    private ConfigServer cfg = ConfigFactory.create(ConfigServer.class);
    private WebDriverWait wait;
    private ChromeOptions options = new ChromeOptions();

    @Before
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        logger.info("драйвер поднят");
    }

    @After
    public void close(){
        if (driver!=null) {
            driver.close();
            driver.quit();
        }
    }


    @Test
    public void openChromeInHeadlessMode()  {

        By searchField = By.id("search_form_input_homepage");
        By searchBtn = By.id("search_button_homepage");
        By searchResult = By.id("r1-0");

        logger.info("Открыть Chrome в headless режиме");
        options.addArguments("headless");
        init(options);

        driver.get("https://duckduckgo.com/"); // driver.get(cfg.urlDUCK());

        wait.until(ExpectedConditions.presenceOfElementLocated(searchField))
                .sendKeys("ОТУС");
        $(searchBtn)
                .click();

        String text =wait.until(ExpectedConditions.presenceOfElementLocated(searchResult))
                .getText();

        logger.info("Первый результат поиска: "+text);
        System.out.println(text);

        Assert.assertTrue(text.contains("Онлайн‑курсы для профессионалов, дистанционное обучение"));
    }

    private void init(ChromeOptions options){
        driver = new ChromeDriver(options);
        logger.info("драйвер поднят");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void openChromeInKioskMode() {

        By closeVideo = By.id("vdo_ai_cross");
        By picture = By.xpath("//li[@data-type='cat-item-1']/child::span");
        By expandBtn = By.cssSelector("a.pp_expand");

        logger.info("Открыть Chrome в режиме киоск");
        options.addArguments("--kiosk");
        init(options);

        driver.get("https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818"); // driver.get(cfg.urlDEMO());

        wait.until(ExpectedConditions.presenceOfElementLocated(closeVideo))
                .click();
        logger.info("Закрыто окно с видео");
        WebElement pic = wait.until(ExpectedConditions.elementToBeClickable(picture));

        ScrollClass scrollClass = new ScrollClass();
        scrollClass.scroll(pic, driver);

        pic.click();
        Assert.assertTrue(wait.until(ExpectedConditions.presenceOfElementLocated(expandBtn)).isEnabled());
        logger.info("картинка открылась в модальном окне");
    }

    @Test
    public void openChromeInFullScreenMode() {

        By OTUSheader = By.cssSelector(".header2__logo");

        logger.info("Открыть Chrome в режиме полного экрана");
        options.addArguments("start-fullscreen");
        init(options);

        driver.get("http://otus.ru"); // driver.get(cfg.urlOTUS());

        Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(OTUSheader)).isEnabled());

        auth();

        logger.info("Вывести в лог все cookie");
        ArrayList<Cookie> cookies=new ArrayList<>(driver.manage().getCookies());
        cookies.forEach(cookie -> logger.info(cookie.getName() + "=" + cookie.getValue()));
    }

    private void auth() {

        By loginBtn = By.xpath("//button[contains(text(),'Вход')]");
        By loginField = By.cssSelector(".js-login input[name='email']");
        By pwdField = By.xpath("//input[@type='password']");
        By loginButton = By.xpath("//button[contains(text(),'Войти')]");
        By avatar = By.cssSelector("div.header2-menu__icon-img.ic-blog-default-avatar");

        logger.info("Авторизация");


        wait.until(ExpectedConditions.elementToBeClickable(loginBtn))
                .click();
        wait.until(ExpectedConditions.and(
                ExpectedConditions.presenceOfElementLocated(loginField),
                ExpectedConditions.presenceOfElementLocated(pwdField)
        ));
        $(loginField)
                .sendKeys("oksana777@list.ru");//.sendKeys(cfg.login());
        $(pwdField)
                .sendKeys("Caiman123!");//.sendKeys(cfg.pwd());
        $(loginButton)
                .submit();

        WebElement avatarPic = wait.until(ExpectedConditions.presenceOfElementLocated(avatar));
        Assert.assertTrue((avatarPic).isDisplayed());
        logger.info("Авторизация прошла успешно");
    }

    private WebElement $(By locator) {
        return driver.findElement(locator);
    }

}
