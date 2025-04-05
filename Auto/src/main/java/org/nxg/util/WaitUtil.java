package org.nxg.util;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class WaitUtil {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration EXTENDED_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration POLLING_INTERVAL = Duration.ofMillis(500);

    public static WebElement waitForVisibility(WebElement element) {
        return waitForVisibility(element, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForVisibility(WebElement element, Duration timeout) {
        try {
            log.debug("Waiting for element visibility: {}", element);
            return getWait(timeout).until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            log.error("Element not visible within timeout: {}", element, e);
            throw new RuntimeException("Element not visible within timeout: " + e.getMessage(), e);
        }
    }

    public static WebElement waitForClickable(WebElement element) {
        return waitForClickable(element, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForClickable(WebElement element, Duration timeout) {
        try {
            log.debug("Waiting for element to be clickable: {}", element);
            return getWait(timeout).until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            log.error("Element not clickable within timeout: {}", element, e);
            throw new RuntimeException("Element not clickable within timeout: " + e.getMessage(), e);
        }
    }

    public static boolean waitForInvisibility(WebElement element) {
        return waitForInvisibility(element, DEFAULT_TIMEOUT);
    }

    public static boolean waitForInvisibility(WebElement element, Duration timeout) {
        try {
            log.debug("Waiting for element invisibility: {}", element);
            return getWait(timeout).until(ExpectedConditions.invisibilityOf(element));
        } catch (TimeoutException e) {
            log.error("Element still visible after timeout: {}", element, e);
            throw new RuntimeException("Element still visible after timeout: " + e.getMessage(), e);
        }
    }

    public static List<WebElement> waitForPresenceOfAll(By locator) {
        return waitForPresenceOfAll(locator, DEFAULT_TIMEOUT);
    }

    public static List<WebElement> waitForPresenceOfAll(By locator, Duration timeout) {
        try {
            log.debug("Waiting for presence of all elements: {}", locator);
            return getWait(timeout).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            log.error("Elements not present within timeout: {}", locator, e);
            throw new RuntimeException("Elements not present within timeout: " + e.getMessage(), e);
        }
    }

    public static void waitForPageLoad() {
        waitForPageLoad(EXTENDED_TIMEOUT);
    }

    public static void waitForPageLoad(Duration timeout) {
        try {
            log.debug("Waiting for page load to complete");
            getWait(timeout).until(driver ->
                    "complete".equals(((JavascriptExecutor) driver).executeScript("return document.readyState"))
            );
        } catch (TimeoutException e) {
            log.error("Page load timeout", e);
            throw new RuntimeException("Page failed to load within timeout: " + e.getMessage(), e);
        }
    }

    public static <T> T waitFor(Function<WebDriver, T> condition) {
        return waitFor(condition, DEFAULT_TIMEOUT);
    }

    public static <T> T waitFor(Function<WebDriver, T> condition, Duration timeout) {
        try {
            log.debug("Waiting for custom condition");
            return getWait(timeout).until(condition);
        } catch (TimeoutException e) {
            log.error("Custom condition timeout", e);
            throw new RuntimeException("Custom condition failed within timeout: " + e.getMessage(), e);
        }
    }

    // ✅ Fixed WebDriverWait method with correct Duration usage
    private static WebDriverWait getWait(Duration timeout) {
        return (WebDriverWait) new WebDriverWait(DriverFactory.getDriver(), timeout)
                .pollingEvery(POLLING_INTERVAL)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);
    }

    public static void waitForAjax() {
        try {
            getWait(DEFAULT_TIMEOUT).until(driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                Object result = js.executeScript("return typeof jQuery !== 'undefined' && jQuery.active === 0;");
                return result instanceof Boolean && (Boolean) result;
            });
        } catch (TimeoutException e) {
            log.error("Ajax requests did not complete within timeout", e);
            throw new RuntimeException("Ajax requests did not complete within timeout: " + e.getMessage(), e);
        } catch (JavascriptException e) {
            log.warn("jQuery is not available on the page", e);
        }
    }

    public static void waitForAngular() {
        try {
            getWait(DEFAULT_TIMEOUT).until(driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                Object result = js.executeScript(
                        "return window.getAllAngularTestabilities && "
                                + "window.getAllAngularTestabilities().every(t => t.isStable())");
                return result instanceof Boolean && (Boolean) result;
            });
        } catch (TimeoutException e) {
            log.error("Angular did not stabilize within timeout", e);
            throw new RuntimeException("Angular did not stabilize within timeout: " + e.getMessage(), e);
        } catch (JavascriptException e) {
            log.warn("Angular is not available on the page", e);
        }
    }
}
