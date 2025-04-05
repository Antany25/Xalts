package org.nxg.page.locator;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignupPageLocators {

    @FindBy(xpath = "//button[normalize-space(text())='Get Started']")
    public WebElement getStarted;

    @FindBy(xpath = "//legend/span[normalize-space(text())='E-Mail']/ancestor::div[contains(@class, 'MuiInputBase-root')]//input")
    public  WebElement userName;

    @FindBy(xpath = "//input[@id='outlined-basic' and @type='password']")
    public WebElement password;

    @FindBy(xpath = "//label[text()='Confirm Password']/following-sibling::div//input[@type='password']")
    public WebElement confirmPassword;

    @FindBy(xpath = "//button[text()='Sign Up']")
    public WebElement signup;

    @FindBy(xpath = "//button[text()='Sign Out']")
    public WebElement signedIn;

}