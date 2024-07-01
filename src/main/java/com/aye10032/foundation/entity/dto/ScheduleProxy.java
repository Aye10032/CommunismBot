package com.aye10032.foundation.entity.dto;

import com.aye10032.foundation.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dazo66(sundazhong.sdz)
 * @date 2022/9/4 14:13
 **/
@Slf4j
public class ScheduleProxy extends QuartzJobBean {

    private static Map<String, QuartzJobBean> realBeanMap = new ConcurrentHashMap<>();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            QuartzJobBean quartzJobBean = realBeanMap.get(context.getJobDetail().getKey().getName());
            if (quartzJobBean == null) {
                log.error("找不到任务：{}，可能是未加载", context.getJobDetail().getKey().getName());
                return;
            }
            quartzJobBean.execute(context);
        } catch (Throwable e) {
            log.info("运行定时任务失败：" + ExceptionUtils.printStack(e));
        }
    }

    public static void putRealBean(String key, QuartzJobBean quartzJobBean) {
        realBeanMap.put(key, quartzJobBean);
    }
}
