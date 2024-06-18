package com.aye10032.foundation.utils;

/**
 * @author Dazo66
 */
@FunctionalInterface
public interface IMsgUpload {

    /**
     * 上传的方法
     *
     * @param contact 上传的对象
     * @param source  上传的资源
     * @return
     */
    String upload(String source) throws Exception;

}
