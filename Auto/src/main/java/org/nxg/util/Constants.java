package org.nxg.util;

import java.time.Duration;

/**
 * Application-wide constants and configuration settings.
 * All constants are immutable and thread-safe.
 */
public final class Constants {
    // Runtime Configuration
    public static final String RUNMODE = System.getProperty("runMode", "remote");
    public static final boolean HEADLESS_MODE = Boolean.parseBoolean(System.getProperty("headless", "false"));

    public static final String CHROME_DRIVER_VERSION = System.getProperty("chromedriver.version", "latest");
    public static final String REMOTE_GRID_URL = System.getProperty("grid.url", "http://localhost:4444/wd/hub");
    public static final int THREAD_COUNT = Integer.parseInt(System.getProperty("thread.count", "3"));

    // Report Configuration
    public static final String REPORT_PATH = System.getProperty("report.path",
            System.getProperty("user.dir") + "/target/test-reports");


}
