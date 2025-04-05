package org.nxg.page.action;

import lombok.extern.slf4j.Slf4j;
import org.nxg.page.locator.SignupPageLocators;
import org.nxg.util.DriverFactory;
import org.nxg.util.WaitUtil;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class SignupPageAction {

     private final SignupPageLocators signupPageLocators;

    public SignupPageAction() {
        this.signupPageLocators = new SignupPageLocators();
        PageFactory.initElements(DriverFactory.getDriver(), signupPageLocators);
    }


    public void navigateSignup() throws InterruptedException {
        WaitUtil.waitForVisibility(signupPageLocators.getStarted);
        signupPageLocators.getStarted.click();
        sleep(3000);
    }

    private void sleep(int time) throws InterruptedException {
       Thread.sleep(time);
    }

    public void credentials(String user, String pass) throws InterruptedException {
        WaitUtil.waitForVisibility(signupPageLocators.userName);
        signupPageLocators.userName.sendKeys(user);
        sleep(2000);
        WaitUtil.waitForVisibility(signupPageLocators.password);
        signupPageLocators.password.sendKeys(pass);
        sleep(2000);
    }

    public void confirmPassword(String cPass) throws InterruptedException {
        WaitUtil.waitForVisibility(signupPageLocators.confirmPassword);
        signupPageLocators.confirmPassword.sendKeys(cPass);
        sleep(3000);
    }

    public void signupButton() throws InterruptedException {
        WaitUtil.waitForVisibility(signupPageLocators.signup);
        signupPageLocators.signup.click();
        sleep(5000);
        WaitUtil.waitForVisibility(signupPageLocators.signedIn);
        signupPageLocators.signedIn.isDisplayed();
    }

    public void signoutButton() {
        WaitUtil.waitForVisibility(signupPageLocators.signedIn);
        signupPageLocators.signedIn.click();
    }
}