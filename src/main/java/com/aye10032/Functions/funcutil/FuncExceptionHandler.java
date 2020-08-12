package com.aye10032.Functions.funcutil;

import com.aye10032.Utils.ExceptionUtils;
import com.aye10032.Zibenbot;
import com.dazo66.command.exceptions.CheckException;
import com.dazo66.command.exceptions.CommandRuntimeException;
import com.dazo66.command.exceptions.IfNotRuntiomeException;
import com.dazo66.command.exceptions.RedundantParametersException;
import com.dazo66.command.interfaces.ExceptionHandler;

/**
 * @author Dazo66
 */
public class FuncExceptionHandler implements ExceptionHandler {

    public static final FuncExceptionHandler INSTENCE = new FuncExceptionHandler();

    @Override
    public void checkExceptionCetch(CheckException e) {
        Zibenbot.logger.warning(ExceptionUtils.printStack(e.getCause()));
    }

    @Override
    public void commandRuntimeExceptionCatch(CommandRuntimeException e) {
        Zibenbot.logger.warning(ExceptionUtils.printStack(e.getCause()));
    }

    @Override
    public void ifNotRunntimeExceptionCatch(IfNotRuntiomeException e) {
        Zibenbot.logger.warning(ExceptionUtils.printStack(e.getCause()));
    }

    @Override
    public void redundantParametersExceptionCatch(RedundantParametersException e) {
        Zibenbot.logger.warning(ExceptionUtils.printStack(e.getCause()));
    }
}
