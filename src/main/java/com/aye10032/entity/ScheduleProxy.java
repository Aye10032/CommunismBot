package com.aye10032.entity;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ExceptionUtils;
import com.google.gson.internal.$Gson$Preconditions;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2022/9/4 14:13
 **/
public class ScheduleProxy extends QuartzJobBean {

    private static Map<String, QuartzJobBean> realBeanMap = new ConcurrentHashMap<>();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            realBeanMap.get(context.getJobDetail().getKey().getName()).execute(context);
        } catch (Throwable e) {
            Zibenbot.logErrorStatic("运行定时任务失败：" + ExceptionUtils.printStack(e));
        }
    }

    public static void putRealBean(String key, QuartzJobBean quartzJobBean) {
        realBeanMap.put(key, quartzJobBean);
    }
}
