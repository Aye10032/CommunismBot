package com.aye10032.utils;

import com.aye10032.Zibenbot;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    public static void closeDriver(WebDriver driver) {
        driver.close();
        driver.quit();
    }

    private static String addhttp(String url) {
        if (url.startsWith("http")) {
            return url;
        } else {
            return "https://" + url;
        }
    }

    public synchronized static File getScreenshot(WebDriver driver, String url, String outFileName, int timeOut, String... js) throws IOException, InterruptedException {
        try {
            driver.get(url = addhttp(url));
            Zibenbot.logInfoStatic("Chrome Dirver switch to" + url);
            Thread.sleep(timeOut);
            JavascriptExecutor driver_js = SeleniumUtils.getDriverJs(driver);
            //Double width = Double.valueOf(driver_js.executeScript(
            //        "return document.getElementsByTagName('html')[0].getBoundingClientRect().width;").toString());
            Double height = Double.valueOf(driver_js.executeScript("return document.getElementsByTagName('html')[0].getBoundingClientRect().height;").toString());
            for (String s : js) {
                driver_js.executeScript(s);
            }
            driver.manage().window().setSize(new Dimension(1366, height.intValue()));
            byte[] bytes = driver.findElement(By.tagName("html")).getScreenshotAs(OutputType.BYTES);
            Zibenbot.logInfoStatic("Chrome Dirver switch to about:blank");
            bytes = ImgUtils.compress(bytes, "png");
            IOUtil.saveFileWithBytes(outFileName, bytes);
        } catch (IOException | InterruptedException e) {
            throw e;
        } finally {
            //释放资源
            SeleniumUtils.closeDriver(driver);
        }
        return new File(outFileName);
    }

    public static File getScreenshot(String url, String outFileName, int timeOut) throws IOException, InterruptedException {
        WebDriver driver = SeleniumUtils.getDriver();
        return getScreenshot(driver, url, outFileName, timeOut);
    }

    public static File getScreenshot(WebDriver driver, String url, String outFileName, int timeOut) throws IOException, InterruptedException {
        return getScreenshot(driver, url, outFileName, timeOut, new String[]{});
    }

    public static void tryCloseDriverForcibly(WebDriver driver) {
        Integer port = -1;
        Integer pid = -1;
        try {
            port = ReflectUtils.getPrivateValueIncludeSuper(driver, "executor.remoteServer", URL.class).getPort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (port != -1) {
                pid = ProcessUtils.getPidWithPort(port);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (pid != -1) {
                ProcessUtils.killProcessWithPid(pid);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver.close();
        driver.quit();
    }

}
