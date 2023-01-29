package com.aye10032.utils.video;

import com.aye10032.Zibenbot;
import com.aye10032.utils.ImgUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Getter
public class BiliInfo {

    protected static final String apiURL1 = "https://api.bilibili.com/x/web-interface/view?";
    protected static final String apiURL2 = "&type=jsonp";
    protected String pvideoapi = "https://api.bilibili.com/pvideo?aid=";
    protected String apiURL;

    private String title = "";
    private String description = "";
    private String imgurl = "";
    private String videoUrlAv = "https://www.bilibili.com/video/av";
    private String videoUrlBv = "https://www.bilibili.com/video/BV";
    private String videoUrl;

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
    private boolean hasVideo = true;
    private int code = 0;
    private String faceImageFilePath = null;
    private String upImageFilePath = null;


    public BiliInfo(String avn, String appDirectory) {
        if (avn.startsWith("a") || avn.startsWith("A")) {
            this.videoUrl = videoUrlAv + avn.substring(2);
            this.apiURL = apiURL1 + "aid=" + avn.substring(2) + apiURL2;
        } else {
            this.videoUrl = videoUrlBv + avn.substring(2);
            this.apiURL = apiURL1 + "bvid=BV" + avn.substring(2) + apiURL2;
        }

        String body = null;
        try {

            OkHttpClient client = Zibenbot.getOkHttpClient();
            Request request = new Request.Builder()
                    .url(apiURL)
                    .method("GET", null)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                body = new String(response.body().bytes());
            }

            JsonElement element = JsonParser.parseString(body);

            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();

                code = jsonObject.get("code").getAsInt();
                if (code != 0) {
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

            }

            File upImageFile = ImgUtils.downloadImg(headurl, avn + "_head", appDirectory, 200, 200);
            File faceImageFile = ImgUtils.downloadImg(imgurl, avn + "_img", appDirectory);
            if (upImageFile != null) {
                upImageFilePath = upImageFile.getPath();
            }
            if (faceImageFile != null) {
                faceImageFilePath = faceImageFile.getPath();
            }

        } catch (IOException e) {
            log.error("查询b站视频信息出错：", e);
            throw new RuntimeException(e);
        }
    }


/*
    private void creatPvideo6Min() {
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
                */
/*try {
                    ImageIO.write(img, "JPG", new File("res/test/" + Integer.toString(i.getAndIncrement()) + ".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*

            });

            encoder.finish();
        }
    }
*/

/*
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
                */
/*try {
                    ImageIO.write(img, "JPG", new File("res/test/" + Integer.toString(i.getAndIncrement()) + ".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*

            });

            encoder.finish();
        }
    }
*/

    static int[] toIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = list.get(i);
        }
        return ret;
    }

}
