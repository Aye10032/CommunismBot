package com.aye10032.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Dazo66
 */
public class IOUtil {

    public static void saveFileWithBytes(File file, byte[] bytes) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.flush();
        fileOutputStream.close();

    }

    public static void saveFileWithBytes(String file, byte[] bytes) throws IOException {
        saveFileWithBytes(new File(file), bytes);
    }

}
