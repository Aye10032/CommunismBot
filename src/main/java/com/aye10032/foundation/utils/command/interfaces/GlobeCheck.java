package com.aye10032.foundation.utils.command.interfaces;

public interface GlobeCheck<S extends ICommand> {

    boolean check(S s);

}
