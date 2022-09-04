package com.aye10032.entity;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ExceptionUtils;
import com.google.gson.internal.$Gson$Preconditions;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2022/9/4 14:13
 **/
public class ScheduleProxy extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            ((QuartzJobBean) context.getJobDetail().getJobDataMap().get("realBean")).execute(context);
        } catch (Throwable e) {
            Zibenbot.logErrorStatic("运行定时任务失败：" + ExceptionUtils.printStack(e));
        }
    }
}
