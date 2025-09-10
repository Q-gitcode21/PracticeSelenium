package assignment.crudbusinessflow;

import com.github.javafaker.Faker;
import common.keywords.WebUI;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CrudBusinessFlowTestV2 {
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
    private final String COLUMN_FIRST_NAME = "//div[@role='gridcell'][1]";
    private final String COLUMN_LAST_NAME = "//div[@role='gridcell'][2]";
    private final String COLUMN_AGE = "//div[@role='gridcell'][3]";
    private final String COLUMN_USER_EMAIL = "//div[@role='gridcell'][4]";
    private final String COLUMN_SALARY = "//div[@role='gridcell'][5]";
    private final String COLUMN_DEPARTMENT = "//div[@role='gridcell'][6]";
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
        clickAddButton();

        User newUser = addUserFake();

        assertTrue(isUserDisplayedInUserTable(newUser));

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


    public void addUser(User user) {
        clickAddButton();
        fillUserForm(user);
        submitUserForm();
    }
    public User addUserFake() {
        User user = userFake();
        addUser(user);
        return user;
    }
    private void fillUserForm(User user) {
        webUI.inputText(TXT_FIRST_NAME, user.getFirstName());
        webUI.inputText(TXT_LAST_NAME, user.getLastName());
        webUI.inputText(TXT_USER_EMAIL, user.getEmail());
        webUI.inputText(TXT_AGE, user.getAge());
        webUI.inputText(TXT_SALARY, user.getSalary());
        webUI.inputText(TXT_DEPARTMENT, user.getDepartment());
    }
    private void submitUserForm() {
        webUI.submitForm(BTN_SUBMIT);
    }
    public boolean isUserDisplayedInUserTable(User user) {
        List<WebElement> rows = webUI.findWebElements(ROWS_DATA);
        if (rows==null){
            return false;
        }

        for (WebElement row : rows) {
            if (isFirstNameDisplayedInRow(row, user.getFirstName())
                    && isLastNameDisplayedInRow(row, user.getLastName())
                    && isAgeDisplayedInRow(row, String.valueOf(user.getAge()))
                    && isEmailDisplayedInRow(row, user.getEmail())
                    && isSalaryDisplayedInRow(row, String.valueOf(user.getSalary()))
                    && isDepartmentDisplayedInRow(row, user.getDepartment())) {
                return true;
            }
        }
        return false;
    }
    public WebElement rowHasUser(User user) {
        List<WebElement> rows = webUI.findWebElements(ROWS_DATA);
        if (rows==null){
            return null;
        }

        for (WebElement row : rows) {
            if (isFirstNameDisplayedInRow(row, user.getFirstName())
                    && isLastNameDisplayedInRow(row, user.getLastName())
                    && isAgeDisplayedInRow(row, String.valueOf(user.getAge()))
                    && isEmailDisplayedInRow(row, user.getEmail())
                    && isSalaryDisplayedInRow(row, String.valueOf(user.getSalary()))
                    && isDepartmentDisplayedInRow(row, user.getDepartment())) {
                return row;
            }
        }
        return null;
    }
    private boolean isFirstNameDisplayedInRow(WebElement row, String firstName) {
        WebElement cellFirstName = webUI.findWebElementInParent(row,COLUMN_FIRST_NAME);
        return webUI.verifyElementText(cellFirstName, firstName);
    }
    private boolean isLastNameDisplayedInRow(WebElement row, String lastName) {
        WebElement cellLastName = webUI.findWebElementInParent(row, COLUMN_LAST_NAME);
        return webUI.verifyElementText(cellLastName, lastName);
    }

    private boolean isAgeDisplayedInRow(WebElement row, String age) {
        WebElement cellAge = webUI.findWebElementInParent(row, COLUMN_AGE);
        return webUI.verifyElementText(cellAge, age);
    }

    private boolean isEmailDisplayedInRow(WebElement row, String email) {
        WebElement cellEmail = webUI.findWebElementInParent(row, COLUMN_USER_EMAIL);
        return webUI.verifyElementText(cellEmail, email);
    }

    private boolean isSalaryDisplayedInRow(WebElement row, String salary) {
        WebElement cellSalary = webUI.findWebElementInParent(row, COLUMN_SALARY);
        return webUI.verifyElementText(cellSalary, salary);
    }

    private boolean isDepartmentDisplayedInRow(WebElement row, String department) {
        WebElement cellDepartment = webUI.findWebElementInParent(row, COLUMN_DEPARTMENT);
        return webUI.verifyElementText(cellDepartment, department);
    }

    public boolean isFirstNameDisplayedInUserTable(String firstName){
        List<WebElement> cellFirstNames = webUI.findWebElements(COLUMN_FIRST_NAME);
        for (WebElement cellFirstName : cellFirstNames){
            if (webUI.verifyElementText(cellFirstName,firstName)){
                return true;
            }
        }
        return false;
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

        assertTrue(isUserDisplayedInUserTable(user));

    }
    public void searchUserByEmail(String email) {
        webUI.scrollToElement(INPUT_SEARCH);
        webUI.clearText(INPUT_SEARCH);
        webUI.inputText(INPUT_SEARCH, email);
        webUI.delayInSecond(3);
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

        User editedUser = userFake();
        searchUserByEmail(user.getEmail());

        assertTrue(isUserDisplayedInUserTable(user));

        editUser(editedUser);

        searchUserByEmail(editedUser.getEmail());

        assertTrue(isUserDisplayedInUserTable(editedUser));


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

        User user = addUserFake();

        searchUserByEmail(user.getEmail());
        assertTrue(isUserDisplayedInUserTable(user));

//        deleteUser();

        searchUserByEmail(user.getEmail());

        assertFalse(isUserDisplayedInUserTable(user));



    }
    @Test
    public void TC005_verify_delete_user_successfully() {
        navigateToUrl(DEMO_QA_URL);

        User user = addUserFake();


        deleteUser(user);


        assertFalse(isUserDisplayedInUserTable(user));



    }


    public void deleteUser(User user) {
        WebElement rowData = rowHasUser(user);
        WebElement btnDeleteRow = webUI.findWebElementInParent(rowData, SPAN_DELETE);
        webUI.click(btnDeleteRow);
    }

    @AfterMethod
    public void tearDown() {
        webUI.closeBrowser();
    }

}
