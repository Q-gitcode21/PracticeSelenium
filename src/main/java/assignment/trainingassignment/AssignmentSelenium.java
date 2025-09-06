package assignment.trainingassignment;

import common.keywords.WebUI;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AssignmentSelenium {

    private WebUI webUI;
    private final String DEMO_QA_TOOL_TIPS_URL = "https://demoqa.com/tool-tips";
    private final String DEMO_QA_DATE_PICKERS_URL = "https://demoqa.com/tool-tips";
    private final String DEMO_QA_UPLOAD_DOWNLOAD_URL = "https://demoqa.com/tool-tips";

//    2. Viết hàm chọn date, time trên Calendar với tham số truyền vào có định dạng là YYYY/MM/DD HH:mm.
//    Link demo: https://demoqa.com/date-picker
//    3. Tìm các giải pháp để thực hiện upload file.
//    Link demo: https://demoqa.com/upload-download



    @BeforeMethod
    public void setUp(){
        webUI=new WebUI();
        webUI.openBrowser("Chrome");
        webUI.maximumWindow();
    }

    //    1. Viết code lấy được text từ tooltip.
    //    Link demo: https://demoqa.com/tool-tips
    @Test
    public void TC_001_verify_text_from_tool_tip(){
        navigateToUrl(DEMO_QA_TOOL_TIPS_URL);

    }

    private void navigateToUrl(String url){
        webUI.navigateToUrl(url);
    }


    @AfterMethod
    public void tearDown(){
        webUI.closeBrowser();
    }


}
