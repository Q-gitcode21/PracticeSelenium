import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeleniumDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumDemo.class.getSimpleName());

    public static void main(String[] args) throws InterruptedException {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--headless=new");
        options.addArguments("--window-size=1000,700");
        WebDriver driver = new ChromeDriver(options);
        LOGGER.info("Open browser");
        LOGGER.error("Open browser");
        LOGGER.warn("Open browser");
        Thread.sleep(5000);
        driver.get("https://dantri.com.vn/");
        Thread.sleep(5000);
        driver.quit();
    }
}