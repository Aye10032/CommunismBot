package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;

public class EChouFunc extends BaseFunc {

    String fileDir;

    public EChouFunc(Zibenbot zibenbot){
        super(zibenbot);
        this.fileDir = zibenbot.appDirectory;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    @Override
    public void setUp() {

    }

    @Override
    public void run(SimpleMsg simpleMsg) {

    }
}
