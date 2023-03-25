package com.aye10032.foundation.utils.command.interfaces;

import com.aye10032.foundation.utils.command.Commander;

/**
 * Commander 的工厂类
 * 用于创建一个Commander
 *
 * @author Dazo66
 */
@FunctionalInterface
public interface CommanderFactory<S extends ICommand> {
    Commander<S> build();

}
