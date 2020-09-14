package com.aye10032.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

/**
 * @author Dazo66
 */
public class SeleniumUtils {

    private static ChromeOptions options = new ChromeOptions();
    private static String dir;


    public static void setup (String driverDir) {
        dir = driverDir;
        System.setProperty("webdriver.chrome.driver", dir);
    }

    static {

        options.addArguments("headless");
        options.addArguments("disable-infobars");
    }

    public static JavascriptExecutor getDriverJs(WebDriver driver) {
        return ((JavascriptExecutor) driver);
    }

    public static WebDriver getDriver() {
        return getNewDriver();
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
        return driver;
    }

    public static void closeDriver(WebDriver driver){
        driver.close();
        driver.quit();
    }
}
