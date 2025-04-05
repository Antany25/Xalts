package org.nxg;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.TestNG;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@CucumberOptions(
        features = "classpath:/TestSuite/signup.feature",
        glue = {"org/nxg/testdefinitions", "Hooks"},
        plugin = {"pretty","io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",}
)

public class TestRun extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
//    public static void main(String[] args) {
//        TestNG testNG = new TestNG();
//        // Add the TestRun class as the suite to be executed
//        testNG.setTestClasses(new Class[]{TestRun.class});
//        // Execute the tests
//        testNG.run();
//    }
}


