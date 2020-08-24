package com.dazo66.command.interfaces;

public interface GlobeCheck<S extends ICommand> {

    boolean check(S s);

}
