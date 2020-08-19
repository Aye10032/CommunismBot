package com.aye10032.NLP;

import com.aye10032.Functions.funcutil.BaseFunc;
import com.aye10032.Functions.funcutil.IFunc;
import com.aye10032.Functions.funcutil.SimpleMsg;
import com.aye10032.Zibenbot;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class DataCollect extends BaseFunc {

    private Connection c = null;

    public static void main(String[] args) {
        /*Connection c = null;
        Statement stmt;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + "D:\\program\\GitHub\\FSBot"+
                    "\\data\\nlpdata\\botnlpdata.db");
            stmt = c.createStatement();
            String sql = "SELECT * FROM question where msg='叶受A';";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println(rs.next());
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.out.println(e);
        }*/
    }

    public Connection getConnection(){
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

    public DataCollect(Zibenbot zibenbot) {
        super(zibenbot);
    }

    @Override
    public void setUp() {
        File database = new File(zibenbot.appDirectory + "\\nlpdata\\botnlpdata.db");
        if (!database.exists()){
            database.getParentFile().mkdirs();
            Connection c = null;
            Statement stmt = null;
            try {
                c = getConnection();
                stmt = c.createStatement();
                String sql = "CREATE TABLE \"question\" (\n" +
                        "\t\"ID\"\tINTEGER NOT NULL,\n" +
                        "\t\"msg\"\tTEXT NOT NULL,\n" +
                        "\t\"fromQQ\"\tTEXT NOT NULL,\n" +
                        "\t\"fromGroup\"\tTEXT NOT NULL,\n" +
                        "\t\"time\"\tTEXT NOT NULL,\n" +
                        "\tPRIMARY KEY(\"ID\" AUTOINCREMENT)\n" +
                        ")";
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
    public void run(SimpleMsg CQmsg) {
        boolean flag = false;
        if (CQmsg.getFromClient() == 1969631968L){
            return;
        }else if (CQmsg.isGroupMsg()) {
            List<Long> atList = zibenbot.getAtMembers(CQmsg.getMsg());
            if (atList.size() != 0){
                for (long qq:atList){
                    if (qq == 2375985957L || qq == 2155231604L){
                        flag = true;
                        break;
                    }
                }
            }else if ((CQmsg.getMsg().contains("aye") || CQmsg.getMsg().contains("Aye") || CQmsg.getMsg().contains("阿叶")
                    || CQmsg.getMsg().contains("小叶") || CQmsg.getMsg().contains("叶受") || CQmsg.getMsg().contains("叶哥哥"))&&
                    !((CQmsg.getMsg().equals("aye") || CQmsg.getMsg().equals("Aye") || CQmsg.getMsg().equals("阿叶")
                    || CQmsg.getMsg().equals("小叶") || CQmsg.getMsg().equals("叶受") || CQmsg.getMsg().equals("叶哥哥")))) {
                flag = true;
            }
        }

        if (flag) {
            if (CQmsg.getFromGroup() == 947657871L){
                if (CQmsg.getMsg().length() <= 6)
                    flag = false;
            }
        }

        if (flag) {
            Statement stmt;
            try {
                stmt = getConnection().createStatement();
                String sql = "SELECT * FROM question where msg='"+CQmsg.getMsg()+"';";
                ResultSet rs = stmt.executeQuery(sql);
                boolean hasMSG = rs.next();
                rs.close();
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                if (!hasMSG) {
                    sql =
                            "INSERT INTO question (msg,fromQQ,fromGroup,time) " + "VALUES ('" + CQmsg.getMsg() + "', '" + CQmsg.getFromClient() + "', '" + CQmsg.getFromGroup() + "', '" + ft.format(dNow) + "' );";
                    stmt.executeUpdate(sql);
                    zibenbot.toPrivateMsg(2375985957L, "已添加数据集：" + CQmsg.getMsg());
                    replyMsg(CQmsg, "[" + ft.format(dNow) + "][INFO] 本条对话已添加NLP待处理数据集");
                }
                /*else {
                    replyMsg(CQmsg, "[" + ft.format(dNow) + "][INFO] 已存在此数据");
                }*/
                stmt.close();
                c.close();
            } catch (Exception e) {
                zibenbot.log(Level.WARNING, e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }
}
