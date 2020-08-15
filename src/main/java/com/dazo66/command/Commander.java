package com.dazo66.command;

import com.dazo66.command.exceptions.CommandRuntimeException;
import com.dazo66.command.exceptions.IfNotRuntiomeException;
import com.dazo66.command.exceptions.RedundantParametersException;
import com.dazo66.command.interfaces.ExceptionHandler;
import com.dazo66.command.interfaces.ICommand;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;


/**
 * 命令器的实现类，由{@link CommanderBuilder}进行构建
 * 运行命令时会根据创建好的piece进行判定
 *
 * @author Dazo66
 */
public class Commander<S extends ICommand> {

    private ExceptionHandler eHandler;
    private CommandPiece<S> piece;

    /**
     * 执行命令，多个空格视为一个空格，忽略前后空格
     * 短路寻找符合要求的分支
     * 如果没有分支符合要求，则返回最接近的分支，并执行错误回调
     *
     * 如果命令片段超过判定的路径则报超过异常
     *
     * math add 1 2
     * -> 3
     * math add x 2
     * -> wrong at d1
     * math add 1 x
     * -> wrong at d2
     * math add 1 2 3
     * -> more args nothing
     * math add
     * -> wrong at d1
     *
     * math sum 1 2 3 4...
     * -> 1+2+3+4+5...
     * math sum x
     * -> wrong at x
     *
     * @param s 要执行的命令
     */
    public void execute(S s) {
        List<String> list = Lists.newArrayList(s.getCommandPieces());
        or<S> or = findPiece(list, piece);
        List<CommandPiece<S>> patchs = getRoad(list, piece);
        if (or != null) {
            if (list.size() == patchs.size() || or.hasArrayCheck()) {
                try {
                    or.getRun().run(s);
                } catch (Exception e) {
                    eHandler.commandRuntimeExceptionCatch(new CommandRuntimeException(
                            "Exception on CommandRun", e));
                }
            }
        } else {
            if (patchs.size() > 0) {
                CommandPiece<S> p = patchs.get(patchs.size() - 1);
                if (patchs.size() < list.size()) {
                    List<or<S>> ors =
                            p.match(list.subList(patchs.size() - 1, list.size()).toArray(new String[]{}), eHandler);
                    if (ors.size() > 0) {
                        try {
                            throw new RedundantParametersException("Redundant Parameters " +
                                    "Exception");
                        } catch (RedundantParametersException e) {
                            eHandler.redundantParametersExceptionCatch(e);
                        }
                    } else if (p.getIfNot() != null) {
                        try {
                            p.getIfNot().run(s);
                        } catch (Exception e) {
                            eHandler.ifNotRunntimeExceptionCatch(new IfNotRuntiomeException(
                                    "Exception on ifnot", e));
                        }
                    }
                } else {
                    if (p.getIfNot() != null) {
                        try {
                            p.getIfNot().run(s);
                        } catch (Exception e) {
                            eHandler.ifNotRunntimeExceptionCatch(new IfNotRuntiomeException(
                                    "Exception on ifnot", e));
                        }
                    }
                }
            }
        }
    }

    protected or<S> findPiece(List<String> strings, CommandPiece<S> main) {
        if (strings.size() > 0) {
            List<or<S>> list = main.match(strings.toArray(new String[]{}), eHandler);
            for (or<S> or : list) {
                if (or.hasRunable()) {
                    if (or.hasArrayCheck()) {
                        return or;
                    } else if (!or.hasArrayCheck() && strings.size() == 1) {
                        return or;
                    }
                }
                if (or.hasPiece()) {
                    or<S> or1 = findPiece(strings.subList(1, strings.size()), or.getPiece());
                    if (or1 != null) {
                        return or1;
                    }
                }
            }
        }
        return null;
    }

    protected CommandPiece getPiece() {
        return piece;
    }

    protected ExceptionHandler geteHandler() {
        return eHandler;
    }

    protected List<CommandPiece<S>> getRoad(List<String> strings, CommandPiece<S> main) {
        return getRoad(new ArrayList<>(), strings, main);
    }

    private List<CommandPiece<S>> getRoad(List<CommandPiece<S>> ret, List<String> strings,
                                       CommandPiece<S> main) {
        List<List<CommandPiece<S>>> ls = new ArrayList<>();
        List<CommandPiece<S>> l = new ArrayList<>(ret);
        if (strings.size() > 0) {
            List<or<S>> list = main.match(strings.toArray(new String[]{}), eHandler);
            if (list.isEmpty()) {
                l.add(main);
                return l;
            } else {
                l.add(main);
            }
            ls.add(l);
            for (or<S> or : list) {
                if (or.hasRunable() && strings.size() == 1) {
                    return l;
                }
                if (or.hasPiece()) {
                    if (strings.size() == 1 && !or.hasArrayCheck()) {
                        l.add(or.getPiece());
                        return l;
                    } else if (or.hasArrayCheck()){
                        l.add(or.getPiece());
                        return l;
                    } else {
                        ls.add(getRoad(l, strings.subList(1, strings.size()), or.getPiece()));
                    }
                }
            }
            List<CommandPiece<S>> ps = new ArrayList<>();
            for (List<CommandPiece<S>> patches : ls) {
                if (patches.size() > ps.size()) {
                    ps = patches;
                }
                if (patches.size() > 0
                        && patches.size() == ps.size()
                        && patches.get(patches.size() - 1).getIfNot() != null
                        && ps.get(ps.size() - 1).getIfNot() == null) {
                    ps = patches;
                }
            }
            return ps;
        } else {

            //l.add(main);
        }
        return l;
    }


    protected void setPiece(CommandPiece<S> piece) {
        this.piece = piece;
    }

    protected void seteHandler(ExceptionHandler eHandler) {
        this.eHandler = eHandler;
    }

}
