package org.nxg.testdefinitions;

import io.cucumber.java.en.And;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nxg.page.action.SignupPageAction;
import org.nxg.util.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class SignupPageDefinition {
    private static final Logger log = LogManager.getLogger(SignupPageDefinition.class);
    private final SignupPageAction signupPageAction = new SignupPageAction();

    @Step("Failing the test: {message}")
    public void failTestInAllure(String message) {
        throw new AssertionError(message);
    }

    static void handleException(Exception e) {
        try {
            // Take a screenshot
            WebDriver driver = DriverFactory.getDriver();
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot", new ByteArrayInputStream(screenshotBytes));
        } catch (Exception ex) {
            // Log the error if taking a screenshot fails
            System.err.println("Failed to capture screenshot: " + ex.getMessage());
        }

        // Attach the exception stack trace to the Allure report
        Allure.addAttachment("Exception", e.toString());
        e.printStackTrace(); // Optional: Print stack trace to console
    }

    @Given("User is on Xalts page {string}")
    public void userIsOnXaltsPage(String url) {
        try {
            DriverFactory.openPage(url);
        } catch (Exception e) {
            log.error("The Url is not able to Open: " + e.getMessage());
            handleException(e);
            failTestInAllure("Test failed due to an exception: " + e.getMessage());
        }
    }

    @And("User clicks the Get started button to navigate signup page")
    public void userClicksTheGetStartedButtonToNavigateSignupPage() {
        try {
            signupPageAction.navigateSignup();
        } catch (Exception e) {
            log.error("The Signup page is not opened: " + e.getMessage());
            handleException(e);
            failTestInAllure("Test failed due to an exception: " + e.getMessage());
        }
    }

    @When("User enters username as {string} and password as {string}")
    public void userEntersUsernameAsAndPasswordAs(String user, String pass) {
        try {
            signupPageAction.credentials(user,pass);
        } catch (Exception e) {
            log.error("The Credentials is not able to enter: " + e.getMessage());
            handleException(e);
            failTestInAllure("Test failed due to an exception: " + e.getMessage());
        }

    }

    @Then("User enters the confirm password as {string}")
    public void userEntersTheConfirmPasswordAs(String cPass) {
        try {
            signupPageAction.confirmPassword(cPass);
        } catch (Exception e) {
            log.error("The Confirm Password is not able to enter: " + e.getMessage());
            handleException(e);
            failTestInAllure("Test failed due to an exception: " + e.getMessage());
        }
    }

    @Then("User clicks the signup button")
    public void userClicksTheSignupButton() {
        try {
            signupPageAction.signupButton();
        } catch (Exception e) {
            log.error("The signup button is not able to click: " + e.getMessage());
            handleException(e);
            failTestInAllure("Test failed due to an exception: " + e.getMessage());
        }
    }

    @Then("User clicks the signout button")
    public void userClicksTheSignoutButton() {
        try {
            signupPageAction.signoutButton();
        } catch (Exception e) {
            log.error("The signout button is not able to click: " + e.getMessage());
            handleException(e);
            failTestInAllure("Test failed due to an exception: " + e.getMessage());
        }
    }
}
