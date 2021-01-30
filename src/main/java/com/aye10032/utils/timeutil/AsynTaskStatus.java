package com.aye10032.utils.timeutil;

import org.intellij.lang.annotations.MagicConstant;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Dazo66
 */
public class AsynTaskStatus {

    public final static int INIT = 0;
    public final static int TASKS_RUNNING = 1;
    public final static int CALL_BACK_RUNNING = 2;
    public final static int CALL_BACK_RUNNED = 3;

    private AtomicInteger status = new AtomicInteger(0);
    private CountDownLatch latch;

    public AsynTaskStatus(CountDownLatch latch) {
        this.latch = latch;
    }

    public int getStatus() {
        return status.get();
    }

    public void setStatus(@MagicConstant(intValues = {INIT, TASKS_RUNNING, CALL_BACK_RUNNED, CALL_BACK_RUNNING}) int status) {
        this.status.set(status);
    }

    public synchronized void await() throws InterruptedException {
        latch.await();
    }

    protected long getCount() {
        return latch.getCount();
    }

    protected void countDown() {
        latch.countDown();
    }

}
