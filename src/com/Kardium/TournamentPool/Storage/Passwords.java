package com.Kardium.TournamentPool.Storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Passwords {
    private static final File PASSWORD_FILE = new File("C:/TournamentPool/passwords.txt");
    private static Map<String, Integer> passwords;

    private Connection connection;

    public Passwords() {
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            connection = DriverManager.getConnection(
                    "jdbc:firebirdsql:kfishbowl3/3050:C:/Program Files/Fishbowl/database/data/TOURNAMENTPOOLDB.FDB",
                    "ksqlread", "ksqlread");
        } catch (Exception e) {
            connection = null;
        }
    }

    public boolean isExistingUser(String name) {
        try {
            String sqlRequest = "SELECT Name FROM PasswordTable\n" +
                    "WHERE Name='" + name + "';";
            ResultSet resultSet = executeRequest(sqlRequest);
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqle) {
            // deal with it
            return false;
        }
    }

    public boolean registerNewUser(String name, int password) throws AlreadyRegisteredUser{
        if (isExistingUser(name)) {
            try {
                String sqlRequest = "DELETE FROM PasswordTable WHERE Name=" + name;
                int ret = connection.createStatement().executeUpdate(sqlRequest);
            } catch (SQLException sqle) {

            }
            //throw new AlreadyRegisteredUser();
        } /*else {*/
            try {
                String sqlRequest = "INSERT INTO PasswordTable VALUES ('" + name + "', " + password + ")";
                return 1 == connection.createStatement().executeUpdate(sqlRequest);
            } catch (SQLException sqle) {
                return false;
            }
        /*}*/
    }

    public int getPassword(String name) throws NotRegisteredUserException{
        if (!isExistingUser(name )) {
            throw new NotRegisteredUserException();
        } else {
            try {
                String sqlRequest = "SELECT PasswordChosen FROM PasswordTable\n" +
                        "WHERE Name='" + name + "';";
                ResultSet resultSet = executeRequest(sqlRequest);
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    return -1;
                }

                } catch (SQLException sqle) {
                return -1;
            }
        }
    }

    private ResultSet executeRequest(String request) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(request);
    }

    public static void main(String [] args) throws Exception {
        Passwords p = new Passwords();
        p.isExistingUser("TempUser");
        p.registerNewUser("Yona", 789456123);
        int password = p.getPassword("Yona");
        if (789456123 ==password) {
            System.out.println("Everything works");
        } else {
            System.out.println("Nothing works");
        }
    }
}
