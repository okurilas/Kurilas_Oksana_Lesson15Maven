import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScrollClass {

    public void scroll(WebElement pic, WebDriver driver) {
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", pic );

    }
}
