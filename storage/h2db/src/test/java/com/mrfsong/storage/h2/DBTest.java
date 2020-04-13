package com.mrfsong.storage.h2;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

/**
 * <p>
 * rocksdb测试
 * </P>
 *
 * @Author songfei20
 * @Date 2020/4/13
 */
@Slf4j
public class DBTest {

    // 数据库连接URL，当前连接的是E:/H2目录下的db数据库(h2数据存储有两种模式,一种是存储在硬盘上,一种是存储在内存中)
    //jdbc:h2:mem:数据库名称
    private static final String JDBC_URL = "jdbc:h2:C:/H2/db";
    // 连接数据库时使用的用户名
    private static final String USER = "root";
    // 连接数据库时使用的密码
    private static final String PASSWORD = "";
    // 连接H2数据库时使用的驱动类，org.h2.Driver这个类是由H2数据库自己提供的，在H2数据库的jar包中可以找到
    private static final String DRIVER_CLASS = "org.h2.Driver";
    // 全局数据库连接
    private Connection conn;
    // 数据库操作接口
    private Statement stmt;

    public void connection() throws Exception {
        // 加载驱动
        Class.forName(DRIVER_CLASS);
        // 根据连接URL，用户名，密码，获取数据库连接
        conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public void statement() throws Exception {
        // 创建操作
        stmt = conn.createStatement();
    }

    public void createTable() throws Exception {
        // 先删
        stmt.execute("DROP TABLE IF EXISTS TEST_H2TABLE");
        // 再建
        stmt.execute("CREATE TABLE TEST_H2TABLE(id VARCHAR(50) PRIMARY KEY,title VARCHAR(50))");
    }

    // 插入数据
    public void insertData() throws Exception {
        stmt.executeUpdate("INSERT INTO TEST_H2TABLE VALUES('" + UUID.randomUUID() + "','我是一个标题')");
        stmt.executeUpdate("INSERT INTO TEST_H2TABLE VALUES('" + UUID.randomUUID() + "','渣渣码农')");
        stmt.executeUpdate("INSERT INTO TEST_H2TABLE VALUES('" + UUID.randomUUID() + "','头发在哪里')");
        stmt.executeUpdate("INSERT INTO TEST_H2TABLE VALUES('" + UUID.randomUUID() + "','我的头发呢?')");
    }

    // 查询数据
    public void queryData() throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT * FROM TEST_H2TABLE");
        while (rs.next()) {
            System.out.println(rs.getString("id") + "," + rs.getString("title"));
        }
    }

    // 释放资源和关闭连接
    public void close() throws Exception {
        stmt.close();
        conn.close();
    }

    public static void main(String[] args){
        DBTest h2 = new DBTest();
        try {
            h2.connection();
            h2.statement();
            h2.createTable();
            h2.insertData();
            h2.queryData();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("场面一度十分尴尬");
        }finally {
            try {
                h2.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("关都关不上了");
            }
        }

    }
}
