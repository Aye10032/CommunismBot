package com.aye10032.Functions;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Utils.ConfigLoader;
import com.aye10032.Utils.StringUtil;
import com.aye10032.Utils.Video.VideoClass;
import com.aye10032.Utils.Video.VideoData;
import com.aye10032.Zibenbot;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class RedStoneFunc extends BaseFunc {

    private Connection c = null;

    public RedStoneFunc(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        File database = new File(zibenbot.appDirectory + "\\videodata.db");
        if (!database.exists()) {
            database.getParentFile().mkdirs();
            Connection c = null;
            Statement stmt = null;
            try {
                c = getConnection();
                stmt = c.createStatement();
                String sql = "CREATE TABLE \"videoinfo\" (\n" +
                        "\t\"ID\"\tINTEGER NOT NULL,\n" +
                        "\t\"URL\"\tTEXT NOT NULL,\n" +
                        "\t\"HASDONE\"\tINTEGER NOT NULL,\n" +
                        "\t\"NEEDTRANS\"\tINTEGER NOT NULL,\n" +
                        "\t\"ISTRANS\"\tINTEGER NOT NULL,\n" +
                        "\t\"FROMEQQ\"\tINTEGER NOT NULL,\n" +
                        "\t\"DESCRIPTION\"\tTEXT NOT NULL,\n" +
                        "\t\"TIME\"\tTEXT NOT NULL,\n" +
                        "\tPRIMARY KEY(\"ID\" AUTOINCREMENT)\n" +
                        ");";
                stmt.executeUpdate(sql);
                sql = "CREATE TABLE \"translist\" (\n" +
                        "\t\"ID\"\tINTEGER NOT NULL,\n" +
                        "\t\"FROMID\"\tINTEGER NOT NULL,\n" +
                        "\t\"FROMQQ\"\tINTEGER NOT NULL,\n" +
                        "\t\"MSG\"\tTEXT NOT NULL,\n" +
                        "\t\"TIME\"\tTEXT NOT NULL,\n" +
                        "\tPRIMARY KEY(\"ID\" AUTOINCREMENT)\n" +
                        ");";
                stmt.executeUpdate(sql);
                stmt.close();
                c.close();
                zibenbot.log(Level.WARNING, "Creat table successfully");
            } catch (Exception e) {
                zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void run(SimpleMsg simpleMsg) {
//        VideoClass videoClass = ConfigLoader.load(zibenbot.appDirectory + "/videoData.json", VideoClass.class);

        StringBuilder returnMSG = new StringBuilder();
        String[] strlist = new StringUtil().split(simpleMsg.getMsg());
        if (simpleMsg.getMsg().equals("搬运")) {
            zibenbot.replyMsg(simpleMsg, "关键词列表:\n" +
                    "搬运 <油管链接> [描述]-----添加搬运需求\n" +
                    "烤 <油管链接|B站链接> [描述]-----添加翻译需求\n" +
                    "搬运列表-----获取当前任务列表\n" +
                    "以下命令仅组群内可用:\n" +
                    "已搬 <序列号|油管链接>-----从搬运列表中去除\n" +
                    "接 <序列号|油管链接|B站链接> [时间段]-----承接翻译\n" +
                    "接坑-----查看当前翻译需求队列");
        } else if ((simpleMsg.getMsg().startsWith("搬运") || simpleMsg.getMsg().startsWith("反向")) && simpleMsg.getMsg().contains(" ")) {
            if (strlist.length == 3) {
//                videoClass.addVideoSum();
//                videoClass.addVideo(new VideoData(videoClass.getVideoSum(), strlist[1], strlist[2], simpleMsg.getFromClient()));
                addInfo(strlist[1], 0, 0, 0, simpleMsg.getFromClient(), strlist[2]);
            } else if (strlist.length == 2) {
//                videoClass.addVideoSum();
//                videoClass.addVideo(new VideoData(videoClass.getVideoSum(), strlist[1], "", simpleMsg.getFromClient()));
                addInfo(strlist[1], 0, 0, 0, simpleMsg.getFromClient(), "无描述");
            }
//            videoClass.updateList();
            zibenbot.replyMsg(simpleMsg, "已添加" + strlist[1] + " " + strlist[2]);
            if (simpleMsg.getFromGroup() != 456919710L) {
                zibenbot.toGroupMsg(456919710L, "已添加" + strlist[1] + " " + strlist[2]);
            }
//            ConfigLoader.save(zibenbot.appDirectory + "/videoData.json", VideoClass.class, videoClass);
        } else if (simpleMsg.getMsg().startsWith("烤 ")) {
            if (strlist.length == 3) {
//                videoClass.addVideoSum();
//                videoClass.addVideo(new VideoData(videoClass.getVideoSum(), strlist[1], strlist[2], true, simpleMsg.getFromClient()));
                addInfo(strlist[1], 0, 1, 0, simpleMsg.getFromClient(), strlist[2]);
            } else if (strlist.length == 2) {
//                videoClass.addVideoSum();
//                videoClass.addVideo(new VideoData(videoClass.getVideoSum(), strlist[1], "", true, simpleMsg.getFromClient()));
                addInfo(strlist[1], 0, 1, 0, simpleMsg.getFromClient(), "无描述");
            }
//            videoClass.updateList();
            zibenbot.replyMsg(simpleMsg, "已添加" + strlist[1] + " " + strlist[2]);
//            ConfigLoader.save(zibenbot.appDirectory + "/videoData.json", VideoClass.class, videoClass);
        } else if (simpleMsg.getMsg().equals("搬运列表")) {
//            videoClass.updateList();
            zibenbot.replyMsg(simpleMsg, getFullList());
        }
        if (simpleMsg.getFromGroup() == 456919710L || simpleMsg.getFromClient() == 2375985957L) {
            if (simpleMsg.getMsg().equals("接坑")) {
//                videoClass.updateList();
                zibenbot.replyMsg(simpleMsg, getNeedTransList());
//                ConfigLoader.save(zibenbot.appDirectory + "/videoData.json", VideoClass.class, videoClass);
            } else if (simpleMsg.getMsg().startsWith("已搬 ")) {
//                videoClass.VideoDone(strlist[1]);
//                videoClass.updateList();
//                zibenbot.replyMsg(simpleMsg, videoClass.getFullList());
//                ConfigLoader.save(zibenbot.appDirectory + "/videoData.json", VideoClass.class, videoClass);
                done(Integer.parseInt(strlist[1]));
            } else if (simpleMsg.getMsg().startsWith("接 ")) {
                /*if (videoClass.getVideoNum() == 0) {
                    zibenbot.replyMsg(simpleMsg, "当前列表中无视频");
                } else {
                    for (VideoData data : videoClass.getDataList()) {
                        if (data.getVideoLink().equals(strlist[1]) || (data.getNO() + "").equals(strlist[1])) {
                            if (strlist.length == 3) {
                                data.addTrans(simpleMsg.getFromClient(), strlist[2]);
                            } else if (strlist.length == 2) {
                                data.addTrans(simpleMsg.getFromClient(), "");
                            }
                        }
                    }
                }*/
                if (strlist.length != 3) {
                    zibenbot.replyMsg(simpleMsg, "格式不正确，回复\"搬运\"查看详细信息");
                } else {
                    addtrans(Integer.parseInt(strlist[1]), strlist[2], simpleMsg.getFromClient());
                }
                zibenbot.replyMsg(simpleMsg, getNeedTransList());
//                ConfigLoader.save(zibenbot.appDirectory + "/videoData.json", VideoClass.class, videoClass);
            }
        }
    }

    private Connection getConnection() {
        try {
            if (c == null || c.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:" + zibenbot.appDirectory +
                        "\\nlpdata\\botnlpdata.db");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    private void addInfo(String url, int hasdone, int needtrans, int istrans, long fromqq, String description) {
        Statement stmt;

        try {
            stmt = getConnection().createStatement();

            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String sql =
                    "INSERT INTO videoinfo (URL,HASDONE,NEEDTRANS,ISTRANS,FROMEQQ,DESCRIPTION,TIME) "
                            + "VALUES ('"
                            + url + "', '"
                            + hasdone + "', '"
                            + needtrans + "', '"
                            + istrans + "', '"
                            + fromqq + "', '"
                            + description + "', '"
                            + ft.format(dNow) + "' );";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void addtrans(int ID, String msg, long fromqq) {
        Statement stmt;

        try {
            stmt = getConnection().createStatement();

            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String sql =
                    "INSERT INTO translist (FROMID,FROMQQ,MSG,TIME) "
                            + "VALUES ('"
                            + ID + "', '"
                            + fromqq + "', '"
                            + msg + "', '"
                            + ft.format(dNow) + "' );";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        } catch (Exception e) {
            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private String getFullList() {
        StringBuilder builder = new StringBuilder();
        builder.append("搬运列表\n------\n");
        Statement stmt;

        try {
            stmt = getConnection().createStatement();
            String sql = "SELECT * FROM videoinfo WHERE HASDONE=" + 0 + ";";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                builder.append("NO.").append(rs.getInt("ID")).append("\n")
                        .append("  链接:").append(rs.getString("URL")).append("\n")
                        .append("  描述:").append(rs.getString("DESCRIPTION")).append("\n")
                        .append("  状态:").append("未搬运 ");
                if (rs.getInt("NEEDTRANS") == 1) {
                    if (rs.getInt("ISTRANS") == 0) {
                        builder.append("待翻译");
                    } else {
                        builder.append("翻译中");
                    }
                }
                builder.append("\n");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }

        return builder.toString();
    }

    private String getNeedTransList() {
        StringBuilder builder = new StringBuilder();
        builder.append("待翻译列表\n------\n");
        Statement stmt;

        try {
            stmt = getConnection().createStatement();
            String sql = "SELECT * FROM videoinfo WHERE HASDONE=" + 0 + " AND NEEDTRANS=" + 1 + ";";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                builder.append("NO.").append(rs.getInt("ID")).append("\n")
                        .append("  链接:").append(rs.getString("URL")).append("\n")
                        .append("  描述:").append(rs.getString("DESCRIPTION")).append("\n")
                        .append("  状态:");

                if (rs.getInt("ISTRANS") == 0) {
                    builder.append("待翻译\n");
                } else {
                    builder.append("翻译中\n").append(getTransList(rs.getInt("ID")));
                }
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }

        return builder.toString();
    }

    private String getTransList(int ID) {
        StringBuilder builder = new StringBuilder();
        Statement stmt;

        try {
            stmt = getConnection().createStatement();
            String sql = "SELECT * FROM translist WHERE FROMID=" + ID + ";";
            ;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                builder.append("    ").append(zibenbot.at(rs.getInt("FROMQQ"))).append(" ")
                        .append(rs.getString("MSG")).append("\n");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }
        return builder.toString();
    }

    private void done(int ID) {
        Statement stmt;

        try {
            stmt = getConnection().createStatement();

            String sql = "UPDATE videoinfo SET HASDONE = 1 WHERE ID = " + ID + ";";
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();

        } catch (Exception e) {
            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
