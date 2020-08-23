package com.dazo66.command.interfaces;

import com.dazo66.command.exceptions.RedundantParametersException;

/**
 * 异常处理器
 * @author Dazo66
 */
public interface ExceptionHandler {

    void checkExceptionCetch(Exception e);

    void commandRuntimeExceptionCatch(Exception e);

    void ifNotRunntimeExceptionCatch(Exception e);

    void redundantParametersExceptionCatch(RedundantParametersException e);

}
