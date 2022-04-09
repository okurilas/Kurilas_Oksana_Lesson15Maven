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

public class firstTest {

    private Logger logger = LogManager.getLogger(firstTest.class);
    WebDriver driver;
    private  ConfigServer cfg = ConfigFactory.create(ConfigServer.class);


    @Before
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver =new ChromeDriver();
        logger.info("драйвер поднят");
    }

    @After
    public void close(){
        if (driver!=null)
        {driver.close();
        }
    }




    @Test
    public void openChromeInHeadlessMode()  {
        logger.info("Открыть Chrome в headless режиме");
        driver.quit();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        driver=new ChromeDriver(options);
        driver.get(cfg.url2());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search_form_input_homepage"))).sendKeys("ОТУС");
        driver.findElement(By.id("search_button_homepage")).click();
        String text =wait.until(ExpectedConditions.presenceOfElementLocated(By.id("r1-0"))).getText();
        logger.info("Первый результат поиска: "+text);
        Assert.assertTrue(text.contains("Онлайн‑курсы для профессионалов, дистанционное обучение"));

    }
    @Test
    public void openChromeInKioskMode() throws InterruptedException {
        logger.info("Открыть Chrome в режиме киоск");
        driver.manage().window().maximize();
        driver.get(cfg.url3());
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("vdo_ai_cross"))).click();
        logger.info("Закрыто окно с видео");

        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='assets/images/p3.jpg']/../.."))) );

        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='assets/images/p3.jpg']/../.."))).click();
        Assert.assertTrue(wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a.pp_expand"))).isEnabled());
        logger.info("картинка открылась в модальном окне");

    }
    @Test
    public void openChromeInFullScreenMode() {
        logger.info("Открыть Chrome в режиме полного экрана");
        driver.manage().window().fullscreen();
        driver.get(cfg.url1());
        auth();
        logger.info("Вывести в лог все cookie");
        logger.info(driver.manage().getCookies());


    }

    public void auth() {
        logger.info("Авторизация");
//        try { driver.findElement(By.xpath("//button[contains(text(),'ОК')]")).click(); }
//        catch (NoSuchElementException e) { System.out.println("Всплывающее окно про куки не появилось при открытии сайта"); }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Вход')]"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input.new-input.new-input_full.js-placeholder.js-input.js-required.js-remove-form-error.new-input_border-no.js-email-input"))).sendKeys(cfg.login()); //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(text(),'Электронная почта ')]/following-sibling::input"))).sendKeys(cfg.login());
        driver.findElement(By.xpath("//input[@type='password']")).sendKeys(cfg.pwd());
        driver.findElement(By.xpath("//button[contains(text(),'Войти')]")).submit(); //driver.findElement(By.cssSelector("div.new-input-line_last:nth-child(5) > button:nth-child(1)")).submit();

        Assert.assertTrue((wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.header2-menu__icon-img.ic-blog-default-avatar")))).isEnabled());
        logger.info("Авторизация прошла успешно");
    }
}
