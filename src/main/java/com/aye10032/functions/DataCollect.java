package com.aye10032.functions;

import com.aye10032.Zibenbot;
import com.aye10032.functions.funcutil.BaseFunc;
import com.aye10032.functions.funcutil.SimpleMsg;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;


public class DataCollect extends BaseFunc {

    private Connection c = null;

/*    public static void main(String[] args) {
        Connection c = null;
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
        }
    }*/

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
    public void run(SimpleMsg simpleMsg) {
        boolean flag = false;
        if (simpleMsg.getFromClient() == 1969631968L){
            return;
        }else if (simpleMsg.isGroupMsg()) {
            List<Long> atList = zibenbot.getAtMembers(simpleMsg.getMsg());
            if (atList.size() != 0){
                for (long qq:atList){
                    if (qq == 2375985957L || qq == 2155231604L){
                        flag = true;
                        break;
                    }
                }
            }else if ((simpleMsg.getMsg().contains("aye") || simpleMsg.getMsg().contains("Aye") || simpleMsg.getMsg().contains("阿叶")
                    || simpleMsg.getMsg().contains("小叶") || simpleMsg.getMsg().contains("叶受") || simpleMsg.getMsg().contains("叶哥哥"))&&
                    !((simpleMsg.getMsg().equals("aye") || simpleMsg.getMsg().equals("Aye") || simpleMsg.getMsg().equals("阿叶")
                    || simpleMsg.getMsg().equals("小叶") || simpleMsg.getMsg().equals("叶受") || simpleMsg.getMsg().equals("叶哥哥")))) {
                flag = true;
            }
        }

        if (flag) {
            if (simpleMsg.getFromGroup() == 947657871L){
                if (simpleMsg.getMsg().length() <= 6)
                    flag = false;
            }
        }

        if (flag) {
            Statement stmt;
            try {
                stmt = getConnection().createStatement();
                String sql = "SELECT * FROM question where msg='"+ simpleMsg.getMsg()+"';";
                ResultSet rs = stmt.executeQuery(sql);
                boolean hasMSG = rs.next();
                rs.close();
                Date dNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                if (!hasMSG) {
                    sql =
                            "INSERT INTO question (msg,fromQQ,fromGroup,time) " + "VALUES ('" + simpleMsg.getMsg() + "', '" + simpleMsg.getFromClient() + "', '" + simpleMsg.getFromGroup() + "', '" + ft.format(dNow) + "' );";
                    stmt.executeUpdate(sql);
                    zibenbot.toPrivateMsg(2375985957L, "已添加数据集：" + simpleMsg.getMsg());
//                    replyMsg(simpleMsg, "[" + ft.format(dNow) + "][INFO] 本条对话已添加NLP待处理数据集");
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
