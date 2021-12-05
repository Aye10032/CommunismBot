package com.aye10032.timetask;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ExceptionUtils;
import com.aye10032.utils.fund.FundingDetail;
import com.aye10032.utils.fund.FundingUtils;
import com.aye10032.utils.timeutil.Reciver;
import com.aye10032.utils.timeutil.SubscribableBase;
import com.aye10032.utils.timeutil.TimeUtils;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Dazo66
 */
public class FundTask extends SubscribableBase {
    public FundTask(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public String getName() {
        return "基金日报";
    }

    @Override
    public Date getNextTime(Date date) {
        return TimeUtils.getMin(TimeUtils.getNextSpecialTime(date, -1, -1, 11, 31, 0, 0),
            TimeUtils.getNextSpecialTime(date, -1, -1, 15, 1, 0, 0),
            TimeUtils.getNextSpecialTime(date, -1, -1, 21, 0, 0, 0));
    }

    @Override
    public void run(List<Reciver> recivers, String[] args) {
        if (ArrayUtils.isEmpty(args)) {
            replyAll(recivers, "订阅的基金为空");
        }
        StringBuilder builder = new StringBuilder();
        builder.append("今天的基金日报为:\n");
        String today = TimeUtils.getDateString(System.currentTimeMillis());
        boolean hasUpdate = false;
        for (String s : args) {
            try {
                FundingDetail status = FundingUtils.getFundingStatus(s, today, Zibenbot.getOkHttpClient());
                if (status.getExpectWorthDate() != null
                    && status.getExpectWorthDate().startsWith(today)) {
                    hasUpdate = true;
                    if (status.getNetWorthDate().equals(today)) {
                        builder.append(String.format("  %s: %s%%\n", status.getName(), status.getDayGrowth()));
                    } else {
                        builder.append(String.format("  %s: 预%s%%\n", status.getName(), status.getExpectGrowth()));
                    }
                } else {
                    builder.append(String.format("  %s %s: %s \n", status.getName(), status.getNetWorthDate(), status.getDayGrowth()));
                }
            } catch (Exception e) {
                Zibenbot.logInfoStatic(ExceptionUtils.printStack(e));
                builder.append(String.format("  %s (读取异常)\n", s));
            }
        }
        if (hasUpdate) {
            replyAll(recivers, builder.subSequence(0, builder.length() - 1).toString());
        }
    }

    @Override
    public Pair<Boolean, String> argsCheck(String[] args) {
        if (ArrayUtils.isEmpty(args)) {
            return new Pair<>(false, "订阅的基金为空");
        }
        return new Pair<>(Arrays.stream(args).allMatch(s -> {
            try {
                FundingUtils.getFundingStatus(s, TimeUtils.getDateString(System.currentTimeMillis()), Zibenbot.getOkHttpClient());
            } catch (Exception e) {
                return false;
            }
            return true;
        }), "请输入合法的基金编码");
    }
}
