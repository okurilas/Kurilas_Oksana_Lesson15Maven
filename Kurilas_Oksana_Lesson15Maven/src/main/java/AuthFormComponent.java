import config.ConfigServer;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class AuthFormComponent {

    private WebElementUtils webElementUtils = new WebElementUtils();
    private ConfigServer cfg = ConfigFactory.create(ConfigServer.class);


    public void auth(WebDriver driver, WebDriverWait wait, Logger logger) {

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
        webElementUtils.$(loginField,driver)
                .sendKeys(cfg.login());//.sendKeys("oksana777@list.ru");//.sendKeys(cfg.login());
        webElementUtils.$(pwdField,driver)
                .sendKeys(cfg.pwd());//.sendKeys("Caiman123!");//.sendKeys(cfg.pwd());
        webElementUtils.$(loginButton,driver)
                .submit();

        WebElement avatarPic = wait.until(ExpectedConditions.presenceOfElementLocated(avatar));
        Assert.assertTrue((avatarPic).isDisplayed());
        logger.info("Авторизация прошла успешно");
    }
}
