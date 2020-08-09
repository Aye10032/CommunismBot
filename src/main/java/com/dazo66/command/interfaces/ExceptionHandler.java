package com.dazo66.command.interfaces;

import com.dazo66.command.exceptions.CheckException;
import com.dazo66.command.exceptions.CommandRuntimeException;
import com.dazo66.command.exceptions.IfNotRuntiomeException;
import com.dazo66.command.exceptions.RedundantParametersException;

/**
 * 异常处理器
 * @author Dazo66
 */
public interface ExceptionHandler {

    void checkExceptionCetch(CheckException e);

    void commandRuntimeExceptionCatch(CommandRuntimeException e);

    void ifNotRunntimeExceptionCatch(IfNotRuntiomeException e);

    void redundantParametersExceptionCatch(RedundantParametersException e);

}
