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
    WebDriver driver;
    private ConfigServer cfg = ConfigFactory.create(ConfigServer.class);
    private WebDriverWait wait;
    private ChromeOptions options = new ChromeOptions();

    @Before
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver =new ChromeDriver();
        logger.info("драйвер поднят");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @After
    public void close(){
        if (driver!=null) {
            driver.close();
        }
    }


    @Test
    public void openChromeInHeadlessMode()  {
        logger.info("Открыть Chrome в headless режиме");
        driver.quit();
        options.addArguments("headless");
        chromeOptions(options);
        driver.get("https://duckduckgo.com/"); // driver.get(cfg.urlDUCK());

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_form_input_homepage"))).sendKeys("ОТУС");
        driver.findElement(By.id("search_button_homepage")).click();
        String text =wait.until(ExpectedConditions.presenceOfElementLocated(By.id("r1-0"))).getText();
        logger.info("Первый результат поиска: "+text);
        System.out.println("ошибка: " + text);
        Assert.assertTrue(text.contains("Онлайн‑курсы для профессионалов, дистанционное обучение"));
    }

    private void chromeOptions(ChromeOptions options){
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void openChromeInKioskMode() {
        logger.info("Открыть Chrome в режиме киоск");
        driver.quit();
        options.addArguments("--kiosk");
        chromeOptions(options);

        driver.get("https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818"); // driver.get(cfg.urlDEMO());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("vdo_ai_cross"))).click();
        logger.info("Закрыто окно с видео");
        WebElement pic = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/section[2]/div/ul[2]/li[3]/span")));

        ScrollClass scrollClass = new ScrollClass();
        scrollClass.scroll(pic, driver);

        pic.click();
        Assert.assertTrue(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.pp_expand"))).isEnabled());
        logger.info("картинка открылась в модальном окне");
    }

    @Test
    public void openChromeInFullScreenMode() {
        logger.info("Открыть Chrome в режиме полного экрана");
        driver.manage().window().fullscreen();
        driver.get("http://otus.ru"); // driver.get(cfg.urlOTUS());
        Assert.assertTrue(wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".header2__logo"))).isEnabled());
        auth();
        logger.info("Вывести в лог все cookie");

        StringBuffer ret = new StringBuffer();
        ArrayList<Cookie> cookies1=new ArrayList<>(driver.manage().getCookies());
        for (int i = 0; i < cookies1.size(); i++) {
            ret.append(cookies1.get(i).getName() + "=" + cookies1.get(i).getValue());
            if (i != cookies1.size() - 1) {
                ret.append("; ");
            }
        }
        logger.info(ret);


    }

    private void auth() {
        logger.info("Авторизация");


//        wait.until(ExpectedConditions.and(
//                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Вход')]")),
//                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".js-login input[name='email']"))
//        ));
//        driver.findElement(By.xpath("//button[contains(text(),'Вход')]"))
//                .click();
//        driver.findElement(By.cssSelector(".js-login input[name='email']"))
//                .sendKeys("oksana777@list.ru");//.sendKeys(cfg.login());

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Вход')]"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".js-login input[name='email']")))
                .sendKeys("oksana777@list.ru");

        driver.findElement(By.xpath("//input[@type='password']"))
                .sendKeys("Caiman123!");//.sendKeys(cfg.pwd());
        driver.findElement(By.xpath("//button[contains(text(),'Войти')]")).submit();

        WebElement avatar = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.header2-menu__icon-img.ic-blog-default-avatar")));
        Assert.assertTrue((avatar).isDisplayed());
        logger.info("Авторизация прошла успешно");
    }



}
