package com.dazo66.command.interfaces;

/**
 * 数组分支检查器
 * @author Dazo66
 */
@FunctionalInterface
public interface ArrayCheck {
    /**
     * 检查传入的数据是否符合要求
     * @param strings 要判定的数据
     * @return boolean
     */
    boolean arrayCheck(String[] strings);

}
