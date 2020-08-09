package com.aye10032.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BiliInfo {

    protected String apiURL1 = "https://api.bilibili.com/x/web-interface/view?";
    protected String apiURL2 = "&type=jsonp";
    protected String pvideoapi = "https://api.bilibili.com/pvideo?aid=";
    protected String apiURL;
    private String appDirectory = "";
    private String pvideodir;

    private String title = "";
    private String description = "";
    private String imgurl = "";
    private String videourl_av = "https://www.bilibili.com/video/av";
    private String videourl_bv = "https://www.bilibili.com/video/BV";
    private String videourl;

    private String headurl = "";
    private String up = "";

    private int view = 0;
    private int danmaku = 0;
    private int like = 0;
    private int coin = 0;
    private int favorite = 0;
    private int reply = 0;

    private String aid = "";
    private String bvid = "";
    private int duration = 0;
    private P_Video p_video;
    private int pv_time = 360;
    public boolean hasPvdeo = true;
    public boolean hasVideo = true;
    public int code = 0;


    public BiliInfo(String avn, String appDirectory) {
        if (avn.startsWith("a") || avn.startsWith("A")) {
            this.videourl = videourl_av + avn.substring(2);
            this.apiURL = apiURL1 + "aid=" + avn.substring(2) + apiURL2;
        } else {
            this.videourl = videourl_bv + avn.substring(2);
            this.apiURL = apiURL1 + "bvid=BV" + avn.substring(2) + apiURL2;
        }
        this.appDirectory = appDirectory;
        if (appDirectory == null || appDirectory.isEmpty()) {
            pvideodir = "image/pvideo.gif";
        } else {
            pvideodir = appDirectory + "/image/pvideo.gif";
        }

        String body = null;
        try {

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(apiURL)
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                body = new String(response.body().bytes());
            }

            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(body);

            if (element.isJsonObject()) {
                System.out.println(element);
                JsonObject jsonObject = element.getAsJsonObject();

                code = jsonObject.get("code").getAsInt();
                if (code != 0) {
                    hasPvdeo = false;
                    hasVideo = false;
                    return;
                }

                JsonObject dataJson = jsonObject.get("data").getAsJsonObject();
                this.title = dataJson.get("title").getAsString();
                this.description = dataJson.get("desc").getAsString();
                this.imgurl = dataJson.get("pic").getAsString();

                JsonObject ownerJson = dataJson.get("owner").getAsJsonObject();
                this.headurl = ownerJson.get("face").getAsString();
                this.up = ownerJson.get("name").getAsString();

                JsonObject statJson = dataJson.get("stat").getAsJsonObject();
                this.view = statJson.get("view").getAsInt();
                this.danmaku = statJson.get("danmaku").getAsInt();
                this.like = statJson.get("like").getAsInt();
                this.coin = statJson.get("coin").getAsInt();
                this.favorite = statJson.get("favorite").getAsInt();
                this.reply = statJson.get("reply").getAsInt();

                this.duration = dataJson.get("duration").getAsInt();
                this.aid = dataJson.get("aid").getAsString();
                this.bvid = dataJson.get("bvid").getAsString();

                Request request1 = new Request.Builder()
                        .url(pvideoapi + this.aid)
                        .method("GET", null)
                        .build();

                Response response1 = client.newCall(request1).execute();
                if (response1.body() != null) {
                    body = new String(response1.body().bytes());
                }
                Gson gson = new Gson();
                this.p_video = gson.fromJson(body, P_Video.class);
                this.hasPvdeo = p_video.code == 0 ? true : false;
            }
            if (hasPvdeo) {
                try {
                    creatPvideo_6min();
                } catch (Exception e) {
                    e.printStackTrace();
                    hasPvdeo = false;
                }
            }
            downloadImg(headurl, "head", 200, 200);
            downloadImg(imgurl, "img");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadImg(String imgurl, String filename) {
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

    public void downloadImg(String imgurl, String filename, int height, int width) {
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

    private byte[] readInputStream(InputStream inStream) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();

    }

    private void creatPvideo_6min() {
        for (String url : p_video.data.image) {

        }
        List<BufferedImage> images = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < p_video.data.index.length; i++) {
            if (pv_time > p_video.data.index[i]) {
                count = i;
            }
        }

        for (String url : p_video.data.image) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(new URL("https:" + url));
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int y = 0; y < p_video.data.img_y_len; y++) {
                for (int x = 0; x < p_video.data.img_x_len; x++) {
                    if (count > images.size()) {
                        images.add(image.getSubimage(
                                x * p_video.data.img_x_size,
                                y * p_video.data.img_y_size,
                                p_video.data.img_x_size,
                                p_video.data.img_y_size));
                    } else {
                        break;
                    }
                }
            }
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            encoder.start(pvideodir);
            encoder.setSize(p_video.data.img_x_size, p_video.data.img_y_size);
            encoder.setTransparent(Color.WHITE);
            encoder.setRepeat(0);
            encoder.setDelay(30);
            encoder.setQuality(256);

            AtomicInteger i = new AtomicInteger(0);
            images.forEach(img -> {
                int last = i.get() == 0 ? 0 : p_video.data.index[i.get() - 1];
                encoder.setDelay((p_video.data.index[i.getAndIncrement()] - last) * 1000 / 15);
                int height = img.getHeight();
                int width = img.getWidth();
                BufferedImage zoomImage = new BufferedImage(width, height, 3);
                Image image1 = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                Graphics gc = zoomImage.getGraphics();
                gc.setColor(Color.WHITE);
                gc.drawImage(image1, 0, 0, null);
                encoder.addFrame(zoomImage);
                /*try {
                    ImageIO.write(img, "JPG", new File("res/test/" + Integer.toString(i.getAndIncrement()) + ".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            });

            encoder.finish();
        }
    }

    private void creatPvideo_all() {
        List<BufferedImage> raw_images = new ArrayList<>();
        for (String url : p_video.data.image) {
            try {
                raw_images.add(ImageIO.read(new URL("https:" + url)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<BufferedImage> images = new ArrayList<>();

        int count = 60;
        int time = 0;
        if (this.duration > 30 * 60) {
            time = 30 * 60;
        } else {
            time = duration;
        }
        int[] index = null;
        for (int i = 0; i < p_video.data.index.length; i++) {
            if (i == p_video.data.index.length - 1) {
                index = p_video.data.index;
            }
            if (time > p_video.data.index[i]) {
                continue;
            } else {
                index = Arrays.copyOf(p_video.data.index, i + 1);
                break;
            }
        }

        if (index.length > 60) {
            float f = (index.length / 60);
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < index.length; i++) {
                if ((int) (i % f) == 0) {
                    list.add(index[i]);
                }
            }
            index = toIntArray(list);
        }

        for (int i = 0; i < raw_images.size(); i++) {
            for (int y = 0; y < p_video.data.img_y_len; y++) {
                for (int x = 0; x < p_video.data.img_x_len; x++) {
                    if (Arrays.binarySearch(index, p_video.data.index[(i + 1) * (y + 1) * (x + 1) - 1]) > -1) {
                        images.add(raw_images.get(i).getSubimage(
                                x * p_video.data.img_x_size,
                                y * p_video.data.img_y_size,
                                p_video.data.img_x_size,
                                p_video.data.img_y_size));
                    } else {
                        continue;
                    }
                }
            }
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            encoder.start(pvideodir);
            encoder.setSize(p_video.data.img_x_size, p_video.data.img_y_size);
            encoder.setTransparent(Color.WHITE);
            encoder.setRepeat(1);
            encoder.setDelay(30);
            encoder.setQuality(256);

            AtomicInteger i1 = new AtomicInteger(0);
            int[] finalIndex = index;
            images.forEach(img -> {
                //延迟计划是实际的60秒变成5秒
                int last = i1.get() == 0 ? 0 : p_video.data.index[i1.get() - 1];
                encoder.setDelay(15 * 1000 / finalIndex.length);
                int height = img.getHeight();
                int width = img.getWidth();
                BufferedImage zoomImage = new BufferedImage(width, height, 3);
                Image image1 = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                Graphics gc = zoomImage.getGraphics();
                gc.setColor(Color.WHITE);
                gc.drawImage(image1, 0, 0, null);
                encoder.addFrame(zoomImage);
                /*try {
                    ImageIO.write(img, "JPG", new File("res/test/" + Integer.toString(i.getAndIncrement()) + ".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            });

            encoder.finish();
        }
    }

    static int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = list.get(i);
        return ret;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return description;
    }

    public String getImgurl() {
        return this.imgurl;
    }

    public String getVideourl() {
        return this.videourl;
    }

    public int getLike() {
        return like;
    }

    public int getCoin() {
        return coin;
    }

    public int getFavorite() {
        return favorite;
    }

    public int getView() {
        return view;
    }

    public int getDanmaku() {
        return danmaku;
    }

    public int getReply() {
        return reply;
    }

    public String getUp() {
        return up;
    }

    public String getHeadurl() {
        return headurl;
    }

    public int getDuration() {
        return duration;
    }

    public class P_Video {
        int code = 0;
        String message = "0";
        int ttl = 1;
        P_Video.data data;

        class data {
            String pvdata = null;
            int img_x_len;
            int img_y_len;
            int img_x_size;
            int img_y_size;
            String[] image;
            int[] index;

        }

    }

}
