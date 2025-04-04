package com.github.monadoeater.db.sqlite;
import com.github.monadoeater.website.fandom.Webpage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;



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
    public static void insertWebpage(Webpage webpage) {
        String sqliteUrl = "jdbc:sqlite:" + DB_PATH;
        String sqlQuery = "INSERT INTO webpage (url, html) VALUES (?, ?);";
        try (Connection conn = DriverManager.getConnection(sqliteUrl)) {
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setString(1, webpage.getUrl());
            statement.setBytes(2, webpage.getDocument().html().getBytes());
            statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Insert webpage URL and binary data to SQLite database.
    public static Document selectHtml(int id) {
        String sqliteUrl = "jdbc:sqlite:" + DB_PATH;
        byte[] htmlBlob = null;
        String sqlQuery = "SELECT html FROM webpage WHERE id = ?;";
        try (Connection conn = DriverManager.getConnection(sqliteUrl)) {
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            htmlBlob = resultSet.getBytes("html");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return blobToJsoupDocument(htmlBlob);
    }


    private static Document blobToJsoupDocument(byte[] blobData) {
        Document document = null;
        if (blobData != null) {
            String htmlContent = new String(blobData);
            document = Jsoup.parse(htmlContent);
        }
        return document;
    }
}
