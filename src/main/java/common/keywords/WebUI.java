package common.keywords;

import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.w3c.dom.html.HTMLFormElement;
import ostype.OS;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wait.WaitCondition;

import java.time.Duration;
import java.util.List;


public class WebUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebUI.class);
    private WebDriver driver;
    private static final int DEFAULT_TIMEOUT = 30;

    //Chrome, chrome, CHROME
    public void openBrowser(String browserName) {
        try {
            LOGGER.info("Opening browser '{}'", browserName.toUpperCase());
            switch (browserName.toUpperCase()) {
                case "CHROME":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "FIREFOX":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "EDGE":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--remote-allow-origins=*");
                    driver = new EdgeDriver(edgeOptions);
                    break;
                default:
                    LOGGER.info("Unsupported browser.");
            }
            LOGGER.info("Opened browser '{}' successfully", browserName.toUpperCase());
        } catch (Exception e) {
            LOGGER.info("Failed to open browser '{}'. Root cause: {}", browserName.toUpperCase(), e.getMessage());
        }

    }


    public void closeBrowser() {
        try {
            LOGGER.info("Closing the browser");
            driver.quit();
            LOGGER.info("Closed the browser successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to close the browser. Root cause: {}", e.getMessage());
        }
    }

    public String getBrowserName() {
        try {
            LOGGER.info("Getting browser name");
            // get browser session details include browserName,browserVersion,platformName,options,etc.
            // only remoteWebDriver provide getCapabilities method >> must be cast from drive to remote drive
            Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
            String browserName = caps.getBrowserName();
            LOGGER.info("Browser name is '{}' ", browserName);
            return browserName;

        } catch (Exception e) {
            LOGGER.error("Failed to get browser name .Root cause : {}", e.getMessage());
        }
        return null;
    }

    public String getTitleOfPage() {
        String title;
        try {
            LOGGER.info("Getting title for this page");
            title = driver.getTitle();
            LOGGER.info("Got title successfully .The title is: {}", title);
            return title;

        } catch (Exception e) {
            LOGGER.error("Failed to get title for this page.Root cause: {}", e.getMessage());

        }
        return null;
    }

    public boolean verifyTitle(String expectedTitle) {
        String actualTitle;
        try {
            LOGGER.info("Verifying title of the page");
            actualTitle = getTitleOfPage();

            if (actualTitle.equals(expectedTitle)) {
                LOGGER.info("Actual title '{}' and expected title '{}' are the same", actualTitle, expectedTitle);
                return true;
            }

            LOGGER.info("Actual title '{}' and expected title '{}' are  not the same", actualTitle, expectedTitle);

        } catch (Exception e) {
            LOGGER.error("Failed to verify title of the page.Root cause: {}", e.getMessage());

        }
        return false;
    }

    public boolean verifyTitleContains(String expectedTitle) {
        String actualTitle;
        try {
            LOGGER.info("Verifying that the page title contains the expected text ");
            actualTitle = getTitleOfPage();

            if (actualTitle.contains(expectedTitle)) {
                LOGGER.info("Actual title '{}' contain expected title '{}' ", actualTitle, expectedTitle);
                return true;
            }

            LOGGER.info("Actual title '{}' does not contain expected title '{}'", actualTitle, expectedTitle);

        } catch (Exception e) {
            LOGGER.error("Failed to verify that the page title contain the expected text.Root cause: {}", e.getMessage());

        }
        return false;
    }

    public void navigateToUrl(String url) {
        try {
            LOGGER.info("Navigating to url {}", url);
            driver.navigate().to(url);
            LOGGER.info("Navigated to url {} successfully.", url);

        } catch (Exception e) {
            LOGGER.error("Failed to navigate to url {} . Root cause: {}", url, e.getMessage());
        }
    }

    public void navigateBack() {
        try {
            LOGGER.info("Backing to previous page");
            driver.navigate().back();
            LOGGER.info("Backed to previous page successfully.");

        } catch (Exception e) {
            LOGGER.error("Failed to back previous page. Root cause: {}", e.getMessage());
        }
    }

    public void navigateForward() {
        try {
            LOGGER.info("Forwarding to next page");
            driver.navigate().forward();
            LOGGER.info("Forwarded to next page successfully");

        } catch (Exception e) {
            LOGGER.error("Failed to forward page.Root cause: {}", e.getMessage());
        }
    }

    public void refreshPage() {
        try {
            LOGGER.info("Refreshing to this page");
            driver.navigate().refresh();
            LOGGER.info("Refreshed to this page successfully.");

        } catch (Exception e) {
            LOGGER.error("Failed to refresh this page.Root cause: {}", e.getMessage());
        }
    }

    public void closeTab() {
        try {
            LOGGER.info("Closing tab");
            driver.close();
            LOGGER.info("Closed tab successfully.");

        } catch (Exception e) {
            LOGGER.error("Failed to close tab.Root cause: {}", e.getMessage());
        }
    }

    // id:userId
    // css:#userName
    // name:userName
    // xpath://input[@id='userName']
    // tag:input

    public By findBy(String locator) {
        String prefix = StringUtils.substringBefore(locator, ":");
        String suffix = StringUtils.substringAfter(locator, ":");

        return switch (prefix) {
            case "id" -> By.id(suffix);
            case "name" -> By.name(suffix);
            case "xpath" -> By.xpath(suffix);
            case "css" -> By.cssSelector(suffix);
            case "class" -> By.className(suffix);
            case "tag" -> By.tagName(suffix);
            case "link" -> By.linkText(suffix);
            case "partial_link" -> By.partialLinkText(suffix);
            default -> By.xpath(locator);
        };
    }

    // 1. Default -> use DEFAULT_TIMEOUT, condition = PRESENCE
    public WebElement findWebElement(String locator) {
        return findWebElement(locator, WaitCondition.PRESENCE, DEFAULT_TIMEOUT);
    }

    // 2. Custom timeout and default condition "PRESENCE"
    public WebElement findWebElement(String locator, int timeoutInSeconds) {
        return findWebElement(locator, WaitCondition.PRESENCE, timeoutInSeconds);
    }

    // 3. Custom condition + custom timeout
    public WebElement findWebElement(String locator, WaitCondition condition, int timeoutInSeconds) {
        try {
            LOGGER.info("Finding element by locator '{}' with condition '{}' and timeout {}s", locator, condition, timeoutInSeconds);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            WebElement webElement = wait.until(WaitCondition.getCondition(condition, findBy(locator)));
            LOGGER.info("Found element '{}' with condition '{}'", locator, condition);
            return webElement;
        } catch (Exception e) {
            LOGGER.error("Failed to find element in web by locator '{}' with condition '{}' and timeout {}s.Root cause: {}", locator, condition, timeoutInSeconds, e.getMessage());
        }
        return null;
    }

    public WebElement findWebElementInParent(WebElement parentElement, String childLocator) {
        return findWebElementInParent(parentElement, childLocator, DEFAULT_TIMEOUT);
    }
    //  findWebElementFromElement with parentElement and childLocator
    public WebElement findWebElementInParent(WebElement parentElement, String childLocator, int timeoutInSeconds) {

        try {
            LOGGER.info("Finding child element '{}' inside parent element '{}' with timeout {}s", childLocator, parentElement, timeoutInSeconds);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            By child = findBy("."+childLocator);
            WebElement webElement = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentElement, child));
            LOGGER.info("Found child element '{}' inside parent element '{}' with timeout {}s", childLocator, parentElement,timeoutInSeconds);
            return webElement;
        } catch (Exception e) {
            LOGGER.error("Failed to find child element '{}' inside parent element '{}' with timeout {}s. Root cause: {}", childLocator, parentElement, timeoutInSeconds, e.getMessage());
        }
        return null;
    }
    public WebElement findWebElementInParent(String parentLocator, String childLocator) {
        return findWebElementInParent(parentLocator, childLocator, DEFAULT_TIMEOUT);
    }

    public WebElement findWebElementInParent(String parentLocator, String childLocator, int timeoutInSeconds) {

        try {
            LOGGER.info("Finding child element '{}' inside parent element '{}' with timeout {}s", childLocator, parentLocator, timeoutInSeconds);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            By child = findBy("."+ childLocator);
            By parent = findBy(parentLocator);
            WebElement webElement = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(parent, child));
            LOGGER.info("Found child element '{}' inside parent element '{}'", childLocator, parentLocator);
            return webElement;
        } catch (Exception e) {
            LOGGER.error("Failed to find child element '{}' inside parent element '{}' with timeout {}s. Root cause: {}", childLocator, parentLocator, timeoutInSeconds, e.getMessage());
        }
        return null;
    }


    public List<WebElement> findWebElements(String locator) {
        return findWebElements(locator, WaitCondition.PRESENCE, DEFAULT_TIMEOUT);
    }

    public List<WebElement> findWebElements(String locator, int timeoutInSeconds) {
        return findWebElements(locator, WaitCondition.PRESENCE, timeoutInSeconds);
    }

    public List<WebElement> findWebElements(String locator, WaitCondition condition, int timeoutInSeconds) {
        List<WebElement> webElementList = null;
        try {
            LOGGER.info("Finding web elements located by '{}' with condition '{}' and timeout {}s", locator, condition, timeoutInSeconds);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            webElementList = wait.until(WaitCondition.getConditionForList(condition, findBy(locator)));
            LOGGER.info("Found {} web elements located by '{}' with condition '{}'", webElementList.size(), locator, condition);

        } catch (Exception e) {
            LOGGER.error("Failed to find web elements by locator '{}' with condition '{}' and timeout {}s.Root cause: {}", locator, condition, timeoutInSeconds, e.getMessage());
        }
        return webElementList;
    }


    public void inputText(String locator, String text) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Inputting text '{}' into web element find by locator '{}' ", text, locator);
            webElement.sendKeys(text);
            LOGGER.info("Inputted text '{}' into web element find by locator '{}' successfully.", text, locator);
        } catch (Exception e) {
            LOGGER.error("Failed to input text '{}' .Root cause: {}", e.getMessage(), text);
        }


    }

    public void pressKeys(String locator, CharSequence... keys) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Pressing key '{}' into web element find by locator '{}' ", String.join("+", keys), locator);
            webElement.sendKeys(keys);
            LOGGER.info("Pressed key '{}' into web element find by locator '{}' successfully.", String.join("+", keys), locator);
        } catch (Exception e) {
            LOGGER.error("Failed to press key '{}' .Root cause: {}", String.join("+", keys), e.getMessage());
        }


    }


    public void click(String locator) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Click to web element find by locator '{}' ", locator);
            webElement.click();
            LOGGER.info("Clicked to web element find by locator '{}' successfully.", locator);
        } catch (Exception e) {
            LOGGER.error("Failed to click to web element find by locator '{}'  .Root cause: {}", locator, e.getMessage());
        }
    }

    public void click(WebElement webElement) {
        try {
            LOGGER.info("Click to web element");
            webElement.click();
            LOGGER.info("Clicked to web element  successfully.");
        } catch (Exception e) {
            LOGGER.error("Failed to click to web element   .Root cause: {}", e.getMessage());
        }
    }

    public void delayInSecond(int seconds) {
        try {
            LOGGER.info("Delaying {} second", seconds);
            // 1000L : convert to Long type to avoid overflow with int type
            Thread.sleep(seconds * 1000L);
        } catch (Exception e) {
            LOGGER.error("Failed to delay {} seconds.Root cause: {} ", seconds, e.getMessage());
        }
    }

    public void maximumWindow() {
        try {
            LOGGER.info("Opening maximum window");
            driver.manage().window().maximize();
            LOGGER.info("Opened maximum window successfully.");

        } catch (Exception e) {
            LOGGER.error("Failed to open maximum window .Root cause: {}", e.getMessage());
        }
    }

    public void submit(String locator) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Submitting on web element located by '{}'", locator);
            webElement.submit();
            LOGGER.info("Submitted on web element located by '{}' successfully", locator);
        } catch (Exception e) {
            LOGGER.error("Failed to submit on web element located by '{}'. Root cause: {}", locator, e.getMessage());
        }
    }

    public void pressCtrlA(String locator) {
        WebElement webElement = findWebElement(locator);
        String osName = System.getProperty("os.name").toLowerCase();
        LOGGER.info("Current OS is {}", osName);
        Keys modifierKey;

        if (osName.contains("win") || osName.contains("linux")) {
            modifierKey = Keys.CONTROL;
        } else if (osName.contains("mac")) {
            modifierKey = Keys.COMMAND;
        } else {
            LOGGER.warn("Unsupported OS {}", osName);
            return;
        }
        try {
            LOGGER.info("Pressing {} + A on web element located by '{}'", modifierKey, locator);
            webElement.sendKeys(Keys.chord(modifierKey, "a"));
            LOGGER.info("Pressed {} + A on web element located by '{}' successfully", modifierKey, locator);

        } catch (Exception e) {
            LOGGER.error("Failed to press {} + A on web element located by '{}'. Root cause: {}", modifierKey, locator, e.getMessage());
        }
    }

    //version 2 use Enum OS Type
    //Case 1 : Need specific locator to press shortcut
    public void pressShortcut(String locator, CharSequence... keys) {
        Keys modifierKey = OS.getShortcutModifierKey();
        if (keys == null || keys.length == 0) {
            LOGGER.warn("No key provided for shortcut with {} on {}.Skip action ", modifierKey.name(), locator);
            return;
        }
        WebElement webElement = findWebElement(locator);

        LOGGER.info("Modifier key is : {}", modifierKey.name());

        //CharSequence is an interface represent for characters
        //Key.chord need parameter is CharSequence[]

        // Build a new array including modifierKey + user keys
        CharSequence[] allKeys = new CharSequence[keys.length + 1];
        allKeys[0] = modifierKey;
        System.arraycopy(keys, 0, allKeys, 1, keys.length);

        // Syntax: System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
        // - src     : the source array (where elements are copied from)
        // - srcPos  : the starting index in the source array
        // - dest    : the destination array (where elements are copied to)
        // - destPos : the starting index in the destination array
        // - length  : the number of elements to copy

        String chord = Keys.chord(allKeys);
        try {
            webElement.sendKeys(chord);
            LOGGER.info("Pressed short cut {} + {} on {}", modifierKey.name(), String.join("+", keys), locator);
        } catch (Exception e) {
            LOGGER.error("Failed to press short cut {} + {} on {}.Root cause: {}", modifierKey.name(), String.join("+", keys), locator, e.getMessage());
        }

    }

    //Case 2 : No need specific locator
    public void pressShortcut(CharSequence... keys) {
        Keys modifierKey = OS.getShortcutModifierKey();
        if (keys == null || keys.length == 0) {
            LOGGER.warn("No provide key for shortcut with {} .Skip action", modifierKey.name());
            return;
        }
        //return the current element is focus to receive keyboard input (active element)
        WebElement webElement = driver.switchTo().activeElement();
        LOGGER.info("The active element is: {} ", webElement);

        CharSequence[] allKeys = new CharSequence[keys.length + 1];
        allKeys[0] = modifierKey;
        System.arraycopy(keys, 0, allKeys, 1, keys.length);

        String chord = Keys.chord(allKeys);

        try {
            LOGGER.info("Pressing shortcut {} + {} successfully.", modifierKey.name(), String.join("+", keys));
            webElement.sendKeys(chord);
            LOGGER.info("Pressed shortcut {} + {} successfully.", modifierKey.name(), String.join("+", keys));
        } catch (Exception e) {
            LOGGER.error("Failed to press shortcut {} + {} .Root cause: {}", modifierKey.name(), String.join("+", keys), e.getMessage());
        }
    }


    private boolean verifyElementVisibility(String locator, boolean expectedVisible) {
        WebElement webElement = findWebElement(locator);
        String strExpectedVisible = expectedVisible ? "visible" : "not visible";

        try {
            boolean actualResult = webElement.isDisplayed();
            LOGGER.info("Verifying web element located by '{}' is {}", locator, strExpectedVisible);
            if (actualResult == expectedVisible) {
                LOGGER.info("Web element located by '{}' is {}", locator, strExpectedVisible);
                return true;
            }
            LOGGER.error("Web element located by '{}' does not match the expected state: '{}' ", locator, strExpectedVisible);
        } catch (Exception e) {
            LOGGER.error("Failed to verify web element located by '{}' visible.Root cause: {}", locator, e.getMessage());
        }
        return false;
    }

    public boolean verifyElementVisible(String locator) {
        return verifyElementVisibility(locator, true);
    }

    public boolean verifyElementNotVisible(String locator) {
        return verifyElementVisibility(locator, false);
    }

    private boolean verifyElementPresently(String locator, boolean expectedPresent) {
        String strExpectedPresent = expectedPresent ? "present" : "not present";

        try {
            WebElement webElement = findWebElement(locator);
            // true is present and false is not present
            boolean actualResult = (webElement != null);
            LOGGER.info("Verifying web element located by '{}' is {}", locator, strExpectedPresent);

            if (actualResult == expectedPresent) {
                LOGGER.info("Web element located by '{}' is {} as expected", locator, strExpectedPresent);
                return true;
            } else {
                LOGGER.error("Web element located by '{}' does not match the expected state: '{}' ", locator, strExpectedPresent);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to verify web element located by '{}' present. Root cause: {}", locator, e.getMessage());
        }
        return false;

    }

    public boolean verifyElementPresent(String locator) {
        return verifyElementPresently(locator, true);
    }

    public boolean verifyElementNotPresent(String locator) {
        return verifyElementPresently(locator, false);
    }

    private boolean verifyElementEnabled(String locator, boolean expectedEnable) {
        String strExpectedEnable = expectedEnable ? "enable" : "not enable";

        try {
            WebElement webElement = findWebElement(locator);
            boolean actualResult = webElement.isEnabled();
            LOGGER.info("Verifying web element located by '{}' is {}", locator, strExpectedEnable);

            if (actualResult == expectedEnable) {
                LOGGER.info("Web element located by '{}' is {} ", locator, strExpectedEnable);
                return true;
            } else {
                LOGGER.error("Web element located by '{}' does not match the expected state: '{}' ", locator, strExpectedEnable);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to verify web element located by '{}' enable. Root cause: {}", locator, e.getMessage());
        }
        return false;

    }

    public boolean verifyElementEnable(String locator) {
        return verifyElementEnabled(locator, true);
    }

    public boolean verifyElementDisable(String locator) {
        return verifyElementEnabled(locator, false);
    }

    public String getText(String locator) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Getting text from web element located by '{}' ", locator);
            String text = webElement.getText();
            LOGGER.info("Text of web element located by '{}' is '{}' ", locator, text);
            return text;

        } catch (Exception e) {
            LOGGER.error("Failed to get text from web element located by '{}'.Root cause: {}", locator, e.getMessage());
        }
        return null;
    }

    public boolean verifyElementText(String locator, String expectedText) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Verifying text of web element located by '{}' ", locator);
            String actualText = webElement.getText();
            if (actualText.equals(expectedText)) {
                LOGGER.info("Actual text '{}' and Expected text '{}' are the same", actualText, expectedText);
                return true;
            }
            LOGGER.error("Actual text '{}' and Expected text '{}' are not the same", actualText, expectedText);

        } catch (Exception e) {
            LOGGER.error("Failed to verify text of web element located by '{}'. Root cause: {}", locator, e.getMessage());
        }
        return false;
    }

    public boolean verifyElementText(WebElement webElement, String expectedText) {

        try {
            LOGGER.info("Verifying text of web element ");
            String actualText = webElement.getText();
            if (actualText.equals(expectedText)) {
                LOGGER.info("Actual text '{}' and Expected text '{}' are the same", actualText, expectedText);
                return true;
            }
            LOGGER.error("Actual text '{}' and Expected text '{}' are not the same", actualText, expectedText);

        } catch (Exception e) {
            LOGGER.error("Failed to verify text of web element . Root cause: {}", e.getMessage());
        }
        return false;
    }

    public boolean verifyElementContainsText(String locator, String expectedText) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Verifying web element located by '{}' contains text : '{}' ", locator, expectedText);
            String actualText = webElement.getText();
            if (actualText.contains(expectedText)) {
                LOGGER.info("Actual text '{}' contains expected text '{}'", actualText, expectedText);
                return true;
            }
            LOGGER.error("Actual text '{}' does not contain expected text '{}' ", actualText, expectedText);

        } catch (Exception e) {
            LOGGER.error("Failed to verify web element located by '{}' contains text '{}'. Root cause: {}", locator, expectedText, e.getMessage());
        }
        return false;
    }

    public String getCssValue(String locator, String cssProperty) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Getting css value of css property '{}' of web element located by '{}'", cssProperty, locator);
            String cssValue = webElement.getCssValue(cssProperty);
            LOGGER.info("Css value of css property '{}' of web element located by '{}' is '{}'", cssValue, locator, cssProperty);
            return cssValue;

        } catch (Exception e) {
            LOGGER.error("Failed to get css value of css property of web element located by '{}'.Root cause: {}", locator, e.getMessage());
        }
        return null;
    }

    public boolean verifyCssValue(String locator, String cssProperty, String expectedCssValue) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Verifying css value of css property '{}' from web element located by '{}'", cssProperty, locator);
            String actualCssValue = webElement.getCssValue(cssProperty);
            if (actualCssValue.equals(expectedCssValue)) {
                LOGGER.info("Actual css value '{}' and expected css value '{}' are the same", actualCssValue, expectedCssValue);
                return true;
            }
            LOGGER.error("Actual css value '{}' and expected css value '{}' are not the same", actualCssValue, expectedCssValue);
        } catch (Exception e) {
            LOGGER.error("Failed to verify css value of css property '{}' from web element located by '{}'. Root cause: {} ", cssProperty, locator, e.getMessage());
        }
        return false;
    }

    public String getAttributeValue(String locator, String attributeName) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Getting attribute value of attribute '{}' from web element located by '{}'", attributeName, locator);
            String attributeValue = webElement.getAttribute(attributeName);
            LOGGER.info("Attribute value of attribute '{}' is '{}'", attributeName, attributeValue);
            return attributeValue;

        } catch (Exception e) {
            LOGGER.error("Failed to get attribute value of attribute  '{}' from web element located by '{}'.Root cause: {}", attributeName, locator, e.getMessage());
        }
        return null;
    }

    public boolean verifyAttributeValue(String locator, String attributeName, String expectedAttributeValue) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Verifying attribute value of attribute '{}' from web element located by '{}'", attributeName, locator);
            String actualAttributeValue = webElement.getAttribute(attributeName);
            if (actualAttributeValue.equals(expectedAttributeValue)) {
                LOGGER.info("Actual attribute value '{}' and expected attribute value '{}' are the same", actualAttributeValue, expectedAttributeValue);
                return true;
            }
            LOGGER.error("Actual attribute value '{}' and expected attribute value '{}' are not the same", actualAttributeValue, expectedAttributeValue);
        } catch (Exception e) {
            LOGGER.error("Failed to verify attribute value of attribute  '{}' from web element located by '{}'. Root cause: {} ", attributeName, locator, e.getMessage());
        }
        return false;
    }

    public boolean verifyAttributeValue(WebElement webElement, String attributeName, String expectedAttributeValue) {

        try {
            LOGGER.info("Verifying attribute value of attribute '{}' from web element ", attributeName);
            String actualAttributeValue = webElement.getAttribute(attributeName);
            if (actualAttributeValue.equals(expectedAttributeValue)) {
                LOGGER.info("Actual attribute value '{}' and expected attribute value '{}' are the same", actualAttributeValue, expectedAttributeValue);
                return true;
            }
            LOGGER.error("Actual attribute value '{}' and expected attribute value '{}' are not the same", actualAttributeValue, expectedAttributeValue);
        } catch (Exception e) {
            LOGGER.error("Failed to verify attribute value of attribute  '{}' from web element. Root cause: {} ", attributeName, e.getMessage());
        }
        return false;
    }

    public int getElementWidth(String locator) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Getting width of web element located by '{}'", locator);
            int width = webElement.getSize().getWidth();
            LOGGER.info("Width of web element located by '{}' is : {}", locator, width);
            return width;

        } catch (Exception e) {
            LOGGER.error("Failed to get width of web element located by '{}'.Root cause: {}", locator, e.getMessage());
        }
        return -1;
    }

    public int getElementHeight(String locator) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Getting height of web element located by '{}'", locator);
            int height = webElement.getSize().getHeight();
            LOGGER.info("Height of web element located by '{}' is : {}", locator, height);
            return height;

        } catch (Exception e) {
            LOGGER.error("Failed to get height of web element located by '{}'.Root cause: {}", locator, e.getMessage());
        }
        return -1;
    }

    public int getHorizontalPosition(String locator) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Getting horizontal position of web element located by '{}'", locator);
            int horizontalPosition = webElement.getLocation().getX();
            LOGGER.info("Horizontal position of web element located by '{}' is : {}", locator, horizontalPosition);
            return horizontalPosition;


        } catch (Exception e) {
            LOGGER.error("Failed to get horizontal position of web element locate by '{}'.Root cause: {}", locator, e.getMessage());
        }
        return -1;
    }

    public int getVerticalPosition(String locator) {
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Getting vertical position of web element located by '{}'", locator);
            int verticalPosition = webElement.getLocation().getY();
            LOGGER.info("Vertical position of web element located by '{}' is : {}", locator, verticalPosition);
            return verticalPosition;


        } catch (Exception e) {
            LOGGER.error("Failed to get vertical position of web element locate by '{}'.Root cause: {}", locator, e.getMessage());
        }
        return -1;
    }

    public void scrollToElement(String locator) {

        WebElement webElement = findWebElement(locator);

        try {
            LOGGER.info("Scrolling to web element located by '{}'", locator);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView();", webElement);
            LOGGER.info("Scrolled to web element located by '{}' successfully", locator);

        } catch (Exception e) {
            LOGGER.error("Failed to scroll to web element located by '{}' .Root cause: {}", locator, e.getMessage());
        }
    }

    public void submitForm(String locator) {
        WebElement webElement = findWebElement(locator);
        try {
            if (webElement instanceof HTMLFormElement || verifyAttributeValue(webElement, "type", "submit")) {
                LOGGER.info("Submitting form by web element located by '{}'", locator);
                webElement.submit();
                LOGGER.info("Submitted form by web element located by '{}'", locator);
            } else {
                LOGGER.error("The element located by '{}' is not a valid submit element", locator);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to submit form by web element located by '{}'.Root cause: {}", locator, e.getMessage());
        }
    }

    public void clearText(String locator) {
        try {
            LOGGER.info("Clearing text in web element located by '{}'", locator);
            pressCtrlA(locator);
            pressKeys(locator,Keys.BACK_SPACE);

            LOGGER.info("Cleared text in web element located by '{}' successfully", locator);

        } catch (Exception e) {
            LOGGER.error("Failed to clear text in web element located by '{}'.Root cause: {}", locator, e.getMessage());
        }
    }

    public void hoverToElement(String locator){
        WebElement webElement = findWebElement(locator);
        try {
            LOGGER.info("Hovering to web element located by '{}'",locator);
            Actions actions= new Actions(driver);
            actions.moveToElement(webElement).perform();
            LOGGER.info("Hovered to web element located by '{}'",locator);

        } catch (Exception e) {
            LOGGER.error("Failed to hover to web element located by '{}'.Root cause: {}",locator,e.getMessage());
        }
    }


}
