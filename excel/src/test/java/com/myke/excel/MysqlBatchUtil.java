package com.myke.excel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlBatchUtil {
    private String sql = "INSERT INTO test.t_user ( age, password, sex, username) VALUES (?, ?, ?, ?)";
    private String charset = "utf-8";
    private String connectStr = "jdbc:mysql://127.0.0.1:3306/test";
    private String username = "root";
    private String password = "root";

    /**
     * 1. 增加批量写的速度：
     * useServerPrepStmts=false     关闭服务器端编译，sql语句在客户端编译好再发送给服务器端.如果为true,sql会采用占位符方式发送到服务器端，在服务器端再组装sql语句
     * rewriteBatchedStatements=true 开启批量写功能
     * useCompression=true 压缩数据传输，优化客户端和MySQL服务器之间的通信性能。
     * <p>
     * 2. 增加读的速度：
     * useServerPrepStmts=true
     * cachePrepStmts=true
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     */
    private void doStore() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true&useCompression=true";
        Connection conn = DriverManager.getConnection(connectStr, username, password);
        conn.setAutoCommit(false); // 设置手动提交  
        int count = 0;
        PreparedStatement psts = conn.prepareStatement(sql);
        long begin = System.currentTimeMillis();
        for (int i = 1; i <= 1000000; i++) {
            psts.setString(1, String.valueOf(i));
            psts.setString(2, String.valueOf(i));
            psts.setString(3, String.valueOf(i));
            psts.setString(4, String.valueOf(i));
            // 加入批量处理
            psts.addBatch();
            count++;
        }
        psts.executeBatch(); // 执行批量处理  
        conn.commit();  // 提交  
        long end = System.currentTimeMillis();
        System.out.println("数量=" + count);
        System.out.println("运行时间=" + (end - begin) / 1000 + "秒");
        conn.close();
    }

    public static void main(String[] args) {
        try {
            new MysqlBatchUtil().doStore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}