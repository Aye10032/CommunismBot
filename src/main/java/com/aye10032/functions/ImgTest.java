/*
package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

//测试用
public class ImgTest extends BaseFunc {

    private File file;

    public ImgTest(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        file = new File(this.appDirectory + "/tmp");
        file.mkdirs();
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        List<Image> list = zibenbot.getImgFromMsg(simpleMsg);
        list.forEach(m -> {
            try {
                File output = new File(file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
                output.createNewFile();
                ImageIO.write((RenderedImage) m, "jpg", output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
*/
