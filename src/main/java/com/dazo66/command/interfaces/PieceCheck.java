package com.dazo66.command.interfaces;

/**
 * 单位片段检查器
 * @author Dazo66
 */
@FunctionalInterface
public interface PieceCheck {
    boolean check(String piece);
}
