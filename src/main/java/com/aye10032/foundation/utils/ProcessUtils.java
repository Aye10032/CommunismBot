package com.aye10032.foundation.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author Dazo66
 */
public class ProcessUtils {
    private ProcessUtils() {
    }

    public static Integer getPidWithPort(Integer port) throws IOException, InterruptedException {
        Process p1 = null;
        if (System.getProperty("os.name").contains("indows")) {
            p1 = Runtime.getRuntime().exec(String.format("cmd.exe /c netstat -ano|findstr \":%d\"", port));
        } else if (System.getProperty("os.name").contains("linux")) {
            throw new RuntimeException("这个方法只能用在window上");
        }
        if (p1 != null) {
            p1.waitFor();
            List<String> list = IOUtils.readLines(p1.getInputStream());
            if (list.size() > 0) {
                String s = list.get(0);
                String[] strings = s.split(" +");
                return Integer.valueOf(strings[strings.length - 1]);
            }
        }
        if (p1 != null && p1.isAlive()) {
            p1.destroyForcibly();
        }
        return -1;
    }

    public static void killProcessWithPid(Integer pid) throws InterruptedException, IOException {
        Process p1 = null;
        try {
            if (System.getProperty("os.name").contains("indows")) {
                p1 = Runtime.getRuntime().exec(String.format("taskkill /pid %d /F /T", pid));
            } else if (System.getProperty("os.name").contains("linux")) {
                p1 = Runtime.getRuntime().exec(String.format("kill -9 %d", pid));
            }
        } catch (Exception e) {
        } finally {
            if (p1 != null) {
                p1.waitFor();
                System.out.println((IOUtils.toString(p1.getInputStream(), "GBK")));
            }
        }
    }

}
