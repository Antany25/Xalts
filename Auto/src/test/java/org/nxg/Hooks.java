package org.nxg;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.nxg.util.Constants;
import org.nxg.util.DriverFactory;

@Slf4j
public class Hooks {


    @Before
    public void setUp(Scenario scenario) {
        String runMode = System.getProperty("runMode", Constants.RUNMODE); // Use Constants.RUNMODE
        DriverFactory.setRunMode(runMode);
        DriverFactory.getDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        DriverFactory.quitDriver();
    }
}
