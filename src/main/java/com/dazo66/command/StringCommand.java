package com.dazo66.command;

import com.dazo66.command.interfaces.ICommand;

public class StringCommand implements ICommand {

    private String command;

    public StringCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String[] getCommandPieces() {
        return command.trim().replaceAll(" +", " ").split(" ");
    }

    public static StringCommand of(String command) {
        return new StringCommand(command);
    }

}
