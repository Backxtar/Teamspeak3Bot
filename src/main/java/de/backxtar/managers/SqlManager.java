package de.backxtar.managers;

import de.backxtar.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlManager {
    private static final Logger logger = LoggerFactory.getLogger(SqlManager.class);
    private static Connection conn = null;

    public static void connect() throws ClassNotFoundException, SQLException {
        if (conn != null) disconnect();

        String jdbcDriver = "com.mysql.cj.jdbc.Driver";
        Class.forName(jdbcDriver);
        conn = DriverManager.getConnection("jdbc:mysql://" + Config.getConfigData().dbHost + ":3306/" + Config.getConfigData().dbName,
                Config.getConfigData().dbUser, Config.getConfigData().dbPassword);
        logger.info("Database connected.");

        createTables();
    }


    private static PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    public static void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
                logger.info("Database disconnected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() {
        return conn;
    }

    private static void createTables() {
        String[] sqlStmts = {
                "CREATE TABLE IF NOT EXISTS API_Keys(" +
                        "id bigint(20) AUTO_INCREMENT," +
                        "clientIdentity varchar(255)," +
                        "GW2_Key varchar(255)," +
                        "accountName varchar(255))",

                "CREATE TABLE IF NOT EXISTS Timers(" +
                        "id bigint(20) AUTO_INCREMENT," +
                        "clientIdentity varchar(255)," +
                        "timeStamp timestamp," +
                        "name varchar(255))"
        };
        List<PreparedStatement> stmts = new ArrayList<>();

        try {
            stmts.add(prepareStatement(sqlStmts[0]));
            stmts.add(prepareStatement(sqlStmts[1]));

            for (PreparedStatement stmt : stmts) stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(String table, String[] fields, Object[] values) throws SQLException {
        StringBuilder stmtString = new StringBuilder("INSERT INTO " + table + Arrays.toString(fields).replace("[", "(").replace("]", ")") + " VALUES(");
        for (Object ignored : values)
            stmtString.append("?,");
        stmtString = new StringBuilder(stmtString.substring(0, stmtString.length() - 1) + ") ");
        PreparedStatement stmt = prepareStatement(stmtString.toString());

        int i = 1;
        for (Object value : values) {
            if (value.getClass().getName().equals("java.lang.String"))
                stmt.setString(i++, (String) value);
            else stmt.setLong(i++, (Long) value);
        }
        stmt.execute();
    }

    public static void delete(String table, String where, Object[] values) throws SQLException {
        PreparedStatement stmt = prepareStatement("DELETE FROM " + table + " WHERE " + where);

        int i = 1;
        for (Object value : values) {
            if (value.getClass().getName().equals("java.lang.String"))
                stmt.setString(i++, (String) value);
            else stmt.setLong(i++, (Long) value);
        }
        stmt.execute();
    }

    public static void delete(String table) throws SQLException {
        PreparedStatement stmt = prepareStatement("DELETE FROM " + table);
        stmt.execute();
    }

    public static ResultSet select(String[] fields, String table) throws SQLException {
        PreparedStatement stmt = prepareStatement("SELECT " + Arrays.toString(fields).replace("[", "").replace("]", "") + " FROM " + table);
        return stmt.executeQuery();
    }

    public static ResultSet select(String field, String table) throws SQLException {
        PreparedStatement stmt = prepareStatement("SELECT " + field + " FROM " + table);
        return stmt.executeQuery();
    }

    public static ResultSet select(String[] fields, String table, String where, Object[] values) throws SQLException {
        PreparedStatement stmt = prepareStatement("SELECT " + Arrays.toString(fields).replace("[", "").replace("]", "") + " FROM " + table + " WHERE " + where);

        int i = 1;
        for (Object value : values) {
            if (value.getClass().getName().equals("java.lang.String"))
                stmt.setString(i++, (String) value);
            else stmt.setLong(i++, (Long) value);
        }
        return stmt.executeQuery();
    }

    public static ResultSet selectAll(String table, String where, Object[] values) throws NumberFormatException, SQLException {
        PreparedStatement stmt = prepareStatement("SELECT * FROM " + table + " WHERE " + where);

        int i = 1;
        for (Object value : values) {
            if (value.getClass().getName().equals("java.lang.String"))
                stmt.setString(i++, (String) value);
            else stmt.setLong(i++, (Long) value);
        }
        return stmt.executeQuery();
    }

    public static ResultSet selectAll(String table) throws SQLException {
        PreparedStatement stmt = prepareStatement("SELECT * FROM " + table);
        return stmt.executeQuery();
    }

    public static ResultSet selectAllOrderBy(String[] fields, String table, String order, String type) throws SQLException {
        PreparedStatement stmt = prepareStatement("SELECT " + Arrays.toString(fields).replace("[", "").replace("]", "") + " FROM " + table + " ORDER BY " + order + " " + type);
        return stmt.executeQuery();
    }

    public static void update(String[] fields, String table, String where, Object[] values) throws SQLException {
        PreparedStatement stmt = prepareStatement("UPDATE " + table + " SET " + Arrays.toString(fields).replace("[", "").replace("]", " = ?").replace(",", "= ? ,") + " WHERE " + where);

        int i = 1;
        for (Object value : values) {
            if (value.getClass().getName().equals("java.lang.String"))
                stmt.setString(i++, (String) value);
            else stmt.setLong(i++, (Long) value);
        }
        stmt.execute();
    }
}
