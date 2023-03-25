package com.dazo66.command.interfaces;

import com.dazo66.command.exceptions.RedundantParametersException;

/**
 * 异常处理器
 * @author Dazo66
 */
public interface ExceptionHandler {

    /**
     * 检查异常处理器
     * @param e
     */
    void checkExceptionCetch(Exception e);

    /**
     * 指令运行时异常处理
     * @param e
     */
    void commandRuntimeExceptionCatch(Exception e);

    /**
     * if not 运行时处理
     * @param e
     */
    void ifNotRunntimeExceptionCatch(Exception e);

    /**
     * 命令执行时参数过多处理
     * @param e
     */
    void redundantParametersExceptionCatch(RedundantParametersException e);

}
