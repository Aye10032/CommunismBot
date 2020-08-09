package com.dazo66.command;

import com.dazo66.command.interfaces.ArrayCheck;
import com.dazo66.command.interfaces.CommandRun;
import com.dazo66.command.interfaces.ICommand;
import com.dazo66.command.interfaces.PieceCheck;


/**
 * 命令分支 其下可能有可运行的或者是指向下一个深度的命令片
 * @author Dazo66
 */
public class or<S extends ICommand> {

    private PieceCheck pieceCheck;
    private ArrayCheck arrayCheck;
    private CommandRun<S> run;
    private CommandPiece<S> piece;

    public void setArrayCheck(ArrayCheck arrayCheck) {
        if (pieceCheck != null) {
            pieceCheck = null;
        }
        this.arrayCheck = arrayCheck;
    }

    public boolean hasArrayCheck(){
        return arrayCheck != null;
    }

    public ArrayCheck getArrayCheck() {
        return arrayCheck;
    }

    public void setPieceCheck(PieceCheck pieceCheck) {
        if (arrayCheck != null) {
            arrayCheck = null;
        }
        this.pieceCheck = pieceCheck;
    }

    public CommandRun<S> getRun() {
        return run;
    }

    public PieceCheck getPieceCheck() {
        return pieceCheck;
    }

    public or(PieceCheck pieceCheck) {
        this.pieceCheck = pieceCheck;
    }

    public void setRun(CommandRun<S> run) {
        this.run = run;
    }

    public void setPiece(CommandPiece<S> piece) {
        this.piece = piece;
    }

    public CommandPiece<S> getPiece() {
        return piece;
    }

    public boolean hasPiece() {
        return getPiece() != null;
    }

    public boolean match(String[] s) {
        if (pieceCheck != null) {
            return pieceCheck.check(s[0]);
        } else {
            return arrayCheck.arrayCheck(s);
        }
    }

    public boolean hasRunable(){
        return run != null;
    }

    public void run(S command){
        run.run(command);
    }

}
