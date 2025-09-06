

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import common.keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class WebKeywordsDemo {


    private WebUI webUI;

    private final String DAN_TRI_URL = "https://dantri.com.vn/xa-hoi.htm";
    private final String EXPECTED_DAN_TRI_TITLE = "Tin Tức thời sự chính trị Xã hội, giao thông nóng cập nhật liên tục 24/7 | Báo Dân trí";
    private final String VNEXPRESS_URL = "https://vnexpress.net/";
    private final String EXPECTED_VNEXPRESS_TITLE = "Báo VnExpress - Báo tiếng Việt nhiều người xem nhất";
    private final String BROWSER_NAME = "Chrome";
    private final String inputText = "Viet nam";
    private final String txtFullName ="id:userName";
    private final String btnSubmit ="id:submit";
    private final String nameLabel ="xpath://p[@id='name']";


    @BeforeMethod
    public void setUp() {
        webUI = new WebUI();
        webUI.openBrowser(BROWSER_NAME);
    }

    //Description:
    // 1. Navigate to url: https://dantri.com.vn/xa-hoi.htm
    // 2. Verify that title of the page is: "Tin Tức thời sự chính trị Xã hội, giao thông nóng cập nhật liên tục 24/7 | Báo Dân trí"
    // 3. Close the browser
    @Test(enabled = false)
    public void TC001_validate_title_of_page() {

        webUI.navigateToUrl(DAN_TRI_URL);

        String actualTitle = webUI.getTitleOfPage();

        assertEquals(actualTitle, EXPECTED_DAN_TRI_TITLE);
        assertTrue(webUI.verifyTitle(EXPECTED_DAN_TRI_TITLE));
        assertTrue(webUI.verifyTitleContains(EXPECTED_DAN_TRI_TITLE));
    }

    //Description:
    // 1. Navigate to url: https://dantri.com.vn/xa-hoi.htm
    // 2. Verify that title of the page is: "Tin Tức thời sự chính trị Xã hội, giao thông nóng cập nhật liên tục 24/7 | Báo Dân trí"
    // 3. Navigate to url: https://vnexpress.net/
    // 4. Verify that title of the page is: "Báo VnExpress - Báo tiếng Việt nhiều người xem nhất"
    // 5. Back to previous page
    // 6. Verify that title of the page is: "Tin Tức thời sự chính trị Xã hội, giao thông nóng cập nhật liên tục 24/7 | Báo Dân trí"
    // 7. Forward to next page
    // 8. Verify that title of the page is: "Báo VnExpress - Báo tiếng Việt nhiều người xem nhất"
    // 9. Refresh to current page
    // 10. Verify that title of the page is: "Báo VnExpress - Báo tiếng Việt nhiều người xem nhất"
    // 5. Close the browser
    @Test(enabled = false)
    public void TC002_validate_navigate_to_another_url() {

        webUI.navigateToUrl(DAN_TRI_URL);
        assertTrue(webUI.verifyTitle(EXPECTED_DAN_TRI_TITLE));

        webUI.navigateToUrl(VNEXPRESS_URL);
        assertTrue(webUI.verifyTitle(EXPECTED_VNEXPRESS_TITLE));

        webUI.navigateBack();
        assertTrue(webUI.verifyTitle(EXPECTED_DAN_TRI_TITLE));

        webUI.navigateForward();
        assertTrue(webUI.verifyTitle(EXPECTED_VNEXPRESS_TITLE));

        webUI.refreshPage();
        assertTrue(webUI.verifyTitle(EXPECTED_VNEXPRESS_TITLE));
    }

    //Description:
    // 1. Navigate to url: https://demoqa.com/text-box
    // 2. Input text: Viet nam in Full name text box
    // 3. Clear text in Full name text box
    // 4. Input text: Viet nam in Full name text box
    // 5. Press CTRL + a (Windows)/ COMMAND + a (MAC)
    // 6. Press DELETE (MAC)
    // 7. Input text: Viet nam in Full name text box
    // 8. Click on Submit button
    // 9. Close the browser
    @Test(enabled = false)
    public void TC003_validate_web_element_commands(){
        webUI.navigateToUrl("https://demoqa.com/text-box");
        webUI.delayInSecond(3);

        webUI.inputText(txtFullName,inputText);
        webUI.clearText(txtFullName);
        webUI.delayInSecond(3);

        webUI.inputText(txtFullName,inputText);
        webUI.pressShortcut(txtFullName,"a");
        webUI.pressKeys(txtFullName, Keys.DELETE);
        webUI.delayInSecond(3);

        webUI.inputText(txtFullName,inputText);
        webUI.click(btnSubmit);

    }
    //Description:
    // 1. Navigate to url: https://demoqa.com/text-box
    // 2. Input text: Viet nam in Full name text box
    // 3. Verify that Name label is not visible
    // 4. Click on Submit button
    // 5. Verify that Name label is visible
    // 6. Close the browser

    @Test(enabled = true)
    public void TC004_validate_web_element_visibility(){
        webUI.navigateToUrl("https://demoqa.com/text-box");

        webUI.inputText(txtFullName,inputText);
        webUI.delayInSecond(1);

        assertTrue(webUI.verifyElementNotVisible(nameLabel));

        webUI.click(btnSubmit);

        assertTrue(webUI.verifyElementVisible(nameLabel));

    }
    //Description
    //1.Navigate to url: https://demo.beeat.dev/elements/dynamic-properties/
    //2.Verify that button is disable
    //3.Verify the text of button is : Button Disabled
    //4.Time sleep 5 seconds
    //5.Verify that button is enable
    //6.Verify the text of button is : Button Enable
    //7.Get css value of "background-color" of the button
    //8.Close the browser
    @Test(enabled = true)
    public void TC005_validate_web_element_enable(){
        webUI.navigateToUrl("https://demo.beeat.dev/elements/dynamic-properties/");
        String btnEnableAfter = "xpath://button[@id='enableAfter']";

        assertTrue(webUI.verifyElementDisable(btnEnableAfter));

        String actualText=webUI.getText(btnEnableAfter);
        String expectedText = "Button Disabled";

        assertEquals(actualText,expectedText);
        assertTrue(webUI.verifyElementText(btnEnableAfter,expectedText));

        webUI.delayInSecond(5);

        assertTrue(webUI.verifyElementEnable(btnEnableAfter));

        actualText=webUI.getText(btnEnableAfter);
        expectedText = "Button Enable";

        assertEquals(actualText,expectedText);
        assertTrue(webUI.verifyElementText(btnEnableAfter,expectedText));

        webUI.getCssValue(btnEnableAfter,"background-color");


    }


    //Description
    //1.Navigate to url : https://demoqa.com/text-box
    //2.Input text 'Viet nam' in Full name textbox
    //3.Get attribute value of attribute "value" from Full name textbox
    //4.Verify attribute value of attribute "value" from Full name textbox is "Viet nam"
    //5.Close the browser
    @Test(enabled = true)
    public void TC006_validate_web_element_attribute(){
        webUI.navigateToUrl("https://demoqa.com/text-box");

        webUI.inputText(txtFullName,inputText);

        webUI.getAttributeValue(txtFullName,"value");
        assertTrue(webUI.verifyAttributeValue(txtFullName,"value","Viet nam"));
    }



    @AfterMethod
    public void tearDown() {
        webUI.closeBrowser();
    }
}