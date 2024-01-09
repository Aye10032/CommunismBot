package com.aye10032.foundation.entity.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

/**
 * @program: communismbot
 * @description: 随机文件实体类
 * @author: Aye10032
 * @create: 2024-01-09 15:01
 **/

@AllArgsConstructor
@Getter
@Setter
public class FileData {
    private File file;
    private List<String> indexList;
}
