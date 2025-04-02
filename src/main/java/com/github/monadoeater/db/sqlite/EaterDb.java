package com.github.monadoeater.db.sqlite;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class EaterDb {
    private static final Path DB_PATH = Paths.get("src", "main", "resources", "database", "db", "eater.db");
    private static final Path SCHEMA_PATH = Paths.get("src", "main", "resources", "database", "sql", "eater_schema.sql");

    // Connect to SQLite database and init schema.
    public static void connect() {
        String sqliteUrl = "jdbc:sqlite:" + DB_PATH;

        try (Connection conn = DriverManager.getConnection(sqliteUrl)) {
            String sqlQuery = Files.readString(SCHEMA_PATH, StandardCharsets.UTF_8);
            System.out.println("Connection to SQLite has been established.");
            PreparedStatement stmt = conn.prepareStatement(sqlQuery);
            stmt.execute();
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert webpage URL and binary data to SQLite database.
    public static void insertWebpage(String webpageUrl, byte[] html) {
        String sqliteUrl = "jdbc:sqlite:" + DB_PATH;
        String sqlQuery = "INSERT INTO webpage (url, html) VALUES (?, ?);";
        try (Connection conn = DriverManager.getConnection(sqliteUrl)) {
            PreparedStatement stmt = conn.prepareStatement(sqlQuery);
            stmt.setString(1, webpageUrl);
            stmt.setBytes(2, html);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
