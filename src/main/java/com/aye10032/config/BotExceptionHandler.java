package com.aye10032.config;

import com.aye10032.foundation.entity.dto.Result;
import com.aye10032.foundation.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2023/8/14 0:19
 **/
@ControllerAdvice
@Slf4j
public class BotExceptionHandler {


    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandler(Exception e){
        log.error("出现异常：" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
        return new Result<>("400", ExceptionUtils.printStack(e), null);
    }

}
