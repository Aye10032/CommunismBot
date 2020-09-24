package com.aye10032.utils;

import org.apache.commons.io.IOUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ImgUtils {

    public static final String PNG_EXTENSION = "png";
    public static final String JPG_EXTENSION = "jpg";

    public static byte[] compress(byte[] bytes, String extension) {
        Graphics2D graphics2D = null;
        ImageWriter imageWriter = null;
        ImageWriteParam imageWriteParams = null;
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            imageWriter = ImageIO.getImageWritersByFormatName(extension).next();
            if(PNG_EXTENSION.equals(extension.toLowerCase())) {
                BufferedImage srcBufferedImage = ImageIO.read(byteArrayInputStream);
                int width = srcBufferedImage.getWidth();
                int height = srcBufferedImage.getHeight();
                BufferedImage newBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_555_RGB);
                graphics2D = newBufferedImage.createGraphics();
                graphics2D.setBackground(Color.WHITE);
                graphics2D.clearRect(0, 0, width, height);
                graphics2D.drawImage(srcBufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
                imageWriter = ImageIO.getImageWritersByFormatName(extension).next();
                byteArrayOutputStream = new ByteArrayOutputStream();
                imageWriter.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
                imageWriter.write(new IIOImage(newBufferedImage, null, null));
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            } else if(JPG_EXTENSION.equals(extension.toLowerCase())) {
                BufferedImage srcBufferedImage = ImageIO.read(byteArrayInputStream);
                imageWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
                imageWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                imageWriteParams.setCompressionQuality(0.8F);
                imageWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
                ColorModel colorModel = ImageIO.read(byteArrayInputStream).getColorModel();
                imageWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
                byteArrayOutputStream = new ByteArrayOutputStream();
                imageWriter.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
                imageWriter.write(null, new IIOImage(srcBufferedImage, null, null), imageWriteParams);
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return bytes;
        } finally {
            if(imageWriter != null) {
                imageWriter.abort();
            }
            IOUtils.closeQuietly(byteArrayOutputStream);
            IOUtils.closeQuietly(byteArrayInputStream);
        }
        return null;
    }


    public static String getFileExt(String fileName) {
        int point = fileName.lastIndexOf('.');
        int length = fileName.length();
        if (point == -1 || point == length - 1) {
            return "";
        } else {
            return fileName.substring(point + 1, length);
        }
    }

    public static void downloadImg(String imgurl, String filename, String appDirectory) {
        try {
            URL img = new URL(imgurl);
            HttpURLConnection conn = (HttpURLConnection) img.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            byte[] data = readInputStream(inStream);
            File imageFile;
            if (appDirectory == null || appDirectory.isEmpty()) {
                imageFile = new File("image\\" + filename + ".jpg");
            } else {
                imageFile = new File(appDirectory + "\\image\\" + filename + ".jpg");
            }
            if (!imageFile.exists()) {
                imageFile.getParentFile().mkdirs();
                imageFile.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(imageFile);
            outStream.write(data);
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadImg(String imgurl, String filename, String appDirectory, int height, int width) {
        try {
            URL img = new URL(imgurl);
            HttpURLConnection conn = (HttpURLConnection) img.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();
            BufferedInputStream in = new BufferedInputStream(inStream);
            File imageFile;
            if (appDirectory == null || appDirectory.isEmpty()) {
                imageFile = new File("image\\" + filename + ".jpg");
            } else {
                imageFile = new File(appDirectory + "\\image\\" + filename + ".jpg");
            }
            if (!imageFile.exists()) {
                imageFile.getParentFile().mkdirs();
                imageFile.createNewFile();
            }
            Image bi = ImageIO.read(in);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(bi, 0, 0, width, height, null);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile));
            ImageIO.write(tag, "jpg", out);
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readInputStream(InputStream inStream) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();

    }

}
