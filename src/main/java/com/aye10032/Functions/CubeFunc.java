package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.FuncExceptionHandler;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;
import com.dazo66.command.Commander;
import com.dazo66.command.CommanderBuilder;
import com.dazo66.command.interfaces.PieceCheck;

import java.io.File;
import java.util.Random;
import java.util.Stack;

public class CubeFunc extends BaseFunc {

    private Stack<String> cubeStack = new Stack<String>();
    private String[] cubarr = new String[]{"F", "B", "U", "D", "R", "L"};
    private String[] statusarr = new String[]{"", "", "'", "'", "2"};
    private int step = 20;
    private String cuberandom = "";

    private Commander<SimpleMsg> commander;

/*    public static void main(String[] args) {
        System.out.println(new AyeCube().getCuberandom());
    }*/

    public CubeFunc(Zibenbot zibenbot) {
        super(zibenbot);
        commander = new CommanderBuilder<SimpleMsg>()
                .seteHandler(FuncExceptionHandler.INSTENCE)
                .start()
                .or(".cube"::equalsIgnoreCase)
                .run((cqmsg) -> {
                    zibenbot.replyMsg(cqmsg, funcInfo());
                })
                .or(".3"::equals)
                .run((cqmsg) -> {
                    newCuberandom();
                    zibenbot.replyMsg(cqmsg, getCuberandom());
                })
                .or(".cfop"::equalsIgnoreCase)
                .run((cqmsg) ->{
                    zibenbot.replyMsg(cqmsg,
                            zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\CFOP1.jpg"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\CFOP2.jpg"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\CFOP3.jpg"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\CFOP4.jpg")));
                })
                .next()
                    .or("f2l"::equalsIgnoreCase)
                    .run((cqmsg) ->{
                        zibenbot.replyMsg(cqmsg, zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\CFOP2.jpg")));
                    })
                    .or("oll"::equalsIgnoreCase)
                    .run((cqmsg) ->{
                        zibenbot.replyMsg(cqmsg, zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\CFOP3.jpg")));
                    })
                    .or("pll"::equalsIgnoreCase)
                    .run((cqmsg) ->{
                        zibenbot.replyMsg(cqmsg, zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\CFOP4.jpg")));
                    })
                .pop()
                .or(".mega"::equalsIgnoreCase)
                .run((cqmsg) ->{
                    zibenbot.replyMsg(cqmsg,
                            zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\Mega1.jpg"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\Mega2.jpg")));
                })
                .or(".22"::equals)
                .run((cqmsg) ->{
                    zibenbot.replyMsg(cqmsg,
                            zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\2X201.png"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\2X202.png"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\2X203.png")));
                })
                .or(".彳亍"::equals)
                .run((cqmsg) ->{
                    zibenbot.replyMsg(cqmsg,
                            zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu01.png"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu02.png"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu03.png"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu04.png"))
                                    + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu05.png")));
                })
                .next()
                    .or("编码"::equals)
                    .run((cqmsg) ->{
                        zibenbot.replyMsg(cqmsg,
                                zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu_bianma.png")));
                    })
                    .or("角"::equals)
                    .run((cqmsg) ->{
                        zibenbot.replyMsg(cqmsg,
                                zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu_jiao.png")));
                    })
                    .or("棱1"::equals)
                    .run((cqmsg) ->{
                        zibenbot.replyMsg(cqmsg,
                                zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu_leng.png")));
                    })
                    .or("棱2"::equals)
                    .run((cqmsg) ->{
                        zibenbot.replyMsg(cqmsg,
                                zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu_leng2.png")));
                    })
                    .or("翻棱"::equals)
                    .run((cqmsg) ->{
                        zibenbot.replyMsg(cqmsg,
                                zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu_fanleng.png")));
                    })
                    .or("翻角"::equals)
                    .run((cqmsg) ->{
                        zibenbot.replyMsg(cqmsg,
                                zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\cube\\chichu_fanjiao.png")));
                    })
                .pop()
                .build();
    }

    private String nextStep(Random random) {
        String temp = "";

        if (!cubeStack.empty()) {
            while (temp.equals("") || cubeStack.peek().contains(temp)) {
                temp = cubarr[random.nextInt(cubarr.length)];
            }
        } else {
            temp = cubarr[random.nextInt(cubarr.length)];
        }

        return temp;
    }

    private String funcInfo(){
        StringBuilder infoBuilder = new StringBuilder();

        infoBuilder
                .append(".cube -----功能一览\n")
                .append(".3 -----三阶打乱\n")
                .append(".CFOP [F2L|OLL|PLL] -----CFOP公式\n")
                .append(".22 -----二阶面先法公式\n")
                .append(".MEGA -----五魔方公式\n")
                .append(".彳亍 [编码|角|棱1|棱2|翻棱|翻角|编码] -----盲拧彳亍法公式，其中棱1是以DF为缓冲；棱2是以UF为缓冲块");

        return infoBuilder.toString();
    }

    private void newCuberandom() {
        Random random = new Random();
        int num = random.nextInt(5);
        step += num;
        for (int i = 0; i < step; i++) {
            String thisStep = nextStep(random) + statusarr[random.nextInt(statusarr.length)];
            cubeStack.push(thisStep);
        }

        String temp = "";
        while (!cubeStack.empty()) {
            temp += cubeStack.pop() + " ";
        }

        setCuberandom(temp);
    }

    public String getCuberandom() {
        return this.cuberandom;
    }

    public void setCuberandom(String cuberandom) {
        this.cuberandom = cuberandom;
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        commander.execute(simpleMsg);
    }

    private PieceCheck commandCheck(String[] strings){
        PieceCheck pieceCheck = new PieceCheck() {
            @Override
            public boolean check(String piece) {
                for (String temp:strings){
                    if (piece.equals(temp)){
                        return true;
                    }
                }
                return false;
            }
        };

        return pieceCheck;
    }
}
