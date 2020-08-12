package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;

import java.io.File;
import java.util.Random;
import java.util.Stack;

public class CubeFunc extends BaseFunc {

    private Stack<String> cubeStack = new Stack<String>();
    private String[] cubarr = new String[]{"F", "B", "U", "D", "R", "L"};
    private String[] statusarr = new String[]{"", "", "'", "'", "2"};
    private int step = 20;
    private String cuberandom = "";

/*    public static void main(String[] args) {
        System.out.println(new AyeCube().getCuberandom());
    }*/

    public CubeFunc(Zibenbot zibenbot) {
        super(zibenbot);
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
    public void run(SimpleMsg CQmsg) {
        if (CQmsg.getMsg().equals(".3")) {
            zibenbot.replyMsg(CQmsg, getCuberandom());
        }else if (CQmsg.getMsg().startsWith("CFOP")||CQmsg.getMsg().startsWith("cfop")) {
            if (CQmsg.isTeamspealMsg()) {
                zibenbot.replyMsg(CQmsg, "ts频道无法发图片，请从群聊或者私聊获取");
                return;
            }
            if (CQmsg.getMsg().contains("F2L") || CQmsg.getMsg().contains("f2l")) {
                zibenbot.replyMsg(CQmsg, zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\CFOP2.jpg")));
            } else if (CQmsg.getMsg().contains("OLL") || CQmsg.getMsg().contains("oll")) {
                zibenbot.replyMsg(CQmsg, zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\CFOP3.jpg")));
            } else if (CQmsg.getMsg().contains("PLL") || CQmsg.getMsg().contains("pll")) {
                zibenbot.replyMsg(CQmsg, zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\CFOP4.jpg")));
            } else {
                zibenbot.replyMsg(CQmsg,
                        zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\CFOP1.jpg"))
                                + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\CFOP2.jpg"))
                                + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\CFOP3.jpg"))
                                + zibenbot.getImg(new File(zibenbot.appDirectory + "\\image\\CFOP4.jpg")));
            }
        }
    }
}
