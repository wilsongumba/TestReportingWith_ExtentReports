package login;

import base.BaseTests;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SecureAreaPage;

import static org.testng.Assert.assertTrue;

public class LoginTests extends BaseTests {

    @Test
    public void loginWithCorrectUsernameAndPassword(){
        test = extent.createTest("Successful Login Test");
        LoginPage loginPage = homePage.clickFormAuthentication();
        loginPage.setUsername("tomsmith");
        loginPage.setPassword("SuperSecretPassword!");

        SecureAreaPage secureAreaPage = loginPage.clickLoginButton();
        assertTrue(secureAreaPage.getAlertText().contains("You logged into a secure area!"), "Alert text incorrect");

    }

    //intentionally failed to see screenshot capture in action
    @Test
    public void loginWithWrongUsernameAndPassword(){
        test = extent.createTest("Unsuccessful Login Test");
        LoginPage loginPage = homePage.clickFormAuthentication();
        loginPage.setUsername("wrong");
        loginPage.setPassword("wrrong");

        SecureAreaPage secureAreaPage = loginPage.clickLoginButton();
        assertTrue(secureAreaPage.getAlertText().contains("You logged into a secure area!"), "Alert text incorrect");

    }
}
