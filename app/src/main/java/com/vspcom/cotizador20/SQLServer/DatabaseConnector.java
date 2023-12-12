package com.vspcom.cotizador20.SQLServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String DB_URL = "jdbc:jtds:sqlserver://FACTURACIONPC\\\\MSSQLSERVER:1433/C:\\\\MyBusinessDatabase\\\\VSP2014.mdf";
    private static final String USER = "sa";
    private static final String PASS = "zorobabel";

    public static Connection connect() throws SQLException {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Connection Failed");
        }
    }
}
