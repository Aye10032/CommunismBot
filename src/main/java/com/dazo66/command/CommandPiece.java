package com.dazo66.command;

import com.dazo66.command.exceptions.CheckException;
import com.dazo66.command.interfaces.CommandRun;
import com.dazo66.command.interfaces.ExceptionHandler;
import com.dazo66.command.interfaces.ICommand;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令片，代表一个命令深度，可以包含多个分支
 * @author Dazo66
 */
public class CommandPiece<S extends ICommand> {

    private List<or<S>> ors = new ArrayList<>();
    private CommandRun<S> ifNot;


    public void setOrs(List<or<S>> ors) {
        this.ors = ors;
    }

    public void addOr(or<S> or){
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
                handler.checkExceptionCetch(new CheckException("Check Exception", e));
            }
        }
        return r;
    }
}
