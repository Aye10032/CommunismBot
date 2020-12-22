package com.aye10032.functions;


import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;
import com.dazo66.command.Commander;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static org.opencv.core.Core.vconcat;
import static org.opencv.imgcodecs.Imgcodecs.*;
import static org.opencv.imgproc.Imgproc.*;

//todo 需要增强
public class LuXunFunc extends BaseFunc {
    private final String text_path = appDirectory + "\\image\\biaoqing\\text.png";
    private final String download_img_path = appDirectory + "\\image\\biaoqing\\src.jpg";

    private Commander<SimpleMsg> commander;
    Map<Integer, String> ImgMap = new HashMap<>();

    public LuXunFunc(Zibenbot zibenbot) {
        super(zibenbot);
        new File(appDirectory + "\\image\\biaoqing\\output").mkdirs();
    }

    @Override
    public void setUp() {
        ImgMap.put(1, appDirectory + "\\image\\biaoqing\\luxun.jpg");
        ImgMap.put(2, appDirectory + "\\image\\biaoqing\\unexpectedly.jpg");
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
        String[] msgs = simpleMsg.getMsg().split(" ");
        if (msgs[0].equals(".鲁迅") && msgs.length == 2) {
            File output = new File(appDirectory + "\\image\\biaoqing\\output\\" + simpleMsg.getMsg().hashCode() + ".jpg");
            addText(1, msgs[1], output);
            zibenbot.replyMsg(simpleMsg, zibenbot.getImg(output));
        } else if ((msgs[0].contains("竟在") || msgs[0].contains("竟是")) && msgs[0].contains(".jpg") && msgs.length == 1) {
            File output = new File(appDirectory + "\\image\\biaoqing\\output\\" + simpleMsg.getMsg().hashCode() + ".jpg");
            addText(2, simpleMsg.getMsg().substring(0, simpleMsg.getMsg().lastIndexOf('.')), output);
            replyMsg(simpleMsg, zibenbot.getImg(output));
        } else if (msgs[0].equals(".黑白") && msgs.length >= 2) {
            File output = new File(appDirectory + "\\image\\biaoqing\\output\\" + simpleMsg.getMsg().hashCode() + ".jpg");
            List<Image> images = zibenbot.getImgFromMsg(simpleMsg);
            if (images.size() == 0) {
                zibenbot.replyMsg(simpleMsg, "未检测到图片");
            } else {
                File file = new File(download_img_path);
                try {
                    ImageIO.write((RenderedImage) images.get(0), "png", file);
                    addBlack_White(download_img_path, Arrays.copyOfRange(msgs, 2, msgs.length), output);
//                    StringBuilder builder = new StringBuilder();
//                    for (String str : msgs) {
//                        builder.append(str).append("\n");
//                    }
                    zibenbot.replyMsg(simpleMsg, zibenbot.getImg(output));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static void main(String[] args) {
//        System.load(System.getProperty("user.dir") + "\\data\\cv\\opencv_java430.dll");
////        File os = new File("data\\cv");
//
//        LuXunFunc luXunFunc = new LuXunFunc(os.getAbsolutePath());
////        luXunFunc.addText(1, "我没说过 --鲁迅");
//        luXunFunc.addBlack_White("data\\image\\biaoqing\\test.png",new String[]{" 人固有一死 "," people always die "});
//    }

    public void addText(int flag, String text, File output) {
        Mat src = imread(ImgMap.get(flag));

        float height = src.rows();
        float width = src.cols();

        System.out.println(width + " " + height);
        Mat dst = src.clone();

        Font font = new Font("微软雅黑", Font.PLAIN, 130);
        List<String> text_list = new ArrayList<>();

        int last = text.length() % 10;
        int times = text.length() / 10;
        if (times>=1) {
            for (int i = 0; i < times; i++) {
                text_list.add(text.substring(i * 10, i * 10 + 10));
            }
        }
        if (last != 0) {
            StringBuilder builder = new StringBuilder();
            String substring = text.substring(text.length() - last, text.length());
            if (times == 0){
                for (int i = 0; i < (10 - last) / 2; i++) {
                    builder.append("    ");
                }
                builder.append(substring);
                for (int i = 0; i < (10-last)/2; i++) {
                    builder.append("    ");
                }
            }else {
                builder.append(substring);
                for (int i = 0; i < 10 - last; i++) {
                    builder.append("    ");
                }
            }
            text_list.add(builder.toString());
        }
        try {
            for (String str : text_list) {
                createImage(str, font, new File(text_path));
                Mat text_src = imread(text_path);

                float text_height = text_src.rows();
                float text_width = text_src.cols();

//                System.out.println(text_width + " " + text_height);
                text_height = (width / text_width) * text_height;
//                System.out.println(text_width + " " + text_height);
                resize(text_src, text_src, new Size(width, text_height), 0, 0, Imgproc.INTER_AREA);

                List<Mat> imgs = new ArrayList<>();
                imgs.add(dst);
                imgs.add(text_src);
                vconcat(imgs, dst);
            }
            imwrite(output.getAbsolutePath(), dst);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        imshow("aa", dst);

//        waitKey(0);
    }

    public void addBlack_White(String img, String[] texts, File output) {
        Mat src = imread(img, IMREAD_GRAYSCALE);

        float width = src.cols();

        Mat dst = src.clone();

        List<Mat> imgs = new ArrayList<>();
        imgs.add(dst);

        Font font = new Font("微软雅黑", Font.PLAIN, 130);
        for (String text : texts) {
            try {
                createImage(text, font, new File(zibenbot.appDirectory + "\\image\\biaoqing\\text.png"));

                Mat text_src = imread(zibenbot.appDirectory + "\\image\\biaoqing\\text.png");
                float text_height = text_src.rows();
                float text_width = text_src.cols();
                System.out.println(text_width + " " + text_height);
                text_height = (width / text_width) * text_height;
                System.out.println(text_width + " " + text_height);
                resize(text_src, text_src, new Size(width, text_height), 0, 0, Imgproc.INTER_AREA);

                cvtColor(text_src, text_src, COLOR_BGR2GRAY);
                imgs.add(text_src);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        vconcat(imgs, dst);
        imwrite(output.getAbsolutePath(), dst);

//        imshow("test",dst);
//
//        waitKey(0);
    }

    private static int[] getWidthAndHeight(String text, Font font) {
        Rectangle2D r = font.getStringBounds(text, new FontRenderContext(
                AffineTransform.getScaleInstance(1, 1), false, false));
        int unitHeight = (int) Math.floor(r.getHeight());
        int width = (int) Math.round(r.getWidth()) + 1;
        int height = unitHeight + 1;
        return new int[]{width, height};
    }

    public static void createImage(String text, Font font, File outFile)
            throws Exception {

        int[] arr = getWidthAndHeight(text, font);
        int width = arr[0];
        int height = arr[1];

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g = image.createGraphics();

        g.setColor(Color.WHITE);

        g.fillRect(0, 0, width, height);

        g.setColor(Color.black);
        g.setFont(font);
        g.drawString(text, 0, font.getSize());

        g.dispose();

        ImageIO.write(image, "png", outFile);
    }
}
