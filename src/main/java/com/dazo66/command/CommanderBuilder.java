package com.dazo66.command;

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
        public void checkExceptionCetch(Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }

        @Override
        public void commandRuntimeExceptionCatch(Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }

        @Override
        public void ifNotRunntimeExceptionCatch(Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }

        @Override
        public void redundantParametersExceptionCatch(RedundantParametersException e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    };
    private ExceptionHandler eHandler = JUST_PRINT;
    private Stack<CommandPiece<S>> stack = new Stack<>();
    private CommandPiece<S> main = new CommandPiece<>();
    private CommandPiece<S> current;
    private or<S> currentOr;
    private GlobeCheck<S> globeCheck;
    private CommandRun<S> callBack;

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
     * 传入命令的全局检查方法，在每次匹配后运行前会执行此方法进行检查
     * 开始构建，在开始之前一定要运行此方法，进行初始化
     * @param globeCheck 全局检查参数 回在匹配后进行检查，返回false则取消执行
     * @return this
     */
    public CommanderBuilder<S> start(GlobeCheck<S> globeCheck) {
        return start(globeCheck, null);
    }

    /**
     * 传入命令的全局检查方法，在每次匹配后运行前会执行此方法进行检查
     * 开始构建，在开始之前一定要运行此方法，进行初始化
     * @param globeCheck 全局检查参数 回在匹配后进行检查，返回false则取消执行
     * @param callBack 全局检查函数返回false后的回调
     * @return this
     */
    public CommanderBuilder<S> start(GlobeCheck<S> globeCheck, CommandRun<S> callBack) {
        current = main;
        stack.push(current);
        this.globeCheck = globeCheck;
        this.callBack = callBack;
        return this;
    }

    public CommanderBuilder<S> setGlobeCheck(GlobeCheck<S> globeCheck) {
        this.globeCheck = globeCheck;
        return this;
    }

    public CommanderBuilder<S> setGlobeCheckCallBack(CommandRun<S> callBack) {
        this.callBack = callBack;
        return this;
    }

    /**
     * 开始构建，在开始之前一定要运行此方法，进行初始化
     * @return this
     */
    public CommanderBuilder<S> start() {
        return start(s -> true);
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
        return _build(ret);
    }

    /**
     * 以当前的状态根据传入factory进行构建的构建
     * @param factory Commander的工厂类 用来创建空白的Commander对象
     * @return this
     */
    public Commander<S> build(CommanderFactory<S> factory){
        Commander<S> ret = factory.build();
        return _build(ret);
    }

    private Commander<S> _build(Commander<S> ret){
        ret.seteHandler(eHandler);
        ret.setPiece(main);
        ret.setGlobeCheck(globeCheck);
        ret.setCallback(callBack);
        return ret;
    }
}
