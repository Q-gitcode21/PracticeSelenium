package wait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public enum WaitCondition {
    PRESENCE,
    VISIBLE,
    CLICKABLE;

    public static ExpectedCondition<WebElement> getCondition(WaitCondition condition, By locator){
        return switch (condition){
            case PRESENCE -> ExpectedConditions.presenceOfElementLocated(locator);
            case VISIBLE -> ExpectedConditions.visibilityOfElementLocated(locator);
            case CLICKABLE ->ExpectedConditions.elementToBeClickable(locator);
            default -> ExpectedConditions.presenceOfElementLocated(locator);
        };
    }
    public static ExpectedCondition<WebElement> getCondition(WaitCondition condition, WebElement webElement){
        return switch (condition){
            case VISIBLE -> ExpectedConditions.visibilityOf(webElement);
            case CLICKABLE ->ExpectedConditions.elementToBeClickable(webElement);
            default -> ExpectedConditions.visibilityOf(webElement);
        };
    }



    public static ExpectedCondition<List<WebElement>> getConditionForList(WaitCondition condition, By locator) {
        return switch (condition) {
            case VISIBLE   -> ExpectedConditions.visibilityOfAllElementsLocatedBy(locator);
            default        -> ExpectedConditions.presenceOfAllElementsLocatedBy(locator);
        };
    }
}
