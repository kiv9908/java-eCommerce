package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection connection = null;
    private static final Properties properties = new Properties();

    static {
        try {
            // 파일 경로를 직접 지정
            FileInputStream fileInputStream = new FileInputStream("src/properties/db.properties");
            properties.load(fileInputStream);
            fileInputStream.close();

            // JDBC 드라이버 로드
            Class.forName(properties.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC 드라이버를 로드하는데 실패했습니다: " + e.getMessage());
            throw new RuntimeException("JDBC 드라이버 로드 실패", e);
        } catch (IOException e) {
            System.err.println("properties 파일을 로드하는데 실패했습니다: " + e.getMessage());
            throw new RuntimeException("properties 파일 로드 실패", e);
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // 로컬 데이터베이스에 연결
                connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
                );
//                System.out.println("로컬 데이터베이스에 연결되었습니다.");
            } catch (SQLException e) {
                System.err.println("데이터베이스 연결에 실패했습니다: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("데이터베이스 연결 실패", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("데이터베이스 연결 종료");
            } catch (SQLException e) {
                System.err.println("데이터베이스 연결 종료에 실패했습니다: " + e.getMessage());
            }
        }
    }
}