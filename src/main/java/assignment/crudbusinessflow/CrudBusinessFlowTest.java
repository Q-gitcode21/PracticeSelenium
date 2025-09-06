package assignment.crudbusinessflow;

import com.github.javafaker.Faker;
import common.keywords.WebUI;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

public class CrudBusinessFlowTest {

    //url: https://demoqa.com/webtables or https://demo.beeat.dev/elements/web-tables/
    //US001: Create new user. Verify that User table show the new user
    //US002: Seaching new user. Verify that User table show the user
    //US003: Edit user. Verify that User table show the eddited user
    //US004: Delete user. Verify that User table does not show the deleted user

    private WebUI webUI;
    private final String BROWSER_NAME = "Chrome";
    private final String DEMO_QA_URL = "https://demoqa.com/webtables";
    private final String TXT_FIRST_NAME = "//input[@id='firstName']";
    private final String TXT_LAST_NAME = "//input[@id='lastName']";
    private final String TXT_USER_EMAIL = "//input[@id='userEmail']";
    private final String TXT_AGE = "//input[@id='age']";
    private final String TXT_SALARY = "//input[@id='salary']";
    private final String TXT_DEPARTMENT = "//input[@id='department']";
    private final String BTN_ADD = "//button[@id='addNewRecordButton']";
    private final String BTN_SUBMIT = "//button[@id='submit']";
    private final String ROWS_DATA = "//div[contains(@class, 'rt-tr -odd') or contains(@class, 'rt-tr -even')]";
    private final String CELL_FIRST_NAME = "//div[@role='gridcell'][1]";
    private final String CELL_LAST_NAME = "//div[@role='gridcell'][2]";
    private final String CELL_AGE = "//div[@role='gridcell'][3]";
    private final String CELL_USER_EMAIL = "//div[@role='gridcell'][4]";
    private final String CELL_SALARY = "//div[@role='gridcell'][5]";
    private final String CELL_DEPARTMENT = "//div[@role='gridcell'][6]";
    private final String SPAN_EDIT = "//div[@role='gridcell'][7]//span[starts-with(@id,'edit-record')]";
    private final String SPAN_DELETE = "//div[@role='gridcell'][7]//span[starts-with(@id,'delete-record')]";
    private final String TOTAL_PAGE = "//span[@class='-totalPages']";
    private final String PAGE_JUMP = "//div[@class = '-pageJump']/child::input";

    private final String INPUT_SEARCH = "//input[@id='searchBox']";


    @BeforeMethod
    public void setUp() {
        webUI = new WebUI();
        webUI.openBrowser(BROWSER_NAME);
        webUI.maximumWindow();
    }

    //US001: Create new user. Verify that User table show the new user
    //Description
    //1.Navigate to url "https://demoqa.com/webtables"
    //2.Click to Add button
    //3.Input information
    //4.Click to Submit button // Submit form
    //5.Verify that the new user is correctly created  with the input values at the User table.
    @Test
    public void TC001_verify_create_user_successfully() {
        navigateToUrl(DEMO_QA_URL);

        for (int i = 0; i < 16; i++) {
            addUserFake();
        }

        User specialUser = new User("test many", "Nguyen", "testmany@gmail.com", "25", "5000", "QA");
        addUser(specialUser);

        navigateToFinalPage();
        verifyLastRow(specialUser);
    }


    public void navigateToUrl(String url) {
        webUI.navigateToUrl(url);
    }

    public void clickAddButton() {
        webUI.scrollToElement(BTN_ADD);
        webUI.click(BTN_ADD);
    }

    private User userFake() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String userEmail = firstName.toLowerCase() + lastName.toLowerCase() + "@gmail.com";
        String age = String.valueOf(faker.number().numberBetween(1, 100));
        String salary = String.valueOf(faker.number().numberBetween(1, 100000));
        String department = faker.job().position();

        return new User(firstName, lastName, userEmail, age, salary, department);
    }

    public User addUserFake() {
        User user = userFake();
        addUser(user);
        return user;
    }

    public void addUser(User user) {
        clickAddButton();
        fillUserForm(user);
        submitUserForm();
    }

    private void fillUserForm(User user) {
        webUI.inputText(TXT_FIRST_NAME, user.getFirstName());
        webUI.inputText(TXT_LAST_NAME, user.getLastName());
        webUI.inputText(TXT_USER_EMAIL, user.getEmail());
        webUI.inputText(TXT_AGE, user.getAge());
        webUI.inputText(TXT_SALARY, user.getSalary());
        webUI.inputText(TXT_DEPARTMENT, user.getDepartment());
    }

    public void verifyLastRow(User user) {
        WebElement lastRow = findLastRow();
        verifyRowData(lastRow, user);
    }

    private void verifyRowData(WebElement row, User user) {
        verifyCell(row, CELL_FIRST_NAME, user.getFirstName());
        verifyCell(row, CELL_LAST_NAME, user.getLastName());
        verifyCell(row, CELL_USER_EMAIL, user.getEmail());
        verifyCell(row, CELL_AGE, user.getAge());
        verifyCell(row, CELL_SALARY, user.getSalary());
        verifyCell(row, CELL_DEPARTMENT, user.getDepartment());
    }

    private void verifyCell(WebElement row, String cellXPath, String expectedValue) {
        WebElement cellElement = webUI.findWebElementInParent(row, cellXPath);
        assertTrue(webUI.verifyElementText(cellElement, expectedValue));
    }

    private void submitUserForm() {
        webUI.submitForm(BTN_SUBMIT);
    }

    public void navigateToFinalPage() {
        //Check total page
        webUI.scrollToElement(TOTAL_PAGE);
        String totalPage = webUI.getText(TOTAL_PAGE);
        if (!totalPage.equals("1")) {
            //Navigate to final page
            webUI.inputText(PAGE_JUMP, totalPage);
            webUI.delayInSecond(3);
            webUI.pressKeys(PAGE_JUMP, Keys.ENTER);
            webUI.delayInSecond(5);
        }
    }

    // Find Last Row based on page size
    private WebElement findLastRow() {
        List<WebElement> rows = webUI.findWebElements(ROWS_DATA);
        String lastRowXPath = "//div[contains(@class, 'rt-tr -padRow')]/preceding::div[contains(@class, 'rt-tr')][1]";
        // Check record per page
        if (rows.size() == 10) {
            // Get the last row
            lastRowXPath = "//div[contains(@class, 'rt-tr')][10]";
        }
        return webUI.findWebElement(lastRowXPath);
    }

    //US002: Seaching new user. Verify that User table show the user
    //Description
    //1.Create a new user
    //2.Search for newly created user by email
    //3.Verify that the user is displayed correctly in the User table
    @Test
    public void TC002_verify_search_newly_user_successfully() {
        navigateToUrl(DEMO_QA_URL);
        User user = addUserFake();
        searchUserByEmail(user.getEmail());
        verifyUser(user);
    }

    public void searchUserByEmail(String email) {
        webUI.scrollToElement(INPUT_SEARCH);
        webUI.clearText(INPUT_SEARCH);
        webUI.inputText(INPUT_SEARCH, email);
        webUI.delayInSecond(3);
    }

    public void verifyUser(User user) {
        WebElement rowData = webUI.findWebElement(ROWS_DATA);
        verifyRowData(rowData, user);
    }


    //US003: Edit user. Verify that User table show the edited user
    //Description
    //1.Get information of the user to edit
    // User "Alden" and email "alden@example.com"
    // Select user by based on the email because the email is unique
    //2.Click the Edit span
    //3.Edit all informations of user
    //4.Click the Submit button
    //5.Verify that edited user is correctly displayed with the edited values
    @Test
    public void TC003_verify_edit_user_successfully() {
        navigateToUrl(DEMO_QA_URL);

        User user = addUserFake();
        System.out.println(user.getEmail());

        User editedUser = userFake();
        System.out.println(editedUser.getEmail());
        searchUserByEmail(user.getEmail());

        editUser(editedUser);

        searchUserByEmail(editedUser.getEmail());
        verifyUser(editedUser);
    }

    private void clearTextInForm() {
        webUI.clearText(TXT_FIRST_NAME);
        webUI.clearText(TXT_LAST_NAME);
        webUI.clearText(TXT_USER_EMAIL);
        webUI.clearText(TXT_AGE);
        webUI.clearText(TXT_SALARY);
        webUI.clearText(TXT_DEPARTMENT);
        webUI.delayInSecond(3);
    }

    public void editUser(User user) {
        clickEditButton();
        clearTextInForm();
        fillUserForm(user);
        submitUserForm();
    }

    public void clickEditButton() {
        WebElement rowData = webUI.findWebElement(ROWS_DATA);
        WebElement btnEditRow = webUI.findWebElementInParent(rowData, SPAN_EDIT);
        webUI.click(btnEditRow);
    }

    //US004: Delete user. Verify that User table does not show the deleted user
    //Description
    //1.Create a new user
    //2.Search for newly created user by email
    //3.Delete the user
    //4.Search the user again
    //5.Verify that user is deleted
    @Test
    public void TC004_verify_delete_user_successfully() {
        navigateToUrl(DEMO_QA_URL);

        User user = userFake();
        addUser(user);

        searchUserByEmail(user.getEmail());
        verifyUser(user);

        deleteUser();
        searchUserByEmail(user.getEmail());
        verifyUserNotFound();
    }


    public void deleteUser() {
        WebElement rowData = webUI.findWebElement(ROWS_DATA);
        WebElement btnDeleteRow = webUI.findWebElementInParent(rowData, SPAN_DELETE);
        webUI.click(btnDeleteRow);
    }

    public void verifyUserNotFound() {
        assertTrue(webUI.verifyElementNotPresent(ROWS_DATA));

    }

    @AfterMethod
    public void tearDown() {
        webUI.closeBrowser();
    }
}
