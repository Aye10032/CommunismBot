package com.aye10032.Functions.funcutil;

import com.aye10032.Utils.ExceptionUtils;
import com.aye10032.Zibenbot;
import com.dazo66.command.exceptions.RedundantParametersException;
import com.dazo66.command.interfaces.ExceptionHandler;

/**
 * @author Dazo66
 */
public class FuncExceptionHandler implements ExceptionHandler {

    public static final FuncExceptionHandler INSTENCE = new FuncExceptionHandler();

    @Override
    public void checkExceptionCetch(Exception e) {
        Zibenbot.logger.warning(ExceptionUtils.printStack(e));
    }

    @Override
    public void commandRuntimeExceptionCatch(Exception e) {
        Zibenbot.logger.warning(ExceptionUtils.printStack(e));
    }

    @Override
    public void ifNotRunntimeExceptionCatch(Exception e) {
        Zibenbot.logger.warning(ExceptionUtils.printStack(e));
    }

    @Override
    public void redundantParametersExceptionCatch(RedundantParametersException e) {
        Zibenbot.logger.warning(ExceptionUtils.printStack(e));
    }
}
