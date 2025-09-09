package assignment.trainingassignment;

import common.keywords.WebUI;
import org.openqa.selenium.Keys;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class AssignmentSelenium {

    private WebUI webUI;
    private final String DEMO_QA_TOOL_TIPS_URL = "https://demoqa.com/tool-tips";
    private final String DEMO_QA_DATE_PICKERS_URL = "https://demoqa.com/date-picker";
    private final String DEMO_QA_UPLOAD_DOWNLOAD_URL = "https://demoqa.com/upload-download";
    private final String TOOL_TIP_BTN = "//button[@id='toolTipButton']";
    private final String TOOL_TIP = "//div[@class='tooltip-inner']";
    private final String DATE_AND_TIME_TXT = "//input[@id='dateAndTimePickerInput']";
    private final String UPLOAD_BTN = "//input[@id='uploadFile']";
    private final String UPLOADED_FILE_PATH = "//p[@id='uploadedFilePath']";


    @BeforeMethod
    public void setUp() {
        webUI = new WebUI();
        webUI.openBrowser("Chrome");
        webUI.maximumWindow();
    }

    //    1. Viết code lấy được text từ tooltip.
    //    Link demo: https://demoqa.com/tool-tips
    //Description
    // Navigate to url https://demoqa.com/tool-tips
    //Hover to tool tip button
    //Verify the text of tooltip is : "Hover me to see"
    @Test
    public void TC_001_verify_text_from_tool_tip() {
        navigateToUrl(DEMO_QA_TOOL_TIPS_URL);
        scrollInToolTipBtn();
        hoverToolTipBtn();
        textOfToolTipShouldBe("You hovered over the Button");


    }

    private void navigateToUrl(String url) {
        webUI.navigateToUrl(url);
    }

    private void hoverToolTipBtn() {
        webUI.hoverToElement(TOOL_TIP_BTN);
        webUI.delayInSecond(3);
    }

    private void textOfToolTipShouldBe(String expectedText) {
        assertTrue(webUI.verifyElementText(TOOL_TIP, expectedText));
    }

    private void scrollInToolTipBtn() {
        webUI.scrollToElement(TOOL_TIP_BTN);
        webUI.delayInSecond(3);
    }

    //    2. Viết hàm chọn date, time trên Calendar với tham số truyền vào có định dạng là YYYY/MM/DD HH:mm.
    //    Link demo: https://demoqa.com/date-picker
    //Description
    //Navigate to url https://demoqa.com/date-picker
    //Input text to date and time input "2025/09/09 17:56"
    //Verify text input is corrected with format
    // MonthName day, Year hour:minute AM/PM :"September 9, 2025 5:56 PM"
    @Test
    public void TC002_input_date_and_time_in_calendar() {
        navigateToUrl(DEMO_QA_DATE_PICKERS_URL);
        scrollInDateTimeTxt();
        inputTextToDateTimeTxt("2025/09/09 17:56");
        textOfDateTimeTxtShouldBe("September 9, 2025 5:56 PM");


    }

    private void scrollInDateTimeTxt() {
        webUI.scrollToElement(DATE_AND_TIME_TXT);
        webUI.delayInSecond(3);
    }

    private void inputTextToDateTimeTxt(String text) {
        webUI.clearText(DATE_AND_TIME_TXT);
        webUI.inputText(DATE_AND_TIME_TXT, text);
        webUI.pressKeys(DATE_AND_TIME_TXT, Keys.ENTER);
        webUI.delayInSecond(3);
    }

    private void textOfDateTimeTxtShouldBe(String expectedText) {
        assertTrue(webUI.verifyAttributeValue(DATE_AND_TIME_TXT, "value", expectedText));
    }

    //    3. Tìm các giải pháp để thực hiện upload file.
    //    Link demo: https://demoqa.com/upload-download
    //Description
    //Navigate to url https://demoqa.com/upload-download
    //C1
    //Input path of file into input type "file"
    //C2
    //Click to upload button
    //Choose a file to upload
    @Test
    public void TC003_upload_file_successfully() {
        navigateToUrl(DEMO_QA_UPLOAD_DOWNLOAD_URL);
        inputFile("C:\\Users\\QUYNH\\OneDrive\\Desktop\\test.docx");
        uploadedFilePathShouldBe("C:\\fakepath\\test.docx");
    }
    private void inputFile(String filePath){
        webUI.scrollToElement(UPLOAD_BTN);
        webUI.inputText(UPLOAD_BTN,filePath);
        webUI.delayInSecond(5);
    }
    private void uploadedFilePathShouldBe(String expectedPath){
        assertTrue(webUI.verifyElementText(UPLOADED_FILE_PATH,expectedPath));
    }


    @AfterMethod
    public void tearDown() {
        webUI.closeBrowser();
    }


}
