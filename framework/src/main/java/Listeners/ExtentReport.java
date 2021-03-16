package Listeners;

import Com.Content;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.*;
import org.testng.xml.Parameters;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class ExtentReport implements IReporter {
    private ExtentReports extent;
    private static int testCaseCount = 0;
    private ExtentHtmlReporter extentHtmlReporter;

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outputDirectory) {
        Properties prop = new Properties();
        FileInputStream FileInputStream = null;
        String reportPath = null;

        try {
            FileInputStream = new FileInputStream(
                    new File("src//main//resources//object.properties"));
            prop.load(FileInputStream);
            reportPath = prop.getProperty("ReportPath");
        } catch (IOException e) {
            e.printStackTrace();
        }

        extent = new ExtentReports(reportPath, true);
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
            }
        }
        extent.flush();
        extent.close();
    }

    private void buildTestNodes(IResultMap tests, LogStatus status) {
        ExtentTest test;

        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                //testCaseCount++;
                test = extent.startTest(Content.map.get(result.getParameters()[0].toString()).getTestCaseDescription());
                test.setStartedTime(getTime(result.getStartMillis()));
                test.setEndedTime(getTime(result.getEndMillis()));
               // test.setDescription(Content.map.get(result.getParameters()[0].toString()).getTestCaseDescription());
                //test.setDescription(result.getParameters()[1].toString());



                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);
                if (result.getThrowable() != null) {
                    test.log(status, result.getParameters()[0] + "  testcase failed ");
                } else {
                    test.log(LogStatus.PASS, result.getParameters()[0] + "  testcase passed");


                }
                extent.endTest(test);
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}