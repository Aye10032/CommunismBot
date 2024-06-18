package com.aye10032.bot.func;

import com.aye10032.bot.Zibenbot;
import com.aye10032.bot.func.funcutil.BaseFunc;
import com.aye10032.bot.func.funcutil.FuncExceptionHandler;
import com.aye10032.bot.func.funcutil.SimpleMsg;
import com.aye10032.foundation.utils.command.Commander;
import com.aye10032.foundation.utils.command.CommanderBuilder;
import org.springframework.stereotype.Service;


@Service
public class ImasCalFunc extends BaseFunc {


    private Commander<SimpleMsg> commander;

    public ImasCalFunc(Zibenbot zibenbot) {
        super(zibenbot);

        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".算分"::equals)
                .next()
                .orArray(s -> true)
                .run((cqmsg) -> {
                    String[] strings = cqmsg.getCommandPieces();

                    int vo = Integer.parseInt(strings[1]);
                    int da = Integer.parseInt(strings[2]);
                    int vi = Integer.parseInt(strings[3]);

                    vo = (vo < 1470) ? vo + 30 : 1500;
                    da = (da < 1470) ? da + 30 : 1500;
                    vi = (vi < 1470) ? vi + 30 : 1500;

                    int statusPoint = (vo + da + vi) * 23 / 10;
                    int orderPoint = 1700;

                    int requiredScorePointAPlus = 11500 - orderPoint - statusPoint;
                    int requiredScorePointS = 13000 - orderPoint - statusPoint;

                    int requiredScoreAPlus = CalculateScore(requiredScorePointAPlus);
                    int requiredScoreS = CalculateScore(requiredScorePointS);

                    zibenbot.replyMsg(cqmsg, "A+ 所需得分: " + requiredScoreAPlus + "\r\nS 所需得分: " + requiredScoreS);

                }).build();
    }

    private static int CalculateScore(int requiredPoint) {

        int result;

        if (requiredPoint <= 1500) {
            result = (int) (requiredPoint / 0.3);
        } else if (requiredPoint <= 2250) {
            result = 5000 + (int) Math.round((requiredPoint - 1500) / 0.15);
        } else if (requiredPoint <= 3050) {
            result = 10000 + (int) Math.round((requiredPoint - 2250) / 0.08);
        } else if (requiredPoint <= 3450) {
            result = 20000 + (int) Math.round((requiredPoint - 3050) / 0.04);
        } else if (requiredPoint <= 3650) {
            result = 30000 + (int) Math.round((requiredPoint - 3450) / 0.02);
        } else {
            result = 40000 + (int) Math.round((requiredPoint - 3650) / 0.01);
        }
        return result;
    }


    @Override
    public void setUp() {
    }

    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }
}
