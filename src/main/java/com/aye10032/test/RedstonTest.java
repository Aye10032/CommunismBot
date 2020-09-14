package com.aye10032.test;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RedstonTest {

    private Connection c = null;

    public static void main(String[] args) {
        RedstonTest redstonTest = new RedstonTest();
//        redstonTest.addInfo("https://youtu.be/ea6py9q46QU", 0, 1, 1, 864586761L, "pack.png，全长11:52");
//        redstonTest.addInfo("urltest2", 0, 0, 0, 123456789L, "description test");
//        redstonTest.getCount();
//        redstonTest.addtrans(4, "0-6", 864586761L);
//        System.out.println(redstonTest.getNeedTransList());
        redstonTest.done(1);
    }

    public RedstonTest() {
        File database = new File("D:\\program\\GitHub\\FSBot\\data" + "\\videodata.db");
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
//                zibenbot.log(Level.WARNING, "Creat table successfully");
            } catch (Exception e) {
//                zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
            }
        }
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
                /*else {
                    replyMsg(CQmsg, "[" + ft.format(dNow) + "][INFO] 已存在此数据");
                }*/
            stmt.close();
            c.close();
        } catch (Exception e) {
//            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private int getCount() {
        int count = 0;

        Statement stmt;

        try {
            stmt = getConnection().createStatement();
            String sql = "SELECT ID FROM videoinfo ORDER BY ID DESC LIMIT 0,1;";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs.getInt("ID"));
            stmt.close();
            c.close();
        } catch (Exception e) {
//            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }

        return count;
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
                /*else {
                    replyMsg(CQmsg, "[" + ft.format(dNow) + "][INFO] 已存在此数据");
                }*/
            stmt.close();
            c.close();

        } catch (Exception e) {
//            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
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
//            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
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
//            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
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
                builder.append("    ").append(rs.getInt("FROMQQ")).append(" ")
                        .append(rs.getString("MSG")).append("\n");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
//            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }
        return builder.toString();
    }

    private void done(int ID) {
        Statement stmt;

        try {
            stmt = getConnection().createStatement();

            String sql = "UPDATE videoinfo SET HASDONE = 1 WHERE ID = " + ID + ";";
            stmt.executeUpdate(sql);
                /*else {
                    replyMsg(CQmsg, "[" + ft.format(dNow) + "][INFO] 已存在此数据");
                }*/
            stmt.close();
            c.close();

        } catch (Exception e) {
//            zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private Connection getConnection() {
        try {
            if (c == null || c.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:" + "D:\\program\\GitHub\\FSBot\\data" +
                        "\\videodata.db");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

}
