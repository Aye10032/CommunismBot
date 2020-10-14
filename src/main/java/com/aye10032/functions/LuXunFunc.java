package com.aye10032.functions;


import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.opencv.core.Core.vconcat;
import static org.opencv.imgcodecs.Imgcodecs.*;
import static org.opencv.imgproc.Imgproc.*;

//todo 需要增强
public class LuXunFunc {
    Map<Integer, String> ImgMap = new HashMap<>();

    public LuXunFunc(String AppPath) {
        System.load(AppPath + "\\opencv_java430.dll");
        ImgMap.put(1, "data\\image\\biaoqing\\luxun.jpg");
    }

/*    public static void main(String[] args) {
        File os = new File("data\\cv");

        LuXunFunc luXunFunc = new LuXunFunc(os.getAbsolutePath());
//        luXunFunc.addText(1, "我没说过 --鲁迅");
        luXunFunc.addBlack_White("data\\image\\biaoqing\\test.png",new String[]{" 人固有一死 "," people always die "});
    }*/

    public void addText(int flag, String text) {
        Mat src = imread(ImgMap.get(flag));

        float height = src.rows();
        float width = src.cols();

        System.out.println(width + " " + height);
        Mat dst = src.clone();

        Font font = new Font("微软雅黑", Font.PLAIN, 130);
        try {
            createImage(text, font, new File("data\\image\\biaoqing\\text.png"));

            Mat text_src = imread("data\\image\\biaoqing\\text.png");
            float text_height = text_src.rows();
            float text_width = text_src.cols();
            System.out.println(text_width + " " + text_height);
            text_height = (width / text_width) * text_height;
            System.out.println(text_width + " " + text_height);
            resize(text_src, text_src, new Size(width, text_height), 0, 0, Imgproc.INTER_AREA);

            List<Mat> imgs = new ArrayList<>();
            imgs.add(dst);
            imgs.add(text_src);
            vconcat(imgs, dst);
            imwrite("data\\image\\biaoqing\\text.jpg", dst);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        imshow("aa", dst);

//        waitKey(0);
    }

    public void addBlack_White(String img,String[] texts){
        Mat src = imread(img,IMREAD_GRAYSCALE);

        float width = src.cols();

        Mat dst = src.clone();

        List<Mat> imgs = new ArrayList<>();
        imgs.add(dst);

        Font font = new Font("微软雅黑", Font.PLAIN, 130);
        for (String text:texts){
            try {
                createImage(text, font, new File("data\\image\\biaoqing\\text.png"));

                Mat text_src = imread("data\\image\\biaoqing\\text.png");
                float text_height = text_src.rows();
                float text_width = text_src.cols();
                System.out.println(text_width + " " + text_height);
                text_height = (width / text_width) * text_height;
                System.out.println(text_width + " " + text_height);
                resize(text_src, text_src, new Size(width, text_height), 0, 0, Imgproc.INTER_AREA);

                cvtColor(text_src,text_src,COLOR_BGR2GRAY);
                imgs.add(text_src);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        vconcat(imgs, dst);
        imwrite("data\\image\\biaoqing\\text.jpg", dst);
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
