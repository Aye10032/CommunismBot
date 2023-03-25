package com.aye10032.bot.func.funcutil;

import com.aye10032.foundation.utils.ExceptionUtils;
import com.aye10032.foundation.utils.command.exceptions.RedundantParametersException;
import com.aye10032.foundation.utils.command.interfaces.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Dazo66
 */
@Slf4j
public class FuncExceptionHandler implements ExceptionHandler {

    public static final FuncExceptionHandler INSTENCE = new FuncExceptionHandler();

    @Override
    public void checkExceptionCetch(Exception e) {
        log.error(ExceptionUtils.printStack(e));
    }

    @Override
    public void commandRuntimeExceptionCatch(Exception e) {
        log.error(ExceptionUtils.printStack(e));
    }

    @Override
    public void ifNotRunntimeExceptionCatch(Exception e) {
        log.error(ExceptionUtils.printStack(e));
    }

    @Override
    public void redundantParametersExceptionCatch(RedundantParametersException e) {
        log.error(ExceptionUtils.printStack(e));
    }
}
