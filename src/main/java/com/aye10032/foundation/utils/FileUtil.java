package com.aye10032.foundation.utils;

import com.aye10032.foundation.entity.base.FileData;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.aye10032.foundation.utils.RandomUtil.getRandomIndex;

/**
 * @program: communismbot
 * @description: 文件相关工具类
 * @author: Aye10032
 * @create: 2024-01-09 14:13
 **/

@Slf4j
public class FileUtil {
    public static FileData getRandomImage(List<String> fileList, String basePath) {
        int index = getRandomIndex(fileList.size());
        Path filePath = Paths.get(basePath, fileList.get(index));
        log.info("获取到路径：" + filePath);
        File image = filePath.toFile();
        if (image.exists()) {
            return new FileData(image, fileList);
        } else {
            log.info("图片不存在，尝试新的路径");
            fileList.remove(index);
            return getRandomImage(fileList, basePath);
        }
    }

    public static List<String> getFileIndex(String index_path) {
        List<String> fileIndex = new ArrayList<>();
        File image_index = new File(index_path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(image_index));
            String fileName = null;
            while ((fileName = reader.readLine()) != null) {
                fileIndex.add(fileName);
            }
            reader.close();

            return fileIndex;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveFileList(List<String> fileList, String indexPath) {
        File imageIndex = new File(indexPath);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(imageIndex));
            for (String name : fileList) {
                writer.write(name + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
