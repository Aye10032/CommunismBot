package com.aye10032.config;

import com.aye10032.foundation.entity.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dazo
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Result<?> exceptionHandler(Exception e){
        log.error("出现异常：" + ExceptionUtils.getStackTrace(e));
        return new Result<>("500", "服务器异常", null);
    }
}

