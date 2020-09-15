package com.aye10032.utils.video;

import com.aye10032.Zibenbot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VideoClass {

    private Integer videoNum = 0;
    private Integer videoSum = 0;
    private List<VideoData> dataList = new ArrayList<VideoData>();

    public VideoClass() {
    }

    public int getVideoNum() {
        if (dataList.isEmpty()) {
            setVideoNum(0);
        } else {
            setVideoNum(dataList.size());
        }

        return this.videoNum;
    }

    public void setVideoNum(int num) {
        this.videoNum = num;
    }

    public int getVideoSum() {
        return this.videoSum;
    }

    public void addVideoSum() {
        videoSum += 1;
    }

    public boolean addVideo(VideoData data) {
        String link = data.getVideoLink();
        boolean addSuccess = true;

        for (VideoData data1 : dataList) {
            if (data1.getVideoLink().equals(link)) {
                if (data1.getDescription().equals("")) {
                    data1.setDescription(data.getDescription());
                } else {
                    if (!data.getDescription().equals("")) {
                        data1.setDescription(data1.getDescription() + "," + data.getDescription());
                    }
                }
                addSuccess = false;
                break;
            }
        }

        if (addSuccess) {
            dataList.add(data);
        }

        getVideoNum();
        return addSuccess;
    }

    public boolean VideoDone(String flag) {
        boolean hasVideo = false;
        for (VideoData data : dataList) {
            if (data.getVideoLink().equals(flag) || (data.getNO() + "").equals(flag)) {
                data.setHasDone(true);
                hasVideo = true;
                break;
            }
        }

        return hasVideo;
    }

    public List<VideoData> getDataList() {
        return dataList;
    }

    public void updateList() {
        Iterator<VideoData> iterator = dataList.iterator();

        while (iterator.hasNext()) {
            VideoData data = iterator.next();
            if (data.isHasDone()) {
                iterator.remove();
            }
        }
    }

    public String getFullList(){
        StringBuilder returnMSG = new StringBuilder("当前列表中");
        if (getVideoNum() == 0) {
            returnMSG.append("无视频");
        } else {
            returnMSG.append("有").append(getVideoNum()).append("个视频\n------");

            for (VideoData data : getDataList()) {
                returnMSG.append("\nNo.").append(data.getNO())
                        .append("\n  链接: ").append(data.getVideoLink())
                        .append("\n  描述: ").append(data.getDescription())
                        .append("\n  状态: ");
                if (!data.getIsTrans()) {
                    if (!data.isHasDone()) {
                        returnMSG.append("未搬运");
                    } else {
                        returnMSG.append("已搬运");
                    }
                    if (data.getNeedTrans()) {
                        returnMSG.append(" 要翻译");
                    }
                } else {
                    returnMSG.append("翻译中");
                }
            }
        }

        return new String(returnMSG);
    }

    public String getTranslateList(Zibenbot zibenbot){
        StringBuilder returnMSG = new StringBuilder("");
        if (getVideoNum() == 0) {
            returnMSG.append("当前列表中无视频");
        } else {
            returnMSG.append("待翻译列表:\n------");

            for (VideoData data : dataList) {
                if (data.getNeedTrans()) {
                    returnMSG.append("\nNo.").append(data.getNO())
                            .append("\n  链接: ").append(data.getVideoLink())
                            .append("\n  描述: ").append(data.getDescription())
                            .append("\n  状态: ");
                    if (!data.getIsTrans()) {
                        returnMSG.append("待翻译");
                    } else {
                        returnMSG.append("翻译中:");

                        for (Map.Entry<Long, String> entry : data.getTransList().entrySet()) {
                            returnMSG.append("\n    ").append(zibenbot.at(entry.getKey())).append(entry.getValue());
                        }
                    }
                }
            }
        }
        return new String(returnMSG);
    }

}
