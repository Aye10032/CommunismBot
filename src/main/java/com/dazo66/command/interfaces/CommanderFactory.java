package com.dazo66.command.interfaces;

import com.dazo66.command.Commander;

/**
 * Commander 的工厂类
 * 用于创建一个Commander
 * @author Dazo66
 */
@FunctionalInterface
public interface CommanderFactory<S extends ICommand> {
    Commander<S> build();

}
