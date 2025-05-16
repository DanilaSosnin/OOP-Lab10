package com.example.lab10.listener;

import com.example.lab10.util.DBUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Application initialization started");

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            if (!isTableExists(conn)) {
                executeSQLScript(sce);
            }

        } catch (Exception e) {
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private boolean isTableExists(Connection conn) throws SQLException {
        ResultSet rs = null;
        try {
            DatabaseMetaData meta = conn.getMetaData();
            rs = meta.getTables(null, null, "tutors", new String[]{"TABLE"});
            return rs.next();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet: " + e.getMessage());
                }
            }
        }
    }

    private void executeSQLScript(ServletContextEvent sce) throws Exception {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             InputStream is = getClass().getClassLoader()
                     .getResourceAsStream("/sql/init.sql");
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("--")) continue;
                sb.append(line);
                if (line.trim().endsWith(";")) {
                    stmt.execute(sb.toString());
                    sb.setLength(0);
                }
            }
            System.out.println("Database tables created successfully");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Application shutdown");
    }
}