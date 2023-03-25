package com.aye10032.foundation.utils.command;

import com.aye10032.foundation.utils.command.interfaces.CommandRun;
import com.aye10032.foundation.utils.command.interfaces.ExceptionHandler;
import com.aye10032.foundation.utils.command.interfaces.ICommand;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令片，代表一个命令深度，可以包含多个分支
 *
 * @author Dazo66
 */
public class CommandPiece<S extends ICommand> {

    private List<or<S>> ors = new ArrayList<>();
    private CommandRun<S> ifNot;


    public void setOrs(List<or<S>> ors) {
        this.ors = ors;
    }

    public void addOr(or<S> or) {
        ors.add(or);
    }

    public List<or<S>> getOrs() {
        return ors;
    }

    public CommandRun<S> getIfNot() {
        return ifNot;
    }

    public void setIfNot(CommandRun<S> ifNot) {
        this.ifNot = ifNot;
    }

    protected List<or<S>> match(String[] patch, ExceptionHandler handler) {
        List<or<S>> r = new ArrayList<>();
        for (or<S> or : ors) {
            try {
                if (or.match(patch)) {
                    r.add(or);
                }
            } catch (Exception e) {
                handler.checkExceptionCetch(e);
            }
        }
        return r;
    }
}
