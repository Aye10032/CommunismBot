package com.aye10032.Utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Dazo66
 */
public class SeleniumUtils {

    private static ChromeOptions options = new ChromeOptions();
    public static List<WebDriver> webDrivers = Collections.synchronizedList(new ArrayList<>());
    private static String dir;
    private static WebDriver driver;


    public static void setup (String driverDir) {
        dir = driverDir;
        System.setProperty("webdriver.chrome.driver", dir);
    }

    static {

        options.addArguments("headless");
        options.addArguments("disable-infobars");

        Runtime rt = Runtime.getRuntime();
        rt.addShutdownHook(new Thread(() -> {
            for (WebDriver driver : webDrivers) {
                try {
                    closeDriver(driver);
                } catch (Exception e) {
                    //ignore
                }
            }
        }));//register to the jvm
    }

    public static JavascriptExecutor getDriverJs(WebDriver driver) {
        return ((JavascriptExecutor) driver);
    }

    public static WebDriver getDriver() {
        if (driver != null) {
            return driver;
        } else {
            driver = getNewDriver();
            return driver;
        }
    }

    private static WebDriver getNewDriver(){
        //Initiating your chromedriver
        WebDriver driver = new ChromeDriver(options);

        //Applied wait time
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        //maximize window
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(1, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        //open browser with desried URL
        webDrivers.add(driver);
        return driver;
    }

    public static void closeDriver(WebDriver driver){
        driver.close();
        driver.quit();
    }
}
