package org.nxg.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.remote.http.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> waitThreadLocal = new ThreadLocal<>();
    private static final String REMOTE_GRID_URL = "http://localhost:4444/wd/hub";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);
    private static String runMode = Constants.RUNMODE;

    public static void setRunMode(String mode) {
        log.info("Setting run mode to: {}", mode);
        runMode = mode;
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            driver = setupDriver();
            driverThreadLocal.set(driver);
            configureDriver(driver);
        }
        return driver;
    }

    public static WebDriverWait getWait() {
        if (waitThreadLocal.get() == null) {
            waitThreadLocal.set(new WebDriverWait(getDriver(), DEFAULT_TIMEOUT));
        }
        return waitThreadLocal.get();
    }

    private static WebDriver setupDriver() {
        try {
            ChromeOptions options = createChromeOptions();
            WebDriver driver;

            if (runMode.equalsIgnoreCase("remote")) {
                log.info("Initializing Remote WebDriver with URL: {}", REMOTE_GRID_URL);
                driver = new RemoteWebDriver(new URL(REMOTE_GRID_URL), options);
            } else {
                log.info("Initializing Local WebDriver");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(options);
            }

            return driver;
        } catch (MalformedURLException e) {
            log.error("Invalid remote URL: {}", REMOTE_GRID_URL);
            throw new RuntimeException("Failed to initialize WebDriver: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Failed to initialize WebDriver: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize WebDriver: " + e.getMessage(), e);
        }
    }


    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Performance optimizations
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.prompt_for_download", false);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments(
            "--start-maximized",
            "--remote-allow-origins=*",
            "--disable-gpu",
            "--disable-dev-shm-usage",
            "--no-sandbox",
            "--disable-extensions",
            "--disable-notifications",
            "--disable-infobars"
        );

        // Add headless mode if needed
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless=new");
        }

        return options;
    }

    private static void configureDriver(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT);
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
        driver.manage().timeouts().scriptTimeout(DEFAULT_TIMEOUT);

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.body.style.zoom='67%'");
        } catch (Exception e) {
            log.warn("Failed to set zoom level: {}", e.getMessage());
        }
    }

    public static void openPage(String url) {
        WebDriver driver = getDriver();
        try {
            log.info("Opening URL: {}", url);
            driver.get(url);
            // Wait for page load to complete
            new WebDriverWait(driver, PAGE_LOAD_TIMEOUT)
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
        } catch (TimeoutException e) {
            log.error("Page load timeout for URL: {}", url);
            throw new RuntimeException("Page failed to load within timeout: " + url, e);
        }
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                log.info("Quitting WebDriver");
                driver.quit();
            } catch (Exception e) {
                log.error("Error while quitting WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
                waitThreadLocal.remove();
            }
        }
    }
}
