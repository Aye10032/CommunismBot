package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Utils.IOUtil;
import com.aye10032.Utils.ImgUtils;
import com.aye10032.Utils.SeleniumUtils;
import com.aye10032.Zibenbot;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;

/**
 * @author Dazo66
 */
public class ScreenshotFunc extends BaseFunc {

    static String BLANK = " ";

    public ScreenshotFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    static {

    }

    @Override
    public void setUp() {
        File dir = new File(zibenbot.appDirectory + "/screenshot");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //清理缓存
        long current = System.currentTimeMillis();
        int i = 0;
        for (File f: dir.listFiles()) {
            if (f.isFile() && current - f.lastModified() > 86400 * 3 * 1000) {
                f.delete();
                i++;
            }
        }
        Zibenbot.logger.info("清理了三天前的缓存 " + i + " 张。");
    }

    @Override
    public void run(SimpleMsg CQmsg) {
        String msg = CQmsg.getMsg();
        if (msg.startsWith("网页快照") || msg.startsWith(".网页快照")){
            msg = msg.replaceAll(" +", " ");
            String[] args = msg.split(" ");
            zibenbot.pool.timeoutEvent(1, () -> {
                try {
                    if (args.length == 3) {
                        replyMsg(CQmsg, zibenbot.getImg(getScreenshot(args[1], Integer.parseInt(args[2]))));
                    } else if (args.length == 2) {
                        replyMsg(CQmsg, zibenbot.getImg(getScreenshot(args[1], 4000)));
                    } else {
                        replyMsg(CQmsg, "参数异常，Example：网页快照 [url] [timeout]");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    replyMsg(CQmsg, "获取网页快照失败，可能是网页不支持" + e);
                }
            });

        }

    }

    public File getScreenshot(String url, int timeOut) throws IOException, InterruptedException {
        String outFileName = getOutFileName(url);
        return getScreenshot(url, outFileName, timeOut);
    }

    public static File getScreenshot(String url, String outFileName,int timeOut) throws IOException, InterruptedException {
        WebDriver driver = SeleniumUtils.getDriver();
        return getScreenshot(driver, url, outFileName, timeOut);
    }

    public static File getScreenshot(WebDriver driver, String url, String outFileName,int timeOut) throws IOException, InterruptedException {
        return getScreenshot(driver, url, outFileName, timeOut, new String[]{});
    }

    public synchronized static File getScreenshot(WebDriver driver, String url, String outFileName, int timeOut, String... js) throws IOException, InterruptedException {
        try {
            driver.get(url = addhttp(url));
            Zibenbot.logger.info("Chrome Dirver switch to" + url);
            Thread.sleep(timeOut);
            JavascriptExecutor driver_js= SeleniumUtils.getDriverJs(driver);
            //Double width = Double.valueOf(driver_js.executeScript(
            //        "return document.getElementsByTagName('html')[0].getBoundingClientRect().width;").toString());
            Double height = Double.valueOf(driver_js.executeScript("return document.getElementsByTagName('html')[0].getBoundingClientRect().height;").toString());
            for (String s : js) {
                driver_js.executeScript(s);
            }
            driver.manage().window().setSize(new Dimension(1366, height.intValue()));
            byte[] bytes = driver.findElement(By.tagName("html")).getScreenshotAs(OutputType.BYTES);
            Zibenbot.logger.info("Chrome Dirver switch to about:blank");
            bytes = ImgUtils.compress(bytes, "png");
            IOUtil.saveFileWithBytes(outFileName, bytes);
        } catch (IOException | InterruptedException e) {
            throw e;
        } finally {
            //释放资源
            driver.get("about:blank");
        }
        return new File(outFileName);
    }

    private static String addhttp(String url) {
        if (url.startsWith("http")) {
            return url;
        } else {
            return "https://" + url;
        }
    }

    public String getOutFileName(String url){
        return zibenbot.appDirectory + "\\screenshot\\" + url.hashCode() + ".jpg";
    }
}
