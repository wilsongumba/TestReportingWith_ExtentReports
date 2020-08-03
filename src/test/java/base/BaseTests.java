package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;
import pages.SecureAreaPage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseTests {

    private WebDriver driver;
    protected HomePage homePage;
    protected LoginPage loginPage;
    protected SecureAreaPage secureAreaPage;

    public ExtentHtmlReporter htmlReporter;
    public ExtentReports extent;
    public ExtentTest test;


    public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        // after execution, you could see a folder "FailedTestsScreenshots" under src folder
        String destination = System.getProperty("user.dir") + "/TestResults/FailedTestScreenshots/" + screenshotName + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        return destination;
    }


    //create reporter
    @BeforeTest
    public void setExtent() {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());

        String testSuiteName = getClass().getSimpleName(); //returns name only
        //String testSuiteName = getClass().getName(); ////returns name and package name
        //String testCaseName = getClass().getEnclosingMethod().getName();


        // specify location of the report
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/TestResults/TestReports/" + testSuiteName + "Report" + dateName + ".html");

        htmlReporter.config().setDocumentTitle("Using Extent Example Report"); // Tile of report
        htmlReporter.config().setReportName("Login Automation Report"); // Name of the report
        htmlReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        // Passing General information
        extent.setSystemInfo("OS", "Windows 10");
        extent.setSystemInfo("Browser", "Google Chrome V83.0.4103.106 (64-bit)");
        extent.setSystemInfo("Department", "STL QA");
        extent.setSystemInfo("Tester Name", "Wilson Gumba");
    }


    @BeforeMethod
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/");
        homePage = new HomePage(driver);


    }


    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {

        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, "TEST CASE FAILED: " + result.getName()); // to add name in extent report
            test.log(Status.FAIL, "FAIL LOG DETAILS: " + result.getThrowable()); // to add error/exception in extent report
            String screenshotPath = BaseTests.getScreenshot(driver, result.getName());
            String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            //System.out.println(screenshotPath);
            //test.addScreenCaptureFromPath(screenshotPath, "Screenshot");
            //test.fail("Screenshot: " + test.addScreenCaptureFromPath(screenshotPath));
            test.addScreenCaptureFromPath(screenshotPath);// adding screen shot
        }
        else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, "TEST CASE SKIPPED: " + result.getName());
        }
        else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, "TEST CASE PASSED: " + result.getName());
        }
        System.out.println("Application run complete.");
        driver.quit();
    }

    //kill reporter
    @AfterTest
    public void endReport() {
        extent.flush();
    }

}