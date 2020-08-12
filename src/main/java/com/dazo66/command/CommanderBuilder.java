package com.dazo66.command;

import com.dazo66.command.exceptions.CheckException;
import com.dazo66.command.exceptions.CommandRuntimeException;
import com.dazo66.command.exceptions.IfNotRuntiomeException;
import com.dazo66.command.exceptions.RedundantParametersException;
import com.dazo66.command.interfaces.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Stack;

/**
 * {@link Commander} 的构建类
 * 通过流式设置进行构建。
 * 具体使用范例：
 * Commander commander1 = new CommanderBuilder()
 *                              .start()
 *                              .next()
 *                                    .or("test"::equals)
 *                                    .next()
 *                                         .or("print"::equals)
 *                                         .next()
 *                                             .or((s)->true)
 *                                             .run((strings) -> System.out.println(strings[2]))
 *                                             .pop()
 *                                         .pop()
 *                                     .pop()
 *                              .build();
 * @author Dazo66
 */
public class CommanderBuilder<S extends ICommand> {

    private static final ExceptionHandler JUST_PRINT = new ExceptionHandler() {
        @Override
        public void checkExceptionCetch(CheckException e) {
            System.out.println(ExceptionUtils.getStackTrace(e.getCause()));
        }

        @Override
        public void commandRuntimeExceptionCatch(CommandRuntimeException e) {
            System.out.println(ExceptionUtils.getStackTrace(e.getCause()));
        }

        @Override
        public void ifNotRunntimeExceptionCatch(IfNotRuntiomeException e) {
            System.out.println(ExceptionUtils.getStackTrace(e.getCause()));
        }

        @Override
        public void redundantParametersExceptionCatch(RedundantParametersException e) {
            System.out.println(ExceptionUtils.getStackTrace(e.getCause()));
        }
    };
    private ExceptionHandler eHandler = JUST_PRINT;
    private Stack<CommandPiece<S>> stack = new Stack<>();
    private CommandPiece<S> main = new CommandPiece<>();
    private CommandPiece<S> current;
    private or<S> currentOr;

    /**
     * 设置异常处理器
     * 当发生异常时会交由异常处理器处理
     * @param eHandler 异常处理器
     * @return this
     */
    public CommanderBuilder<S> seteHandler(ExceptionHandler eHandler) {
        this.eHandler = eHandler;
        return this;
    }

    /**
     * 开始构建，在开始之前一定要运行此方法，进行初始化
     * @return this
     */
    public CommanderBuilder<S> start() {
        current = main;
        stack.push(current);
        return this;
    }

    /**
     * 设置当前分支的运行器
     * @param run 当命令刚好触发时会呼叫
     * @return this
     */
    public CommanderBuilder<S> run(CommandRun<S> run){
        currentOr.setRun(run);
        return this;
    }

    /**
     * 在当前深度下创建一个全新的分支
     * 此分支只对单个字段响应
     * @param b 检查器 运行命令到此深度的时候会触发
     * @return this
     */
    public CommanderBuilder<S> or(PieceCheck b) {
        current.addOr(currentOr = new or<>(b));
        return this;
    }

    /**
     * 往下扩展一个深度
     * 如果当前分支是数组分支 则无法扩展
     * @return this
     */
    public CommanderBuilder<S> next(){
        if (currentOr.hasArrayCheck()) {
            //todo
        } else {
            current = new CommandPiece<S>();
            stack.push(current);
            currentOr.setPiece(current);
            currentOr = null;
        }
        return this;
    }

    /**
     * 在当前深度下创建一个全新的数组分支
     * 此分支会对之后所有的字段进行判断
     * @param check 数组分支检查器
     * @return this
     */
    public CommanderBuilder<S> orArray(ArrayCheck check) {
        current.addOr(currentOr = new or<>(null));
        currentOr.setArrayCheck(check);
        return this;
    }

    /**
     * 往上跳出一个深度
     * @return this
     */
    public CommanderBuilder<S> pop(){
        stack.pop();
        current = stack.peek();
        currentOr = current.getOrs().get(current.getOrs().size() - 1);
        return this;
    }

    /**
     * 设置当前深度的判定失败的回调
     * @param runnable 如果当前深度全部判定失败 则运行
     * @return this
     */
    public CommanderBuilder<S> ifNot(CommandRun<S> runnable){
        current.setIfNot(runnable);
        return this;
    }

    /**
     * 以当前的状态进行默认构建
     * @return Commander
     */
    public Commander<S> build(){
        Commander<S> ret = new Commander<>();
        ret.seteHandler(eHandler);
        ret.setPiece(main);
        return ret;
    }

    /**
     * 以当前的状态根据传入factory进行构建的构建
     * @param factory Commander的工厂类 用来创建空白的Commander对象
     * @return this
     */
    public Commander<S> build(CommanderFactory<S> factory){
        Commander<S> ret = factory.build();
        ret.seteHandler(eHandler);
        ret.setPiece(main);
        return ret;
    }
}
